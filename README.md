# ExploreWithMe

Приложение «Explore With Me» — это платформа для поиска, создания и управления событиями.

В этом репозитории два сервиса:

1. **Основной сервис "Explore With Me"** (`ewm-main-service`): предоставляет CRUD‑операции для событий, категорий, подборок и заявок на участие, с публичными, приватными и админскими эндпоинтами для платформы  событий.
2. **Сервис статистики** (`ewm-stats-service`): собирает и отдает статистику просмотров (hits) по URI, используется основным сервисом для записи и получения данных о запросах.

Они обмениваются данными по HTTP и могут запускаться локально или в продакшн‑среде.

## Конфигурация

В `application.yml` задаются:

```yaml
server:
  port: 8080            # 9090 для сервиса статистики
spring:
  datasource:
    url: jdbc:postgresql://...
    username: ...
    password: ...
stats:
  service-url: http://localhost:9090
```

## API

### Основной сервис

#### Публичные

- **GET /events**  
  Получение опубликованных событий с фильтрами (текст, категории, платность, диапазон дат, доступность, сортировка, пагинация). Возвращает `views` и `confirmedRequests`, и записывает hit в сервис статистики.

- **GET /events/{id}**  
  Детальная информация по событию (опубликовано). Возвращает `views` и `confirmedRequests`, и записывает hit.

- **GET /categories**  
- **GET /categories/{catId}**  
- **GET /compilations**  
- **GET /compilations/{compId}**

#### Приватные (пользователь)

- **POST /users/{userId}/events**  
- **GET /users/{userId}/events**  
- **GET /users/{userId}/events/{eventId}**  
- **PATCH /users/{userId}/events/{eventId}**  
- **GET /users/{userId}/requests**  
- **POST /users/{userId}/requests?eventId=**  
- **PATCH /users/{userId}/requests/{requestId}/cancel**

#### Админские

- **POST /admin/categories**, **DELETE /admin/categories/{catId}**, **PATCH /admin/categories/{catId}**  
- **POST /admin/compilations**, **DELETE /admin/compilations/{compId}**, **PATCH /admin/compilations/{compId}**  
- **GET /admin/events**, **PATCH /admin/events/{eventId}**  
- **GET /admin/users**, **POST /admin/users**, **DELETE /admin/users/{userId}**

Подробные схемы запросов и ответов в `ewm-main-service-spec.json`.

### Сервис статистики

- **POST /hit**  
  Запись информации о запросе:
  ```json
  {
    "app": "ewm-main-service",
    "uri": "/events/42",
    "ip": "203.0.113.4",
    "timestamp": "2025-04-28 15:22:10"
  }
  ```

- **GET /stats?start={start}&end={end}[&uris={uriList}][&unique={true|false}]**  
  Получение статистики просмотров по URI за период.

Подробности в `ewm-stats-service-spec.json`.
