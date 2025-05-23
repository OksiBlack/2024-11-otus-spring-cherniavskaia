# Переписать приложение для хранения книг на ORM

# Цель: полноценно работать с JPA + Hibernate для подключения к реляционным БД посредством ORM-фреймворка

Результат: Высокоуровневое приложение с JPA-маппингом сущностей


Описание/Пошаговая инструкция выполнения домашнего задания:

Домашнее задание выполняется переписыванием предыдущего на JPA.


Требования:

1. Использовать JPA, Hibernate только в качестве JPA-провайдера

2. Spring Data пока использовать нельзя

3. Загрузка связей сущностей не должна приводить к большому количеству запросов к БД или избыточному по объему набору данных (проблема N+1 и проблема произведения таблиц)

4. Добавить сущность "комментария к книге", реализовать CRUD для новой сущности. Получение всех комментариев делать не нужно. Только конкретного комментария по id и всех комментариев по конкретной книге по ее id

5. DDL через Hibernate должно быть отключено

6. LAZY-связи не должны присутствовать в equals/hashCode/toString. В т.ч. за счет @Data

7. Аннотация @Transactional должна присутствовать только на методах сервиса

8. Покрыть репозитории тестами, используя H2 базу данных и @DataJpaTest

9. Написать интеграционные тесты сервисов книг и комментариев, которые будут проверять работу с БД. Транзакционность в этих тестах должна быть отклкючена, чтобы не влияла на транзакции в сервисах. Проверить, что доступ к связям, которые используются снаружи серивсов не вызывают LazyInitialzationException. Не забыть учесть кэширование контекста в тестах

9. Добавить в решение тесты из заготовки. Для приема работы тесты должны проходить
