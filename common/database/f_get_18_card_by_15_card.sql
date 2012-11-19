CREATE OR REPLACE FUNCTION f_get_18_card_by_15_card(
                            p_card varchar2 --15位身份证号
                            )return varchar2 is
r_new_card varchar2(36);
i number;
j number;
s number;
TYPE TYPE2 IS TABLE OF VARCHAR2(10);
w TYPE2 := TYPE2('7', '9', '10', '5', '8', '4', '2', '1', '6', '3', '7', '9', '10', '5', '8', '4', '2', '1' );
a TYPE2 := TYPE2( '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' );
begin
  if(length(p_card) = 15  AND substr(p_card,length(p_card),1) != 'X') then
    r_new_card := substr(p_card,0,6) || '19' || substr(p_card,7);
    i :=0;
    j :=0;
    s :=0;
    for i in 1..length(r_new_card)
    loop
        j := to_number(substr(r_new_card,i,1))*w(i);
        s := s+j;
    end loop;
    s := mod(s,11);--s%11;
    r_new_card := r_new_card || a(s+1);  --取最后一位校验码
    return r_new_card;
  else
    r_new_card := p_card;
    return r_new_card;
  end if;
  exception
    when others then
    return p_card;
end;


/**
* 身份证15位升18位
* 黄红滔
**/
/
