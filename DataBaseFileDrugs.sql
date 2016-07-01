-- MySQL Script to generate SQL database for the Table Annotator
--
-- NOTE: It is up to you what database you load these tables into!
--
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Table `Article`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Article` ;

CREATE TABLE IF NOT EXISTS `Article` (
  `idArticle` INT NOT NULL AUTO_INCREMENT,
  `PMCID` VARCHAR(50) NULL,
  `PMID` VARCHAR(45) NULL,
  `pissn` VARCHAR(45) NULL,
  `eissn` VARCHAR(45) NULL,
  `Title` VARCHAR(2000) NULL,
  `Abstract` VARCHAR(5000) NULL,
  `JournalName` VARCHAR(2000) NULL,
  `JournalPublisherName` VARCHAR(2000) NULL,
  `JournalPublisherLocation` VARCHAR(5000) NULL,
  `Source` VARCHAR(500) NULL,
  `SpecId` VARCHAR(500) NULL,
  `PlainText` LONGTEXT NULL,
  PRIMARY KEY (`idArticle`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Author`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Author` ;

CREATE TABLE IF NOT EXISTS `Author` (
  `idAuthor` INT NOT NULL AUTO_INCREMENT,
  `FirstName` VARCHAR(200) NULL,
  `LastName` VARCHAR(200) NULL,
  `Article_idArticle` INT NOT NULL,
  PRIMARY KEY (`idAuthor`),
  INDEX `fk_Author_Article1_idx` (`Article_idArticle` ASC),
  CONSTRAINT `fk_Author_Article1`
    FOREIGN KEY (`Article_idArticle`)
    REFERENCES `Article` (`idArticle`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ArtTable`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ArtTable` ;

CREATE TABLE IF NOT EXISTS `ArtTable` (
  `idTable` INT NOT NULL AUTO_INCREMENT,
  `TableOrder` VARCHAR(500) NULL,
  `TableCaption` VARCHAR(5000) NULL,
  `TableFooter` VARCHAR(5000) NULL,
  `StructureType` VARCHAR(45) NULL,
  `PragmaticType` VARCHAR(45) NULL,
  `HasXML` VARCHAR(40) NULL,
  `Article_idArticle` INT NOT NULL,
  `Section` VARCHAR(1000) NULL,
  PRIMARY KEY (`idTable`),
  INDEX `fk_Table_Article1_idx` (`Article_idArticle` ASC),
  CONSTRAINT `fk_Table_Article1`
    FOREIGN KEY (`Article_idArticle`)
    REFERENCES `Article` (`idArticle`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Cell`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Cell` ;

CREATE TABLE IF NOT EXISTS `Cell` (
  `idCell` INT NOT NULL AUTO_INCREMENT,
  `CellID` VARCHAR(200) NULL,
  `CellType` VARCHAR(200) NULL,
  `Table_idTable` INT NOT NULL,
  `RowN` INT NULL,
  `ColumnN` INT NULL,
  `HeaderRef` VARCHAR(200) NULL,
  `StubRef` VARCHAR(200) NULL,
  `SuperRowRef` VARCHAR(200) NULL,
  `Content` VARCHAR(5000) NULL,
  `WholeHeader` VARCHAR(5000) NULL,
  `WholeStub` VARCHAR(5000) NULL,
  `WholeSuperRow` VARCHAR(5000) NULL,
  PRIMARY KEY (`idCell`),
  INDEX `fk_Cell_Table1_idx` (`Table_idTable` ASC),
  CONSTRAINT `fk_Cell_Table1`
    FOREIGN KEY (`Table_idTable`)
    REFERENCES `ArtTable` (`idTable`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CellRole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CellRole` ;

CREATE TABLE IF NOT EXISTS `CellRole` (
  `idCellRole` INT NOT NULL AUTO_INCREMENT,
  `CellRoleName` VARCHAR(45) NULL,
  PRIMARY KEY (`idCellRole`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CellRoles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CellRoles` ;

CREATE TABLE IF NOT EXISTS `CellRoles` (
  `idCellRoles` INT NOT NULL AUTO_INCREMENT,
  `CellRole_idCellRole` INT NOT NULL,
  `Cell_idCell` INT NOT NULL,
  PRIMARY KEY (`idCellRoles`),
  INDEX `fk_CellRoles_CellRole1_idx` (`CellRole_idCellRole` ASC),
  INDEX `fk_CellRoles_Cell1_idx` (`Cell_idCell` ASC),
  CONSTRAINT `fk_CellRoles_CellRole1`
    FOREIGN KEY (`CellRole_idCellRole`)
    REFERENCES `CellRole` (`idCellRole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CellRoles_Cell1`
    FOREIGN KEY (`Cell_idCell`)
    REFERENCES `Cell` (`idCell`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Annotation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Annotation` ;

CREATE TABLE IF NOT EXISTS `Annotation` (
  `idAnnotation` INT NOT NULL AUTO_INCREMENT,
  `Content` VARCHAR(1000) NULL,
  `Start` INT NULL,
  `End` INT NULL,
  `AnnotationID` VARCHAR(500) NULL,
  `AnnotationSchemaVersion` VARCHAR(500) NULL,
  `AnnotationDescription` VARCHAR(1500) NULL,
  `AnnotationURL` VARCHAR(1000) NULL,
  `AgentName` VARCHAR(500) NULL,
  `AgentType` VARCHAR(45) NULL,
  `EnvironmentDescription` VARCHAR(1000) NULL,
  `DateOfAction` DATETIME NULL,
  `Location` VARCHAR(500) NULL,
  `Cell_idCell` INT NOT NULL,
  PRIMARY KEY (`idAnnotation`),
  INDEX `fk_Annotation_Cell1_idx` (`Cell_idCell` ASC),
  CONSTRAINT `fk_Annotation_Cell1`
    FOREIGN KEY (`Cell_idCell`)
    REFERENCES `Cell` (`idCell`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Affiliation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Affiliation` ;

CREATE TABLE IF NOT EXISTS `Affiliation` (
  `idAffiliation` INT NOT NULL AUTO_INCREMENT,
  `AffiliationName` VARCHAR(345) NULL,
  `Author_idAuthor` INT NOT NULL,
  PRIMARY KEY (`idAffiliation`),
  INDEX `fk_Affiliation_Author1_idx` (`Author_idAuthor` ASC),
  CONSTRAINT `fk_Affiliation_Author1`
    FOREIGN KEY (`Author_idAuthor`)
    REFERENCES `Author` (`idAuthor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Email`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Email` ;

CREATE TABLE IF NOT EXISTS `Email` (
  `idEmail` INT NOT NULL AUTO_INCREMENT,
  `Email` VARCHAR(1000) NULL,
  `Author_idAuthor` INT NOT NULL,
  PRIMARY KEY (`idEmail`, `Author_idAuthor`),
  INDEX `fk_Email_Author1_idx` (`Author_idAuthor` ASC),
  CONSTRAINT `fk_Email_Author1`
    FOREIGN KEY (`Author_idAuthor`)
    REFERENCES `Author` (`idAuthor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OriginalArticle`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `OriginalArticle` ;

CREATE TABLE IF NOT EXISTS `OriginalArticle` (
  `idOriginalArticle` INT NOT NULL AUTO_INCREMENT,
  `PMCID` VARCHAR(50) NULL,
  `PMID` VARCHAR(45) NULL,
  `pissn` VARCHAR(45) NULL,
  `eissn` VARCHAR(45) NULL,
  `xml` LONGTEXT NULL,
  `Article_idArticle` INT NOT NULL,
  PRIMARY KEY (`idOriginalArticle`),
  UNIQUE INDEX `idOriginalArticle_UNIQUE` (`idOriginalArticle` ASC),
  INDEX `fk_OriginalArticle_Article1_idx` (`Article_idArticle` ASC),
  CONSTRAINT `fk_OriginalArticle_Article1`
    FOREIGN KEY (`Article_idArticle`)
    REFERENCES `Article` (`idArticle`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `CellRole`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `CellRole` (`idCellRole`, `CellRoleName`) VALUES (1, 'Header');
INSERT INTO `CellRole` (`idCellRole`, `CellRoleName`) VALUES (2, 'Stub');
INSERT INTO `CellRole` (`idCellRole`, `CellRoleName`) VALUES (3, 'Data');
INSERT INTO `CellRole` (`idCellRole`, `CellRoleName`) VALUES (4, 'SuperRow');

COMMIT;

