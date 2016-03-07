import os

def chunks(l, n):
    """Yield successive n-sized chunks from l."""
    for i in xrange(0, len(l), n):
        yield l[i:i+n]

table_names_from_dir = [line.rstrip('\n') for line in open('recent_tables.txt')]

setIDs = [line.rstrip('\n') for line in open('ddi-setIDs.txt')]

table_names = []

for name in table_names_from_dir:
	if "/" in name and "TABLE" in name and "drugInteractions" in name:
		splits = name.split("/")
		orig_table_name = splits[1]
		print orig_table_name
		setID_from_table_name = orig_table_name[6:-23]
		print setID_from_table_name
		if setID_from_table_name in setIDs and orig_table_name not in table_names:
			table_names.append(orig_table_name)
	else:
		pass


print str(len(setIDs))
print str(len(table_names))
table_names.sort()

with open('html-tables-for-stats.txt', 'wb') as outfile:
	for s in table_names:
		outfile.write("%s\n" % s)