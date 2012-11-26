--linux启动数据库
1,以oracle用户登录
su oracle
2,启动TNS监听器
$ORACLE_HOME/bin/lsnrctl start
3,用sqlplus启动数据库
$ORACLE_HOME/bin/sqlplus /nolog
SQL> connect system/manager as sysdba
SQL> startup

出现如下显示，表示Oracle已经成功启动
ORACLE instance started.

Total System Global Area  205520896 bytes
Fixed Size                   778392 bytes
Variable Size              74456936 bytes
Database Buffers          130023424 bytes
Redo Buffers                 262144 bytes
Database mounted.
Database opened.

--执行job
EXEC DBMS_JOB.RUN('41');

--查看job运行状态
SELECT * FROM DBA_JOBS_RUNNING

--查询被锁的表
SELECT A.OWNER,
      A.OBJECT_NAME,
      B.XIDUSN,
      B.XIDSLOT,
      B.XIDSQN,
      B.SESSION_ID,
      B.ORACLE_USERNAME,
      B.OS_USER_NAME,
      B.PROCESS,
      B.LOCKED_MODE,
      C.MACHINE,
      C.STATUS,
      C.SERVER,
      C.SID,
      C.SERIAL#,
      C.PROGRAM
FROM ALL_OBJECTS A, V$LOCKED_OBJECT B, v$SESSION C
WHERE (A.OBJECT_ID = B.OBJECT_ID)
      AND (B.PROCESS = C.PROCESS)
      and  b.SESSION_ID=c.SID
    ORDER BY 1, 2
--杀掉
alter system kill session 'sid, serial#'  

--识别'低效执行'的SQL语句
SELECT  EXECUTIONS , DISK_READS, BUFFER_GETS,
ROUND((BUFFER_GETS-DISK_READS)/BUFFER_GETS,2) Hit_radio,
ROUND(DISK_READS/EXECUTIONS,2) Reads_per_run,
SQL_TEXT
FROM  V$SQLAREA
WHERE  EXECUTIONS>0
AND  BUFFER_GETS > 0
AND  (BUFFER_GETS-DISK_READS)/BUFFER_GETS < 0.8
ORDER BY  4 DESC;

--自动增长
CREATE TRIGGER TRG_CONTROL_RELATION_ACCOUNT BEFORE
INSERT ON WEB_CONTROL_RELATION_ACCOUNT FOR EACH ROW
BEGIN
	SELECT SEQ_CONTROL_RELATION_ACCOUNT.NEXTVAL INTO:NEW.ID FROM DUAL;
END;

--查询字符集编码
select parameter,value from nls_database_parameters where parameter like 'NLS_CHARACTERSET';

--在sqlplus查看oracle内存分配
show parameter sga;
show parameter pga;

--查看数据库版本
SELECT * FROM V$VERSION;

--查看用户所拥有的表：
SELECT TABLE_NAME FROM USER_TABLES;
--用户可存取的表：
SELECT TABLE_NAME FROM ALL_TABLES;
--数据库中所有表：
SELECT TABLE_NAME FROM DBA_TABLES；
--或者
SELECT * FROM TAB;

--赋予debug权限
grant debug connect session to username;

--导入
imp system/manager file=dmp log=log full=y(formuser=user) ignore=y 
--导出
exp system/manager file=dir full=y(owner=username)

/*
 生成执行计划：
 1.set autotrace on
 这个语句的优点就是它的缺点，这样在用该方法查看执行时间较长的sql语句时，需要等待该语句执行成功后，才返回执行计划，使优化的周期大大增长。
 2.set autotrace traceonly
 这样还是会执行语句。它比set autotrace on的优点是：不会显示出查询的数据，但是还是会将数据输出到客户端，这样当语句查询的数据比较多时，
 语句执行将会花费大量的时间，因为很大部分时间用在将数据从数据库传到客户端上了。
 3.set autotrace traceonly explain
 如同用explain plan命令。对于select 语句，不会执行select语句，而只是产生执行计划。但是对于dml语句，还是会执行语句，不同版本的数据
 库可能会有小的差别。这样在优化执行时间较长的select语句时，大大减少了优化时间，解决了 “set autotrace on”与“set autotrace traceonly”
 命令优化时执行时间长的问题，但同时带来的问题是：不会产生Statistics数据，而通过Statistics数据的物理I/O的次数，我们可以简单的判断语句执行效率的优劣。
 
 执行上述语句报错时：在要分析的用户下：
 SQL> @ ?/rdbms/admin/utlxplan.sql
 SQL> @ ?/sqlplus/admin/plustrce.sql
 SQL> grant plustrace to username;
设置sqlplus显示宽度：set linesize 100;
记录输出到文件：spool filename
		   select * from xxx;
		   spool off;
 
 干预执行计划，hints提示
 指示优化器的方法和目标的hints：
 ALL_ROWS --基于代价的优化器，以吞吐量为目标
 FIRST_ROWS(n) --基于代价的优化器，以响应时间为目标
 CHOOST --根据是否有统计信息，选择不同的优化器
 RULE --使用基于规则的优化器
 
 例子：
 	SELECT /*+ FIRST_ROWS(10) */ employee_id, last_name, salary, job_id
	FROM employees
	WHERE department_id = 20;
	
	SELECT /*+ CHOOSE */ employee_id, last_name, salary, job_id
	FROM employees
	WHERE employee_id = 7566;
	
	SELECT /*+ RULE */ employee_id, last_name, salary, job_id
	FROM employees
	WHERE employee_id = 7566;
	
 指示存储路径的hints:
 FULL /*+ FULL(table) */  指定该表使用全表扫描
 ROWID /*+ ROWID(table) */ 指定对该表使用rowid存取方法，不常用
 INDEX /*+ (table [index]) */ 使用该表上指定的索引对表进行扫描
 INDEX_FFS /*+ INDEX_FFS(table [index]) */ 使用快速索引扫描
 NO_INDEX /*+ NO_INDEX(table [index]) */ 不使用该表上的指定索引进行存取，仍然可以使用其他的索引进行索引扫描
 
 例子：
 	SELECT /*+ FULL(e) */ employee_id, last_name
	FROM employees e
	WHERE last_name LIKE :b1;
	
	SELECT /*+ROWID(employees)*/ *
	FROM employees
	WHERE rowid > 'AAAAtkAABAAAFNTAAA' AND employee_id = 155;
	
	SELECT /*+ INDEX(A sex_index) use sex_index because there are few
	male patients */ A.name, A.height, A.weight
	FROM patients A
	WHERE A.sex = ’m’;
	
	SELECT /*+NO_INDEX(employees emp_empid)*/ employee_id
	FROM employees
	WHERE employee_id > 200;

 指示连接顺序的hints:
 ORDERED /*+ ORDERED */ 按from字句中表的顺序从左到右的连接
 STAR /*+ STAR */ 指示优化器使用星型查询
 
 例子：
	SELECT /*+ORDERED */ o.order_id, c.customer_id, l.unit_price * l.quantity
	FROM customers c, order_items l, orders o
	WHERE c.cust_last_name = :b1
	AND o.customer_id = c.customer_id
	AND o.order_id = l.order_id;
	        
	/*+ ORDERED USE_NL(FACTS) INDEX(facts fact_concat) */
 指示连接类型的hints:
 USE_NL /*+ USE_NL(table [,table,table……]) */使用嵌套连接
 USE_MERAGE /*+ USE_MERAGE(table [,table,……) */ 使用排序--合并连接
 USE_HASH /*+ USE_HASH(table [,table,……) */ 使用HASH连接
 注意：如果表有alias(别名)，则上面的table是表的别名，而不是真实的表名
 
 */