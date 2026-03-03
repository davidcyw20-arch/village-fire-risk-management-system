# village-fire-risk-management-system 数据库 ER 结构说明（第一步）

## 1. 设计目标
本数据库围绕“农村/社区消防隐患治理”构建，覆盖：
- 用户与角色（居民/网格员/管理员）
- 隐患上报、分派、处理闭环
- 风险权重与区域风险评估支撑
- 知识库推荐支撑
- 通知日志与操作日志审计
- 住户、房屋、设施、危险品管理

---

## 2. 实体与主关系

### 2.1 用户与档案层
1. **users**（系统用户）
   - 角色：`RESIDENT` / `GRID` / `ADMIN`
   - 核心字段：username、password(BCrypt)、role、area_code
   - 约束：username 唯一

2. **housing_info**（房屋信息）
   - 房屋地址、楼栋、单元、房间、区域编码、户主信息

3. **resident_info**（居民扩展信息）
   - `user_id -> users.id`（1:1 或 1:N）
   - `housing_id -> housing_info.id`（N:1）
   - 用于居民身份与房屋关联

4. **facility_info**（公共设施信息）
   - 如消防泵房、微型消防站、配电室等

### 2.2 危险源层
5. **dangerous_goods**（危险品字典）
   - 危险品类型、危险等级、存储要求

6. **dangerous_goods_usage**（危险品使用/存放记录）
   - `goods_id -> dangerous_goods.id`
   - 可关联 `housing_id` 或 `facility_id`
   - 可记录责任人 `user_id`

### 2.3 隐患治理层
7. **hazards**（隐患主表）
   - 上报主体、类型、图片、位置（经纬度）、地址、状态、分派信息
   - 关联：
     - `reported_by -> users.id`
     - `assigned_to -> users.id`
     - `assigned_by -> users.id`

8. **hazard_process_records**（隐患流程记录）
   - `hazard_id -> hazards.id`
   - `processor_id -> users.id`
   - 记录报告、分派、处理、办结等动作链

### 2.4 风险与知识层
9. **risk_type_weights**（隐患类型权重）
   - 每种隐患类型对应风险权重 `type_weight`
   - `is_high_risk` 用于“高危额外 +5 分”规则

10. **knowledge_articles**（知识库文章）
   - 按 `target_role` + `target_risk_level` 定向推荐

### 2.5 审计与消息层
11. **notification_log**（通知日志）
   - 短信/系统通知 Mock 留痕

12. **operation_log**（操作日志）
   - 记录接口、模块、耗时、结果码、IP 等

---

## 3. 关键关系（文字 ER）
- users (1) —— (N) hazards.reported_by
- users (1) —— (N) hazards.assigned_to
- users (1) —— (N) hazards.assigned_by
- hazards (1) —— (N) hazard_process_records
- users (1) —— (N) hazard_process_records.processor_id
- users (1) —— (N) resident_info
- housing_info (1) —— (N) resident_info
- dangerous_goods (1) —— (N) dangerous_goods_usage
- housing_info (1) —— (N) dangerous_goods_usage
- facility_info (1) —— (N) dangerous_goods_usage
- users (1) —— (N) dangerous_goods_usage
- users (1) —— (N) notification_log
- users (1) —— (N) operation_log

---

## 4. 索引设计
按要求建立：
- `hazards(area_code, status)`：支持按区域+状态统计与检索
- `hazards(assigned_to)`：支持网格员任务列表
- `hazards(created_at)`：支持时间序查询、报表
- `users(username)`：登录检索

并补充业务索引：
- `hazard_process_records(hazard_id)`
- `notification_log(biz_type, biz_id)`
- `operation_log(module, created_at)`
- `knowledge_articles(target_role, target_risk_level)`

---

## 5. 统一审计字段
所有表均包含：
- `id BIGINT PRIMARY KEY`
- `created_at DATETIME`
- `updated_at DATETIME`

已在 `init.sql` 中全部落地。
