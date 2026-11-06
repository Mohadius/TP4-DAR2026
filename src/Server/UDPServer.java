package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

public class UDPServer {
    public static void main(String[] args) {
        int port = 1234;
        Set<SocketAddress> clientAddresses = new HashSet<>();

        try (DatagramSocket socket = new DatagramSocket(new InetSocketAddress(port))) {
            System.out.println("Serveur UDP en attente sur le port " + port + "...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                SocketAddress senderAddress = packet.getSocketAddress();
                String message = new String(packet.getData(), 0, packet.getLength());

                if (!clientAddresses.contains(senderAddress)) {
                    clientAddresses.add(senderAddress);
                    System.out.println("Nouveau client enregistré : " + senderAddress);
                }

                System.out.println(senderAddress + " -> " + message);

                for (SocketAddress clientAddr : clientAddresses) {
                    if (!clientAddr.equals(senderAddress)) {
                        DatagramPacket sendPacket = new DatagramPacket(
                                packet.getData(),
                                packet.getLength(),
                                clientAddr
                        );
                        socket.send(sendPacket);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}