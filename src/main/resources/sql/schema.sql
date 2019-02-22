/**************************订单表**************************/
DROP TABLE IF EXISTS t_canary_loan_order;
CREATE TABLE t_canary_loan_order (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(64) NOT NULL COMMENT '订单号',
  `user_code` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `order_type` varchar(64) NOT NULL COMMENT '订单类型',
  `order_state` varchar(64) NOT NULL COMMENT '订单状态',
  `repayment_day` int COMMENT '还款日',
  `instalment` int  NULL COMMENT '分期数',
  `instalment_unit` varchar(64) NOT NULL COMMENT '分期单位',
  `instalment_rate` DECIMAL(18,8) NOT NULL COMMENT '分期利率',
  `penalty_rate` DECIMAL(18,8) NOT NULL COMMENT '分期利率',
  `lend_mode` varchar(64) NOT NULL COMMENT '放款模式',
  `apply_currency` varchar(64) NOT NULL COMMENT '借款币种',
  `equivalent` varchar(64) NOT NULL COMMENT '一般等价物, 锚定币种',
  `equivalent_rate` DECIMAL(18,8) NOT NULL COMMENT '一般等价物的兑换率, 1个applyCurrency = x个equivalent',
  `apply_amount` DECIMAL(18,8) NOT NULL COMMENT '申请借款金额, 对应借款币种',
  `lent_amount` DECIMAL(18,8) NOT NULL COMMENT '放款金额, 对应借款币种',
  `equivalent_amount` DECIMAL(18,8) NOT NULL COMMENT '锚定金额, 对应锚定币种',
  `fee` JSON COMMENT '直接分配到某个分期的费用',
  `lent_time` bigint(20) NOT NULL COMMENT '放款时间',
  `end_time` bigint(20) NOT NULL COMMENT '结束时间',
  `time_zone` int  NULL COMMENT '时区',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_order_id` (`order_id`) USING BTREE,
  INDEX `index_user_code` (`user_code`) USING BTREE,
  INDEX `index_update_time` (`update_time`) USING BTREE,
  INDEX `index_order_type` (`order_type`) USING BTREE,
  INDEX `index_order_state` (`order_state`) USING BTREE,
  INDEX `index_lent_time` (`lent_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';


/***************************分期表**************************/
DROP TABLE IF EXISTS t_canary_instalment;
CREATE TABLE t_canary_instalment (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `instalment_id` varchar(64) NOT NULL COMMENT '订单号',
  `order_id` varchar(64) NOT NULL COMMENT '订单号',
  `user_code` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `order_type` varchar(64) NOT NULL COMMENT '订单类型',
  `instalment_state` varchar(64) NOT NULL COMMENT '订单状态',
  `instalment` int  NULL COMMENT '分期数',
  `equivalent` varchar(64) NOT NULL COMMENT '一般等价物, 锚定币种',
  `original_principal` DECIMAL(18,8) NOT NULL COMMENT '原始本金',
  `original_interest` DECIMAL(18,8) NOT NULL COMMENT '原始利息',
  `original_fee` JSON NOT NULL COMMENT '原始直接分配到某个分期的费用, JSON',
  `paid_principal` DECIMAL(18,8) NOT NULL COMMENT '已还本金',
  `paid_interest` DECIMAL(18,8) NOT NULL COMMENT '已还利息',
  `Paid_penalty` DECIMAL(18,8) NOT NULL COMMENT '已还罚息',
  `last_paid_principal_date` bigint(20) NOT NULL COMMENT '上次还本金的时间',
  `paid_fee` JSON COMMENT '已还直接分配到某个分期的费用, JSON',
  `should_pay_time` bigint(20) NOT NULL COMMENT '应还清时间',
  `clear_time` bigint(20) NOT NULL COMMENT '超过这个时间, 此分期强制结束, 如果为 -1 , 表示会一直记罚息',
  `end_time` bigint(20) NOT NULL COMMENT '实际结束时间',
  `time_zone` int  NULL COMMENT '时区',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_instalment_id` (`instalment_id`) USING BTREE,
  INDEX `index_order_id` (`order_id`) USING BTREE,
  INDEX `index_user_code` (`user_code`) USING BTREE,
  INDEX `index_update_time` (`update_time`) USING BTREE,
  INDEX `index_order_type` (`order_type`) USING BTREE,
  INDEX `index_instalment_state` (`instalment_state`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分期表';



/***************************还款订单表*************************/
DROP TABLE IF EXISTS t_canary_pay_order;
CREATE TABLE t_canary_pay_order (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pay_order_id` varchar(64) NOT NULL COMMENT '还款订单号',
  `pay_batch_id` varchar(64) NOT NULL COMMENT '还款订单批次号, 允许一次性用多个货币还款',
  `user_code` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `pay_order_type` varchar(64) NOT NULL COMMENT '订单类型',
  `pay_order_state` varchar(64) NOT NULL COMMENT '订单状态',
  `apply_currency` varchar(64) NOT NULL COMMENT '借款币种',
  `equivalent` varchar(64) NOT NULL COMMENT '一般等价物, 锚定币种',
  `equivalent_rate` DECIMAL(18,8) NOT NULL COMMENT '一般等价物的兑换率, 1个applyCurrency = x个equivalent',
  `apply_amount` DECIMAL(18,8) NOT NULL COMMENT '申请借款金额, 对应借款币种',
  `pay_amount` DECIMAL(18,8) DEFAULT 0 NOT NULL COMMENT '实际扣款金额， 对应借款币种',
  `equivalent_amount` DECIMAL(18,8) DEFAULT 0 NOT NULL COMMENT '锚定金额, 对应锚定币种',
  `entry_amount` DECIMAL(18,8) DEFAULT 0 NOT NULL COMMENT '实际入账金额， 对应锚定币种',
  `pay_time` bigint(20) DEFAULT -1 NOT NULL COMMENT '支付时间',
  `end_time` bigint(20) DEFAULT -1 NOT NULL COMMENT '入账结束时间',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_pay_order_id` (`pay_order_id`) USING BTREE,
  INDEX `index_user_code` (`user_code`) USING BTREE,
  INDEX `index_update_time` (`update_time`) USING BTREE,
  INDEX `index_pay_order_type` (`pay_order_type`) USING BTREE,
  INDEX `index_pay_order_state` (`pay_order_state`) USING BTREE,
  INDEX `index_pay_time` (`pay_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='还款订单表';


/***************************还款明细表*************************/
DROP TABLE IF EXISTS t_canary_pay_order_detail;
CREATE TABLE t_canary_pay_order_detail (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `source` varchar(64) NOT NULL COMMENT '来源类型',
  `source_id` varchar(64) NOT NULL COMMENT '来源id',
  `destination` varchar(64) NOT NULL COMMENT '目标',
  `destination_id` varchar(64) NOT NULL COMMENT '目标id',
  `user_code` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `instalment` INT COMMENT '还款期数, 还款订单时有值',
  `repayment_date` bigint(20) COMMENT '应还日期, 还款订单时有值',
  `equivalent` varchar(64) NOT NULL COMMENT '一般等价物, 锚定币种',
  `element` varchar(64) NOT NULL COMMENT '元素, 本金等',
  `should_pay` DECIMAL(18,8) NOT NULL COMMENT '应付金额',
  `paid` DECIMAL(18,8) NOT NULL COMMENT '付款金额',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  INDEX `index_source_id` (`source_id`) USING BTREE,
  INDEX `index_user_code` (`user_code`) USING BTREE,
  INDEX `index_update_time` (`update_time`) USING BTREE,
  INDEX `index_destination_id` (`destination_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='还款明细表';


/***************************优惠表, 优惠券 *************************/
DROP TABLE IF EXISTS t_canary_coupon;
CREATE TABLE t_canary_coupon (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `coupon_batch_id` varchar(64) NOT NULL COMMENT '批量优惠券编号, 有多张优惠券组成',
  `coupon_id` varchar(64) NOT NULL COMMENT '优惠券号, 唯一编号',
  `coupon_type` varchar(64) NOT NULL COMMENT '优惠券类型',
  `coupon_state` varchar(64) NOT NULL COMMENT '优惠券状态',
  `user_code` varchar(64) COMMENT '绑定用户唯一标识',
  `bound_order_id` varchar(64) COMMENT '绑定订单号, 绑定订单后有值',
  `instalment` INT COMMENT '辅助字段, 用于计算schema, 决定优惠某一期, 空表示都有效',
  `element` varchar(64) COMMENT '辅助字段, 用于计算schema, 决定优惠某一元素, 空表示都有效',
  `condition` JSON COMMENT '使用特别限制',
  `equivalent` varchar(64) NOT NULL COMMENT '一般等价物, 锚定币种',
  `default_amount` DECIMAL(18,8) NOT NULL COMMENT '优惠券默认值, 根据类型可能时百分比或固定量, 如果是固定量, 则应与apply_amount相等 ',
  `apply_amount` DECIMAL(18,8) DEFAULT 0 COMMENT '可优惠金额, 绑定订单后有值',
  `entry_amount` DECIMAL(18,8) DEFAULT 0  COMMENT '已入账金额, 绑定订单后有值',
  `effective_date` BIGINT DEFAULT -1 COMMENT '生效起始日期',
  `expire_date` BIGINT DEFAULT -1 COMMENT '失效日期',
  `entered_frequency` INT DEFAULT 0 COMMENT '已入账的次数',
  `apply_time` bigint(20) DEFAULT -1 COMMENT '使用时间',
  `end_time` bigint(20) DEFAULT -1 COMMENT '入账结束时间',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_coupon_id` (`coupon_id`) USING BTREE,
  INDEX `index_user_code` (`user_code`) USING BTREE,
  INDEX `index_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠表';


/***************************策略表 *************************/
DROP TABLE IF EXISTS t_canary_strategy;
CREATE TABLE t_canary_strategy (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `strategy_id` varchar(64) NOT NULL COMMENT '策略号',
  `strategy_type` varchar(64) NOT NULL COMMENT '策略类型',
  `subject` varchar(64) NOT NULL COMMENT '应用主体',
  `instalment` INT NOT NULL COMMENT '辅助字段, 用于计算schema, 决定优惠某一期, 不可为空',
  `element` varchar(64) NOT NULL COMMENT '辅助字段, 用于计算schema, 决定优惠某一元素, 不可为空',
  `condition` JSON COMMENT '使用特别限制',
  `equivalent` varchar(64) COMMENT '一般等价物, 百分比时为空',
  `default_amount` DECIMAL(18,8) NOT NULL COMMENT '策略的默认值, 根据类型可能时百分比或固定量',
  `effective_date` BIGINT DEFAULT -1 COMMENT '生效起始日期, 包括头',
  `expire_date` BIGINT DEFAULT -1 COMMENT '失效日期, 不包括尾',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
PRIMARY KEY (`id`),
UNIQUE INDEX `index_strategy_id` (`strategy_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='策略表';


/***************************条件设置表 *************************/
DROP TABLE IF EXISTS t_canary_condition_set;
CREATE TABLE t_canary_condition_set (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `subject` varchar(64) NOT NULL COMMENT '限制的主体',
  `subject_type` varchar(64) NOT NULL COMMENT '主体的类型',
  `condition` varchar(64) NOT NULL COMMENT '条件',
  `operator` varchar(64) NOT NULL COMMENT '操作符',
  `target` varchar(1024) NOT NULL COMMENT '目标值',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_subject_type_condition` (`subject`, `subject_type`, `condition`) USING BTREE,
  INDEX `index_subject` (`subject`) USING BTREE,
  INDEX `index_subject_type` (`subject_type`) USING BTREE,
  INDEX `index_condition` (`condition`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='条件设置表';



/***************************用户表**************************/
DROP TABLE IF EXISTS t_canary_user;
CREATE TABLE t_canary_user (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `nick_name` varchar(64) NOT NULL COMMENT '名字',
  `level` varchar(64) NOT NULL COMMENT '等级',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_user_code` (`user_code`) USING BTREE,
  INDEX `index_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

INSERT INTO t_canary_user (id, user_code, nick_name, level, remark, create_time, update_time, is_deleted)
  VALUES (1, '1', 'xzhang', '1', '', 21331313, 1231231231, 0);

/***************************用户级别表*************************/
DROP TABLE IF EXISTS t_canary_user_level_setting;
CREATE TABLE t_canary_user_level_setting (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `level` varchar(64) NOT NULL COMMENT '等级',
  `daily_interest_rate` DECIMAL(18,8) NOT NULL COMMENT '利率',
  `daily_penalty_rate` DECIMAL(18,8) NOT NULL COMMENT '罚息率',
  `max_loanable_amount` DECIMAL(18,8) NOT NULL COMMENT '累计最大借款额',
  `min_loanable_amount` DECIMAL(18,8) NOT NULL COMMENT '每次最小借款额',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_level` (`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户级别表';

INSERT INTO t_canary_user_level_setting (id,  level, daily_interest_rate, daily_penalty_rate, max_loanable_amount, min_loanable_amount, remark, create_time, update_time, is_deleted)
  VALUES (1, '1', '0.0005', '0.001', 100000, 10, '', 21331313, 1231231231, 0);
