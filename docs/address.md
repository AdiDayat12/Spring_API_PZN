
# Address API Spec

## Create Address
**Endpoint**: `POST /api/contacts/{idContact}/addresses`

Request Header :
- `X-API-TOKEN` : Token(mandatory)

**Request Body**:
```json
{
  "street": " ", 
  "city": " ",    
  "province": " ",        
  "country": " ",
  "postalCode" : " "
}
```

**Response Body (Success)**:
```json
{
  "data": {
    "id": "address-id",
    "street": "123 Main St",
    "city": "Anytown",
    "province": "CA",
    "country": "USA",
    "postalCode": "12345"
  }
}
```

**Response Body (Failed)**:
```json
{
  "error": "error messages"
}
```

---

## Update Address
**Endpoint**: `PUT /api/contacts/{idContact}/addresses`

**Request Header**:
- `X-API-TOKEN`: Token (mandatory)

**Request Body**:
```json
{
  "street": " ", 
  "city": " ",    
  "province": " ",        
  "country": " ",
  "postalCode" : " "
}
```

**Response Body (Success)**:
```json
{
  "data": {
    "id": "address-id",
    "street": "123 Main St",
    "city": "Anytown",
    "province": "CA",
    "country": "USA",
    "postalCode": "12345"
  }
}
```

**Response Body (Failed)**:
```json
{
  "error": "error messages"
}
```

---

## Get Address
**Endpoint**: `GET /api/contacts/{idContact}/addresses/{idAddress}`

**Request Header**:
- `X-API-TOKEN`: Token (mandatory)

**Response Body (Success)**:
```json
{
  "data": {
    "id": "address-id",
    "street": "123 Main St",
    "city": "Anytown",
    "province": "CA",
    "country": "USA",
    "postalCode": "12345"
  }
}
```


**Response Body (Failed)**:
```json
{
  "error": "not found"
}
```

---

## Remove Address
**Endpoint**: `DELETE /api/contacts/{idContact}/addresses/{idAddress}`

**Request Header**:
- `X-API-TOKEN`: Token (mandatory)

**Response Body (Success)**:
```json
{
  "data": "OK"
}
```

**Response Body (Failed)**:
```json
{
  "error": "not found"
}
```

---

## List Addresses
**Endpoint**: `GET /api/contacts/{idContact}/addresses`

**Request Header**:
- `X-API-TOKEN`: Token (mandatory)

**Response Body (Success)**:
```json
{
  "data": [
    {
      "id": "address-id",
      "street": "123 Main St",
      "city": "Anytown",
      "province": "CA",
      "country": "USA",
      "postalCode": "12345"
    },
    {
      "id": "address-id",
      "street": "123 Main St",
      "city": "Anytown",
      "province": "CA",
      "country": "USA",
      "postalCode": "12345"
    }
  ]
}
```

**Response Body (Failed)**:
```json
{
  "error": "contact no found"
}
```
