import mysql.connector
import csv
import re

# Get database connection settings
settings = []
with open("settings.cfg", "r+") as config_file:
	hold = config_file.readlines()
	for h in hold:
		splits = h.split(";")
		line = []
		line.append(splits[0])
		line.append(splits[1])
		settings.append(line)

for line in settings:
	if "database_username" in line[0]:
		usr = line[1]
	if "database_password" in line[0]:
		pword = line[1]
	if "database_host" in line[0]:
		hst = line[1]
	if "database_name" in line[0]:
		dbname = line[1]
	if "database_port" in line[0]:
		dbport = line[1]

cnx = mysql.connector.connect(user=usr, password=pword, host=hst, database=dbname)

cursor = cnx.cursor()

setIDs_already_annotated = []

check_query = (
	"SELECT DISTINCT SpecId "
	"FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell "
	"INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable "
	"INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle "
	"WHERE AgentName = 'MetaMap'; ")

cursor.execute(check_query)


with open('setIDs-already-annotated.txt', 'wb') as outfile:
	for s in cursor:
		outfile.write("%s\n" % s)

