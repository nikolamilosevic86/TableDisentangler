TablInExj - Table information extraction system
===============================================

TablInExj stands for Table Information Extraction (j is for java). TablInExj is a tool for extracting information from tables in PMC clinical documents in XML format.

Tool does this in a couple of steps. Firstly, tables are decomposed to a matrix of cell objects containing data and information about navigational path (headers, stubs, subheaders). After the table is decomposed, set of rules are applied to extract information about key clinical trial characteristics.

This project is developed on the University of Manchester as a part of my PhD

Requirements
------------

The tool requires Java, OpenNLP, Weka toolkit, MySQL database and possibly it will require some semantic tools such as metamap, WikipediaMiner etc.

Some manipulation on dataset (splitting data to training, testing and cross-validation sets, downloading data, extracting tables etc.) are done by python scripts in TableMiningHelpers git project.
  
License
-------

The tool is under GNU/GPL 3 license. Licence agreement may be read here: http://www.gnu.org/copyleft/gpl.html