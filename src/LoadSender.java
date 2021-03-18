import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadSender implements Runnable{

    private static final String UDP_SERVER_HOST = "localhost";

    private final String balancerHost;
    private final int udpServerPort;
    private final AtomicInteger tcpConnectionsCounter;
    private final int sendingFrequency;

    public LoadSender(String balancerHost, int udpServerPort, AtomicInteger tcpConnectionsCounter, int sendingFrequency) {
        this.balancerHost = balancerHost;
        this.udpServerPort = udpServerPort;
        this.tcpConnectionsCounter = tcpConnectionsCounter;
        this.sendingFrequency = sendingFrequency;
    }

    @Override
    public void run() {

        try {
            InetAddress inetAddress = InetAddress.getByName(UDP_SERVER_HOST);
            DatagramSocket udpSocket = new DatagramSocket(udpServerPort);

            while (true) {
                Thread.sleep(sendingFrequency);
                String line = tcpConnectionsCounter.toString();

                byte[] outputData = line.getBytes();

                DatagramPacket packetOut = new DatagramPacket(
                        outputData,
                        outputData.length,
                        inetAddress,
                        udpServerPort);

                udpSocket.send(packetOut);

            }

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
