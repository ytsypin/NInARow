package general;

import java.util.*;

public class UserManager {
    private final Map<String, Participant> usersMap;
    private final Set<String> usersSet;

    public UserManager() {
        usersMap = new HashMap<>();
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(Participant user) {

        System.out.println("Adding user " + user);
        usersMap.put(user.name, user);
        usersSet.add(user.name);
    }

    public synchronized void removeUser(String username) { usersMap.remove(username); }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public boolean isUserHuman(String username){
        return usersMap.get(username).isHuman;
    }
}
