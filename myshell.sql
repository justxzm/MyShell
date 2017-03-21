/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50523
Source Host           : localhost:3306
Source Database       : myshell

Target Server Type    : MYSQL
Target Server Version : 50523
File Encoding         : 65001

Date: 2017-03-21 14:05:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
