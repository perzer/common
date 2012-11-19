CREATE OR REPLACE TYPE address_typ AS OBJECT (
       street     VARCHAR2(15),
       city       VARCHAR2(15),
       state      CHAR(2),
       zip        VARCHAR2(15)
)
/* 对象类型 */;
/

CREATE OR REPLACE TYPE varray_address_typ AS VARRAY(2) OF VARCHAR2(50)
/* 变长数组 */;
/

CREATE OR REPLACE TYPE nested_table_address_typ AS TABLE OF address_typ
/* 嵌套表,不要指定嵌套表的最大大小。可以在嵌套表中插入任意数组的元素 */;
/
