import mysql.connector
import csv
import re
import os
import string

def median(lst):
    lst = sorted(lst)
    if len(lst) < 1:
            return None
    if len(lst) %2 == 1:
            return lst[((len(lst)+1)/2)-1]
    else:
            return float(sum(lst[(len(lst)/2)-1:(len(lst)/2)+1]))/2.0

def convertToStructureDict(structure):
	finaldict = {}
	for s in structure:
		splits = s.split("\t", 1)
		if splits[1] not in finaldict.keys():
			finaldict[splits[1]] = splits[0]
		elif splits[1] in finaldict.keys():
			hold = finaldict[splits[1]]
			hold += " " + splits[0]
			finaldict[splits[1]] = hold
	return finaldict

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

get_table_ids_query = ("SELECT DISTINCT idTable "
	"FROM ArtTable INNER JOIN Cell ON ArtTable.idTable=Cell.Table_idTable "
	"INNER JOIN Annotation ON Cell.idCell=Annotation.Cell_idCell "
	"INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle "
	"WHERE AgentName = 'MetaMap' "
	"AND Section LIKE '%DRUG INTERACTIONS%' "
	"AND SpecId IN ( "
		"SELECT DISTINCT SpecId "
		"FROM Article ); ")

cursor.execute(get_table_ids_query)

table_ids = []

for table_id in cursor:
	table_ids.append(table_id[0])

print "Number of unique tables within the Drug Interactions section: " +  str(len(table_ids))

cursor.close()

cursor = cnx.cursor()

table_content_dump = []

for table_id in table_ids:
	table_content = []
	get_table_content = ("SELECT Content FROM Cell "
		"INNER JOIN CellRoles ON Cell.idCell=CellRoles.Cell_idCell "
		"INNER JOIN CellRole ON CellRoles.CellRole_idCellRole=CellRole.idCellRole "
		"INNER JOIN ArtTable ON Cell.Table_idTable=ArtTable.idTable "
		"WHERE CellType != 'Empty' "
		"AND idTable = %(table_id)s ; ")

	cursor.execute(get_table_content, { 'table_id': table_id })
	
	table_content.append(table_id)

	for c in cursor:
		clean_content = c[0].encode('utf-8').lstrip().rstrip()
		clean_content = clean_content.upper()
		clean = re.sub(r'[\W_]+', '', clean_content)
		if clean not in table_content:
			table_content.append(clean)

	table_content_dump.append(table_content)

cursor.close()
cursor = cnx.cursor()
row_counts = []
col_counts = []

median_query = ("SELECT count(DISTINCT RowN), count(DISTINCT ColumnN) "
	"FROM ArtTable "
	"INNER JOIN Cell ON Cell.Table_idTable=ArtTable.idTable "
	"WHERE Section LIKE '%DRUG INTERACTION%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable; ")

cursor.execute(median_query)

for (r, c) in cursor:
	row_counts.append(r)
	col_counts.append(c)

cursor.close()
cursor = cnx.cursor()

print "\nMedian number of rows: " + str(median(row_counts))
print "Median number of columns: " + str(median(col_counts))

median_anno_query = ("SELECT count(annotation_avg.idAnnotation) "
	"FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell "
	"INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable "
	"INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle "
	"AND cell_avg.CellType != 'Empty' "
	"AND arttable_avg.Section LIKE '%DRUG INTERACTIONS%' "
	"AND annotation_avg.AgentName = 'MetaMap' "
	"GROUP BY arttable_avg.idTable; ")

cursor.execute(median_anno_query)
anno_counts = []
for m in cursor:
	anno_counts.append(m[0])

print "\nMedian number of annotations: " + str(median(anno_counts))



headerClassification = {}

with open("Categories.txt", "rb") as txtData:
	for line in txtData:
		line = line.translate(None, '[\n\r]')
		line = filter(lambda x: x in string.printable, line)
		line = re.split(r"\t+", line.strip("\n"))
		cat = line[1]
		header = re.sub(r'[\W_]+', '', line[0])
		if cat in headerClassification:
			if header not in headerClassification.values():
				headerClassification[cat].append(header)
			else:
				break
		else:
			headerClassification.update({header: cat})

tableStructure = []
unique_headers = []

for table in table_content_dump:
	header_string = str(table[0]) + '\t'
	#print '\n'
	for i in range(1, len(table)):
		if table[i] in headerClassification:
			if table[i] not in unique_headers:
				unique_headers.append(table[i])
			header_string += headerClassification[table[i]] + '\t'
	tableStructure.append(header_string)
	#print header_string

groupings_dict = convertToStructureDict(tableStructure)
'''for key in groupings_dict:
	if key in groupings_dict.keys():
		splits = groupings_dict[key].split(' ')
		print 'Table Header Structure: '
		print key
		print str(len(splits)) + ' tables.'
		print '\n' '''

drugnameorclass = []
reccom = []
eff = []
intsubprop = []
misc = []
intprop = []
sampsize = []
intsub = []
other = []


print "\nNumber of unique headers: " + str(len(unique_headers))

for header in unique_headers:
	if header in headerClassification:
		if 'Drug Name or Drug Class' in headerClassification[header]:
			drugnameorclass.append(header)
		elif 'Recommendation or Comment' in headerClassification[header]:
			reccom.append(header)
		elif 'Effect on Drug' in headerClassification[header]:
			eff.append(header)
		elif 'Interacting Substance Properties' in headerClassification[header]:
			intsubprop.append(header)
		elif 'Misc.' in headerClassification[header]:
			misc.append(header)
		elif 'Interaction Properties' in headerClassification[header]:
			intprop.append(header)
		elif 'Sample Size' in headerClassification[header]:
			sampsize.append(header)
		elif headerClassification[header] == 'Interacting Substance':
			intsub.append(header)
		else:
			other.append(header)
			
print "\nNumber of unique headers per category: "
print "Drug Name or Drug Class: " + str(len(drugnameorclass))
print "Recommendation or Comment: " + str(len(reccom))
print "Effect on Drug: " + str(len(eff))
print "Interacting Substance Properties: " + str(len(intsubprop))
print "Misc.: " + str(len(misc))
print "Interaction Properties: " + str(len(intprop))
print "Sample Size: " + str(len(sampsize))
print "Interacting Substance: " + str(len(intsub))
print "Other: " + str(len(other))



# print headerClassification

