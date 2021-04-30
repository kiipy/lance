package nl.kiipdevelopment.lance;

import nl.kiipdevelopment.lance.client.LanceConsoleClient;
import nl.kiipdevelopment.lance.configuration.ConfigurationBuilder;
import nl.kiipdevelopment.lance.configuration.DefaultConfiguration;
import nl.kiipdevelopment.lance.server.LanceServer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                "##\n" +
                "##\n" +
                "##           ########     ## ######      #######     #######\n" +
                "##          ##########    ##########    #########   #########\n" +
                "##          ##########    ##########   ##########  ###########\n" +
                "##          ###    ###    ###    ###   ###    ###  ###     ###\n" +
                "##                 ###    ###    ###   ###         ###     ###\n" +
                "##             #######    ###    ###   ###         ###########\n" +
                "##           #########    ##      ##   ###         ###########\n" +
                "##          ##########    ##      ##   ###         ###\n" +
                "##          ###    ####   ##      ##   ###         ###\n" +
                "##          ###    ####   ##      ##   ###    ###  ####    ###\n" +
                "##          ###########   ##      ##   ##########  ###########\n" +
                "##########  ###########   ##      ##    #########   #########\n" +
                "##########   ###### ###   ##      ##     #######     #######\n\n" +
                "######################################################################\n"
        );

        String[] serverIpAndPort = null;
        String[] clientIpAndPort = null;

        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "Which ones do you want to start?\n" +
                    "[1] Server\n" +
                    "[2] Client\n" +
                    "[3] Both\n"
        );

        boolean startServer = false;
        boolean startClient = false;

        switch (scanner.nextLine()) {
            case "1":
                startServer = true;
                break;
            case "2":
                startClient = true;
                break;
            case "3":
                startServer = true;
                startClient = true;
                break;
            default:
                System.exit(1);
        }

        if (startServer) {
            System.out.println("Enter the ip and port for the server (Example: " + DefaultConfiguration.HOST + ":" + DefaultConfiguration.PORT + ")");

            String line = scanner.nextLine();

            if (line.isEmpty())
                serverIpAndPort = (DefaultConfiguration.HOST + ":" + DefaultConfiguration.PORT).split(":");
            else serverIpAndPort = line.split(":");
        }

        if (startClient) {
            System.out.println("Enter the ip and port for the client (Example: " + DefaultConfiguration.HOST + ":" + DefaultConfiguration.PORT + ")");

            String line = scanner.nextLine();

            if (line.isEmpty())
                clientIpAndPort = (DefaultConfiguration.HOST + ":" + DefaultConfiguration.PORT).split(":");
            else clientIpAndPort = line.split(":");
        }

        System.out.println("Enter the password (Leave empty for no password)");

        String password = scanner.nextLine();

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        if (!password.isEmpty())
            configurationBuilder.setPassword(password);

        if (serverIpAndPort != null)
            new LanceServer(serverIpAndPort[0], Integer.parseInt(serverIpAndPort[1]), configurationBuilder.buildServerConfiguration()).start();

        if (clientIpAndPort != null)
            new LanceConsoleClient(clientIpAndPort[0], Integer.parseInt(clientIpAndPort[1]), configurationBuilder.build()).connect();
    }
}
