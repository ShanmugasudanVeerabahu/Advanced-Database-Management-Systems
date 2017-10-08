<%-- 
    Document   : BrowseResults
    Created on : May 29, 2016, 2:46:34 PM
    Author     : shanmugasudan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Browse Movie Results</title>
    </head>
    <body>
            <style >
            body{background-color:greenyellow}
        </style>
        <table border='1'>  <tr>
            <th>Movie Id</th>
            <th>Movie Name</th>
            <th>Actor Name</th>
            <th>Actress Name</th>
            <th>Genre</th>
            <th>Year of Release</th>
            </tr>   
            <c:forEach var="movie" items="${requestScope.browseResults}">
            <tr>
             <td><c:out value="${movie.movieid}"></c:out></td>   
            <td><c:out value="${movie.title}"></c:out></td>
            <td><c:out value="${movie.actor}"></c:out></td>
            <td><c:out value="${movie.actress}"></c:out></td>
            <td><c:out value="${movie.genre}"></c:out></td>
            <td><c:out value="${movie.year}"></c:out></td>
            </tr>
        </c:forEach>
     </table>
    </body>
</html>
