package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 1234;

        try {
            DatagramSocket socket = new DatagramSocket();
            Scanner scanner = new Scanner(System.in);

            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String msg = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("\n" + msg);
                    }
                } catch (Exception e) {
                    System.out.println("Connexion fermée ou erreur de réception.");
                }
            });
            receiveThread.setDaemon(true);
            receiveThread.start();

            System.out.print("Nom d'utilisateur : ");
            String username = scanner.nextLine();
            System.out.println("Connecté ! Écrivez votre message ou tapez 'quitter' pour sortir.");

            while (true) {
                String input = scanner.nextLine();
                if ("quitter".equalsIgnoreCase(input.trim())) {
                    break;
                }

                if (!input.trim().isEmpty()) {
                    String formattedMessage = "[" + username + "] : " + input;
                    byte[] buffer = formattedMessage.getBytes();
                    InetAddress address = InetAddress.getByName(serverHost);

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, serverPort);
                    socket.send(packet);
                }
            }

            socket.close();
            System.out.println("Vous avez quitté le chat.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}