/*
 **************************账户表*************************
 */
DROP TABLE IF EXISTS t_canary_loan_order;
CREATE TABLE t_canary_loan_order (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(64) NOT NULL COMMENT '订单号',
  `user_code` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `order_type` varchar(64) NOT NULL COMMENT '订单类型',
  `order_state` varchar(64) NOT NULL COMMENT '订单状态',
  `instalment` int  NULL COMMENT '分期数',
  `instalment_unit` varchar(64) NOT NULL COMMENT '分期单位',
  `instalment_rate` decimal(18,8) NOT NULL COMMENT '分期利率',
  `penalty_rate` decimal(18,8) NOT NULL COMMENT '分期利率',
  `apply_currency` varchar(64) NOT NULL COMMENT '借款币种',
  `anchor_currency` varchar(64) NOT NULL COMMENT '锚定币种',
  `apply_amount` decimal(18,8) NOT NULL COMMENT '申请借款金额, 对应借款币种',
  `lent_amount` decimal(18,8) NOT NULL COMMENT '放款金额, 对应借款币种',
  `anchor_amount` decimal(18,8) NOT NULL COMMENT '锚定金额, 对应锚定币种',
  `lent_time` bigint(20) NOT NULL COMMENT '放款时间',
  `end_time` bigint(20) NOT NULL COMMENT '结束时间',
  `timeZone` int  NULL COMMENT '时区',
  `remark` text COMMENT '备注',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除（0：否；1:是）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_code` (`user_code`) USING BTREE,
  UNIQUE INDEX `order_id` (`order_id`) USING BTREE,
  INDEX `update_time` (`update_time`) USING BTREE,
  INDEX `order_type` (`order_type`) USING BTREE,
  INDEX `order_state` (`update_time`) USING BTREE,
  INDEX `lent_time` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户表';
