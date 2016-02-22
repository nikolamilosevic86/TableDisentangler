# Testing how to query a MySQL database using a Python Script
# Author: Steven DeMarco

import mysql.connector
import csv
import re

cnx = mysql.connector.connect(user='std30', password='5bboys',
                                                          host='127.0.0.1',
                                                          database='table_db_drugs')

cursor = cnx.cursor()

drugInt_clinPharm_query = (
	"SELECT TableCaption, Section, Content, CellID, idCell, CellType, SpecId "
	"FROM Cell INNER JOIN ArtTable ON Cell.Table_idTable=ArtTable.idTable "
	"INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle "
	"INNER JOIN CellRoles ON Cell.idCell=CellRoles.Cell_idCell "
	"INNER JOIN CellRole ON CellRole.idCellRole=CellRoles.CellRole_idCellRole "
	"WHERE CellType != 'Empty' "
	"AND (Section LIKE '%DRUG INTERACTIONS%' "
	"OR Section LIKE '%CLINICAL PHARMACOLOGY%') "
	"ORDER BY idTable ASC; "
)

cursor.execute(drugInt_clinPharm_query)

table_content = []
open_files = []
setIDs = []

for (TableCaption, Section, Content, CellID, idCell, CellType, SpecId) in cursor:
	row = []
	
	# Open one file for each setID
	if str(SpecId) not in setIDs:
		setIDs.append(str(SpecId))
	
	# Only concerned about cell's content and id
	row.append(str(idCell))
	row.append(Content.encode('utf-8').lstrip().rstrip())
	row.append(str(SpecId))

	# Reduce repeats
	if row not in table_content:
		table_content.append(row)

print "Length of table_content:\t" + str(len(table_content))

# Open each output file named for each unique setID
open_files = []
for setID in setIDs:
	f = open("to_NER/" + setID + ".txt", "wb")
	open_files.append(f)

# Write out table_content corresponding to each setID
for f in open_files:
	hold = f.name.split("/")
	setID = hold[1]
	
	output_writer = csv.writer(f, delimiter='\t')
	
	for row in table_content:
		if row[2] in setID:
			output_writer.writerow(row)

cursor.close()
cnx.close()

'''specific_query = (
	"SELECT TableCaption, Section, Content, CellID, idCell, CellType, SpecId "
	"FROM Cell INNER JOIN ArtTable ON Cell.Table_idTable=ArtTable.idTable "
	"INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle "
	"INNER JOIN CellRoles ON Cell.idCell=CellRoles.Cell_idCell "
	"INNER JOIN CellRole ON CellRole.idCellRole=CellRoles.CellRole_idCellRole "
	"WHERE SpecId = 'b3e740ad-46d9-48f5-85ab-7c319919ceb1' "
	"AND Section = '7 DRUG INTERACTIONS' "
	"AND CellType != 'Empty' "
	"ORDER BY CellID ASC; ") 

cursor.execute(specific_query)

p = re.compile("0\.\d")
header_string = ''

for row in table_content:
	if p.match(str(row[0])):
		header_string += row[1] + '\t' '''
