package Client.sample;

import java.util.concurrent.TimeUnit;

//utworzenie watku - klasa Timer dziedziczy po klasie Thread
public class Timer extends Thread
{
    private int seconds = 0; //sekundy
    private int minutes = 0; //minuty
    private int hours = 0; //godziny

    //metoda run() - cialo watku
    public void run()
    {
        try
        {
            while (true)
            {
                //zatrzask
                if(GameWindow.zatrzask != null)
                {
                    GameWindow.zatrzask.countDown();
                }

                seconds++;
                if (seconds >= 60)
                {
                    seconds = 0;
                    minutes++;
                    if (minutes >= 60)
                    {
                        minutes = 0;
                        hours++;
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
        catch (Exception e)
        {
            System.out.println("Timer error");
        }
    }

    //czas jest wyswietlany w formacie: HH:MM:SS
    //jezeli godziny lub minuty nie istnieja (gracz wykonal zadanie w czasie mniejszym niz 1 godzina/minuta), to tylko sekundy lub sekundy i minuty sa wyswietlane
    public String Display_time()
    {
        String sec=Integer.toString(seconds);
        if(seconds < 10 && minutes > 0) sec = "0" + Integer.toString(seconds);
        String min = Integer.toString(minutes);
        if(minutes < 10 && hours > 0) min = "0" + Integer.toString(minutes);
        String h = Integer.toString(hours);
        String display = sec;
        if(minutes > 0)
        {
            display = min + ":" + sec;
        }
        if(hours > 0)
        {
            display = h + ":" + min + ":" + sec;
        }
        //System.out.println("Timer: " + display);
        return display;
    }
}