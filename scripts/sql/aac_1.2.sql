-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `level` tinyint(1) NOT NULL COMMENT '行政级别\r\n1: 直辖市\r\n2: 省或自治区\r\n3: 特别行政区',
  `order_number` tinyint(2) NOT NULL COMMENT '在所属行政级别中的排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='省级、直辖市级、特别行政区级单位字典';

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `lng` decimal(10,6) NOT NULL COMMENT '经度',
  `lat` decimal(10,6) NOT NULL COMMENT '纬度',
  `province_id` int(11) NOT NULL,
  `order_number` tinyint(2) NOT NULL COMMENT '在所属级别中的排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='市级单位字典';


-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(10) NOT NULL COMMENT '名称',
  `create_time` bigint(13) NOT NULL COMMENT '添加时间',
  `readonly` tinyint(1) NOT NULL COMMENT '是否只读分类\r\n1: 是\r\n2: 否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='文章类型字典';


-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `category_id` int(10) NOT NULL COMMENT '文章分类ID',
  `title` varchar(40) NOT NULL COMMENT '标题',
  `author_id` int(10) NOT NULL COMMENT '发布者ID',
  `author_type` tinyint(1) NOT NULL COMMENT '发布者分类\r\n1: APP用户\r\n2: 运营人员',
  `create_time` bigint(13) NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`),
  KEY `idx_c` (`category_id`) USING BTREE,
  KEY `idx_t` (`title`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='文章';


-- ----------------------------
-- Table structure for article_element
-- ----------------------------
DROP TABLE IF EXISTS `article_element`;
CREATE TABLE `article_element` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `article_id` int(10) NOT NULL COMMENT '文章ID',
  `element_type` tinyint(1) NOT NULL COMMENT '元素类型\r\n1: 文字\r\n2: 视频\r\n3: 图片',
  `element_content` varchar(500) NOT NULL COMMENT '元素内容（文字或URL）',
  `order_number` tinyint(2) NOT NULL COMMENT '排序号',
  PRIMARY KEY (`id`),
  KEY `idx_a` (`article_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='文章元素';


-- ----------------------------
-- Table structure for article_interaction
-- ----------------------------
DROP TABLE IF EXISTS `article_interaction`;
CREATE TABLE `article_interaction` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `article_id` int(10) NOT NULL COMMENT '文章ID',
  `parent_id` int(10) DEFAULT NULL COMMENT '父级互动ID',
  `action` tinyint(1) NOT NULL COMMENT '互动类型\r\n1: 评论\r\n2: 点赞\r\n3: 阅读',
  `action_description` varchar(128) DEFAULT NULL COMMENT '互动描述信息（评论内容）',
  `action_user_id` int(10) NOT NULL COMMENT '互动用户ID',
  `action_time` bigint(13) NOT NULL COMMENT '互动时间',
  PRIMARY KEY (`id`),
  KEY `idx_a` (`action`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='文章互动';


-- ----------------------------
-- Table structure for article_personal
-- ----------------------------
DROP TABLE IF EXISTS `article_personal`;
CREATE TABLE `article_personal` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `article_id` int(10) NOT NULL COMMENT '文章ID',
  `policy` tinyint(1) NOT NULL COMMENT '个性化策略\r\n1: 关注\r\n2: 不想再看',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章个性化记录';


-- ----------------------------
-- Table structure for exchange
-- ----------------------------
DROP TABLE IF EXISTS `exchange`;
CREATE TABLE `exchange` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `category` tinyint(1) NOT NULL COMMENT '大类\r\n1: 话费\r\n2: 油卡',
  `sub_category` tinyint(1) NOT NULL COMMENT '小类\r\n1-1: 移动电话\r\n2-1: 中石油\r\n2-2: 中石化',
  `customized` tinyint(1) NOT NULL COMMENT '是否支持自定义金额\r\n1: 是\r\n2: 否',
  `limit_in_month` tinyint(2) NOT NULL COMMENT '每月可兑换次数',
  `activated` tinyint(1) NOT NULL COMMENT '是否已激活\r\n1: 是\r\n2: 否',
  `order_number` tinyint(2) NOT NULL COMMENT '排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑换服务';


-- ----------------------------
-- Table structure for exchange_item
-- ----------------------------
DROP TABLE IF EXISTS `exchange_item`;
CREATE TABLE `exchange_item` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `exchange_id` int(10) NOT NULL COMMENT '兑换服务ID',
  `rmb_amount` int(5) NOT NULL COMMENT '兑换法币金额',
  `platform_amount` decimal(8,2) NOT NULL COMMENT '平台币付款金额',
  `order_number` tinyint(2) NOT NULL COMMENT '排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑换服务项';


-- ----------------------------
-- Table structure for exchange_record
-- ----------------------------
DROP TABLE IF EXISTS `exchange_record`;
CREATE TABLE `exchange_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `exchange_id` int(10) NOT NULL COMMENT '兑换服务ID',
  `charging_number` varchar(20) NOT NULL COMMENT '充值号码',
  `rmb_amount` int(5) NOT NULL COMMENT '兑换法币金额',
  `platform_amount` decimal(8,2) NOT NULL COMMENT '平台币付款金额',
  `record_time` bigint(13) NOT NULL COMMENT '兑换时间',
  `status` tinyint(1) NOT NULL COMMENT '状态\r\n1: 待充值\r\n2: 充值完成',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='兑换服务记录';


-- ----------------------------
-- Table structure for mining_activity
-- ----------------------------
DROP TABLE IF EXISTS `mining_activity`;
CREATE TABLE `mining_activity` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `begin_time` bigint(13) NOT NULL COMMENT '开始时间',
  `end_time` bigint(13) NOT NULL COMMENT '结束时间',
  `status` tinyint(1) NOT NULL COMMENT '状态\r\n1: 正常\r\n2: 已终止',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='挖矿活动';


-- ----------------------------
-- Table structure for mining_activity_item
-- ----------------------------
DROP TABLE IF EXISTS `mining_activity_item`;
CREATE TABLE `mining_activity_item` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activity_id` int(10) NOT NULL COMMENT '挖矿活动ID',
  `user_level_id` int(10) DEFAULT NULL COMMENT '限制进入等级ID（NULL为不限制等级）',
  `total_bonus` decimal(8,2) NOT NULL COMMENT '场次总奖金',
  `lucky_rate` tinyint(3) NOT NULL COMMENT '暴击率（百分号之前的值）',
  `lucky_times` tinyint(2) NOT NULL COMMENT '暴击倍数',
  `hit_ad_number` tinyint(2) NOT NULL COMMENT '点击第几次出现广告',
  `order_number` tinyint(2) NOT NULL COMMENT '排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='挖矿活动项';


-- ----------------------------
-- Table structure for mining_activity_statistics
-- ----------------------------
DROP TABLE IF EXISTS `mining_activity_statistics`;
CREATE TABLE `mining_activity_statistics` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activity_item_id` int(10) NOT NULL COMMENT '挖矿活动项ID',
  `user_id` int(10) NOT NULL COMMENT '参与者用户ID',
  `gained` decimal(8,2) NOT NULL COMMENT '获利平台币数',
  `ad_clicked` tinyint(2) NOT NULL COMMENT '广告点击数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='挖矿活动统计';

-- ----------------------------
-- Table structure for mining_activity_participant
-- ----------------------------
DROP TABLE IF EXISTS `mining_activity_participant`;
CREATE TABLE `mining_activity_participant` (
  `activity_item_id` int(10) NOT NULL COMMENT '挖矿活动项ID',
  `user_id` int(10) NOT NULL COMMENT '参与者用户ID',
  `queuing_code` int(10) NOT NULL COMMENT '参与者排队码'
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='挖矿活动参与者';


-- 下列三表仅更新注释或数据长度，表结构无变化
ALTER TABLE `platform_assert_income_expenditure_record`
MODIFY COLUMN `action`  tinyint(2) NOT NULL COMMENT '交易类型\r\n1: 充值平台币（法币进账）\r\n2: 发币押金（平台币进账）(已去掉)\r\n3: 交易手续费（平台币进账）\r\n4: 广告费（平台币进账）\r\n5: 购买平台币转账（平台币出帐）\r\n6: 充值话费（平台币进账）\r\n7: 充值中石油油卡（平台币进账）\r\n8: 充值中石化油卡（平台币进账）\r\n9: 充值话费（法币出账）\r\n10: 充值中石油油卡（法币出账）\r\n11: 充值中石化油卡（法币出账）' AFTER `rmb_amount`;

ALTER TABLE `user_mining_record`
MODIFY COLUMN `action`  tinyint(4) NOT NULL COMMENT '挖矿类型\r\n1: 阅读\r\n2: 实名认证\r\n3: 签到\r\n4: 关注公众号\r\n5: 邀请好友（包含好友挖矿提成）\r\n6: 红信\r\n7: 答题\r\n8: 自然增长\r\n9: 邀请码注册\r\n10: 挖\r\n11:发表文章' AFTER `inviter_id`;

ALTER TABLE `platform_assert_trade_record`
MODIFY COLUMN `trade_type`  tinyint(1) NOT NULL COMMENT '交易类型\r\n1: 商户发币押金\r\n2: 买入\r\n3: 转账\r\n4: 分红\r\n5: 兑换话费\r\n6: 兑换油卡' AFTER `initiator_name`;

-- 修改关注公众号简介文本 brief_text
UPDATE brief_text SET content = '<p><span style="color: rgb(165, 165, 165);">1、在微信公众号众搜索“BT实体经济”并关注</span></p><p><br/></p><p><span style="color: rgb(165, 165, 165);">2、在赚链公众号内发送“领取元力”获取验证码</span></p><p><br/></p><p><span style="color: rgb(165, 165, 165);">3、在下方输入验证码，验证成功后即可领取元力</span></p><hr/><p><span style="color: rgb(165, 165, 165);">说明：每个帐号仅有一次领取机会</span><br/></p>' 
WHERE `key` = 'FOCUS_ON_WECHAT';


-- 新增初始化配置数据
DELETE FROM param_config WHERE category = 1 AND sub_category = 1;
INSERT INTO param_config (category, sub_category, `key`, value)
VALUES
	-- 阅读时长（单位：秒）
	(1, 1, 'READING_TIME', '10'),
	-- 每天最大阅读奖励次数
	(1, 1, 'MAX_READING_PER_DAY', '8'),
	-- 每次阅读奖励金额
	(1, 1, 'READING_CURRENCY', '1'),
	-- 文章最少字数
	(1, 1, 'MIN_ARTICLE_LENGTH', '200'),
	-- 每天最大发帖奖励次数
	(1, 1, 'MAX_PUBLISHING_PER_DAY', '1'),
	-- 每次发帖奖励金额
	(1, 1, 'PUBLISHING_CURRENCY', '10');

-- 新增初始化文章分类数据
DELETE FROM article_category WHERE readonly = 1;
INSERT INTO article_category (name, create_time, readonly)
VALUES
	('科技', 1545372155291, 1),
  ('养生', 1545372155291, 1),
	('娱乐', 1545372155291, 1);


-- 新增初始化兑换数据
DELETE FROM exchange;
INSERT INTO exchange (name, category, sub_category, customized, limit_in_month, activated, order_number)
VALUES
	( '兑换话费', 1, 1, 0, 0, 2, 1),
	( '兑换油卡（中石油）', 2, 1, 0, 0, 2, 2),
	( '兑换油卡（中石化）', 2, 2, 0, 0, 2, 3);

--新增资讯文章奖励规则
INSERT INTO brief_text (`key`, `name`, content)
VALUES('ARTICLE_AWARD','资讯文章奖励规则','<p style="text-align: center;">奖励说明</p><p><span style="text-decoration: none;"></span></p><hr/><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、发帖字数须达到200</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、发帖成功可得10AAB奖励</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="text-decoration: none;">3、个人所发帖在关注中可查看</span><br/></p>');
