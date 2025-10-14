#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script to convert project structure to standard Maven directory layout.

Handles various Eclipse project layouts:
- Standard: src/ -> src/main/java/ and test/ -> src/test/java/
- Custom: JavaSource/ -> src/main/java/ and test/ -> src/test/java/
- Web: WebContent/ -> src/main/webapp/ (only if WebContent exists)

Supports Maven projects with custom sourceDirectory configurations:
- <sourceDirectory>${basedir}/JavaSource</sourceDirectory>
- <testSourceDirectory>${basedir}/test</testSourceDirectory>

The script automatically detects the source directory structure and converts
to standard Maven layout while preserving package hierarchies.
"""

import os
import shutil
import sys
from pathlib import Path

def create_maven_structure(project_root, has_webcontent=False):
    """
    Create the standard Maven directory structure.
    """
    maven_dirs = [
        'src/main/java',
        'src/main/resources',
        'src/test/java',
        'src/test/resources'
    ]
    
    # Add webapp directory only if WebContent exists
    if has_webcontent:
        maven_dirs.append('src/main/webapp')
    
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

def move_webcontent(source_dir, target_dir, description):
    """
    Move entire WebContent directory to webapp directory, preserving structure.
    """
    if not os.path.exists(source_dir):
        print(f"Source directory {source_dir} does not exist, skipping {description}")
        return 0
    
    moved_count = 0
    
    # Walk through all files in WebContent directory
    for root, dirs, files in os.walk(source_dir):
        for file in files:
            source_file = os.path.join(root, file)
            # Calculate relative path from source_dir
            rel_path = os.path.relpath(source_file, source_dir)
            target_file = os.path.join(target_dir, rel_path)
            
            # Create target directory if it doesn't exist
            target_file_dir = os.path.dirname(target_file)
            os.makedirs(target_file_dir, exist_ok=True)
            
            # Move the file
            shutil.move(source_file, target_file)
            print(f"Moved webapp file: {rel_path} -> webapp/{rel_path}")
            moved_count += 1
    
    return moved_count

def move_ejb_module(source_dir, java_target_dir, resources_target_dir, description):
    """
    Move EJB module directory to Maven structure:
    - Java files go to src/main/java/
    - META-INF and other resources go to src/main/resources/
    """
    if not os.path.exists(source_dir):
        print(f"Source directory {source_dir} does not exist, skipping {description}")
        return 0, 0
    
    java_count = 0
    resource_count = 0
    
    # Walk through all files in ejbModule directory
    for root, dirs, files in os.walk(source_dir):
        for file in files:
            source_file = os.path.join(root, file)
            # Calculate relative path from source_dir
            rel_path = os.path.relpath(source_file, source_dir)
            
            if file.endswith('.java'):
                # Move Java files to src/main/java
                target_file = os.path.join(java_target_dir, rel_path)
                target_file_dir = os.path.dirname(target_file)
                os.makedirs(target_file_dir, exist_ok=True)
                
                shutil.move(source_file, target_file)
                print(f"Moved EJB Java file: {rel_path} -> src/main/java/{rel_path}")
                java_count += 1
            else:
                # Move resources (including META-INF) to src/main/resources
                target_file = os.path.join(resources_target_dir, rel_path)
                target_file_dir = os.path.dirname(target_file)
                os.makedirs(target_file_dir, exist_ok=True)
                
                shutil.move(source_file, target_file)
                print(f"Moved EJB resource: {rel_path} -> src/main/resources/{rel_path}")
                resource_count += 1
    
    return java_count, resource_count

def detect_source_directories(project_root):
    """
    Detect source directories in the project, handling both standard and custom layouts.
    Returns a dictionary with detected directories.
    """
    directories = {
        'main_source': None,
        'test_source': None,
        'webcontent': None,
        'ejb_module': None
    }
    
    # Check for EJB module directory first (Eclipse EJB projects)
    ejb_module_dir = os.path.join(project_root, 'ejbModule')
    if os.path.exists(ejb_module_dir):
        directories['ejb_module'] = ejb_module_dir
        print(f"Detected EJB module directory: ejbModule")
    
    # Check for custom source directories (JavaSource, etc.)
    potential_main_dirs = ['JavaSource', 'src']
    for dir_name in potential_main_dirs:
        dir_path = os.path.join(project_root, dir_name)
        if os.path.exists(dir_path):
            directories['main_source'] = dir_path
            print(f"Detected main source directory: {dir_name}")
            break
    
    # Check for test directory
    test_dir = os.path.join(project_root, 'test')
    if os.path.exists(test_dir):
        directories['test_source'] = test_dir
        print(f"Detected test source directory: test")
    
    # Check for WebContent directory
    webcontent_dir = os.path.join(project_root, 'WebContent')
    if os.path.exists(webcontent_dir):
        directories['webcontent'] = webcontent_dir
        print(f"Detected web content directory: WebContent")
    
    return directories

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

def backup_structure(project_root, directories):
    """
    Create a backup of the current structure before conversion.
    """
    backup_dir = os.path.join(project_root, 'backup_original_structure')
    
    if os.path.exists(backup_dir):
        print(f"Backup directory already exists: {backup_dir}")
        return False
    
    # Create backup directory
    os.makedirs(backup_dir)
    
    # Copy main source directory if it exists
    if directories['main_source']:
        source_name = os.path.basename(directories['main_source'])
        shutil.copytree(directories['main_source'], os.path.join(backup_dir, source_name))
        print(f"Backed up: {source_name}/ -> backup_original_structure/{source_name}/")
    
    # Copy EJB module directory if it exists
    if directories['ejb_module']:
        shutil.copytree(directories['ejb_module'], os.path.join(backup_dir, 'ejbModule'))
        print("Backed up: ejbModule/ -> backup_original_structure/ejbModule/")
    
    # Copy test directory if it exists
    if directories['test_source']:
        shutil.copytree(directories['test_source'], os.path.join(backup_dir, 'test'))
        print("Backed up: test/ -> backup_original_structure/test/")
    
    # Copy WebContent directory if it exists
    if directories['webcontent']:
        shutil.copytree(directories['webcontent'], os.path.join(backup_dir, 'WebContent'))
        print("Backed up: WebContent/ -> backup_original_structure/WebContent/")
    
    return True

def convert_to_maven_structure(project_root, create_backup=True):
    """
    Convert project to standard Maven directory structure.
    """
    project_root = os.path.abspath(project_root)
    print(f"Converting project structure in: {project_root}")
    print("=" * 60)
    
    # Detect existing source directories
    print("Detecting source directories...")
    directories = detect_source_directories(project_root)
    print()
    
    # Create backup if requested
    if create_backup:
        print("Creating backup of original structure...")
        backup_created = backup_structure(project_root, directories)
        if backup_created:
            print("✓ Backup created successfully")
        print()
    
    # Create Maven directory structure
    print("Creating Maven directory structure...")
    has_webcontent = directories['webcontent'] is not None
    create_maven_structure(project_root, has_webcontent)
    print()
    
    # Define source and target directories
    old_src_dir = directories['main_source']  # Could be JavaSource, src, etc.
    old_test_dir = directories['test_source']
    old_webcontent_dir = directories['webcontent']
    old_ejb_module_dir = directories['ejb_module']
    
    # Create temporary directories for the move
    temp_main_java = os.path.join(project_root, 'temp_main_java')
    temp_main_resources = os.path.join(project_root, 'temp_main_resources')
    temp_test_java = os.path.join(project_root, 'temp_test_java')
    temp_test_resources = os.path.join(project_root, 'temp_test_resources')
    temp_webapp = os.path.join(project_root, 'temp_webapp')
    temp_ejb_java = os.path.join(project_root, 'temp_ejb_java')
    temp_ejb_resources = os.path.join(project_root, 'temp_ejb_resources')
    
    try:
        main_java_count = 0
        main_resource_count = 0
        test_java_count = 0
        test_resource_count = 0
        webapp_count = 0
        ejb_java_count = 0
        ejb_resource_count = 0
        
        # Move EJB module files to temporary directories (takes precedence over regular src)
        if old_ejb_module_dir:
            print("Moving EJB module files...")
            os.makedirs(temp_ejb_java, exist_ok=True)
            os.makedirs(temp_ejb_resources, exist_ok=True)
            
            ejb_java_count, ejb_resource_count = move_ejb_module(
                old_ejb_module_dir, temp_ejb_java, temp_ejb_resources, "EJB module files"
            )
            
            print(f"✓ Moved {ejb_java_count} EJB Java files and {ejb_resource_count} EJB resources from ejbModule/")
            print()
        
        # Move main source files to temporary directories (if no EJB module)
        elif old_src_dir:
            print(f"Moving main source files from {os.path.basename(old_src_dir)}/...")
            os.makedirs(temp_main_java, exist_ok=True)
            os.makedirs(temp_main_resources, exist_ok=True)
            
            main_java_count = move_java_files(old_src_dir, temp_main_java, "main Java files")
            main_resource_count = move_resources(old_src_dir, temp_main_resources, "main resources")
            
            print(f"✓ Moved {main_java_count} Java files and {main_resource_count} resource files from {os.path.basename(old_src_dir)}/")
            print()
        
        # Move test files to temporary directories
        if old_test_dir:
            print("Moving test files...")
            os.makedirs(temp_test_java, exist_ok=True)
            os.makedirs(temp_test_resources, exist_ok=True)
            
            test_java_count = move_java_files(old_test_dir, temp_test_java, "test Java files")
            test_resource_count = move_resources(old_test_dir, temp_test_resources, "test resources")
            
            print(f"✓ Moved {test_java_count} test Java files and {test_resource_count} test resource files from test/")
            print()
        
        # Move WebContent to webapp
        if old_webcontent_dir:
            print("Moving WebContent to webapp...")
            os.makedirs(temp_webapp, exist_ok=True)
            
            webapp_count = move_webcontent(old_webcontent_dir, temp_webapp, "WebContent files")
            
            print(f"✓ Moved {webapp_count} webapp files from WebContent/")
            print()
        
        # Clean up empty directories
        print("Cleaning up empty directories...")
        if old_src_dir:
            cleanup_empty_directories(old_src_dir)
        if old_ejb_module_dir:
            cleanup_empty_directories(old_ejb_module_dir)
        if old_test_dir:
            cleanup_empty_directories(old_test_dir)
        if old_webcontent_dir:
            cleanup_empty_directories(old_webcontent_dir)
        print()
        
        # Move from temporary directories to final Maven structure
        print("Moving to final Maven structure...")
        
        # Move temp directories to final locations
        # Handle EJB module files first (they go to main/)
        if os.path.exists(temp_ejb_java) and os.listdir(temp_ejb_java):
            target_main_java = os.path.join(project_root, 'src', 'main', 'java')
            for item in os.listdir(temp_ejb_java):
                src_path = os.path.join(temp_ejb_java, item)
                dest_path = os.path.join(target_main_java, item)
                if os.path.exists(dest_path):
                    if os.path.isdir(dest_path):
                        shutil.rmtree(dest_path)
                    else:
                        os.remove(dest_path)
                shutil.move(src_path, dest_path)
            shutil.rmtree(temp_ejb_java, ignore_errors=True)
            print(f"✓ Moved EJB Java files to src/main/java/")
            
        if os.path.exists(temp_ejb_resources) and os.listdir(temp_ejb_resources):
            target_main_resources = os.path.join(project_root, 'src', 'main', 'resources')
            for item in os.listdir(temp_ejb_resources):
                src_path = os.path.join(temp_ejb_resources, item)
                dest_path = os.path.join(target_main_resources, item)
                if os.path.exists(dest_path):
                    if os.path.isdir(dest_path):
                        shutil.rmtree(dest_path)
                    else:
                        os.remove(dest_path)
                shutil.move(src_path, dest_path)
            shutil.rmtree(temp_ejb_resources, ignore_errors=True)
            print(f"✓ Moved EJB resources to src/main/resources/")
        
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
        
        if os.path.exists(temp_webapp) and os.listdir(temp_webapp):
            target_webapp = os.path.join(project_root, 'src', 'main', 'webapp')
            shutil.rmtree(target_webapp, ignore_errors=True)
            shutil.move(temp_webapp, target_webapp)
            print(f"✓ Moved webapp files to src/main/webapp/")
        
        print()
        print("=" * 60)
        print("✓ SUCCESS: Project structure converted to Maven standard layout!")
        print()
        print("Summary:")
        print(f"  - Main Java files: {main_java_count} -> src/main/java/")
        print(f"  - Main resources: {main_resource_count} -> src/main/resources/")
        print(f"  - Test Java files: {test_java_count} -> src/test/java/")
        print(f"  - Test resources: {test_resource_count} -> src/test/resources/")
        if webapp_count > 0:
            print(f"  - Webapp files: {webapp_count} -> src/main/webapp/")
        
        if create_backup:
            print(f"  - Original structure backed up in: backup_original_structure/")
        
    except Exception as e:
        print(f"❌ ERROR during conversion: {e}")
        print("Attempting to clean up temporary directories...")
        
        # Clean up temporary directories on error
        for temp_dir in [temp_main_java, temp_main_resources, temp_test_java, temp_test_resources, temp_webapp]:
            if os.path.exists(temp_dir):
                shutil.rmtree(temp_dir, ignore_errors=True)
        
        raise
    
    finally:
        # Clean up any remaining temporary directories
        for temp_dir in [temp_main_java, temp_main_resources, temp_test_java, temp_test_resources, temp_webapp]:
            if os.path.exists(temp_dir):
                shutil.rmtree(temp_dir, ignore_errors=True)

def is_project_directory(directory):
    """
    Check if a directory looks like a single project (has pom.xml, JavaSource, src, etc.)
    vs a parent directory containing multiple modules.
    """
    project_indicators = ['pom.xml', 'JavaSource', 'src', 'WebContent', 'ejbModule', 'build.xml']
    return any(os.path.exists(os.path.join(directory, indicator)) for indicator in project_indicators)

def main():
    if len(sys.argv) < 2:
        print("Usage: python3 maven_structure_converter.py <project_or_modules_directory> [--no-backup]")
        print("Examples:")
        print("  Single project: python3 maven_structure_converter.py ./NotmotorIplWebb")
        print("  Multiple modules: python3 maven_structure_converter.py ./modules_parent")
        print("  No backup: python3 maven_structure_converter.py ./project --no-backup")
        sys.exit(1)

    target_dir = sys.argv[1]
    create_backup = "--no-backup" not in sys.argv

    if not os.path.exists(target_dir):
        print(f"❌ ERROR: Directory {target_dir} does not exist")
        sys.exit(1)

    if not os.path.isdir(target_dir):
        print(f"❌ ERROR: {target_dir} is not a directory")
        sys.exit(1)

    # Check if this is a single project or a parent directory with multiple modules
    if is_project_directory(target_dir):
        # Single project conversion
        print(f"=== Processing single project: {os.path.basename(target_dir)} ===")
        try:
            convert_to_maven_structure(target_dir, create_backup)
        except Exception as e:
            print(f"❌ FATAL ERROR: {e}")
    else:
        # Multiple modules conversion
        subdirs = [d for d in os.listdir(target_dir) 
                  if os.path.isdir(os.path.join(target_dir, d)) and not d.startswith('.')]
        
        if not subdirs:
            print(f"No sub-modules found in {target_dir}")
            sys.exit(0)

        print(f"Found {len(subdirs)} potential modules in {target_dir}")
        
        for subdir in subdirs:
            module_path = os.path.join(target_dir, subdir)
            # Only process directories that look like projects
            if is_project_directory(module_path):
                print(f"\n=== Processing module: {subdir} ===")
                try:
                    convert_to_maven_structure(module_path, create_backup)
                except Exception as e:
                    print(f"❌ FATAL ERROR in module {subdir}: {e}")
            else:
                print(f"Skipping {subdir} (not a project directory)")

if __name__ == "__main__":
    main()