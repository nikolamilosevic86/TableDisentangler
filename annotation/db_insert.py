import json
from pprint import pprint
import csv
import os.path
import csv
from os import listdir
from os.path import isfile, join
import mysql.connector

cnx = mysql.connector.connect(
	user='std30', password='5bboys',
    host='127.0.0.1', database='table_db_drugs')


table_content = []


INpath = str(os.getcwd()) + '/unique_annots/'
print INpath

annots_files = [f for f in listdir(INpath) if isfile(join(INpath, f))]
print annots_files

for f in annots_files:
	annots_file_name = str(os.getcwd()) + '/unique_annots/' + f
	hold = f.split('.')
	setID = hold[0]
	print setID
	print annots_file_name
	annots = []
	with open(annots_file_name, 'rb') as annots_csvfile:
		reader = csv.reader(annots_csvfile, delimiter='\t')
		for row in reader:
			annots.append(row)
			# print row

	annots_to_insert = []
	orig_file_name = str(os.getcwd()) + '/to_NER/' + setID + '.txt'
	with open(orig_file_name, 'rb') as orig_csvfile:
		reader = csv.reader(orig_csvfile, delimiter='\t')
		for row in reader:
			for anno in annots:
				new_anno = []
				if anno[0] in row[1]:
					new_anno.append(row[0]) # idCell
					new_anno.append(anno[0]) # preferredName
					new_anno.append(anno[2]) # AnnotationURL
					annots_to_insert.append(new_anno)
	for row in annots_to_insert:
		cursor = cnx.cursor()

		insert_query = (
		"INSERT INTO Annotation "
		"(Content,Start,End,AnnotationID,AgentType, "
		"AgentName,AnnotationURL,EnvironmentDescription, "
		"Cell_idCell,AnnotationDescription,AnnotationSchemaVersion, "
		"DateOfAction,Location) "
		"VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s); "
		)

		anno_info = (row[1], 0, 0, 'HOLD', 'Software', 'DBMI-NER', row[2], 'HOLD', int(float(row[0])), 'HOLD', 'HOLD', '12/23/1993', 'Pittsburgh, PA')

		cursor.execute(insert_query, anno_info)
		cnx.commit()

cursor.close()
cnx.close()




OUTpath = 'annots_to_insert/'




'''for row in annots_to_insert:
	cursor = cnx.cursor()
	row[2].split('/')

	 

anno_info = {
	'Content': row[1],
	'Start': 0,
	'End': 0,
	'AnnotationID': 'HOLD',
	'AgentType': 'Software',
	'AgentName': 'DBMI-NER',
	'AnnotationURL': row[2],
	'EnvironmentDescription': 'HOLD',
	'Cell_idCell': int(float(row[0])),
	'AnnotationDescription': 'HOLD',
	'AnnotationSchemaVersion': 'HOLD',
	'DateOfAction': '12/23/1993',
	'Location': 'Pittsburgh, PA'
	} '''
