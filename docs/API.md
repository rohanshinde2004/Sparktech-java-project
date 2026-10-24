# API reference

Base URL: `http://localhost:8080/api/v1`

| Method | Path | Description | Success |
|---|---|---|---|
| GET | `/employees` | List employees. Optional `?department=HR` | 200 |
| GET | `/employees/{id}` | Get one employee | 200 |
| POST | `/employees` | Create an employee | 201 + `Location` header |
| PUT | `/employees/{id}` | Replace an employee | 200 |
| DELETE | `/employees/{id}` | Delete an employee | 204 |

Request body for POST and PUT:

```json
{
  "name": "Rohan",
  "email": "rohan@sparktech.com",
  "department": "Engineering",
  "salary": 85000
}
```

Errors return:

```json
{
  "timestamp": "2026-10-24T10:15:30Z",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id 99"
}
```

| Status | When |
|---|---|
| 400 | Validation failed (blank name, bad email, salary not positive) |
| 404 | Employee id does not exist |
| 409 | Email already used by another employee |
