-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: pharmacy
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales` (
  `idsales` int(11) NOT NULL AUTO_INCREMENT,
  `iditem` int(11) DEFAULT NULL,
  `idstore` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `totalprice` double DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`idsales`),
  UNIQUE KEY `idsales_UNIQUE` (`idsales`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (37,123456789,1234,20,35,'2017-01-01'),(38,123456789,1234,20,35,'2017-02-02'),(39,123456789,1234,20,35,'2017-03-03'),(40,123456789,1234,20,35,'2017-04-04'),(41,123456789,1234,20,35,'2017-05-05'),(42,123456789,1234,20,35,'2017-06-06'),(43,123456789,1234,20,35,'2017-07-07'),(44,123456789,1234,20,35,'2017-08-08'),(45,123456789,1234,20,35,'2017-09-09'),(46,123456789,1234,20,35,'2017-10-10'),(47,123456789,1234,20,35,'2017-11-11'),(48,123456789,1234,20,35,'2017-12-12'),(49,123456789,1234,20,35,'2017-12-12'),(50,33000000,1234,20,35,'2017-12-12'),(51,1,6,10,950,'2017-04-27'),(52,2,6,25,250,'2017-04-27'),(53,3,6,50,2500,'2017-04-27'),(54,1,6,50,4750,'2017-04-27'),(55,3,6,450,22500,'2017-04-27'),(56,2,6,30,300,'2017-04-27'),(57,2,6,3,30,'2017-04-27'),(58,1,6,100,9500,'2017-04-27'),(59,1,6,50,4750,'2017-04-27'),(60,1,6,50,4750,'2017-04-27'),(61,1,6,100,9500,'2017-04-27'),(62,1,6,1000,9999999000,'2017-04-27'),(63,1,6,31900,318999968100,'2017-04-27'),(64,1,6,99999999,999999890000001,'2017-04-27'),(65,1,6,5,49999995,'2017-04-27'),(66,1,6,1,9999999,'2017-04-27'),(67,1,6,1,9999999,'2017-04-27'),(68,1,6,1,9999999,'2017-04-27'),(69,9,6,1,3,'2017-04-27'),(70,1,6,1,9999999,'2017-04-27'),(71,9,6,1,3,'2017-04-27'),(72,1,6,1,9999999,'2017-04-27'),(73,9,6,1,3,'2017-04-27'),(74,1,6,1,9999999,'2017-04-27'),(75,9,6,1,3,'2017-04-27'),(76,3,6,1,45,'2017-04-27'),(77,3,6,1,45,'2017-04-27'),(78,1,6,1,9999999,'2017-04-27');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-27 20:07:27
