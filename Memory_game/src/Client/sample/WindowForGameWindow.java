package Client.sample;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//klasa odpowiedzialna za okienko i scene dla okienka gry (GameWindow)
public class WindowForGameWindow extends Stage
{
    //parametry okna
    Scene scene;
    String title;
    int number;
    //utworzenie kontenera typu BorderPane
    BorderPane borderPane = new BorderPane();
    VBox vbox1;
    VBox vbox2;
    FlowPane flow;

    //konstruktor
    public WindowForGameWindow(int number, String title, VBox vbox1, VBox vbox2, FlowPane flow)
    {
        this.number = number;
        this.title = title;
        this.vbox1 = vbox1;
        this.vbox2 = vbox2;
        this.flow = flow;
    }

    //ustawienie parametrow okna
    public void Set()
    {
        switch (number)
        {
            case 1:
                //dodanie elementow do kontenera borderPane
                borderPane.setTop(vbox2);
                borderPane.setCenter(flow);
                borderPane.setBottom(vbox1);
                //utworzenie sceny i dodanie do niej kontenera borderPane jako wezel korzenia
                scene = new Scene(borderPane, 600, 600);
                scene.getStylesheets().add("style.css");
                break;
        }
    }
}
