import os
import os.path
import string
import shutil
import errno

def make_sure_path_exists(path, file_name, f):
	try:
		os.makedirs(path)
		print "Copying file: " + file_name
		print "to directory: " + path + f
		shutil.copyfile(file_name, path + f)
	except OSError as exception:
		if exception.errno != errno.EEXIST:
			raise

file_names = [line.rstrip('\n') for line in open('file-names-for-spls-with-tables-ddi-or-clin-pharm.txt')]

for f in file_names:
	splits = f.split('.')
	dir_name = splits[0]
	
	full_file_name = '/home/rdb20/bio2rdf/linkedSPLs/LinkedSPLs-update/load-dailymed-spls/spls/' + dir_name + '/' + f
	new_directory = '/home/std30/01_TableAnnotator/TableAnnotator/annotation/input_XML/' + dir_name + '/'
	
	print full_file_name
	
	if os.path.isfile(full_file_name):
		make_sure_path_exists(new_directory, full_file_name, f)
