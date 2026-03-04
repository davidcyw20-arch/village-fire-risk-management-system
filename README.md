# village-fire-risk-management-system

企业级村镇消防风险管理系统（纯 Web），包含 Spring Boot 3.x 后端 + 原生 HTML/JS 前端。

## 项目定位（毕业设计最新范围）

- **仅网页端，不做移动端**。
- 三端架构：**居民用户端 / 网格员管理端 / 系统管理员端**。
- 技术架构：`Spring Boot + 原生HTML/JS + MySQL + GIS(Leaflet) + 短信Mock + 报表导出`。

### 业务背景
城中村建筑密集、人口流动性大，存在消防通道堵塞、设施老化、安全意识薄弱等问题。传统纸质台账与人工巡检存在记录易丢失、难追溯、响应滞后、知识宣教单一、协同效率低等缺陷。

### 毕设核心模块
1. **隐患多渠道上报与处理**：居民拍照/文字/定位上报，网格员处理反馈，管理员督办。
2. **风险评估与GIS可视化**：区域风险评分与地图点位展示。
3. **消防知识个性化推荐**：按角色与风险等级推送知识。
4. **用户信息管理**：用户基础信息增删改查与权限分层。

### 三端功能对应
- **居民端**：注册登录、个人信息、隐患上报、风险可视化、进度查询、信息接收、简易自查。
- **网格员端**：任务处理、处理反馈、巡查管理、辖区信息维护、统计查看。
- **管理员端**：系统配置、用户管理、流程监控、风险模型参数、知识管理、数据驾驶舱、日志审计。

---

## 1. 环境要求

- JDK 17
- Maven 3.9+
- MySQL 8.x
- 现代浏览器（Chrome / Edge / Firefox）
- （可选）Python 3 用于快速启动前端静态服务

> 端口约定：
> - 后端：`8080`
> - 前端：`5500`

---

## 2. 初始化数据库步骤

### 2.1 创建库表与演示数据
在 MySQL 中执行根目录 `init.sql`：

```bash
mysql -uroot -p < init.sql
```

该脚本会完成：
- 创建数据库：`village_fire_risk_db`
- 创建核心表、索引、外键
- 插入演示账号与业务数据（用户、隐患、知识库、权重、日志等）

### 2.2 数据库连接确认
默认连接配置位于 `src/main/resources/application.yml`：
- url: `jdbc:mysql://localhost:3306/village_fire_risk_db...`
- username: `root`
- password: `123456`

如本地账号密码不同，请自行修改。

---

## 3. application.yml 配置说明

配置文件路径：`src/main/resources/application.yml`

### 3.1 服务配置
- `server.port=8080`

### 3.2 数据源配置
- `spring.datasource.url`：MySQL 连接串
- `spring.datasource.username/password`：数据库账号密码
- `spring.jpa.hibernate.ddl-auto=validate`：按现有表结构校验

### 3.3 文件上传配置
- `spring.servlet.multipart.max-file-size=10MB`
- `spring.servlet.multipart.max-request-size=20MB`
- `app.upload.path=uploads`

### 3.4 JWT 配置
- `app.jwt.secret`：JWT 签名密钥（生产环境请更换）
- `app.jwt.expiration-ms`：Token 过期时间（毫秒）

### 3.5 Swagger 配置
- `springdoc.swagger-ui.path=/swagger-ui.html`
- `springdoc.api-docs.path=/v3/api-docs`

---

## 4. 启动后端方式

在项目根目录执行：

```bash
mvn spring-boot:run
```

启动后可访问：
- API 文档：<http://localhost:8080/swagger-ui.html>
- OpenAPI JSON：<http://localhost:8080/v3/api-docs>

---

## 5. 启动前端方式

前端目录为 `/frontend`，使用任意静态文件服务启动，示例：

```bash
python -m http.server 5500 --directory frontend
```

访问：
- 登录页：<http://localhost:5500/login.html>
- 居民页：<http://localhost:5500/resident.html>
- 网格员页：<http://localhost:5500/grid.html>
- 管理员页：<http://localhost:5500/admin.html>
- 地图页：<http://localhost:5500/map.html>

---

## 6. 默认账号密码

> 所有默认账号密码相同：`Password@123`

### ADMIN（1个）
- `admin`

### GRID（2个）
- `grid_zhang`
- `grid_li`

### RESIDENT（2个）
- `resident_wang`
- `resident_zhao`

密码在数据库中以 BCrypt 哈希方式存储（见 `init.sql`）。

---

## 7. 测试流程（端到端）

按以下流程验证核心业务闭环：

1. **登录**
   - 打开 `login.html`
   - 使用管理员或居民账号登录（token 写入 `localStorage`，key=`token`）

2. **上报隐患（居民）**
   - 进入 `resident.html`
   - 填写隐患标题、描述、类型、经纬度、地址并提交

3. **分派隐患（管理员）**
   - 进入 `admin.html`
   - 输入隐患 ID 与网格员用户 ID，执行分派

4. **处理隐患（网格员）**
   - 进入 `grid.html`
   - 输入隐患 ID、处理说明，提交处理（IN_PROGRESS / RESOLVED）

5. **查看风险评估**
   - 在 `admin.html` 点击“刷新风险区域”
   - 或调用 `GET /api/v1/risk/areas`

6. **地图展示**
   - 打开 `map.html`
   - 查看隐患点位与详情弹窗
   - 点击地图可获取经纬度

7. **导出报表**
   - 在 `admin.html` 点击“导出Excel报表”
   - 或访问 `GET /api/v1/reports/hazards/export`

---

## 8. 关键接口速览

- 鉴权：`/api/v1/auth/register`、`/api/v1/auth/login`
- 隐患：`/api/v1/hazards`、`/api/v1/hazards/{id}/assign`、`/api/v1/hazards/{id}/process`
- 风险：`/api/v1/risk/areas`
- 上传：`/api/v1/files/upload`
- 知识库：`/api/v1/knowledge/recommend`
- 通知：`/api/v1/notifications/mock-send`
- 报表：`/api/v1/reports/hazards/export`

统一响应格式：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```
