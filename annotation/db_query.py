# Testing how to query a MySQL database using a Python Script
# Author: Steven DeMarco

import mysql.connector
import csv
import re

cnx = mysql.connector.connect(user='std30', password='5bboys',
                                                          host='127.0.0.1',
                                                          database='table_db_drugs')

cursor = cnx.cursor()

query = ("SELECT idArticle, SpecId FROM `table_db_drugs`.`Article`;")

cursor.execute(query)
setIDs = []

for (idArticle, SpecId) in cursor:
	# print ("{}: {}".format(idArticle, SpecId))
	setIDs.append(str(SpecId))

print 'setIDs:'
for setID in setIDs:
	print setID

drugInt_clinPharm_query = {
	"SELECT idTable, TableCaption, Section, WholeHeader, Content, CellID, idCell, CellRoleName, CellType, SpecId "
	"FROM Cell INNER JOIN ArtTable ON Cell.Table_idTable=ArtTable.idTable "
	"INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle "
	"INNER JOIN CellRoles ON Cell.idCell=CellRoles.Cell_idCell "
	"INNER JOIN CellRole ON CellRole.idCellRole=CellRoles.CellRole_idCellRole "
	"WHERE CellType != 'Empty' "
	"AND (Section LIKE '%DRUG INTERACTIONS%' "
	"OR Section LIKE '%CLINICAL PHARMACOLOGY%') "
	"ORDER BY idTable ASC; "
}

specific_query = (
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

table_content = []
ex_setID = ''
for (TableCaption, Section, Content, CellID, idCell, CellType, SpecId) in cursor:
	row = []
	print 'Content: \t' + Content.lstrip().rstrip()
	print '\n'
	row.append(str(idCell))
	row.append(str(CellID))
	row.append(Content.encode('utf-8').lstrip().rstrip())
	row.append(SpecId.encode('utf-8').lstrip().rstrip())
	ex_setID = str(SpecId)
	if row not in table_content:
		table_content.append(row)

print "Length of table_content:\t" + str(len(table_content))


'''p = re.compile("0\.\d")
header_string = ''


for row in table_content:
	if p.match(str(row[0])):
		header_string += row[1] + '\t' '''

# print header_string

with open("to_NER/" + ex_setID + ".txt", "wb") as csv_file:
	query_writer = csv.writer(csv_file, delimiter='\t')
	for row in table_content:
		query_writer.writerow(row)

cursor.close()
cnx.close()
