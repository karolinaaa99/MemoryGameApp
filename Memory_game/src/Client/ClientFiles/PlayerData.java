package Client.ClientFiles;

public class PlayerData
{
    private int scores; //zdobyte punkty
    private String time; //czas rozgrywki
    private String date; //data
    private String dayOfWeek; //dzien tygodnia
    private int dayOfYear; //numer dnia roku

    //konstruktor
    public PlayerData(int scores, String time, String date, String dayOfWeek, int dayOfYear)
    {
        this.scores = scores;
        this.time = time;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.dayOfYear = dayOfYear;
    }

    //settery

    public void setScores(int scores)
    {
        this.scores = scores;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setDayOfWeek(String dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDayOfYear(int dayOfYear)
    {
        this.dayOfYear = dayOfYear;
    }

    //gettery

    public int getScores()
    {
        return this.scores;
    }

    public String getTime()
    {
        return this.time;
    }

    public String getDate()
    {
        return this.date;
    }

    public String getDayOfWeek()
    {
        return this.dayOfWeek;
    }

    public int getDayOfYear()
    {
        return this.dayOfYear;
    }

    //przeslonienie metody toString
    @Override
    public String toString()
    {
        /**return "PlayerData{" +
                "scores=" + scores +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", dayOfYear=" + dayOfYear +
                '}';**/

        return "Data|" + scores + "|" + time + "|" + date + "|" + dayOfWeek + "|" + dayOfYear;
    }
}
