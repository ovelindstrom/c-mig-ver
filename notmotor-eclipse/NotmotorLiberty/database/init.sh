#!/bin/bash

echo "Starting DDL execution via init.sh..."

# The key is to explicitly call the 'db2' utility with the correct flags
# and reference the DDL file's path inside the container.
db2 connect to notmotor user db2inst1 using db2inst1
db2 -tvf /var/custom/tables.ddl -z /tmp/ddl_output.log
db2 terminate

echo "DDL execution complete. Check /tmp/ddl_output.log for details."