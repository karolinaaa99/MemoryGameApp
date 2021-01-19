package Client.sample;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//klasa odpowiedzialna za okienko i scene
public class Window extends Stage
{
    //parametry okna
    Scene scene;
    int number, MinSize1, MinSize2, Vgap, Hgap;
    String title;
    GridPane layout_grid = new GridPane();

    //konstruktor
    public Window (int Number_layout, int MinSize01, int MineSize02, int V, int H, String name)
    {
        number = Number_layout;
        MinSize1 = MinSize01;
        MinSize2 = MineSize02;
        Vgap = V;
        Hgap = H;
        title = name;
    }

    //ustawienie parametrow okna
    public void Set()
    {
        switch (number)
        {
            case 1:
                scene = new Scene(layout_grid);
                scene.getStylesheets().add("style.css");
                layout_grid.setMinSize(MinSize1,MinSize2);
                layout_grid.setVgap(Vgap);
                layout_grid.setHgap(Hgap);
                break;
        }
    }
}
