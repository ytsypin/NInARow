package ninaRow.servlets.gameServlets;

import chat.constants.Constants;
import chat.utils.ServletUtils;
import com.google.gson.Gson;
import general.GameManager;
import general.UserManager;
import general.regularGame.RegularGame;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GameBoardServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        response.setContentType("application/json");

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        int gameNum = (int)request.getSession(false).getAttribute(Constants.GAME_NUMBER);

        String name = (String)request.getSession(false).getAttribute(Constants.USERNAME);

        int[][] board = gameManager.getGameBoard(gameNum);
        int cols = gameManager.getColumns(gameNum);
        int rows = gameManager.getRows(gameNum);
        boolean myTurn = gameManager.getIfMyTurn(gameNum, name);
        String variant = gameManager.getVariant(gameNum);

        GameBoardData info = new GameBoardData(cols, rows, board, myTurn, variant);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(info);
        System.out.println(jsonResponse);

        try(PrintWriter out = response.getWriter()){
            out.print(jsonResponse);
            out.flush();
        }
    }

    class GameBoardData{
        int cols;
        int rows;
        int[][] board;
        boolean myTurn;
        String variant;

        public GameBoardData(int cols, int rows, int[][] board, boolean myTurn, String variant){
            this.cols = cols;
            this.rows = rows;
            this.board = board;
            this.myTurn = myTurn;
            this.variant = variant;
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
