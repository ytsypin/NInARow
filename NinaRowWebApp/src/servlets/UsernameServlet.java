package servlets;

import constants.Constants;
import constants.ServletUtils;
import com.google.gson.Gson;
import general.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UsernameServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        response.setContentType("application/json");

        String username = (String)request.getSession(false).getAttribute(Constants.USERNAME);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        boolean isHuman;

        if(username == null){
            response.sendRedirect("/NinaRow/index.html");
        }

        isHuman = userManager.isUserHuman(username);

        ParticipantInformation info = new ParticipantInformation(username, isHuman);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(info);
        System.out.println(jsonResponse);

        try(PrintWriter out = response.getWriter()){
            out.print(jsonResponse);
            out.flush();
        }
    }

    class ParticipantInformation{
        final String username;
        final boolean humanity;

        public ParticipantInformation(String username, boolean isHuman){
            this.username = username;
            humanity = isHuman;
        }
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
        return "User List Servlet";
    }// </editor-fold>
}
