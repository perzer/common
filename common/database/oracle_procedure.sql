
存储过程创建语法：

       create or replace procedure 存储过程名（param1 in type，param2 out type）

as

变量1 类型（值范围）;

变量2 类型（值范围）;

Begin

    Select count(*) into 变量1 from 表A where列名=param1；

    If (判断条件) then

       Select 列名 into 变量2 from 表A where列名=param1；

       Dbms_output。Put_line(‘打印信息’);

    Elsif (判断条件) then

       Dbms_output。Put_line(‘打印信息’);

    Else

       Raise 异常名（NO_DATA_FOUND）;

    End if;

Exception

    When others then

       Rollback;

End;

 

注意事项：

1，  存储过程参数不带取值范围，in表示传入，out表示输出

2，  变量带取值范围，后面接分号

3，  在判断语句前最好先用count（*）函数判断是否存在该条操作记录

4，  用select 。。。into。。。给变量赋值

5，  在代码中抛异常用 raise+异常名

 

 

以命名的异常

命名的系统异常                          产生原因

ACCESS_INTO_NULL                   未定义对象

CASE_NOT_FOUND                     CASE 中若未包含相应的 WHEN ，并且没有设置

ELSE 时

COLLECTION_IS_NULL                集合元素未初始化

CURSER_ALREADY_OPEN          游标已经打开

DUP_VAL_ON_INDEX                   唯一索引对应的列上有重复的值

INVALID_CURSOR                 在不合法的游标上进行操作

INVALID_NUMBER                       内嵌的 SQL 语句不能将字符转换为数字

NO_DATA_FOUND                        使用 select into 未返回行，或应用索引表未初始化的 

 

TOO_MANY_ROWS                      执行 select into 时，结果集超过一行

ZERO_DIVIDE                              除数为 0

SUBSCRIPT_BEYOND_COUNT     元素下标超过嵌套表或 VARRAY 的最大值

SUBSCRIPT_OUTSIDE_LIMIT       使用嵌套表或 VARRAY 时，将下标指定为负数

VALUE_ERROR                             赋值时，变量长度不足以容纳实际数据

LOGIN_DENIED                           PL/SQL 应用程序连接到 oracle 数据库时，提供了不

正确的用户名或密码

NOT_LOGGED_ON                       PL/SQL 应用程序在没有连接 oralce 数据库的情况下

访问数据

PROGRAM_ERROR                       PL/SQL 内部问题，可能需要重装数据字典＆ pl./SQL

系统包

ROWTYPE_MISMATCH                宿主游标变量与 PL/SQL 游标变量的返回类型不兼容

SELF_IS_NULL                             使用对象类型时，在 null 对象上调用对象方法

STORAGE_ERROR                        运行 PL/SQL 时，超出内存空间

SYS_INVALID_ID                         无效的 ROWID 字符串

TIMEOUT_ON_RESOURCE         Oracle 在等待资源时超时  

CREATE OR REPLACE PROCEDURE P_ACCOUNT_FILING_TOTAL(beginDate in VARCHAR2,
                                                   endDate   in VARCHAR2,
                                                   firmId    in VARCHAR2,
                                                   startPos  in NUMBER,
                                                   pageSize  in NUMBER,
                                                   isPage    in NUMBER,
                                                   total     out NUMBER,
                                                   my_cursor out SYS_REFCURSOR) AS
  sqls VARCHAR2(1000);--查询数据SQL
  sql_total VARCHAR2(1000);
BEGIN
  IF isPage = 1 AND startPos IS NOT NULL AND pageSize IS NOT NULL THEN
     sqls := ' SELECT * FROM (SELECT ROW_.*, ROWNUM ROWNUM_ FROM ( ';
  END IF;
  sql_total := 'SELECT T.FIRM_ID,
           COUNT(T.ID) NUMBERS,
           COUNT(DISTINCT T.CUSTOMER_ID) PEOPLE_NUM,
           SUM(DECODE(T.CUSTOMER_KIND, ''N'', 1, 0)) N_NUM,
           SUM(DECODE(T.CUSTOMER_KIND, ''L'', 1, 0)) L_NUM,
           COUNT(T.P_CARD_NO) CONTROL_NUM,
           COUNT(T.C_CARD_NO) BE_CONTROL_NUM
      FROM (SELECT * FROM WEB_CONTROL_RELATION_ACCOUNT WHERE 1 = 1 '; 
   IF beginDate IS NOT NULL THEN
      sql_total := sql_total || ' AND TRUNC(RECORD_DATE) >= TO_DATE(''' || beginDate || ''',''yyyy-MM-dd'') ';
   END IF;
   IF beginDate IS NOT NULL THEN
      sql_total := sql_total || ' AND TRUNC(RECORD_DATE) <= TO_DATE(''' || endDate || ''',''yyyy-MM-dd'') ';
   END IF;
   IF firmId IS NOT NULL THEN
      sql_total := sql_total || ' AND FIRM_ID = ''' || firmId || '''';
   END IF;
   sql_total := sql_total || ') T GROUP BY ROLLUP(T.FIRM_ID)';
   sqls := sqls || sql_total;
   sql_total := 'SELECT COUNT(1) FROM (' || sql_total || ')';
   OPEN my_cursor FOR sql_total;
   LOOP
        FETCH my_cursor into total;
        EXIT WHEN my_cursor%NOTFOUND;
   END LOOP;
   DBMS_OUTPUT.PUT_LINE(sql_total);
   IF isPage = 1 AND startPos IS NOT NULL AND pageSize IS NOT NULL THEN
     sqls := sqls || ' ) ROW_) WHERE ROWNUM_ <= ' || (startPos + pageSize) || 
     ' AND ROWNUM_ > ' || startPos;
   END IF;
   DBMS_OUTPUT.PUT_LINE(sqls);
  OPEN my_cursor for sqls;
END P_ACCOUNT_FILING_TOTAL
/* 实际控制关系账户报备情况统计 2011.05.26 朱培元*/;
/