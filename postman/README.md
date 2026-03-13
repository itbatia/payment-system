## Демо

Для быстрого тестирования API используйте Postman-коллекцию:

📁 [Payment-system.postman_collection](/postman/Payment-system.postman_collection.json)

🔹 Примеры сценариев для демо:

### 📌 /auth/registration

---
1) ✅ **Успешная регистрация → 201 Created:**

Request body:
```json
{
  "email": "test@mail.ru",
  "password": "password",
  "confirm_password": "password"
}
```
Response body:
```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJRN2V1ci15dWJEYkdZcFhydl9WMmdzTjhkeVJubVVhdGdhWWhpZHIxdk5NIn0.eyJleHAiOjE3NzM0MzUzMDEsImlhdCI6MTc3MzQzNTAwMSwianRpIjoib25ydHJvOjJiNGY2NjllLTkxM2ItZmU5ZC1lNGJmLTlhYWY3ZWY0NzJjYyIsImlzcyI6Imh0dHA6Ly9rZXljbG9hazo4MDgwL3JlYWxtcy9pbmRpdmlkdWFscyIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI2YjE0OTVjYy01MjliLTRlZTUtYWRiNS03ZDJkZjY0MWJmOGUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJpbmRpdmlkdWFscy1hcGkiLCJzaWQiOiJaWHlZRnpkaG5lb0txcXV2SllxcUdkNUkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtaW5kaXZpZHVhbHMiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdEBtYWlsLnJ1IiwiZW1haWwiOiJ0ZXN0QG1haWwucnUifQ.PoaAQD_9eP-_Z6OCV9SFGQ6PKNLRO0pBcyqzajOdwrTLth6wnj6xTkc4-BH5_TFTzecTsOrJqgT1C1j2SPi9SqFQjgf9J7BOXWP2b9HLunZyU0p5pt65P5QAMnRgmpD_UQ6ikjj7PYPLk-r8bXpke2EOW-boG1i_Fu6jVNt4cgM2hDSv03jnltCtNbxvwq6i-osYypWqlGTLfPQFJa_wXuwmvZK25p3i5zfxD-_Q7Kv--XgedXjWUkMFioVYnvwYCUVTF9ZKOcQ8her-CtKU-sP0Bw2USF5BJUqNRsgXvSlSTcAImCZ_wQ9QF6Yg3fhT4eb5cm7OzzgQsPOrJ_JFpQ",
    "expires_in": 300,
    "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlZDFhZjhhMC00ZTcxLTQ5YTMtOGNhOS01OGFkOGQ4M2NiYjQifQ.eyJleHAiOjE3NzM0MzY4MDEsImlhdCI6MTc3MzQzNTAwMSwianRpIjoiZTdjYWI3MTEtOGE4Ny1iOTA0LWUxN2MtM2E5YmY5NTdlNjM4IiwiaXNzIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwiYXVkIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwic3ViIjoiNmIxNDk1Y2MtNTI5Yi00ZWU1LWFkYjUtN2QyZGY2NDFiZjhlIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImluZGl2aWR1YWxzLWFwaSIsInNpZCI6IlpYeVlGemRobmVvS3FxdXZKWXFxR2Q1SSIsInNjb3BlIjoiYWNyIHByb2ZpbGUgc2VydmljZV9hY2NvdW50IHJvbGVzIHdlYi1vcmlnaW5zIGVtYWlsIGJhc2ljIn0.UjrmZr4Ph2ODUTr4DvgjU68ncLXLz7qPschNGPWG4CPxqPGfMn5pd-oL5nwlp7h11mFVbR4ZXmt4vfbM-i0RPQ",
    "token_type": "Bearer"
}
```
---
2) ❌ **Повторная регистрация → 409 Conflict:**

Request body:
```json
{
  "email": "test@mail.ru",
  "password": "password",
  "confirm_password": "password"
}
```
Response body:
```json
{
  "error": "User exists with same email",
  "status": 409
}
```
---
3) ❌ **Пароли  не совпадают → 400 Bad request:**

Request body:
```json
{
  "email": "test@mail.ru",
  "password": "password-1",
  "confirm_password": "password-2"
}
```
Response body:
```json
{
  "error": "Password and confirmation do not match",
  "status": 400
}
```
---

4) ❌ **Недействительный адрес электронной почты → 400 Bad request:**

Request body:
```json
{
  "email": "test",
  "password": "password",
  "confirm_password": "password"
}
```
Response body:
```json
{
  "error": "Email must be a valid email address",
  "status": 400
}
```
---

### 📌 /auth/login

1) ✅ **Успешный логин → 200 OK:**

Request body:
```json
{
  "email": "test@mail.ru",
  "password": "password"
}
```
Response body:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJRN2V1ci15dWJEYkdZcFhydl9WMmdzTjhkeVJubVVhdGdhWWhpZHIxdk5NIn0.eyJleHAiOjE3NzM0MzYyOTAsImlhdCI6MTc3MzQzNTk5MCwianRpIjoib25ydHJvOjA3ZWY5YmU3LTQ3NDEtMDYxYy0yMGZiLWE0MTViZmE0YmU3OSIsImlzcyI6Imh0dHA6Ly9rZXljbG9hazo4MDgwL3JlYWxtcy9pbmRpdmlkdWFscyIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI2YjE0OTVjYy01MjliLTRlZTUtYWRiNS03ZDJkZjY0MWJmOGUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJpbmRpdmlkdWFscy1hcGkiLCJzaWQiOiJlWGtNd2JXTGhJdWRsUTZJTnM5NTczQUgiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtaW5kaXZpZHVhbHMiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdEBtYWlsLnJ1IiwiZW1haWwiOiJ0ZXN0QG1haWwucnUifQ.BrPR3vKWcKmZScQsKpZLsCdZHoylNGrDb3roJlwet-OhrD9gDV5V-5NGjWThqRPRaasQt2JGYbpTaPNqk3z9KKJgnHJ_AUS6E_o0rdyXrgCVPknffLbC1e2D2s0chrhtLiok4YR0NK186XExpUVD0_aq8gbr-g6Jyq4ZdxDkooqnY304rIlwKN0cY5-o5naCsU5nsXrXlRMQ4nko7KCAnpURgPXClGEjyECup0keY176mt7qTdwOH0ZyPI0UUHgICoKHDLaK0hkG0wqqWhBK1dfXV698_AIPq3bUY3n3vGMh0BEdtbOU1SobLeHy1hbjpGfPjT-W9sd_KyjfJmXq1w",
  "expires_in": 300,
  "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlZDFhZjhhMC00ZTcxLTQ5YTMtOGNhOS01OGFkOGQ4M2NiYjQifQ.eyJleHAiOjE3NzM0Mzc3OTAsImlhdCI6MTc3MzQzNTk5MCwianRpIjoiODRhY2FmNmUtNTYxYy05ZWRjLWJkNDMtY2NkNTdhYWJiMDhlIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwiYXVkIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwic3ViIjoiNmIxNDk1Y2MtNTI5Yi00ZWU1LWFkYjUtN2QyZGY2NDFiZjhlIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImluZGl2aWR1YWxzLWFwaSIsInNpZCI6ImVYa013YldMaEl1ZGxRNklOczk1NzNBSCIsInNjb3BlIjoiYWNyIHByb2ZpbGUgc2VydmljZV9hY2NvdW50IHJvbGVzIHdlYi1vcmlnaW5zIGVtYWlsIGJhc2ljIn0.wBGb3APvqL8AfUuKVRkvXCDKxJ57q6eAwEd2VqaCtJ_XPGq12hMEy3log6fN_gMuVyUGt0RYH5w1B7wxwbT0vA",
  "token_type": "Bearer"
}
```
---

2) ❌ **Неверный пароль → 401 Unauthorized:**

Request body:
```json
{
  "email": "test@mail.ru",
  "password": "password-1"
}
```
Response body:
```json
{
  "error": "Invalid user credentials",
  "status": 401
}
```
---

3) ❌ **Отсутствует обязательное поле → 400 Bad Request:**

Request body:
```json
{
  "email": "test@mail.ru"
}
```
Response body:
```json
{
  "error": "Password field is required",
  "status": 400
}
```
---

### 📌 /auth/refresh-token

1) ✅ **Успешное обновление → 200 OK:**

Request body:
```json
{
  "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjOGUwYTVhMS00MmYyLTRiOWMtODkyZi1lYTUzMzgyMTdkNTUifQ.eyJleHAiOjE3NzM0Mzg2NDEsImlhdCI6MTc3MzQzNjg0MSwianRpIjoiMTIxOWQ4MWItMzc3NC1iMjRjLWI1NmItYmMwNzNlYTlhNDViIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwiYXVkIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwic3ViIjoiMjU4OGFkNDktNTJiOS00ZjMxLWE2MTQtY2E3YjY5MTY1MmZiIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImluZGl2aWR1YWxzLWFwaSIsInNpZCI6IjRnTnFyVmRkczJKS3kwRHlsQk5HU19TQyIsInNjb3BlIjoiYWNyIHByb2ZpbGUgc2VydmljZV9hY2NvdW50IHJvbGVzIHdlYi1vcmlnaW5zIGVtYWlsIGJhc2ljIn0.NnzWXfPzaS9lhgHzV5V2XTa7qYMnW69nrtiz52yElNKG6nyV9FgRNqxX6fckWjOGG9cDz6PvdA-JzDIr3Qyixw"
}
```
Response body:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5VWU5eVQ4dG9xaU1YdmRfYm51aTFwSnNqaDRyM3o4aWlPTFNMaDhFZmQ4In0.eyJleHAiOjE3NzM0MzcxNTksImlhdCI6MTc3MzQzNjg1OSwianRpIjoib25ydHJ0OmRjMTIxYzQzLWE1YzEtMDQ1My01NzM2LWZjMjcxNjQ4ZjYzNSIsImlzcyI6Imh0dHA6Ly9rZXljbG9hazo4MDgwL3JlYWxtcy9pbmRpdmlkdWFscyIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIyNTg4YWQ0OS01MmI5LTRmMzEtYTYxNC1jYTdiNjkxNjUyZmIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJpbmRpdmlkdWFscy1hcGkiLCJzaWQiOiI0Z05xclZkZHMySkt5MER5bEJOR1NfU0MiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtaW5kaXZpZHVhbHMiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdEBtYWlsLnJ1IiwiZW1haWwiOiJ0ZXN0QG1haWwucnUifQ.aJElY3eajZooosiOB6vNVO_4TIQAqkgST78X8FOhr4Rz2KsV5eEz1qvXP1ckS2qp3Jhcw3Wc_qX7XGbNX3hl5CbSwD8yw3dihNtbpvuJ_EtyAjN9wgu1H7ko7wG-vrfkZCVRczYO28jbjXvW-MoLQ4v0Fv3TtBBPMb9gW9r06AJdNFxtYi_MYHBSsU3AK1AiV2GUGAtNVwpmyTyTq7TAmRtq5CWW5-ioO7nZeYRq3EJGeHU4iWxXcn65lpUqagBh5XVucHBeDAcKq5BNN5SvW4UKqmWSlC2d7Ujcxu7UEyiW9KDPEDM2BZUByavEwruaV9ujEpgPet2hyHtCQK-aLQ",
  "expires_in": 300,
  "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjOGUwYTVhMS00MmYyLTRiOWMtODkyZi1lYTUzMzgyMTdkNTUifQ.eyJleHAiOjE3NzM0Mzg2NTksImlhdCI6MTc3MzQzNjg1OSwianRpIjoiNmUyZjIyNzYtYTUwNy1iODMwLTMwMjMtOTQ5NWIwYjIxMGU5IiwiaXNzIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwiYXVkIjoiaHR0cDovL2tleWNsb2FrOjgwODAvcmVhbG1zL2luZGl2aWR1YWxzIiwic3ViIjoiMjU4OGFkNDktNTJiOS00ZjMxLWE2MTQtY2E3YjY5MTY1MmZiIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImluZGl2aWR1YWxzLWFwaSIsInNpZCI6IjRnTnFyVmRkczJKS3kwRHlsQk5HU19TQyIsInNjb3BlIjoiYWNyIHByb2ZpbGUgc2VydmljZV9hY2NvdW50IHJvbGVzIHdlYi1vcmlnaW5zIGVtYWlsIGJhc2ljIn0.HuOTG8i3K3vM8QikvY1HY1o1NuS7iYBMAFOL-n4-TtCHwG0Bm3jhev-5hVZ1VV2g8-sQq9dgj4OC88HaaXI0Wg",
  "token_type": "Bearer"
}
```
---

2) ❌ **Неверный токен → 400 Bad Request:**

Request body:
```json
{
  "refresh_token": "eyJhbGciOiJIUzU...invalid...91tkz4seuDbGXxQISJoZOVS-KQ"
}
```
Response body:
```json
{
  "error": "Invalid refresh token",
  "status": 400
}
```
---

3) ❌ **Отсутствует обязательное поле → 400 Bad Request:**

Request body:
```json
{}
```
Response body:
```json
{
  "error": "RefreshToken field is required",
  "status": 400
}
```
---

### 📌 /auth/me

1) ✅ **Успешное получение профиля пользователя → 200 OK:**

Header required:
```text
Authorization: Bearer <access_token>
```
Response body:
```json
{
  "created_at": "2026-03-13T21:20:41.64Z",
  "email": "test@mail.ru",
  "id": "2588ad49-52b9-4f31-a614-ca7b691652fb",
  "roles": []
}
```
---

2) ❌ **Неверный токен → 401 Unauthorized:**

Header required:
```text
Authorization: Bearer <invalid_access_token>
```

---
