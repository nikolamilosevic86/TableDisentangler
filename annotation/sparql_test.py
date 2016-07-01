import re, string
import codecs
from SPARQLWrapper import SPARQLWrapper, JSON
import pickle
import sys, os
import fileinput
import glob

from bs4 import BeautifulSoup

setID_drug_mappings = {}

spls = ['009575f9-74e8-4a6d-9fa6-3cae72fd01c3','01243d39-ccf2-43ce-b9bf-b445b9c22c75','b3e740ad-46d9-48f5-85ab-7c319919ceb1']
for spl in spls:
	splUri = "http://bio2rdf.org/linkedspls:" + spl
	qry = '''
	PREFIX dc: <http://purl.org/dc/elements/1.1/>
	PREFIX linkedspls_vocabulary: <http://bio2rdf.org/linkedspls_vocabulary:>
	PREFIX ncbit: <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#>
	PREFIX fn:<http://www.w3.org/2005/xpath-functions#>
	PREFIX dr:<http://www.w3.org/2000/01/rdf-schema#>

	SELECT DISTINCT ?predicate ?object
	WHERE {
	?s linkedspls_vocabulary:setId "%s".
	?s linkedspls_vocabulary:activeMoiety ?amUri.

	?amUri ?predicate ?object.

	}
	LIMIT 2 ''' % (spl)

	sparql = SPARQLWrapper("http://dbmi-icode-01.dbmi.pitt.edu/sparql")

	sparql.setQuery(qry)
	sparql.setReturnFormat(JSON)
	results = sparql.query().convert()

	#print "Results:"
	#print results
	#print "Keys:"
	#print results.keys()


	for res in results["results"]["bindings"]:
		#print "The original keys:"
		#print str(res.keys()) + '\n'
		#print "Predicate and object keys:"
		#print str(res["predicate"].keys())
		#print str(res["object"].keys())
		#print "The values:"
		if "rdf-schema#label" in res["predicate"]["value"]:
			#print "setID: " + spl + " drug name: " + res["object"]["value"]
			setID_drug_mappings[spl]=res["object"]["value"]

print setID_drug_mappings
