/*
Navicat MySQL Data Transfer

Source Server         : AAC_dev
Source Server Version : 50724
Source Host           : 118.24.58.147:3306
Source Database       : aac

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-31 10:30:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for brief_text
-- ----------------------------
DROP TABLE IF EXISTS `brief_text`;
CREATE TABLE `brief_text` (
  `key` varchar(32) NOT NULL COMMENT 'key:\r\nELEMENTARY_FORCE --元力简介\r\nPLATFORM_CURRENCY --平台币简介\r\nFOCUS_ON_WECHAT --关注微信\r\nANSWER_GAME --答题游戏',
  `name` varchar(50) NOT NULL COMMENT '简介名称',
  `content` text NOT NULL COMMENT '简介内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='简介文本';

-- ----------------------------
-- Table structure for currency_mall_index_info
-- ----------------------------
DROP TABLE IF EXISTS `currency_mall_index_info`;
CREATE TABLE `currency_mall_index_info` (
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `trad_volume` int(10) DEFAULT NULL COMMENT '最新24H交易量',
  `yesterday_platform_price` decimal(15,2) DEFAULT NULL COMMENT '昨日收盘价',
  `last_platform_price` decimal(15,2) DEFAULT NULL COMMENT '最新收盘价',
  `create_time` bigint(13) NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='币商城首页列表';

-- ----------------------------
-- Table structure for increase_strategy
-- ----------------------------
DROP TABLE IF EXISTS `increase_strategy`;
CREATE TABLE `increase_strategy` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(10) NOT NULL COMMENT '策略名称',
  `increased_power_point` tinyint(2) NOT NULL COMMENT '昨日增长元力（基数）',
  `consumed_ad` tinyint(2) NOT NULL COMMENT '昨日广告有效点击率或提交信息总数量（基数）',
  `platform_currency` decimal(4,2) NOT NULL COMMENT '昨日AAC数量（基数）',
  `is_default` tinyint(1) NOT NULL COMMENT '是否默认算法\r\n1: 是\r\n2: 否',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) NOT NULL COMMENT '最新更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_n` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='自然增长策略';

-- ----------------------------
-- Table structure for lock_money_transaction
-- ----------------------------
DROP TABLE IF EXISTS `lock_money_transaction`;
CREATE TABLE `lock_money_transaction` (
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `order_id` int(10) NOT NULL COMMENT '挂单ID',
  `price` decimal(15,2) NOT NULL COMMENT '单价',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货币交易当前数据锁';

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) NOT NULL COMMENT '真实姓名',
  `merchant_name` varchar(32) DEFAULT NULL COMMENT '商户名称',
  `merchant_visit_url` varchar(255) DEFAULT NULL COMMENT '商家二维码连接',
  `gender` tinyint(1) NOT NULL COMMENT '性别\r\n1: 男\r\n2: 女',
  `id_number` varchar(32) NOT NULL COMMENT '身份证号',
  `mobile_number` bigint(11) NOT NULL COMMENT '电话号码',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='商户基础';

-- ----------------------------
-- Table structure for merchant_assert_issuance
-- ----------------------------
DROP TABLE IF EXISTS `merchant_assert_issuance`;
CREATE TABLE `merchant_assert_issuance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `merchant_id` int(10) NOT NULL COMMENT '商家ID',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `total` decimal(15,2) NOT NULL COMMENT '总发行量',
  `sell_rate` decimal(5,2) NOT NULL COMMENT '出售占比',
  `mining_rate` decimal(5,2) NOT NULL COMMENT '挖矿占比',
  `fixed_income_rate` decimal(5,2) DEFAULT NULL COMMENT '固定收益占比',
  `sto_dividend_rate` decimal(5,2) DEFAULT NULL COMMENT 'STO分红占比',
  `other_mode` tinyint(1) DEFAULT NULL COMMENT '其他模式',
  `income_period` int(4) NOT NULL COMMENT '收益周期(天)',
  `restriction_period` int(4) NOT NULL COMMENT '投资限售期(天)',
  `introduction` text NOT NULL COMMENT '简介',
  `white_paper_url` varchar(64) DEFAULT NULL COMMENT '白皮书URL',
  `issuing_date` bigint(13) NOT NULL COMMENT '发行日期',
  `service_charge_id` int(10) DEFAULT NULL COMMENT '服务费策略ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_c` (`currency_symbol`) USING BTREE,
  UNIQUE KEY `uni_m` (`merchant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='商户货币发行';

-- ----------------------------
-- Table structure for merchant_assert_issuance_audit
-- ----------------------------
DROP TABLE IF EXISTS `merchant_assert_issuance_audit`;
CREATE TABLE `merchant_assert_issuance_audit` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `issuance_id` int(10) NOT NULL COMMENT '商家发币信息ID',
  `status` tinyint(1) NOT NULL COMMENT '状态\r\n1. 发币待审核\r\n6: 发币审核失败\r\n2: 发币审核通过，待付押金\r\n3: 押金待审核\r\n4: 押金审核失败\r\n5: 押金审核通过',
  `audit_comment` varchar(100) DEFAULT NULL COMMENT '审核批注',
  `request_time` bigint(13) NOT NULL COMMENT '申请时间',
  `audit_time` bigint(13) DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_m` (`issuance_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='商户货币发行审核';

-- ----------------------------
-- Table structure for merchant_assert_latest_data
-- ----------------------------
DROP TABLE IF EXISTS `merchant_assert_latest_data`;
CREATE TABLE `merchant_assert_latest_data` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `max_price` decimal(15,2) DEFAULT NULL COMMENT '最高价',
  `min_price` decimal(15,2) DEFAULT NULL COMMENT '最低价',
  `recent_price` decimal(15,2) DEFAULT NULL COMMENT '最后一笔单价',
  `open_price` decimal(15,2) DEFAULT NULL COMMENT '开盘价',
  `yesterday_close_price` decimal(15,2) DEFAULT NULL COMMENT '昨日收盘价',
  `platform_trad_num` decimal(15,2) DEFAULT NULL COMMENT 'APP端成交币数',
  `applet_trad_num` decimal(15,2) DEFAULT NULL COMMENT '小程序端成交币数',
  `currency_num` int(10) DEFAULT NULL COMMENT '成交总笔数',
  `count_type` tinyint(1) NOT NULL COMMENT '统计类型：（1、日---2、周---3、月）',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='商户资产最新数据';

-- ----------------------------
-- Table structure for merchant_assert_statistics
-- ----------------------------
DROP TABLE IF EXISTS `merchant_assert_statistics`;
CREATE TABLE `merchant_assert_statistics` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `merchant_id` int(10) NOT NULL COMMENT '商户ID',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `key` varchar(32) NOT NULL COMMENT '键\r\nUNRESTRICTED: 是否已过限售期（1-是；2-否）\r\nTRADED:       交易次数\r\nMINING_MIND:  被挖量\r\nMINING_REST:  剩余可挖量\r\nSELL_SOLD:    已售出量\r\nSELL_REST:    剩余可售量',
  `value` decimal(15,2) NOT NULL COMMENT '值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_mck` (`merchant_id`,`currency_symbol`,`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='商户资产统计';

-- ----------------------------
-- Table structure for merchant_assert_today_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `merchant_assert_today_trade_record`;
CREATE TABLE `merchant_assert_today_trade_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `trade_type` tinyint(1) NOT NULL COMMENT '交易类型\r\n1: 买入\r\n2: 售出\r\n3: 转账',
  `platform_price` decimal(15,2) NOT NULL COMMENT '平台币单价（每一个单位的商户币兑换多少平台币）',
  `trade_amount` decimal(15,2) NOT NULL COMMENT '交易额度',
  `trade_time` bigint(13) NOT NULL COMMENT '交易时间',
  `order_id` int(10) NOT NULL COMMENT '挂单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='商户币今日成交数据';

-- ----------------------------
-- Table structure for merchant_assert_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `merchant_assert_trade_record`;
CREATE TABLE `merchant_assert_trade_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `initiator_id` int(10) NOT NULL COMMENT '交易发起人ID',
  `initiator_name` varchar(32) NOT NULL COMMENT '交易发起人名称',
  `trade_type` tinyint(1) NOT NULL COMMENT '交易类型\r\n1: 买入\r\n2: 售出\r\n3: 转账',
  `trade_time` bigint(13) NOT NULL COMMENT '交易时间',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `platform_price` decimal(15,2) NOT NULL COMMENT '平台币单价（每一个单位的商户币兑换多少平台币）',
  `trade_amount` decimal(15,2) NOT NULL COMMENT '交易额度',
  `balance` decimal(15,2) NOT NULL COMMENT '交易完成后的平台币余额',
  `valid_balance` decimal(15,2) NOT NULL COMMENT '交易完成后的平台币有效余额',
  `merchant_balance` decimal(15,2) NOT NULL COMMENT '交易完成后商家币余额',
  `merchant_valid_balance` decimal(15,2) NOT NULL COMMENT '交易完成后商家币有效余额',
  `partner_id` int(10) NOT NULL COMMENT '交易伙伴ID',
  `partner_name` varchar(32) NOT NULL COMMENT '交易伙伴名称',
  `trade_result` varchar(32) DEFAULT NULL COMMENT '消耗流向',
  `order_id` int(10) DEFAULT NULL COMMENT '商家币挂单ID',
  PRIMARY KEY (`id`),
  KEY `idx_t` (`trade_type`),
  KEY `idx_c` (`currency_symbol`) USING BTREE,
  KEY `idx_time` (`trade_time`) USING BTREE,
  KEY `idx_i` (`initiator_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='商户币交易记录';

-- ----------------------------
-- Table structure for merchant_currency_statistics
-- ----------------------------
DROP TABLE IF EXISTS `merchant_currency_statistics`;
CREATE TABLE `merchant_currency_statistics` (
  `merchant_id` int(10) NOT NULL COMMENT '商家ID',
  `asset_type` tinyint(1) NOT NULL COMMENT '资产类型\r\n1、活跃存量\r\n2、冻结存量\r\n3、挖矿存量',
  `currency_num` decimal(15,2) NOT NULL COMMENT '总币数',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户货币统计表';

-- ----------------------------
-- Table structure for merchant_dividend_record
-- ----------------------------
DROP TABLE IF EXISTS `merchant_dividend_record`;
CREATE TABLE `merchant_dividend_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `merchant_id` int(10) NOT NULL COMMENT '商家ID',
  `dividend_date` bigint(13) NOT NULL COMMENT '分红日期',
  `dividend_Issue_date` bigint(13) DEFAULT NULL COMMENT '分红发放日期',
  `profit_amount` decimal(15,2) DEFAULT NULL COMMENT '盈利额（平台币）',
  `dividend_amount` decimal(15,2) DEFAULT NULL COMMENT '本期分红总金额 （平台币）',
  `status` bigint(1) NOT NULL COMMENT '本期分红状态\r\n1、未到分红日\r\n2、分红待发放\r\n3、已完成分红\r\n4、逾期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='商户分红信息表';

-- ----------------------------
-- Table structure for merchant_dividend_temp_data
-- ----------------------------
DROP TABLE IF EXISTS `merchant_dividend_temp_data`;
CREATE TABLE `merchant_dividend_temp_data` (
  `merchant_user_id` int(10) NOT NULL COMMENT '商家的个人ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `amount` decimal(15,2) NOT NULL COMMENT '分红金额'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户分红临时数据';

-- ----------------------------
-- Table structure for merchant_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `merchant_trade_record`;
CREATE TABLE `merchant_trade_record` (
  `merchant_id` int(10) NOT NULL COMMENT '商家ID',
  `user_id` int(10) DEFAULT NULL COMMENT '用户ID',
  `transaction_mode` tinyint(1) NOT NULL COMMENT '交易方式：\r\n流失\r\n1、小程序充值\r\n2、个人转币\r\n3、挂卖单\r\n增长\r\n4、取消卖单\r\n5、店内消费\r\n6、挂买单成交\r\n7、直接回购\r\n8、个人挖矿',
  `flow_direction` tinyint(1) NOT NULL COMMENT '流通方向：\r\n1、账户冻结存量\r\n2、市场流通量\r\n3、账户挖矿存量',
  `trade_amount` decimal(15,2) NOT NULL COMMENT '交易额度',
  `platform_price` decimal(15,2) DEFAULT NULL COMMENT '平台币单价（每一个单位的商户币兑换多少平台币）',
  `add_subtract` tinyint(1) NOT NULL COMMENT '流失增长\r\n1、流失\r\n2、增长',
  `trade_time` bigint(13) NOT NULL COMMENT '交易时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户交易记录';

-- ----------------------------
-- Table structure for mining_answer
-- ----------------------------
DROP TABLE IF EXISTS `mining_answer`;
CREATE TABLE `mining_answer` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `question_id` int(10) NOT NULL COMMENT '题目ID',
  `name` varchar(10) NOT NULL,
  `order_number` tinyint(1) NOT NULL COMMENT '答案序号',
  `is_correct` tinyint(1) NOT NULL COMMENT '是否是正确答案\r\n1: 是\r\n2: 否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='挖矿题目答案';

-- ----------------------------
-- Table structure for mining_question
-- ----------------------------
DROP TABLE IF EXISTS `mining_question`;
CREATE TABLE `mining_question` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `is_single_choice` tinyint(1) NOT NULL COMMENT '是否是单选题\r\n1: 是\r\n2: 否',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `power_point_bonus` int(3) NOT NULL COMMENT '答对奖励元力值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='挖矿题目';

-- ----------------------------
-- Table structure for operator
-- ----------------------------
DROP TABLE IF EXISTS `operator`;
CREATE TABLE `operator` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `login_name` varchar(20) NOT NULL COMMENT '帐号',
  `password` varchar(20) NOT NULL COMMENT '密码',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `department` varchar(10) NOT NULL COMMENT '部门',
  `status` tinyint(1) NOT NULL COMMENT '状态\r\n1: 启用\r\n2: 停用',
  `role` tinyint(1) NOT NULL COMMENT '角色\r\n1: 普通运营\r\n2: 超级管理员',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) NOT NULL COMMENT '最新更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_l` (`login_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='操作员基础';

-- ----------------------------
-- Table structure for operator_action_log
-- ----------------------------
DROP TABLE IF EXISTS `operator_action_log`;
CREATE TABLE `operator_action_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operator_id` int(10) NOT NULL COMMENT '操作员ID',
  `module` tinyint(1) NOT NULL COMMENT '功能模块\r\n1: 商户管理-商户审核\r\n2: 广告主管理-广告主审核\r\n3: 广告内容管理-广告审核\r\n4: 交易管理-AAB交易\r\n5: 系统管理-挂单人员设置\r\n',
  `action` tinyint(1) NOT NULL COMMENT '操作类型\r\n1-1: 通过了“商户名称”的审核\r\n1-2: 拒绝了“商户名称”的审核\r\n2-1: 通过了“企业名称”的审核\r\n2-2: 拒绝了“企业名称”的审核\r\n3-1: 通过了“企业名称-广告名称”的审核\r\n3-2: 拒绝了“企业名称-广告名称”的审核\r\n4-1: 转账\r\n4-2: 添加挂单\r\n5-1: 添加挂单人\r\n5-2: 删除挂单人\r\n5-3: 修改挂单人',
  `additional_info` varchar(255) DEFAULT NULL COMMENT '附加信息',
  `action_time` bigint(13) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='操作员访问日志';

-- ----------------------------
-- Table structure for order_reference_number
-- ----------------------------
DROP TABLE IF EXISTS `order_reference_number`;
CREATE TABLE `order_reference_number` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `reference_number` char(6) NOT NULL COMMENT '订单参考号',
  `create_time` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_n` (`reference_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='订单参考号';

-- ----------------------------
-- Table structure for param_config
-- ----------------------------
DROP TABLE IF EXISTS `param_config`;
CREATE TABLE `param_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `category` tinyint(1) NOT NULL COMMENT '大类\r\n1: 挖矿\r\n2: 交易',
  `sub_category` tinyint(1) NOT NULL COMMENT '小类\r\n1-1: 阅读\r\n1-2: 实名认证\r\n1-3: 签到\r\n1-4: 关注公众号\r\n1-5: 邀请好友\r\n1-6: 红信\r\n1-7: 答题\r\n2-1: 挂单买卖\r\n2-2: 汇率',
  `key` varchar(32) NOT NULL COMMENT '键\r\n详情见初始化SQL',
  `value` varchar(32) NOT NULL COMMENT '值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_csk` (`category`,`sub_category`,`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='参数配置';

-- ----------------------------
-- Table structure for platform_assert_income_expenditure_record
-- ----------------------------
DROP TABLE IF EXISTS `platform_assert_income_expenditure_record`;
CREATE TABLE `platform_assert_income_expenditure_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `direction` tinyint(1) NOT NULL COMMENT '进出帐方向\r\n1: 进账\r\n2: 出帐',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `user_name` varchar(32) NOT NULL COMMENT '用户名称',
  `action_time` bigint(13) NOT NULL COMMENT '记录时间',
  `amount` decimal(15,2) NOT NULL COMMENT '平台币交易金额',
  `rmb_amount` decimal(15,2) DEFAULT NULL COMMENT '法币交易金额（仅限进账）',
  `action` tinyint(1) NOT NULL COMMENT '交易类型\r\n1: 充值平台币（法币进账）\r\n2: 发币押金（平台币进账）(已去掉)\r\n3: 交易手续费（平台币进账）\r\n4: 广告费（平台币进账）\r\n5: 购买平台币转账（平台币出帐）',
  PRIMARY KEY (`id`),
  KEY `idx_da` (`direction`,`action`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='平台进出帐记录';

-- ----------------------------
-- Table structure for platform_assert_seller
-- ----------------------------
DROP TABLE IF EXISTS `platform_assert_seller`;
CREATE TABLE `platform_assert_seller` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(16) NOT NULL COMMENT '名称',
  `support_alipay` tinyint(1) NOT NULL COMMENT '是否支持支付宝\r\n1: 是\r\n2: 否',
  `support_wechat` tinyint(1) NOT NULL COMMENT '是否支持微信\r\n1: 是\r\n2: 否',
  `support_bank_card` tinyint(1) NOT NULL COMMENT '是否支持银行卡\r\n1: 是\r\n2: 否',
  `alipay_account` varchar(32) DEFAULT NULL COMMENT '支付宝账号',
  `alipay_qr_code_path` varchar(255) DEFAULT NULL COMMENT '支付宝收款码附件路径',
  `wechat_account` varchar(32) DEFAULT NULL COMMENT '微信账号',
  `wechat_qr_code_path` varchar(255) DEFAULT NULL COMMENT '微信收款码附件路径',
  `bank_card_number` varchar(32) DEFAULT NULL COMMENT '银行卡号',
  `total_sold_currency` decimal(15,2) NOT NULL COMMENT '总共售出平台币金额',
  `total_sold_count` int(7) NOT NULL COMMENT '总共售出单数',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) NOT NULL COMMENT '最新更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_n` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='平台币挂单人员';

-- ----------------------------
-- Table structure for platform_assert_selling_order
-- ----------------------------
DROP TABLE IF EXISTS `platform_assert_selling_order`;
CREATE TABLE `platform_assert_selling_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `seller_id` int(10) NOT NULL COMMENT '卖单人ID',
  `available_trade_amount` decimal(15,2) NOT NULL COMMENT '可出售总额',
  `min_amount_limit` decimal(15,2) NOT NULL COMMENT '单笔最小限额',
  `max_amount_limit` decimal(15,2) NOT NULL COMMENT '单笔最大限额',
  `rmb_price` decimal(7,2) NOT NULL COMMENT '法币单价',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) NOT NULL COMMENT '最新更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='平台币卖单';

-- ----------------------------
-- Table structure for platform_assert_statistics
-- ----------------------------
DROP TABLE IF EXISTS `platform_assert_statistics`;
CREATE TABLE `platform_assert_statistics` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `key` varchar(32) NOT NULL COMMENT '键\r\nUPDATE_TIME:           更新时间\r\nTOTAL:                 资产总量\r\nACTIVE:                活跃总量\r\nACTIVE_INCREASE:       活跃自然增长量\r\nACTIVE_INCREASE_MINED: 活跃自然增长被挖量\r\nACTIVE_INCREASE_REST:  活跃自然增长剩余量\r\nACTIVE_MINING:         活跃挖矿量\r\nACTIVE_MINING_MINED:   活跃挖矿被挖量\r\nACTIVE_MINING_REST:    活跃挖矿剩余量\r\nFIXED:                 固定总量\r\nFIXED_SOLD:            固定已售出量\r\nFIXED_REST:            固定剩余量',
  `value` decimal(15,2) NOT NULL COMMENT '值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='平台资产统计';

-- ----------------------------
-- Table structure for platform_assert_trade_record
-- ----------------------------
DROP TABLE IF EXISTS `platform_assert_trade_record`;
CREATE TABLE `platform_assert_trade_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `initiator_id` int(10) NOT NULL COMMENT '购买人ID',
  `initiator_name` varchar(32) NOT NULL COMMENT '购买人名称',
  `trade_type` tinyint(1) NOT NULL COMMENT '交易类型\r\n1: 商户发币押金\r\n2: 买入\r\n3: 转账\r\n4: 分红',
  `trade_time` bigint(13) NOT NULL COMMENT '交易时间',
  `rmb_price` decimal(7,2) DEFAULT NULL COMMENT '法币单价（每一个单位的平台币兑换多少法币）',
  `available_trade_amount` decimal(15,2) DEFAULT NULL COMMENT '可交易额度',
  `trade_amount` decimal(15,2) NOT NULL COMMENT '实际交易额度',
  `min_amount_limit` decimal(15,2) DEFAULT NULL COMMENT '单笔最小限额',
  `max_amount_limit` decimal(15,2) DEFAULT NULL COMMENT '单笔最大限额',
  `wallet_address` varchar(32) NOT NULL COMMENT '平台币钱包地址',
  `balance` decimal(15,2) NOT NULL COMMENT '交易完成后的平台币余额',
  `valid_balance` decimal(15,2) NOT NULL COMMENT '交易完成后的平台币有效余额',
  `partner_id` int(10) NOT NULL COMMENT '交易伙伴ID',
  `partner_name` varchar(32) NOT NULL COMMENT '交易伙伴名称',
  `pay_number` char(6) DEFAULT NULL COMMENT '付款参考号',
  `status` tinyint(1) DEFAULT NULL COMMENT '转账审核状态\r\n1: 后台待转账\r\n2: 已完成\r\n3: 已取消',
  `selling_order_id` int(10) DEFAULT NULL COMMENT 'trade_type=3时，null',
  PRIMARY KEY (`id`),
  KEY `idx_t` (`trade_type`) USING BTREE,
  KEY `idx_i` (`initiator_name`) USING BTREE,
  KEY `idx_time` (`trade_time`) USING BTREE,
  KEY `idx_p` (`pay_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='平台币交易记录';

-- ----------------------------
-- Table structure for platform_service_charge_strategy
-- ----------------------------
DROP TABLE IF EXISTS `platform_service_charge_strategy`;
CREATE TABLE `platform_service_charge_strategy` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(10) NOT NULL COMMENT '策略名称',
  `trade_charge_rate` decimal(5,2) NOT NULL COMMENT '交易费率（不含%）',
  `issuance_deposit` decimal(15,2) NOT NULL COMMENT '发币押金',
  `is_default` tinyint(1) NOT NULL COMMENT '是否是默认策略\r\n1: 是\r\n2: 否',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) NOT NULL COMMENT '最新更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_n` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='平台服务费策略';

-- ----------------------------
-- Table structure for red_packet_dividing
-- ----------------------------
DROP TABLE IF EXISTS `red_packet_dividing`;
CREATE TABLE `red_packet_dividing` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `red_packet_id` int(10) NOT NULL COMMENT '红信ID',
  `dividing_amount` decimal(15,2) NOT NULL COMMENT '单份金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 COMMENT='红信子项';

-- ----------------------------
-- Table structure for red_packet_dividing_grabbing
-- ----------------------------
DROP TABLE IF EXISTS `red_packet_dividing_grabbing`;
CREATE TABLE `red_packet_dividing_grabbing` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `red_packet_id` int(10) NOT NULL COMMENT '红信ID',
  `dividing_id` int(10) NOT NULL COMMENT '红信子项ID',
  `grabber_id` int(10) NOT NULL COMMENT '入手用户ID',
  `grabbing_time` bigint(13) NOT NULL COMMENT '入手时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_dg` (`dividing_id`,`grabber_id`) USING BTREE,
  UNIQUE KEY `uni_rd` (`red_packet_id`,`dividing_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='红信子项获取';

-- ----------------------------
-- Table structure for red_packet_image
-- ----------------------------
DROP TABLE IF EXISTS `red_packet_image`;
CREATE TABLE `red_packet_image` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `red_packet_id` int(10) NOT NULL COMMENT '红信ID',
  `image_url` varchar(128) NOT NULL COMMENT '图片URL',
  `order_number` tinyint(2) NOT NULL COMMENT '序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='红信图片';

-- ----------------------------
-- Table structure for red_packet_interaction
-- ----------------------------
DROP TABLE IF EXISTS `red_packet_interaction`;
CREATE TABLE `red_packet_interaction` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `red_packet_id` int(10) NOT NULL COMMENT '红信ID',
  `parent_id` int(10) DEFAULT NULL COMMENT '父级互动ID',
  `action` tinyint(1) NOT NULL COMMENT '互动类型\r\n1: 评论\r\n2: 点赞\r\n3: 点击链接',
  `action_description` varchar(128) DEFAULT NULL COMMENT '互动描述信息（评论内容）',
  `action_user_id` int(10) NOT NULL COMMENT '互动用户ID',
  `action_time` bigint(13) NOT NULL COMMENT '互动时间',
  PRIMARY KEY (`id`),
  KEY `idx_a` (`action`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT='红信互动';

-- ----------------------------
-- Table structure for red_packet_issuance
-- ----------------------------
DROP TABLE IF EXISTS `red_packet_issuance`;
CREATE TABLE `red_packet_issuance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `issuer_id` int(10) NOT NULL COMMENT '发布用户ID',
  `description` varchar(128) DEFAULT NULL COMMENT '描述信息',
  `dividing_number` int(5) NOT NULL COMMENT '红包个数',
  `dividing_amount` decimal(15,2) DEFAULT NULL COMMENT '发布金额',
  `lng` decimal(10,6) NOT NULL COMMENT '发布位置经度',
  `lat` decimal(10,6) NOT NULL COMMENT '发布位置纬度',
  `location` varchar(64) DEFAULT NULL COMMENT '发布位置名称',
  `radius` int(4) NOT NULL COMMENT '发布范围（单位：公里）',
  `grabbing_limit` tinyint(1) DEFAULT NULL COMMENT '领取限制\r\nnull: 不限\r\n1:    男\r\n2:    女\r\n',
  `link_title` varchar(10) DEFAULT NULL COMMENT '链接标题',
  `link_url` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `issuance_time` bigint(13) NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='红信发布';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别\r\n1: 男\r\n2: 女',
  `id_number` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `mobile_number` bigint(11) NOT NULL COMMENT '电话号码',
  `payment_password` char(6) DEFAULT NULL COMMENT '支付密码',
  `source` tinyint(1) NOT NULL COMMENT '用户来源\r\n1: 常规注册用户\r\n2: 其他用户',
  `reg_time` bigint(13) NOT NULL COMMENT '注册时间',
  `inviter_id` int(10) DEFAULT NULL COMMENT '推荐人ID',
  `inviter_code` char(8) NOT NULL COMMENT '用户自己的邀请码',
  `registration_type` tinyint(1) NOT NULL COMMENT '注册来源0-本地APP 1-小程序 2-餐饮',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_i` (`inviter_code`) USING BTREE,
  UNIQUE KEY `uni_m` (`mobile_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='用户基础';

-- ----------------------------
-- Table structure for user_address_book
-- ----------------------------
DROP TABLE IF EXISTS `user_address_book`;
CREATE TABLE `user_address_book` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `wallet_address` varchar(32) NOT NULL COMMENT '钱包地址',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `nike_name` varchar(10) NOT NULL COMMENT '姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户钱包地址簿';

-- ----------------------------
-- Table structure for user_assert
-- ----------------------------
DROP TABLE IF EXISTS `user_assert`;
CREATE TABLE `user_assert` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `balance` decimal(15,2) NOT NULL COMMENT '余额（包含冻结部分）',
  `history_max_balance` decimal(15,2) NOT NULL COMMENT '历史最大余额（用于划分用户等级）',
  `wallet_address` varchar(32) DEFAULT NULL COMMENT '钱包地址',
  `synchronized_time` bigint(13) DEFAULT NULL COMMENT '同步时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_uc` (`user_id`,`currency_symbol`) USING BTREE,
  UNIQUE KEY `uni_w` (`wallet_address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='用户资产';

-- ----------------------------
-- Table structure for user_assert_freeze
-- ----------------------------
DROP TABLE IF EXISTS `user_assert_freeze`;
CREATE TABLE `user_assert_freeze` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `amount` decimal(15,2) NOT NULL COMMENT '额度',
  `reason` tinyint(1) NOT NULL COMMENT '原因\r\n1: 发币押金\r\n2: 挂单购买\r\n3: 挂单出售',
  `action_time` bigint(13) NOT NULL COMMENT '冻结时间',
  PRIMARY KEY (`id`),
  KEY `idx_uc` (`user_id`,`currency_symbol`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='用户资产冻结';

-- ----------------------------
-- Table structure for user_behaviour_statistics
-- ----------------------------
DROP TABLE IF EXISTS `user_behaviour_statistics`;
CREATE TABLE `user_behaviour_statistics` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `key` varchar(32) NOT NULL COMMENT '键\r\nTRADE:                    交易次数\r\nSIGN_IN:                  签到次数\r\nANSWER:                   答题次数\r\nATTENTION_PUBLIC_NUMBER:  关注公众号次数\r\nREAD_ARTICLE:             阅读文章次数\r\nSEND_RED_PACKET:          发红包次数\r\nGRAB_RED_PACKET:          抢到红包次数\r\nPLAY_GAME:                玩游戏次数\r\nMINING_EVENT:             挖矿活动参加次数\r\nINVITE:                   邀请好友次数\r\nPOST_ARTICLE:             发文章次数\r\nCLICK_AD:                 有效点击广告次数\r\nSUBMIT_AD_FORM:           广告表单提交次数',
  `value` int(8) NOT NULL COMMENT '值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_uk` (`user_id`,`key`) USING BTREE,
  KEY `idx_uk` (`user_id`,`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=313 DEFAULT CHARSET=utf8 COMMENT='用户行为统计';

-- ----------------------------
-- Table structure for user_birthday_login_fail_record
-- ----------------------------
DROP TABLE IF EXISTS `user_birthday_login_fail_record`;
CREATE TABLE `user_birthday_login_fail_record` (
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `record_time` bigint(13) NOT NULL COMMENT '记录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户生日登录失败记录';

-- ----------------------------
-- Table structure for user_challenge_questions_record
-- ----------------------------
DROP TABLE IF EXISTS `user_challenge_questions_record`;
CREATE TABLE `user_challenge_questions_record` (
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `frequency` tinyint(1) NOT NULL COMMENT '答题挑战次数（同一个用户当天累计答题挑战次数）',
  `answer_number` tinyint(2) DEFAULT '0' COMMENT '作答次数（每次挑战次数更新该值清零）',
  `power_point_bonus` int(3) DEFAULT '0' COMMENT '本次挑战累计奖励值',
  `create_time` bigint(13) NOT NULL COMMENT '答题时间',
  `answer_times` tinyint(2) DEFAULT NULL COMMENT '答对次数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户挑战答题次数纪录';

-- ----------------------------
-- Table structure for user_income_statistics
-- ----------------------------
DROP TABLE IF EXISTS `user_income_statistics`;
CREATE TABLE `user_income_statistics` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `currency_symbol` varchar(3) NOT NULL COMMENT '货币符号',
  `key` varchar(32) NOT NULL COMMENT '键\r\nRECHARGE:     充值收入总计\r\nSELL:         卖出商家币收入总计\r\nTRANSFER:     转账收入总计\r\nMINING：      挖矿收入总计',
  `value` decimal(15,2) NOT NULL COMMENT '值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_uck` (`user_id`,`currency_symbol`,`key`) USING BTREE,
  KEY `idx_uck` (`user_id`,`currency_symbol`,`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8 COMMENT='用户收入统计';

-- ----------------------------
-- Table structure for user_level
-- ----------------------------
DROP TABLE IF EXISTS `user_level`;
CREATE TABLE `user_level` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) NOT NULL COMMENT '等级名称',
  `icon_path` varchar(255) NOT NULL COMMENT '勋章图标附件路径',
  `match_condition` decimal(15,3) NOT NULL COMMENT '需持有平台币数额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_n` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户等级';

-- ----------------------------
-- Table structure for user_mining_record
-- ----------------------------
DROP TABLE IF EXISTS `user_mining_record`;
CREATE TABLE `user_mining_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `inviter_id` int(10) DEFAULT NULL COMMENT '邀请人ID',
  `action` tinyint(4) NOT NULL COMMENT '挖矿类型\r\n1: 阅读\r\n2: 实名认证\r\n3: 签到\r\n4: 关注公众号\r\n5: 邀请好友（包含好友挖矿提成）\r\n6: 红信\r\n7: 答题\r\n8: 自然增长\r\n9: 邀请码注册',
  `action_time` bigint(13) NOT NULL COMMENT '挖矿时间',
  `bonus` decimal(15,2) NOT NULL COMMENT '奖励数额',
  `bonus_type` tinyint(1) NOT NULL COMMENT '奖励类型\r\n1: 平台币\r\n2: 元力',
  PRIMARY KEY (`id`),
  KEY `idx_a` (`action`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8 COMMENT='用户挖矿记录';

-- ----------------------------
-- Table structure for user_natural_growth_record
-- ----------------------------
DROP TABLE IF EXISTS `user_natural_growth_record`;
CREATE TABLE `user_natural_growth_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID NATURAL GROWTH',
  `growth_amount` decimal(15,2) NOT NULL COMMENT '增长金额',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8 COMMENT='用户自然增长记录';

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '卖单/买单人ID',
  `original_limit` decimal(15,2) NOT NULL,
  `available_trade_amount` decimal(15,2) NOT NULL COMMENT '出售额度',
  `currency_symbol` varchar(3) NOT NULL COMMENT '出售货币符号',
  `aab_price` decimal(15,2) NOT NULL COMMENT 'AAB单价',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `type` tinyint(1) NOT NULL COMMENT '发布类型\r\n1:买单\r\n2:卖单',
  `status` tinyint(1) NOT NULL COMMENT '状态：\r\n1：上架\r\n2：下架',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `update_time` bigint(13) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户挂单';

-- ----------------------------
-- Table structure for user_payment_error_record
-- ----------------------------
DROP TABLE IF EXISTS `user_payment_error_record`;
CREATE TABLE `user_payment_error_record` (
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `create_time` bigint(13) NOT NULL COMMENT '记录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户交易支付错误记录';

-- ----------------------------
-- Table structure for user_property
-- ----------------------------
DROP TABLE IF EXISTS `user_property`;
CREATE TABLE `user_property` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `power_point` int(8) NOT NULL COMMENT '元力值',
  `increase_strategy_id` int(10) NOT NULL COMMENT '自然增长策略ID',
  `real_name_crt_time` decimal(13,0) DEFAULT NULL COMMENT '实名认证时间',
  `status` tinyint(1) NOT NULL COMMENT '状态\r\n1: 启用\r\n2: 停用',
  `status_description` varchar(50) DEFAULT NULL COMMENT '状态描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_u` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户属性';

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `is_merchant` tinyint(1) NOT NULL COMMENT '是否是商户\r\n1: 是\r\n2: 否',
  `is_advertiser` tinyint(1) NOT NULL COMMENT '是否是广告主\r\n1: 是\r\n2: 否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Table structure for user_sms_code
-- ----------------------------
DROP TABLE IF EXISTS `user_sms_code`;
CREATE TABLE `user_sms_code` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mobile_number` bigint(11) NOT NULL COMMENT '手机号码',
  `type` tinyint(1) NOT NULL COMMENT '验证码类型\r\n1：登录\r\n2：修改密码 \r\n3：商户实名认证\r\n4：小程序同步',
  `code` char(4) NOT NULL COMMENT '验证码',
  `send_time` bigint(13) NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COMMENT='用户短信验证码';

-- ----------------------------
-- Table structure for we_chat_verification_code
-- ----------------------------
DROP TABLE IF EXISTS `we_chat_verification_code`;
CREATE TABLE `we_chat_verification_code` (
  `code` char(6) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信公众号验证码';
