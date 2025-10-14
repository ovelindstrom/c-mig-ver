#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script to convert project structure to standard Maven directory layout.
Moves src/ -> src/main/java/ and test/ -> src/test/java/
"""

import os
import shutil
import sys
from pathlib import Path

def create_maven_structure(project_root):
    """
    Create the standard Maven directory structure.
    """
    maven_dirs = [
        'src/main/java',
        'src/main/resources',
        'src/test/java',
        'src/test/resources'
    ]
    
    for dir_path in maven_dirs:
        full_path = os.path.join(project_root, dir_path)
        os.makedirs(full_path, exist_ok=True)
        print(f"Created directory: {dir_path}")

def move_java_files(source_dir, target_dir, description):
    """
    Move Java files from source to target directory, preserving package structure.
    """
    if not os.path.exists(source_dir):
        print(f"Source directory {source_dir} does not exist, skipping {description}")
        return 0
    
    moved_count = 0
    
    # Walk through all files in source directory
    for root, dirs, files in os.walk(source_dir):
        for file in files:
            if file.endswith('.java'):
                source_file = os.path.join(root, file)
                # Calculate relative path from source_dir
                rel_path = os.path.relpath(source_file, source_dir)
                target_file = os.path.join(target_dir, rel_path)
                
                # Create target directory if it doesn't exist
                target_file_dir = os.path.dirname(target_file)
                os.makedirs(target_file_dir, exist_ok=True)
                
                # Move the file
                shutil.move(source_file, target_file)
                print(f"Moved: {rel_path} -> {os.path.join(os.path.basename(target_dir), rel_path)}")
                moved_count += 1
    
    return moved_count

def move_resources(source_dir, target_dir, description):
    """
    Move non-Java files (resources) from source to target directory.
    """
    if not os.path.exists(source_dir):
        print(f"Source directory {source_dir} does not exist, skipping {description}")
        return 0
    
    moved_count = 0
    
    # Walk through all files in source directory
    for root, dirs, files in os.walk(source_dir):
        for file in files:
            if not file.endswith('.java'):
                source_file = os.path.join(root, file)
                # Calculate relative path from source_dir
                rel_path = os.path.relpath(source_file, source_dir)
                target_file = os.path.join(target_dir, rel_path)
                
                # Create target directory if it doesn't exist
                target_file_dir = os.path.dirname(target_file)
                os.makedirs(target_file_dir, exist_ok=True)
                
                # Move the file
                shutil.move(source_file, target_file)
                print(f"Moved resource: {rel_path} -> {os.path.join(os.path.basename(target_dir), rel_path)}")
                moved_count += 1
    
    return moved_count

def cleanup_empty_directories(directory):
    """
    Remove empty directories recursively.
    """
    if not os.path.exists(directory):
        return
    
    removed_count = 0
    # Walk bottom-up to remove empty directories
    for root, dirs, files in os.walk(directory, topdown=False):
        for dir_name in dirs:
            dir_path = os.path.join(root, dir_name)
            try:
                if not os.listdir(dir_path):  # Directory is empty
                    os.rmdir(dir_path)
                    print(f"Removed empty directory: {os.path.relpath(dir_path, directory)}")
                    removed_count += 1
            except OSError:
                pass  # Directory not empty or other error
    
    # Try to remove the root directory if it's empty
    try:
        if not os.listdir(directory):
            os.rmdir(directory)
            print(f"Removed empty root directory: {os.path.basename(directory)}")
            removed_count += 1
    except OSError:
        pass
    
    return removed_count

def backup_structure(project_root):
    """
    Create a backup of the current structure before conversion.
    """
    backup_dir = os.path.join(project_root, 'backup_original_structure')
    
    if os.path.exists(backup_dir):
        print(f"Backup directory already exists: {backup_dir}")
        return False
    
    # Create backup directory
    os.makedirs(backup_dir)
    
    # Copy src directory if it exists
    src_dir = os.path.join(project_root, 'src')
    if os.path.exists(src_dir):
        shutil.copytree(src_dir, os.path.join(backup_dir, 'src'))
        print("Backed up: src/ -> backup_original_structure/src/")
    
    # Copy test directory if it exists
    test_dir = os.path.join(project_root, 'test')
    if os.path.exists(test_dir):
        shutil.copytree(test_dir, os.path.join(backup_dir, 'test'))
        print("Backed up: test/ -> backup_original_structure/test/")
    
    return True

def convert_to_maven_structure(project_root, create_backup=True):
    """
    Convert project to standard Maven directory structure.
    """
    project_root = os.path.abspath(project_root)
    print(f"Converting project structure in: {project_root}")
    print("=" * 60)
    
    # Create backup if requested
    if create_backup:
        print("Creating backup of original structure...")
        backup_created = backup_structure(project_root)
        if backup_created:
            print("✓ Backup created successfully")
        print()
    
    # Create Maven directory structure
    print("Creating Maven directory structure...")
    create_maven_structure(project_root)
    print()
    
    # Define source and target directories
    old_src_dir = os.path.join(project_root, 'src')
    old_test_dir = os.path.join(project_root, 'test')
    
    # Create temporary directories for the move
    temp_main_java = os.path.join(project_root, 'temp_main_java')
    temp_main_resources = os.path.join(project_root, 'temp_main_resources')
    temp_test_java = os.path.join(project_root, 'temp_test_java')
    temp_test_resources = os.path.join(project_root, 'temp_test_resources')
    
    try:
        # Move main source files to temporary directories
        print("Moving main source files...")
        os.makedirs(temp_main_java, exist_ok=True)
        os.makedirs(temp_main_resources, exist_ok=True)
        
        main_java_count = move_java_files(old_src_dir, temp_main_java, "main Java files")
        main_resource_count = move_resources(old_src_dir, temp_main_resources, "main resources")
        
        print(f"✓ Moved {main_java_count} Java files and {main_resource_count} resource files from src/")
        print()
        
        # Move test files to temporary directories
        print("Moving test files...")
        os.makedirs(temp_test_java, exist_ok=True)
        os.makedirs(temp_test_resources, exist_ok=True)
        
        test_java_count = move_java_files(old_test_dir, temp_test_java, "test Java files")
        test_resource_count = move_resources(old_test_dir, temp_test_resources, "test resources")
        
        print(f"✓ Moved {test_java_count} test Java files and {test_resource_count} test resource files from test/")
        print()
        
        # Clean up empty directories
        print("Cleaning up empty directories...")
        cleanup_empty_directories(old_src_dir)
        cleanup_empty_directories(old_test_dir)
        print()
        
        # Move from temporary directories to final Maven structure
        print("Moving to final Maven structure...")
        
        # Move temp directories to final locations
        if os.path.exists(temp_main_java) and os.listdir(temp_main_java):
            target_main_java = os.path.join(project_root, 'src', 'main', 'java')
            shutil.rmtree(target_main_java, ignore_errors=True)  # Remove empty dir first
            shutil.move(temp_main_java, target_main_java)
            print(f"✓ Moved main Java files to src/main/java/")
        
        if os.path.exists(temp_main_resources) and os.listdir(temp_main_resources):
            target_main_resources = os.path.join(project_root, 'src', 'main', 'resources')
            shutil.rmtree(target_main_resources, ignore_errors=True)
            shutil.move(temp_main_resources, target_main_resources)
            print(f"✓ Moved main resources to src/main/resources/")
        
        if os.path.exists(temp_test_java) and os.listdir(temp_test_java):
            target_test_java = os.path.join(project_root, 'src', 'test', 'java')
            shutil.rmtree(target_test_java, ignore_errors=True)
            shutil.move(temp_test_java, target_test_java)
            print(f"✓ Moved test Java files to src/test/java/")
        
        if os.path.exists(temp_test_resources) and os.listdir(temp_test_resources):
            target_test_resources = os.path.join(project_root, 'src', 'test', 'resources')
            shutil.rmtree(target_test_resources, ignore_errors=True)
            shutil.move(temp_test_resources, target_test_resources)
            print(f"✓ Moved test resources to src/test/resources/")
        
        print()
        print("=" * 60)
        print("✓ SUCCESS: Project structure converted to Maven standard layout!")
        print()
        print("Summary:")
        print(f"  - Main Java files: {main_java_count} -> src/main/java/")
        print(f"  - Main resources: {main_resource_count} -> src/main/resources/")
        print(f"  - Test Java files: {test_java_count} -> src/test/java/")
        print(f"  - Test resources: {test_resource_count} -> src/test/resources/")
        
        if create_backup:
            print(f"  - Original structure backed up in: backup_original_structure/")
        
    except Exception as e:
        print(f"❌ ERROR during conversion: {e}")
        print("Attempting to clean up temporary directories...")
        
        # Clean up temporary directories on error
        for temp_dir in [temp_main_java, temp_main_resources, temp_test_java, temp_test_resources]:
            if os.path.exists(temp_dir):
                shutil.rmtree(temp_dir, ignore_errors=True)
        
        raise
    
    finally:
        # Clean up any remaining temporary directories
        for temp_dir in [temp_main_java, temp_main_resources, temp_test_java, temp_test_resources]:
            if os.path.exists(temp_dir):
                shutil.rmtree(temp_dir, ignore_errors=True)

def main():
    if len(sys.argv) < 2:
        print("Usage: python3 maven_structure_converter.py <modules_parent_directory> [--no-backup]")
        print("Example: python3 maven_structure_converter.py ./modules")
        print("Example: python3 maven_structure_converter.py /path/to/modules --no-backup")
        sys.exit(1)

    parent_dir = sys.argv[1]
    create_backup = "--no-backup" not in sys.argv

    if not os.path.exists(parent_dir):
        print(f"❌ ERROR: Directory {parent_dir} does not exist")
        sys.exit(1)

    if not os.path.isdir(parent_dir):
        print(f"❌ ERROR: {parent_dir} is not a directory")
        sys.exit(1)

    # Iterate over subdirectories (modules)
    subdirs = [d for d in os.listdir(parent_dir) if os.path.isdir(os.path.join(parent_dir, d))]
    if not subdirs:
        print(f"No sub-modules found in {parent_dir}")
        sys.exit(0)

    for subdir in subdirs:
        module_path = os.path.join(parent_dir, subdir)
        print(f"\n=== Processing module: {subdir} ===")
        try:
            convert_to_maven_structure(module_path, create_backup)
        except Exception as e:
            print(f"❌ FATAL ERROR in module {subdir}: {e}")

if __name__ == "__main__":
    main()