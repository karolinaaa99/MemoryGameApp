package Client.sample;

import java.time.DayOfWeek;
import java.time.LocalDate;

//utworzenie watku - klasa Timer dziedziczy po klasie Thread
public class Date extends Thread
{
    //biezaca data na podstawie zegara systemowego
    private LocalDate localDate;
    //dzien tygodnia
    private DayOfWeek dayOfWeek;
    //dzien roku
    private int dayOfYear;

    //metoda run() - cialo watku
    @Override
    public void run()
    {
        try
        {
            localDate = LocalDate.now();
            dayOfWeek = localDate.getDayOfWeek();
            dayOfYear = localDate.getDayOfYear();
        }
        catch (Exception e)
        {
            System.out.println("Date error");
        }
    }

    //zwraca date
    public String getLocalDate()
    {
        return localDate.toString();
    }

    //zwraca dzien tygodnia
    public String getDayOfWeek()
    {
        return dayOfWeek.toString();
    }

    //zwraca dzien roku
    public int getDayOfYear()
    {
        return dayOfYear;
    }
}
