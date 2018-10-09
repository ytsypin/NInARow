package general;

import general.gameBoard.Participant;

import java.util.*;

public class UserManager {
    private final Map<String, Participant> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(Participant user) {
        System.out.println("Adding user " + user);
        usersMap.put(user.getName(), user);
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersMap.keySet());
    }

    public boolean isUserExists(String username) {
        return usersMap.keySet().contains(username);
    }

    public boolean isUserHuman(String username){
        return usersMap.get(username).getIsHuman();
    }

    public Participant getParticipant(String username){ return usersMap.get(username); }

    public boolean containsPerson(String usernameFromSession) {
        return usersMap.get(usernameFromSession) != null;
    }
}
