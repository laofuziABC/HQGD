prompt MYSQL Developer import file
prompt Created on 2018-11-07 by yaorong
prompt Audited on 2018-11-07 by yaorong

prompt 201811071348_ACCESS_CONTROL_USER_INSERT.SQL Start!

-- 添加用户

INSERT INTO ACCESS_CONTROL_USER (
  ID,
  USER_NAME,
  PASSWORD,
  COMMENTS,
  ENABLED,
  ISDEL,
  ORG_NAME,
  CREATOR,
  CREATE_TIME,
  UPDATER,
  UPDATE_TIME
) 
SELECT 
  '1',
  'admin',
  'admin',
  '系统管理员',
  'Y',
  'N',
  '综合管理控制台',
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
    ACCESS_CONTROL_USER 
  WHERE ID = '1') ;

	
prompt 201811071348_ACCESS_CONTROL_USER_INSERT.SQL End!
