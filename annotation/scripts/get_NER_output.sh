#!/bin/bash

echo "This script will copy the output of the NER program back into this working directory."

echo "Moving files..."
cp ~/u-of-pitt-SPL-drug-NER/processed-output/*.xml ~/01_TableAnnotator/TableAnnotator/annotation/from_NER/
echo "Files moved."