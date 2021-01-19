package Client.ClientFiles;

import Client.sample.GameWindow;
import Client.sample.Main;
import Client.sample.PromptWindow;
import javafx.application.Platform;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

//utworzenie watku za pomoca implementacji interfejsu Runnable
public class Client implements Runnable
{
    private final String hostname; //adres ip serwera cyfrowo lub z uzyciem dns
    private final int port; //numer portu, na ktorym nasluchuje serwer
    SocketChannel channel; //kanal gniazda
    private boolean running; //czy dziala

    //konstruktor
    public Client(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
        this.running = true;
    }

    //metoda run() - cialo watku
    @Override
    public void run()
    {
        while (running)
        {
            try
            {
                channel = SocketChannel.open(); //otwarcie kanalu gniazda
                channel.connect(new InetSocketAddress(hostname, port)); //polaczenie kanalu z serwerem

                while (!channel.finishConnect())
                {
                    System.out.println("Still connecting");
                }

                StringBuilder message = new StringBuilder();

                while (true)
                {
                    //alokowanie bufora bajtowego
                    ByteBuffer bufferA = ByteBuffer.allocate(65536);

                    while (channel.read(bufferA) > 0)
                    {
                        bufferA.flip();
                        message.append(Charset.defaultCharset().decode(bufferA));
                    }

                    while (message.length() > 0 && message.indexOf("\n") > 0)
                    {
                        String packet = message.substring(0, message.indexOf("\n"));
                        message.delete(0, message.indexOf("\n") + 1);
                        System.out.println(packet);
                        parsePacket(packet);
                    }
                }
            }
            catch (ConnectException e)
            {
                if (!running) return; //jezeli nie dziala
                //jak nie mozna polaczyc to czeka 3 sekundy
                System.out.println("Couldn't connect: " + e.getLocalizedMessage() + "; retrying in 3 seconds");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException interruptedException)
                {
                    interruptedException.printStackTrace();
                }
            }
            catch (IOException e)
            {
                if (!running) return; //jezeli nie dziala
                System.out.println("Disconnected. " + e.getLocalizedMessage() + "; retrying in 3 seconds");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException interruptedException)
                {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    //metoda do wysylania pakietu do serwera
    public boolean send(String packet)
    {
        if (!channel.isConnected()) return false; //jezeli nie ma polaczenia
        try
        {
            channel.write(ByteBuffer.wrap(packet.getBytes()));
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    //metoda do zakonczenia polaczenia
    public void disconnect()
    {
        try
        {
            this.running = false; //wylaczenie polaczenia
            channel.close(); //zamkniecie kanalu
        }
        catch (IOException e)
        {
           e.printStackTrace();
        }
    }

    public void parsePacket(String packet)
    {
        var zmienna = packet.split("\\|");
        switch (zmienna[0])
        {
            case "Bledne":
                Platform.runLater( () -> {
                    String str = "Bledne logowanie!";
                    Main.promptWindow = new PromptWindow(str);
                    Main.promptWindow.show();
                });
                break;
            case "Zalogowano":
            case "Utworzono":
                Platform.runLater( () -> {
                    Main.gameWindow = new GameWindow();
                    Main.gameWindow.show();
                });
                break;
            case "Konto":
                Platform.runLater( () -> {
                    String strr = "Konto o podanych danych juz istnieje!";
                    Main.promptWindow = new PromptWindow(strr);
                    Main.promptWindow.show();
                });
                break;
        }
    }
}

