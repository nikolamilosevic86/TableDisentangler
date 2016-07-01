# Selects unique setIDs from database
SELECT DISTINCT SpecId
FROM Article;

# Number of distinct cells that have been annotated by MetaMap and are from the DI section
SELECT DISTINCT idCell
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
WHERE AgentName = 'MetaMap'
AND LOWER(Section) LIKE "%interaction%"
AND SpecId IN (
	SELECT DISTINCT SpecId
	FROM `table_db_drugs`.`Article`);

# Counts for each cell role
SELECT CellRoleName, count(CellRoleName) FROM Cell
INNER JOIN CellRoles ON Cell.idCell=CellRoles.Cell_idCell
INNER JOIN CellRole ON CellRoles.CellRole_idCellRole=CellRole.idCellRole
INNER JOIN ArtTable ON Cell.Table_idTable=ArtTable.idTable
WHERE CellType != "Empty"
AND idTable IN (
SELECT DISTINCT idTable
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
WHERE AgentName = 'MetaMap'
AND LOWER(Section) LIKE "%interaction%"
AND SpecId IN (
	SELECT DISTINCT SpecId
	FROM Article))
GROUP BY CellRoleName;

SELECT count(DISTINCT idCell) 
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
WHERE CellType != 'Empty'
AND LOWER(Section) LIKE '%interaction%'
AND SpecId IN (
	SELECT DISTINCT SpecId
	FROM Article);

# Counts for each table structure
SELECT count(StructureType), StructureType
FROM ArtTable WHERE idTable IN (
SELECT idTable FROM ArtTable
INNER JOIN Cell ON Cell.Table_idTable=ArtTable.idTable
INNER JOIN Annotation ON Annotation.Cell_idCell=Cell.idCell
WHERE AgentName = 'MetaMap'
AND LOWER(Section) LIKE "%interaction%")
GROUP BY StructureType;

# Selects the count of rows and columns from each distinct table
SELECT count(DISTINCT RowN), count(DISTINCT ColumnN)
FROM ArtTable
INNER JOIN Cell ON Cell.Table_idTable=ArtTable.idTable
WHERE LOWER(Section) LIKE "%interaction%"
AND CellType != "Empty"
GROUP BY idTable;

# Average number of rows per table in the Drug Interactions section
SELECT avg(indv_table_r.row_count) FROM (
SELECT count(DISTINCT RowN) as row_count
FROM ArtTable AS article_table_r
INNER JOIN Cell ON Cell.Table_idTable=article_table_r.idTable
WHERE LOWER(Section) LIKE "%interaction%"
AND CellType != "Empty"
GROUP BY idTable) AS indv_table_r;

# Minimum number of rows per table in the Drug Interactions section
SELECT MIN(indv_table_r.row_count) FROM (
SELECT count(DISTINCT RowN) as row_count
FROM ArtTable AS article_table_r
INNER JOIN Cell ON Cell.Table_idTable=article_table_r.idTable
WHERE LOWER(Section) LIKE "%interaction%"
AND CellType != "Empty"
GROUP BY idTable) AS indv_table_r;

# Maximum number of rows per table in the Drug Interactions section
SELECT MAX(indv_table_r.row_count) FROM (
SELECT count(DISTINCT RowN) as row_count
FROM ArtTable AS article_table_r
INNER JOIN Cell ON Cell.Table_idTable=article_table_r.idTable
WHERE LOWER(Section) LIKE "%interaction%"
AND CellType != "Empty"
GROUP BY idTable) AS indv_table_r;

# Average number of columns per table in the Drug Interactions section
SELECT avg(indv_table_c.col_count) FROM (
SELECT count(DISTINCT ColumnN) as col_count
FROM ArtTable AS article_table_c
INNER JOIN Cell as cell_c ON cell_c.Table_idTable=article_table_c.idTable
WHERE LOWER(Section) LIKE "%interaction%"
AND CellType != "Empty"
GROUP BY idTable) AS indv_table_c;

# Minimum number of columns per table in the Drug Interactions section
SELECT MIN(indv_table_c.col_count) FROM (
SELECT count(DISTINCT ColumnN) as col_count
FROM ArtTable AS article_table_c
INNER JOIN Cell as cell_c ON cell_c.Table_idTable=article_table_c.idTable
WHERE LOWER(Section) LIKE "%interaction%"
AND CellType != "Empty"
GROUP BY idTable) AS indv_table_c;

# Maximum number of columns per table in the Drug Interactions section
SELECT MAX(indv_table_c.col_count) FROM (
SELECT count(DISTINCT ColumnN) as col_count
FROM ArtTable AS article_table_c
INNER JOIN Cell as cell_c ON cell_c.Table_idTable=article_table_c.idTable
WHERE LOWER(Section) LIKE "%interaction%"
AND CellType != "Empty"
GROUP BY idTable) AS indv_table_c;

# Get all annotations from each setID that comes from Drug Int
# where the annotation agent is MetaMap
SELECT idAnnotation, SpecId, Cell.idCell, Cell.CellID, Annotation.Content, AnnotationURL, AgentName, Annotation.Start, Annotation.End, AnnotationID, AgentType 
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
AND CellType != "Empty"
AND LOWER(Section) LIKE "%interaction%"
AND AgentName = "MetaMap"
ORDER BY idCell;

# Average number of MetaMap annotations per table in the Drug Interactions section
SELECT avg(total_annotation_per_table.annotation_count) AS AnnotationAverage FROM (
	SELECT count(annotation_avg.idAnnotation) AS annotation_count
	FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell
	INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable
	INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle
	AND cell_avg.CellType != "Empty"
	AND LOWER(arttable_avg.Section) LIKE "%interaction%"
	AND annotation_avg.AgentName = "MetaMap"
	GROUP BY arttable_avg.idTable) AS total_annotation_per_table;

# Count of MetaMap annotations per table in Drug Interactions section
	SELECT count(annotation_avg.idAnnotation) AS annotation_count
	FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell
	INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable
	INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle
	AND cell_avg.CellType != "Empty"
	AND LOWER(arttable_avg.Section) LIKE "%interaction%"
	AND annotation_avg.AgentName = "MetaMap"
	GROUP BY arttable_avg.idTable;

# Maximum number of MetaMap annotations from tables in the Drug Interactions section
SELECT MAX(total_annotation_per_table.annotation_count) AS AnnotationMax FROM (
	SELECT count(annotation_avg.idAnnotation) AS annotation_count
	FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell
	INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable
	INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle
	AND cell_avg.CellType != "Empty"
	AND LOWER(arttable_avg.Section) LIKE "%interaction%"
	AND annotation_avg.AgentName = "MetaMap"
	GROUP BY arttable_avg.idTable) AS total_annotation_per_table;

# Minimum number of MetaMap annotations for tables in Drug Interactions section
SELECT MIN(total_annotation_per_table.annotation_count) AS AnnotationMin FROM (
	SELECT count(annotation_avg.idAnnotation) AS annotation_count
	FROM Annotation as annotation_avg INNER JOIN Cell AS cell_avg ON cell_avg.idCell=annotation_avg.Cell_idCell
	INNER JOIN ArtTable AS arttable_avg ON arttable_avg.idTable=cell_avg.Table_idTable
	INNER JOIN Article AS article_avg ON article_avg.idArticle=arttable_avg.Article_idArticle
	AND cell_avg.CellType != "Empty"
	AND LOWER(arttable_avg.Section) LIKE "%interaction%"
	AND annotation_avg.AgentName = "MetaMap"
	GROUP BY arttable_avg.idTable) AS total_annotation_per_table;

# All setIDs that are in the database and do not have any MetaMap annotations 
SELECT DISTINCT SpecId FROM Article WHERE SpecId NOT IN (SELECT DISTINCT SpecId
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
WHERE AgentName = 'MetaMap');

# FOR UMLS DATABASE:
# select * from MRCONSO inner join MRSTY on MRCONSO.CUI = MRSTY.CUI LIMIT 50000;