<%-- 
    Document   : MovieDetails
    Created on : May 30, 2016, 4:55:00 PM
    Author     : shanmugasudan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add a movie</title>
    </head>
    <body>
            <style >
            body{background-color:blueviolet}
        </style>
         <form action="movie-details-added.htm?action=addmovie" method="post">
          Movie Title  <input type="text" name="title" /><br/>
          Lead Actor <input type="text" name="actor" /><br/>
          Lead Actress<input type="text" name="actress" /><br/>
          Genre<input type="text" name="genre" /><br/>
          Year<input type="text" name="year" /><br/>
            <input type="submit" name=" Add movie"/><br/>
           
        </form>
    </body>
</html>
