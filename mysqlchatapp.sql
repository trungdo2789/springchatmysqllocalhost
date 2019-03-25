-- MySQL dump 10.13  Distrib 8.0.14, for Win64 (x86_64)
--
-- Host: localhost    Database: chatapp
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `chat` (
  `chatid` int(11) NOT NULL AUTO_INCREMENT,
  `isgroup` bit(1) DEFAULT b'0',
  `name` varchar(150) DEFAULT NULL,
  `createday` datetime DEFAULT CURRENT_TIMESTAMP,
  `lasttime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`chatid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (12,_binary '\0',NULL,'2019-02-22 21:04:58','2019-03-19 02:33:05'),(13,_binary '','group1','2019-02-23 15:16:48','2019-02-24 12:02:19'),(14,_binary '','hellogr','2019-03-15 09:03:51','2019-03-15 09:03:51'),(20,_binary '\0',NULL,'2019-03-15 09:56:03','2019-03-18 10:05:45'),(21,_binary '\0',NULL,'2019-03-18 23:23:51',NULL),(22,_binary '\0',NULL,'2019-03-19 00:31:05','2019-03-20 09:42:56'),(23,_binary '\0',NULL,'2019-03-19 00:33:15','2019-03-19 00:34:17'),(24,_binary '\0',NULL,'2019-03-19 00:37:02','2019-03-19 00:38:15'),(25,_binary '\0',NULL,'2019-03-19 00:39:26','2019-03-19 00:39:43'),(26,_binary '','group1','2019-03-19 01:17:33','2019-03-20 12:33:20'),(27,_binary '','22222222222','2019-03-19 11:06:33','2019-03-20 12:36:30'),(28,_binary '','i am supper man and i can fly','2019-03-19 12:01:35','2019-03-21 23:50:49'),(29,_binary '\0',NULL,'2019-03-20 10:10:26','2019-03-20 10:11:57'),(30,_binary '','12112','2019-03-20 10:13:55','2019-03-20 10:14:45'),(31,_binary '','sass=','2019-03-21 18:09:08','2019-03-21 18:09:08');
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `message` (
  `messid` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `sendtime` datetime DEFAULT CURRENT_TIMESTAMP,
  `chatid` int(11) DEFAULT NULL,
  `sender` int(11) DEFAULT NULL,
  PRIMARY KEY (`messid`),
  KEY `FKnoyxst79gy5q47np9fp7nxh4x` (`chatid`),
  KEY `FK6s5abaofhkmqe9fhff327g6we` (`sender`),
  CONSTRAINT `FK6s5abaofhkmqe9fhff327g6we` FOREIGN KEY (`sender`) REFERENCES `userinfo` (`userid`),
  CONSTRAINT `FKnoyxst79gy5q47np9fp7nxh4x` FOREIGN KEY (`chatid`) REFERENCES `chat` (`chatid`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (8,'ms1','2019-02-24 03:09:22',13,12),(9,'ms1','2019-02-24 03:10:22',12,12),(10,'ms12','2019-02-24 12:07:39',12,12),(11,'hello','2019-03-17 23:31:44',12,12),(12,'hello','2019-03-17 23:35:54',12,12),(13,'hello','2019-03-17 23:39:24',12,12),(14,'hello1','2019-03-17 23:40:47',12,12),(15,'hello1','2019-03-18 00:38:39',12,12),(16,'hello1','2019-03-18 00:40:02',12,12),(17,'hello1','2019-03-18 01:04:02',12,11),(18,'hello1','2019-03-18 01:04:53',12,11),(19,'hello1','2019-03-18 01:06:11',12,11),(20,'hello1','2019-03-18 01:06:18',12,11),(21,'hello1','2019-03-18 10:05:45',20,12),(22,'hello12','2019-03-18 10:46:55',12,12),(23,'hello12','2019-03-18 10:49:34',12,12),(24,'hello123','2019-03-18 10:51:07',12,12),(25,'hello123','2019-03-18 10:53:15',12,12),(26,'hello1234','2019-03-18 10:53:27',12,12),(27,'hello12345','2019-03-18 11:02:21',12,12),(28,'121','2019-03-18 11:16:42',12,11),(29,'dsad','2019-03-18 11:16:48',12,12),(30,'chào','2019-03-18 11:17:20',12,12),(31,':>>>>>>>>>>>>','2019-03-18 11:17:29',12,11),(32,'dsad','2019-03-18 11:27:43',12,11),(33,'sad','2019-03-18 11:28:50',12,11),(34,'2','2019-03-18 11:28:52',12,11),(35,'haha','2019-03-18 11:28:55',12,11),(36,'dsa','2019-03-18 11:29:54',12,11),(37,'dsad','2019-03-18 11:30:04',12,11),(38,'a','2019-03-18 11:30:26',12,12),(39,'2','2019-03-18 11:34:30',12,11),(40,'sda','2019-03-18 11:35:45',12,11),(41,'sad','2019-03-18 11:36:44',12,11),(42,'dsa','2019-03-18 11:37:18',12,11),(43,'dsa','2019-03-18 11:39:11',12,11),(44,'hahahah','2019-03-18 11:46:33',12,12),(45,'hahah','2019-03-18 11:46:43',12,12),(46,'helleads','2019-03-18 21:54:20',12,11),(47,'22222222222222','2019-03-18 21:54:33',12,12),(48,'21','2019-03-18 21:54:37',12,11),(49,'sda','2019-03-19 00:32:04',22,16),(50,'hello','2019-03-19 00:33:23',23,11),(51,'dsa','2019-03-19 00:34:17',23,11),(52,'2','2019-03-19 00:37:14',24,17),(53,'asds','2019-03-19 00:38:15',24,17),(54,'2ds','2019-03-19 00:39:43',25,18),(55,'2','2019-03-19 01:17:42',26,12),(56,'3','2019-03-19 01:17:45',26,12),(57,'2','2019-03-19 02:33:05',12,12),(58,'22222222222222222222222222','2019-03-19 12:00:53',27,12),(59,'1','2019-03-19 12:17:47',28,12),(60,'222222222222','2019-03-19 23:28:25',22,16),(61,'213','2019-03-19 23:33:11',22,12),(62,'213','2019-03-19 23:34:07',22,12),(63,'1121','2019-03-19 23:34:48',22,16),(64,'111','2019-03-19 23:37:17',22,12),(65,'2222222','2019-03-19 23:37:24',22,12),(66,'2','2019-03-19 23:37:38',22,12),(67,'2','2019-03-19 23:37:51',22,12),(68,'1','2019-03-19 23:38:17',22,12),(69,'3','2019-03-19 23:41:32',22,12),(70,'2','2019-03-20 09:05:22',22,12),(71,'33333333333333333','2019-03-20 09:11:13',22,12),(72,'111111111111111','2019-03-20 09:11:54',22,16),(73,'dsa','2019-03-20 09:24:47',22,16),(74,'1111','2019-03-20 09:28:51',22,16),(75,'hello','2019-03-20 09:33:13',22,16),(76,'11111111','2019-03-20 09:33:22',22,16),(77,'2','2019-03-20 09:33:42',22,16),(78,':v','2019-03-20 09:33:55',22,12),(79,'333333','2019-03-20 09:34:49',22,16),(80,'44444444444','2019-03-20 09:35:22',22,16),(81,'111111111','2019-03-20 09:36:07',22,16),(82,'1','2019-03-20 09:39:12',22,16),(83,'222','2019-03-20 09:40:48',22,12),(84,'1111','2019-03-20 09:41:58',22,16),(85,'2222','2019-03-20 09:42:13',22,16),(86,'hello','2019-03-20 09:42:56',22,16),(87,'11111111111','2019-03-20 09:43:24',26,16),(88,'helllllllllllllllllllllo','2019-03-20 09:44:01',26,16),(89,'1111111111111111','2019-03-20 09:44:45',26,12),(90,'1','2019-03-20 09:45:38',28,12),(91,'2','2019-03-20 09:45:49',28,12),(92,'2','2019-03-20 10:09:02',28,12),(93,'1111111','2019-03-20 10:11:57',29,16),(94,'2211','2019-03-20 10:14:45',30,14),(95,'2222222222222222222','2019-03-20 12:33:20',26,12),(96,'adsfadsgdfsa','2019-03-20 12:36:30',27,12),(97,'1111111','2019-03-20 12:39:03',28,12),(98,'1111111111111','2019-03-21 22:49:58',28,14),(99,'2222222222','2019-03-21 23:01:24',28,11),(100,'hello ae','2019-03-21 23:50:49',28,12);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_chat`
--

DROP TABLE IF EXISTS `user_has_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_has_chat` (
  `userid` int(11) NOT NULL,
  `chatid` int(11) NOT NULL,
  PRIMARY KEY (`userid`,`chatid`),
  KEY `FK2pdeclw5mdxl723hj5jv6mltp` (`chatid`),
  CONSTRAINT `FK2pdeclw5mdxl723hj5jv6mltp` FOREIGN KEY (`chatid`) REFERENCES `chat` (`chatid`),
  CONSTRAINT `FKnq9fvwvcqupglrraj1lj9hnwo` FOREIGN KEY (`userid`) REFERENCES `userinfo` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_chat`
--

LOCK TABLES `user_has_chat` WRITE;
/*!40000 ALTER TABLE `user_has_chat` DISABLE KEYS */;
INSERT INTO `user_has_chat` VALUES (11,12),(12,12),(12,13),(12,14),(14,14),(12,20),(14,20),(11,21),(14,21),(12,22),(16,22),(11,23),(16,23),(16,24),(17,24),(16,25),(18,25),(11,26),(12,26),(16,26),(12,27),(17,27),(11,28),(12,28),(14,28),(16,28),(14,29),(16,29),(14,30),(16,30),(12,31);
/*!40000 ALTER TABLE `user_has_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_message`
--

DROP TABLE IF EXISTS `user_has_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_has_message` (
  `messid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `seentime` datetime DEFAULT NULL,
  PRIMARY KEY (`messid`,`userid`),
  KEY `FKff5qkn9kvl98yrypl45vkwywv` (`userid`),
  CONSTRAINT `FKff5qkn9kvl98yrypl45vkwywv` FOREIGN KEY (`userid`) REFERENCES `userinfo` (`userid`),
  CONSTRAINT `FKp88mkwn6syuchu85nsqxxeiog` FOREIGN KEY (`messid`) REFERENCES `message` (`messid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_message`
--

LOCK TABLES `user_has_message` WRITE;
/*!40000 ALTER TABLE `user_has_message` DISABLE KEYS */;
INSERT INTO `user_has_message` VALUES (8,12,'2019-02-24 03:09:22'),(9,11,NULL),(9,12,'2019-02-24 03:10:22'),(10,11,NULL),(10,12,'2019-02-24 12:07:39'),(11,11,NULL),(11,12,'2019-03-17 23:31:45'),(12,11,NULL),(12,12,'2019-03-17 23:35:55'),(13,11,NULL),(13,12,'2019-03-17 23:39:24'),(14,11,NULL),(14,12,'2019-03-17 23:40:47'),(15,11,NULL),(15,12,'2019-03-18 00:38:39'),(16,11,NULL),(16,12,'2019-03-18 00:40:02'),(17,11,'2019-03-18 01:04:03'),(17,12,NULL),(18,11,'2019-03-18 01:04:53'),(18,12,NULL),(19,11,'2019-03-18 01:06:11'),(19,12,NULL),(20,11,'2019-03-18 01:06:18'),(20,12,NULL),(21,12,'2019-03-18 10:05:45'),(21,14,NULL),(22,11,NULL),(22,12,'2019-03-18 10:46:55'),(23,11,NULL),(23,12,'2019-03-18 10:49:34'),(24,11,NULL),(24,12,'2019-03-18 10:51:07'),(25,11,NULL),(25,12,'2019-03-18 10:53:15'),(26,11,NULL),(26,12,'2019-03-18 10:53:28'),(27,11,NULL),(27,12,'2019-03-18 11:02:21'),(28,11,'2019-03-18 11:16:42'),(28,12,NULL),(29,11,NULL),(29,12,'2019-03-18 11:16:48'),(30,11,NULL),(30,12,'2019-03-18 11:17:20'),(31,11,'2019-03-18 11:17:29'),(31,12,NULL),(32,11,'2019-03-18 11:27:43'),(32,12,NULL),(33,11,'2019-03-18 11:28:50'),(33,12,NULL),(34,11,'2019-03-18 11:28:53'),(34,12,NULL),(35,11,'2019-03-18 11:28:55'),(35,12,NULL),(36,11,'2019-03-18 11:29:54'),(36,12,NULL),(37,11,'2019-03-18 11:30:04'),(37,12,NULL),(38,11,NULL),(38,12,'2019-03-18 11:30:26'),(39,11,'2019-03-18 11:34:30'),(39,12,NULL),(40,11,'2019-03-18 11:35:45'),(40,12,NULL),(41,11,'2019-03-18 11:36:44'),(41,12,NULL),(42,11,'2019-03-18 11:37:18'),(42,12,NULL),(43,11,'2019-03-18 11:39:11'),(43,12,NULL),(44,11,NULL),(44,12,'2019-03-18 11:46:33'),(45,11,NULL),(45,12,'2019-03-18 11:46:43'),(46,11,'2019-03-18 21:54:20'),(46,12,NULL),(47,11,NULL),(47,12,'2019-03-18 21:54:33'),(48,11,'2019-03-18 21:54:37'),(48,12,NULL),(49,12,NULL),(49,16,'2019-03-19 00:32:05'),(50,11,'2019-03-19 00:33:23'),(50,16,NULL),(51,11,'2019-03-19 00:34:17'),(51,16,NULL),(52,16,NULL),(52,17,'2019-03-19 00:37:14'),(53,16,NULL),(53,17,'2019-03-19 00:38:15'),(54,16,NULL),(54,18,'2019-03-19 00:39:43'),(55,12,'2019-03-19 01:17:42'),(56,12,'2019-03-19 01:17:45'),(57,11,NULL),(57,12,'2019-03-19 02:33:05'),(58,12,'2019-03-19 12:00:53'),(58,17,NULL),(59,12,'2019-03-19 12:17:47'),(60,12,NULL),(60,16,'2019-03-19 23:28:25'),(61,12,'2019-03-19 23:33:11'),(61,16,NULL),(62,12,'2019-03-19 23:34:07'),(62,16,NULL),(63,12,NULL),(63,16,'2019-03-19 23:34:48'),(64,12,'2019-03-19 23:37:17'),(64,16,NULL),(65,12,'2019-03-19 23:37:24'),(65,16,NULL),(66,12,'2019-03-19 23:37:38'),(66,16,NULL),(67,12,'2019-03-19 23:37:51'),(67,16,NULL),(68,12,'2019-03-19 23:38:17'),(68,16,NULL),(69,12,'2019-03-19 23:41:32'),(69,16,NULL),(70,12,'2019-03-20 09:05:22'),(70,16,NULL),(71,12,'2019-03-20 09:11:13'),(71,16,NULL),(72,12,NULL),(72,16,'2019-03-20 09:11:54'),(73,12,NULL),(73,16,'2019-03-20 09:24:47'),(74,12,NULL),(74,16,'2019-03-20 09:28:51'),(75,12,NULL),(75,16,'2019-03-20 09:33:13'),(76,12,NULL),(76,16,'2019-03-20 09:33:22'),(77,12,NULL),(77,16,'2019-03-20 09:33:42'),(78,12,'2019-03-20 09:33:55'),(78,16,NULL),(79,12,NULL),(79,16,'2019-03-20 09:34:49'),(80,12,NULL),(80,16,'2019-03-20 09:35:22'),(81,12,NULL),(81,16,'2019-03-20 09:36:07'),(82,12,NULL),(82,16,'2019-03-20 09:39:12'),(83,12,'2019-03-20 09:40:48'),(83,16,NULL),(84,12,NULL),(84,16,'2019-03-20 09:41:58'),(85,12,NULL),(85,16,'2019-03-20 09:42:13'),(86,12,NULL),(86,16,'2019-03-21 22:15:49'),(87,11,NULL),(87,12,NULL),(87,16,'2019-03-20 09:43:24'),(88,11,NULL),(88,12,NULL),(88,16,'2019-03-20 09:44:01'),(89,11,NULL),(89,12,'2019-03-20 09:44:45'),(89,16,NULL),(90,12,'2019-03-20 09:45:38'),(91,12,'2019-03-20 09:45:49'),(91,16,NULL),(92,12,'2019-03-20 10:09:02'),(92,14,NULL),(92,16,NULL),(93,14,NULL),(93,16,'2019-03-21 22:15:42'),(94,14,'2019-03-20 10:14:45'),(95,11,NULL),(95,12,'2019-03-21 22:16:22'),(95,16,NULL),(96,12,'2019-03-20 12:36:30'),(96,17,NULL),(97,12,'2019-03-21 22:14:17'),(97,14,NULL),(97,16,NULL),(98,11,NULL),(98,12,NULL),(98,14,'2019-03-21 22:49:58'),(98,16,NULL),(99,11,'2019-03-21 23:01:24'),(99,12,NULL),(99,14,NULL),(99,16,NULL),(100,11,NULL),(100,12,'2019-03-21 23:50:49'),(100,14,NULL),(100,16,NULL);
/*!40000 ALTER TABLE `user_has_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userinfo`
--

DROP TABLE IF EXISTS `userinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `userinfo` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `isonline` bit(1) DEFAULT b'0',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avatarurl` varchar(155) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `UK8h620irpir8kcurgsdkhns8lt` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userinfo`
--

LOCK TABLES `userinfo` WRITE;
/*!40000 ALTER TABLE `userinfo` DISABLE KEYS */;
INSERT INTO `userinfo` VALUES (11,_binary '\0','$2a$10$8SyAvvRZ2D9nPFqtNnTxH.rq0m0sTSP.NgU8dqDf1t25JT3YzS062','test1211','https://drive.google.com/uc?id=1fRiNrR5_U3TPz7Iaiy3yPu8c_a3AS6Cm&export=download'),(12,_binary '','$2a$10$sk0eokH3esdvlO1W8NSFS.WvNgKRQAGPku6JUP4ftsV2yXSExrqq2','dttr','/downloadFile/fb_page_avatar___happy_by_muller_saru-d58lfe4.png'),(14,_binary '\0','$2a$10$HfuSnooQ3SeIkG98FQzypeb2berJ3JjOKCiISEhpqeCvyiFRH1Yai','dttr1','https://drive.google.com/uc?id=19BCNy9paxTjU6h6VAqNK1nREMCVg4Wky&export=download'),(15,_binary '\0','$2a$10$hOag6y8bDsyre.aFA85TSuEEC5.dkdSs1LtU.aH7Vyxj7xETPNnVK','12123',NULL),(16,_binary '\0','$2a$10$HJICoVii4WI4Pbehsjsuw..TW9iuusodXNZuBFcGurhe9TwddkLH2','socnau','https://drive.google.com/uc?id=1MIFm-O3g85kRkB5TT-b3SL28JEwxJjWb&export=download'),(17,_binary '\0','$2a$10$TAIMDTfmfAvwqFPWZbaI..qiVLOkKSHBpl8.qZsZBljenMTWhwyaa','socnau1',NULL),(18,_binary '\0','$2a$10$pIAa5knRLF0QAlhPQkqwB.A0njWvHYIo2mJqW/79Mf0qMRtZPJOlO','socnau2',NULL),(19,_binary '\0','$2a$10$bwmnAUVh47yKIAElNjtpFOLOBo8A/EQMehZ7giM1pe9p3wIadtFza','socnau123',NULL),(20,_binary '\0','$2a$10$0gF0BI1aSZXV7a3Wz5JVgegwmVFdZAvNTbQTzZClcBe/tjmz7TOVS','socnau1212',NULL);
/*!40000 ALTER TABLE `userinfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-26  0:06:55
