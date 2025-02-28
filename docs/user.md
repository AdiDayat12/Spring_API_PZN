# User API Spec

## Register User
Endpoint : POST /api/users

Request Body :
 ```json
{
  "username" : " ",
  "password" : " ",
  "name" : " "
}
```

Response Body (Success)
 ```json
{
  "data" : "OK"
}
```
Response Body (Failed)
 ```json
{
  "error" : "error messages"
}
```
## Login User
Endpoint : POST /api/auth/login

Request Body :
 ```json
{
  "username" : " ",
  "password" : " "
}
```

Response Body (Success)
 ```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 23123213 // millisecond
  }
}
```
Response Body (Failed)
 ```json
{
  "error" : "error messages"
}
```

## Get User
Endpoint : GET /api/users/current

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body (Success)
 ```json
{
  "data" : {
    "username" : " ",
    "name" : " "
  }
}
```
Response Body (Failed)
 ```json
{
  "error" : "unauthorized"
}
```
## Update User
Endpoint : PATCH /api/users/current

Request Header :
- X-API-TOKEN : Token(mandatory)
Request Body :
 ```json
{
  "name" : "new name",
  "password" : "new pass"
}
```

Response Body (Success)
 ```json
{
  "data" : {
    "username" : " ",
    "name" : " "
  }
}
```
Response Body (Failed)
 ```json
{
  "error" : "error messages"
}
```


## Logout User
Endpoint : DELETE /api/auth/login

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body (Success)
 ```json
{
  "data" : "OK"
}
```