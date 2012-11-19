5.5以上版本：
 groupadd mysql
 useradd -r -g mysql mysql
 tar zxvf mysql-VERSION.tar.gz
 cd mysql-VERSION
 cmake .
 make
 make install
 cd /usr/local/mysql
 chown -R mysql .
 chgrp -R mysql .
 scripts/mysql_install_db --user=mysql
 chown -R root .
 chown -R mysql data
 cp support-files/my-medium.cnf /etc/my.cnf
 bin/mysqld_safe --user=mysql &
 cp support-files/mysql.server /etc/init.d/mysql.server
