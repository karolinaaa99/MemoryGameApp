package Client.sample;

import javafx.application.Application;
import javafx.stage.Stage;

import Client.ClientFiles.*;

public class Main extends Application
{

    public static Stage stage;
    public static LoginWindow loginWindow;
    public static RegisterWindow registerWindow;
    public static GameWindow gameWindow;
    public static StartWindow startWindow;
    public static PromptWindow promptWindow;

    //deklaracja klienta
    static Client client;

    //przeslonienie metody start
    @Override
    public void start(Stage primaryStage) throws Exception
    {

        stage = primaryStage;
        startWindow = new StartWindow();
        startWindow.show();
    }

    public static void main(String[] args)
    {
        //utworzenie klienta
        client = new Client("127.0.0.1", 4999);
        //utworzenie watku dla klienta
        Thread t = new Thread(client);
        //uruchomienie watku dla klienta
        t.start();

        launch(args);
    }
}