/*
Navicat MySQL Data Transfer

Source Server         : myfirst
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : who_is_spy

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2017-08-26 15:40:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creater_id` varchar(255) NOT NULL,
  `room_id` int(4) NOT NULL,
  `sum_peoples_nums` int(3) DEFAULT '0',
  `word_1` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `word_2` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `unused_time` timestamp NULL DEFAULT NULL,
  `spy_1_id` int(2) DEFAULT NULL,
  `spy_2_id` int(2) DEFAULT NULL,
  `spy_3_id` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `room_id` int(4) NOT NULL,
  `character_id` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for words
-- ----------------------------
DROP TABLE IF EXISTS `words`;
CREATE TABLE `words` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_one` varchar(255) DEFAULT NULL,
  `word_two` varchar(255) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
