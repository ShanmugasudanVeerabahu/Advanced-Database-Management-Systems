/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.bookcontroller;

import com.neu.bookmodel.Book;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author shanmugasudan
 */
public class BookContoller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
         String action=(String) request.getParameter("action");
             
                  int counter=0;
                    if(action!=null){
                            if(action.equalsIgnoreCase("addBook")){
                                   Connection conn = establishConnectionJDBC();
                                    HttpSession session= request.getSession();
                                    float temp=0;
                                   String isbn[]= new String[request.getParameterValues("isbn[]").length];
                                    String authors[]= new String[request.getParameterValues("authors[]").length];
                                    String  price[]= new String[request.getParameterValues("price[]").length];
                                      float  priceValue[]= new float[request.getParameterValues("price[]").length];
                                    String title[]= new String[request.getParameterValues("title[]").length];
                                     isbn=request.getParameterValues("isbn[]");
                                    authors=request.getParameterValues("authors[]");
                                    price=request.getParameterValues("price[]");
                                    title=request.getParameterValues("title[]");
                                            for(int p=0;p<price.length;p++){
                                                    temp=Float.parseFloat(price[p]);
                                                    priceValue[p]=temp;
                                                }
                                    for(int i=0;i<isbn.length;i++){
                                        String queryLogin = "insert into booksdb.books (isbn,title,authors,price) values(?,?,?,?) ";
                                        PreparedStatement prepStmt = conn.prepareStatement(queryLogin);
                                        prepStmt.setString(1,isbn[i]);
                                        prepStmt.setString(2, title[i]);
                                        prepStmt.setString(3, authors[i]);
                                        prepStmt.setFloat(4,priceValue[i] );
                                        int rs=prepStmt.executeUpdate();
                                    }
                                    request.setAttribute("counter", String.valueOf(isbn.length));
                                    RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/jsp/ResultsPage.jsp");
                                    rd.forward(request, response);    
                            }
                }
                else{
                        RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/jsp/BooksHomePage.jsp");
                       rd.forward(request, response);
                    }
        }
        catch(Exception e)
        {
        }
    }
        protected Connection establishConnectionJDBC()
            throws IOException{
        Connection connection = null;
        try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();	
	} 
        try {
		connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/booksdb","root", "MySQL$2016");
        } catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
	}  
        if (connection != null) {
		System.out.println("You made it, take control your database now!");           
	} 
          return  connection;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
