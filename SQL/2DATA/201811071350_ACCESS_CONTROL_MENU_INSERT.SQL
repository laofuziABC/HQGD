prompt MYSQL Developer import file
prompt Created on 2018-11-07 by yaorong
prompt Audited on 2018-11-07 by yaorong 

prompt 201811071350_ACCESS_CONTROL_MENU_INSERT.SQL Start!

-- 添加菜单
INSERT INTO ACCESS_CONTROL_MENU (
  ID,
  TEXT,
  TYPE,
  PID,
  SYS,
  ENABLED,
  URL,
  COMMENTS,
  ICON,
  CODE,
  MENU_ORDER,
  CREATOR,
  UPDATER
  
) 
SELECT 
  '1',
  '综合管理控制台',
  'GROUP',
  NULL,
  'root',
  'Y',
  '',
  '',
  '',
  'ZONG_HE_GUAN_LI_KONG_ZHI_TAI',
  '',
  'root',
  'root' 
FROM
  DUAL 
WHERE NOT EXISTS 
  (SELECT 
    ID 
  FROM
    ACCESS_CONTROL_MENU 
  WHERE ID = '1') ;

INSERT INTO ACCESS_CONTROL_MENU (
  ID,
  TEXT,
  TYPE,
  PID,
  SYS,
  ENABLED,
  URL,
  COMMENTS,
  ICON,
  CODE,
  MENU_ORDER,
  CREATOR,
  UPDATER
) 
SELECT 
  '2',
  '系统管理',
  'GROUP',
  '2',
  'root',
  'Y',
  '',
  '',
  '',
  'ZI_XI_TONG_GUAN_LI_1',
  '',
  'root',
  'root'
FROM
  DUAL 
WHERE NOT EXISTS 
  (SELECT 
    ID 
  FROM
    ACCESS_CONTROL_MENU 
  WHERE ID = '2') ;

INSERT INTO ACCESS_CONTROL_MENU (
  ID,
  TEXT,
  TYPE,
  PID,
  SYS,
  ENABLED,
  URL,
  COMMENTS,
  ICON,
  CODE,
  MENU_ORDER,
  CREATOR,
  UPDATER
) 
SELECT 
  '3',
  '用户管理',
  'MENU',
  '2',
  'root',
  'Y',
  'systemUser.next',
  '',
  '',
  'YONG_HU_GUAN_LI_1',
  '',
  'root',
  'root' 
FROM
  DUAL 
WHERE NOT EXISTS 
  (SELECT 
    ID 
  FROM
    ACCESS_CONTROL_MENU 
  WHERE ID = '3') ;

INSERT INTO ACCESS_CONTROL_MENU (
  ID,
  TEXT,
  TYPE,
  PID,
  SYS,
  ENABLED,
  URL,
  COMMENTS,
  ICON,
  CODE,
  MENU_ORDER,
  CREATOR,
  UPDATER
  
) 
SELECT 
  '4',
  '设备管理',
  'MENU',
  '2',
  'root',
  'Y',
  'equipment.next',
  '',
  '',
  'SHE_BEI_GUAN_LI_1',
  '',
  'root',
  'root'
FROM
  DUAL 
WHERE NOT EXISTS 
  (SELECT 
    ID 
  FROM
    ACCESS_CONTROL_MENU 
  WHERE ID = '4') ;

prompt 201811071350_ACCESS_CONTROL_MENU_INSERT.SQL End!
