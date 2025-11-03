import os
import re

def process_java_file(file_path):
    """Remove everything before the package statement in a Java file."""
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()

    # Find the index of the package statement
    package_index = next((i for i, line in enumerate(lines) if line.strip().startswith('package ')), None)

    if package_index is not None:
        # Keep only the lines from the package statement onwards
        new_content = ''.join(lines[package_index:])
        
        # Write the updated content back to the file
        with open(file_path, 'w', encoding='utf-8') as file:
            file.write(new_content)
        print(f"Processed: {file_path}")
    else:
        print(f"No package statement found in: {file_path}")

def process_java_files_in_directory(directory):
    """Recursively process all Java files in a directory."""
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                process_java_file(file_path)

if __name__ == "__main__":
    # Set the directory to the current working directory
    base_directory = os.getcwd()

    # Process all Java files in the directory
    process_java_files_in_directory(base_directory)