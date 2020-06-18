
[![Build Status](https://travis-ci.org/AlexBezsh/eatingpoll.svg?branch=master)](https://travis-ci.org/AlexBezsh/eatingpoll)

# Eating Poll
### The web application was developed in accordance with the task:
Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind;
    - If it is after 11:00 then it is too late, vote can't be changed _(in application you can change voting end time in resources/app.properties)_.
    
Each restaurant provides new menu each day.

### Programs and technologies used:
Spring MVC, Spring Data JPA + Hibernate, Maven, Tomcat 9, tests - JUnit, database - HSQLDB, JDK version - 11. User interface (created for data display only) - JSP, JSTL, CSS. IDE - IntelliJ IDEA.
