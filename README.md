# Payment System

## Структура
- `individuals-api` — оркестратор аутентификации

## Сборка
`./gradlew clean :individuals-api:bootJar`

## Запуск
`docker-compose up`

## Gradle commands

✅ Удалить папки `build/` во всех модулях:  
`./gradlew clean`  

✅ Собрать JAR для указанного модуля (individuals-api):  
`/gradlew :individuals-api:bootJar`  

✅ Полная пересборка модуля (очистка + сборка):  
`./gradlew clean :individuals-api:bootJar`  

✅ Пересобрать Java-код на основе OpenAPI-спецификации (блок openApiGenerate):  
`./gradlew clean :individuals-api:openApiGenerate`
