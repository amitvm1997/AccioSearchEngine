package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet("/Search")
public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try{
            String keyword = request.getParameter("keyword");
            Connection connection = DatabaseConnection.getConnection();
            //setting the keyword to lowercase
            keyword= keyword.toLowerCase();

            //Getting result from database

            ResultSet resultSet= connection.createStatement().executeQuery("select pagetitle, pagelink,(length(lower(pagedata))-length(replace(lower(pagedata), '"+keyword+"', '')))/length('"+keyword+"')as countoccurance from pages order by countoccurance desc limit 30; \n");

            //creating an arraylist for search

            ArrayList<SearchResult> results= new ArrayList<SearchResult>();
            while(resultSet.next())
            {
                SearchResult searchResult= new SearchResult();
                searchResult.setTitle(resultSet.getString("pagetitle"));
                searchResult.setLink(resultSet.getString("pagelink"));
                results.add(searchResult);

            }

            PreparedStatement preparedStatement = connection.prepareStatement("Insert into history values (?,?)");
            preparedStatement.setString(1,keyword);
            preparedStatement.setString(2, "http://localhost:6080/AccioSearchEngine/Search?keyword="+keyword);
            preparedStatement.executeUpdate();

            request.setAttribute("results", results);
            request.getRequestDispatcher("search.jsp").forward(request,response);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>This is my servlet "+keyword+" </h3>");


        }
        catch (IOException ioException){
            System.out.println(ioException);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}

