# 第一阶段必做功能代码落地清单（Phase 1）

> 项目：`village-fire-risk-management-system`
> 目标：确保 MVP 阶段可稳定支撑“登录 → 上报 → 分派 → 处理 → 风险查看 → 地图展示 → 报表导出”闭环。

## A. 基础运行与环境（P0）

- [x] Maven 项目可编译打包（`mvn clean package`）。
- [x] 后端端口固定 `8080`。
- [x] 前端静态端口固定 `5500`。
- [x] `application.yml` 配置 MySQL/JWT/上传目录/Swagger。
- [x] 数据库初始化脚本 `init.sql` 可一键建库、建表、建索引、写入 demo 数据。

## B. 认证与权限（P0）

- [x] 用户注册接口：`POST /api/v1/auth/register`。
- [x] 用户登录接口：`POST /api/v1/auth/login`。
- [x] JWT 鉴权过滤器生效，受保护接口需 Bearer Token。
- [x] 角色模型：`RESIDENT` / `GRID` / `ADMIN`。
- [x] 前端 token 存储键：`localStorage['token']`。

## C. 隐患治理主流程（P0）

- [x] 居民/网格/管理员可上报隐患：`POST /api/v1/hazards`。
- [x] 管理员分派：`POST /api/v1/hazards/{id}/assign`。
- [x] 网格员处理：`POST /api/v1/hazards/{id}/process`。
- [x] 隐患列表查询：`GET /api/v1/hazards`。
- [x] 处理过程留痕：`hazard_process_records`。

## D. 风险评估（P0）

- [x] 风险接口：`GET /api/v1/risk/areas`。
- [x] 落地算法：
  - `Σ(type_weight × count)`
  - 未完成超 7 天 +10
  - 未完成超 15 天 +20
  - 高危类型额外 +5
- [x] 风险等级映射：LOW/MID/HIGH/CRITICAL。

## E. 支撑功能（P1）

- [x] 文件上传：`POST /api/v1/files/upload`，映射 `/uploads/**`。
- [x] 知识推荐：`GET /api/v1/knowledge/recommend`。
- [x] 通知 Mock：`POST /api/v1/notifications/mock-send`，写入 `notification_log`。
- [x] Excel 导出：`GET /api/v1/reports/hazards/export`。
- [x] AOP 操作日志：注解 + 切面写入 `operation_log`。

## F. 统一规范（P0）

- [x] API 前缀：`/api/v1`。
- [x] 统一响应：`{ code, message, data }`。
- [x] 全局异常处理。
- [x] CORS 放行前端：`http://localhost:5500`。
- [x] Swagger 地址：`/swagger-ui.html`。

## G. 前端页面（P1）

- [x] `login.html`（登录）
- [x] `resident.html`（上报）
- [x] `grid.html`（处理）
- [x] `admin.html`（分派、风险、导出）
- [x] `map.html`（Leaflet + OSM）
- [x] 页面支持基础分页

---

## 建议验收脚本（手工）

1. 执行数据库初始化：`mysql -uroot -p < init.sql`
2. 启动后端：`mvn spring-boot:run`
3. 启动前端：`python -m http.server 5500 --directory frontend`
4. 打开 `http://localhost:5500/login.html`
5. 按流程验证：登录 → 上报 → 分派 → 处理 → 风险 → 地图 → 导出

---

## 第二阶段建议（预留）

- [ ] 增加自动化测试（单测/集成测试）
- [ ] 增加分页与条件查询后端接口
- [ ] 增加更细粒度 RBAC 权限控制
- [ ] 增加 Docker Compose 一键部署
- [ ] 增加前端组件化与可视化风格体系
