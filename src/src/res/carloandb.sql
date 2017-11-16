-- MySQL dump 10.13  Distrib 5.6.22, for Win64 (x86_64)
--
-- Host: localhost    Database: carloandb
-- ------------------------------------------------------
-- Server version	5.6.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `autoveicolo`
--

DROP TABLE IF EXISTS `autoveicolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autoveicolo` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `Marca` varchar(15) NOT NULL,
  `Cilindrata` int(5) NOT NULL,
  `NumeroPorte` int(1) NOT NULL,
  `Targa` char(7) NOT NULL,
  `annoimmatricolazione` char(4) NOT NULL,
  `TipoCambio` varchar(10) NOT NULL,
  `AccessoriSerie` varchar(200) NOT NULL,
  `IDStato` int(5) NOT NULL,
  `IDCategoria` int(5) NOT NULL,
  `numeroposti` int(1) NOT NULL,
  `modello` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Targa` (`Targa`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autoveicolo`
--

LOCK TABLES `autoveicolo` WRITE;
/*!40000 ALTER TABLE `autoveicolo` DISABLE KEYS */;
INSERT INTO `autoveicolo` VALUES (3,'12',12,3,'as123as','2010','Manuale','123',3,3,2,'12');
/*!40000 ALTER TABLE `autoveicolo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoria` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `PrezzoChilometrico` float(6,2) NOT NULL,
  `Descrizione` varchar(120) NOT NULL,
  `Nome` varchar(15) NOT NULL,
  `cilindrata` int(5) NOT NULL,
  `tipocambio` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Nome` (`Nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (3,12.00,'12','asdasd',12,'Manuale');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cliente` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(15) NOT NULL,
  `Cognome` varchar(15) NOT NULL,
  `NumeroTelefono` char(10) NOT NULL,
  `CodiceFiscale` char(16) NOT NULL,
  `DataNascita` date NOT NULL,
  `IndirizzoResidenza` varchar(30) NOT NULL,
  `TipoPatente` varchar(3) NOT NULL,
  `numeropatente` char(9) NOT NULL,
  `ScadenzaPatente` date NOT NULL,
  `CittaResidenza` varchar(15) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CodiceFiscale` (`CodiceFiscale`),
  UNIQUE KEY `numeropatente` (`numeropatente`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (5,'Antonio','Pargoli','1234567990','NTNPRG45P76L261K','1990-02-25','Via dadadadad','B','AS123456L','2019-02-24','Andria');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contratto`
--

DROP TABLE IF EXISTS `contratto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contratto` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `DataInizio` date NOT NULL,
  `DataFine` date NOT NULL,
  `StatoContratto` varchar(10) NOT NULL,
  `IDImpiegato` int(5) NOT NULL,
  `SedeRitiro` int(5) NOT NULL,
  `SedeStipula` int(5) NOT NULL,
  `IDCliente` int(5) NOT NULL,
  `IDAutoveicolo` int(5) NOT NULL,
  `IDTariffa` int(5) NOT NULL,
  `nChilometri` int(11) NOT NULL,
  `dataStipula` date NOT NULL,
  `dataChiusura` date DEFAULT NULL,
  `prezzo` float(8,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDAutoveicolo` (`IDAutoveicolo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contratto`
--

LOCK TABLES `contratto` WRITE;
/*!40000 ALTER TABLE `contratto` DISABLE KEYS */;
/*!40000 ALTER TABLE `contratto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `Username` varchar(15) NOT NULL,
  `Password` binary(128) NOT NULL,
  `TipoPermesso` int(5) NOT NULL,
  `IDUtente` int(5) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Username` (`Username`),
  KEY `IDUtente` (`IDUtente`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` VALUES (1,'root.admin','0071e3de75b498457bcea8fadacabee813a4ed0f53643a704065ad262323b1d048302fe5ab02028e120d7eb57c6420369d88cb7acb41a2aaf8a68f9d2ab6c97e',1,1);
/*!40000 ALTER TABLE `login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `optional`
--

DROP TABLE IF EXISTS `optional`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `optional` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) NOT NULL,
  `descrizione` varchar(200) NOT NULL,
  `Prezzo` float(6,2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `optional`
--

LOCK TABLES `optional` WRITE;
/*!40000 ALTER TABLE `optional` DISABLE KEYS */;
/*!40000 ALTER TABLE `optional` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `optional_contratto`
--

DROP TABLE IF EXISTS `optional_contratto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `optional_contratto` (
  `IDContratto` int(5) NOT NULL,
  `IDOptional` int(5) NOT NULL,
  PRIMARY KEY (`IDContratto`,`IDOptional`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `optional_contratto`
--

LOCK TABLES `optional_contratto` WRITE;
/*!40000 ALTER TABLE `optional_contratto` DISABLE KEYS */;
/*!40000 ALTER TABLE `optional_contratto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sede`
--

DROP TABLE IF EXISTS `sede`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sede` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `Citta` varchar(15) NOT NULL,
  `Indirizzo` varchar(30) NOT NULL,
  `NumeroTelefono` char(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sede`
--

LOCK TABLES `sede` WRITE;
/*!40000 ALTER TABLE `sede` DISABLE KEYS */;
/*!40000 ALTER TABLE `sede` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stato_autoveicolo`
--

DROP TABLE IF EXISTS `stato_autoveicolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stato_autoveicolo` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `NumeroChilometri` int(11) NOT NULL,
  `Dettagli` varchar(200) NOT NULL,
  `stato` varchar(20) NOT NULL,
  `IDSede` int(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stato_autoveicolo`
--

LOCK TABLES `stato_autoveicolo` WRITE;
/*!40000 ALTER TABLE `stato_autoveicolo` DISABLE KEYS */;
INSERT INTO `stato_autoveicolo` VALUES (3,12,'12','Disponibile',0);
/*!40000 ALTER TABLE `stato_autoveicolo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tariffa`
--

DROP TABLE IF EXISTS `tariffa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tariffa` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `nome` varchar(15) NOT NULL,
  `prezzoGiornaliero` float(6,2) NOT NULL,
  `tipoTariffa` varchar(30) NOT NULL,
  `descrizione` varchar(200) NOT NULL,
  `prezzoKM` float(6,2) NOT NULL,
  `prezzoGiornalieroBonus` float(6,2) NOT NULL,
  `prezzoKMBonus` float(6,2) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tariffa`
--

LOCK TABLES `tariffa` WRITE;
/*!40000 ALTER TABLE `tariffa` DISABLE KEYS */;
/*!40000 ALTER TABLE `tariffa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utente` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(15) NOT NULL,
  `Cognome` varchar(15) NOT NULL,
  `codicefiscale` char(16) NOT NULL,
  `DataNascita` date NOT NULL,
  `sede` int(5) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `codicefiscale` (`codicefiscale`),
  UNIQUE KEY `codicefiscale_2` (`codicefiscale`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (1,'Francesco','Sinisi','SNSFNC94A03B285K','1994-08-03',NULL);
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-12 19:56:51
