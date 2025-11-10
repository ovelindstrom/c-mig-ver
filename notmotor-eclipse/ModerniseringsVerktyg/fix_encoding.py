#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script to fix encoding issues in Java source files.
Handles corrupted Unicode escapes and encoding mismatches for Swedish characters.
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
        # Try reading with different encodings
        content = None
        original_encoding = None
        
        # Try different encodings and check for replacement characters
        content = None
        original_encoding = None
        
        # Try ISO-8859-1 first (most common for Swedish legacy files)
        try:
            with open(file_path, 'r', encoding='iso-8859-1') as f:
                content = f.read()
                original_encoding = 'iso-8859-1'
                # Check if this looks like it needs conversion to UTF-8
                # (has Swedish characters but might be misinterpreted)
        except UnicodeDecodeError:
            pass
            
        # Try UTF-8 if ISO-8859-1 didn't work or if we want to double-check
        if not content:
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    utf8_content = f.read()
                    # Check if UTF-8 reading resulted in replacement characters
                    if '�' not in utf8_content:
                        content = utf8_content
                        original_encoding = 'utf-8'
                    elif not content:  # Fall back if no ISO content
                        content = utf8_content
                        original_encoding = 'utf-8-with-replacements'
            except UnicodeDecodeError:
                pass
        
        # Try Windows-1252 as last resort
        if not content:
            try:
                with open(file_path, 'r', encoding='cp1252') as f:
                    content = f.read()
                    original_encoding = 'cp1252'
            except UnicodeDecodeError:
                # Absolute last resort: read as bytes and decode with errors='replace'
                with open(file_path, 'rb') as f:
                    raw_content = f.read()
                    content = raw_content.decode('utf-8', errors='replace')
                    original_encoding = 'utf-8-with-errors'
        
        if not content:
            return False
            
        original_content = content
        
        # Fix corrupted characters and encoding issues
        content = fix_corrupted_characters(content)
        
        # Check if file was modified
        if content != original_content:
            # Write back as UTF-8
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Fixed: {file_path} (was {original_encoding})")
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
    """
    # These are specific byte-level corruptions observed in the files
    iso_corruptions = {
        # Character code 0xD6 (Ö) wrongly used instead of 0xE5 (å)  
        'Ö': 'å',  # This is the specific case we saw in hexdump
        
        # Other potential corruptions - characters that are unlikely in Swedish names/text
        'Í': 'ä',  # 0xCD might be used instead of 0xE4
        'È': 'ö',  # 0xC8 might be used instead of 0xF6
        'Æ': 'ä',  # 0xC6 sometimes corrupts to ä
        'Ø': 'ö',  # 0xD8 (Danish/Norwegian ø) might corrupt to Swedish ö
        
        # Capital versions
        'Ì': 'Ä',  # Similar corruption patterns for capitals
        'Ç': 'Ö',  # 0xC7 might be used instead of 0xD6
    }
    
    result = text
    
    # Only replace these characters if they appear in Swedish-like contexts
    for corrupted, correct in iso_corruptions.items():
        # Use regex to be more careful about context
        # Only replace if it's part of a word that looks Swedish
        import re
        
        # Replace in Swedish names and common words
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

def process_java_files(root_dir):
    """
    Process all .java files in the given directory and subdirectories.
    """
    root_path = Path(root_dir)
    java_files = list(root_path.rglob("*.java"))
    
    print(f"Found {len(java_files)} Java files to process...")
    
    modified_count = 0
    total_fixes = 0
    
    for java_file in java_files:
        print(f"Processing: {java_file}")
        if fix_encoding_issues(java_file):
            modified_count += 1
    
    print(f"\nSummary:")
    print(f"- Processed {len(java_files)} Java files")
    print(f"- Modified {modified_count} files")
    return modified_count

def parse_maven_errors(error_lines):
    """
    Parse Maven compilation error output to extract file paths with encoding errors.
    
    Maven error format example:
    [ERROR] /path/to/File.java:[line,column] unmappable character (0xE5) for encoding UTF-8
    
    Returns a set of unique file paths that have encoding errors.
    """
    file_paths = set()
    
    for line in error_lines:
        # Match Maven error pattern for encoding issues
        # Pattern: [ERROR] /path/to/file.java:[line,col] unmappable character
        match = re.search(r'\[ERROR\]\s+([^:]+\.java):\[\d+,\d+\].*unmappable character', line)
        if match:
            file_path = match.group(1)
            file_paths.add(file_path)
        
        # Alternative pattern without [ERROR] prefix
        match = re.search(r'((?:/[^/\s:]+)+\.java):\[\d+,\d+\].*unmappable character', line)
        if match and match.group(1) not in file_paths:
            file_path = match.group(1)
            file_paths.add(file_path)
    
    return file_paths

def process_maven_errors():
    """
    Read Maven error output from stdin and fix encoding issues in affected files.
    This is called when --maven-errors flag is used.
    """
    print("Reading Maven error output from stdin...")
    
    # Read all lines from stdin
    error_lines = sys.stdin.readlines()
    
    if not error_lines:
        print("No input received from stdin.")
        return 0
    
    print(f"Received {len(error_lines)} lines of Maven output")
    
    # Parse file paths from Maven errors
    file_paths = parse_maven_errors(error_lines)
    
    if not file_paths:
        print("No encoding errors found in Maven output.")
        print("Running general encoding fix on current directory...")
        return process_java_files(".")
    
    print(f"\nFound {len(file_paths)} files with encoding errors:")
    for file_path in sorted(file_paths):
        print(f"  - {file_path}")
    
    print("\nFixing encoding issues...")
    modified_count = 0
    
    for file_path in sorted(file_paths):
        if os.path.exists(file_path):
            if fix_encoding_issues(file_path):
                modified_count += 1
        else:
            print(f"Warning: File not found: {file_path}")
    
    print(f"\nSummary:")
    print(f"- Found {len(file_paths)} files with Maven encoding errors")
    print(f"- Fixed {modified_count} files")
    
    return modified_count

def test_encoding_fix(test_string):
    """
    Test function to verify the encoding fix logic.
    """
    print(f"Testing: {repr(test_string)}")
    fixed = fix_corrupted_characters(test_string)
    print(f"Fixed:   {repr(fixed)}")
    print()

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 fix_encoding.py <source_directory> [--test]")
        print("       python3 fix_encoding.py --maven-errors  (reads Maven errors from stdin)")
        print("Example: python3 fix_encoding.py src/")
        print("         mvn compile 2>&1 | python3 fix_encoding.py --maven-errors")
        print("Test mode: python3 fix_encoding.py --test")
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
        # Maven error mode - read from stdin
        process_maven_errors()
    else:
        source_dir = sys.argv[1]
        if not os.path.exists(source_dir):
            print(f"Directory {source_dir} does not exist")
            sys.exit(1)
        
        process_java_files(source_dir)