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
/*
 * 1.删除数据库对象
 * 2.删除JOB
 */
--删除数据库对象
SELECT 'DROP ' || T.OBJECT_TYPE || ' ' || T.OBJECT_NAME || ';' FROM USER_OBJECTS T
WHERE T.OBJECT_TYPE NOT IN ('INDEX','LOB')
ORDER BY T.OBJECT_TYPE,T.OBJECT_NAME;
--删除JOB
SELECT 'EXEC DBMS_JOB.REMOVE(' || T.JOB || ');'
	FROM USER_JOBS T
UNION ALL
SELECT 'COMMIT;' FROM DUAL;

--查询所有的dblink
SELECT * FROM ALL_DB_LINKS;
--删除dblink
DROP PUBLIC DATABASE LINK link_name;

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

--删除指定用户的所有数据
select 'drop ' || t.object_type || ' ' || t.object_name || ';' from user_objects t
where t.object_type not in ('INDEX','LOB')
order by t.object_type,t.object_name
--删除JOB
SELECT 'EXEC DBMS_JOB.REMOVE(' || T.JOB || ');'
	FROM USER_JOBS T
UNION ALL
SELECT 'COMMIT;' FROM DUAL;

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

--导入
imp system/manager file=dmp log=log full=y(formuser=user) ignore=y 
--导出
exp system/manager file=dir full=y(owner=username)

--删除空间
drop tablespace '空间名' including contents and datafiles;

--创建空间
create tablespace '空间名' 
datafile '/oracle/oradata/CZCESMSDB/CZCESMSDB.dbf' size 1000m autoextend on next 100m maxsize unlimited;

--创建用户  
create user '用户名' identified by '密码' default tablespace '空间名'; 

--赋予权限
grant dba,connect,resource to '用户名';


--恢复表数据
--分为两种方法：scn和时间戳两种方法恢复。
--一、通过scn恢复删除且已提交的数据
　　--1、获得当前数据库的scn号
　　　　select current_scn from v$database; --(切换到sys用户或system用户查询) 
　　　　--查询到的scn号为：1499223

　　--2、查询当前scn号之前的scn
　　　　select * from tablename as of scn 1499220; --(确定删除的数据是否存在，如果存在，则恢复数据；如果不是，则继续缩小scn号)

　　--3、恢复删除且已提交的数据
　　　　flashback table 表名 to scn 1499220;

--二、通过时间恢复删除且已提交的数据
　　--1、查询当前系统时间
　　　　select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual;

　　--2、查询删除数据的时间点的数据
　　　　select * from tablename as of timestamp to_timestamp('2013-05-29 15:29:00','yyyy-mm-dd hh24:mi:ss');  --(如果不是，则继续缩小范围)

　　--3、打开Flash存储的权限
	 alter table tablename enable row movement ;
	 
　　--4、恢复删除且已提交的数据
　　　　flashback table 表名 to timestamp to_timestamp('2013-05-29 15:29:00','yyyy-mm-dd hh24:mi:ss');