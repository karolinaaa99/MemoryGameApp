package Server;

public class Main
{
    static MySql mysql;

    static Server server;

    public static void main(String[] args)
    {
        mysql = new MySql();
        mysql.connect();
        //utworzenie serwera
        server = new Server("127.0.0.1", 4999);
        //uruchomienie serwera
        server.run();
    }

}
