# spring-outh-services
demo using spring outh 2.0 server and client.

#oauth server demo

curl -X POST --user clientapp:123456 http://localhost:8081/oauth/token -H "content-type: application/x-www-form-urlencoded" -d "code=f5AmTx&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8082%2Flogin&scope=read_profile_info"

这个有问题，跑的有问题

## 启动OAuthServerDemo后，可以使用以下命令进行测试：
###获取token
    curl -i -X POST -d "username=wsf&password=123&grant_type=password&client_id=djdemoapi&client_secret=123456" http://localhost:8081/auth/oauth/token
###判断token
    curl localhost:8081/auth/oauth/check_token/?token=41f8de6d-a21b-41dc-9f34-7648062839db
###刷新token
    curl -i -X POST -d "grant_type=refresh_token&client_id=djdemoapi&client_secret=123456&refresh_token=1e233acf-5349-4b7b-95ee-a2ad82d8e8e8" http://localhost:8081/auth/oauth/token