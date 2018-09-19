package ninaRow.servlets;

import general.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resources.generated.GameDescriptor;
import resources.generated.Player;

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
        Collection<Part> parts = request.getParts();

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

            List<Player> allPlayers = gameDescriptor.getPlayers().getPlayer();

            if (rows < 5 || 50 < rows) {
//            throw new InvalidNumberOfRowsException(rows);
            }

            if (cols < 6 || 30 < cols) {
//            throw new InvalidNumberOfColsException(cols);
            }


            if (N >= Math.min(rows, cols) || N < 2) {
//            throw new InvalidTargetException(N);
            }

            if (allPlayers.size() < 2 || 6 < allPlayers.size()) {
//            throw new InvalidNumberOfPlayersException(allPlayers.size());
            }


            ObservableList<Participant> playerList = FXCollections.observableArrayList();
            List<Short> idList = new ArrayList<>();

            int playerNum = 1;
            for (Player player : allPlayers) {
                Short playerID = player.getId();

                boolean isBot;
                if (player.getType().equals("Computer")) {
                    isBot = true;
                } else if (player.getType().equals("Human")) {
                    isBot = false;
                } else {
//                throw new ParticipantTypeException();
                }
//            Participant participant = new Participant(player.getName(), isBot, player.getId(), playerNum);
                playerNum++;
//            playerList.add(participant);
                if (idList.contains(player.getId())) {
//                throw new IDDuplicateException(player.getId());
                } else {
                    idList.add(player.getId());
                }
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
        }
*/
        } catch (Exception e){}
    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
