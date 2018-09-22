package ninaRow.servlets;

import com.google.gson.Gson;
import resources.generated.GameDescriptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Collection;
import java.util.Scanner;

@MultipartConfig
public class LoadGameServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        Collection<Part> parts = request.getParts();

        boolean isLoaded = true;

        String errorMessage = "none";

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {

            //to write the content of the file to a string
            fileContent.append(readFromInputStream(part.getInputStream()));
        }

        try {
            StringReader stringReader = new StringReader(fileContent.toString());

            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            GameDescriptor gameDescriptor = (GameDescriptor) jaxbUnmarshaller.unmarshal(stringReader);

            int rows = gameDescriptor.getGame().getBoard().getRows();

            int cols = gameDescriptor.getGame().getBoard().getColumns().intValue();

            int N = gameDescriptor.getGame().getTarget().intValue();

            int numOfPlayers = gameDescriptor.getDynamicPlayers().getTotalPlayers();

            if (rows < 5 || 50 < rows) {
//            throw new InvalidNumberOfRowsException(rows);
                isLoaded = false;
                errorMessage = "Invalid number of rows!";
            }

            if (cols < 6 || 30 < cols) {
//            throw new InvalidNumberOfColsException(cols);
                isLoaded = false;
                errorMessage = "Invalid number of columns!";
            }


            if (N >= Math.min(rows, cols) || N < 2) {
//            throw new InvalidTargetException(N);
                isLoaded = false;
                errorMessage = "Invalid goal(N) value!";
            }

            if (numOfPlayers < 2 || 6 < numOfPlayers) {
//            throw new InvalidNumberOfPlayersException(allPlayers.size());
                isLoaded = false;
                errorMessage =  "Invalid number of players!";
            }

/*
        if(gameDescriptor.getGame().getVariant().equals("Circular")){
            retGame = new CircularGame(N, playerList, rows, cols);
        } else if (gameDescriptor.getGame().getVariant().equals("Popout")) {
            retGame = new PopoutGame(N, playerList, rows, cols);
        } else if (gameDescriptor.getGame().getVariant().equals("Regular")) {
            retGame = new RegularGame(N, playerList, rows, cols);
        } else {
            throw new GameTypeException();
            isLoaded = false;
            errorMessage = "Invalid game type set!";
        }
*/
        } catch (Exception e){}

        GameStatus gameStatus = new GameStatus(isLoaded, errorMessage);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(gameStatus);

        try(PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    private class GameStatus {
        final boolean isLoaded;
        final String errorMessage;

        private GameStatus(boolean isLoaded, String errorMessage){
            this.isLoaded = isLoaded;
            this.errorMessage = errorMessage;
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
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
