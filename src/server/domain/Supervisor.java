package server.domain;

public class Supervisor extends User {
    private static Supervisor instance;

    public Supervisor() {
        setUsername("m.asif");
        setPassword("Ts12");
        setUserRole("Supervisor");
    }

    public static Supervisor getInstance() {
        if (instance == null) {
            // Create the instance if it's not already created
            instance = new Supervisor();
        }
        return instance;
    }

}
