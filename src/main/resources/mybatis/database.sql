create database if not exists hashtag_articles default charset utf8;

/* websites */
CREATE TABLE `hashtag_articles`.`websites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `domainUrl` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `domainUrl` (`domainUrl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* topics */
CREATE TABLE `hashtag_articles`.`topics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `fromUrl` varchar(64) NOT NULL,
  `parentId` int(11) NOT NULL DEFAULT '-1',
  `mappedId` int(11) NOT NULL DEFAULT '-1',
  `type` int(4) NOT NULL DEFAULT '-1',
  `siteId` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fromUrl` (`fromUrl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* articles */
CREATE TABLE `hashtag_articles`.`articles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(250) NOT NULL,
  `title` varchar(1024) DEFAULT NULL,
  `subTitle` varchar(1024) DEFAULT NULL,
  `crawlDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `imageUrls` text,
  `authors` varchar(512) DEFAULT NULL,
  `paragraphs` text,
  `topicId` int(11) DEFAULT '0',
  `rawTopicId` int(11) DEFAULT '0',
  `status` int(4) DEFAULT '0',
  `siteId` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
