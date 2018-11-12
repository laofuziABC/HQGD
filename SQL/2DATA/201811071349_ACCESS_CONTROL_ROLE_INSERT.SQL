prompt MYSQL Developer import file
prompt Created on 2018-11-07 by yaorong
prompt Audited on 2018-11-07 by yaorong

prompt 201811071349_ACCESS_CONTROL_ROLE_INSERT.SQL Start!

-- 添加角色
INSERT INTO ACCESS_CONTROL_ROLE  (
  ID,
  NAME,
  PID,
  ADMIN,
  ENABLED,
  COMMENTS,
  TYPE,
  CODE,
  CREATOR,
  CREATE_TIME,
  UPDATER,
  UPDATE_TIME
) 
SELECT 
  '1',
  '系统管理员',
  NULL,
  NULL,
  'Y',
  '系统管理员',
  '0',
  NULL,
  'root',
  NOW(),
  'root',
  NOW() 
FROM
  DUAL 
WHERE NOT EXISTS 
  (SELECT 
    ID 
  FROM
    ACCESS_CONTROL_ROLE 
  WHERE ID = '1') ;


  
prompt 201811071349_ACCESS_CONTROL_ROLE_INSERT.SQL End!
  