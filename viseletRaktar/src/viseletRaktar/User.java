package viseletRaktar;

import java.util.List;

// Absztrakt User osztály
public abstract class User {
    private int id;
    private String username;
    private String password;
    private String userGroup;

    // Getterek és setterek
    // ...

    // Absztrakt metódus az összes felhasználó lekérésére adatbázisból
    public abstract List<User> getAllUsers();
}
