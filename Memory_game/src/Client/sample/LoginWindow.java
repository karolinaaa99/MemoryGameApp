package Client.sample;

import Server.MySql;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginWindow
{
    public LoginWindow()
    {
        login_window.Set();
        login_window.layout_grid.setAlignment(Pos.CENTER);
        login_window.layout_grid.add(username_label, 0,0);
        login_window.layout_grid.add(username, 1,0);
        login_window.layout_grid.add(password_label, 0,1);
        login_window.layout_grid.add(pass, 1,1);
        login_window.layout_grid.add(signLog, 1,2);

        Sign_in(); //logowanie
    }

    //obiekty innych klas
    MySql sql_login = new MySql();
    Window login_window = new Window(1, 800, 600, 5, 5, "Logowanie");

    //przyciski
    Button signLog = new Button(); //logowanie

    //textfields and passwordfields
    TextField username = new TextField();
    PasswordField pass = new PasswordField();

    //labels
    Label username_label = new Label("Nazwa uzytkownika");
    Label password_label = new Label("Haslo");

    //sign in - logowanie
    public void Sign_in ()
    {
        signLog.setText("Logowanie");
        signLog.setOnAction(e -> {
            //wysylam dane do serwera
            Main.client.send("Login" + "|" + username.getText() + "|" + pass.getText() + "\n");
            Get_Acces(1);
        });
    }

    //dostep do sesji logowania
    public Integer Get_Acces(Integer acces)
    {
        return acces;
    }

    public void show()
    {
        Main.stage.setScene(login_window.scene);
        Main.stage.setTitle("Logowanie");
        Main.stage.show();
    }
}
