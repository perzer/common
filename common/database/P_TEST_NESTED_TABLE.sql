CREATE OR REPLACE PROCEDURE P_TEST_NESTED_TABLE(lines OUT NUMBER) AS
  TYPE record_typ IS TABLE OF V_CONTROL_RELATION_TOTAL_REAL%ROWTYPE;
  record    V_CONTROL_RELATION_TOTAL_REAL%ROWTYPE;
  records   record_typ;
  my_cursor SYS_REFCURSOR;
BEGIN
  SELECT * BULK COLLECT
    INTO records
    FROM V_CONTROL_RELATION_TOTAL_REAL
   WHERE ROWNUM <= 5;
  DBMS_OUTPUT.PUT_LINE('before delete');

  FOR i IN records.FIRST .. records.LAST LOOP
    DBMS_OUTPUT.PUT_LINE('records(' || i || ').P_CUSTOMER_ID=' ||
                         records(i).P_CUSTOMER_ID);
  END LOOP;

  records.DELETE(4);
  DBMS_OUTPUT.NEW_LINE();
  DBMS_OUTPUT.PUT_LINE('after delete');

  FOR i IN records.FIRST .. records.LAST LOOP
    IF records.EXISTS(i) THEN
      DBMS_OUTPUT.PUT_LINE('records(' || i || ').P_CUSTOMER_ID=' ||
                           records(i).P_CUSTOMER_ID);
    ELSE
      DBMS_OUTPUT.PUT_LINE('records(' || i || ') = null');
    END IF;
  END LOOP;
END;
/
