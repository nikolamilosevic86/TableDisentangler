#!/bin/sh
echo "Processing of DailyMed files is starting!"
java -jar TableAnnotator.jar DrugLabelSmall\prescription dailymed makestats -compexclassify -doie -ld -databasesave