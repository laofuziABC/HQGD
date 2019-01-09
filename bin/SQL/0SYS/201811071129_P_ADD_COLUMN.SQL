prompt MYSQL Developer import file
prompt Created on 2018-11-07 by yaorong
prompt Audited on 2018-11-07 by yaorong

prompt 201811071129_P_ADD_COLUMN.SQL Start!

/*字段存在则删除处理*/
/*传入参数p_tableName表名,p_columnName字段名,str_sql操作表结构的sql语句(如:alter table tb1 drop column field1)*/
DROP PROCEDURE IF EXISTS `P_ADD_COLUMN`;
DELIMITER $$

CREATE PROCEDURE `P_ADD_COLUMN`(IN p_tableName VARCHAR(200), IN p_columnName VARCHAR(200),IN str_sql VARCHAR(2000))
BEGIN  
DECLARE isExistColumn INT DEFAULT 0 ;
DECLARE str VARCHAR(500);
DECLARE fetchSeqOk_out INT DEFAULT 0; /*是否达到记录的末尾控制变量*/ 
/*获取除过MYSQL系统自身的数据库外的实例中该表的某个列的个数*/
DECLARE CONTINUE HANDLER FOR NOT FOUND SET fetchSeqOk_out = 1;/*如果没有找到则表示到记录的末尾，设置变量为1*/
SELECT LOCATE(p_columnName, str_sql) INTO isExistColumn ;
IF isExistColumn != 0 THEN 	
	SELECT COUNT(*) INTO @cnt FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND table_name=p_tableName AND column_name=p_columnName;  	
	IF @cnt > 0 THEN
		SET @str=REPLACE(str_sql,CONCAT(' ',p_tableName,' '),CONCAT(' ',DATABASE(),'.',p_tableName,' '));
		PREPARE stmt FROM @str;
		EXECUTE stmt ;
	ELSE
		SELECT CONCAT("该字段",p_tableName,".",p_columnName,"不存在！");
	END IF;
	SET fetchSeqOk_out=0;
ELSE
	SELECT CONCAT("字段",p_tableName,".",p_columnName,"必须语句中存在！");
END IF;
END$$

DELIMITER ;

prompt 201811071129_P_ADD_COLUMN.SQL End!