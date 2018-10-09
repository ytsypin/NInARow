package servlets.gameServlets;

import constants.Constants;
import constants.ServletUtils;
import com.google.gson.Gson;
import general.GameManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegularMoveServlet extends HttpServlet {
    private final Object lockObject = new Object();

    private final String GAME_ROOM_URL = "/gameroom.html";
    private final String CHATROOM_URL = "/chatroom.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("application/json");

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        String columnString = request.getParameter("column");

        int column = Integer.parseInt(columnString);

        System.out.println("Making regular move column " + column);

        String result = gameManager.makeRegularMove((int)request.getSession(false).getAttribute(Constants.GAME_NUMBER), column);

        MoveStatus moveStatus = new MoveStatus(result);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(moveStatus);

        try(PrintWriter out = response.getWriter()){
            out.print(jsonResponse);
            out.flush();
        }
    }

    public static class MoveStatus{
        String result;
        boolean isOK;

        public MoveStatus(String result){
            if(result.equals("")){
                this.result = result;
                isOK = true;
            } else {
                this.result = result;
                isOK = false;
            }
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
