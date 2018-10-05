package ninaRow.servlets.gameServlets;

import chat.constants.Constants;
import chat.utils.ServletUtils;
import com.google.gson.Gson;
import general.GameManager;
import general.UserManager;
import general.gameBoard.Participant;
import general.regularGame.RegularGame;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CurrentStatusServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        response.setContentType("application/json");

        String username = (String)request.getSession(true).getAttribute("username");

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if(username == null){
            response.sendRedirect("/NinaRow/index.html");
        }

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        int gameNum = (int)request.getSession(false).getAttribute(Constants.GAME_NUMBER);

        boolean isActive = gameManager.isGameActive(gameNum);

        String currentPlayerName = gameManager.getCurrentPlayerName(gameNum);

        Participant myParticiapnt = userManager.getParticipant(username);

        boolean myTurn = gameManager.isItMyTurn(gameNum, myParticiapnt);

        boolean isWinnerFound = gameManager.isWinnerFound(gameNum);

        List<String> winnerNames = isWinnerFound? gameManager.getWinnerNames(gameNum) : null;

        CurrentStatusInfo info = new CurrentStatusInfo(isActive, currentPlayerName, myTurn, isWinnerFound, winnerNames);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(info);
        System.out.println(jsonResponse);

        try(PrintWriter out = response.getWriter()){
            out.print(jsonResponse);
            out.flush();
        }
    }

    class CurrentStatusInfo{
        final boolean isActive;
        final String currentPlayerName;
        final boolean myTurn;
        final boolean isWinnerFound;
        final List<String> winnerNames;

        public CurrentStatusInfo(boolean isActive, String currentPlayerName, boolean myTurn, boolean isWinnerFound, List<String> winnerNames){
            this.isActive = isActive;
            this.currentPlayerName = currentPlayerName;
            this.myTurn = myTurn;
            this.isWinnerFound = isWinnerFound;
            this.winnerNames = winnerNames;
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
