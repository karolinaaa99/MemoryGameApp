package Server;

import java.sql.*;

//klasa odpowiedzialna za zapis i odczyt z bazy danych
public class MySql
{
    //zmienne
    private Connection conn;
    private final String url;
    private final String userName;
    private final String password_base;

    //konstruktor
    public MySql()
    {
        //memory_game to nazwa bazy danych
        //3306 to numer portu
        url = "jdbc:mysql://localhost:3306/memory_game";
        userName = "root";
        password_base = "123";
    }

    //polaczenie z baza
    public String connect()
    {
        try
        {
            conn = DriverManager.getConnection(url, userName, password_base);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return e.toString();
        }
        return "Connected successfully";
    }

    //zakonczenie polaczenia z baza
    public void disconnect()
    {
        try
        {
            conn.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.toString());
        }
    }

    // Getting Strings from the Database
    public String Get_String(String query)
    {
        connect();
        Statement statement;
        ResultSet rs;
        String result="";

        try
        {
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            rs.next();
            result = rs.getString(1);
        }
        catch (SQLException e)
        {
            System.err.println(e.toString());
            return e.toString();
        }
        //disconnect();
        return result;
    }

    // Getting Integers from the Database
    public Integer Get_Int(String query)
    {
        connect();
        Statement statement;
        ResultSet rs;
        int result = 0;

        try
        {
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next())
            {
                result = rs.getInt(1);
            }

        }
        catch (SQLException e)
        {
            System.err.println(e.toString());
        }
        return result;
    }

    //rejestracja uzytkownika w bazie danych
    public void Registarion(String query, String first, String second)
    {
        //connect();
        PreparedStatement prstatement;
        try
        {
            prstatement = conn.prepareStatement(query);
            prstatement.setString(1, first);
            prstatement.setString(2, second);
            prstatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println(e.toString());
        }
        //disconnect();
    }

    //wyslanie wynikow gry do bazy danych
    public void sendData(String query, int userId, String scores, String time, String date, String dayOfWeek, String dayOfYear)
    {
        PreparedStatement prsStatement;
        try
        {
            prsStatement = conn.prepareStatement(query);
            prsStatement.setString(1, String.valueOf(userId));
            prsStatement.setString(2, scores);
            prsStatement.setString(3, time);
            prsStatement.setString(4, date);
            prsStatement.setString(5, dayOfWeek);
            prsStatement.setString(6, dayOfYear);
            prsStatement.executeUpdate();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }
}
