# Структура базы данных

## Таблицы

### `client`
| Поле | Тип | Описание | Ограничения |
|----------|----------|----------|----------|
| id   | BIGINT  |   Уникальный идентификатор | PRIMARY KEY, AUTO_INCREMENT |
| name    | VARCHAR(255)  | Имя клиента  | |
| email    | VARCHAR(255)  | Электронная почта | |
| phone    | VARCHAR(255)  | Номер телефона | |

### `session_hour`
| Поле | Тип | Описание | Ограничения |
|----------|----------|----------|----------|
| id   | BIGINT  |   Уникальный идентификатор | PRIMARY KEY, AUTO_INCREMENT |
| date_time    | timestamp  | Дата и время сеанса  | |
### `reservation`
| Поле | Тип | Описание | Ограничения |
|----------|----------|----------|----------|
| id   | BIGINT  |   Уникальный идентификатор | PRIMARY KEY, AUTO_INCREMENT |
| client_id    | BIGINT  | Внешний ключ к client  | |
| session_hour_id    | BIGINT  | Внешний ключ к session_hour | |
## Связи
- **`client_id`** ссылается на **`id`** в таблице **`client`** (Многие к одному)
- **`session_hour_id`** ссылается на **`id`** в таблице **`session_hour`** (Многие к одному)

