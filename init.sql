-- =====================================================
-- 项目: village-fire-risk-management-system
-- 说明: MySQL 8 初始化脚本（建库 + 建表 + 索引 + Demo 数据）
-- =====================================================

DROP DATABASE IF EXISTS village_fire_risk_db;
CREATE DATABASE village_fire_risk_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE village_fire_risk_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- 1) 用户与基础信息
-- =========================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(32),
    role ENUM('RESIDENT', 'GRID', 'ADMIN') NOT NULL,
    area_code VARCHAR(32),
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB;

CREATE TABLE housing_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    building_no VARCHAR(50) NOT NULL,
    unit_no VARCHAR(50),
    room_no VARCHAR(50),
    area_code VARCHAR(32) NOT NULL,
    address VARCHAR(255) NOT NULL,
    owner_name VARCHAR(100),
    owner_phone VARCHAR(32),
    occupancy_count INT DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE resident_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    housing_id BIGINT,
    id_card_no VARCHAR(64),
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(32),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_resident_info_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_resident_info_housing FOREIGN KEY (housing_id) REFERENCES housing_info(id)
) ENGINE=InnoDB;

CREATE TABLE facility_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    facility_name VARCHAR(100) NOT NULL,
    facility_type VARCHAR(50) NOT NULL,
    area_code VARCHAR(32) NOT NULL,
    address VARCHAR(255),
    status ENUM('NORMAL', 'MAINTENANCE', 'OFFLINE') NOT NULL DEFAULT 'NORMAL',
    last_inspection_time DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE dangerous_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_name VARCHAR(100) NOT NULL,
    goods_type VARCHAR(50) NOT NULL,
    hazard_level ENUM('LOW', 'MID', 'HIGH', 'CRITICAL') NOT NULL,
    description VARCHAR(500),
    storage_requirement VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE dangerous_goods_usage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL,
    housing_id BIGINT,
    facility_id BIGINT,
    user_id BIGINT,
    quantity DECIMAL(10,2) NOT NULL DEFAULT 0,
    unit VARCHAR(20) DEFAULT 'kg',
    usage_purpose VARCHAR(255),
    last_used_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_dgu_goods FOREIGN KEY (goods_id) REFERENCES dangerous_goods(id),
    CONSTRAINT fk_dgu_housing FOREIGN KEY (housing_id) REFERENCES housing_info(id),
    CONSTRAINT fk_dgu_facility FOREIGN KEY (facility_id) REFERENCES facility_info(id),
    CONSTRAINT fk_dgu_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- =========================
-- 2) 隐患与流程
-- =========================
CREATE TABLE hazards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    hazard_type VARCHAR(50) NOT NULL,
    area_code VARCHAR(32) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    image_url VARCHAR(500),
    status ENUM('REPORTED', 'ASSIGNED', 'IN_PROGRESS', 'RESOLVED', 'REJECTED') NOT NULL DEFAULT 'REPORTED',
    severity ENUM('LOW', 'MID', 'HIGH', 'CRITICAL') NOT NULL DEFAULT 'MID',
    reported_by BIGINT NOT NULL,
    assigned_to BIGINT,
    assigned_by BIGINT,
    assigned_at DATETIME,
    due_at DATETIME,
    resolved_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_hazards_reported_by FOREIGN KEY (reported_by) REFERENCES users(id),
    CONSTRAINT fk_hazards_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(id),
    CONSTRAINT fk_hazards_assigned_by FOREIGN KEY (assigned_by) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE hazard_process_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hazard_id BIGINT NOT NULL,
    action_type ENUM('REPORT', 'ASSIGN', 'PROCESS', 'RESOLVE', 'REJECT', 'COMMENT') NOT NULL,
    processor_id BIGINT NOT NULL,
    process_note VARCHAR(1000),
    before_status VARCHAR(50),
    after_status VARCHAR(50),
    attachment_url VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_hpr_hazard FOREIGN KEY (hazard_id) REFERENCES hazards(id),
    CONSTRAINT fk_hpr_processor FOREIGN KEY (processor_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE risk_type_weights (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hazard_type VARCHAR(50) NOT NULL,
    type_weight INT NOT NULL,
    is_high_risk TINYINT(1) NOT NULL DEFAULT 0,
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    remark VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_risk_type_weights_type (hazard_type)
) ENGINE=InnoDB;

-- =========================
-- 3) 知识库、通知、日志
-- =========================
CREATE TABLE knowledge_articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    target_role ENUM('RESIDENT', 'GRID', 'ADMIN', 'ALL') NOT NULL DEFAULT 'ALL',
    target_risk_level ENUM('LOW', 'MID', 'HIGH', 'CRITICAL', 'ALL') NOT NULL DEFAULT 'ALL',
    content TEXT NOT NULL,
    source VARCHAR(255),
    published TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE notification_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    channel ENUM('SMS', 'SYSTEM') NOT NULL DEFAULT 'SMS',
    template_code VARCHAR(100),
    receiver VARCHAR(100) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    status ENUM('SUCCESS', 'FAILED') NOT NULL,
    sent_at DATETIME,
    biz_type VARCHAR(50),
    biz_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    username VARCHAR(64),
    module VARCHAR(100) NOT NULL,
    operation VARCHAR(100) NOT NULL,
    method VARCHAR(20) NOT NULL,
    request_uri VARCHAR(255),
    request_params TEXT,
    result_code INT,
    duration_ms BIGINT,
    ip_address VARCHAR(64),
    user_agent VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_operation_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- =========================
-- 索引（按要求）
-- =========================
CREATE INDEX idx_hazards_area_status ON hazards(area_code, status);
CREATE INDEX idx_hazards_assigned_to ON hazards(assigned_to);
CREATE INDEX idx_hazards_created_at ON hazards(created_at);
CREATE INDEX idx_users_username ON users(username);

CREATE INDEX idx_hpr_hazard ON hazard_process_records(hazard_id);
CREATE INDEX idx_notification_biz ON notification_log(biz_type, biz_id);
CREATE INDEX idx_operation_module_time ON operation_log(module, created_at);
CREATE INDEX idx_knowledge_role_risk ON knowledge_articles(target_role, target_risk_level);

-- =========================
-- Demo 数据
-- 默认密码: Password@123
-- BCrypt: $2y$12$f7CJF6ouRqGC8WTGoVik9uBqdN70dMJChy2ObtSnXxhq/HZSqy7L6
-- =========================
INSERT INTO users (id, username, password, full_name, phone, role, area_code, enabled) VALUES
(1, 'admin', '$2y$12$f7CJF6ouRqGC8WTGoVik9uBqdN70dMJChy2ObtSnXxhq/HZSqy7L6', '系统管理员', '13800000001', 'ADMIN', 'A001', 1),
(2, 'grid_zhang', '$2y$12$f7CJF6ouRqGC8WTGoVik9uBqdN70dMJChy2ObtSnXxhq/HZSqy7L6', '张网格', '13800000002', 'GRID', 'A001', 1),
(3, 'grid_li', '$2y$12$f7CJF6ouRqGC8WTGoVik9uBqdN70dMJChy2ObtSnXxhq/HZSqy7L6', '李网格', '13800000003', 'GRID', 'A002', 1),
(4, 'resident_wang', '$2y$12$f7CJF6ouRqGC8WTGoVik9uBqdN70dMJChy2ObtSnXxhq/HZSqy7L6', '王居民', '13800000004', 'RESIDENT', 'A001', 1),
(5, 'resident_zhao', '$2y$12$f7CJF6ouRqGC8WTGoVik9uBqdN70dMJChy2ObtSnXxhq/HZSqy7L6', '赵居民', '13800000005', 'RESIDENT', 'A002', 1);

INSERT INTO housing_info (id, building_no, unit_no, room_no, area_code, address, owner_name, owner_phone, occupancy_count) VALUES
(1, '1栋', '1单元', '101', 'A001', '幸福路1号1栋1单元101', '王居民', '13800000004', 3),
(2, '2栋', '2单元', '202', 'A002', '平安路8号2栋2单元202', '赵居民', '13800000005', 2),
(3, '3栋', '1单元', '303', 'A001', '幸福路3号1单元303', '刘先生', '13800000006', 4);

INSERT INTO resident_info (id, user_id, housing_id, id_card_no, emergency_contact, emergency_phone) VALUES
(1, 4, 1, '110101199001011234', '王父', '13900000001'),
(2, 5, 2, '110101199202023456', '赵母', '13900000002');

INSERT INTO facility_info (id, facility_name, facility_type, area_code, address, status, last_inspection_time) VALUES
(1, 'A001消防泵房', 'PUMP_ROOM', 'A001', '幸福路消防泵房', 'NORMAL', NOW() - INTERVAL 5 DAY),
(2, 'A002微型消防站', 'FIRE_STATION', 'A002', '平安路微型消防站', 'NORMAL', NOW() - INTERVAL 3 DAY),
(3, 'A001配电室', 'POWER_ROOM', 'A001', '幸福路配电室', 'MAINTENANCE', NOW() - INTERVAL 10 DAY);

INSERT INTO dangerous_goods (id, goods_name, goods_type, hazard_level, description, storage_requirement) VALUES
(1, '液化石油气', 'GAS', 'HIGH', '居民常用燃气', '远离火源，保持通风'),
(2, '稀释剂', 'CHEMICAL', 'CRITICAL', '易燃溶剂', '防爆柜储存'),
(3, '柴油', 'FUEL', 'HIGH', '备用发电燃料', '阴凉通风，防泄漏');

INSERT INTO dangerous_goods_usage (id, goods_id, housing_id, facility_id, user_id, quantity, unit, usage_purpose, last_used_at) VALUES
(1, 1, 1, NULL, 4, 15.00, 'kg', '居民炊事', NOW() - INTERVAL 1 DAY),
(2, 2, NULL, 3, 2, 8.50, 'L', '设备维护', NOW() - INTERVAL 2 DAY),
(3, 3, NULL, 1, 3, 30.00, 'L', '应急发电', NOW() - INTERVAL 7 DAY);

INSERT INTO risk_type_weights (id, hazard_type, type_weight, is_high_risk, enabled, remark) VALUES
(1, 'ELECTRICAL', 8, 1, 1, '电气线路老化、私拉乱接'),
(2, 'GAS_LEAK', 10, 1, 1, '燃气泄漏、软管老化'),
(3, 'BLOCKED_EXIT', 5, 0, 1, '消防通道堵塞'),
(4, 'ILLEGAL_STORAGE', 7, 1, 1, '易燃易爆物违规堆放'),
(5, 'FIRE_FACILITY_DAMAGE', 6, 0, 1, '灭火器/消火栓损坏'),
(6, 'OPEN_FLAME', 9, 1, 1, '违规动火');

INSERT INTO hazards (id, title, description, hazard_type, area_code, address, latitude, longitude, image_url, status, severity, reported_by, assigned_to, assigned_by, assigned_at, due_at, resolved_at, created_at, updated_at) VALUES
(1, '楼道杂物堆积', '2层楼道堆放纸箱影响逃生', 'BLOCKED_EXIT', 'A001', '幸福路1号2层', 31.2304100, 121.4737000, '/uploads/h1.jpg', 'ASSIGNED', 'MID', 4, 2, 1, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 1 DAY, NULL, NOW() - INTERVAL 9 DAY, NOW() - INTERVAL 8 DAY),
(2, '燃气软管老化', '厨房燃气软管裂纹明显', 'GAS_LEAK', 'A001', '幸福路1号101', 31.2305000, 121.4736000, '/uploads/h2.jpg', 'IN_PROGRESS', 'HIGH', 4, 2, 1, NOW() - INTERVAL 16 DAY, NOW() - INTERVAL 3 DAY, NULL, NOW() - INTERVAL 17 DAY, NOW() - INTERVAL 16 DAY),
(3, '电动车飞线充电', '夜间飞线充电存在火灾风险', 'ELECTRICAL', 'A001', '幸福路3号楼下', 31.2303000, 121.4735000, '/uploads/h3.jpg', 'REPORTED', 'HIGH', 5, NULL, NULL, NULL, NOW() + INTERVAL 2 DAY, NULL, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
(4, '消火栓损坏', '小区消火栓阀门损坏漏水', 'FIRE_FACILITY_DAMAGE', 'A002', '平安路8号门口', 31.2310000, 121.4740000, '/uploads/h4.jpg', 'RESOLVED', 'MID', 5, 3, 1, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 1 DAY),
(5, '地下室存放汽油', '地下室存放多桶汽油', 'ILLEGAL_STORAGE', 'A002', '平安路8号地下室', 31.2312000, 121.4741000, '/uploads/h5.jpg', 'ASSIGNED', 'CRITICAL', 5, 3, 1, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 10 DAY, NULL, NOW() - INTERVAL 21 DAY, NOW() - INTERVAL 20 DAY),
(6, '楼道应急灯故障', '应急照明无法正常开启', 'FIRE_FACILITY_DAMAGE', 'A001', '幸福路5号3层', 31.2307000, 121.4739000, '/uploads/h6.jpg', 'REPORTED', 'LOW', 4, NULL, NULL, NULL, NOW() + INTERVAL 5 DAY, NULL, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
(7, '违规使用明火', '公共区域烧烤用明火', 'OPEN_FLAME', 'A002', '平安路中心广场', 31.2315000, 121.4745000, '/uploads/h7.jpg', 'IN_PROGRESS', 'HIGH', 5, 3, 1, NOW() - INTERVAL 4 DAY, NOW() + INTERVAL 1 DAY, NULL, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 4 DAY),
(8, '消防通道停放车辆', '消防通道被私家车占用', 'BLOCKED_EXIT', 'A001', '幸福路南门', 31.2302000, 121.4732000, '/uploads/h8.jpg', 'ASSIGNED', 'MID', 4, 2, 1, NOW() - INTERVAL 3 DAY, NOW() + INTERVAL 2 DAY, NULL, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 3 DAY),
(9, '电表箱异味', '电表箱有焦糊味', 'ELECTRICAL', 'A002', '平安路2栋1层', 31.2313000, 121.4743000, '/uploads/h9.jpg', 'REPORTED', 'HIGH', 5, NULL, NULL, NULL, NOW() + INTERVAL 1 DAY, NULL, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
(10, '楼顶杂物可燃物堆积', '楼顶堆放木材和纸板', 'ILLEGAL_STORAGE', 'A001', '幸福路7号楼顶', 31.2309000, 121.4742000, '/uploads/h10.jpg', 'RESOLVED', 'MID', 4, 2, 1, NOW() - INTERVAL 12 DAY, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 4 DAY);

INSERT INTO hazard_process_records (id, hazard_id, action_type, processor_id, process_note, before_status, after_status, attachment_url, created_at, updated_at) VALUES
(1, 1, 'ASSIGN', 1, '分派至A001网格员', 'REPORTED', 'ASSIGNED', NULL, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY),
(2, 2, 'ASSIGN', 1, '高风险优先处理', 'REPORTED', 'ASSIGNED', NULL, NOW() - INTERVAL 16 DAY, NOW() - INTERVAL 16 DAY),
(3, 2, 'PROCESS', 2, '已联系居民更换软管', 'ASSIGNED', 'IN_PROGRESS', '/uploads/p2.jpg', NOW() - INTERVAL 14 DAY, NOW() - INTERVAL 14 DAY),
(4, 4, 'RESOLVE', 3, '更换阀门并测试通过', 'IN_PROGRESS', 'RESOLVED', '/uploads/p4.jpg', NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
(5, 5, 'ASSIGN', 1, '危化品案件，立即处理', 'REPORTED', 'ASSIGNED', NULL, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 20 DAY),
(6, 7, 'PROCESS', 3, '已现场劝阻并持续巡查', 'ASSIGNED', 'IN_PROGRESS', NULL, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
(7, 10, 'RESOLVE', 2, '清理完毕并复查', 'IN_PROGRESS', 'RESOLVED', '/uploads/p10.jpg', NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY);

INSERT INTO knowledge_articles (id, title, category, target_role, target_risk_level, content, source, published) VALUES
(1, '家庭燃气安全自查清单', 'GAS', 'RESIDENT', 'MID', '检查软管老化、阀门关闭、报警器电量。', '消防宣传手册', 1),
(2, '电动车充电防火指南', 'ELECTRICAL', 'RESIDENT', 'HIGH', '禁止飞线充电，集中充电点管理。', '应急管理部', 1),
(3, '网格员隐患巡查标准流程', 'PROCESS', 'GRID', 'ALL', '巡查-拍照-定位-上报-复核闭环。', '街道安委会', 1),
(4, '高层建筑消防设施检查要点', 'FACILITY', 'GRID', 'HIGH', '重点检查喷淋、消火栓、泵房压力。', '住建消防规范', 1),
(5, '重大风险挂牌督办机制', 'MANAGEMENT', 'ADMIN', 'CRITICAL', '建立清单、责任人、时限与复盘机制。', '区应急局', 1),
(6, '居民逃生路线规划建议', 'ESCAPE', 'RESIDENT', 'LOW', '熟悉楼道与安全出口，避免杂物堵塞。', '社区服务中心', 1),
(7, '危险化学品小规模储存要求', 'CHEMICAL', 'ADMIN', 'HIGH', '分类分区、防泄漏、防静电。', '危化管理条例', 1),
(8, '消防通道治理专项行动模板', 'BLOCKED_EXIT', 'GRID', 'MID', '规范告知、清障执法、复查通报。', '城管联动办', 1),
(9, '老旧小区电气火灾预防', 'ELECTRICAL', 'ALL', 'MID', '定期检测线路，及时更换老化线缆。', '电力安全白皮书', 1),
(10, '社区级应急演练组织手册', 'DRILL', 'ALL', 'ALL', '每季度组织一次疏散与灭火演练。', '社区应急预案', 1);

INSERT INTO notification_log (id, user_id, channel, template_code, receiver, content, status, sent_at, biz_type, biz_id) VALUES
(1, 2, 'SMS', 'HAZARD_ASSIGN', '13800000002', '您有新的隐患待处理：#1', 'SUCCESS', NOW() - INTERVAL 8 DAY, 'HAZARD_ASSIGN', 1),
(2, 3, 'SMS', 'HAZARD_ASSIGN', '13800000003', '您有新的隐患待处理：#5', 'SUCCESS', NOW() - INTERVAL 20 DAY, 'HAZARD_ASSIGN', 5),
(3, 4, 'SYSTEM', 'HAZARD_FEEDBACK', 'resident_wang', '您上报的隐患#10已完成处理', 'SUCCESS', NOW() - INTERVAL 4 DAY, 'HAZARD_RESOLVE', 10);

INSERT INTO operation_log (id, user_id, username, module, operation, method, request_uri, request_params, result_code, duration_ms, ip_address, user_agent) VALUES
(1, 1, 'admin', 'Hazard', 'Assign Hazard', 'POST', '/api/v1/hazards/5/assign', '{"gridUserId":3}', 0, 43, '127.0.0.1', 'PostmanRuntime/7.39.0'),
(2, 2, 'grid_zhang', 'Hazard', 'Process Hazard', 'POST', '/api/v1/hazards/2/process', '{"note":"更换软管"}', 0, 58, '127.0.0.1', 'Mozilla/5.0'),
(3, 5, 'resident_zhao', 'Hazard', 'Report Hazard', 'POST', '/api/v1/hazards', '{"type":"ELECTRICAL"}', 0, 71, '127.0.0.1', 'Mozilla/5.0');

SET FOREIGN_KEY_CHECKS = 1;
