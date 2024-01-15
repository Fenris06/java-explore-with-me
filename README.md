# java-explore-with-me
Сервис-афиша для размещения информации о предстоящих событиях. Сревис реализован на модульной архитектуре и состоит из двух основных модулей:
* **main-mvc** осноной сервис. В нем происходит основное взаимодействие с клиенской часть. Сервсис разбит по уровню допуска к данным:
    * **публичный** позволяет не зарегестрированным пользователям проматривать события и подборки событий.
    * **приватный** позволяет пользователям регистрироватся. Создовать события и подовать заявку на участин в событиях. После окончания события  приневшие в нем участие пользователи могут оставить комментарии о нем.
    * **административный** предназначен для модерации администраторами созданых пользователями событий и с дальшей регистрации в базе данных или его удалением.
* **stats-mvc** сервис сбора статистики. Он разбит на три модуля:
    * **stats-client** клиет для сбора статистики. Он ижектится в основной сервис и обеспечивает сбор данных из основного сервиса в сервис статистики.
    * **stats-dto** модуль Dto классов для сервиса статистики stats-service и клента stats-client.
    * **stats-service** сервис статистики. Он позволяет сохранить статистику просморов позьзователями эвентов.
# main-mvc database diagram
![database diagram](https://github.com/Fenris06/java-explore-with-me/blob/main/mainmvc%20-%20public.png)
https://github.com/Fenris06/java-explore-with-me/pull/5
# stats-service database diagram
![database diagram](https://github.com/Fenris06/java-explore-with-me/blob/main/statsmvc%20-%20public.png)
