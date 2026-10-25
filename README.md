# Интеграционное тестирование (Testcontainers)

Spring Boot приложение из задачи «Conditional приложение»: по `GET /profile` возвращает
`Current profile is dev` или `Current profile is production` в зависимости от
параметра `netology.profile.dev`. Интеграционный тест поднимает два Docker-образа
(`devapp` и `prodapp`) через Testcontainers и проверяет ответы.

Стек: Java 17, Spring Boot 3.5, Maven, Testcontainers 1.21.4.

## Как запустить

Нужен запущенный Docker (Docker Desktop).

### 1. Образ devapp (порт 8080, профиль dev)

`src/main/resources/application.properties`:
```properties
server.port=8080
netology.profile.dev=true
```
`Dockerfile`: `EXPOSE 8080`

```bash
mvn clean package -DskipTests
docker build -t devapp .
```

### 2. Образ prodapp (порт 8081, профиль prod)

`src/main/resources/application.properties`:
```properties
server.port=8081
netology.profile.dev=false
```
`Dockerfile`: `EXPOSE 8081`

```bash
mvn clean package -DskipTests
docker build -t prodapp .
```

### 3. Тесты

```bash
mvn test
```

`-DskipTests` при сборке нужен потому, что тест обращается к образам `devapp`/`prodapp`,
которых до первой сборки ещё нет.

## Отличия от шаблона задания

- Базовый образ `eclipse-temurin:17-jre-alpine` вместо `openjdk:8-jdk-alpine`:
  Spring Boot 3 требует Java 17, а образы `openjdk` устарели.
- Testcontainers 1.21.4 вместо 1.15.1: старые версии не работают с современным Docker
  (ошибка `client version ... is too old`).
- Сборщик Maven, поэтому jar лежит в `target/`.
