#!/bin/sh
echo "Processing of PubMedCentral files is starting!"
java -jar TableAnnotator.jar PMCSmall PMC makestats -compexclassify -doie -ld -databasesave