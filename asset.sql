/*
 Navicat Premium Dump SQL

 Source Server         : 77
 Source Server Type    : MySQL
 Source Server Version : 90000 (9.0.0)
 Source Host           : localhost:3306
 Source Schema         : asset

 Target Server Type    : MySQL
 Target Server Version : 90000 (9.0.0)
 File Encoding         : 65001

 Date: 08/11/2025 13:47:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset_categories
-- ----------------------------
DROP TABLE IF EXISTS `asset_categories`;
CREATE TABLE `asset_categories`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` int NULL DEFAULT NULL COMMENT '上级分类ID，0为根节点',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '分类名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of asset_categories
-- ----------------------------
INSERT INTO `asset_categories` VALUES (2, 8, '更新后的电子产品', '更新后的备注信息', '2025-10-10 16:49:57', '2025-10-29 17:13:14');
INSERT INTO `asset_categories` VALUES (3, 0, '更新后的电子产品', '更新后的备注信息', '2025-10-29 14:54:27', '2025-11-07 16:51:02');
INSERT INTO `asset_categories` VALUES (4, 10, 'xxx', 'xxxxx', '2025-10-29 14:54:27', '2025-10-29 17:14:55');
INSERT INTO `asset_categories` VALUES (6, 3, '8888', '', '2025-10-29 17:01:48', '2025-10-29 17:01:48');
INSERT INTO `asset_categories` VALUES (8, 2, '8888', '', '2025-10-29 17:02:13', '2025-10-29 17:02:13');
INSERT INTO `asset_categories` VALUES (9, 9, 'aaa', 'aaaaa', '2025-10-29 17:13:31', '2025-10-29 17:13:39');
INSERT INTO `asset_categories` VALUES (10, 0, 'ABC', '77777', '2025-10-29 17:14:16', '2025-10-29 17:15:02');
INSERT INTO `asset_categories` VALUES (11, 4, 'aaa', '', '2025-10-29 19:22:18', '2025-10-29 19:22:18');
INSERT INTO `asset_categories` VALUES (12, 10, 'cccc', '', '2025-10-29 21:33:47', '2025-10-29 21:33:47');

-- ----------------------------
-- Table structure for cat
-- ----------------------------
DROP TABLE IF EXISTS `cat`;
CREATE TABLE `cat`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cat
-- ----------------------------
INSERT INTO `cat` VALUES (1, '小白', '母', 3);

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '部门编码',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '部门名称',
  `parent_id` int NULL DEFAULT NULL COMMENT '上级部门ID 0为根节点',
  `level` tinyint NULL DEFAULT NULL COMMENT '部门层级',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '部门路径 0-1-2',
  `manager_id` bigint NULL DEFAULT NULL COMMENT '部门负责人ID',
  `contact` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '联系电话',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态1-启用 2-停用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (2, '', '测试部门名称777', 0, 4, '', 1, '联系人信息', 1);

-- ----------------------------
-- Table structure for dog
-- ----------------------------
DROP TABLE IF EXISTS `dog`;
CREATE TABLE `dog`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL,
  `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dog
-- ----------------------------
INSERT INTO `dog` VALUES (1, '二哈', '哈士奇', '公', 1);
INSERT INTO `dog` VALUES (2, '大黄 ', '中环田园犬', '母', 2);
INSERT INTO `dog` VALUES (3, '小黑', '博美', '母', 2);
INSERT INTO `dog` VALUES (4, '小白', '萨摩耶', '母', 2);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '用户编码',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '登陆账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '加密密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NOT NULL COMMENT '真实姓名',
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL COMMENT '头像URL',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别 1-男 2-女 3-未知',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '用户类型 0-管理员 1-员工',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '邮箱地址',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '手机号码',
  `status` tinyint NULL DEFAULT NULL COMMENT '账号状态 1-启用 2-禁用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_zh_0900_as_cs NULL DEFAULT NULL COMMENT '最后登录ip',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_zh_0900_as_cs ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '001', 'admire', '123456', '超级管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES (5, '777', '七七', '929577', '七七', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES (7, 'e3fb23a2-0db6-464d-b2a4-50a9b200f7c6', '宿开慧', 'dOsVKCOIQboyNfT', '水治涛', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-24 15:14:50', NULL);
INSERT INTO `users` VALUES (8, '52cc1b13-0de1-4efb-99d4-78d810d95f39', '霜建军', 'yxbzTS8PQF6i5Lw', '琦梓涵', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-24 15:14:53', NULL);
INSERT INTO `users` VALUES (9, '4b5527fd-2846-483f-906f-00448f08364a', '九国良', 'bb_7Ef83notekyK', '喜玲', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-24 15:14:56', NULL);
INSERT INTO `users` VALUES (10, '1780cfa0-ec84-4471-ba92-dc735dd81014', '危家明', '2b2eKZZPMAFjPYZ', '广梓涵', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-24 15:14:59', NULL);
INSERT INTO `users` VALUES (11, '9545a58a-c9b7-42de-8313-846f2b7b3460', '慈治涛', 'TrgDg2xr9PUKLJp', '候军', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-24 15:15:01', NULL);
INSERT INTO `users` VALUES (12, '21961455-b373-4966-b0ec-fd6a40f41b00', '扬苡沫', 'STS22Yp1dQFmr_Q', '疏智杰', NULL, NULL, NULL, '122222222', '41222222222', 2, NULL, NULL, '2025-10-24 15:15:02', '2025-10-24 17:10:42');
INSERT INTO `users` VALUES (21, NULL, '1111111', '122222222', '333333', '', 1, '1', '444444444', '7777777777777', 1, NULL, NULL, '2025-10-24 16:58:33', '2025-10-24 17:10:34');
INSERT INTO `users` VALUES (22, 'U1D60A659', '123333333333', '124111111111', '124', '', 1, '1', '124124', '124124124124124', 1, NULL, NULL, '2025-10-24 17:10:55', '2025-10-24 17:10:55');
INSERT INTO `users` VALUES (23, 'UF1FA5AFA', '421412412', '421412', '124124', '', 1, '1', '124', '124124124', 1, NULL, NULL, '2025-10-24 17:11:00', '2025-10-24 17:11:00');
INSERT INTO `users` VALUES (24, 'U93352BCE', '123123', '123123123', '213123123', '16491676-edfd-4095-9017-ab7537a7381b.png', 2, '1', '123123123', '123123123', 1, NULL, NULL, '2025-10-24 18:26:27', '2025-10-24 18:26:27');

SET FOREIGN_KEY_CHECKS = 1;
