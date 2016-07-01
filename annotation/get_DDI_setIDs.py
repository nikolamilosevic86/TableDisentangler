import os

table_names = [line.rstrip('\n') for line in open('table_names.txt')]
#print table_names

setIDs = []

with open("ddi-setIDs.txt", "w") as outfile:
	for name in table_names:
		if "TABLE" in name:
			tokens = name.split("-")
			setID = tokens[1] + '-' + tokens[2] + '-' + tokens[3] + '-' + tokens[4] + '-' + tokens[5]
			if setID not in setIDs:
				setIDs.append(setID)
				outfile.write("%s\n" % setID)

