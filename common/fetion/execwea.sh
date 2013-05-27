#!/bin/sh

# 1. set path
FETION_DIR=/usr/py/fetion
BASE_DIR=/usr/py/fetion/weather
HTML2TEXT_DIR=/usr/bin

# 3. get weather from internet
wget -nv -O $BASE_DIR/weather.html http://wap.weather.com.cn/wap/weather/101180101.shtml > $BASE_DIR/log 2>&1

# 4. format
echo "" > $BASE_DIR/msg
cat $BASE_DIR/weather.html | $HTML2TEXT_DIR/html2text | grep -A 6 '日'  | sed 's/\[.*//g' | sed 's/.*]$//g' | sed 's/ //g' | grep -v '^$' | head -n 9 > $BASE_DIR/msg

# 5. send sms
echo `date` > $BASE_DIR/log
sh $FETION_DIR/fetion.sh sendno pwd reciveno "`cat $BASE_DIR/msg`" >> $BASE_DIR/log 2>&1

# 6. clear env
rm -r $BASE_DIR/weather.html
rm -r $BASE_DIR/msg
