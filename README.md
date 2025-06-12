# Lottery API

Простой REST API для управления лотерейными тиражами и билетами.

## Технологии

- Java 17
- Spring Boot 2.7.18
- Hibernate 5.6.15
- SQLite (встроенная БД)

  ## Структура проекта

```
src
├── controller
│   └── DrawController, TicketController
├── service
│   └── DrawService, TicketService
├── repository
│   └── DrawRepository, TicketRepository
├── model
│   └── Draw, Ticket
├── dto
│   └── DrawResultDTO
```

## Как запустить

1. Убедитесь, что установлен JDK 17+ и Gradle.
2. Клонируйте проект и перейдите в папку:

```
git clone <URL> && cd lottery-api
```

3. Запустите Spring Boot приложение:

```
./gradlew bootRun
```

4. Приложение будет доступно по адресу:

```
http://localhost:8080
```

## Поддерживаемые команды

### 1. Создать тираж

```
curl -X POST http://localhost:8080/draws
```

Ответ:
```json
{
  "id": 1,
  "status": "ACTIVE",
  "winningNumbers": null
}
```

### 2. Купить билет

```
curl -X POST http://localhost:8080/tickets \
  -H "Content-Type: application/json" \
  -d '{"numbers": [1, 5, 12, 27, 33]}'
```

Ответ:
```json
{
  "id": 3,
  "numbers": [1,5,12,27,33],
  "draw": {
    "id": 1,
    "status": "ACTIVE",
    "winningNumbers": []
  }
}
```

### 3. Закрыть тираж

```
curl -X POST http://localhost:8080/draws/1/close
```

Ответ:
```json
{
  "id": 1,
  "status": "CLOSED",
  "winningNumbers": [8, 15, 21, 27, 33]
}
```

### 4. Получить результаты тиража

```
curl http://localhost:8080/draws/1/results
```

Ответ:
```json
{
  "drawId": 1,
  "winningNumbers": [8, 15, 21, 27, 33],
  "tickets": [
    {
      "id": 1,
      "numbers": [1, 5, 12, 27, 33],
      "isWinner": false
    },
    {
      "id": 2,
      "numbers": [8, 15, 21, 27, 33],
      "isWinner": true
    }
  ]
}
```

## Правила и ограничения

### Билет:
- Должен содержать ровно 5 чисел
- Числа должны быть:
  - от 1 до 36
  - уникальны
- Каждый билет относится к одному активному тиражу

### Победитель:
- Билет выигрывает, если его числа полностью совпадают с выигрышной комбинацией (независимо от порядка)

## Примечание

- В каждый момент времени может быть только один активный тираж
- После закрытия тиража (`/draws/{id}/close`) он становится недоступен для покупки билетов

  "error": "Нужно выбрать ровно 5 чисел."
}
```


