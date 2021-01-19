package Client.sample;

import Server.MySql;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class RegisterWindow
{
    public RegisterWindow()
    {
        register_window.Set();
        register_window.layout_grid.setAlignment(Pos.CENTER);
        register_window.layout_grid.add(username_label, 0,0);
        register_window.layout_grid.add(username, 1,0);
        register_window.layout_grid.add(password_label, 0,1);
        register_window.layout_grid.add(pass, 1,1);
        register_window.layout_grid.add(password_label_test, 0, 2);
        register_window.layout_grid.add(passTest, 1, 2);
        register_window.layout_grid.add(signReg, 1,3);

        Sign_up(username, pass, passTest);
    }

    //obiekty innych klas
    MySql sql_register = new MySql();
    Window register_window = new Window(1, 800, 600, 5, 5, "Rejestracja");

    //przyciski
    Button signReg = new Button(); //logowanie

    //textfields and passwordfields
    TextField username = new TextField();
    PasswordField pass = new PasswordField();
    PasswordField passTest = new PasswordField();

    //text and labels
    Label username_label = new Label("Nazwa uzytkownika");
    Label password_label = new Label("Haslo");
    Label password_label_test = new Label("Wpisz haslo ponownie");

    //metoda sprawdzajaca czy podane hasla sa takie same
    public static boolean checkPasswords(String password, String passwordTest)
    {
        return password.equals(passwordTest);
    }

    //sign up - rejestracja
    public void Sign_up (TextField username2, PasswordField pass2, PasswordField passTest2)
    {
        signReg.setText("Rejestracja");
        signReg.setOnAction(e -> {
            if (checkPasswords(pass2.getText(), passTest2.getText()))
            {
                //wysylam dane do serwera
                Main.client.send("Register" + "|" + username2.getText() + "|" + pass2.getText() + "\n");
            }
            else
            {
                String str = "Podane hasla sa rozne!";
                Main.promptWindow = new PromptWindow(str);
                Main.promptWindow.show();
            }
        });
        Get_Acces(1);
    }

    //dostep do sesji logowania
    public Integer Get_Acces(Integer acces)
    {
        return acces;
    }

    public void show()
    {
        Main.stage.setScene(register_window.scene);
        Main.stage.setTitle("Rejestracja");
        Main.stage.show();
    }
}
