
[![Build Status](https://travis-ci.org/AlexBezsh/eatingpoll.svg?branch=master)](https://travis-ci.org/AlexBezsh/eatingpoll)

# Eating Poll
### Веб-приложение реализовано с минимальным графическим интерфейсом и функционалом в соответствии с заданием:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind;
    - If it is after 11:00 then it is too late, vote can't be changed _(для тестирования приложения в BasicProfilesController-e переменная votingFinish выставлена на 23:59)_.
    
Each restaurant provides new menu each day.

### Использованные технологии: 
Spring MVC, Spring Data JPA, Hibernate, тесты - JUnit, база данных - HSQLDB, cборка приложения - Maven, запуск - Tomcat 9, графический интерфейс - JSP, JSTL. Среда разработки - IntelliJ IDEA.
