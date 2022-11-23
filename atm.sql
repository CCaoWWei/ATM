/*
 Navicat Premium Data Transfer

 Source Server         : guwenzi
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : atm

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 23/11/2022 14:53:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adminaccount
-- ----------------------------
DROP TABLE IF EXISTS `adminaccount`;
CREATE TABLE `adminaccount`  (
  `ID` int(0) NOT NULL,
  `PASSWORD` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adminaccount
-- ----------------------------
INSERT INTO `adminaccount` VALUES (10000000, '000000');

-- ----------------------------
-- Table structure for customeraccount
-- ----------------------------
DROP TABLE IF EXISTS `customeraccount`;
CREATE TABLE `customeraccount`  (
  `ID` int(0) NOT NULL AUTO_INCREMENT,
  `PASSWORD` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0',
  `Balance` float NULL DEFAULT 0,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customeraccount
-- ----------------------------
INSERT INTO `customeraccount` VALUES (1000000000, '123456', 18200);
INSERT INTO `customeraccount` VALUES (1000000001, '000000', 58000);

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `ID` int(0) NOT NULL AUTO_INCREMENT,
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `amount` int(0) NULL DEFAULT NULL,
  `outid` int(0) NULL DEFAULT NULL,
  `inid` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, '2022/11/23/12:28', 50, 1000000000, 1000000001);

SET FOREIGN_KEY_CHECKS = 1;
