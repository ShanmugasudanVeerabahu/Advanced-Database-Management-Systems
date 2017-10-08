<%-- 
    Document   : Homepage
    Created on : May 29, 2016, 2:45:01 PM
    Author     : shanmugasudan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Movies Database Home Page</title>
        <script src="DropDown.js"></script>
    </head>
    <body>
        <style>
            body{background-color:pink}
  
            ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    background-color: #333;
}

li {
    float: left;
}

li a, .dropbtn {
    display: inline-block;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
}

li a:hover, .dropdown:hover .dropbtn {
    background-color: red;
}

li.dropdown {
    display: inline-block;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #f9f9f9;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
}

.dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    text-align: left;
}

.dropdown-content a:hover {background-color: #f1f1f1}

.show {display:block;}
</style>
<ul>
  <li><a class="active" href="#home">Home</a></li>
  <li><a href="browse-a-movie.htm?action=browsemovie">Search movie</a></li>
  <li class="dropdown">
    <a  class="dropbtn" onclick="myFunction()">Drop down</a>
    <div class="dropdown-content" id="myDropdown">
      <a href="browse-a-movie.htm?action=browsemovie">Browse Movie</a>
      <a href="add-a-movie.htm?action=newmovie">Add movie</a>
    </div>
  </li>
</ul>
    </body>
</html>


