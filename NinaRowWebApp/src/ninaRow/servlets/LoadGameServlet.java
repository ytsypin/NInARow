package ninaRow.servlets;

import com.google.gson.Gson;
import general.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.generated.GameDescriptor;
import resources.generated.Player;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

@MultipartConfig
public class LoadGameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        JsonResponse jsonResponse = new JsonResponse(isLoaded, errorMessage);

        Gson gson = new Gson();
        String json = gson.toJson(jsonResponse);

        try(PrintWriter out = response.getWriter()) {
            out.print(json);
            out.flush();
        }
    }

    private class JsonResponse{
        boolean isLoaded;
        String errorMessage;

        private JsonResponse(boolean isLoaded, String errorMessage){
            this.isLoaded = isLoaded;
            this.errorMessage = errorMessage;
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
