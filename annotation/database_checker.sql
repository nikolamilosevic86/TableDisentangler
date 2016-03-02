SELECT DISTINCT SpecId
FROM `table_db_drugs`.`Article`;

SELECT idTable, TableCaption, Section, WholeHeader, Content, CellID, idCell, CellRoleName, CellType, SpecId 
FROM Cell INNER JOIN ArtTable ON Cell.Table_idTable=ArtTable.idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
INNER JOIN CellRoles ON Cell.idCell=CellRoles.Cell_idCell
INNER JOIN CellRole ON CellRole.idCellRole=CellRoles.CellRole_idCellRole
WHERE CellType != "Empty"
AND (Section LIKE "%DRUG INTERACTIONS%"
OR Section LIKE "%CLINICAL PHARMACOLOGY%")
ORDER BY idTable ASC;

DESCRIBE Annotation;

SELECT idTable, TableCaption, Section, WholeHeader, Content, CellID, idCell, CellRoleName, CellType, SpecId 
FROM Cell INNER JOIN ArtTable ON Cell.Table_idTable=ArtTable.idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
INNER JOIN CellRoles ON Cell.idCell=CellRoles.Cell_idCell
INNER JOIN CellRole ON CellRole.idCellRole=CellRoles.CellRole_idCellRole
WHERE SpecId = "b3e740ad-46d9-48f5-85ab-7c319919ceb1"
AND Section = "7 DRUG INTERACTIONS"
AND CellType != "Empty"
ORDER BY CellID ASC;

# Get all annotations from each setID that comes from Drug Int or Clin Pharm sections,
# where the annotation agent was either MetaMap or DBMI-NER
SELECT idAnnotation, SpecId, Cell.idCell, Cell.CellID, Annotation.Content, AnnotationURL, AgentName, Annotation.Start, Annotation.End, AnnotationID, AgentType 
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
AND CellType != "Empty"
AND (Section LIKE "%DRUG INTERACTIONS%"
OR Section LIKE "%CLINICAL PHARMACOLOGY%")
AND (AgentName = "DBMI-NER"
OR AgentName = "MetaMap")
ORDER BY idCell;

SELECT SpecId FROM Article WHERE SpecId NOT IN (SELECT DISTINCT SpecId
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
WHERE AgentName = 'MetaMap');

# Number of distinct setIDs that have been annotated by MetaMap
SELECT DISTINCT SpecId
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
WHERE AgentName = 'MetaMap';

SELECT idAnnotation, SpecId, Cell.idCell, Cell.CellID, Annotation.Content, AnnotationURL, AgentName, Annotation.Start, Annotation.End, AnnotationID, AgentType 
FROM Annotation INNER JOIN Cell ON Cell.idCell=Annotation.Cell_idCell
INNER JOIN ArtTable ON ArtTable.idTable=Cell.Table_idTable
INNER JOIN Article ON Article.idArticle=ArtTable.Article_idArticle
WHERE SpecID = '80beab2c-396e-4a37-a4dc-40fdb62859cf';