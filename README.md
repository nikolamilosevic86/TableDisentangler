TableDisentangler - A tool for automatic disentangling of functional areas in tables and their annotation
===============================================

TableDisentangler is a tool for annotating tables written in Java. It uses specific annotation schema we proposed that is able to capture information about functions of a cell and inter-cell relationships. TableDisentangler is a tool for extracting annotations from tables in PMC clinical documents in XML format (it is possible to generate XML from PDF).

Tool does this in a couple of steps. Firstly, tables are decomposed to a matrix of cell objects containing data and information about navigational path (headers, stubs, subheaders).

This project is developed on the University of Manchester as a part of my PhD

Requirements
------------

The tool requires Java, OpenNLP, Weka toolkit, MySQL database, installed MetaMap and WordNet.


Other project dependences
---------------------------

Some manipulation on dataset (splitting data to training, testing and cross-validation sets, downloading data, extracting tables etc.) are done by python scripts in TableMiningHelpers git project.
  
Database output of this system may be used as input database for the MedCurator project

You need also to checkout [Marvin project](https://github.com/nikolamilosevic86/Marvin) and include reference to it in a project. 

License
-------

The tool is under GNU/GPL 3 license. Licence agreement may be read here: http://www.gnu.org/copyleft/gpl.html

Referencing
------------
- Milosevic, N., Gregson, C., Hernandez, R., & Nenadic, G. (2016, June). [Disentangling the Structure of Tables in Scientific Literature.](http://link.springer.com/chapter/10.1007/978-3-319-41754-7_14) In International Conference on Applications of Natural Language to Information Systems (pp. 162-174). Springer International Publishing. [Article on Academia.edu (free access)](https://www.academia.edu/26501270/Disentangling_the_structure_of_tables_in_scientic_literature)

- Milosevic N., Gregson C., Hernandez R. and Nenadic G. (2016). [Extracting Patient Data from Tables in Clinical Literature - Case Study on Extraction of BMI, Weight and Number of Patients.](https://www.academia.edu/19974490/Extracting_patient_data_from_tables_in_clinical_literature_Case_study_on_extraction_of_BMI_weight_and_number_of_patients) In Proceedings of the 9th International Joint Conference on Biomedical Engineering Systems and Technologies ISBN 978-989-758-170-0, pages 223-228. DOI: 10.5220/0005660102230228
