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

public class BasicInfoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        response.setContentType("application/json");
        String username = (String)request.getSession(true).getAttribute("username");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        boolean isHuman;

        if(username == null){
            response.sendRedirect("/NinaRow/index.html");
        }

        synchronized (this){
            isHuman = userManager.isUserHuman(username);
        }

        GameManager gameManager = ServletUtils.getGameManager(getServletContext());

        int gameNum = (int)request.getSession(false).getAttribute(Constants.GAME_NUMBER);

        String variant;

        switch(gameManager.getGameType(gameNum)){
            case RegularGame.popoutGame :
                variant = "Popout";
                break;
            case RegularGame.circularGame :
                variant = "Regular";
                break;
            default :
                variant = "Circular";
                break;
        }

        int goal = gameManager.getGameGoal(gameNum);

        BasicInfo info = new BasicInfo(username, isHuman, variant, goal);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(info);
        System.out.println(jsonResponse);

        try(PrintWriter out = response.getWriter()){
            out.print(jsonResponse);
            out.flush();
        }
    }

    class BasicInfo{
        final String username;
        final boolean humanity;
        final String variant;
        final int goal;

        public BasicInfo(String username, boolean isHuman, String variant, int goal){
            this.username = username;
            humanity = isHuman;
            this.variant = variant;
            this.goal = goal;
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
