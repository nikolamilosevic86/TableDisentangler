import json
from pprint import pprint
import csv

annots = []
table_content = []
annots_to_insert = []

# with open('json-output/b3e740ad-46d9-48f5-85ab-7c319919ceb1.txt-PROCESSED.xml.json') as data_file:
	# data = json.load(data_file)

with open('json-output/b3e740ad-46d9-48f5-85ab-7c319919ceb1.txt-PROCESSED.xml.txt', 'rb') as csvfile:
	reader = csv.reader(csvfile, delimiter='\t')
	for row in reader:
		annots.append(row)
		print row

with open('to_NER/b3e740ad-46d9-48f5-85ab-7c319919ceb1.txt', 'rb') as origfile:
	reader = csv.reader(origfile, delimiter='\t')
	for row in reader:
		table_content.append(row)
		print row

match_cnt = 0
for row in table_content:
	for anno in annots:
		new_anno = []
		if anno[0] in row[2]:
			new_anno.append(row[0]) # idCell
			new_anno.append(anno[0]) # preferredName
			new_anno.append(anno[2]) # AnnotationURL
			# print "\nFrom annotations: " + anno[0] + " matches: " + row[2]
			# print "And the idCell is : " + row[0]
			# if row[2] not in anno[1]:
				# print "But the preferred name is: " + anno[1]
			match_cnt += 1
			annots_to_insert.append(new_anno)


print "Number of rows: " + str(len(table_content))
print "Number of matches: " + str(match_cnt)

for row in annots_to_insert:
	print row


import mysql.connector
cnx = mysql.connector.connect(
	user='std30', password='5bboys',
    host='127.0.0.1', database='table_db_drugs')

cursor = cnx.cursor()
