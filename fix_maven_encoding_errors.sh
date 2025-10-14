#!/bin/bash
# Convenience script to fix encoding errors from Maven compilation
# 
# Usage: 
#   ./fix_maven_encoding_errors.sh [directory]
#   
# If directory is not provided, uses current directory

set -e

DIR="${1:-.}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "=== Fixing Maven Encoding Errors ==="
echo "Directory: $DIR"
echo

# Run Maven compilation and capture errors
echo "Running Maven compile to detect encoding errors..."
if mvn compile -f "$DIR" 2>&1 | grep "unmappable character" > /tmp/maven_errors.txt; then
    echo "Found Maven encoding errors. Processing with fix_encoding.py..."
    echo
    
    # Process the errors with our Python script
    cat /tmp/maven_errors.txt | python3 "$SCRIPT_DIR/fix_encoding.py" --maven-errors
    
    echo
    echo "=== Re-running Maven compile to verify fixes ==="
    if mvn compile -f "$DIR" -Dfile.encoding=UTF-8; then
        echo
        echo "✅ SUCCESS: All encoding errors have been fixed!"
        echo "Maven compilation completed successfully."
    else
        echo
        echo "❌ WARNING: Maven compilation still has issues."
        echo "There may be other types of errors beyond encoding."
    fi
else
    echo "No Maven encoding errors found. Running general encoding fix..."
    echo
    
    # Run general fix on all Java files
    python3 "$SCRIPT_DIR/fix_encoding.py" "$DIR"
    
    echo
    echo "✅ Encoding fix completed."
fi

echo
echo "=== Summary ==="
echo "All Swedish character encoding issues should now be resolved."
echo "Files have been converted from ISO-8859-1 to UTF-8."

# Clean up
rm -f /tmp/maven_errors.txt