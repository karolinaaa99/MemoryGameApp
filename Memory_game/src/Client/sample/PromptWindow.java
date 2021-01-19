package Client.sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PromptWindow
{
    static Label prompt;

    public PromptWindow(String str)
    {
        prompt = new Label(str);
        prompt_window.Set();
        prompt_window.layout_grid.setAlignment(Pos.CENTER);
        prompt_window.layout_grid.add(prompt, 0,0);
        prompt_window.layout_grid.add(imageView, 0, 1);
    }

    Window prompt_window = new Window(1, 800, 600, 5, 5, "Komunikat o błędzie");

    //utworzenie obiektu typu Image - do pobrania obrazka
    Image image = new Image("Error.jpg");

    //utworzenie obiektu typu ImageView - do wyswietlenia obrazka
    ImageView imageView = new ImageView(image);

    //zmiana wielkosci obrazu z zachowaniem proporcji
    //imageView.setFitWidth(400);
    //imageView.setPreserveRatio(true);

    public void show()
    {
        Main.stage.setScene(prompt_window.scene);
        Main.stage.setTitle("Komunikat o błędzie");
        Main.stage.show();
    }
}
