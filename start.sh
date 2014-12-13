#!/bin/bash

echo "Create unique index for hash"
mongo test ./mongo/create.unique.index.js
echo "Start sbt..."
sbt 
