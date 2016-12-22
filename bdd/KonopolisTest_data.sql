CREATE DATABASE  IF NOT EXISTS `konopolisTest` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `konopolisTest`;

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

DROP TABLE IF EXISTS `tbadmins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbadmins` (
  `username` char(30) NOT NULL,
  `hash` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbadmins` WRITE;
/*!40000 ALTER TABLE `tbadmins` DISABLE KEYS */;
INSERT INTO `tbadmins` VALUES ('admin','$2a$16$i6nzrUCs8ISb1Gg3ajzSF.2tgi4y2ZF04eWitLQ6Zqpsoegbh07ta');
/*!40000 ALTER TABLE `tbadmins` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbcasts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbcasts` (
  `cast_id` int(11) NOT NULL AUTO_INCREMENT,
  `cast` char(30) NOT NULL,
  PRIMARY KEY (`cast_id`),
  UNIQUE KEY `ctUnique` (`cast`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbcasts` WRITE;
/*!40000 ALTER TABLE `tbcasts` DISABLE KEYS */;
INSERT INTO `tbcasts` VALUES (1,'Charlotte Gainsbourg'),(2,'Omar Sy'),(3,'Tahar Rahim');
/*!40000 ALTER TABLE `tbcasts` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbcustomers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbcustomers` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `sRow` int(11) NOT NULL,
  `sColumn` int(11) NOT NULL,
  `movie_room_id` int(11) NOT NULL,
  `customer_type_id` int(11) NOT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `movie_room_id` (`movie_room_id`),
  KEY `customer_type_id` (`customer_type_id`),
  CONSTRAINT `tbcustomers_ibfk_1` FOREIGN KEY (`movie_room_id`) REFERENCES `tbmoviesrooms` (`movie_room_id`) ON UPDATE CASCADE,
  CONSTRAINT `tbcustomers_ibfk_2` FOREIGN KEY (`customer_type_id`) REFERENCES `tbcustomerstype` (`customer_type_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbcustomers` WRITE;
/*!40000 ALTER TABLE `tbcustomers` DISABLE KEYS */;
INSERT INTO `tbcustomers` VALUES (1,1,1,1,4),(2,1,3,1,1),(3,2,10,1,3),(4,1,4,1,2);
/*!40000 ALTER TABLE `tbcustomers` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbcustomerstype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbcustomerstype` (
  `customer_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_type` char(30) NOT NULL,
  `reduction` double NOT NULL,
  PRIMARY KEY (`customer_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbcustomerstype` WRITE;
/*!40000 ALTER TABLE `tbcustomerstype` DISABLE KEYS */;
INSERT INTO `tbcustomerstype` VALUES (1,'Junior',0.3),(2,'Etudiant',0.4),(3,'Senior',0.5),(4,'VIP',0.7),(5,'Normal',0.0);
/*!40000 ALTER TABLE `tbcustomerstype` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbgenres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbgenres` (
  `genre_id` int(11) NOT NULL AUTO_INCREMENT,
  `genre` char(30) NOT NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `grUnique` (`genre`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbgenres` WRITE;
/*!40000 ALTER TABLE `tbgenres` DISABLE KEYS */;
INSERT INTO `tbgenres` VALUES (1,'Action'),(2,'Comédie'),(3,'Drame');
/*!40000 ALTER TABLE `tbgenres` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbincomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbincomes` (
  `incomes` double NOT NULL,
  PRIMARY KEY (`incomes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbincomes` WRITE;
/*!40000 ALTER TABLE `tbincomes` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbincomes` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tblanguages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tblanguages` (
  `language_id` int(11) NOT NULL AUTO_INCREMENT,
  `language` char(30) NOT NULL,
  PRIMARY KEY (`language_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tblanguages` WRITE;
/*!40000 ALTER TABLE `tblanguages` DISABLE KEYS */;
INSERT INTO `tblanguages` VALUES (1,'français'),(2,'anglais'),(3,'espagnol');
/*!40000 ALTER TABLE `tblanguages` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbmovies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbmovies` (
  `movie_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` char(50) NOT NULL,
  `description` mediumtext NOT NULL,
  `director` char(30) NOT NULL,
  `time` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`movie_id`),
  UNIQUE KEY `mvUnique` (`title`),
  KEY `language_id` (`language_id`),
  CONSTRAINT `tbmovies_ibfk_1` FOREIGN KEY (`language_id`) REFERENCES `tblanguages` (`language_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbmovies` WRITE;
/*!40000 ALTER TABLE `tbmovies` DISABLE KEYS */;
INSERT INTO `tbmovies` VALUES (1, 'Samba', 'Samba, sénégalais en France depuis 10 ans, collectionne les petits boulots. Alice est une cadre supérieure épuisée par un burn out. Lui essaye par tous les moyens d''obtenir ses papiers, alors qu''elle tente de se reconstruire par le bénévolat dans une association. Chacun cherche à sortir de son impasse jusqu''au jour où leurs destins se croisent... Entre humour et émotion, leur histoire se fraye un autre chemin vers le bonheur. Et si la vie avait plus d''imagination qu''eux ?', 'Eric Toledano',118,1,10);
/*!40000 ALTER TABLE `tbmovies` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbmoviescasts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbmoviescasts` (
  `movie_cast_id` int(11) NOT NULL AUTO_INCREMENT,
  `movie_id` int(11) NOT NULL,
  `cast_id` int(11) NOT NULL,
  PRIMARY KEY (`movie_cast_id`),
  KEY `movie_id` (`movie_id`),
  KEY `cast_id` (`cast_id`),
  CONSTRAINT `tbmoviescasts_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `tbmovies` (`movie_id`) ON UPDATE CASCADE,
  CONSTRAINT `tbmoviescasts_ibfk_2` FOREIGN KEY (`cast_id`) REFERENCES `tbcasts` (`cast_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbmoviescasts` WRITE;
/*!40000 ALTER TABLE `tbmoviescasts` DISABLE KEYS */;
INSERT INTO `tbmoviescasts` VALUES (1,1,1),(2,1,2),(3,1,3);
/*!40000 ALTER TABLE `tbmoviescasts` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbmoviesgenres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbmoviesgenres` (
  `movie_genre_id` int(11) NOT NULL AUTO_INCREMENT,
  `movie_id` int(11) NOT NULL,
  `genre_id` int(11) NOT NULL,
  PRIMARY KEY (`movie_genre_id`),
  KEY `movie_id` (`movie_id`),
  KEY `genre_id` (`genre_id`),
  CONSTRAINT `tbmoviesgenres_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `tbmovies` (`movie_id`) ON UPDATE CASCADE,
  CONSTRAINT `tbmoviesgenres_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `tbgenres` (`genre_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbmoviesgenres` WRITE;
/*!40000 ALTER TABLE `tbmoviesgenres` DISABLE KEYS */;
INSERT INTO `tbmoviesgenres` VALUES (1,1,1),(2,1,2);
/*!40000 ALTER TABLE `tbmoviesgenres` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbmoviesrooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbmoviesrooms` (
  `movie_room_id` int(11) NOT NULL AUTO_INCREMENT,
  `movie_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `show_start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`movie_room_id`),
  KEY `movie_id` (`movie_id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `tbmoviesrooms_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `tbmovies` (`movie_id`) ON UPDATE CASCADE,
  CONSTRAINT `tbmoviesrooms_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `tbrooms` (`room_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbmoviesrooms` WRITE;
/*!40000 ALTER TABLE `tbmoviesrooms` DISABLE KEYS */;
INSERT INTO `tbmoviesrooms` VALUES (1,1,2,'2016-12-19 14:30:00'),(2,1,2,'2016-12-20 16:15:00');
/*!40000 ALTER TABLE `tbmoviesrooms` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tbrooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbrooms` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `rows` int(11) NOT NULL,
  `seats_by_row` int(11) NOT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tbrooms` WRITE;
/*!40000 ALTER TABLE `tbrooms` DISABLE KEYS */;
INSERT INTO `tbrooms` VALUES (1,15,30),(2,10,10),(3,10,15),(4,15,8);
/*!40000 ALTER TABLE `tbrooms` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

