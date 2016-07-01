#!/bin/bash

# This script will check which setIDs are currently in the database, then remove the corresponding
# setIDs from the input_XML directory for the TableAnnotator program.

# The first python script queries the database to determine the setIDs that have already been annotated.
echo "Retrieving setIDs that have already been annotated."
python /home/std30/01_TableAnnotator/TableAnnotator/annotation/check_annotated_setIDs.py

# Next, we need to clear the input_XML directory
echo "Clearing input_XML directory..."
rm -rf /home/std30/01_TableAnnotator/TableAnnotator/annotation/input_XML/*/

# Then, we need to retrieve the filenames that correspond to each setID
echo "Retrieving filenames from linkedSPLs database..."
python /home/std30/01_TableAnnotator/TableAnnotator/annotation/retrieve_SPL_filenames.py

# Finally, we need to copy the corresponding XML files from the dev server archives
echo "Retrieving XML files from linkedSPLs archives..."
python /home/std30/01_TableAnnotator/TableAnnotator/annotation/retrieve_SPLs_in_XML.py

echo "Done."
