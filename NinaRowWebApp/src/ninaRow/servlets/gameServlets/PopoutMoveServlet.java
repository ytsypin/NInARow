package ninaRow.servlets.gameServlets;

import chat.constants.Constants;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import general.GameManager;
import general.UserManager;
import general.gameBoard.Participant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PopoutMoveServlet extends HttpServlet {
    private final Object lockObject = new Object();

    private final String GAME_ROOM_URL = "/NinaRow/pages/gameroom/gameroom.html";
    private final String CHATROOM_URL = "/NinaRow/pages/chatroom/chatroom.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("application/javascript");

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        String columnString = request.getParameter("column");

        int column = Integer.parseInt(columnString);

        String result = gameManager.makePopoutMove((int)request.getSession(false).getAttribute(Constants.GAME_NUMBER), column);

        RegularMoveServlet.MoveStatus moveStatus = new RegularMoveServlet.MoveStatus(result);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(moveStatus);

        try(PrintWriter out = response.getWriter()){
            out.print(jsonResponse);
            out.flush();
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
