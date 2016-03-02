import re
import os
import string
import glob
import mysql.connector

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

cnx = mysql.connector.connect(user=usr, password=pword, host=hst, database='linkedSPLs')

cursor = cnx.cursor()

setIDs = [line.rstrip('\n') for line in open('set-ids-for-spls-with-tables-ddi-or-clin-pharm.txt')]

file_names = []

for sid in setIDs:

	drugInt_clinPharm_query = (
		"SELECT filename FROM structuredProductLabelMetadata WHERE setId = %(setId)s ;"
	)

	cursor.execute(drugInt_clinPharm_query, { 'setId': sid })

	for fn in cursor:
		file_names.append(fn)

cursor.close()
cnx.close()

with open('file-names-for-spls-with-tables-ddi-or-clin-pharm.txt', 'wb') as outfile:
	for f in file_names:
		outfile.write("%s\n" % f)
