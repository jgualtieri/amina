package com.example.JustinGualtieri.myapplication.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JustinGualtieri on 3/6/17.
 */

public class AddPinServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {

        // Decode the pin info from the post
        String entryId = request.getParameter("entryId");
        String userId = request.getParameter("userId");
        String locationX = request.getParameter("locationX");
        String locationY = request.getParameter("locationY");
        String dateTime = request.getParameter("dateTime");
        String safetyStatus = request.getParameter("safetyStatus");
        String comment = request.getParameter("comment");

        Pin pin = new Pin(entryId, userId, locationX, locationY, dateTime, safetyStatus, comment);

        // Add it to the datastore
        boolean retVal = DatastoreHelper.addPin(pin);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        doGet(request, response);
    }
}
