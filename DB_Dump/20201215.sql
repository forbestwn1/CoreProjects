CREATE DATABASE  IF NOT EXISTS `nosliw` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `nosliw`;
-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: nosliw
-- ------------------------------------------------------
-- Server version	8.0.16

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
-- Table structure for table `converterjs`
--

DROP TABLE IF EXISTS `converterjs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `converterjs` (
  `id` varchar(20) NOT NULL,
  `value` varchar(2000) NOT NULL,
  `dataTypeName` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `converterjs`
--

LOCK TABLES `converterjs` WRITE;
/*!40000 ALTER TABLE `converterjs` DISABLE KEYS */;
INSERT INTO `converterjs` VALUES ('1580152069943','\nfunction (data, toDataType, reverse, context) {\n    if (reverse) {\n        return {dataTypeId: \"test.options;1.0.0\", value: {value: data.value}};\n    } else {\n        return {dataTypeId: \"test.string;1.0.0\", value: data.value.value};\n    }\n}\n','test.options;1.0.0'),('1580152069961','\nfunction (data, toDataType, context) {\n    return {dataTypeId: \"test.string;1.0.0\", value: \"http:\" + data.value};\n}\n','test.url;1.0.0'),('1580152069967','\nfunction (data, toDataType, context) {\n    \"conertTo\";\n}\n','test.url;1.1.0');
/*!40000 ALTER TABLE `converterjs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cronjob_state`
--

DROP TABLE IF EXISTS `cronjob_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `cronjob_state` (
  `ID` varchar(20) NOT NULL,
  `CRONJOBID` varchar(20) NOT NULL,
  `POLLTIME` datetime NOT NULL,
  `STATE` varchar(2000) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cronjob_state`
--

LOCK TABLES `cronjob_state` WRITE;
/*!40000 ALTER TABLE `cronjob_state` DISABLE KEYS */;
INSERT INTO `cronjob_state` VALUES ('1583530746581','1583530746580','2020-03-06 21:39:06','{\"flightNumber\":{\"dataTypeId\":\"test.string;1.0.0\",\n\"valueFormat\":\"JSON\",\n\"value\":\"1234\"\n},\n\"date\":{\"dataTypeId\":\"test.date;1.0.0\",\n\"valueFormat\":\"JSON\",\n\"value\":\"2020-03-06T21:39:06.553Z\"\n}\n}'),('1583557333478','1583557333477','2020-03-07 05:02:14','{\"flightNumber\":{\"dataTypeId\":\"test.string;1.0.0\",\n\"valueFormat\":\"JSON\",\n\"value\":\"1234\"\n},\n\"date\":{\"dataTypeId\":\"test.date;1.0.0\",\n\"valueFormat\":\"JSON\",\n\"value\":\"2020-03-07T05:02:13.450Z\"\n}\n}'),('1583557508206','1583557508205','2020-03-07 05:05:09','{\"flightNumber\":{\"dataTypeId\":\"test.string;1.0.0\",\n\"valueFormat\":\"JSON\",\n\"value\":\"1234\"\n},\n\"date\":{\"dataTypeId\":\"test.date;1.0.0\",\n\"valueFormat\":\"JSON\",\n\"value\":\"2020-03-07T05:05:08.180Z\"\n}\n}');
/*!40000 ALTER TABLE `cronjob_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datatype`
--

DROP TABLE IF EXISTS `datatype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `datatype` (
  `info` varchar(2000) DEFAULT NULL,
  `isComplex` varchar(20) DEFAULT NULL,
  `fullName` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `versionFullName` varchar(20) NOT NULL,
  `versionMajor` int(11) DEFAULT NULL,
  `versionMinor` int(11) DEFAULT NULL,
  `versionRevision` varchar(20) DEFAULT NULL,
  `parentsInfo` varchar(500) DEFAULT NULL,
  `linkedVersion` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`fullName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datatype`
--

LOCK TABLES `datatype` WRITE;
/*!40000 ALTER TABLE `datatype` DISABLE KEYS */;
INSERT INTO `datatype` VALUES ('{}','false','base.boolean;1.0.0','base.boolean','1.0.0',1,0,'0',NULL,NULL),('{}','false','base.data;1.0.0','base.data','1.0.0',1,0,'0',NULL,NULL),('{}','false','base.integer;1.0.0','base.integer','1.0.0',1,0,'0',NULL,NULL),('{}','false','base.number;1.0.0','base.number','1.0.0',1,0,'0',NULL,NULL),('{}','false','base.string;1.0.0','base.string','1.0.0',1,0,'0',NULL,NULL),('{}','false','common.entity;1.0.0','common.entity','1.0.0',1,0,'0',NULL,NULL),('{}','false','common.expression;1.0.0','common.expression','1.0.0',1,0,'0',NULL,NULL),('{}','false','common.geolocation;1.0.0','common.geolocation','1.0.0',1,0,'0',NULL,NULL),('{}','false','common.lengthunit;1.0.0','common.lengthunit','1.0.0',1,0,'0',NULL,NULL),('{}','false','core.array;1.0.0','core.array','1.0.0',1,0,'0',NULL,NULL),('{}','false','core.datatype;1.0.0','core.datatype','1.0.0',1,0,'0',NULL,NULL),('{}','false','core.distance;1.0.0','core.distance','1.0.0',1,0,'0',NULL,NULL),('{}','false','core.text;1.0.0','core.text','1.0.0',1,0,'0',NULL,NULL),('{}','false','core.url;1.0.0','core.url','1.0.0',1,0,'0','[\"base.string;1.0.0\",\"core.text;1.0.0\",\"base.boolean;1.0.0\"]',NULL),('{}','false','core.url;1.1.0','core.url','1.1.0',1,1,'0',NULL,'1.0.0'),('{}','true','test.array;1.0.0','test.array','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.boolean;1.0.0','test.boolean','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.dataTypeCriteria;1.0.0','test.dataTypeCriteria','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.date;1.0.0','test.date','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.distance;1.0.0','test.distance','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.expression;1.0.0','test.expression','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.float;1.0.0','test.float','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.geo;1.0.0','test.geo','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.integer;1.0.0','test.integer','1.0.0',1,0,'0',NULL,NULL),('{}','true','test.map;1.0.0','test.map','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.options;1.0.0','test.options','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL),('{}','false','test.parm;1.0.0','test.parm','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.price;1.0.0','test.price','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.string;1.0.0','test.string','1.0.0',1,0,'0',NULL,NULL),('{}','false','test.url;1.0.0','test.url','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL),('{}','false','test.url;1.1.0','test.url','1.1.0',1,1,'0',NULL,'1.0.0');
/*!40000 ALTER TABLE `datatype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datatypeoperation`
--

DROP TABLE IF EXISTS `datatypeoperation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `datatypeoperation` (
  `info` varchar(2000) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `operationId` varchar(20) NOT NULL,
  `source` varchar(100) DEFAULT NULL,
  `target` varchar(100) DEFAULT NULL,
  `path` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datatypeoperation`
--

LOCK TABLES `datatypeoperation` WRITE;
/*!40000 ALTER TABLE `datatypeoperation` DISABLE KEYS */;
INSERT INTO `datatypeoperation` VALUES (NULL,'add','normal','1582589950172','base.integer;1.0.0','base.integer;1.0.0','[]'),(NULL,'subString','normal','1582589950176','base.string;1.0.0','base.string;1.0.0','[]'),(NULL,'length','normal','1582589950181','base.string;1.0.0','base.string;1.0.0','[]'),(NULL,'new','new','1582589950168','core.distance;1.0.0','core.distance;1.0.0','[]'),(NULL,'isLonger','normal','1582589950156','core.distance;1.0.0','core.distance;1.0.0','[]'),(NULL,'getDistance','normal','1582589950160','core.distance;1.0.0','core.distance;1.0.0','[]'),(NULL,'convert','normal','1582589950164','core.distance;1.0.0','core.distance;1.0.0','[]'),(NULL,'text_normal1','normal','1582589950184','core.text;1.0.0','core.text;1.0.0','[]'),(NULL,'text_normal2','normal','1582589950188','core.text;1.0.0','core.text;1.0.0','[]'),(NULL,'text_normal3','normal','1582589950192','core.text;1.0.0','core.text;1.0.0','[]'),(NULL,'subString','normal','1582589950176','base.string;1.0.0','base.string;1.0.0','[]'),(NULL,'length','normal','1582589950181','base.string;1.0.0','base.string;1.0.0','[]'),(NULL,'text_normal1','normal','1582589950184','core.text;1.0.0','core.text;1.0.0','[]'),(NULL,'text_normal2','normal','1582589950188','core.text;1.0.0','core.text;1.0.0','[]'),(NULL,'text_normal3','normal','1582589950192','core.text;1.0.0','core.text;1.0.0','[]'),(NULL,'subString','normal','1582589950176','core.url;1.0.0','base.string;1.0.0','[\"parent|base.string;1.0.0\"]'),(NULL,'length','normal','1582589950181','core.url;1.0.0','base.string;1.0.0','[\"parent|base.string;1.0.0\"]'),(NULL,'text_normal1','normal','1582589950196','core.url;1.0.0','core.url;1.0.0','[]'),(NULL,'text_normal2','normal','1582589950188','core.url;1.0.0','core.text;1.0.0','[\"parent|core.text;1.0.0\"]'),(NULL,'text_normal3','normal','1582589950192','core.url;1.0.0','core.text;1.0.0','[\"parent|core.text;1.0.0\"]'),(NULL,'url_normal1','normal','1582589950200','core.url;1.0.0','core.url;1.0.0','[]'),(NULL,'url_normal2','normal','1582589950204','core.url;1.0.0','core.url;1.0.0','[]'),(NULL,'text_normal1','normal','1582589950196','core.url;1.0.0','core.url;1.0.0','[]'),(NULL,'url_normal1','normal','1582589950200','core.url;1.0.0','core.url;1.0.0','[]'),(NULL,'url_normal2','normal','1582589950204','core.url;1.0.0','core.url;1.0.0','[]'),(NULL,'new1','new','1582589950219','core.url;1.1.0','core.url;1.1.0','[]'),(NULL,'new2','new','1582589950222','core.url;1.1.0','core.url;1.1.0','[]'),(NULL,'text_normal1','normal','1582589950196','core.url;1.1.0','core.url;1.0.0','[\"linked|1.0.0\"]'),(NULL,'url_normal1','normal','1582589950200','core.url;1.1.0','core.url;1.0.0','[\"linked|1.0.0\"]'),(NULL,'url_normal2','normal','1582589950207','core.url;1.1.0','core.url;1.1.0','[]'),(NULL,'url_normal3','normal','1582589950211','core.url;1.1.0','core.url;1.1.0','[]'),(NULL,'url_withBase','normal','1582589950215','core.url;1.1.0','core.url;1.1.0','[]'),(NULL,'host1','normal','1582589950225','core.url;1.1.0','core.url;1.1.0','[]'),(NULL,'host2','normal','1582589950229','core.url;1.1.0','core.url;1.1.0','[]'),(NULL,'process','normal','1582589950233','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'getChildrenNames','normal','1582589950238','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'isAccessChildById','normal','1582589950241','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'addChild','normal','1582589950244','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'add','normal','1582589950249','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'removeChild','normal','1582589950253','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'length','normal','1582589950257','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'getChildData','normal','1582589950260','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'getChildDataByIndex','normal','1582589950264','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'emptyArray','normal','1582589950268','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'new','normal','1582589950270','test.array;1.0.0','test.array;1.0.0','[]'),(NULL,'opposite','normal','1582589950272','test.boolean;1.0.0','test.boolean;1.0.0','[]'),(NULL,'getChild','normal','1582589950275','test.dataTypeCriteria;1.0.0','test.dataTypeCriteria;1.0.0','[]'),(NULL,'getChildren','normal','1582589950279','test.dataTypeCriteria;1.0.0','test.dataTypeCriteria;1.0.0','[]'),(NULL,'addChild','normal','1582589950282','test.dataTypeCriteria;1.0.0','test.dataTypeCriteria;1.0.0','[]'),(NULL,'shorterThan','normal','1582589950287','test.distance;1.0.0','test.distance;1.0.0','[]'),(NULL,'outputCriteria','normal','1582589950291','test.expression;1.0.0','test.expression;1.0.0','[]'),(NULL,'outputCriteriaFromParmData','normal','1582589950295','test.expression;1.0.0','test.expression;1.0.0','[]'),(NULL,'execute','normal','1582589950299','test.expression;1.0.0','test.expression;1.0.0','[]'),(NULL,'distance','normal','1582589950303','test.geo;1.0.0','test.geo;1.0.0','[]'),(NULL,'add','normal','1582589950307','test.integer;1.0.0','test.integer;1.0.0','[]'),(NULL,'put','normal','1582589950311','test.map;1.0.0','test.map;1.0.0','[]'),(NULL,'new','normal','1582589950316','test.map;1.0.0','test.map;1.0.0','[]'),(NULL,'getChildrenNames','normal','1582589950318','test.map;1.0.0','test.map;1.0.0','[]'),(NULL,'isAccessChildById','normal','1582589950321','test.map;1.0.0','test.map;1.0.0','[]'),(NULL,'length','normal','1582589950324','test.map;1.0.0','test.map;1.0.0','[]'),(NULL,'getChildData','normal','1582589950327','test.map;1.0.0','test.map;1.0.0','[]'),(NULL,'setChildData','normal','1582589950331','test.map;1.0.0','test.map;1.0.0','[]'),(NULL,'emptyString','normal','1582589950345','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'isEmpty','normal','1582589950347','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'subString','normal','1582589950350','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'length','normal','1582589950355','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'notImpplementedOperation','normal','1582589950358','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'emptyString','normal','1582589950345','test.options;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'isEmpty','normal','1582589950347','test.options;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'subString','normal','1582589950350','test.options;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'length','normal','1582589950355','test.options;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'notImpplementedOperation','normal','1582589950358','test.options;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'all','normal','1582589950336','test.options;1.0.0','test.options;1.0.0','[]'),(NULL,'getValue','normal','1582589950339','test.parm;1.0.0','test.parm;1.0.0','[]'),(NULL,'getCriteria','normal','1582589950342','test.parm;1.0.0','test.parm;1.0.0','[]'),(NULL,'emptyString','normal','1582589950345','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'isEmpty','normal','1582589950347','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'subString','normal','1582589950350','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'length','normal','1582589950355','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'notImpplementedOperation','normal','1582589950358','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'emptyString','normal','1582589950345','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'isEmpty','normal','1582589950347','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'subString','normal','1582589950350','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'length','normal','1582589950355','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'notImpplementedOperation','normal','1582589950358','test.string;1.0.0','test.string;1.0.0','[]'),(NULL,'emptyString','normal','1582589950345','test.url;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'isEmpty','normal','1582589950347','test.url;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'subString','normal','1582589950350','test.url;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'length','normal','1582589950355','test.url;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'notImpplementedOperation','normal','1582589950358','test.url;1.0.0','test.string;1.0.0','[\"parent|test.string;1.0.0\"]'),(NULL,'text_normal1','normal','1582589950363','test.url;1.0.0','test.url;1.0.0','[]'),(NULL,'url_normal1','normal','1582589950367','test.url;1.0.0','test.url;1.0.0','[]'),(NULL,'url_normal2','normal','1582589950371','test.url;1.0.0','test.url;1.0.0','[]'),(NULL,'text_normal1','normal','1582589950363','test.url;1.0.0','test.url;1.0.0','[]'),(NULL,'url_normal1','normal','1582589950367','test.url;1.0.0','test.url;1.0.0','[]'),(NULL,'url_normal2','normal','1582589950371','test.url;1.0.0','test.url;1.0.0','[]'),(NULL,'new1','new','1582589950386','test.url;1.1.0','test.url;1.1.0','[]'),(NULL,'new2','new','1582589950389','test.url;1.1.0','test.url;1.1.0','[]'),(NULL,'text_normal1','normal','1582589950363','test.url;1.1.0','test.url;1.0.0','[\"linked|1.0.0\"]'),(NULL,'url_normal1','normal','1582589950367','test.url;1.1.0','test.url;1.0.0','[\"linked|1.0.0\"]'),(NULL,'url_normal2','normal','1582589950374','test.url;1.1.0','test.url;1.1.0','[]'),(NULL,'url_normal3','normal','1582589950378','test.url;1.1.0','test.url;1.1.0','[]'),(NULL,'url_withBase','normal','1582589950382','test.url;1.1.0','test.url;1.1.0','[]'),(NULL,'host1','normal','1582589950392','test.url;1.1.0','test.url;1.1.0','[]'),(NULL,'host2','normal','1582589950396','test.url;1.1.0','test.url;1.1.0','[]');
/*!40000 ALTER TABLE `datatypeoperation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `helperjs`
--

DROP TABLE IF EXISTS `helperjs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `helperjs` (
  `id` varchar(20) NOT NULL,
  `value` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `helperjs`
--

LOCK TABLES `helperjs` WRITE;
/*!40000 ALTER TABLE `helperjs` DISABLE KEYS */;
INSERT INTO `helperjs` VALUES ('1580152069876','{\"a\":123,\"b\":\"bb\",\"c\":\"cc\",\"d\":\nfunction (parm1) {\n    return parm1 = parm1 + \"fff\";\n}\n,\"e\":[1,2,3,4],\"f\":true,\"h\":[true,false,true]}'),('1580152069879','{\"a\":123,\"b\":\"bb\",\"c\":\"cc\",\"d\":\nfunction (parm1) {\n    return parm1 = parm1 + \"fff\";\n}\n,\"e\":[1,2,3,4],\"f\":true,\"h\":[true,false,true]}'),('1580152069950','{\"a\":123,\"b\":\"bb\",\"c\":\"cc\",\"d\":\nfunction (parm1) {\n    return parm1 = parm1 + \"fff\";\n}\n,\"e\":[1,2,3,4],\"f\":true,\"h\":[true,false,true]}'),('1580152069953','{\"a\":123,\"b\":\"bb\",\"c\":\"cc\",\"d\":\nfunction (parm1) {\n    return parm1 = parm1 + \"fff\";\n}\n,\"e\":[1,2,3,4],\"f\":true,\"h\":[true,false,true]}'),('1580152069956','{\"a\":123,\"b\":\"bb\",\"c\":\"cc\",\"d\":\nfunction (parm1) {\n    return parm1 = parm1 + \"fff\";\n}\n,\"e\":[1,2,3,4],\"f\":true,\"h\":[true,false,true]}'),('1580152069959','{\"a\":123,\"b\":\"bb\",\"c\":\"cc\",\"d\":\nfunction (parm1) {\n    return parm1 = parm1 + \"fff\";\n}\n,\"e\":[1,2,3,4],\"f\":true,\"h\":[true,false,true]}');
/*!40000 ALTER TABLE `helperjs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miniapp_group`
--

DROP TABLE IF EXISTS `miniapp_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `miniapp_group` (
  `ID` varchar(200) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miniapp_group`
--

LOCK TABLES `miniapp_group` WRITE;
/*!40000 ALTER TABLE `miniapp_group` DISABLE KEYS */;
INSERT INTO `miniapp_group` VALUES ('SchoolGroup','SchoolGroup','no description'),('SoccerForFun','Soccer For Fun','Soccer for fun group in Toronto'),('SoccerGroup','SoccerGroup','no description');
/*!40000 ALTER TABLE `miniapp_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miniapp_miniapp`
--

DROP TABLE IF EXISTS `miniapp_miniapp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `miniapp_miniapp` (
  `ID` varchar(200) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `DATAOWNERTYPE` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miniapp_miniapp`
--

LOCK TABLES `miniapp_miniapp` WRITE;
/*!40000 ALTER TABLE `miniapp_miniapp` DISABLE KEYS */;
INSERT INTO `miniapp_miniapp` VALUES ('Airplane Arrival','Airplane Arrival',NULL),('App_SoccerForFun_PlayerInformation','你的信息','group'),('App_SoccerForFun_PlayerLineup','阵容',NULL),('App_SoccerForFun_PlayerUpdate','操作',NULL),('AppMySchool','MySchool',NULL),('MyRealtor','MyRealtor',NULL);
/*!40000 ALTER TABLE `miniapp_miniapp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miniapp_setting`
--

DROP TABLE IF EXISTS `miniapp_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `miniapp_setting` (
  `ID` varchar(200) NOT NULL,
  `USERID` varchar(200) NOT NULL,
  `COMPONENTTYPE` varchar(200) NOT NULL,
  `COMPONENTID` varchar(200) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  `DATA` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miniapp_setting`
--

LOCK TABLES `miniapp_setting` WRITE;
/*!40000 ALTER TABLE `miniapp_setting` DISABLE KEYS */;
INSERT INTO `miniapp_setting` VALUES ('1572230361874','testUser','app','testApp','setting','[{\"data\":{},\"id\":\"390\",\"version\":\"New Setting\"}]'),('1574922317745','testUser','group','App_SoccerForFun_PlayerInformation;main','playerInfo','[{\"data\":{\"player\":{\"name\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"易道\"},\"registered\":{\"dataTypeId\":\"test.boolean;1.0.0\",\"value\":true},\"email\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"8877\"}}},\"id\":\"494\",\"version\":\"\"}]'),('1575065239086','testUser','app','AppMySchool;main','setting','[{\"data\":{\"schoolTypeInData\":{\"dataTypeId\":\"test.options;1.0.0\",\"value\":{\"optionsId\":\"schoolType\",\"value\":\"First Nation\"}},\"schoolRatingInData\":{\"dataTypeId\":\"test.float;1.0.0\",\"value\":8}},\"id\":\"542\",\"version\":\"New Setting\"},{\"data\":{\"schoolTypeInData\":{\"dataTypeId\":\"test.options;1.0.0\",\"value\":{\"optionsId\":\"schoolType\",\"value\":\"Public\"}},\"schoolRatingInData\":{\"dataTypeId\":\"test.float;1.0.0\",\"value\":9.59}},\"id\":\"987\",\"version\":\"New Setting\"}]'),('1575230621224','testUser','group','SoccerForFun','playerInfo','[{\"data\":{\"player\":{\"name\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"908\"},\"registered\":{\"dataTypeId\":\"test.boolean;1.0.0\",\"value\":false},\"email\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"789\"}}},\"id\":\"883\",\"version\":\"\"}]'),('1575310117873','1575310117868','group','SoccerForFun','playerInfo','[{\"data\":{\"player\":{\"name\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"王宁\"},\"registered\":{\"dataTypeId\":\"test.boolean;1.0.0\",\"value\":true},\"email\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"forbestwn@gmail.com\"}}},\"id\":\"620\",\"version\":\"\"}]'),('1575869573547','1575869573542','group','SoccerForFun','playerInfo','[{\"data\":{\"player\":{\"name\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"player123\"},\"registered\":{\"dataTypeId\":\"test.boolean;1.0.0\",\"value\":false},\"email\":{\"dataTypeId\":\"test.string;1.0.0\",\"value\":\"final@ggg.com\"}}},\"id\":\"441\",\"version\":\"\"}]');
/*!40000 ALTER TABLE `miniapp_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miniapp_user`
--

DROP TABLE IF EXISTS `miniapp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `miniapp_user` (
  `ID` varchar(200) NOT NULL,
  `NAME` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miniapp_user`
--

LOCK TABLES `miniapp_user` WRITE;
/*!40000 ALTER TABLE `miniapp_user` DISABLE KEYS */;
INSERT INTO `miniapp_user` VALUES ('1575310117868','1575310117868'),('1575439706597','1575439706597'),('1575572886108','1575572886108'),('1575869573542','1575869573542');
/*!40000 ALTER TABLE `miniapp_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miniapp_userapp`
--

DROP TABLE IF EXISTS `miniapp_userapp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `miniapp_userapp` (
  `ID` varchar(200) NOT NULL,
  `USERID` varchar(200) NOT NULL,
  `APPID` varchar(200) NOT NULL,
  `APPNAME` varchar(200) NOT NULL,
  `GROUPID` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`USERID`,`APPID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miniapp_userapp`
--

LOCK TABLES `miniapp_userapp` WRITE;
/*!40000 ALTER TABLE `miniapp_userapp` DISABLE KEYS */;
INSERT INTO `miniapp_userapp` VALUES ('1573883310756','1573883310754','App_SoccerForFun_PlayerInformation','PlayerInformation','SoccerForFun'),('1573883310758','1573883310754','App_SoccerForFun_PlayerLineup','PlayerLineup','SoccerForFun'),('1573883310757','1573883310754','App_SoccerForFun_PlayerUpdate','PlayerUpdate','SoccerForFun'),('1575310117870','1575310117868','App_SoccerForFun_PlayerInformation','PlayerInformation','SoccerForFun'),('1575310117872','1575310117868','App_SoccerForFun_PlayerLineup','PlayerLineup','SoccerForFun'),('1575310117871','1575310117868','App_SoccerForFun_PlayerUpdate','PlayerUpdate','SoccerForFun'),('1575439706599','1575439706597','App_SoccerForFun_PlayerInformation','PlayerInformation','SoccerForFun'),('1575439706601','1575439706597','App_SoccerForFun_PlayerLineup','PlayerLineup','SoccerForFun'),('1575439706600','1575439706597','App_SoccerForFun_PlayerUpdate','PlayerUpdate','SoccerForFun'),('1575572886110','1575572886108','App_SoccerForFun_PlayerInformation','PlayerInformation','SoccerForFun'),('1575572886112','1575572886108','App_SoccerForFun_PlayerLineup','PlayerLineup','SoccerForFun'),('1575572886111','1575572886108','App_SoccerForFun_PlayerUpdate','PlayerUpdate','SoccerForFun'),('1575869573544','1575869573542','App_SoccerForFun_PlayerInformation','PlayerInformation','SoccerForFun'),('1575869573546','1575869573542','App_SoccerForFun_PlayerLineup','PlayerLineup','SoccerForFun'),('1575869573545','1575869573542','App_SoccerForFun_PlayerUpdate','PlayerUpdate','SoccerForFun');
/*!40000 ALTER TABLE `miniapp_userapp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miniapp_usergroup`
--

DROP TABLE IF EXISTS `miniapp_usergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `miniapp_usergroup` (
  `ID` varchar(200) NOT NULL,
  `USERID` varchar(200) NOT NULL,
  `GROUPID` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miniapp_usergroup`
--

LOCK TABLES `miniapp_usergroup` WRITE;
/*!40000 ALTER TABLE `miniapp_usergroup` DISABLE KEYS */;
INSERT INTO `miniapp_usergroup` VALUES ('1573883310755','1573883310754','SoccerForFun'),('1575310117869','1575310117868','SoccerForFun'),('1575439706598','1575439706597','SoccerForFun'),('1575572886109','1575572886108','SoccerForFun'),('1575869573543','1575869573542','SoccerForFun');
/*!40000 ALTER TABLE `miniapp_usergroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `operation` (
  `info` varchar(2000) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `id` varchar(20) NOT NULL,
  `dataTypeName` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation`
--

LOCK TABLES `operation` WRITE;
/*!40000 ALTER TABLE `operation` DISABLE KEYS */;
INSERT INTO `operation` VALUES (NULL,'isLonger','normal','1582589950156','core.distance;1.0.0'),(NULL,'getDistance','normal','1582589950160','core.distance;1.0.0'),(NULL,'convert','normal','1582589950164','core.distance;1.0.0'),(NULL,'new','new','1582589950168','core.distance;1.0.0'),(NULL,'add','normal','1582589950172','base.integer;1.0.0'),(NULL,'subString','normal','1582589950176','base.string;1.0.0'),(NULL,'length','normal','1582589950181','base.string;1.0.0'),(NULL,'text_normal1','normal','1582589950184','core.text;1.0.0'),(NULL,'text_normal2','normal','1582589950188','core.text;1.0.0'),(NULL,'text_normal3','normal','1582589950192','core.text;1.0.0'),(NULL,'text_normal1','normal','1582589950196','core.url;1.0.0'),(NULL,'url_normal1','normal','1582589950200','core.url;1.0.0'),(NULL,'url_normal2','normal','1582589950204','core.url;1.0.0'),(NULL,'url_normal2','normal','1582589950207','core.url;1.1.0'),(NULL,'url_normal3','normal','1582589950211','core.url;1.1.0'),(NULL,'url_withBase','normal','1582589950215','core.url;1.1.0'),(NULL,'new1','new','1582589950219','core.url;1.1.0'),(NULL,'new2','new','1582589950222','core.url;1.1.0'),(NULL,'host1','normal','1582589950225','core.url;1.1.0'),(NULL,'host2','normal','1582589950229','core.url;1.1.0'),(NULL,'process','normal','1582589950233','test.array;1.0.0'),(NULL,'getChildrenNames','normal','1582589950238','test.array;1.0.0'),(NULL,'isAccessChildById','normal','1582589950241','test.array;1.0.0'),(NULL,'addChild','normal','1582589950244','test.array;1.0.0'),(NULL,'add','normal','1582589950249','test.array;1.0.0'),(NULL,'removeChild','normal','1582589950253','test.array;1.0.0'),(NULL,'length','normal','1582589950257','test.array;1.0.0'),(NULL,'getChildData','normal','1582589950260','test.array;1.0.0'),(NULL,'getChildDataByIndex','normal','1582589950264','test.array;1.0.0'),(NULL,'emptyArray','normal','1582589950268','test.array;1.0.0'),(NULL,'new','normal','1582589950270','test.array;1.0.0'),(NULL,'opposite','normal','1582589950272','test.boolean;1.0.0'),(NULL,'getChild','normal','1582589950275','test.dataTypeCriteria;1.0.0'),(NULL,'getChildren','normal','1582589950279','test.dataTypeCriteria;1.0.0'),(NULL,'addChild','normal','1582589950282','test.dataTypeCriteria;1.0.0'),(NULL,'shorterThan','normal','1582589950287','test.distance;1.0.0'),(NULL,'outputCriteria','normal','1582589950291','test.expression;1.0.0'),(NULL,'outputCriteriaFromParmData','normal','1582589950295','test.expression;1.0.0'),(NULL,'execute','normal','1582589950299','test.expression;1.0.0'),(NULL,'distance','normal','1582589950303','test.geo;1.0.0'),(NULL,'add','normal','1582589950307','test.integer;1.0.0'),(NULL,'put','normal','1582589950311','test.map;1.0.0'),(NULL,'new','normal','1582589950316','test.map;1.0.0'),(NULL,'getChildrenNames','normal','1582589950318','test.map;1.0.0'),(NULL,'isAccessChildById','normal','1582589950321','test.map;1.0.0'),(NULL,'length','normal','1582589950324','test.map;1.0.0'),(NULL,'getChildData','normal','1582589950327','test.map;1.0.0'),(NULL,'setChildData','normal','1582589950331','test.map;1.0.0'),(NULL,'all','normal','1582589950336','test.options;1.0.0'),(NULL,'getValue','normal','1582589950339','test.parm;1.0.0'),(NULL,'getCriteria','normal','1582589950342','test.parm;1.0.0'),(NULL,'emptyString','normal','1582589950345','test.string;1.0.0'),(NULL,'isEmpty','normal','1582589950347','test.string;1.0.0'),(NULL,'subString','normal','1582589950350','test.string;1.0.0'),(NULL,'length','normal','1582589950355','test.string;1.0.0'),(NULL,'notImpplementedOperation','normal','1582589950358','test.string;1.0.0'),(NULL,'text_normal1','normal','1582589950363','test.url;1.0.0'),(NULL,'url_normal1','normal','1582589950367','test.url;1.0.0'),(NULL,'url_normal2','normal','1582589950371','test.url;1.0.0'),(NULL,'url_normal2','normal','1582589950374','test.url;1.1.0'),(NULL,'url_normal3','normal','1582589950378','test.url;1.1.0'),(NULL,'url_withBase','normal','1582589950382','test.url;1.1.0'),(NULL,'new1','new','1582589950386','test.url;1.1.0'),(NULL,'new2','new','1582589950389','test.url;1.1.0'),(NULL,'host1','normal','1582589950392','test.url;1.1.0'),(NULL,'host2','normal','1582589950396','test.url;1.1.0');
/*!40000 ALTER TABLE `operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operationjs`
--

DROP TABLE IF EXISTS `operationjs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `operationjs` (
  `id` varchar(200) NOT NULL,
  `value` varchar(2000) NOT NULL,
  `operationId` varchar(200) NOT NULL,
  `operationName` varchar(200) NOT NULL,
  `dataTypeName` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operationjs`
--

LOCK TABLES `operationjs` WRITE;
/*!40000 ALTER TABLE `operationjs` DISABLE KEYS */;
INSERT INTO `operationjs` VALUES ('1580152069873','\nfunction (parms, context) {\n    context.logging.info(\"Operand Calcualting integer.add ----------------\");\n    var parm1 = parms.getParm(\"parm1\").data;\n    var parm2 = parms.getParm(\"parm2\").data;\n    var out = parm1 + parm2;\n    return {dataTypeId: \"base.integer;1.0.0\", value: out};\n}\n','1561472058404','add','base.integer;1.0.0'),('1580152069875','\nfunction (parms, context) {\n    context.logging.info(\"Operand Calcualting [subString]  ----------------\");\n    var from = parms.getParm(\"from\").data;\n    var to = parms.getParm(\"to\").data;\n    var outStr = this.value.substring(from, to);\n    outStr = context.getResourceDataByName(\"globalHelper\").d(outStr);\n    return {dataTypeId: \"base.string;1.0.0\", value: outStr};\n}\n','1561472058408','subString','base.string;1.0.0'),('1580152069878','\nfunction (parms, context) {\n    var baseData = parms[base];\n    return out;\n}\n','1561472058413','length','base.string;1.0.0'),('1580152069881','\nfunction (parms, context) {\n}\n','1561472058439','url_normal2','core.url;1.1.0'),('1580152069883','\nfunction (parms, context) {\n    context.logging.info(\"Operand Calcualting  ----------------\");\n}\n','1561472058443','url_normal3','core.url;1.1.0'),('1580152069885','\nfunction (parms, context) {\n    var valueOut = [];\n    var expressionData = parms.getParm(\"expression\");\n    _.each(this.value, function (eleData, i) {\n        var gatewayParms = {\"expression\": expressionData.value.expression, \"constants\": expressionData.value.constants, \"variablesValue\": {}};\n        gatewayParms.variablesValue[parms.getParm(\"elementVariableName\").value] = eleData;\n        var eleResult = context.getResourceDataByName(\"myGateWay\").command(\"executeExpression\", gatewayParms);\n        valueOut.push(eleResult);\n    }, this);\n    return {dataTypeId: \"test.array;1.0.0\", value: valueOut};\n}\n','1561472058465','process','test.array;1.0.0'),('1580152069887','\nfunction (parms, context) {\n    var names = [];\n    _.each(this.value, function (arrayValue, index) {\n        names.push({dataTypeId: \"test.string;1.0.0\", value: index + \"\"});\n    });\n    return {dataTypeId: \"test.array;1.0.0\", value: names};\n}\n','1561472058470','getChildrenNames','test.array;1.0.0'),('1580152069889','\nfunction (parms, context) {\n    return {dataTypeId: \"test.boolean;1.0.0\", value: false};\n}\n','1561472058473','isAccessChildById','test.array;1.0.0'),('1580152069891','\nfunction (parms, context) {\n    return {dataTypeId: \"test.integer;1.0.0\", value: parms.getParm(\"base\").value.length};\n}\n','1561472058489','length','test.array;1.0.0'),('1580152069893','\nfunction (parms, context) {\n    var name = parms.getParm(\"name\").value;\n    return this.value[parseInt(name)];\n}\n','1561472058492','getChildData','test.array;1.0.0'),('1580152069895','\nfunction (parms, context) {\n    var index = parms.getParm(\"index\").value;\n    var childValue = parms.getParm(\"child\");\n    this.value.splice(index, 0, childValue);\n    return this;\n}\n','1561472058476','addChild','test.array;1.0.0'),('1580152069897','\nfunction (parms, context) {\n    var childValue = parms.getParm(\"child\");\n    this.value.push(childValue);\n    return this;\n}\n','1561472058481','add','test.array;1.0.0'),('1580152069899','\nfunction (parms, context) {\n    var index = parms.getParm(\"index\").value;\n    var childValue = this.value[index];\n    this.value.splice(index, 1);\n    return childValue;\n}\n','1561472058485','removeChild','test.array;1.0.0'),('1580152069901','\nfunction (parms, context) {\n    var index = parms.getParm(\"index\").value;\n    return this.value[index];\n}\n','1561472058496','getChildDataByIndex','test.array;1.0.0'),('1580152069903','\nfunction (parms, context) {\n    return {dataTypeId: \"test.array;1.0.0\", value: []};\n}\n','1561472058500','emptyArray','test.array;1.0.0'),('1580152069905','\nfunction (parms, context) {\n    return {dataTypeId: \"test.array;1.0.0\", value: []};\n}\n','1561472058502','new','test.array;1.0.0'),('1580152069907','\nfunction (parms, context) {\n    var bValue = parms.getParm(\"base\").value;\n    var outValue;\n    if (bValue == true) {\n        outValue = false;\n    } else {\n        if (bValue == false) {\n            outValue = true;\n        }\n    }\n    return {dataTypeId: \"test.boolean;1.0.0\", value: outValue};\n}\n','1561472058504','opposite','test.boolean;1.0.0'),('1580152069909','\nfunction (parms, context) {\n    var gatewayParms = {\"criteria\": this.value, \"childName\": parms.getParm(\"childName\").value};\n    var criteriaStr = context.getResourceDataByName(\"myGateWay\").command(\"getChildCriteria\", gatewayParms).data;\n    return {dataTypeId: \"test.datatypecriteria;1.0.0\", value: criteriaStr};\n}\n','1561472058507','getChild','test.datatypecriteria;1.0.0'),('1580152069911','\nfunction (parms, context) {\n    var gatewayParms = {\"criteria\": this.value};\n    var childrenCriterias = context.getResourceDataByName(\"myGateWay\").command(\"getChildrenCriteria\", gatewayParms);\n    var out = {dataTypeId: \"test.map;1.0.0\", value: {}};\n    _.each(childrenCriterias, function (childCriteria, name) {\n        out.value[name] = {dataTypeId: \"test.datatypecriteria;1.0.0\", value: childCriteria};\n    });\n    return out;\n}\n','1561472058511','getChildren','test.datatypecriteria;1.0.0'),('1580152069913','\nfunction (parms, context) {\n    var gatewayParms = {\"criteria\": this.value, \"childName\": parms.getParm(\"childName\").value, \"child\": parms.getParm(\"child\").value};\n    var criteriaStr = context.getResourceDataByName(\"myGateWay\").command(\"addChildCriteria\", gatewayParms);\n    return {dataTypeId: \"test.datatypecriteria;1.0.0\", value: criteriaStr};\n}\n','1561472058514','addChild','test.datatypecriteria;1.0.0'),('1580152069915','\nfunction (parms, context) {\n    var distance1 = parms.getParm(\"dis1\").value.distance;\n    var distance2 = parms.getParm(\"dis2\").value.distance;\n    if (distance1 < distance2) {\n        return {dataTypeId: \"test.boolean;1.0.0\", value: true};\n    } else {\n        return {dataTypeId: \"test.boolean;1.0.0\", value: false};\n    }\n}\n','1561472058519','shorterThan','test.distance;1.0.0'),('1580152069917','\nfunction (parms, context) {\n    var varCriterias = {};\n    _.each(parms.getParm(\"parms\").value, function (varCriteriaData, varName) {\n        varCriterias[varName] = varCriteriaData.value;\n    });\n    var gatewayParms = {\"expression\": this.value, \"variablesCriteria\": varCriterias};\n    var criteriaStr = context.getResourceDataByName(\"myGateWay\").command(\"getOutputCriteria\", gatewayParms);\n    return {dataTypeId: \"test.datatypecriteria;1.0.0\", value: criteriaStr};\n}\n','1561472058523','outputCriteria','test.expression;1.0.0'),('1580152069919','\nfunction (parms, context) {\n    var varCriterias = {};\n    _.each(parms.getParm(\"parms\").value, function (parm, varName) {\n        var parmCriteria = context.operation(\"test.parm;1.0.0\", \"getCriteria\", [{name: \"base\", value: parm}]);\n        varCriterias[varName] = parmCriteria.value;\n    });\n    var gatewayParms = {\"expression\": this.value.expression, \"constants\": this.value.constants, \"variablesCriteria\": varCriterias};\n    var criteriaStr = context.getResourceDataByName(\"myGateWay\").command(\"getOutputCriteria\", gatewayParms);\n    return {dataTypeId: \"test.datatypecriteria;1.0.0\", value: criteriaStr};\n}\n','1561472058527','outputCriteriaFromParmData','test.expression;1.0.0'),('1580152069921','\nfunction (parms, context) {\n    var varValues = {};\n    _.each(parms.getParm(\"parms\").value, function (varValue, varName) {\n        varValues[varName] = varValue;\n    });\n    var gatewayParms = {\"expression\": this.value, \"variablesValue\": varValues};\n    var resultData = context.getResourceDataByName(\"myGateWay\").command(\"executeExpression\", gatewayParms);\n    return resultData;\n}\n','1561472058531','execute','test.expression;1.0.0'),('1580152069923','\nfunction (parms, context) {\n    var fromLat = parms.getParm(\"from\").value.latitude;\n    var fromLon = parms.getParm(\"from\").value.longitude;\n    var toLat = parms.getParm(\"to\").value.latitude;\n    var toLon = parms.getParm(\"to\").value.longitude;\n    var prv_calcCrow = function (lat1, lon1, lat2, lon2) {\n        var R = 6371;\n        var dLat = prv_toRad(lat2 - lat1);\n        var dLon = prv_toRad(lon2 - lon1);\n        var lat1 = prv_toRad(lat1);\n        var lat2 = prv_toRad(lat2);\n        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);\n        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));\n        var d = R * c;\n        return d;\n    };\n    var prv_toRad = function (Value) {\n        return Value * Math.PI / 180;\n    };\n    var distance = prv_calcCrow(fromLat, fromLon, toLat, toLon);\n    return {dataTypeId: \"test.distance;1.0.0\", value: {distance: distance, unit: \"km\"}};\n}\n','1561472058535','distance','test.geo;1.0.0'),('1580152069925','\nfunction (parms, context) {\n    var parm1 = parms.getParm(\"parm1\").value;\n    var parm2 = parms.getParm(\"parm2\").value;\n    var out = parm1 + parm2;\n    return {dataTypeId: \"test.integer;1.0.0\", value: out};\n}\n','1561472058539','add','test.integer;1.0.0'),('1580152069927','\nfunction (parms, context) {\n    var name = parms.getParm(\"name\").value;\n    var data = parms.getParm(\"value\");\n    this.value[name] = data;\n    return this;\n}\n','1561472058543','put','test.map;1.0.0'),('1580152069929','\nfunction (parms, context) {\n    var names = [];\n    _.each(this.value, function (mapValue, name) {\n        names.push({dataTypeId: \"test.string;1.0.0\", value: name});\n    });\n    return {dataTypeId: \"test.array;1.0.0\", value: names};\n}\n','1561472058550','getChildrenNames','test.map;1.0.0'),('1580152069931','\nfunction (parms, context) {\n    return {dataTypeId: \"test.boolean;1.0.0\", value: true};\n}\n','1561472058553','isAccessChildById','test.map;1.0.0'),('1580152069933','\nfunction (parms, context) {\n    var obj = parms.getParm(\"base\").value;\n    var count = 0;\n    for (var k in obj) {\n        if (obj.hasOwnProperty(k)) {\n            ++count;\n        }\n    }\n    return {dataTypeId: \"test.integer;1.0.0\", value: count};\n}\n','1561472058556','length','test.map;1.0.0'),('1580152069935','\nfunction (parms, context) {\n    var name = parms.getParm(\"name\").value;\n    return this.value[name];\n}\n','1561472058559','getChildData','test.map;1.0.0'),('1580152069937','\nfunction (parms, context) {\n    var name = parms.getParm(\"name\").value;\n    var value = parms.getParm(\"value\");\n    this.value[name] = value;\n    return this;\n}\n','1561472058563','setChildData','test.map;1.0.0'),('1580152069939','\nfunction (parms, context) {\n    return {dataTypeId: \"test.map;1.0.0\", value: {}};\n}\n','1561472058548','new','test.map;1.0.0'),('1580152069941','\nfunction (parms, context) {\n    var valueOut = [];\n    var gatewayParms = {\"id\": parms.getParm(\"optionsId\").value};\n    var out = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(\"options\", \"getValues\", gatewayParms, {success: function (requestInfo, optionsValues) {\n        _.each(optionsValues, function (value, i) {\n            valueOut.push({dataTypeId: \"test.options;1.0.0\", value: value});\n        });\n        return {dataTypeId: \"test.array;1.0.0\", value: valueOut};\n    }});\n    return out;\n}\n','1561472058568','all','test.options;1.0.0'),('1580152069945','\nfunction (parms, context) {\n    return this.value.data;\n}\n','1561472058571','getValue','test.parm;1.0.0'),('1580152069947','\nfunction (parms, context) {\n    var out = this.value.outputCriteria;\n    return {dataTypeId: \"test.datatypecriteria;1.0.0\", value: out};\n}\n','1561472058574','getCriteria','test.parm;1.0.0'),('1580152069949','\nfunction (parms, context) {\n    return {dataTypeId: \"test.string;1.0.0\", value: \"\"};\n}\n','1561472058577','emptyString','test.string;1.0.0'),('1580152069952','\nfunction (parms, context) {\n    if (this.value == \"\") {\n        return {dataTypeId: \"test.boolean;1.0.0\", value: true};\n    } else {\n        return {dataTypeId: \"test.boolean;1.0.0\", value: false};\n    }\n}\n','1561472058579','isEmpty','test.string;1.0.0'),('1580152069955','\nfunction (parms, context) {\n    var from = parms.getParm(\"from\").value;\n    var to = parms.getParm(\"to\").value;\n    var outStr = this.value.substring(from, to);\n    outStr = context.getResourceDataByName(\"globalHelper\").d(outStr);\n    return {dataTypeId: \"test.string;1.0.0\", value: outStr};\n}\n','1561472058582','subString','test.string;1.0.0'),('1580152069958','\nfunction (parms, context) {\n    var baseData = parms[base];\n    return out;\n}\n','1561472058587','length','test.string;1.0.0'),('1580152069963','\nfunction (parms, context) {\n}\n','1561472058606','url_normal2','test.url;1.1.0'),('1580152069965','\nfunction (parms, context) {\n    context.logging.info(\"Operand Calcualting  ----------------\");\n}\n','1561472058610','url_normal3','test.url;1.1.0');
/*!40000 ALTER TABLE `operationjs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operationparm`
--

DROP TABLE IF EXISTS `operationparm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `operationparm` (
  `id` varchar(20) NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `isBase` varchar(20) DEFAULT NULL,
  `dataTypeId` varchar(100) DEFAULT NULL,
  `operationId` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `description` varchar(20) DEFAULT NULL,
  `criteria` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operationparm`
--

LOCK TABLES `operationparm` WRITE;
/*!40000 ALTER TABLE `operationparm` DISABLE KEYS */;
INSERT INTO `operationparm` VALUES ('1582589950157','parm','false','core.distance;1.0.0','1582589950156','distance1',NULL,'core.distance;1.0.0'),('1582589950158','parm','false','core.distance;1.0.0','1582589950156','distance2',NULL,'core.distance;1.0.0'),('1582589950159','out','false','core.distance;1.0.0','1582589950156',NULL,NULL,'base.boolean;1.0.0'),('1582589950161','parm','false','core.distance;1.0.0','1582589950160','distance',NULL,'core.distance;1.0.0'),('1582589950162','parm','false','core.distance;1.0.0','1582589950160','unit',NULL,'core.lengthunit;1.0.0'),('1582589950163','out','false','core.distance;1.0.0','1582589950160',NULL,NULL,'base.number;1.0.0'),('1582589950165','parm','false','core.distance;1.0.0','1582589950164','distance',NULL,'core.distance;1.0.0'),('1582589950166','parm','false','core.distance;1.0.0','1582589950164','unit',NULL,'core.lengthunit;1.0.0'),('1582589950167','out','false','core.distance;1.0.0','1582589950164',NULL,NULL,'core.distance;1.0.0'),('1582589950169','parm','false','core.distance;1.0.0','1582589950168','number',NULL,'core.number;1.0.0'),('1582589950170','parm','false','core.distance;1.0.0','1582589950168','unit',NULL,'core.lengthunit;1.0.0'),('1582589950171','out','false','core.distance;1.0.0','1582589950168',NULL,NULL,'core.distance;1.0.0'),('1582589950173','parm','false','base.integer;1.0.0','1582589950172','parm1',NULL,'base.integer;1.0.0'),('1582589950174','parm','false','base.integer;1.0.0','1582589950172','parm2',NULL,'base.integer;1.0.0'),('1582589950175','out','false','base.integer;1.0.0','1582589950172',NULL,NULL,'base.integer;1.0.0'),('1582589950177','parm','true','base.string;1.0.0','1582589950176','ABCDEFGHIJKLMN',NULL,'base.string;1.0.0'),('1582589950178','parm','false','base.string;1.0.0','1582589950176','from',NULL,'base.integer;1.0.0'),('1582589950179','parm','false','base.string;1.0.0','1582589950176','to',NULL,'base.integer;1.0.0'),('1582589950180','out','false','base.string;1.0.0','1582589950176',NULL,NULL,'base.string;1.0.0'),('1582589950182','parm','true','base.string;1.0.0','1582589950181','ABCDEFGHIJKLMN',NULL,'base.string;1.0.0'),('1582589950183','out','false','base.string;1.0.0','1582589950181',NULL,NULL,'base.integer;1.0.0'),('1582589950185','parm','false','core.text;1.0.0','1582589950184','aa',NULL,'core.text;1.0.0'),('1582589950186','parm','false','core.text;1.0.0','1582589950184','bb',NULL,'core.text;1.0.0'),('1582589950187','out','false','core.text;1.0.0','1582589950184',NULL,NULL,'core.text;1.0.0'),('1582589950189','parm','false','core.text;1.0.0','1582589950188','text_normal_aa',NULL,'core.text;1.0.0'),('1582589950190','parm','false','core.text;1.0.0','1582589950188','text_normal_bb',NULL,'core.text;1.0.0'),('1582589950191','out','false','core.text;1.0.0','1582589950188',NULL,NULL,'core.text;1.0.0'),('1582589950193','parm','false','core.text;1.0.0','1582589950192','text_normal_aa',NULL,'core.text;1.0.0'),('1582589950194','parm','false','core.text;1.0.0','1582589950192','text_normal_bb',NULL,'core.text;1.0.0'),('1582589950195','out','false','core.text;1.0.0','1582589950192',NULL,NULL,'core.text;1.0.0'),('1582589950197','parm','false','core.url;1.0.0','1582589950196','aa',NULL,'core.text;1.0.0'),('1582589950198','parm','false','core.url;1.0.0','1582589950196','bb',NULL,'core.text;1.0.0'),('1582589950199','out','false','core.url;1.0.0','1582589950196',NULL,NULL,'core.text;1.0.0'),('1582589950201','parm','false','core.url;1.0.0','1582589950200','aa',NULL,'core.text;1.0.0'),('1582589950202','parm','false','core.url;1.0.0','1582589950200','bb',NULL,'core.text;1.0.0'),('1582589950203','out','false','core.url;1.0.0','1582589950200',NULL,NULL,'core.text;1.0.0'),('1582589950205','parm','false','core.url;1.0.0','1582589950204','aa',NULL,'core.text;1.0.0'),('1582589950206','parm','false','core.url;1.0.0','1582589950204','bb',NULL,'core.text;1.0.0'),('1582589950208','parm','false','core.url;1.1.0','1582589950207','aa',NULL,'core.text;1.0.0'),('1582589950209','parm','false','core.url;1.1.0','1582589950207','bb',NULL,'core.text;1.0.0'),('1582589950210','out','false','core.url;1.1.0','1582589950207',NULL,NULL,'core.text;1.0.0'),('1582589950212','parm','false','core.url;1.1.0','1582589950211','aa',NULL,'core.text;1.0.0'),('1582589950213','parm','false','core.url;1.1.0','1582589950211','bb',NULL,'core.text;1.0.0'),('1582589950214','out','false','core.url;1.1.0','1582589950211',NULL,NULL,'core.text;1.0.0'),('1582589950216','parm','true','core.url;1.1.0','1582589950215','aa',NULL,'core.url;1.1.0'),('1582589950217','parm','false','core.url;1.1.0','1582589950215','bb',NULL,'core.text;1.0.0'),('1582589950218','out','false','core.url;1.1.0','1582589950215',NULL,NULL,'core.text;1.0.0'),('1582589950220','parm','false','core.url;1.1.0','1582589950219','aa',NULL,'core.text;1.0.0'),('1582589950221','parm','false','core.url;1.1.0','1582589950219','bb',NULL,'core.text;1.0.0'),('1582589950223','parm','false','core.url;1.1.0','1582589950222','aa',NULL,'core.text;1.0.0'),('1582589950224','parm','false','core.url;1.1.0','1582589950222','bb',NULL,'core.text;1.0.0'),('1582589950226','parm','false','core.url;1.1.0','1582589950225','aa1',NULL,'core.text;1.0.0'),('1582589950227','parm','false','core.url;1.1.0','1582589950225','bb1',NULL,'core.text;1.0.0'),('1582589950228','out','false','core.url;1.1.0','1582589950225',NULL,NULL,'core.text;1.0.0'),('1582589950230','parm','false','core.url;1.1.0','1582589950229','aa2',NULL,'core.text;1.0.0'),('1582589950231','parm','false','core.url;1.1.0','1582589950229','bb2',NULL,'core.text;1.0.0'),('1582589950232','out','false','core.url;1.1.0','1582589950229',NULL,NULL,'core.text;1.0.0'),('1582589950234','parm','true','test.array;1.0.0','1582589950233','base',NULL,'test.array;1.0.0'),('1582589950235','parm','false','test.array;1.0.0','1582589950233','elementVariableName',NULL,'test.string;1.0.0'),('1582589950236','parm','false','test.array;1.0.0','1582589950233','expression',NULL,'test.expression;1.0.0'),('1582589950237','out','false','test.array;1.0.0','1582589950233',NULL,NULL,'test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(expression;;;?(expression)?.getValue();;parms;;;!(test.map)!.new().put(name;;;?(elementVariableName)?.getValue();;value;;;?(base)?.getCriteria().getChild(childName;;;#test.string;element)))||@||%'),('1582589950239','parm','true','test.array;1.0.0','1582589950238','base',NULL,'test.map;1.0.0'),('1582589950240','out','false','test.array;1.0.0','1582589950238',NULL,NULL,'test.array;1.0.0%||element:test.string;1.0.0||%'),('1582589950242','parm','true','test.array;1.0.0','1582589950241','base',NULL,'test.array;1.0.0'),('1582589950243','out','false','test.array;1.0.0','1582589950241',NULL,NULL,'test.boolean;1.0.0'),('1582589950245','parm','true','test.array;1.0.0','1582589950244','base',NULL,'test.array;1.0.0'),('1582589950246','parm','false','test.array;1.0.0','1582589950244','index',NULL,'test.integer;1.0.0'),('1582589950247','parm','false','test.array;1.0.0','1582589950244','child',NULL,'*'),('1582589950248','out','false','test.array;1.0.0','1582589950244',NULL,NULL,'test.array;1.0.0'),('1582589950250','parm','true','test.array;1.0.0','1582589950249','base',NULL,'test.array;1.0.0'),('1582589950251','parm','false','test.array;1.0.0','1582589950249','child',NULL,'*'),('1582589950252','out','false','test.array;1.0.0','1582589950249',NULL,NULL,'test.array;1.0.0'),('1582589950254','parm','true','test.array;1.0.0','1582589950253','base',NULL,'test.array;1.0.0'),('1582589950255','parm','false','test.array;1.0.0','1582589950253','index',NULL,'test.integer;1.0.0'),('1582589950256','out','false','test.array;1.0.0','1582589950253',NULL,NULL,'*'),('1582589950258','parm','true','test.array;1.0.0','1582589950257','base',NULL,'test.array;1.0.0'),('1582589950259','out','false','test.array;1.0.0','1582589950257',NULL,NULL,'test.integer;1.0.0'),('1582589950261','parm','true','test.array;1.0.0','1582589950260','base',NULL,'test.array;1.0.0'),('1582589950262','parm','false','test.array;1.0.0','1582589950260','name',NULL,'test.string;1.0.0'),('1582589950263','out','false','test.array;1.0.0','1582589950260',NULL,NULL,'*'),('1582589950265','parm','true','test.array;1.0.0','1582589950264','base',NULL,'test.array;1.0.0'),('1582589950266','parm','false','test.array;1.0.0','1582589950264','index',NULL,'test.integer;1.0.0'),('1582589950267','out','false','test.array;1.0.0','1582589950264',NULL,NULL,'*'),('1582589950269','out','false','test.array;1.0.0','1582589950268',NULL,NULL,'test.array;1.0.0'),('1582589950271','out','false','test.array;1.0.0','1582589950270',NULL,NULL,'test.array;1.0.0'),('1582589950273','parm','true','test.boolean;1.0.0','1582589950272','base',NULL,'test.boolean;1.0.0'),('1582589950274','out','false','test.boolean;1.0.0','1582589950272',NULL,NULL,'test.boolean;1.0.0'),('1582589950276','parm','true','test.dataTypeCriteria;1.0.0','1582589950275','base',NULL,'test.dataTypeCriteria;1.0.0'),('1582589950277','parm','false','test.dataTypeCriteria;1.0.0','1582589950275','childName',NULL,'test.string;1.0.0'),('1582589950278','out','false','test.dataTypeCriteria;1.0.0','1582589950275',NULL,NULL,'test.dataTypeCriteria;1.0.0'),('1582589950280','parm','true','test.dataTypeCriteria;1.0.0','1582589950279','base',NULL,'test.dataTypeCriteria;1.0.0'),('1582589950281','out','false','test.dataTypeCriteria;1.0.0','1582589950279',NULL,NULL,'test.map;1.0.0'),('1582589950283','parm','true','test.dataTypeCriteria;1.0.0','1582589950282','base',NULL,'test.dataTypeCriteria;1.0.0'),('1582589950284','parm','false','test.dataTypeCriteria;1.0.0','1582589950282','childName',NULL,'test.string;1.0.0'),('1582589950285','parm','false','test.dataTypeCriteria;1.0.0','1582589950282','child',NULL,'test.dataTypeCriteria;1.0.0'),('1582589950286','out','false','test.dataTypeCriteria;1.0.0','1582589950282',NULL,NULL,'test.dataTypeCriteria;1.0.0'),('1582589950288','parm','true','test.distance;1.0.0','1582589950287','dis1',NULL,'test.distance;1.0.0'),('1582589950289','parm','false','test.distance;1.0.0','1582589950287','dis2',NULL,'test.distance;1.0.0'),('1582589950290','out','false','test.distance;1.0.0','1582589950287',NULL,NULL,'test.boolean;1.0.0'),('1582589950292','parm','true','test.expression;1.0.0','1582589950291','expression',NULL,'test.expression;1.0.0'),('1582589950293','parm','false','test.expression;1.0.0','1582589950291','parms',NULL,'test.map;1.0.0'),('1582589950294','out','false','test.expression;1.0.0','1582589950291',NULL,NULL,'test.dataTypeCriteria;1.0.0'),('1582589950296','parm','true','test.expression;1.0.0','1582589950295','expression',NULL,'test.expression;1.0.0'),('1582589950297','parm','false','test.expression;1.0.0','1582589950295','parms',NULL,'test.map;1.0.0'),('1582589950298','out','false','test.expression;1.0.0','1582589950295',NULL,NULL,'test.dataTypeCriteria;1.0.0'),('1582589950300','parm','true','test.expression;1.0.0','1582589950299','expression',NULL,'test.expression;1.0.0'),('1582589950301','parm','false','test.expression;1.0.0','1582589950299','parms',NULL,'test.map;1.0.0'),('1582589950302','out','false','test.expression;1.0.0','1582589950299',NULL,NULL,'@||!(test.expression)!.outputCriteria(expression;;;!(test.parm)!.getValue(?(expression)?);;parms;;;!(test.datatypecriteria)!.getChildren(base;;;!(test.parm)!.getCriteria(?(parms)?)))||@'),('1582589950304','parm','false','test.geo;1.0.0','1582589950303','from',NULL,'test.geo;1.0.0'),('1582589950305','parm','false','test.geo;1.0.0','1582589950303','to',NULL,'test.geo;1.0.0'),('1582589950306','out','false','test.geo;1.0.0','1582589950303',NULL,NULL,'test.distance;1.0.0'),('1582589950308','parm','false','test.integer;1.0.0','1582589950307','parm1',NULL,'test.integer;1.0.0'),('1582589950309','parm','false','test.integer;1.0.0','1582589950307','parm2',NULL,'test.integer;1.0.0'),('1582589950310','out','false','test.integer;1.0.0','1582589950307',NULL,NULL,'test.integer;1.0.0'),('1582589950312','parm','true','test.map;1.0.0','1582589950311','base',NULL,'test.map;1.0.0'),('1582589950313','parm','false','test.map;1.0.0','1582589950311','name',NULL,'test.string;1.0.0'),('1582589950314','parm','false','test.map;1.0.0','1582589950311','value',NULL,'*'),('1582589950315','out','false','test.map;1.0.0','1582589950311',NULL,NULL,'@||!(test.dataTypeCriteria)!.addChild(base;;;!(test.parm)!.getCriteria(&(base)&);;childName;;;!(test.parm)!.getValue(&(name)&);;child;;;!(test.parm)!.getCriteria(&(value)&))||@'),('1582589950317','out','false','test.map;1.0.0','1582589950316',NULL,NULL,'test.map;1.0.0'),('1582589950319','parm','true','test.map;1.0.0','1582589950318','base',NULL,'test.map;1.0.0'),('1582589950320','out','false','test.map;1.0.0','1582589950318',NULL,NULL,'test.array;1.0.0%||element:test.string;1.0.0||%'),('1582589950322','parm','true','test.map;1.0.0','1582589950321','base',NULL,'test.map;1.0.0'),('1582589950323','out','false','test.map;1.0.0','1582589950321',NULL,NULL,'test.boolean;1.0.0'),('1582589950325','parm','true','test.map;1.0.0','1582589950324','base',NULL,'test.map;1.0.0'),('1582589950326','out','false','test.map;1.0.0','1582589950324',NULL,NULL,'test.integer;1.0.0'),('1582589950328','parm','true','test.map;1.0.0','1582589950327','base',NULL,'test.map;1.0.0'),('1582589950329','parm','false','test.map;1.0.0','1582589950327','name',NULL,'test.string;1.0.0'),('1582589950330','out','false','test.map;1.0.0','1582589950327',NULL,NULL,'@||!(test.dataTypeCriteria)!.getChild(base;;;!(test.parm)!.getCriteria(base;;;?(base)?);;childName;;;!(test.parm)!.getValue(base;;;?(name)?))||@'),('1582589950332','parm','true','test.map;1.0.0','1582589950331','base',NULL,'test.map;1.0.0'),('1582589950333','parm','false','test.map;1.0.0','1582589950331','name',NULL,'test.string;1.0.0'),('1582589950334','parm','false','test.map;1.0.0','1582589950331','value',NULL,'*'),('1582589950335','out','false','test.map;1.0.0','1582589950331',NULL,NULL,'test.map;1.0.0'),('1582589950337','parm','false','test.options;1.0.0','1582589950336','optionsId',NULL,'test.string;1.0.0'),('1582589950338','out','false','test.options;1.0.0','1582589950336',NULL,NULL,'test.array;1.0.0%||element:test.options;1.0.0||%'),('1582589950340','parm','true','test.parm;1.0.0','1582589950339','base',NULL,'test.parm;1.0.0'),('1582589950341','out','false','test.parm;1.0.0','1582589950339',NULL,NULL,'*'),('1582589950343','parm','true','test.parm;1.0.0','1582589950342','base',NULL,'test.parm;1.0.0'),('1582589950344','out','false','test.parm;1.0.0','1582589950342',NULL,NULL,'test.dataTypeCriteria;1.0.0'),('1582589950346','out','false','test.string;1.0.0','1582589950345',NULL,NULL,'test.string;1.0.0'),('1582589950348','parm','true','test.string;1.0.0','1582589950347','base',NULL,'test.string;1.0.0'),('1582589950349','out','false','test.string;1.0.0','1582589950347',NULL,NULL,'test.boolean;1.0.0'),('1582589950351','parm','true','test.string;1.0.0','1582589950350','base',NULL,'test.string;1.0.0'),('1582589950352','parm','false','test.string;1.0.0','1582589950350','from',NULL,'test.integer;1.0.0'),('1582589950353','parm','false','test.string;1.0.0','1582589950350','to',NULL,'test.integer;1.0.0'),('1582589950354','out','false','test.string;1.0.0','1582589950350',NULL,NULL,'test.string;1.0.0'),('1582589950356','parm','true','test.string;1.0.0','1582589950355','ABCDEFGHIJKLMN',NULL,'test.string;1.0.0'),('1582589950357','out','false','test.string;1.0.0','1582589950355',NULL,NULL,'test.integer;1.0.0'),('1582589950359','parm','true','test.string;1.0.0','1582589950358','ABCDEFGHIJKLMN',NULL,'test.string;1.0.0'),('1582589950360','parm','false','test.string;1.0.0','1582589950358','from',NULL,'test.integer;1.0.0'),('1582589950361','parm','false','test.string;1.0.0','1582589950358','to',NULL,'test.integer;1.0.0'),('1582589950362','out','false','test.string;1.0.0','1582589950358',NULL,NULL,'test.string;1.0.0'),('1582589950364','parm','false','test.url;1.0.0','1582589950363','aa',NULL,'test.text;1.0.0'),('1582589950365','parm','false','test.url;1.0.0','1582589950363','bb',NULL,'test.text;1.0.0'),('1582589950366','out','false','test.url;1.0.0','1582589950363',NULL,NULL,'test.text;1.0.0'),('1582589950368','parm','false','test.url;1.0.0','1582589950367','aa',NULL,'test.text;1.0.0'),('1582589950369','parm','false','test.url;1.0.0','1582589950367','bb',NULL,'test.text;1.0.0'),('1582589950370','out','false','test.url;1.0.0','1582589950367',NULL,NULL,'test.text;1.0.0'),('1582589950372','parm','false','test.url;1.0.0','1582589950371','aa',NULL,'test.text;1.0.0'),('1582589950373','parm','false','test.url;1.0.0','1582589950371','bb',NULL,'test.text;1.0.0'),('1582589950375','parm','false','test.url;1.1.0','1582589950374','aa',NULL,'test.text;1.0.0'),('1582589950376','parm','false','test.url;1.1.0','1582589950374','bb',NULL,'test.text;1.0.0'),('1582589950377','out','false','test.url;1.1.0','1582589950374',NULL,NULL,'test.text;1.0.0'),('1582589950379','parm','false','test.url;1.1.0','1582589950378','aa',NULL,'test.text;1.0.0'),('1582589950380','parm','false','test.url;1.1.0','1582589950378','bb',NULL,'test.text;1.0.0'),('1582589950381','out','false','test.url;1.1.0','1582589950378',NULL,NULL,'test.text;1.0.0'),('1582589950383','parm','true','test.url;1.1.0','1582589950382','aa',NULL,'test.url;1.1.0'),('1582589950384','parm','false','test.url;1.1.0','1582589950382','bb',NULL,'test.text;1.0.0'),('1582589950385','out','false','test.url;1.1.0','1582589950382',NULL,NULL,'test.text;1.0.0'),('1582589950387','parm','false','test.url;1.1.0','1582589950386','aa',NULL,'test.text;1.0.0'),('1582589950388','parm','false','test.url;1.1.0','1582589950386','bb',NULL,'test.text;1.0.0'),('1582589950390','parm','false','test.url;1.1.0','1582589950389','aa',NULL,'test.text;1.0.0'),('1582589950391','parm','false','test.url;1.1.0','1582589950389','bb',NULL,'test.text;1.0.0'),('1582589950393','parm','false','test.url;1.1.0','1582589950392','aa1',NULL,'test.text;1.0.0'),('1582589950394','parm','false','test.url;1.1.0','1582589950392','bb1',NULL,'test.text;1.0.0'),('1582589950395','out','false','test.url;1.1.0','1582589950392',NULL,NULL,'test.text;1.0.0'),('1582589950397','parm','false','test.url;1.1.0','1582589950396','aa2',NULL,'test.text;1.0.0'),('1582589950398','parm','false','test.url;1.1.0','1582589950396','bb2',NULL,'test.text;1.0.0'),('1582589950399','out','false','test.url;1.1.0','1582589950396',NULL,NULL,'test.text;1.0.0');
/*!40000 ALTER TABLE `operationparm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationship`
--

DROP TABLE IF EXISTS `relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `relationship` (
  `id` varchar(20) NOT NULL,
  `path` varchar(500) DEFAULT NULL,
  `sourceDataType_info` varchar(2000) DEFAULT NULL,
  `sourceDataType_isComplex` varchar(20) DEFAULT NULL,
  `sourceDataType_fullName` varchar(100) NOT NULL,
  `sourceDataType_name` varchar(100) NOT NULL,
  `sourceDataType_versionFullName` varchar(20) NOT NULL,
  `sourceDataType_versionMajor` int(11) DEFAULT NULL,
  `sourceDataType_versionMinor` int(11) DEFAULT NULL,
  `sourceDataType_versionRevision` varchar(20) DEFAULT NULL,
  `sourceDataType_parentsInfo` varchar(500) DEFAULT NULL,
  `sourceDataType_linkedVersion` varchar(20) DEFAULT NULL,
  `targetDataType_info` varchar(2000) DEFAULT NULL,
  `targetDataType_isComplex` varchar(20) DEFAULT NULL,
  `targetDataType_fullName` varchar(100) NOT NULL,
  `targetDataType_name` varchar(100) NOT NULL,
  `targetDataType_versionFullName` varchar(20) NOT NULL,
  `targetDataType_versionMajor` int(11) DEFAULT NULL,
  `targetDataType_versionMinor` int(11) DEFAULT NULL,
  `targetDataType_versionRevision` varchar(20) DEFAULT NULL,
  `targetDataType_parentsInfo` varchar(500) DEFAULT NULL,
  `targetDataType_linkedVersion` varchar(20) DEFAULT NULL,
  `targetType` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationship`
--

LOCK TABLES `relationship` WRITE;
/*!40000 ALTER TABLE `relationship` DISABLE KEYS */;
INSERT INTO `relationship` VALUES ('1582589950400','[]','{}','false','base.boolean;1.0.0','base.boolean','1.0.0',1,0,'0',NULL,NULL,'{}','false','base.boolean;1.0.0','base.boolean','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950401','[]','{}','false','base.data;1.0.0','base.data','1.0.0',1,0,'0',NULL,NULL,'{}','false','base.data;1.0.0','base.data','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950402','[]','{}','false','base.integer;1.0.0','base.integer','1.0.0',1,0,'0',NULL,NULL,'{}','false','base.integer;1.0.0','base.integer','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950403','[]','{}','false','base.number;1.0.0','base.number','1.0.0',1,0,'0',NULL,NULL,'{}','false','base.number;1.0.0','base.number','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950404','[]','{}','false','base.string;1.0.0','base.string','1.0.0',1,0,'0',NULL,NULL,'{}','false','base.string;1.0.0','base.string','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950405','[]','{}','false','common.entity;1.0.0','common.entity','1.0.0',1,0,'0',NULL,NULL,'{}','false','common.entity;1.0.0','common.entity','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950406','[]','{}','false','common.expression;1.0.0','common.expression','1.0.0',1,0,'0',NULL,NULL,'{}','false','common.expression;1.0.0','common.expression','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950407','[]','{}','false','common.geolocation;1.0.0','common.geolocation','1.0.0',1,0,'0',NULL,NULL,'{}','false','common.geolocation;1.0.0','common.geolocation','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950408','[]','{}','false','common.lengthunit;1.0.0','common.lengthunit','1.0.0',1,0,'0',NULL,NULL,'{}','false','common.lengthunit;1.0.0','common.lengthunit','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950409','[]','{}','false','core.array;1.0.0','core.array','1.0.0',1,0,'0',NULL,NULL,'{}','false','core.array;1.0.0','core.array','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950410','[]','{}','false','core.datatype;1.0.0','core.datatype','1.0.0',1,0,'0',NULL,NULL,'{}','false','core.datatype;1.0.0','core.datatype','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950411','[]','{}','false','core.distance;1.0.0','core.distance','1.0.0',1,0,'0',NULL,NULL,'{}','false','core.distance;1.0.0','core.distance','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950412','[]','{}','false','core.text;1.0.0','core.text','1.0.0',1,0,'0',NULL,NULL,'{}','false','core.text;1.0.0','core.text','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950413','[\"parent|base.string;1.0.0\"]','{}','false','core.url;1.0.0','core.url','1.0.0',1,0,'0','[\"base.string;1.0.0\",\"core.text;1.0.0\",\"base.boolean;1.0.0\"]',NULL,'{}','false','base.string;1.0.0','base.string','1.0.0',1,0,'0',NULL,NULL,'root'),('1582589950414','[\"parent|base.boolean;1.0.0\"]','{}','false','core.url;1.0.0','core.url','1.0.0',1,0,'0','[\"base.string;1.0.0\",\"core.text;1.0.0\",\"base.boolean;1.0.0\"]',NULL,'{}','false','base.boolean;1.0.0','base.boolean','1.0.0',1,0,'0',NULL,NULL,'root'),('1582589950415','[]','{}','false','core.url;1.0.0','core.url','1.0.0',1,0,'0','[\"base.string;1.0.0\",\"core.text;1.0.0\",\"base.boolean;1.0.0\"]',NULL,'{}','false','core.url;1.0.0','core.url','1.0.0',1,0,'0','[\"base.string;1.0.0\",\"core.text;1.0.0\",\"base.boolean;1.0.0\"]',NULL,'self'),('1582589950416','[\"parent|core.text;1.0.0\"]','{}','false','core.url;1.0.0','core.url','1.0.0',1,0,'0','[\"base.string;1.0.0\",\"core.text;1.0.0\",\"base.boolean;1.0.0\"]',NULL,'{}','false','core.text;1.0.0','core.text','1.0.0',1,0,'0',NULL,NULL,'root'),('1582589950417','[\"linked|1.0.0\",\"parent|base.boolean;1.0.0\"]','{}','false','core.url;1.1.0','core.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','base.boolean;1.0.0','base.boolean','1.0.0',1,0,'0',NULL,NULL,'intermedia'),('1582589950418','[\"linked|1.0.0\",\"parent|base.string;1.0.0\"]','{}','false','core.url;1.1.0','core.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','base.string;1.0.0','base.string','1.0.0',1,0,'0',NULL,NULL,'intermedia'),('1582589950419','[]','{}','false','core.url;1.1.0','core.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','core.url;1.1.0','core.url','1.1.0',1,1,'0',NULL,'1.0.0','self'),('1582589950420','[\"linked|1.0.0\",\"parent|core.text;1.0.0\"]','{}','false','core.url;1.1.0','core.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','core.text;1.0.0','core.text','1.0.0',1,0,'0',NULL,NULL,'intermedia'),('1582589950421','[\"linked|1.0.0\"]','{}','false','core.url;1.1.0','core.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','core.url;1.0.0','core.url','1.0.0',1,0,'0',NULL,NULL,'intermedia'),('1582589950422','[]','{}','false','test.array;1.0.0','test.array','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.array;1.0.0','test.array','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950423','[]','{}','false','test.boolean;1.0.0','test.boolean','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.boolean;1.0.0','test.boolean','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950424','[]','{}','false','test.dataTypeCriteria;1.0.0','test.dataTypeCriteria','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.dataTypeCriteria;1.0.0','test.dataTypeCriteria','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950425','[]','{}','false','test.date;1.0.0','test.date','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.date;1.0.0','test.date','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950426','[]','{}','false','test.distance;1.0.0','test.distance','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.distance;1.0.0','test.distance','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950427','[]','{}','false','test.expression;1.0.0','test.expression','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.expression;1.0.0','test.expression','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950428','[]','{}','false','test.float;1.0.0','test.float','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.float;1.0.0','test.float','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950429','[]','{}','false','test.geo;1.0.0','test.geo','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.geo;1.0.0','test.geo','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950430','[]','{}','false','test.integer;1.0.0','test.integer','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.integer;1.0.0','test.integer','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950431','[]','{}','true','test.map;1.0.0','test.map','1.0.0',1,0,'0',NULL,NULL,'{}','true','test.map;1.0.0','test.map','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950432','[]','{}','false','test.options;1.0.0','test.options','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL,'{}','false','test.options;1.0.0','test.options','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL,'self'),('1582589950433','[\"parent|test.string;1.0.0\"]','{}','false','test.options;1.0.0','test.options','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL,'{}','false','test.string;1.0.0','test.string','1.0.0',1,0,'0',NULL,NULL,'root'),('1582589950434','[]','{}','false','test.parm;1.0.0','test.parm','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.parm;1.0.0','test.parm','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950435','[]','{}','false','test.price;1.0.0','test.price','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.price;1.0.0','test.price','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950436','[]','{}','false','test.string;1.0.0','test.string','1.0.0',1,0,'0',NULL,NULL,'{}','false','test.string;1.0.0','test.string','1.0.0',1,0,'0',NULL,NULL,'self'),('1582589950437','[]','{}','false','test.url;1.0.0','test.url','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL,'{}','false','test.url;1.0.0','test.url','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL,'self'),('1582589950438','[\"parent|test.string;1.0.0\"]','{}','false','test.url;1.0.0','test.url','1.0.0',1,0,'0','[\"test.string;1.0.0\"]',NULL,'{}','false','test.string;1.0.0','test.string','1.0.0',1,0,'0',NULL,NULL,'root'),('1582589950439','[\"linked|1.0.0\"]','{}','false','test.url;1.1.0','test.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','test.url;1.0.0','test.url','1.0.0',1,0,'0',NULL,NULL,'intermedia'),('1582589950440','[\"linked|1.0.0\",\"parent|test.string;1.0.0\"]','{}','false','test.url;1.1.0','test.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','test.string;1.0.0','test.string','1.0.0',1,0,'0',NULL,NULL,'intermedia'),('1582589950441','[]','{}','false','test.url;1.1.0','test.url','1.1.0',1,1,'0',NULL,'1.0.0','{}','false','test.url;1.1.0','test.url','1.1.0',1,1,'0',NULL,'1.0.0','self');
/*!40000 ALTER TABLE `relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcedependency`
--

DROP TABLE IF EXISTS `resourcedependency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `resourcedependency` (
  `id` varchar(20) NOT NULL,
  `resourceId` varchar(100) NOT NULL,
  `dependency` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcedependency`
--

LOCK TABLES `resourcedependency` WRITE;
/*!40000 ALTER TABLE `resourcedependency` DISABLE KEYS */;
INSERT INTO `resourcedependency` VALUES ('1580152069874','operation|___simple_base.integer;1.0.0;add','[]'),('1580152069877','operation|___simple_base.string;1.0.0;subString','[\"operation|___simple_base.integer;1.0.0;add:[\"op1\"]\",\"jshelper|___simple_1580152069876:[\"globalHelper\"]\"]'),('1580152069880','operation|___simple_base.string;1.0.0;length','[\"operation|___simple_base.integer;new:[\"op1\"]\",\"jshelper|___simple_1580152069879:[\"globalHelper\"]\"]'),('1580152069882','operation|___simple_core.url;1.1.0;url_normal2','[\"operation|___simple_core.text;1.0.0;text_normal2:[\"op2\"]\",\"operation|___simple_core.text;1.0.0;text_normal1:[\"op1\"]\",\"jslibrary|___simple_underscore;1.8.3:[\"underline\"]\"]'),('1580152069884','operation|___simple_core.url;1.1.0;url_normal3','[]'),('1580152069886','operation|___simple_test.array;1.0.0;process','[\"jsGateway|___simple_discovery:[\"myGateWay\"]\"]'),('1580152069888','operation|___simple_test.array;1.0.0;getChildrenNames','[]'),('1580152069890','operation|___simple_test.array;1.0.0;isAccessChildById','[]'),('1580152069892','operation|___simple_test.array;1.0.0;length','[]'),('1580152069894','operation|___simple_test.array;1.0.0;getChildData','[]'),('1580152069896','operation|___simple_test.array;1.0.0;addChild','[]'),('1580152069898','operation|___simple_test.array;1.0.0;add','[]'),('1580152069900','operation|___simple_test.array;1.0.0;removeChild','[]'),('1580152069902','operation|___simple_test.array;1.0.0;getChildDataByIndex','[]'),('1580152069904','operation|___simple_test.array;1.0.0;emptyArray','[]'),('1580152069906','operation|___simple_test.array;1.0.0;new','[]'),('1580152069908','operation|___simple_test.boolean;1.0.0;opposite','[]'),('1580152069910','operation|___simple_test.datatypecriteria;1.0.0;getChild','[\"jsGateway|___simple_criteria:[\"myGateWay\"]\"]'),('1580152069912','operation|___simple_test.datatypecriteria;1.0.0;getChildren','[\"jsGateway|___simple_criteria:[\"myGateWay\"]\"]'),('1580152069914','operation|___simple_test.datatypecriteria;1.0.0;addChild','[\"jsGateway|___simple_criteria:[\"myGateWay\"]\"]'),('1580152069916','operation|___simple_test.distance;1.0.0;shorterThan','[]'),('1580152069918','operation|___simple_test.expression;1.0.0;outputCriteria','[\"jsGateway|___simple_discovery:[\"myGateWay\"]\"]'),('1580152069920','operation|___simple_test.expression;1.0.0;outputCriteriaFromParmData','[\"jsGateway|___simple_discovery:[\"myGateWay\"]\",\"operation|___simple_test.parm;1.0.0;getCriteria:[\"op1\"]\"]'),('1580152069922','operation|___simple_test.expression;1.0.0;execute','[\"jsGateway|___simple_discovery:[\"myGateWay\"]\"]'),('1580152069924','operation|___simple_test.geo;1.0.0;distance','[]'),('1580152069926','operation|___simple_test.integer;1.0.0;add','[]'),('1580152069928','operation|___simple_test.map;1.0.0;put','[]'),('1580152069930','operation|___simple_test.map;1.0.0;getChildrenNames','[]'),('1580152069932','operation|___simple_test.map;1.0.0;isAccessChildById','[]'),('1580152069934','operation|___simple_test.map;1.0.0;length','[]'),('1580152069936','operation|___simple_test.map;1.0.0;getChildData','[]'),('1580152069938','operation|___simple_test.map;1.0.0;setChildData','[]'),('1580152069940','operation|___simple_test.map;1.0.0;new','[]'),('1580152069942','operation|___simple_test.options;1.0.0;all','[]'),('1580152069944','converter|___simple_test.options;1.0.0;convert','[]'),('1580152069946','operation|___simple_test.parm;1.0.0;getValue','[]'),('1580152069948','operation|___simple_test.parm;1.0.0;getCriteria','[]'),('1580152069951','operation|___simple_test.string;1.0.0;emptyString','[\"jshelper|___simple_1580152069950:[\"globalHelper\"]\"]'),('1580152069954','operation|___simple_test.string;1.0.0;isEmpty','[\"jshelper|___simple_1580152069953:[\"globalHelper\"]\"]'),('1580152069957','operation|___simple_test.string;1.0.0;subString','[\"operation|___simple_test.integer;1.0.0;add:[\"op1\"]\",\"jshelper|___simple_1580152069956:[\"globalHelper\"]\"]'),('1580152069960','operation|___simple_test.string;1.0.0;length','[\"operation|___simple_test.integer;new:[\"op1\"]\",\"jshelper|___simple_1580152069959:[\"globalHelper\"]\"]'),('1580152069962','converter|___simple_test.url;1.0.0;convert','[]'),('1580152069964','operation|___simple_test.url;1.1.0;url_normal2','[\"jslibrary|___simple_underscore;1.8.3:[\"underline\"]\",\"operation|___simple_test.text;1.0.0;text_normal2:[\"op2\"]\",\"operation|___simple_test.text;1.0.0;text_normal1:[\"op1\"]\"]'),('1580152069966','operation|___simple_test.url;1.1.0;url_normal3','[]'),('1580152069968','converter|___simple_test.url;1.1.0;convert','[\"jslibrary|___simple_underscore;1.8.3:[\"underline\"]\",\"operation|___simple_test.text;1.0.0;text_normal1:[\"op1\"]\",\"operation|___simple_test.text;1.0.0;text_normal2:[\"op2\"]\"]');
/*!40000 ALTER TABLE `resourcedependency` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-15 13:22:58
