package servlets.gameServlets;

import constants.Constants;
import constants.ServletUtils;
import com.google.gson.Gson;
import general.GameManager;
import general.gameBoard.Participant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PlayerTableServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        response.setContentType("application/json");

        try(PrintWriter out = response.getWriter()){
            GameManager gameManager = ServletUtils.getGameManager(getServletContext());

            int gameNum = (int)request.getSession(false).getAttribute(Constants.GAME_NUMBER);

            List<Participant> participantList = gameManager.getListOfParticipants(gameNum);

            List<ParticipantInfo> info = new ArrayList<>();

            for(Participant participant : participantList){
                String name = participant.getName();
                boolean isHuman = participant.getIsHuman();
                int turns = participant.getTurnsTaken();

                info.add(new ParticipantInfo(name, isHuman, turns));
            }

            Gson gson = new Gson();

            String json = gson.toJson(info);

            out.println(json);
            out.flush();
        }
    }

    class ParticipantInfo{
        final String name;
        final boolean isHuman;
        final int turns;

        public ParticipantInfo(String name, boolean isHuman, int turns){
            this.name = name;
            this.isHuman = isHuman;
            this.turns = turns;
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
