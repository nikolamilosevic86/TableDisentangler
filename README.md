TableAnnotator - A tool for automatic table annotation
===============================================

TableAnnotator is a tool for annotating tables written in Java. It uses specific annotation schema we proposed that is able to capture information about functions of a cell and inter-cell relationships. TableAnnotator is a tool for extracting annotations from tables in PMC clinical documents in XML format.

Tool does this in a couple of steps. Firstly, tables are decomposed to a matrix of cell objects containing data and information about navigational path (headers, stubs, subheaders).

This project is developed on the University of Manchester as a part of my PhD

Requirements
------------

The tool requires Java, OpenNLP, Weka toolkit, MySQL database and possibly it will require some semantic tools such as metamap, WikipediaMiner etc.


Other project dependences
---------------------------

Some manipulation on dataset (splitting data to training, testing and cross-validation sets, downloading data, extracting tables etc.) are done by python scripts in TableMiningHelpers git project.
  
Database output of this system may be used as input database for the MedCurator project

License
-------

The tool is under GNU/GPL 3 license. Licence agreement may be read here: http://www.gnu.org/copyleft/gpl.html

Referencing
------------
Extracting patient data from tables in clinical literature: Case study on extraction of BMI, weight and number of patients; 
Nikola Milosevic, Cassie Gregson, Rober Hernandez, Goran Nenadic; 
In Proceedings of the 9th International Joint Conference on Biomedical Engineering Systems and Technologies (BIOSTEC 2016) - Volume 5: HEALTHINF, pages 223-228
ISBN: 978-989-758-170-0
https://www.academia.edu/19974490/Extracting_patient_data_from_tables_in_clinical_literature_Case_study_on_extraction_of_BMI_weight_and_number_of_patients
