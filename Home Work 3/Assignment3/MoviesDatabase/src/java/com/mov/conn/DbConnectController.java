/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mov.conn;

import com.mov.model.Movie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author shanmugasudan
 */
@WebServlet(name = "DbConnectController", urlPatterns = {"/DbConnectController"})
public class DbConnectController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  //      Statement statement= null;
         ResultSet resultSet=null;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
     //   response.setContentType("text/html;charset=UTF-8");
        try  {                                                                                            //PrintWriter out = response.getWriter()
            String action=(String) request.getParameter("action");
                 Connection conn = establishConnectionJDBC();
                 HttpSession session= request.getSession();
            if(action!=null){
            //    out.println(action);
                
                if(action.equalsIgnoreCase("searchResult")){
                        String result=request.getParameter("search");
           //         out.println("Output is being processed");
                        String keyword=(String) request.getParameter("keyword");
                        if(result.equalsIgnoreCase("search by Title")){
                                String queryLogin = "select * from moviedb.movies where title like ?";
                                PreparedStatement selStmt = conn.prepareStatement(queryLogin);
                                selStmt.setString(1, "%"+keyword+"%");
                                ArrayList<Movie>movieList= new ArrayList<>();
                                ResultSet resultSet= selStmt.executeQuery();  
                             //   request.setAttribute("movieList", movieList);

                     //       out.println(" We are selecting the database");


                         while(resultSet.next()){
                              Movie movie = new Movie();
                                 movie.setMovieid(resultSet.getInt("movieid"));
                                movie.setTitle(resultSet.getString("title"));
                                movie.setActor(resultSet.getString("actor"));
                                movie.setActress(resultSet.getString("actress"));
                                movie.setGenre(resultSet.getString("genre"));
                                movie.setYear(resultSet.getInt("year"));
                                movieList.add(movie);
                         }
                              request.setAttribute("browseResults", movieList);
                           RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/BrowseResults.jsp");
                            rd.forward(request, response);
                    }
  
              else  if(result.equalsIgnoreCase("search by Actor")){
           //         out.println("Output is being processed");
  
                                String queryLogin = "select * from moviedb.movies where actor like ?";
                                PreparedStatement selStmt = conn.prepareStatement(queryLogin);
                                selStmt.setString(1, "%"+keyword+"%");
                               ArrayList<Movie>movieList= new ArrayList<>();
                               ResultSet resultSet= selStmt.executeQuery();  
                            //   request.setAttribute("movieList", movieList);

             //              out.println(" We are selecting the database");


                        while(resultSet.next()){
                             Movie movie = new Movie();
                                movie.setMovieid(resultSet.getInt("movieid"));
                               movie.setTitle(resultSet.getString("title"));
                               movie.setActor(resultSet.getString("actor"));
                               movie.setActress(resultSet.getString("actress"));
                               movie.setGenre(resultSet.getString("genre"));
                               movie.setYear(resultSet.getInt("year"));
                               movieList.add(movie);
                        }
                             request.setAttribute("browseResults", movieList);
                          RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/BrowseResults.jsp");
                           rd.forward(request, response);
                     }
                             else  if(result.equalsIgnoreCase("search by Actress")){
           //         out.println("Output is being processed");
                                 String queryLogin = "select * from moviedb.movies where actress like ?";
                                PreparedStatement selStmt = conn.prepareStatement(queryLogin);
                                selStmt.setString(1, "%"+keyword+"%");
                                  ArrayList<Movie>movieList= new ArrayList<>();
                                  ResultSet resultSet= selStmt.executeQuery();  
                               //   request.setAttribute("movieList", movieList);

                   //           out.println(" We are selecting the database");


                           while(resultSet.next()){
                                Movie movie = new Movie();
                                   movie.setMovieid(resultSet.getInt("movieid"));
                                  movie.setTitle(resultSet.getString("title"));
                                  movie.setActor(resultSet.getString("actor"));
                                  movie.setActress(resultSet.getString("actress"));
                                  movie.setGenre(resultSet.getString("genre"));
                                  movie.setYear(resultSet.getInt("year"));
                                  movieList.add(movie);
                           }
                                request.setAttribute("browseResults", movieList);
                             RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/BrowseResults.jsp");
                              rd.forward(request, response);
                                }
                }
       else if(action.equalsIgnoreCase("browsemovie")){
                      RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/BrowseMovie.jsp");
                       rd.forward(request, response);
                }
                
            }
                     
                else{
                    RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/Homepage.jsp");
                    rd.forward(request, response);
                }
        }
            catch(Exception e){ } 
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
