/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80017 (8.0.17)
 Source Host           : localhost:3306
 Source Schema         : seata-tc

 Target Server Type    : MySQL
 Target Server Version : 80017 (8.0.17)
 File Encoding         : 65001

 Date: 26/09/2023 21:44:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table`  (
                                 `branch_id` bigint(20) NOT NULL,
                                 `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `transaction_id` bigint(20) NULL DEFAULT NULL,
                                 `resource_group_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `branch_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `status` tinyint(4) NULL DEFAULT NULL,
                                 `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `gmt_create` datetime(6) NULL DEFAULT NULL,
                                 `gmt_modified` datetime(6) NULL DEFAULT NULL,
                                 PRIMARY KEY (`branch_id`) USING BTREE,
                                 INDEX `idx_xid`(`xid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of branch_table
-- ----------------------------

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
DROP TABLE IF EXISTS `global_table`;
CREATE TABLE `global_table`  (
                                 `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `transaction_id` bigint(20) NULL DEFAULT NULL,
                                 `status` tinyint(4) NOT NULL,
                                 `application_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `transaction_service_group` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `transaction_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `timeout` int(11) NULL DEFAULT NULL,
                                 `begin_time` bigint(20) NULL DEFAULT NULL,
                                 `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `gmt_create` datetime NULL DEFAULT NULL,
                                 `gmt_modified` datetime NULL DEFAULT NULL,
                                 PRIMARY KEY (`xid`) USING BTREE,
                                 INDEX `idx_gmt_modified_status`(`gmt_modified` ASC, `status` ASC) USING BTREE,
                                 INDEX `idx_transaction_id`(`transaction_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of global_table
-- ----------------------------

-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
DROP TABLE IF EXISTS `lock_table`;
CREATE TABLE `lock_table`  (
                               `row_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                               `xid` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                               `transaction_id` bigint(20) NULL DEFAULT NULL,
                               `branch_id` bigint(20) NOT NULL,
                               `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                               `table_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                               `pk` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                               `gmt_create` datetime NULL DEFAULT NULL,
                               `gmt_modified` datetime NULL DEFAULT NULL,
                               PRIMARY KEY (`row_key`) USING BTREE,
                               INDEX `idx_branch_id`(`branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lock_table
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;