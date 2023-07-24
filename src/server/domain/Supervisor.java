package server.domain;

public class Supervisor extends User {
    private static Supervisor instance;

    public static Supervisor getInstance() {
        if (instance == null) {

            instance = new Supervisor();

        }
        return instance;
    }

}
