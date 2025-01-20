# nacos
/Users/zhikaixu/Desktop/nacos2.0.3/bin/shutdown.sh
/Users/zhikaixu/Desktop/nacos2.0.3/bin/startup.sh -m standalone

# redis
redis-cli -p 6379 shutdown
redis-cli -p 6380 shutdown
redis-cli -p 6381 shutdown
redis-server /usr/local/etc/redis-6379-master/redis-6379.conf &
redis-server /usr/local/etc/redis-6380-slave/redis-6380.conf &
redis-server /usr/local/etc/redis-6381-slave/redis-6381.conf &

# ngrok
ngrok http http://localhost:9001