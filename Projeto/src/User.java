import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;
    private List<String> idFlights;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.idFlights = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getIdFlights() {
        return idFlights;
    }

    public void setIdFlights(List<String> idFlights) {
        this.idFlights = idFlights;
    }


    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", idFlights=" + idFlights +
                '}';
    }
}
