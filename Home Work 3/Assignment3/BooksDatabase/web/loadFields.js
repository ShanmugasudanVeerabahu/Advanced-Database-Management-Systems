/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function loadfields(){
    var counter =document.getElementById("count").value;
    var isbn= new Array(counter);
    var authors= new Array(counter);
    var price= new Array(counter);
    var title= new Array(counter);
    var i;
        var text="";
 //   alert("you are here");
      document.write("<html>");
       document.write("<head><Title>Add Books</title></head>");
       document.write("<body>");
       document.write("<form action=\"Results-page.htm?action=addBook\" method=\"Post\">");
       document.write("<table><tr>");
       document.write("<input type=\"text\" value=\"ISBN\" readonly>");
       document.write("<input type=\"text\" value=\"Book Title\" readonly>");
       document.write("<input type=\"text\" value=\"Authors\" readonly>");
       document.write("<input type=\"text\" value=\"Price\" readonly><br/>");
       document.write("</tr>");
       
    for(i=1;i<=counter;i++)
            {
         document.write("<tr>");
         document.write("<input type=\"text\"  name=\"isbn[]\" required />");
         document.write("<input type=\"text\"  name=\"title[]\" required />");
         document.write("<input type=\"text\"  name=\"authors[]\" required />");
         document.write("<input type=\"text\"  name=\"price[]\" required /><br/>");
         document.write("</tr>");
            }
          document.write("</table>");  
  //        document.write("<input type=\"hidden\" name=\"action\" value=\"addBook\">");
          document.write(" <input type=\"submit\" name=\"submitQuery\"  value=\"submit\"  >") ;  //onclick=\"passArray()\"
         
          document.write("</form>");
         
          document.write("</body>");
           document.write("</html>");   
    }
