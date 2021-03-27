package nl.kiipdevelopment.lance.server;

import nl.kiipdevelopment.lance.configuration.DefaultConfiguration;
import nl.kiipdevelopment.lance.configuration.ServerConfiguration;
import nl.kiipdevelopment.lance.server.command.CommandManager;
import nl.kiipdevelopment.lance.server.storage.Storage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class LanceServer extends Thread {
    private final ServerConfiguration configuration;
    private final String host;
    private final int port;

    private ServerSocket serverSocket;
    private Storage<?> storage;

    public final List<ServerConnectionHandler> handlers = new ArrayList<>();
    public final List<String> connections = new ArrayList<>();
    public boolean active = true;

    public LanceServer() {
        this(DefaultConfiguration.HOST, DefaultConfiguration.PORT, DefaultConfiguration.getDefaultServerConfiguration());
    }

    public LanceServer(ServerConfiguration configuration) {
        this(DefaultConfiguration.HOST, DefaultConfiguration.PORT, configuration);
    }

    public LanceServer(String host, int port) {
        this(host, port, DefaultConfiguration.getDefaultServerConfiguration());
    }

    public LanceServer(String host, int port, ServerConfiguration configuration) {
        super("Lance-Server");

        this.host = host;
        this.port = port;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        Storage.updateDefaultStorage(configuration.getStorageType(), configuration.getStorageLocation());

        storage = Storage.getDefaultStorage();
        
        CommandManager.init();

        try {
            serverSocket = new ServerSocket(port, configuration.getBacklog(), InetAddress.getByName(host));

            System.out.println("[" + getName() + "] " + "Server started on " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());

            while (active)
                try {
                    ServerConnectionHandler handler = new ServerConnectionHandler(this, configuration, serverSocket.accept());
                    handler.start();

                    handlers.add(handler);
                } catch (SocketException e) {
                    if (e.getMessage().equals("Socket closed"))
                        System.out.println("[" + getName() + "] " + "Server shut down.");
                    else e.printStackTrace();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        active = false;

        try {
            serverSocket.close();

            for (ServerConnectionHandler handler : handlers)
                handler.close("Server closing.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
