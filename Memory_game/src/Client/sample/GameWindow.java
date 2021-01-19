package Client.sample;

import Client.ClientFiles.PlayerData;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GameWindow
{
    static Scene scene;

    WindowForGameWindow gameWindow;

    //zatrzask
    public static CountDownLatch zatrzask;

    public static PlayerData playerData;

    public static Timer t1;

    public static Date t2;

    int counter = 0; //ile razy ktos kliknal w dany przycisk
    List<Button> buttonList = new ArrayList<>(); //lista przyciskow
    int lastIndex; //indeks kliknietego przycisku

    int good = 0; //zlicza punkty dla dobrze dopasowanych obrazkow
    int bad = 0; //zlicza punkty dla zle kliknietych obrazkow
    int scores = 0; //oblicza zdobyte punkty, scores = good - bad

    int clicked = 4; //liczba koniecznych dopasowan obrazkow do zakonczenia gry

    private List<PlayerData> gamersList = new ArrayList<>(); //lista rozgrywek

    public GameWindow()
    {

        //utworzenie kontenera typu FlowPane - przechowuje przyciski
        FlowPane flow = new FlowPane();

        //utworzenie etykiet
        Label labelScores = new Label(); //zdobyte punkty
        Label labelTimer = new Label(); //czas
        Label labelWelcome = new Label("Witaj w grze typu memory!"); //naglowek gry
        Label labelGoodLuck = new Label("POWODZENIA!"); //etykieta powodzenia
        Label labelDate = new Label(); //data
        Label labelDayOfWeek = new Label(); //dzien tygodnia
        Label labelDayOfYear = new Label(); //dzien roku

        //robie odstępy miedzy przyciskami
        flow.setVgap(8);
        flow.setHgap(4);

        int imageId; //id obrazka

        //do pomieszania kolejnosci obrazkow
        Integer[] imagesArrayUp = {1, 2, 3, 4};
        Integer[] imagesArrayDown = {1, 2, 3, 4};

        List<Integer> arrayAsList;
        arrayAsList = Arrays.asList(imagesArrayUp);
        Collections.shuffle(arrayAsList);
        arrayAsList.toArray(imagesArrayUp);

        arrayAsList = Arrays.asList(imagesArrayDown);
        Collections.shuffle(arrayAsList);
        arrayAsList.toArray(imagesArrayDown);

        //odpalenie timera
        t1 = new Timer();
        //uruchomienie watku
        t1.start();

        //odpalenie daty
        t2 = new Date();
        //uruchomienie watku
        t2.start();

        //za pomoca petli robie 8 przyciskow
        for (int i=1; i<=8; i++)
        {
            //utworzenie przycisku
            Button button = new Button("");
            //ustawienie wymiarow przycisku
            button.setPrefHeight(120);
            button.setPrefWidth(120);
            //wybranie obrazka
            imageId = i<=4 ? imagesArrayUp[i-1] : imagesArrayDown[i - 5]; //skrocone if
            button.setId(String.valueOf(imageId));
            buttonList.add(button); //dodaje do listy przycisk

            //wyrazenie lambda
            button.setOnAction(actionEvent ->
            {
                Main.client.send("111 \n"); //wyslanie pakietu testowego, zeby sprawdzic czy klient wysyla dane do serwera
                counter++;
                Button clickedButton =  (Button) actionEvent.getSource();
                Image image = new Image(getClass().getResourceAsStream("/"+clickedButton.getId()+".png"));
                clickedButton.setGraphic(new ImageView(image));
                System.out.println("Kliknieto mnie!"); //wyswietla napis, jezeli przycisk zostal klikniety - testowanie

                if (counter % 2 == 0) //jesli jest parzysty
                {
                    if (buttonList.get(lastIndex).getId().equals(clickedButton.getId())) //dopasowano obrazki
                    {
                        clickedButton.setDisable(true);
                        buttonList.get(lastIndex).setDisable(true);
                        good += 4; //za każde poprawne dopasowanie gracz otrzymuje 4 punkty
                        clicked--;
                        try
                        {
                            chechWin(clicked);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else //jezeli nie dopasowano obrazkow
                    {
                        buttonList.get(lastIndex).setGraphic(null);
                        counter = 1;
                        bad += 2; //za kazde bledne dopasowanie gracz traci 2 punkty
                    }
                }

                lastIndex = buttonList.indexOf(clickedButton); //indeks kliknieto przycisku
                scores = good - bad; //obliczenie punktow zdobytych przez gracza

                labelScores.setText("Zdobyte punkty: " + scores);
                labelTimer.setText("Czas rozgrywki: " + t1.Display_time() + " sekund");
                labelDate.setText("Data: " + t2.getLocalDate());
                labelDayOfWeek.setText("Dzien tygodnia: " + t2.getDayOfWeek());
                labelDayOfYear.setText("Numer dnia roku: " + t2.getDayOfYear());


            });

            //do kontenera flow dodaje przyciski
            flow.getChildren().add(button);
        }

        flow.setAlignment(Pos.CENTER);

        //utworzenie kontenera typu VBox i dodanie do niego etykiet
        VBox vbox1 = new VBox(labelTimer, labelScores, labelDate, labelDayOfWeek, labelDayOfYear);
        vbox1.setAlignment(Pos.CENTER);

        //utworzenie kontenera typu VBox i dodanie do niego etykiet
        VBox vbox2 = new VBox(labelWelcome, labelGoodLuck);
        vbox2.setAlignment(Pos.CENTER);

        gameWindow = new WindowForGameWindow(1, "Memory game", vbox1, vbox2, flow);
        gameWindow.Set();
    }

    //metoda, ktora sprawdza czy gracz dopasowal poprawnie wszystkie obrazki
    //jezeli tak, to wyswietla nowe okienko informujace o zakonczeniu gry
    public void chechWin(int cli) throws InterruptedException
    {
        //jezeli gra sie zakonczyla - dopasowano wszystkie obrazki
        if (cli == 0)
        {
            //tworze obiekt typu PlayerData, ktore przechowuje dane o rozgrywce
            playerData = new PlayerData(scores, t1.Display_time(), t2.getLocalDate(), t2.getDayOfWeek(), t2.getDayOfYear());
            //dodaje dane o rozgrywce do listy rozgrywek
            gamersList.add(playerData);

            //w Stringu zapisuje dane do wyslania do serwera
            String daneDoWyslania = playerData.toString() + "\n";
            //wysylam dane do serwera
            Main.client.send(daneDoWyslania);

            //utworzenie drugiego plotna - do wyswietlania okienka, ze gra sie zakonczyla
            Stage endOfGameStage = new Stage();

            //ustawienie tytulu plotna
            endOfGameStage.setTitle("End of memory game");

            //etykiety
            Label end = new Label("KONIEC GRY!");
            Label tryAgain = new Label("Sprobuj jeszcze raz!");

            //utworzenie obiektu typu Image - do pobrania obrazka
            Image image = new Image("GoodJob.png");

            //utworzenie obiektu typu ImageView - do wyswietlenia obrazka
            ImageView imageView = new ImageView(image);

            //zmiana wielkosci obrazu z zachowaniem proporcji
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);

            VBox vBox3 = new VBox(end, tryAgain, imageView);
            vBox3.setAlignment(Pos.CENTER);
            BorderPane borderPane2 = new BorderPane();
            borderPane2.setCenter(vBox3);

            //utworzenie sceny
            Scene scene2 = new Scene(borderPane2, 800, 600);
            scene2.getStylesheets().add("style.css");

            //utworzenie zatrzasku i ustawienie go na 1
            //jak inny proces - timer - zmniejszy 1, to program bedzie dzialac dalej
            zatrzask = new CountDownLatch(1);
            //watek glowny main czeka
            zatrzask.await();
            //dodanie sceny do plotna
            endOfGameStage.setScene(scene2);
            //wyswietlenie plotna
            endOfGameStage.show();
        }
    }

    public void show()
    {
        Main.stage.setScene(gameWindow.scene);
        Main.stage.setTitle("Memory game");
        Main.stage.show();
    }
}
