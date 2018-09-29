package ninaRow.servlets;

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
import javax.servlet.http.Part;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

public class JoinGameServlet extends HttpServlet {
    private final String GAME_ROOM_URL = "/NinaRow/pages/gameroom/gameroom.html";
    private final String CHATROOM_URL = "/NinaRow/pages/chatroom/chatroom.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("application/javascript");

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        String gameNumberString = request.getParameter("gameNumber");

        int gameNumber = Integer.parseInt(gameNumberString);

        if(!gameManager.isGameActive(gameNumber)) {
            request.getSession(false).setAttribute(Constants.GAME_NUMBER, gameNumber);

            String participantName = SessionUtils.getUsername(request);

            System.out.println(participantName + "Joining game " + gameNumber);

            Participant newParticipant = userManager.getParticipant(participantName);

            gameManager.addParticipantToGame(gameNumber, newParticipant);
        } else {
            response.setStatus(500);
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
