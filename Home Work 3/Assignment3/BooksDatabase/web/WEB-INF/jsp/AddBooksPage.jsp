<%-- 
    Document   : AddBooksPage
    Created on : May 31, 2016, 1:43:59 AM
    Author     : shanmugasudan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Books Page</title>
        <script src="loadFields.js"></script>
    </head>
    <body>
       <table>
   <tr>
          <th>
    <td>ISBN</td>
    <td>Book Title</td>
    <td>Authors</td>
    <td>Price</td>
    </th>
</tr>
<script type="text/javascript" > //<![CDATA[
    var counter=document.getElementbyId("")
    for (var celsius = 0; celsius <= 50; celsius++) {
        document.write("<tr><td>"+ celsius +"</td>");
        document.write("<td>"+ ((celsius*9/5)+32) +"</td></tr>");
}
//]]></script>
</table>
    </body>
</html>
