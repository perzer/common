CREATE OR REPLACE FUNCTION fn_checkidcard(p_idcard IN VARCHAR2) RETURN INT IS
  v_regstr    VARCHAR2(2000);
  v_sum       NUMBER;
  v_mod       NUMBER;
  v_checkcode CHAR(11) := '10X98765432';
  v_checkbit  CHAR(1);
  v_areacode  VARCHAR2(2000) := '11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91,';
BEGIN
  CASE LENGTHB(p_idcard)
    WHEN 15 THEN
      -- 15位
      IF INSTRB(v_areacode, SUBSTR(p_idcard, 1, 2) || ',') = 0 THEN
        RETURN 0;
      END IF;
    
      IF MOD(TO_NUMBER(SUBSTRB(p_idcard, 6, 2)) + 1900, 400) = 0 OR
         (MOD(TO_NUMBER(SUBSTRB(p_idcard, 6, 2)) + 1900, 100) <> 0 AND
          MOD(TO_NUMBER(SUBSTRB(p_idcard, 6, 2)) + 1900, 4) = 0) THEN
        -- 闰年
        v_regstr := '^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$';
      ELSE
        v_regstr := '^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$';
      END IF;
    
      IF REGEXP_LIKE(p_idcard, v_regstr) THEN
        RETURN 1;
      ELSE
        RETURN 0;
      END IF;
    WHEN 18 THEN
      -- 18位
      IF INSTRB(v_areacode, SUBSTRB(p_idcard, 1, 2) || ',') = 0 THEN
        RETURN 0;
      END IF;
    
      IF MOD(TO_NUMBER(SUBSTRB(p_idcard, 6, 4)), 400) = 0 OR
         (MOD(TO_NUMBER(SUBSTRB(p_idcard, 6, 4)), 100) <> 0 AND
          MOD(TO_NUMBER(SUBSTRB(p_idcard, 6, 4)), 4) = 0) THEN
        -- 闰年
        v_regstr := '^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$';
      ELSE
        v_regstr := '^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$';
      END IF;
    
      IF REGEXP_LIKE(p_idcard, v_regstr) THEN
        v_sum      := (TO_NUMBER(SUBSTRB(p_idcard, 1, 1)) +
                      TO_NUMBER(SUBSTRB(p_idcard, 11, 1))) * 7 +
                      (TO_NUMBER(SUBSTRB(p_idcard, 2, 1)) +
                      TO_NUMBER(SUBSTRB(p_idcard, 12, 1))) * 9 +
                      (TO_NUMBER(SUBSTRB(p_idcard, 3, 1)) +
                      TO_NUMBER(SUBSTRB(p_idcard, 13, 1))) * 10 +
                      (TO_NUMBER(SUBSTRB(p_idcard, 4, 1)) +
                      TO_NUMBER(SUBSTRB(p_idcard, 14, 1))) * 5 +
                      (TO_NUMBER(SUBSTRB(p_idcard, 5, 1)) +
                      TO_NUMBER(SUBSTRB(p_idcard, 15, 1))) * 8 +
                      (TO_NUMBER(SUBSTRB(p_idcard, 6, 1)) +
                      TO_NUMBER(SUBSTRB(p_idcard, 16, 1))) * 4 +
                      (TO_NUMBER(SUBSTRB(p_idcard, 7, 1)) +
                      TO_NUMBER(SUBSTRB(p_idcard, 17, 1))) * 2 +
                      TO_NUMBER(SUBSTRB(p_idcard, 8, 1)) * 1 +
                      TO_NUMBER(SUBSTRB(p_idcard, 9, 1)) * 6 +
                      TO_NUMBER(SUBSTRB(p_idcard, 10, 1)) * 3;
        v_mod      := MOD(v_sum, 11);
        v_checkbit := SUBSTRB(v_checkcode, v_mod + 1, 1);
      
        IF v_checkbit = SUBSTRB(p_idcard, 18, 1) THEN
          RETURN 1;
        ELSE
          RETURN 0;
        END IF;
      ELSE
        RETURN 0;
      END IF;
    ELSE
      RETURN 0; -- 身份证号码位数不对
  END CASE;
EXCEPTION
  WHEN OTHERS THEN
    RETURN 0;
END fn_checkidcard;
