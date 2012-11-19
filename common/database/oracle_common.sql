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

/*
 * oracle sqlplus方向键使用 rlwrap:http://utopia.knoware.nl/~hlub/rlwrap/rlwrap-0.37.tar.gz
 * 在.bash_profile中配置如下
 * alias sqlplus='rlwrap sqlplus'
 * alias rman='rlwrap rman'
 */

/*
ORACLE exp/imp命令详解：
exp/imp一共有四种模式：Full、User、Tables、Tablespaces
Full
要执行Full模式的导出，必须具有EXP_FULL_DATABASE的权限。在Full模式下将导出数据库中所有用户的对象，但是有几个用户是受 oracle保护的不能被exp导出
（ORDSYS 、MDSYS 、CTXSYS 、ORDPLUGINS 、LBACSYS）sys用户的对象也不会导出，因此在full模式导出时属于sys用户的一些触发器之类的对象将会丢
失，在导入后应该手工创建。
Users
具有create session的用户，即可对属于自己的schame进行导出。Users模式将导出该模式下所有的对象及对象权限（不包括系统权限）等。
Tables
导出指定的表(表名可用通配符%)，当数据库中存在较大的表时，可以利用该模式进行并行导出可以加快速度。对于分区表，可以利用该模式导出某个或全部分区。
对于集群表导出时将不包含集群定义而以非集群方式导出，因此可以用这种模式取消表集群。
Tablespaces
进行Tablespaces模式主要用于进行transport Tablespaces，必须以sysdba权限才能进行。
除此之外，transport Tablespaces如果条件允许可以使用的话，应该是最快的数据迁移方式，因为它是直接进行操作系统文件拷贝。

参数介绍
Buffer
exp用这个参数来确定一次fetch所获取的最大行数。其默认值是由操作系统决定的，单位是byte。可以用buffer_size = rows_in_array * maximum_row_size来大概估算所需的大小，较大的rows_in_array能提高性能，但也不是越大越好（TOM建议100左右是个比较合适的值，但是不知道根据是什么）。当buffer设置为0时，exp将一行一行的获取，而且当表中含有LONG, LOB, BFILE, REF, ROWID, LOGICAL ROWID, DATE类型的列时，exp将忽略buffer的设置也将一行一行获取。在计算row_size时要注意每一列应加上两个byte的分隔符(null)长度，这个分隔符（null）是exp自动添加的，用以区别列与列。例如T（id varchar2(20),name varchar2(30)）则maximum_row_size=54（20+2+30+2）。
Buffer参数只对常规路径导出生效，直接路径导出有个与之相对的参数RecoredLength。
Compress
Compress参数不是设置对导出的内容进行压缩，而是指示exp如何控制create table中的storage语句。当compress设置为Y时，storage语句将包含一个和当前对象所占extent总和相当的一个初始 extent（这个初始extent可能很大），这样在导入时所有数据将都在初始extent中。设置为N时，将保持原始的参数设置。
这个参数默认值是Y，当不了解这个参数的设置时可能会造成一些错误。有一个故事是：有人想利用rows=n导出某个表结构，在原库中这个用户的表数据大概有20个G。当他设置rows=n时，他认为只需要很少的存储空间就可以完成导出。但是当导出进行时，他很诧异的发现所需空间不断膨胀，结果磁盘空间不够了只好不断删除旧文件凑空间，最后发现导出的文件所用空间和原来所占空间几乎一样。这里就是compress在捣乱。
File
File参数指定了导出文件的存储位置和存储文件名，结合参数FILESIZE使用可以指定多个文件名。
Filesize
在某些平台下文件的大小是有限制的，当导出的对象较大时就需要使用这个参数来指定文件的最大值。它必须配合FILE参数进行使用，file参数指定多个文件，当文件的值达到filesize时，exp自动转到下一个文件继续写。
File，Filesize组合在一起使用时，可以实现一边exp，一边imp的效果，这样就大大减少了等待时间。Exp的时候指定多个file name等导出一两个file后就可以开始进行imp了。但是要注意潜在的风险，因为exp可能会失败。
Rows
指定导出时，是否包括表中的数据行（注意compress参数）。
Query
Exp通过该参数提供导出表中一部分数据的功能。利用 query可以进行一些特殊操作，例如当我们需要导出一个非分区的大表时，可以使用query=where_clause 人为的将表分成几个不重叠部分进行并行导出，这样可以加快导出速度，再结合file，filesize还可以边exp边imp。
Direct
Direct指示exp使用常规路径导出（N）还是直接路径导出（Y）。当设置为Y它是一个很有用的提速参数，它带来的速度提升是非常惊人的。在一般的情况下都应该设置为Y，但是很遗憾，其默认值为N（在 10g 的data pump中这个情况已经改变）。关于direct的原理我一直没有弄得很清楚，有人说是类似于direct path read，但是官方文档上的解释并非如此。官方解释是direct=Y和direct=N相比90%的路径都是相同的，只是绕过了SQL evaluation buffer。Evaluation buffer主要是用于where 子句的处理，列格式的转化等功能，基于此当direct=Y时，query参数是非法的。注意，当表中含有lobs列时，即使指定 direct=Y，oracle也将使用常规路径导出这些表。

--导入
imp system/manager file=bible_db log=dible_db full=y ignore=y
--导出
exp system/manager file=dir full=y(owner=username)
*/

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