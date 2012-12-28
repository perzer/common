netstat -nlp|grep 8080 查看8080端口是否被占用

开启shell执行：
touch /var/log/pacct
accton /var/log/pacct
查看：lastcomm -f /var/log/pacct

查看登录记录：last -f /var/log/wtmp

pgrep java == ps -ef|grep java

split 分割文件:split -b 50m filename.tar LF_
cat 合并文件:cat LF_*>filename.tar

安装open-ssh前需要安装：zlib、openssl。
在最后一步报错： Privilege separation user sshd does not exist 解决：在/etc/passwd 中加入： sshd:x:74:74:Privilege-separated SSH:/var/empty/sshd:/sbin/nologin 
再次make install 编译安装成功。
启动sshd时出现：Could not load host key: /etc/ssh/ssh_host_key时
在终端中输入：ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key 重新建立ssh_host_dsa_key文件
直接回车

在终端中输入：ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key 重新建立ssh_host_rsa_key文件
直接回车
然后启动sshd

ssh使用密钥登录：
这种方法要求用户必须提供自己的公钥。如果没有现成的，可以直接用ssh-keygen生成一个：

    　　$ ssh-keygen

运行上面的命令以后，系统会出现一系列提示，可以一路回车。其中有一个问题是，要不要对私钥设置口令（passphrase），如果担心私钥的安全，这里可以设置一个。

运行结束以后，在$HOME/.ssh/目录下，会新生成两个文件：id_rsa.pub和id_rsa。前者是你的公钥，后者是你的私钥。

这时再输入下面的命令，将公钥传送到远程主机host上面：

    　　$ ssh-copy-id user@host
   	可选-i ~/.ssh/id_rsa.pub

好了，从此你再登录，就不需要输入密码了。

配置网卡：
ifconfig eth0 172.16.3.233 netmask 255.255.0.0 up 
route add default gw 172.16.3.95
vi /etc/resolv.conf 
文件内容：
nameserver 222.85.85.85

查看操作系统位数：getconf LONG_BIT
查看CPU个数：cat /proc/cpuinfo|grep processor

查看进程数：ps -ef|cat -n

重复执行过的命令：!-n

已HTTP方式共享当前文件夹：$ python -m SimpleHTTPServer

在以普通用户打开的VIM中保存一个ROOT用户文件：:w !sudo tee %

切换回上一个目录：$cd -

替换上一条命令中的一个短语：$^foo^bar^ 原始命令是：$!!:s/foo/bar/

快速备份一个文件：$cp filename{,.bak} 原理：bash对大括号的展开操作

用喜欢的编辑器来敲命令：command <CTRL-x CTRL-e>,指定编辑器：$export EDITOR=vim

清空或创建一个文件：$> file.txt,清空文件内容；$touch file.txt,touch本来是用作修改文件的时间戳，但文件如果不存在，就自动创建了。

重置终端：reset

映射一个内存目录：$mount -t tmpfs -o size=1024m tmpfs /mnt/ram

用 Wget 的递归方式下载整个网站
wget --random-wait -r -p -e robots=off -U Mozilla www.example.com
参数解释:
- -random-wait 等待 0.5 到 1.5 秒的时间来进行下一次请求
-r 开启递归检索
-e robots=off 忽略 robots.txt
-U Mozilla 设置 User-Agent 头为 Mozilla
其它一些有用的参数:
- -limit-rate=20K 限制下载速度为 20K
-o logfile.txt 记录下载日志
-l 0 删除深度(默认为5)
--wait=1h 每下载一个文件后等待1小时

执行一个条命令但不保存到history中：$<space> command

显示当前目录中所有子目录的大小：$du -h --max-depth(深度)=1

显示消耗内存最多的 10 个运行中的进程,以内存使用量排序
ps aux | sort -nk +4 | tail

接受用户输入：
echo -n "enter your name"
read INTUT
#输出
echo $INPUT

监测路由走向：traceroute(linux)  tracert(windows)

同步时间：ntpdate pool.ntp.org

通过inode删除文件：
find . -inum INODE_NUM -exec rm -i {} \;
find . -inum INODE_NUM -delete

开机运行脚本配置：vim /etc/rc.d/rc.local

Linux下批量杀掉 包含某个关键字的 程序进程:
ps -ef|grep LOCAL=NO|grep -v grep|cut -c 9-15|xargs kill -9
下面将这条命令作一下简单说明：
管道符"|"用来隔开两个命令，管道符左边命令的输出会作为管道符右边命令的输入。
"ps -ef" 是linux里查看所有进程的命令。这时检索出的进程将作为下一条命令"grep LOCAL=NO"的输入。
"grep LOCAL=NO" 的输出结果是，所有含有关键字"LOCAL=NO"的进程。
"grep -v grep" 是在列出的进程中去除含有关键字"grep"的进程。
"cut -c 9-15" 是截取输入行的第9个字符到第15个字符，而这正好是进程号PID。
"xargs kill -9" 中的 xargs 命令是用来把前面命令的输出结果（PID）作为"kill -9"命令的参数，并执行该命令。"kill -9"会强行杀掉指定进程。
其它类似的情况，只需要修改"grep LOCAL=NO"中的关键字部分就可以了。

plink -N Username@ip -pw Password -D 127.0.0.1:7070

Username：为你的SSH账号
qiang.be：为服务器域名或IP
Password：为你的SSH密码

关闭笔记本触摸板 ：
sudo rmmod psmouse
要恢复也简单：
sudo modprobe psmouse

批量修改文件后缀名: rename 's/.c/.h/'  ./* (把当前目录下的后缀名为.c的文件更改为.h的文件)

查看服务器型号：dmidecode |grep Product
查看服务器操作系统：cat /etc/issue