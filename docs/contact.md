# Contact API Spec

## Create Contact

Endpoint : POST /api/contacts

Request Header :
- X-API-TOKEN : Token(mandatory)

Request Body :
```json
{
  "firstName" : " ",
  "lastName" : " ",
  "email" : " ",
  "phone" : " "
}
```

Response Body (Success) :

```json
{
  "data" : {
    "id" : " ",
    "firstName" : " ",
    "lastName" : " ",
    "email" : " ",
    "phone" : " "
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "error messages"
}
```


## Update Contact

Endpoint : PUT /api/contacts/{idContact}

Request Header :
- X-API-TOKEN : Token(mandatory)

Request Body :
```json
{
  "firstName" : " ",
  "lastName" : " ",
  "email" : " ",
  "phone" : " "
}
```

Response Body (Success) :

```json
{
  "data" : {
    "id" : " ",
    "firstName" : " ",
    "lastName" : " ",
    "email" : " ",
    "phone" : " "
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "error messages"
}
```

## Get Contact

Endpoint : GET /api/contacts/{idContact}

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body (Success) :

```json
{
  "data" : {
    "id" : " ",
    "firstName" : " ",
    "lastName" : " ",
    "email" : " ",
    "phone" : " "
  }
}
```

Response Body (Failed, 404) :

```json
{
  "errors" : "error messages"
}
```

## Search Contact

Endpoint : GET /api/contacts

Query Param : 
- name : String, contact firstname or lastname, using like query, optional
- phone :  String, contact phone, using like query, optional
- email : String, contact email, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 3

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body (Success) :

```json
{
  "data" : [
    {
      "id" : " ",
      "firstName" : " ",
      "lastName" : " ",
      "email" : " ",
      "phone" : " "
    }
  ],
  "paging" : {
    "currentPage" : 0,
    "totalPage" : 0,
    "size" : 0
  }
}
```

Response Body (Failed, Unauthorized) :

```json
{
  "errors" : "error messages"
}
```

## Remove Contact

Endpoint : DELETE /api/contacts/{idContact}

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body (Success) :

```json
{
  "data" : "ok"
}
```

Response Body (Failed, 404) :

```json
{
  "errors" : "error messages"
}
```
