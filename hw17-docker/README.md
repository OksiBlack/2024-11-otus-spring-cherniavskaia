# Обернуть приложение в docker-контейнер

## Цель: деплоить приложение в современном DevOps-стеке
## Результат: обёртка приложения в Docker


1. Обернуть приложение в docker-контейнер. Dockerfile принято располагать в корне репозитория. В image должна попадать JAR-приложения. Сборка в контейнере рекомендуется, но не обязательна.

2. БД в собственный контейнер оборачивать не нужно (если только Вы не используете кастомные плагины)

3. Настроить связь между контейнерами, с помощью docker-compose

4. Опционально: сделать это в локальном кубе.

5. Приложение желательно реализовать с помощью всех Best Practices Docker (логгирование в stdout и т.д.)

## Users and passwords:

1) admin/nimda
   role: ADMIN, EDITOR
2) bilbo/bilbo
   role: READER
3) aragorn/aragorn
   roles: EDITOR


Все rest контроллеры (API в endpoints) защищены с помощью Keycloak authorization server,
spring resource server, используются jwt токены.

1) client credentials token
   curl --location 'http://localhost:58080/realms/bookstore/protocol/openid-connect/token' \
   --header 'Content-Type: application/x-www-form-urlencoded' \
   --data-urlencode 'client_id=bookstore-client' \
   --data-urlencode 'client_secret=bookstore-client-secret' \
   --data-urlencode 'grant_type=client_credentials'
2) direct access grant

curl --location 'http://localhost:58080/realms/bookstore/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'X-API-KEY: ••••••' \
--header 'Cookie: JSESSIONID=A656BA251224B2CFB751780E7B0065BC' \
--data-urlencode 'client_id=bookstore-public-client' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=nimda'