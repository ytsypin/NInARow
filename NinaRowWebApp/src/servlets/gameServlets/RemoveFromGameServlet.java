package servlets.gameServlets;

import constants.Constants;
import constants.ServletUtils;
import constants.SessionUtils;
import general.GameManager;
import general.UserManager;
import general.gameBoard.Participant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveFromGameServlet extends HttpServlet {
    private final Object lockObject = new Object();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("application/javascript");

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        int gameNumber = (int)request.getSession(false).getAttribute(Constants.GAME_NUMBER);

        String participantName = SessionUtils.getUsername(request);

        synchronized (lockObject) {
            System.out.println("Removing " + participantName + " from game");
            Participant participant = userManager.getParticipant(participantName);

            gameManager.removeParticipantFromGame(gameNumber, participant);

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
