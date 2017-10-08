/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mov.hwd;

import com.mov.model.Movie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
public class MovieController extends HttpServlet {

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
   //     response.setContentType("text/html;charset=UTF-8");
        try  {
            /* TODO output your page here. You may use following sample code. */
        
           String action =  request.getParameter("action");
              if (action != null) {
            if (action.equalsIgnoreCase("addmovie")) {  //
                        Connection conn = establishConnectionJDBC();
                        HttpSession session= request.getSession();
                        String movieTitle=(String) request.getParameter("title");
                        String actorName=(String) request.getParameter("actor");
                        String actressName=(String) request.getParameter("actress");
                        String genre=(String) request.getParameter("genre");
                        String year=(String) request.getParameter("year");
                        int yr=Integer.parseInt(year);
                  //      System.out.println(movieTitle+"\n"+actorName+"\n"+actressName+"\n"+genre+"\n"+year);
                        String queryLogin = " insert into movies (title,actor,actress,genre,year ) VALUES (?,?,?,?, ?)";
                          
                                PreparedStatement selStmt = conn.prepareStatement(queryLogin);
                                selStmt.setString(1, movieTitle);
                                selStmt.setString(2,actorName);
                                selStmt.setString(3,actressName);
                                selStmt.setString(4, genre);
                                selStmt.setInt(5, yr);
                                 selStmt.executeUpdate();
                         //     System.out.println(" The record is inserted");
                            
                    RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/AddedMovieDetails.jsp");
                    rd.forward(request, response);
                              
                          
            }   //newmovie    
            else if (action.equalsIgnoreCase("newmovie")) {
                 RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/MovieDetails.jsp");
                    rd.forward(request, response);
            }
            
              }
              else{
               RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Homepage.jsp");
                   rd.forward(request, response);
            
        }
              
    }
        catch(Exception e){}
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
		connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/moviedb","root", "MySQL$2016");
        } catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
	}  
        if (connection != null) {
		System.out.println("You made it, take control your database now!");           
	} 
          return  connection;
    }
    
}
