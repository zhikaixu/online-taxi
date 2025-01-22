# nacos
/Users/zhikaixu/Desktop/nacos2.0.3/bin/shutdown.sh
/Users/zhikaixu/Desktop/nacos2.0.3/bin/startup.sh -m standalone

# single redis
redis-cli -p 6379 shutdown
redis-server /usr/local/etc/redis.conf &

# redis sentinel
#redis-cli -p 6379 shutdown
#redis-cli -p 6380 shutdown
#redis-cli -p 6381 shutdown
#redis-cli -p 26379 shutdown
#redis-cli -p 26380 shutdown
#redis-cli -p 26381 shutdown
#redis-server /usr/local/etc/redis-6379-master/redis-6379.conf &
#redis-server /usr/local/etc/redis-6380-slave/redis-6380.conf &
#redis-server /usr/local/etc/redis-6381-slave/redis-6381.conf &
#redis-server /usr/local/etc/redis-6379-master-sentinel/redis-sentinel-6379.conf --sentinel &
#redis-server /usr/local/etc/redis-6380-slave-sentinel/redis-sentinel-6380.conf --sentinel &
#redis-server /usr/local/etc/redis-6381-slave-sentinel/redis-sentinel-6381.conf --sentinel &

#redis cluster
#redis-cli -p 30001 shutdown
#redis-cli -p 30002 shutdown
#redis-cli -p 30003 shutdown
#redis-cli -p 30004 shutdown
#redis-cli -p 30005 shutdown
#redis-cli -p 30006 shutdown
#
#cd /usr/local/etc/redis-cluster/node1
#redis-server /usr/local/etc/redis-cluster/node1/node1.conf &
#cd /usr/local/etc/redis-cluster/node2
#redis-server /usr/local/etc/redis-cluster/node2/node2.conf &
#cd /usr/local/etc/redis-cluster/node3
#redis-server /usr/local/etc/redis-cluster/node3/node3.conf &
#cd /usr/local/etc/redis-cluster/node4
#redis-server /usr/local/etc/redis-cluster/node4/node4.conf &
#cd /usr/local/etc/redis-cluster/node5
#redis-server /usr/local/etc/redis-cluster/node5/node5.conf &
#cd /usr/local/etc/redis-cluster/node6
#redis-server /usr/local/etc/redis-cluster/node6/node6.conf &
#cd /usr/local/etc/redis-cluster
#redis-cli --cluster create 127.0.0.1:30001 127.0.0.1:30002 127.0.0.1:30003 127.0.0.1:30004 127.0.0.1:30005 127.0.0.1:30006 --cluster-replicas 1

# redlock
#redis-cli -p 6379 shutdown
#redis-cli -p 6380 shutdown
#redis-cli -p 6381 shutdown
#redis-cli -p 6382 shutdown
#redis-cli -p 6383 shutdown
#
#cd /usr/local/etc/redis-redlock/redis-6379
#redis-server /usr/local/etc/redis-redlock/redis-6379/redis-6379.conf &
#cd /usr/local/etc/redis-redlock/redis-6380
#redis-server /usr/local/etc/redis-redlock/redis-6380/redis-6380.conf &
#cd /usr/local/etc/redis-redlock/redis-6381
#redis-server /usr/local/etc/redis-redlock/redis-6381/redis-6381.conf &
#cd /usr/local/etc/redis-redlock/redis-6382
#redis-server /usr/local/etc/redis-redlock/redis-6382/redis-6382.conf &
#cd /usr/local/etc/redis-redlock/redis-6383
#redis-server /usr/local/etc/redis-redlock/redis-6383/redis-6383.conf &

# ngrok
#ngrok http http://localhost:9001

#zookeeper single
/Users/zhikaixu/Desktop/apache-zookeeper-3.8.4-bin/bin/zkServer.sh start
#/Users/zhikaixu/Desktop/apache-zookeeper-3.8.4-bin/bin/zkCli.sh -server 127.0.0.1:2181

#zookeeper cluster
/Users/zhikaixu/Desktop/zookeeper/zookeeper1/apache-zookeeper-3.8.4-bin/bin/zkServer.sh start
/Users/zhikaixu/Desktop/zookeeper/zookeeper2/apache-zookeeper-3.8.4-bin/bin/zkServer.sh start
/Users/zhikaixu/Desktop/zookeeper/zookeeper3/apache-zookeeper-3.8.4-bin/bin/zkServer.sh start