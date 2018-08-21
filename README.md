## 订单系统

目的: 
    1. 以自己的想法构建并实现订单系统
    2. 测试mysql5.7的json类型

目标订单系统特性: 
    1. 支持分期
    2. 支持部分还款
    3. 支持多订单多次还款
    4. 使用一般等价物, 以支持借还完全分离
    
设计思路:
    1. 订单表,       存订单基本信息, 还款时, 该表只会改变状态(state)字段
    2. 分期表,       存订单分期信息, 还款时, 会改变状态以及应还字段
    3. 还款表,       存用户/系统一次还款操作信息
    4. 优惠券表,     用户所拥有的优惠券
    5. 系统优惠表,   系统所使用的借款/还款策略表
    6. 还款明细表,   记录还款详细信息, 精确到某一期的本金/利息等, 同时会包括减免明细信息
    7. 借还款设置表, 用于设置一些参数
    
问题与思考
1. 产品可能会要求用户支付一些与订单无关的费用, 但是却要和订单绑定, 还款时按一定顺序还款, 如何支持?
答: 需要还款顺序枚举, 并且需要两个额外字段: 和订单绑定的费用, 还款时有可能分期, 或固定某一起, 分别存在这两个json字段中

2. 还款时, 一旦出现乘除法, 就有可能出现剩下一分钱还不清或多还一分钱的情况, 如何避免?
答: 在生成还款订单前做好乘除法运算, 再插入订单, 避免乘除法

3. 订单的要素?
答: 我能想到的, 是在某个时间点, 要还多少钱, 这些钱用于还了哪些应还项目, 所以: 
    1. 还款时间
    2. 每期应还金额
    3. 应还类型入账顺序
这三个要素决定了订单的类型


关于每日结算:
如果每日结算发生异常, 将会有巨大的不确定性, 且恢复困难, 而且, 以账单为单位进行每日结算, 量将会是巨大的,
每日结算的目的其实是更新分期表中的应还金额字段.
作以下处理:
会有每日结算定时器去更新分期表中的应还金额字段, 但时这些字段主要是做报表的时候使用.
在程序有一个开关, 可以决定拿当前应还金额是实时计算还是直接取数据库里的值,默认为实时计算