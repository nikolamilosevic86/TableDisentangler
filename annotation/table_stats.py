import mysql.connector
import csv
import re
import os
import string
import operator


# Function to calculate median of a list of ints
def median(lst):
    lst = sorted(lst)
    if len(lst) < 1:
            return None
    if len(lst) %2 == 1:
            return lst[((len(lst)+1)/2)-1]
    else:
            return float(sum(lst[(len(lst)/2)-1:(len(lst)/2)+1]))/2.0

# Function for outputting the table header structures in a dictionary
# Key: unique structure of table header categories
# Values: setIDs that contain this structure
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

# Gather unique number of tables from the Drug Interaction section that were correctly loaded into the schema
cursor = cnx.cursor()
get_table_ids_query = ("SELECT DISTINCT idTable "
	"FROM ArtTable INNER JOIN Cell ON ArtTable.idTable=Cell.Table_idTable "
	"INNER JOIN Annotation ON Cell.idCell=Annotation.Cell_idCell "
	"INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle "
	"WHERE AgentName = 'MetaMap' "
	"AND LOWER(Section) LIKE '%interaction%' "
	"AND SpecId IN ( "
		"SELECT DISTINCT SpecId "
		"FROM Article ); ")
cursor.execute(get_table_ids_query)
table_ids = []
for table_id in cursor:
	table_ids.append(table_id[0])
print "Number of unique tables within the Drug Interactions section: " +  str(len(table_ids))
cursor.close()


# Gather all content to filter for unique headers and assign tables into their corresponding categories
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


# Classify the table content into the corresponding header structures
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
for key in groupings_dict:
	if key in groupings_dict.keys():
		splits = groupings_dict[key].split(' ')

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
print "Effect on Drug: " + str(len(eff))
print "Interaction Properties: " + str(len(intprop))
print "Interacting Substance: " + str(len(intsub))
print "Interacting Substance Properties: " + str(len(intsubprop))
print "Misc.: " + str(len(misc))
print "Recommendation or Comment: " + str(len(reccom))
print "Sample Size: " + str(len(sampsize))
print "Other: " + str(len(other))


# Gather counts of rows and columsn for calculating the median
cursor = cnx.cursor()
row_counts = []
col_counts = []

median_query = ("SELECT count(DISTINCT RowN), count(DISTINCT ColumnN) "
	"FROM ArtTable "
	"INNER JOIN Cell ON Cell.Table_idTable=ArtTable.idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable; ")

cursor.execute(median_query)

for (r, c) in cursor:
	row_counts.append(r)
	col_counts.append(c)

cursor.close()
print "\nMedian number of columns: " + str(median(col_counts))
print "Median number of rows: " + str(median(row_counts))

# Minimum column calculation
cursor = cnx.cursor()
min_col_query = ("SELECT MIN(indv_table_c.col_count) FROM ( "
	"SELECT count(DISTINCT ColumnN) as col_count "
	"FROM ArtTable AS article_table_c "
	"INNER JOIN Cell as cell_c ON cell_c.Table_idTable=article_table_c.idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable) AS indv_table_c; ")
cursor.execute(min_col_query)
for m in cursor:
	print "\nMinimum number of columns: " + str(m[0])
cursor.close()

# Maximum column calculation
cursor = cnx.cursor()
max_col_query = ("SELECT MAX(indv_table_c.col_count) FROM ( "
	"SELECT count(DISTINCT ColumnN) as col_count "
	"FROM ArtTable AS article_table_c "
	"INNER JOIN Cell as cell_c ON cell_c.Table_idTable=article_table_c.idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable) AS indv_table_c; ")
cursor.execute(max_col_query)
for m in cursor:
	print "Maximum number of columns: " + str(m[0])
cursor.close()

# Average column number calculation
cursor = cnx.cursor()
avg_col_query = ("SELECT avg(indv_table_c.col_count) FROM ( "
	"SELECT count(DISTINCT ColumnN) as col_count "
	"FROM ArtTable AS article_table_c "
	"INNER JOIN Cell as cell_c ON cell_c.Table_idTable=article_table_c.idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable) AS indv_table_c; ")
cursor.execute(avg_col_query)
for m in cursor:
	print "Average number of columns: " + str(m[0])
cursor.close()

# Minimum number of rows calculation
cursor = cnx.cursor()
min_row_query = ("SELECT MIN(indv_table_r.row_count) FROM ( "
	"SELECT count(DISTINCT RowN) as row_count "
	"FROM ArtTable AS article_table_r "
	"INNER JOIN Cell ON Cell.Table_idTable=article_table_r.idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable) AS indv_table_r; ")
cursor.execute(min_row_query)
for m in cursor:
	print "Minimum number of rows: " + str(m[0])
cursor.close()

# Maximum number of rows calculation
cursor = cnx.cursor()
max_row_query = ("SELECT MAX(indv_table_r.row_count) FROM ( "
	"SELECT count(DISTINCT RowN) as row_count "
	"FROM ArtTable AS article_table_r "
	"INNER JOIN Cell ON Cell.Table_idTable=article_table_r.idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable) AS indv_table_r; ")
cursor.execute(max_row_query)
for m in cursor:
	print "Maximum number of rows: " + str(m[0])
cursor.close()

# Average number of rows calculation
cursor = cnx.cursor()
avg_row_query = ("SELECT avg(indv_table_r.row_count) FROM ( "
	"SELECT count(DISTINCT RowN) as row_count "
	"FROM ArtTable AS article_table_r "
	"INNER JOIN Cell ON Cell.Table_idTable=article_table_r.idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND CellType != 'Empty' "
	"GROUP BY idTable) AS indv_table_r; ")
cursor.execute(avg_row_query)
for m in cursor:
	print "Average number of rows: " + str(m[0])
cursor.close()

# Retrieve the count of annotations to calculate the median
cursor = cnx.cursor()
median_anno_query = ("SELECT count(annotation_avg.idAnnotation) "
	"FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell "
	"INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable "
	"INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle "
	"AND cell_avg.CellType != 'Empty' "
	"AND LOWER(arttable_avg.Section) LIKE '%interaction%' "
	"AND annotation_avg.AgentName = 'MetaMap' "
	"GROUP BY arttable_avg.idTable; ")
cursor.execute(median_anno_query)
anno_counts = []
for m in cursor:
	anno_counts.append(m[0])
print "\nMedian number of annotations: " + str(median(anno_counts))

# Minimum number of annotations per table
cursor = cnx.cursor()
min_anno_query = ("SELECT MIN(total_annotation_per_table.annotation_count) AS AnnotationMin FROM ( "
	"SELECT count(annotation_avg.idAnnotation) AS annotation_count "
	"FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell "
	"INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable "
	"INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle "
	"AND cell_avg.CellType != 'Empty' "
	"AND LOWER(arttable_avg.Section) LIKE '%interaction%' "
	"AND annotation_avg.AgentName = 'MetaMap' "
	"GROUP BY arttable_avg.idTable) AS total_annotation_per_table; ")
cursor.execute(min_anno_query)
for m in cursor:
	print "Minimum number of annotations: " + str(m[0])
cursor.close()

# Maximum number of annotations per table
cursor = cnx.cursor()
max_anno_query = ("SELECT MAX(total_annotation_per_table.annotation_count) AS AnnotationMax FROM ( "
	"SELECT count(annotation_avg.idAnnotation) AS annotation_count "
	"FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell "
	"INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable "
	"INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle "
	"AND cell_avg.CellType != 'Empty' "
	"AND LOWER(arttable_avg.Section) LIKE '%interaction%' "
	"AND annotation_avg.AgentName = 'MetaMap' "
	"GROUP BY arttable_avg.idTable) AS total_annotation_per_table; ")
cursor.execute(max_anno_query)
for m in cursor:
	print "Maximum number of annotations: " + str(m[0])
cursor.close()

# Average number of annotations per table
cursor = cnx.cursor()
avg_anno_query = ("SELECT avg(total_annotation_per_table.annotation_count) AS AnnotationAverage FROM ( "
	"SELECT count(annotation_avg.idAnnotation) AS annotation_count "
	"FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell "
	"INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable "
	"INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle "
	"AND cell_avg.CellType != 'Empty' "
	"AND LOWER(arttable_avg.Section) LIKE '%interaction%' "
	"AND annotation_avg.AgentName = 'MetaMap' "
	"GROUP BY arttable_avg.idTable) AS total_annotation_per_table; ")
cursor.execute(avg_anno_query)
for m in cursor:
	print "Average number of annotations: " + str(m[0])
cursor.close() 

# Retrieves the counts for each CUI for all tables in the Drug Interactions section
cursor = cnx.cursor()
ret_CUI_counts = ("SELECT count(AnnotationID), AnnotationID "
	"FROM Annotation INNER JOIN Cell on Cell.idCell=Annotation.Cell_idCell "
	"INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable "
	"WHERE LOWER(Section) LIKE '%interaction%' "
	"AND AgentName = 'MetaMap' "
	"AND CellType != 'Empty' "
	"GROUP BY AnnotationID "
	"ORDER BY count(AnnotationID) DESC; ")
cursor.execute(ret_CUI_counts)
CUI_counts = {}
for (ct, CUI) in cursor:
	if str(CUI) not in CUI_counts.keys():
		CUI_counts[str(CUI)] = int(ct)
	elif str(CUI) in CUI_counts.keys():
		hold = CUI_counts[CUI]
		hold += ct
		CUI_counts[CUI] = hold
cursor.close()
#cnx.close()
#print CUI_counts

# Grab the CUIs for the Annotation figure
cursor = cnx.cursor()
example_CUIs = []
grab_CUIs = ("SELECT Annotation.Content, AnnotationID FROM Article INNER JOIN ArtTable ON Article.idArticle=ArtTable.Article_idArticle "
	"INNER JOIN Cell ON Cell.Table_idTable=ArtTable.idTable "
	"INNER JOIN Annotation ON Annotation.Cell_idCell=Cell.idCell "
	"WHERE SpecId = '992306f0-c612-41dd-a4df-35d1fd360033' "
	"AND CellType != 'Empty' "
	"AND (CellID = '21.2' "
	"OR CellID = '21.1' "
	"OR CellID = '21.0') "
	"AND AgentName = 'MetaMap'; ")
cursor.execute(grab_CUIs)
for (cont, c) in cursor:
	line = []
	line.append(str(cont))
	line.append(str(c))
	example_CUIs.append(line)
cursor.close()
cnx.close()

print "\nThe CUIs for the figure example: "
for line in example_CUIs:
	print line

# Grab the STY's for the example figure
cnx = mysql.connector.connect(user=usr, password=pword, host=hst, database='umls')
cursor = cnx.cursor()
example_STY = {}
for line in example_CUIs:
	cui = line[1]
	get_STY = ("SELECT STY FROM MRSTY WHERE CUI = %(line_CUI)s; ")
	cursor.execute(get_STY, { 'line_CUI': cui})
	for sty in cursor:
		if cui not in example_STY:
			example_STY[cui] = str(sty[0])
		elif cui in example_STY:
			hold = example_STY[cui]
			hold+= '\t' + str(sty[0])
			example_STY[cui] = hold
cursor.close()
#print example_STY

# Grab the CUI to STY mappings for all distinct CUIs in the Drug Interactions section
cursor = cnx.cursor()
CUI_STY_mappings = {}
for key in CUI_counts.keys():
	get_CUI_STY = ("SELECT STY FROM MRSTY WHERE CUI = %(line_CUI)s; ")
	cursor.execute(get_CUI_STY, { 'line_CUI': key })
	for sty in cursor:
		if key not in CUI_STY_mappings.keys():
			CUI_STY_mappings[key] = str(sty[0])
		elif key in CUI_STY_mappings.keys():
			hold = CUI_STY_mappings[key]
			hold += '\t' + str(sty[0])
			CUI_STY_mappings[key] = hold
cursor.close()
cnx.close()

#print str(len(CUI_STY_mappings.keys()))
#print CUI_STY_mappings

STY_count = {}
# Calculate the frequency for each STY from the queries above
for CUI in CUI_counts.keys():
	if CUI in CUI_STY_mappings:
		if '\t' in CUI_STY_mappings[CUI]:
			splits = CUI_STY_mappings[CUI].split('\t')
			for sty in splits:
				if sty not in STY_count:
					STY_count[sty] = CUI_counts[CUI]
				elif sty in STY_count:
					hold_count = STY_count[sty]
					hold_count += CUI_counts[CUI]
					STY_count[sty] = hold_count
		else:
			sty = CUI_STY_mappings[CUI]
			if sty not in STY_count:
				STY_count[sty] = CUI_counts[CUI]
			elif sty in STY_count:
				hold_count = STY_count[sty]
				hold_count += CUI_counts[CUI]
				STY_count[sty] = hold_count


print "\nNumber of distinct semantic types: " + str(len(STY_count.keys()))
tot = 332504
#print STY_count
sorted_x = sorted(STY_count.items(), key=operator.itemgetter(1))
print "\nSemantic type distribution: " 
for s in sorted_x[-10:]:
	print s[0] + '\t' + str(s[1]) + '\t' + str(float(int(s[1])/float(tot)) * 100)