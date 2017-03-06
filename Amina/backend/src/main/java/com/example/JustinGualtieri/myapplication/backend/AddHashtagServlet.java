package com.example.JustinGualtieri.myapplication.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JustinGualtieri on 3/6/17.
 */

public class AddHashtagServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {

        String value = request.getParameter("value");
        String associatedPin = request.getParameter("associatedPin");

        Hashtag hashtag = new Hashtag(value, "1", associatedPin);

        // Add it to the datastore
        boolean retVal = DatastoreHelper.addUpdateHashtag(hashtag);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        doGet(request, response);
    }
}
