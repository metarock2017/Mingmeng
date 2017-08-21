/*
Navicat MySQL Data Transfer

Source Server         : myfirst
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : student

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-08-19 21:02:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `stuId` varchar(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `gender` tinyint(1) unsigned NOT NULL,
  `grade` int(4) unsigned NOT NULL,
  `college` varchar(50) NOT NULL,
  `major` varchar(100) NOT NULL,
  `class` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `stunum` (`stuId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18912 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `questionForProtected` varchar(255) DEFAULT NULL,
  `answerForProtected` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
