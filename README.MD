# Explore with me

## Архитектура

### Модуль infra

1. **discover-server**
    - Поднимает сервер Spring Cloud Eureka

2. **config-server**
    - Поднимает Spring Cloud Config Server
    - хранит конфиграции для всех сервисов

3. **gateway-server**
    - Поднимает Spring Cloud Gateway
    - имеет заранее настроенные роуты, для сервисов core

### Модуль stats

1. **stats-server**
    - Сервис по работе со статистикой

2. **stats-client**
    - feign client для отправки в сервис статистики

3. **stats-dto**
    - Описанные схемы, для взаимодействия с сервисом статистики

### Модуль core

1. **interaction-api**
    - Описанные схемы, для взаимодествия с сервисами core

2. **user-service**
    - Сервис для работы с пользователями

3. **event-service**
    - Сервис для работы с событиями

4. **request-service**
    - Сервис для работы с запросами, на включение пользователя, в участии конкретного события

5. **comment-service**
    - Сервис по работе с комментариями

## Взаимодействие сервисов

Сервисы взаимодействуют друг сдругом, по технологиям OpenFeign и Eureka

## API Сервисов

1. [Сервис событий](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json)
2. [Сервис статистики](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json)
