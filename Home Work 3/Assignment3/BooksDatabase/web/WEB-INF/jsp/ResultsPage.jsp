<%-- 
    Document   : ResultsPage
    Created on : May 31, 2016, 1:44:16 AM
    Author     : shanmugasudan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Results Page</title>
    </head>
    <body>
        <h2>Hello dude there you go!</h2>
        <% 
                String taskValue = (String)request.getAttribute("counter");
                out.println(taskValue+" books added succesfully");
        %>
        <a href="welcome-to-books-database.htm">Click here to go to home page</a>
    </body>
</html>
