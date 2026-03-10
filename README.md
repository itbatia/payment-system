# Payment System

### Структура

- `individuals-api` — оркестратор аутентификации

### Сборка

`./gradlew clean :individuals-api:bootJar`

### Запуск

`docker-compose up -d`

### Gradle commands

✅ Удалить папки `build/` во всех модулях:  
`./gradlew clean`

✅ Собрать JAR для указанного модуля (individuals-api):  
`/gradlew :individuals-api:bootJar`

✅ Полная пересборка модуля (очистка + сборка):  
`./gradlew clean :individuals-api:bootJar`

✅ Пересобрать Java-код на основе OpenAPI-спецификации (блок openApiGenerate):  
`./gradlew clean :individuals-api:openApiGenerate`

# Individuals-API

## Metrics and Observability

Сервис `individuals-api` реализует полноценную систему мониторинга на основе `Spring Boot Actuator` + `Micrometer` +
`Prometheus` + `Grafana`, что позволяет оперативно отслеживать:

- Состояние сервиса (HTTP-запросы, ошибки, задержки),
- Бизнес-метрики (логины, регистрации, успех/неудача),
- Время выполнения критических операций (вызов Keycloak).

🔧 **Архитектура**

| Компонент                        | Роль                                                    |
|:---------------------------------|:--------------------------------------------------------|
| `micrometer-registry-prometheus` | Экспорт метрик в формате временных рядов `Prometheus`   |
| `/actuator/prometheus`           | Эндпоинт для сбора метрик (включён в `application.yml`) | 
| `Prometheus` (Docker)            | Сбор и хранение метрик с интервалом 15 секунд           | 
| `Grafana` (Docker)               | Визуализация через дашборд `individuals-api-dashboard`  | 

📈 **Ключевые метрики**

1. Бизнес-метрики (кастомные, регистрируются в коде)

- `login_total{status="success"}` — успешные входы
- `login_total{status="fail"}` — неудачные входы
- `registration_total{status="success"}` — успешные регистрации
- `registration_total{status="fail"}` — неудачные регистрации
- `kc_login_latency_seconds_*` — латентность вызова Keycloak при логине
- `kc_registration_latency_seconds_*` — латентность вызова Keycloak при регистрации*

> *Реализовано через `MeterRegistry` и `Timer.Sample`, чтобы измерять точное время взаимодействия с `Keycloak`,
> а не весь HTTP-запрос.

2. Системные метрики (автоматически от `Spring Boot`)

- `http_server_requests_seconds_count{uri="/api/v1/auth/login", status="401"}` — количество ошибок аутентификации
- `http_server_requests_seconds_sum` / `_count` — среднее время обработки запросов

🛠 **Как это работает в коде**

```java
// Пример: измерение времени логина
@Override
public Mono<TokenResponse> login(String username, String password) {
    Timer.Sample sample = metricsService.startTimer();
    return keycloakClient.requestToken(username, password)
        .doOnSuccess(_ -> {
            metricsService.incrementSuccessfulLogin();
            metricsService.stopTimerOnSuccess(sample, Meter.KC_LOGIN_LATENCY);
        })
        .doOnError(_ -> {
            metricsService.incrementFailedLogin();
            metricsService.stopTimerOnError(sample, Meter.KC_LOGIN_LATENCY);
        });
}
```

Метрики регистрируются в `MetricsConfig` и используются в `MetricsService` для централизованного управления.

📊 **Дашборд в Grafana**

Дашборд [individuals-api](grafana/dashboards/individuals-api-dashboard.json) автоматически загружается при старте
контейнера `Grafana` (через `provisioning/dashboards/`). Он включает **4 ключевые панели**:

1. Общее количество логинов и регистраций (за последний час)  
   → Показывает абсолютное число событий (`increase(...)`) с цветовой индикацией:  
   ✅ Успешные — зелёный  
   ❌ Неудачные — красный

2. Доля успешных логинов и регистраций  
   → Вычисляется, как `успех / всего * 100%`, с порогами:  
   < 90% → 🔴 красный  
   ≥ 90% → 🟢 зелёный

3. Средняя латентность Keycloak-вызовов  
   → Сравнение `kc_login_latency` и `kc_registration_latency` (в секундах)  
   → Использует `rate(sum)/rate(count)` с защитой от деления на ноль.

4. Сравнение: HTTP vs Keycloak latency  
   → Показывает, сколько времени тратится на сам сервис (`http_server_requests`) и сколько — на вызов Keycloak (
   `kc_*_latency`).  
   → Помогает выявить узкие места (например, если Keycloak медленный, а HTTP-обработка быстрая).

> 💡 Все панели используют фиксированный `UID` источника данных (`PROMETHEUS_DS`), поэтому дашборд корректно
> импортируется в любую `Grafana` с такой же `provisioning`-конфигурацией.

🖼 **Примеры визуализации**

| Панель №  | Скриншот                                           |
|:---------:|:---------------------------------------------------|
|   1 и 2   | ![](readme-sources/grafana-dashboard-screen-1.jpg) |
|   3 и 4   | ![](readme-sources/grafana-dashboard-screen-2.jpg) |

