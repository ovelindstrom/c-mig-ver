#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script to fix encoding issues in Java source files.

This script specifically addresses Maven compilation errors like:
[ERROR] file.java:[line,col] error: unmappable character (0xE4) for encoding UTF-8
[ERROR] file.java:[line,col] error: unmappable character (0xF6) for encoding UTF-8
[ERROR] file.java:[line,col] error: unmappable character (0xE5) for encoding UTF-8

These errors occur when Java files contain Swedish characters encoded in ISO-8859-1
but Maven tries to compile them as UTF-8.

The script:
1. Detects files with problematic byte codes (0xE4=ä, 0xF6=ö, 0xE5=å, 0xC4=Ä, 0xD6=Ö)
2. Reads files in their original encoding (typically ISO-8859-1)
3. Converts and saves them as proper UTF-8
4. Handles corrupted Unicode escapes and double-encoding issues
5. Preserves Swedish characters correctly during the conversion

Usage examples:
- Process all Java files in a directory: python3 fix_encoding.py src/
- Process files from Maven error output: mvn compile 2>&1 | python3 fix_encoding.py --maven-errors
- Test the encoding fix logic: python3 fix_encoding.py --test
"""

import os
import re
import sys
from pathlib import Path
import unicodedata

def fix_encoding_issues(file_path):
    """
    Fix encoding issues in a Java file.
    Returns True if file was modified, False otherwise.
    """
    try:
        content = None
        original_encoding = None
        needs_conversion = False
        
        # First, read as bytes to check for problematic byte sequences
        with open(file_path, 'rb') as f:
            raw_bytes = f.read()
        
        # Check if file contains the problematic byte codes from Maven errors
        problematic_bytes = [0xE4, 0xF6, 0xE5, 0xC4, 0xD6]  # ä, ö, å, Ä, Ö in ISO-8859-1
        has_swedish_bytes = any(byte_val in raw_bytes for byte_val in problematic_bytes)
        
        # Try ISO-8859-1 first if we detected Swedish byte codes
        if has_swedish_bytes:
            try:
                content = raw_bytes.decode('iso-8859-1')
                original_encoding = 'iso-8859-1'
                # Check if this contains Swedish characters that need UTF-8 conversion
                swedish_chars = ['å', 'ä', 'ö', 'Å', 'Ä', 'Ö']
                if any(char in content for char in swedish_chars):
                    needs_conversion = True
            except UnicodeDecodeError:
                pass
        
        # Try UTF-8 if ISO-8859-1 didn't work or wasn't needed
        if not content:
            try:
                content = raw_bytes.decode('utf-8')
                original_encoding = 'utf-8'
                # Check if UTF-8 reading resulted in replacement characters
                if '�' in content:
                    needs_conversion = True
            except UnicodeDecodeError:
                pass
        
        # Try Windows-1252 as fallback
        if not content:
            try:
                content = raw_bytes.decode('cp1252')
                original_encoding = 'cp1252'
                needs_conversion = True
            except UnicodeDecodeError:
                pass
        
        # Last resort: force decode with error handling
        if not content:
            content = raw_bytes.decode('utf-8', errors='replace')
            original_encoding = 'utf-8-with-errors'
            needs_conversion = True
        
        if not content:
            return False
            
        original_content = content
        
        # Fix corrupted characters and encoding issues
        if needs_conversion or original_encoding != 'utf-8':
            content = fix_corrupted_characters(content)
        
        # Always convert to UTF-8 if not already UTF-8 or if we found issues
        file_was_modified = (content != original_content) or (original_encoding != 'utf-8' and has_swedish_bytes)
        
        if file_was_modified:
            # Write back as UTF-8
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Fixed: {file_path} (was {original_encoding}, Swedish chars: {has_swedish_bytes})")
            return True
        
        return False
        
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

def fix_corrupted_characters(text):
    """
    Fix corrupted character encodings for Swedish characters.
    Handles Unicode escape sequences and encoding mismatches.
    """
    if not text:
        return text
    
    result = text
    
    # Step 1: Fix Unicode escape sequences for Swedish characters
    unicode_mappings = {
        # Swedish characters as Unicode escapes
        r'\\u00e5': 'å',  # å (small a with ring above)
        r'\\u00c5': 'Å',  # Å (capital A with ring above)
        r'\\u00e4': 'ä',  # ä (small a with diaeresis)
        r'\\u00c4': 'Ä',  # Ä (capital A with diaeresis)
        r'\\u00f6': 'ö',  # ö (small o with diaeresis)
        r'\\u00d6': 'Ö',  # Ö (capital O with diaeresis)
        
        # Common corrupted patterns
        r'\\ufffd': '�',  # Replacement character - will be handled in next step
        r'\\uFFFD': '�',  # Replacement character (uppercase)
    }
    
    for pattern, replacement in unicode_mappings.items():
        result = re.sub(pattern, replacement, result, flags=re.IGNORECASE)
    
    # Step 2: Handle replacement characters and attempt to restore Swedish characters
    # This handles cases where the original character was corrupted during encoding
    result = fix_replacement_characters(result)
    
    # Step 3: Fix double-encoded characters (e.g., UTF-8 decoded as Latin-1)
    result = fix_double_encoding(result)
    
    # Step 4: Fix ISO-8859-1 character corruptions
    result = fix_iso88591_corruptions(result)
    
    # Step 5: Normalize Unicode characters
    result = unicodedata.normalize('NFC', result)
    
    return result

def fix_replacement_characters(text):
    """
    Attempt to fix replacement characters (�) by analyzing context.
    Uses heuristics to determine which Swedish character was likely intended.
    """
    if '�' not in text:
        return text
    
    result = text
    
    # Context-based replacements for Swedish characters
    # These patterns look at surrounding characters to make educated guesses
    context_patterns = [
        # Common Swedish letter patterns
        (r'f�r\b', 'för'),      # "för" (for/to)
        (r'\bf�r\b', 'för'),    # "för" at word boundary
        (r's�', 'så'),          # "så" (so)
        (r'�r\b', 'är'),        # "är" (is/are)
        (r'\b�r\b', 'är'),      # "är" as whole word
        (r'�ven\b', 'även'),    # "även" (also)
        (r'h�r\b', 'här'),      # "här" (here)
        (r'n�r\b', 'när'),      # "när" (when)
        (r'd�r\b', 'där'),      # "där" (there)
        (r'v�r\b', 'vår'),      # "vår" (our/spring)
        (r'k�n\b', 'kön'),      # "kön" (gender/queue)
        (r'h�g\b', 'hög'),      # "hög" (high)
        (r'r�d\b', 'röd'),      # "röd" (red)
        (r'gr�n\b', 'grön'),    # "grön" (green)
        (r'bl�\b', 'blå'),      # "blå" (blue)
        (r'st�r\b', 'står'),    # "står" (stands)
        (r'g�r\b', 'gör'),      # "gör" (does)
        (r'h�r\b', 'hör'),      # "hör" (hear)
        (r'k�r\b', 'kör'),      # "kör" (drives)
        (r'l�r\b', 'lär'),      # "lär" (learns)
        
        # Common endings and prefixes
        (r'�ndr', 'ändr'),      # "ändra" (change)
        (r'h�mt', 'hämt'),      # "hämta" (fetch)
        (r'k�ll', 'käll'),      # "källa" (source)
        (r'v�rd', 'värd'),      # "värde" (value)
        (r't�nk', 'tänk'),      # "tänka" (think)
        (r'�g', 'äg'),          # "äga" (own)
        (r'l�s', 'läs'),        # "läsa" (read)
        (r'h�ll', 'håll'),      # "hålla" (hold)
        (r'f�ll', 'föll'),      # "föll" (fell)
        (r't�ll', 'täll'),      # Less common but possible
        
        # Capitalized versions
        (r'F�r\b', 'För'),      # "För"
        (r'�r\b', 'Är'),        # "Är"
        (r'�ven\b', 'Även'),    # "Även"
    ]
    
    for pattern, replacement in context_patterns:
        result = re.sub(pattern, replacement, result, flags=re.IGNORECASE)
    
    # For remaining isolated � characters, make educated guesses based on position
    # In Swedish, 'å' is the most common of å/ä/ö, so default to that
    result = result.replace('�', 'å')
    
    return result

def fix_double_encoding(text):
    """
    Fix double-encoded Swedish characters.
    This handles cases where UTF-8 bytes were incorrectly interpreted as Latin-1.
    """
    # Common double-encoding patterns for Swedish characters
    double_encoding_fixes = {
        # UTF-8 bytes for Swedish chars interpreted as Latin-1
        'Ã¥': 'å',  # UTF-8 bytes C3 A5 for å interpreted as Latin-1
        'Ã\x85': 'Å',  # UTF-8 bytes C3 85 for Å
        'Ã¤': 'ä',  # UTF-8 bytes C3 A4 for ä
        'Ã\x84': 'Ä',  # UTF-8 bytes C3 84 for Ä
        'Ã¶': 'ö',  # UTF-8 bytes C3 B6 for ö
        'Ã\x96': 'Ö',  # UTF-8 bytes C3 96 for Ö
        
        # Windows-1252 to UTF-8 issues
        'Ã': 'å',   # Sometimes just the first byte
        'Ã¤': 'ä',
        'Ã¶': 'ö',
        
        # Other common corruptions
        'Ã¥': 'å',
        'Ã\xa5': 'å',
        'Ã\xa4': 'ä',
        'Ã\xb6': 'ö',
    }
    
    result = text
    for corrupted, correct in double_encoding_fixes.items():
        result = result.replace(corrupted, correct)
    
    return result

def fix_iso88591_corruptions(text):
    """
    Fix common ISO-8859-1 character corruptions where Swedish characters
    were replaced with wrong Latin-1 characters.
    
    This function handles characters that are valid Swedish characters in ISO-8859-1
    but need to be preserved correctly when converting to UTF-8.
    """
    result = text
    
    # The problematic characters from Maven errors are actually CORRECT Swedish characters
    # in ISO-8859-1, they just need proper UTF-8 encoding. No character replacement needed.
    # 0xE4 = ä, 0xF6 = ö, 0xE5 = å, 0xC4 = Ä, 0xD6 = Ö
    
    # However, we still handle edge cases where characters might be genuinely corrupted
    iso_corruptions = {
        # Only handle cases where we have obviously wrong characters
        # that are unlikely to appear in Swedish text in Java comments/strings
        'Í': 'ä',  # 0xCD is very unlikely in Swedish context  
        'È': 'ö',  # 0xC8 is very unlikely in Swedish context
        'Æ': 'ä',  # 0xC6 (ligature) sometimes corrupts to ä
        'Ø': 'ö',  # 0xD8 (Danish/Norwegian ø) might need conversion to Swedish ö
        'Ì': 'Ä',  # Similar corruption patterns for capitals
        'Ç': 'Ö',  # 0xC7 might be corrupted Ö
    }
    
    # Only replace these characters if they appear in Swedish-like contexts
    for corrupted, correct in iso_corruptions.items():
        # Use regex to be more careful about context
        # Only replace if it's part of a word that looks Swedish
        import re
        
        # Replace in Swedish names and common words - be conservative
        patterns = [
            (rf'\b([A-Za-z]*){re.escape(corrupted)}([a-z]*)\b', lambda m: m.group(1) + correct + m.group(2)),
            # In middle of words (most common case)
            (rf'([a-z]+){re.escape(corrupted)}([a-z]+)', lambda m: m.group(1) + correct + m.group(2)),
        ]
        
        for pattern, replacement in patterns:
            result = re.sub(pattern, replacement, result)
    
    return result

def detect_likely_swedish_char(context_before, context_after):
    """
    Analyze context around a corrupted character to guess which Swedish character it should be.
    Returns the most likely Swedish character (å, ä, or ö) based on common patterns.
    """
    context = (context_before + context_after).lower()
    
    # Common patterns that suggest specific Swedish characters
    if any(pattern in context for pattern in ['för', 'går', 'står', 'hår', 'får', 'klar']):
        return 'å'
    elif any(pattern in context for pattern in ['här', 'där', 'när', 'ser', 'ter', 'ker']):
        return 'ä'  
    elif any(pattern in context for pattern in ['hör', 'gör', 'kör', 'för', 'röd', 'grön']):
        return 'ö'
    
    # Default to å as it's most common
    return 'å'

def check_for_maven_error_bytes(file_path):
    """
    Check if a file contains the specific byte codes that cause Maven compilation errors.
    Returns a dict with information about problematic bytes found.
    """
    try:
        with open(file_path, 'rb') as f:
            raw_bytes = f.read()
        
        # Maven error byte codes for Swedish characters in ISO-8859-1
        error_bytes = {
            0xE4: 'ä (small a with diaeresis)',
            0xF6: 'ö (small o with diaeresis)', 
            0xE5: 'å (small a with ring above)',
            0xC4: 'Ä (capital A with diaeresis)',
            0xD6: 'Ö (capital O with diaeresis)'
        }
        
        found_issues = {}
        for byte_code, description in error_bytes.items():
            if byte_code in raw_bytes:
                # Find positions of this byte
                positions = []
                for i, b in enumerate(raw_bytes):
                    if b == byte_code:
                        positions.append(i)
                found_issues[byte_code] = {
                    'description': description,
                    'count': len(positions),
                    'positions': positions[:10]  # Limit to first 10 occurrences
                }
        
        return found_issues
    except Exception as e:
        print(f"Error checking {file_path}: {e}")
        return {}

def process_java_files(root_dir):
    """
    Process all .java files in the given directory and subdirectories.
    """
    root_path = Path(root_dir)
    java_files = list(root_path.rglob("*.java"))
    
    print(f"Found {len(java_files)} Java files to process...")
    
    modified_count = 0
    files_with_maven_errors = 0
    
    for java_file in java_files:
        # Check for Maven error bytes before processing
        maven_issues = check_for_maven_error_bytes(java_file)
        
        if maven_issues:
            files_with_maven_errors += 1
            print(f"Processing: {java_file} (Maven error bytes detected)")
            for byte_code, info in maven_issues.items():
                print(f"  - Found {info['count']} instances of 0x{byte_code:02X} ({info['description']})")
        else:
            print(f"Processing: {java_file}")
            
        if fix_encoding_issues(java_file):
            modified_count += 1
    
    print(f"\nSummary:")
    print(f"- Processed {len(java_files)} Java files")
    print(f"- Files with Maven error bytes: {files_with_maven_errors}")
    print(f"- Modified {modified_count} files")
    return modified_count

def test_encoding_fix(test_string):
    """
    Test function to verify the encoding fix logic.
    """
    print(f"Testing: {repr(test_string)}")
    fixed = fix_corrupted_characters(test_string)
    print(f"Fixed:   {repr(fixed)}")
    print()

def process_maven_error_files(error_log_text):
    """
    Parse Maven error output and process only the files mentioned in the errors.
    """
    import re
    
    # Extract file paths from Maven error messages
    error_pattern = r'\[ERROR\] ([^:]+\.java):\[\d+,\d+\] error: unmappable character'
    error_files = set()
    
    for line in error_log_text.split('\n'):
        match = re.search(error_pattern, line)
        if match:
            file_path = match.group(1)
            error_files.add(file_path)
    
    print(f"Found {len(error_files)} files with Maven encoding errors:")
    
    modified_count = 0
    for file_path in sorted(error_files):
        if os.path.exists(file_path):
            print(f"\nProcessing Maven error file: {file_path}")
            maven_issues = check_for_maven_error_bytes(file_path)
            
            if maven_issues:
                for byte_code, info in maven_issues.items():
                    print(f"  - Found {info['count']} instances of 0x{byte_code:02X} ({info['description']})")
            
            if fix_encoding_issues(file_path):
                modified_count += 1
        else:
            print(f"Warning: File not found: {file_path}")
    
    print(f"\nSummary:")
    print(f"- Processed {len(error_files)} files from Maven errors")
    print(f"- Modified {modified_count} files")
    return modified_count

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 fix_encoding.py <source_directory> [--test] [--maven-errors]")
        print("Example: python3 fix_encoding.py src/")
        print("Test mode: python3 fix_encoding.py --test")
        print("Maven errors: python3 fix_encoding.py --maven-errors < maven_output.txt")
        print("             (reads Maven error output from stdin)")
        sys.exit(1)
    
    if sys.argv[1] == "--test":
        # Test mode - run some test cases
        print("Running encoding fix tests...\n")
        test_cases = [
            "f\\ufffdr",  # Should become "för"
            "\\ufffd\\ufffdr",  # Should become "åär" or similar
            "anv\\ufffdndas",  # Should become "användas"
            "Ã¥",  # Double-encoded å
            "Ã¤r",  # Double-encoded är
            "h\\ufffdr",  # Should become "här"
            "k\\ufffdlla",  # Should become "källa"
        ]
        
        for test in test_cases:
            test_encoding_fix(test)
    elif sys.argv[1] == "--maven-errors":
        # Process files from Maven error output
        print("Reading Maven error output from stdin...")
        maven_output = sys.stdin.read()
        process_maven_error_files(maven_output)
    else:
        source_dir = sys.argv[1]
        if not os.path.exists(source_dir):
            print(f"Directory {source_dir} does not exist")
            sys.exit(1)
        
        process_java_files(source_dir)