package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

//utworzenie watku za pomoca implementacji interfejsu Runnable
public class Server implements Runnable
{
    //przechowuje kanal gniazda i polaczonego klienta
    private final HashMap<SocketChannel, ClientConnected> activeConnections = new HashMap<>();
    public String hostname; //nazwa hosta
    public int port; //numer portu
    public final static long TIMEOUT = 10000;

    private ServerSocketChannel serverChannel; //kanal gniazda serwera
    Selector selector; //selektor

    //konstruktor
    public Server(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
        init();
    }

    //inizcjalizacja serwera
    private void init()
    {
        System.out.println("Initializing server");
        if (selector != null) return;
        if (serverChannel != null) return;

        try
        {
            //otwarcie gniazda serwera
            serverChannel = ServerSocketChannel.open();

            //ustalenie trybu nieblokujacego dla kanalu serwera gniazda
            serverChannel.configureBlocking(false);

            //metoda socket() klasy ServerSocketChannel zwraca skojarzone z kanalem gniazdo klasy ServerSocket
            //uzywana jest tutaj jedna z wersji metody bind tej klasy
            serverChannel.socket().bind(new InetSocketAddress(hostname, port));

            //utworzenie selektora
            selector = Selector.open();

            //rejestracja kanalu gniazda serwera u selektora i akceptacja polaczenia
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //metoda run() - cialo watku
    @Override
    public void run()
    {
        System.out.println("Now accepting connections...");
        try
        {
            while (!Thread.currentThread().isInterrupted())
            {
                //selekcja gotowej operacji
                //to wywolanie jest blokujace
                //czeka, az selektor powiadomi
                //o gotowosci jakiejs operacji na jakims kanale
                selector.select(TIMEOUT);

                //teraz jakies operacje sa gotowe do wykonania, keys - zbior kluczy
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                //przegladamy gotowe klucze
                while (keys.hasNext())
                {
                    //pobranie klucza
                    SelectionKey key = keys.next();

                    //usuniecie klucza - nie ma automatycznego usuwania
                    //w przeciwnym razie w kolejnym kroku petli obsluzony klucz
                    //dostalibysmy do ponownej obslugi
                    keys.remove();

                    //czy klucz jest poprawny
                    if (!key.isValid())
                    {
                        continue;
                    }

                    //wykonanie operacji opisywanej przez klucz
                    if (key.isAcceptable())
                    {
                        System.out.println("Accepting connection");
                        accept(key);
                    }

                    //ktorys z kanalu gotowy do pisania
                    if (key.isWritable())
                    {
                        System.out.println("Writing...");
                        write(key);
                    }

                    //ktorys z kanalow gotowy do czytania
                    if (key.isReadable())
                    {
                        System.out.println("Reading connection");
                        read(key);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }

    //metoda do obsluzenia kanalu gotowego do pisania
    public void write(SelectionKey key) throws IOException
    {
        //uzyskanie kanalu
        SocketChannel channel = (SocketChannel) key.channel();
        System.out.println("Sending to: " + activeConnections.get(channel));
        activeConnections.get(channel).packetQueue.forEach(d ->
        {
            try
            {
                channel.write(ByteBuffer.wrap(d.getBytes()));
                System.out.println("Halo wysylam!");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        activeConnections.get(channel).packetQueue.clear();
        key.interestOps(SelectionKey.OP_READ);
    }

    //zamkniecie polaczenia serwera z klientem
    private void closeConnection()
    {
        System.out.println("Closing server down");
        if (selector != null)
        {
            try
            {
                selector.close();
                serverChannel.socket().close();
                serverChannel.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    //metoda do wykonania operacji opisywanej przez klucz
    private void accept(SelectionKey key) throws IOException
    {
        //uzyskanie kanalu
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        //uzyskanie kanalu do komunikacji z klientem
        //accept jest nieblokujace, bo juz jest polaczenie
        SocketChannel socketChannel = serverSocketChannel.accept();

        //kanal nieblokujacy, bo bedzie rejestrowany u selektora
        socketChannel.configureBlocking(false);

        //rejestracja kanalu komunikacji z klientem
        //do monitorowania przez ten sam selektor
        SelectionKey clientKey = socketChannel.register(selector, SelectionKey.OP_READ);

        //utworzenie polaczonego klienta z okreslonym kanalem i nazwa
        ClientConnected client = new ClientConnected(socketChannel, clientKey);
        activeConnections.put(socketChannel, client);

        System.out.println("New client: " + socketChannel.getRemoteAddress());


    }

    //metoda do obsluzenia kanalu gotowego do czytania
    private void read(SelectionKey key) throws IOException
    {
        //uzyskanie kanalu
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(65536);
        buffer.clear();

        ClientConnected c = activeConnections.get(channel);

        int read;

        try
        {
            while ((read = channel.read(buffer)) > 0)
            {
                buffer.flip();
                c.receiveBuffer.append(Charset.defaultCharset().decode(buffer));
            }
        }
        catch (IOException e)
        {
            System.out.println("Reading problem, closing connection");
            //e.printStackTrace();
            key.cancel();
            channel.close();
            return;
        }

        if (read == -1)
        {
            System.out.println("Nothing was there to be read, closing connection");
            channel.close();
            key.cancel();
            return;
        }

        while (c.receiveBuffer.length() > 0 && c.receiveBuffer.indexOf("\n") > 0)
        {
            String packet = c.receiveBuffer.substring(0, c.receiveBuffer.indexOf("\n"));
            c.receiveBuffer.delete(0, c.receiveBuffer.indexOf("\n") + 1);

            c.parsePacket(packet);
        }

        key.interestOps(SelectionKey.OP_WRITE);
    }
}

