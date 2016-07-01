#!/bin/bash

echo "This script will query the db_table_drugs database using a Python script."
echo "Then, it will clean the input and output directories for the NER program."
echo "Finally, it will copy the output into the NER directory and set up the environment variables to run the NER program."

echo "Moving files..."
# rm /home/std30/u-of-pitt-SPL-drug-NER/textfiles/*.txt
# rm /home/std30/u-of-pitt-SPL-drug-NER/processed-output/*.xml
cp ~/01_TableAnnotator/TableAnnotator/annotation/to_NER/*.txt ~/u-of-pitt-SPL-drug-NER/textfiles
echo "Files moved."

echo "Setting environment variables..."
export BASEPATH="/home/std30/u-of-pitt-SPL-drug-NER"

export CLASSPATH=$BASEPATH/lib/xml-apis-1.4.01.jar:$BASEPATH/lib/jena-iri-0.9.2.jar:$BASEPATH/lib/httpcore-4.1.3.jar:$BASEPATH/lib/extjwnl-1.6.4.jar:$BASEPATH/lib/commons-codec-1.5.jar:$BASEPATH/lib/jena-arq-2.9.2.jar:$BASEPATH/lib/log4j-1.2.16.jar:$BASEPATH/lib/commons-httpclient-3.0.1.jar:$BASEPATH/lib/commons-logging-1.1.1.jar:$BASEPATH/lib/concurrentlinkedhashmap-lru-1.2.jar:$BASEPATH/lib/jena-tdb-0.9.2.jar:$BASEPATH/lib/httpclient-4.1.2.jar:$BASEPATH/lib/extjwnl-utilities-1.6.4.jar:$BASEPATH/lib/jena-core-2.7.2.jar:$BASEPATH/lib/mysql-connector-java-5.1.17.jar:$BASEPATH/lib/xercesImpl-2.10.0.jar:$BASEPATH/lib/http-builder-0.5.2.jar:$BASEPATH/lib/jackson-core-2.1.1.jar:$BASEPATH/lib/jackson-databind-2.1.1.jar:$BASEPATH/lib/jackson-annotations-2.1.1.jar:$BASEPATH/target/classes/:
echo "Evironment variables set."
