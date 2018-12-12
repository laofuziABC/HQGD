prompt MYSQL Developer import file
prompt Created on 2018-11-07 by yaorong
prompt Audited on 2018-11-07 by yaorong

prompt 201811071126_P_ADD_IDX.SQL Start!

/*增加索引时，判断是否已经存在，存在则先删后加，不存在直接加*/
DROP PROCEDURE IF EXISTS P_ADD_IDX;
DELIMITER $$

CREATE PROCEDURE P_ADD_IDX (
  IN p_tableName VARCHAR (200),
  IN p_idxName VARCHAR (200),
  IN p_sql VARCHAR (2000)
) 
BEGIN
  DECLARE sql_drop VARCHAR (500) ;
  DECLARE sql_create VARCHAR (2000) ;
  DECLARE fetchSeqOk_out INT DEFAULT 0 ;
  /*是否达到记录的末尾控制变量*/
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET fetchSeqOk_out = 1 ;
  /*如果没有找到则表示到记录的末尾，设置变量为1*/
  SELECT 
    COUNT(1) INTO @idx_cnt 
  FROM
    information_schema.statistics 
  WHERE TABLE_SCHEMA = DATABASE() 
    AND table_name = p_tableName 
    AND index_name = p_idxName ;
  IF @idx_cnt > 0 THEN
		SELECT CONCAT("该索引",p_tableName,".",p_idxName,"已经存在，删除成功！");
	  SET @sql_drop = CONCAT(' drop index ',p_idxName,' on ',DATABASE(),'.',p_tableName) ;
	  PREPARE stmt FROM @sql_drop ;
	  EXECUTE stmt ;
  END IF ;
  SELECT LOCATE(CONCAT(' ', p_tableName, ' '),p_sql) INTO @cnt1 ;
  IF @cnt1 > 0 THEN
	  SET @str_sql = REPLACE(
	    p_sql,
	    CONCAT(' ', p_tableName, ' '),
	    CONCAT(' ', DATABASE(),'.',p_tableName,' ')
	  ) ;
  END IF ;
  SELECT 
    LOCATE(CONCAT(' ', p_tableName, '('),p_sql) INTO @cnt2 ;
  IF @cnt2 > 0 THEN
	  SET @str_sql = REPLACE(
	    p_sql,
	    CONCAT(' ', p_tableName, '('),
	    CONCAT(' ', DATABASE(),'.',p_tableName,'(')
	  ) ;
  END IF ;
  PREPARE stmt_sql FROM @str_sql ;
  EXECUTE stmt_sql ;
  SET fetchSeqOk_out = 0 ;
END $$

DELIMITER ;

prompt 201811071126_P_ADD_IDX.SQL End!