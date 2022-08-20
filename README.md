# Computer Store - diploma web project on JAVA and SPRING
# Online магазин за продажба на компютри и компютърни компоненти

## I. User functionality of the system
### Инициализация на първоначални данни
* Инициализация на първоначални данни - чрез InitialazbleService интерфейси съгласно Open-Close принципа - в init/AppInit.java class в @PostConstruct анотирания метод.
* Инициализация от data.sql - възможна, но Hibernatе не му харесва (виж края на този Readme файл)

### ADMIN панел функционалност - **само от ADMIN**
* Служител е user с роля/роли EMPLOYEE_PURCHASES или EMPLOYEE_SALES
* ADMIN може да добавя нов служител EMPLOYEE_PURCHASES или EMPLOYEE_SALES
* Сменяне на ролите нa потребителите EMPLOYEE_PURCHASES и EMPLOYEE_SALES
* Може да има само един ADMIN, и той има всичките четири роли
* Функционалност за смяна кой служител да е единствения ADMIN - при успешна смяна на admin, то системата се logout-ва автоматично и е нужен нов login
* Всеки служител (купувач, продавач) трябва да има поне 2 роли - обикновено CUSTOMER + EMPLOYEE_PURCHASES/EMPLOYEE_SALES
* Възможност за статистика:
  * за брой направени поръчки, брой артикули, на каква обща стойност, и колко печалба.
  * за брой http заявки на анонимен и на логнат потребител.

### Settings панел функционалност - **All users**
* Функционалност всеки потребител да си сменя паролата
* при ADMIN се достъпва от ADMIN панела
* при успешна смяна, то системата се logout-ва автоматично, и е нужен нов login

### Качване на оферти за компютри в сайта - **само от ADMIN и от EMPLOYEE_PURCHASES**
* Възможност за добавяне, редактиране и изтриване на компютърни компоненти.
* Ограничения: 
  * пазим уникален model на всеки item
  * при добавяне на нов item, ако моделът му съществува, то уведомяваме потребителя, че той може да зареди формата за Update/Едит и само да го update-не артикула с нови данни.
  * приемаме, че при update/edit реално ако залагаме нови покупни и продажни цени, то тези цени са за всички бройки артикули от този модел. Променяме (добавяме) главно количество, но не само.
  * once a customer puts an item in his/her basket, it is not possible to delete the item from the database
  * Може да има и редакция на снимка. Всяка качена снимка изтрива предишната снимка в Cloudinary, но Update-ва реда от PictireEntity (таблицата pictures) с новия public_id и url.

### Избор на продукти в Basket кошницата - само за логнати клиенти - **всеки потребител, който има и роля CUSTOMER**
* Всяка кошница има статус или CLOSED или OPEN
* Всеки регистриран потребител има само една единствена кошница под един и същи номер - при регистрация, то се добавя кошницата автоматично
* Възможност за поръчване и слагане в кошница/страница на продукти.
* При слагане на продукт от даден вид в кошница, то залагаме първоначална 1 бройка количество от този item
* При добавяне на количество продукти в кошницата, намаляме реалното налично количество продукти
* Възможност за изтриване на част от продуктите от кошницата - връщаме съответното количество обратно към наличното
* Потвърждаване на продуктите в кошницата - изтриване на кошницата и помощните таблици за тази кошница и създаваме на реална поръчката.
* Даване на номер реалната поръчка - чрез UUID генератора
* Scheduled event - за логнати потребители - периодично минаване (на всеки 5 минути) за изтриване на кошници със статус OPEN (направени преди повече от 20 минути и все още незатворени) - при изтриване връщаме количеството на всеки Item обратно към наличното в магазина.

### Реалната поръчка
* При реална поръчка, клиента въвежда данни за **адрес на доставка**, **телефонен номер** и **бележки** - отделна таблица client_orders_extra_info, която е свързана и с таблица orders и с таблица users!
* Визуализиране на поръчките (сортирани по датачас - последна поръчка стои най-отгоре в списъка) - всички за даден user или абсолютно всички поръчки за user-и, които имат SALES и ADMIN роли
* Промяна на статус поръчка:
  * След като клиент потвърди поръчка, то тя се записва в базата данни на статус CONFIRMED_BY_CUSTOMER.
  * Продавача проверява физически дали ги има артикулите, пакетира доставката, сменя статуса на поръчката на CONFIRMED_BY_STORE, вика куриер – само от EMPLOYEE_SALES и от ADMIN.
  * След като пратката/поръчката е получена от клиента, продавача получава известие от куриера и променя ръчно статуса на поръчката на DELIVERED – само от EMPLOYEE_SALES и от ADMIN.
* Статус поръчка – проверка дали дадена поръчка е на статус CONFIRMED_BY_CUSTOMER, CONFIRMED_BY_STORE, DELIVERED. – от CUSTOMER, EMPLOYEE_SALES, ADMIN - за момента само за логнати потребители спрямо тяхното ниво на достъп.

### Проследимост на общия брой поръчки
* В горния ляв ъгъл се показва общия брой поръчки до момента
* Demo using Spring event when an order is created - we catch the Spring custom event by Event listener - we increase the total numbers of orders. We also prepare for sending e-mail to the user and for adding bonus points to the user.

### Search
* Имплементиран search за дисплейване/намиране на поръчки чрез REST и Fetch API - работи само за логнати потребители и съответно достъпа е както следва:
Всеки ADMIN и EMPLOYEE_SALES имат достъп до промяна на статуса на поръчката - за всички поръчки.
Всеки потребител CUSTOMER или потребител PURCHASE & CUSTOMER има само стандартната информация - и то само за своите си поръчки, а не за всички поръчки.
Работи сортиран (by date DESCENDING) като последно добавена поръчка излиза първа в списъка. 
* //TODO - глобална търсачка в commons.html за всички типове продукти - по тип на продукта + име на модел/цена по-голяма от... - само чрез Thymeleaf





## II. SoftUni Requirements done
### Използвани структури от данни
* Sets - за ролите
* Lists - като връщаме и Unmodifiable когато е неoбходимо

### Преубразуване на данни
* Чрез ModelMapper
* Чрез MapStruct - плюс един деклариран default mapping method (about the photo)
* Ръчно - чрез constructor и setters

### Validating input data
* client-side via HTML
* server-side via @Valid annotation

### Три custom annotation валидации
* За това дали username или e-mail вече съществуват в базата данни
* За това дали паролите се еднакви
* За това дали покупна и продажна цена са валидни цели и/или дробни числа

### Spring data, Hibernate and database
* using MySQL
* implemented Single Table inheritance for all the products
* all tables interconnected one another relationally
* userId е реално винаги и basketId
* към момента заложената релационна връзка е да има повече от една кошница за user, но ние реално ползваме само една единствена кошница за user 
* Особеност при basket и order - имаме един кръг от четири таблици свързани релационно и можем да подходим от две посоки за каквото и да е
![img_1.png](img_1.png)

### Cloudinary
* За качване/смяна на снимка за всеки продукт

### Interceptors
* report for http request from anonymous and authenticated user
* I18N – change language - just a demo for the header part and some title/paragraphs of pages - from English to Bulgarian and vice versa
* //ТODO - YESS - колко потребителя има активни в момента - ще го дисплейваме на commons (NOOO!!! - how many people visited the website)

### Generating HTML
* with Thymeleaf engine secured 
* and rest fetch API inside html for some pages

### Responsive Web Page Design 
* using Bootstrap

### Spring security
* only via the security chain! - not using @PreAuthorize on method level
* secured user role management
* secured password change
* secured admin user change
* secured adding new employee of Computer store
* secured (MVC @Controller secured and also @RestController JSON secured - both via @AuthenticationPrincipal) - adding/removing items or changing quantities of the basket or just viewing the basket, confirming basket into the final order, final order details, view final orders and change status of a final order.

### Error Handling 
1. Spring security default re-direct to login page for not allowed operations/wrong urls - from anonymous users
2. Adding a custom ComputerStoreErrorHandler
* disabling the default Spring whitelable error.html page
* adding a custom ComputerStoreErrorHandler implementing the markup interface ErrorController - custom error pages for 404 Not Found, 403 Forbidden and 500 Internal Server Error.
* when wrong url error-404.html displayed; when correct url but not authorized error-403.html displayed
* picture for the error pages 404, 403 and 500
3. More customs error handling experience with @ControllerAdvice
* using global application exception handling with @ControllerAdvice on all GET operations - connected with items, baskets, orders
* Exceptions for @ControllerAdvice for POST, PATCH, DELETE operations not needed as they are secured by the Spring security and CSFR (but I included them also for extra security)
4. //TODO - Nobody can see other baskets and/or the page confirming the basket into a final order - except his/her own basket - one more handler here to add
5. Only EMPLOYEE_SALES and ADMIN can see all the final orders. EMPLOYEE_PURCHASES and CUSTOMER can see only their own orders.

### Loading data with FETCH api in the Thymeleaf html
* Добавянето, изтриване и промяна количества на Item-s в кошницата чрез Rest и FETCH Api (jQuery and/or Vanilla JS)
* Извикване на диалогови прозорци на база response статуса от RestController-a:
  - за добавяне на item в кошницата (за успешно добавен item, за дублиран вече такъв item или за item който е с нула количество)
  - при изтриване на item от кошницата
  - при промяна количества на item в кошницата
* Дисплейване на всяка кошница - чрез Rest и Fetch Api (jQuery and JS)
* Demo with text inlining - with Vanilla JS - for authorizing when displaying the orders
* Дисплейване на поръчки, промяна статус на поръчка и търсене на поръчки - според ролите на user-а - чрез Rest и Fetch Api (jQuery and JS)
* Комбинирано търсене с промяна на статус - при едновременно зададен критерий за търсене и при смяна на статус поръчка, то се визуализират само поръчките отговарящи на търсения критерий (в повечето случай е само 1 поръчка), като по този начин на една и съща поръчка можем лесно без да търсим допълнително да й сменим на два пъти статуса
* Когато поръчка е на статус CONFIRMED_BY_CUSTOMER, то имаме опция да сменим статуса само на CONFIRMED_BY_STORE
* Когато поръчка е на статус CONFIRMED_BY_STORE, то имаме опция да сменим статуса само на DELIVERED

###	Scheduling jobs and Spring events
* Schedule a job - за логнати потребители - периодично минаване (на всеки 5 минути) за изтриване на кошници със статус OPEN (направени преди повече от 20 минути и все още незатворени) - при нулиране на кошницата, то връщаме количеството на всеки Item обратно към наличното в магазина.
* Using Spring event when an order is created - we catch the Spring custom event by Event listener - we increase the total numbers of orders. We also prepare for sending e-mail to the user and for adding bonus points to the user.
* Особеност при дисплейване на общия брой поръчки в горния ляв ъгъл:
    - използваме в commons.html следния thymeleaf израз:  ${#session.getAttribute('totalOrdersCount')
    - първоначалните автоматично създаваните поръчки не се хващат от нашия custom event listener (не ги регистрирам publish-вам listener-ите изрично при първоначалнотостартиране на приложението)
    - задаваме статична глобална променлива за пазене на общия брой поръчки като вземаме при стартиране бройката поръчки от базата данни
    - задаваме на http session cookie-то JSESSIONID атрибут "totalOrdersCount" - при първоначално логване на страница от url "/" със стойността на глобалното статично поле за брой поръчки
    - създаден custom event class OrderCreatedEvent
    - създаваме инстанция на OrderCreatedEvent и я публикуваме
    - създадени 3 класа с метод с анотация @EventListener(OrderCreatedEvent.class) - for increasing total orders number, and for e-mail sending to user and bonus points
    - event Listener-a за увеличаване на общия брой поръчки хваща събитието създадена поръчка, и увеличава статичната променлива с 1-ца
    - на предпоследния ред, и след като вече поръчката е създадена, на метода viewOrderWithItemsAndAddAddressConfirm от BasketAndOrderController класа, задаваме нова стойност на атрибута http cookie session JSESSIONID

### Search
* Търсене на поръчки - според ролите на user-а - чрез Rest и Fetch Api (jQuery and JS)
* //TODO - глобална търсачка в commons.html за всички типове продукти - по тип на продукта + име на модел/цена по-голяма от... - само чрез Thymeleaf

### Unit and integration testing
### Coverage so far - global lines 681 (32%), service logic lines 451 (58%), web layer controllers lines 127 (27%)
* Integration testing in the web controllers and in the services - with in-memory database HyperSQL database and/or H2 database
* Important notes before starting testing:
  - first, disable in the class AppSeedInit.java  the @PostConstrict annotated method beginInit()
  - second, copy the real CLOUDINARY_SECRET in the application.yml in the test section // or other option is to set Enviromental Variables for every test class manually
* For testing - do not use columnDefinition @Column(name = "more_info", columnDefinition = "TEXT") - (in the ItemEntity class for field moreInfo, I removed the columnDefinition so that the in-memory HyperSQL grammar is satisfied)
* Testing with BasketServiceTest.java  - test each method separately as I am using @Transactional to re-enable the Hibernate session




## III. General MORE TODOs
### SoftUni MUST-TODOs
* Implement one or more Advice (AOP).
* Host the application in a cloud environment.

### Other TODOs
* Накрая на представянето на проекта, може да добавя за демо, да се инициализират и 2 монитора от data.sql файла, но по принцип не му харесва на Hibernate след това. Сменяме от never на always, добавяме си 2 мониторa, a след това задаваме 'never' веднага преди ново пускане на системата
  sql:
  init:
  mode: never


* Можем да добавим и още артикули, и става сравнително лесно
Клавиатури и мишки
Хард диск / Hardisk / SSD disk
Видео карта/ Video card
Процесор / Processor
Дъно / Motherboard
Рам / Ram


* Подобие на чат (ако остане време)


* Pageable and sorted – to implement it. – лесно става в Java, но за да се display-не на html-a, то:
  - При client-side rendering трябва чрез JS да ги вземаме нещата.
  - При server-side rendering с обикновен контролер -  трябва в Thymeleaf модела да ги сложим
  - Чрез Page . content вземаме лист от елементите от текущия Page, previous enabled, next enabled, previousPage, nextPage


* Възможност за нелогнат потребител да си добавя продукти в кошница. За да ги поръча обаче трябва да се логне – след регистрация и логване, кошницата дали ще може да се запази.
* За нелогнати потребители - не е готово - периодично минаване (на всеки 60 минути) за изтриване на кошници със статус OPEN (направени преди повече от 30 минути и все още незатворени) - при изтриване връщаме количеството на всеки Item обратно към наличното в магазина.


* Възможност за статистика:
  * за брой клиенти и средна стойност в лева за една поръчка;


* При 20 000 артикула, то допълнителната информация може да я слагаме във вложени JSON-и