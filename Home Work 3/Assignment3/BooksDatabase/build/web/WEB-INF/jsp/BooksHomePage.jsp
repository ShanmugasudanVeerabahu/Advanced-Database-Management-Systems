<%-- 
    Document   : BooksHomePage
    Created on : May 31, 2016, 1:43:41 AM
    Author     : shanmugasudan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Books Home Page</title>
        <script src="loadFields.js"></script>
    </head>
    <body>
        <h1>How many Books do you want to add?</h1><br/>
                <form  method="get" >
                    <input type="text" name="count" id="count" >
                    <input type="button" name="submit" value="Submit" onclick="loadfields()">
               </form>
    </body>
</html>
