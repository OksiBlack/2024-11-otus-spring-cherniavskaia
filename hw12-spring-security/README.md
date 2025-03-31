В CRUD Web-приложение добавить механизм аутентификации

# Цель: защитить Web-приложение аутентификацией и простой авторизацией
Результат: приложение с использованием Spring Security


## Задание выполняется на основе нереактивного приложения Spring MVC!


1. Добавить в приложение новую сущность - пользователь. Не обязательно реализовывать методы по созданию пользователей - допустимо добавить пользователей только через БД-скрипты.

2. В существующее CRUD-приложение добавить механизм Form-based аутентификации.

3. UserDetailsService реализовать самостоятельно.

4. Авторизация на всех страницах - для всех аутентифицированных. Форма логина - доступна для всех.

5. Написать тесты контроллеров, которые проверяют, что все необходимые ресурсы действительно защищены.


## Users and passwords:

1) admin/nimda
role: ADMIN, EDITOR
2) bilbo/bilbo
role: READER
3) aragorn/aragorn
roles: EDITOR
4) gandalf/gandalf
roles: READER, EDITOR



Все rest контроллеры (API в endpoints) защищены с помощью Keycloak, resource server, используются jwt токены.
Run with keycloak profile active.

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