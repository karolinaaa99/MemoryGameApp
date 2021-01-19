package Client.sample;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StartWindow
{
    public StartWindow()
    {
        start_window.Set();
        start_window.layout_grid.setAlignment(Pos.CENTER);
        start_window.layout_grid.add(witaj, 0,0);
        start_window.layout_grid.add(haveAccount, 0,1);
        start_window.layout_grid.add(signLog, 0,2);
        start_window.layout_grid.add(notHaveAccount, 0,3);
        start_window.layout_grid.add(signReg, 0,4);

        choice();
    }

    Window start_window = new Window(1, 800, 600, 5, 5, "Memory game - strona startowa");

    Label witaj = new Label("Witaj w grze typu memory!");
    Label haveAccount = new Label("Masz juz konto? Zaloguj sie!");
    Button signLog = new Button("Logowanie");
    Label notHaveAccount = new Label("Nie masz jeszcze konta? Zarejestruj sie!");
    Button signReg = new Button("Rejestracja");

    public void choice()
    {
        signLog.setOnAction(e -> {
            Main.loginWindow = new LoginWindow();
            Main.loginWindow.show();
        });

        signReg.setOnAction(e -> {
            Main.registerWindow = new RegisterWindow();
            Main.registerWindow.show();
        });
    }

    public void show()
    {
        Main.stage.setScene(start_window.scene);
        Main.stage.setTitle("Memory game - strona startowa");
        Main.stage.show();
    }
}
