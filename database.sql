-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 34.123.83.219    Database: university
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `Admin_key` int NOT NULL,
  `Password` varchar(45) NOT NULL,
  PRIMARY KEY (`Admin_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1337,'1337');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boolean_table`
--

DROP TABLE IF EXISTS `boolean_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boolean_table` (
  `idboolean_table` bigint NOT NULL AUTO_INCREMENT,
  `Description` varchar(45) NOT NULL,
  `boolean` tinyint NOT NULL,
  PRIMARY KEY (`idboolean_table`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boolean_table`
--

LOCK TABLES `boolean_table` WRITE;
/*!40000 ALTER TABLE `boolean_table` DISABLE KEYS */;
INSERT INTO `boolean_table` VALUES (1,'can_reg',0),(2,'can_drop',0);
/*!40000 ALTER TABLE `boolean_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `Course_ID` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Credit` int NOT NULL,
  `Title` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Department` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Total_seats` int NOT NULL,
  `Seats_Left` int NOT NULL,
  PRIMARY KEY (`Course_ID`),
  KEY `pk_department_idx` (`Department`),
  CONSTRAINT `pk_department` FOREIGN KEY (`Department`) REFERENCES `department` (`Name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('CS111',3,'Program Design I','College of Engineering',100,4),('CS141',3,'Program Design II','College of Engineering',200,30),('CS151',3,'Mathematical Foundations of Computing','College of Engineering',260,10),('CS211',2,'Programing Practicum','College of Engineering',120,13),('CS251',4,'Data Structures','College of Engineering',150,119),('CS261',3,'Machine Organization','College of Engineering',90,32),('CS301',3,'Languages and Automata','College of Engineering',60,14),('CS341',3,'Programming Language Design and Implementation','College of Engineering',70,0),('CS342',3,'Software Design','College of Engineering',200,2),('CS361',3,'Systems Programing','College of Engineering',150,14),('CS362',3,'Computer Design','College of Engineering',40,36),('CS401',3,'Computer Algorithms I','College of Engineering',30,63),('CS412',3,'Machine Learning','College of Engineering',25,25),('CS480',3,'Database Systems','College of Engineering',40,-1),('ENGL160',3,'Academic Writing I: Writing in Academic and Public Contexts','College of Liberal Arts and Sciences',150,4),('ENGL161',3,'Academic Writing II: Writing for Inquiry and Research','College of Liberal Arts and Sciences',120,26),('ENGL207',3,'Interpretation and Critical Analysis','College of Liberal Arts and Sciences',80,17),('IE342',3,'Probability and Statistics','College of Engineering',90,90),('MATH215',3,'Introduction to Advanced Mathematics','College of Engineering',28,28);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_member`
--

DROP TABLE IF EXISTS `course_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_member` (
  `PK` bigint NOT NULL AUTO_INCREMENT,
  `UIN` int NOT NULL,
  `Course_ID` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`PK`),
  KEY `fk_course_idx` (`Course_ID`) /*!80000 INVISIBLE */,
  KEY `pk_UIN_idx` (`UIN`),
  CONSTRAINT `pk_courseid` FOREIGN KEY (`Course_ID`) REFERENCES `course` (`Course_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_member`
--

LOCK TABLES `course_member` WRITE;
/*!40000 ALTER TABLE `course_member` DISABLE KEYS */;
INSERT INTO `course_member` VALUES (30,107153854,'CS211'),(31,117131003,'CS480'),(40,622940104,'CS480'),(42,940078946,'CS480'),(43,922793354,'CS480'),(44,414621580,'CS480'),(45,823307933,'CS480'),(46,685650916,'CS480'),(47,675607108,'CS480'),(48,657325069,'CS480'),(49,641988691,'CS480'),(50,625349403,'CS480'),(51,622940104,'CS480'),(52,590887133,'CS480'),(53,583940188,'CS480'),(54,580245421,'CS480'),(55,548897540,'CS480'),(56,527976125,'CS480'),(57,522051561,'CS480'),(58,499938319,'CS480'),(59,473373142,'CS480'),(60,275584923,'CS480'),(62,271459385,'CS480'),(63,177477368,'CS480'),(64,107153854,'CS480'),(65,685650916,'CS401'),(66,675607108,'IE342'),(67,657325069,'CS401'),(68,641988691,'IE342'),(69,625349403,'CS401'),(70,622940104,'IE342'),(71,590887133,'CS401'),(72,583940188,'IE342'),(73,580245421,'IE342'),(74,548897540,'CS401'),(75,527976125,'CS401'),(76,522051561,'CS401'),(77,499938319,'IE342'),(78,473373142,'IE342'),(79,275584923,'IE342'),(81,271459385,'IE342'),(82,177477368,'IE342'),(83,107153854,'CS401'),(84,473373142,'CS401'),(85,473373142,'CS251'),(86,473373142,'CS211'),(87,473373142,'CS141'),(88,473373142,'CS151'),(89,473373142,'CS342'),(90,473373142,'CS341'),(91,473373142,'CS261'),(92,473373142,'CS361'),(93,473373142,'CS362'),(94,473373142,'CS111'),(95,140143164,'CS111'),(96,152298065,'CS141'),(97,156281418,'CS151'),(98,174790226,'CS211'),(99,184211405,'CS251'),(100,354072955,'CS261'),(101,419647062,'CS301'),(102,525628543,'CS341'),(103,572021685,'CS342'),(104,586600833,'CS361'),(105,590074495,'CS362'),(106,657587633,'CS401'),(107,658225785,'CS401'),(108,742670419,'CS480'),(109,833278622,'ENGL160'),(110,836493990,'ENGL161'),(111,949055012,'ENGL207'),(112,986226382,'IE342'),(114,230319323,'CS480');
/*!40000 ALTER TABLE `course_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `current_grade`
--

DROP TABLE IF EXISTS `current_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `current_grade` (
  `pk` int NOT NULL AUTO_INCREMENT,
  `UIN` int NOT NULL,
  `Grade` int NOT NULL DEFAULT '100',
  `Course_ID` varchar(45) NOT NULL,
  PRIMARY KEY (`pk`),
  KEY `pk_UIN_idx` (`UIN`),
  CONSTRAINT `pk_UIN1` FOREIGN KEY (`UIN`) REFERENCES `student` (`UIN`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `current_grade`
--

LOCK TABLES `current_grade` WRITE;
/*!40000 ALTER TABLE `current_grade` DISABLE KEYS */;
INSERT INTO `current_grade` VALUES (103,675607108,100,'IE342'),(104,641988691,100,'IE342'),(105,622940104,100,'IE342'),(106,583940188,100,'IE342'),(107,580245421,100,'IE342'),(108,499938319,100,'IE342'),(109,473373142,100,'IE342'),(110,275584923,100,'IE342'),(111,271459385,100,'IE342'),(112,177477368,100,'IE342'),(113,117131003,100,'CS480'),(114,622940104,100,'CS480'),(115,940078946,100,'CS480'),(116,922793354,100,'CS480'),(117,414621580,100,'CS480'),(118,823307933,100,'CS480'),(119,685650916,100,'CS480'),(120,675607108,100,'CS480'),(121,657325069,100,'CS480'),(122,641988691,100,'CS480'),(123,625349403,100,'CS480'),(124,622940104,100,'CS480'),(125,590887133,100,'CS480'),(126,583940188,100,'CS480'),(127,580245421,100,'CS480'),(128,548897540,100,'CS480'),(129,527976125,100,'CS480'),(130,522051561,100,'CS480'),(131,499938319,100,'CS480'),(132,473373142,100,'CS480'),(133,275584923,100,'CS480'),(134,271832772,100,'CS480'),(135,271459385,100,'CS480'),(136,177477368,100,'CS480'),(137,107153854,100,'CS480'),(138,685650916,100,'CS401'),(139,657325069,100,'CS401'),(140,625349403,100,'CS401'),(141,590887133,100,'CS401'),(142,548897540,100,'CS401'),(143,527976125,100,'CS401'),(144,522051561,100,'CS401'),(145,271832772,100,'CS401'),(146,107153854,100,'CS401'),(147,107153854,100,'CS211'),(152,230319323,-999,'CS261'),(153,230319323,100,'CS480');
/*!40000 ALTER TABLE `current_grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `Name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES ('College of Applied Health Sciences'),('College of Architecture, Design, and the Arts'),('College of Business Administration'),('College of Education'),('College of Engineering'),('College of Liberal Arts and Sciences'),('College of Nursing'),('College of Pharmacy'),('College of Urban Planning and Public Affairs'),('Honors College'),('School of Public Health');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dropped_student`
--

DROP TABLE IF EXISTS `dropped_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dropped_student` (
  `PK` int NOT NULL AUTO_INCREMENT,
  `UIN` int NOT NULL,
  `Course_ID` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Executor` varchar(45) NOT NULL,
  `Reason` varchar(45) NOT NULL,
  PRIMARY KEY (`PK`),
  KEY `pk_uin_idx` (`UIN`),
  KEY `course_pk1_idx` (`Course_ID`),
  CONSTRAINT `course_pk1` FOREIGN KEY (`Course_ID`) REFERENCES `course` (`Course_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pk_uin` FOREIGN KEY (`UIN`) REFERENCES `student` (`UIN`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dropped_student`
--

LOCK TABLES `dropped_student` WRITE;
/*!40000 ALTER TABLE `dropped_student` DISABLE KEYS */;
INSERT INTO `dropped_student` VALUES (14,685650916,'CS401','685650916','\'Too Hard\''),(15,622940104,'CS480','622940104','\'Too Hard\''),(16,583940188,'CS480','583940188','\'Too Hard\''),(17,527976125,'CS480','527976125','\'Too Hard\''),(18,622940104,'IE342','622940104','\'Too Hard\''),(19,230319323,'CS261','230319323','Too Hard');
/*!40000 ALTER TABLE `dropped_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `final_grade`
--

DROP TABLE IF EXISTS `final_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `final_grade` (
  `PK` int NOT NULL AUTO_INCREMENT,
  `UIN` int NOT NULL,
  `Course` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `Grade` varchar(45) NOT NULL,
  PRIMARY KEY (`PK`),
  KEY `UIN_idx` (`UIN`),
  KEY `Course_idx` (`Course`),
  KEY `coursef_idx` (`Course`),
  CONSTRAINT `pk_course` FOREIGN KEY (`Course`) REFERENCES `course` (`Course_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `UIN` FOREIGN KEY (`UIN`) REFERENCES `student` (`UIN`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `final_grade`
--

LOCK TABLES `final_grade` WRITE;
/*!40000 ALTER TABLE `final_grade` DISABLE KEYS */;
INSERT INTO `final_grade` VALUES (22,107153854,'CS211','A'),(23,685650916,'CS401','W'),(24,657325069,'CS401','B'),(25,625349403,'CS401','C'),(26,590887133,'CS401','B'),(27,548897540,'CS401','C'),(28,527976125,'CS401','B'),(29,522051561,'CS401','C'),(30,271832772,'CS401','A'),(31,107153854,'CS401','B'),(32,117131003,'CS480','A'),(33,622940104,'CS480','W'),(34,940078946,'CS480','A'),(35,922793354,'CS480','B'),(36,414621580,'CS480','A'),(37,823307933,'CS480','A'),(38,685650916,'CS480','A'),(39,675607108,'CS480','B'),(40,657325069,'CS480','A'),(41,641988691,'CS480','C'),(42,625349403,'CS480','A'),(43,622940104,'CS480','A'),(44,590887133,'CS480','B'),(45,583940188,'CS480','A'),(46,580245421,'CS480','A'),(47,548897540,'CS480','C'),(48,527976125,'CS480','W'),(49,522051561,'CS480','A'),(50,499938319,'CS480','C'),(51,473373142,'CS480','C'),(52,275584923,'CS480','A'),(53,271832772,'CS480','C'),(54,271459385,'CS480','A'),(55,177477368,'CS480','A'),(56,107153854,'CS480','B'),(57,675607108,'IE342','C'),(58,641988691,'IE342','A'),(59,622940104,'IE342','W'),(60,583940188,'IE342','B'),(61,580245421,'IE342','C'),(62,499938319,'IE342','A'),(63,473373142,'IE342','B'),(64,275584923,'IE342','A'),(65,271459385,'IE342','A'),(66,177477368,'IE342','A'),(67,473373142,'CS111','A'),(68,473373142,'CS141','A'),(69,473373142,'CS251','A');
/*!40000 ALTER TABLE `final_grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `homework`
--

DROP TABLE IF EXISTS `homework`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `homework` (
  `Homework_ID` int NOT NULL AUTO_INCREMENT,
  `Total_points` int NOT NULL,
  `Title` varchar(45) DEFAULT NULL,
  `Due_Date` varchar(45) NOT NULL,
  `Status` varchar(45) NOT NULL DEFAULT 'In Progress',
  `Course_ID` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`Homework_ID`),
  KEY `fk_course_idx` (`Course_ID`),
  CONSTRAINT `fk_course` FOREIGN KEY (`Course_ID`) REFERENCES `course` (`Course_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `homework`
--

LOCK TABLES `homework` WRITE;
/*!40000 ALTER TABLE `homework` DISABLE KEYS */;
INSERT INTO `homework` VALUES (9,100,'HW1','2022-08-02 11:59:59','In Progress','ENGL160');
/*!40000 ALTER TABLE `homework` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `UIN` int NOT NULL,
  `First_name` varchar(45) NOT NULL,
  `Middle_name` varchar(45) DEFAULT NULL,
  `Last_name` varchar(45) NOT NULL,
  `Gender` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`UIN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professor`
--

LOCK TABLES `professor` WRITE;
/*!40000 ALTER TABLE `professor` DISABLE KEYS */;
INSERT INTO `professor` VALUES (140143164,'Catrin','','Gaines','Female','#Professor1','2022-08-03 15:37:26','2022-08-03 15:37:26'),(152298065,'Tanner','Robert','Short','Male','#Professor1','2022-08-03 15:36:46','2022-08-03 15:36:46'),(156281418,'Kamila','','Sharpe','Female','#Professor1','2022-08-03 15:35:32','2022-08-03 15:35:32'),(174790226,'Sila','','Leblanc','Female','#Professor1','2022-08-03 15:37:42','2022-08-03 15:37:42'),(184211405,'Lacey','Mae','Carter','Female','Qwerty1!!','2022-08-03 15:38:01','2022-08-03 15:38:01'),(275584923,'Richard','','Bush','Male','Qwerty1234!@#$','2022-08-03 16:12:10','2022-08-03 16:12:10'),(354072955,'Simran','','Ridley','Male','Qwerty1!!','2022-08-03 15:36:14','2022-08-03 15:36:14'),(419647062,'Liya','Ruth','Deleon','Female','#Professor1','2022-08-03 15:36:13','2022-08-03 15:36:26'),(525628543,'Viktor','','Gillespie','Male','Qwerty1!!','2022-08-03 15:39:00','2022-08-03 15:39:00'),(572021685,'Alessia','','Fulton','Female','#Professor1','2022-08-03 15:35:01','2022-08-03 15:35:09'),(586600833,'Kynan','','Wallace','Male','Qwerty1!!','2022-08-03 15:37:25','2022-08-03 15:37:25'),(590074495,'Edwin','','Holmes','Male','Qwerty1!!','2022-08-03 15:44:34','2022-08-03 15:44:34'),(657587633,'Nimrah','','Clarke','Male','Qwerty1!!','2022-08-03 15:44:07','2022-08-03 15:44:07'),(658225785,'Azra','','Huff','Male','Qwerty1!!','2022-08-03 15:37:03','2022-08-03 15:37:03'),(742670419,'Aarush','','Schmidt','Male','Qwerty1!!','2022-08-03 15:39:36','2022-08-03 15:39:36'),(833278622,'Hilbert','','Lee','Male','#Professor1','2022-08-03 15:39:33','2022-08-03 15:39:33'),(836493990,'Tony','','Heart','Female','#Professor1','2022-08-03 15:39:14','2022-08-03 15:39:14'),(949055012,'Lily','','Selena','Female','#Professor1','2022-08-03 15:44:27','2022-08-03 15:44:27'),(986226382,'Tom','','Bass','Male','#Professor1','2022-08-03 15:37:08','2022-08-03 15:37:08');
/*!40000 ALTER TABLE `professor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `register_key`
--

DROP TABLE IF EXISTS `register_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `register_key` (
  `reg_key` varchar(45) NOT NULL,
  `Generated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Used_time` varchar(45) NOT NULL DEFAULT '1983-01-01 00:00:01',
  PRIMARY KEY (`reg_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `register_key`
--

LOCK TABLES `register_key` WRITE;
/*!40000 ALTER TABLE `register_key` DISABLE KEYS */;
INSERT INTO `register_key` VALUES ('0PSI1XWSQ','2022-08-03 06:47:37','1983-01-01 00:00:01'),('235WQ8191','2022-08-03 16:03:27','2022-08-03 11:04:40'),('459854O64','2022-08-03 06:47:37','1983-01-01 00:00:01'),('7FGX3D4H0','2022-08-03 06:47:37','1983-01-01 00:00:01'),('7YWHR88J5','2022-08-03 06:47:37','1983-01-01 00:00:01'),('8B5XFDWAY','2022-08-02 19:01:53','2022-08-02 14:02:48'),('H936LS9L2','2022-08-03 06:47:37','1983-01-01 00:00:01'),('KF3MGRHX2','2022-08-03 06:47:37','1983-01-01 00:00:01'),('KGN3V17C4','2022-08-02 19:11:02','2022-08-02 14:11:57'),('NMZAP9Z45','2022-08-03 06:47:37','1983-01-01 00:00:01'),('R7017N5I6','2022-08-03 06:47:37','1983-01-01 00:00:01'),('R84H4F3KW','2022-08-02 19:21:37','1983-01-01 00:00:01'),('UP5BJ0VBV','2022-08-03 06:47:37','1983-01-01 00:00:01'),('UUFLEKO79','2022-08-03 06:47:37','1983-01-01 00:00:01');
/*!40000 ALTER TABLE `register_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `UIN` int NOT NULL,
  `First_name` varchar(45) NOT NULL,
  `Middle_name` varchar(45) DEFAULT NULL,
  `Last_name` varchar(45) NOT NULL,
  `Gender` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`UIN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (107153854,'Antonio','','Burrow','Other','Qwerty1234!@#$','2022-08-02 19:42:59','2022-08-02 20:26:39'),(117131003,'Hailie','','Conrad','Female','Qwerty1234!@#$','2022-08-02 19:44:34','2022-08-02 20:26:39'),(177477368,'Belle','','Griffith','Female','Qwerty1234!@#$','2022-08-02 19:55:55','2022-08-02 20:26:39'),(208580373,'Victor','','Clegg','Male','Qwerty1234!@#$','2022-08-02 20:12:21','2022-08-02 20:26:39'),(230319323,'Haoji','','Ni','Male','Qwerty1234!@#$','2022-08-03 16:04:40','2022-08-03 16:09:04'),(271459385,'Kerrie','','Li','Female','Qwerty1234!@#$','2022-08-02 19:42:31','2022-08-02 20:26:39'),(271832772,'Bob','','Phil','Male','Qwerty1234!@#$','2022-08-02 23:33:12','2022-08-03 15:05:07'),(275584923,'Richard','','Bush','Male','Qwerty1234!@#$','2022-08-02 19:53:20','2022-08-02 20:26:39'),(289436546,'Dani','','Smith','Female','Qwerty1234!@#$','2022-08-02 20:15:28','2022-08-02 20:26:40'),(318294684,'Joseff','','Lee','Male','Qwerty1234!@#$','2022-08-02 20:12:43','2022-08-02 20:26:40'),(323401157,'Arlena','','Mccabe','Female','Qwerty1234!@#$','2022-08-02 19:53:42','2022-08-02 20:26:40'),(349698359,'Lola','','Lozano','Female','Qwerty1234!@#$','2022-08-02 20:01:07','2022-08-02 20:26:40'),(396697133,'Ava','Grace','Turnbell','Female','Qwerty1234!@#$','2022-08-02 20:15:05','2022-08-02 20:26:40'),(407992026,'Orlaith','','Plummer','Male','Qwerty1234!@#$','2022-08-02 19:48:17','2022-08-02 20:26:40'),(414621580,'Harriet','','Liu','Female','Qwerty1234!@#$','2022-08-02 20:13:22','2022-08-02 20:26:40'),(447801445,'Ryan','','Smythe','Other','Qwerty1234!@#$','2022-08-02 23:32:59','2022-08-03 15:04:23'),(451963424,'Jozef','','Owens','Male','Qwerty1234!@#$','2022-08-02 19:50:11','2022-08-02 20:26:40'),(473373142,'Fred','','Booker','Male','Qwerty1234!@#$','2022-08-02 19:50:54','2022-08-02 20:26:40'),(499938319,'Kobe','Trejo','Callahan','Male','Qwerty1234!@#$','2022-08-02 20:16:04','2022-08-02 20:26:40'),(511422111,'Vlad','','Muradov','Male','Qwerty1234!@#$','2022-08-03 16:10:26','2022-08-03 16:10:26'),(522051561,'Klara','','Wang','Female','Qwerty1234!@#$','2022-08-02 19:44:10','2022-08-02 20:26:40'),(527976125,'Zahran','','Crossley','Male','Qwerty1234!@#$','2022-08-02 20:15:43','2022-08-02 20:26:40'),(548897540,'Byron','','Haley','Male','Qwerty1234!@#$','2022-08-02 20:09:40','2022-08-02 20:26:40'),(580245421,'Alexis','','Mccaffrey','Female','Qwerty1234!@#$','2022-08-02 20:17:12','2022-08-02 20:26:40'),(583940188,'Logan','','Holcomb','Male','Qwerty1234!@#$','2022-08-02 20:14:26','2022-08-02 20:26:40'),(590887133,'Marin','','Female','Female','Qwerty1234!@#$','2022-08-02 20:42:19','2022-08-02 20:42:19'),(622940104,'Crystal','','Villa','Female','Qwerty1234!@#$','2022-08-02 20:11:46','2022-08-02 20:26:40'),(625349403,'Lucas','','Owens','Male','Qwerty1234!@#$','2022-08-02 19:54:12','2022-08-02 20:26:40'),(641988691,'Suleman','','Sherman','Male','Qwerty1234!@#$','2022-08-02 19:43:46','2022-08-02 20:26:41'),(657325069,'Lukas','','Dickson','Male','Qwerty1234!@#$','2022-08-02 19:52:58','2022-08-02 20:26:41'),(675607108,'Bernard','Jem','Mohamed','Male','Qwerty1234!@#$','2022-08-02 19:54:43','2022-08-02 20:26:41'),(685650916,'Suranne','','Wilkes','Other','Qwerty1234!@#$','2022-08-02 19:48:37','2022-08-02 20:26:41'),(726647819,'Loren','','Cornish','Female','Qwerty1234!@#$','2022-08-02 19:51:08','2022-08-02 20:26:41'),(739628870,'Perry','Isra','Wheeler','Male','Qwerty1234!@#$','2022-08-02 20:18:07','2022-08-02 20:26:41'),(753125331,'Pollyanna','','Walters','Female','Qwerty1234!@#$','2022-08-02 20:00:08','2022-08-02 20:26:41'),(774022500,'Rudra','','Appleton','Female','Qwerty1234!@#$','2022-08-02 20:16:58','2022-08-02 20:26:41'),(807401043,'Omer','','Sierra','Male','Qwerty1234!@#$','2022-08-02 20:13:52','2022-08-02 20:26:41'),(812497297,'Alena','','Pitts','Female','Qwerty1234!@#$','2022-08-02 20:17:30','2022-08-02 20:26:41'),(823307933,'Reja','','White','Female','Qwerty1234!@#$','2022-08-02 20:02:57','2022-08-02 20:26:41'),(827075808,'Saad','','Johnson','Male','Qwerty1234!@#$','2022-08-02 19:51:24','2022-08-02 20:26:41'),(864103033,'Hussein','','Khan','Male','Qwerty1234!@#$','2022-08-02 19:46:18','2022-08-02 20:26:41'),(865432707,'Sarina','Agatha','Ray','Female','Qwerty1234!@#$','2022-08-02 19:45:21','2022-08-02 20:26:41'),(907465201,'Cassius','','Rios','Male','Qwerty1234!@#$','2022-08-02 20:13:38','2022-08-02 20:26:41'),(916945707,'Janae','','Ray','Female','Qwerty1234!@#$','2022-08-02 20:14:41','2022-08-02 20:26:41'),(921865656,'Nial','','Bourne','Other','Qwerty1234!@#$','2022-08-02 19:50:37','2022-08-02 20:26:41'),(922793354,'Raiden','','Cannon','Male','Qwerty1234!@#$','2022-08-02 19:42:09','2022-08-02 20:26:42'),(940078946,'Acacia','','Archer','Female','Qwerty1234!@#$','2022-08-02 19:55:23','2022-08-02 20:26:42'),(980264817,'Lacy','','Giles','Female','Qwerty1234!@#$','2022-08-02 20:16:38','2022-08-02 20:26:42'),(987913805,'Ian','','Hahn','Male','Qwerty1234!@#$','2022-08-02 20:14:07','2022-08-02 20:26:42');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-08-03 11:37:51
