package Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;

//klasa obslugujaca klientow polaczonych juz z serwerem
//za pomoca nowego kanalu
//klienci polaczeni z serwerem
public class ClientConnected
{
    private final SocketChannel socketChannel; //kanal gniazda
    SelectionKey key;
    public StringBuilder receiveBuffer = new StringBuilder();
    ArrayDeque<String> packetQueue = new ArrayDeque<>();

    public static int userId;

    //konstruktor
    public ClientConnected(SocketChannel socketChannel, SelectionKey key)
    {
        this.socketChannel = socketChannel;
        this.key = key;
    }

    public void send(String packet)
    {
        //packetQueue.add(packet + "\n");
        try {
            //Main.server.write(key);
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(ByteBuffer.wrap(packet.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parsePacket(String packet)
    {
        System.out.println(packet);
        //do sprawdzenia czy pakiet dotarl
        send("Dane dotarly \n");
        var zmienna = packet.split("\\|");
        switch (zmienna[0])
        {
            case "Login":
                int idZmienna = Main.mysql.Get_Int("SELECT userId FROM users WHERE username = '" + zmienna[1] + "' AND password = '" + zmienna[2] + "';");
                userId = idZmienna;
                System.out.println(idZmienna);
                if(idZmienna == 0)
                {
                    send("Bledne|logowanie \n");
                }
                else
                {
                    //send("\n Zalogowano: " + idZmienna + "\n");
                    send("Zalogowano|Zalogowano \n");
                }
                break;
            case "Register":
                int idZmienna2 = Main.mysql.Get_Int("SELECT userId FROM users WHERE username = '" + zmienna[1] + "' AND password = '" + zmienna[2] + "';");
                System.out.println(idZmienna2);
                if(idZmienna2 == 0)
                {
                    Main.mysql.Registarion("INSERT INTO users (username, password) VALUES (?,?)", zmienna[1], zmienna[2]);
                    userId = Main.mysql.Get_Int("SELECT userId FROM users WHERE username = '" + zmienna[1] + "' AND password = '" + zmienna[2] + "';");
                    send("Utworzono|nowe|konto \n");
                }
                else
                {
                    send("Konto|o|podanych|danych|juz|istnieje \n");
                }
                break;
            case "Data":
                Main.mysql.sendData("INSERT INTO games (userId, scores, time, date, dayOfWeek, dayOfYear) VALUES (?, ?, ?, ?, ?, ?)", userId, zmienna[1], zmienna[2], zmienna[3], zmienna[4], zmienna[5]);
                break;
        }
    }

}

