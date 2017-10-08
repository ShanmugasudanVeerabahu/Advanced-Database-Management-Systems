<%-- 
    Document   : BrowseMovie
    Created on : May 30, 2016, 8:06:24 PM
    Author     : shanmugasudan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search a movie</title>

    </head>
    <body>
        <style >
            body{background-color:yellowgreen}
        </style>
        <h1><b>Searching Movies</b></h1>
        <form  action="browse-movie-results.htm?action=searchResult" method="post">   <!---->
           Keyword:  <input type="text" name="keyword" required/><br/>
            <input type="radio"  name="search"  value="search by Title"  id="1">    <!---->
            <label for ="1">search by Title</label><br/>
           
            <input type="radio" name="search" value="search by Actor"  id="2">      <!---->
            <label for ="2">search by Actor</label><br/>
            
             <input type="radio" name= "search" value="search by Actress"  id="3">    <!---->
            <label for ="3">search by Actress</label><br/>         
            

       <input type="Submit" name="Search" />
        </form>

    </body>
</html>
