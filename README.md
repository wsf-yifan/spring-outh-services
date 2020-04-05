# spring-outh-services
demo using spring outh 2.0 server and client.

#oauth server demo

curl -X POST --user clientapp:123456 http://localhost:8081/oauth/token -H "content-type: application/x-www-form-urlencoded" -d "code=f5AmTx&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8082%2Flogin&scope=read_profile_info"

这个有问题，跑的有问题
