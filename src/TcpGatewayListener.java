import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpGatewayListener implements Runnable {

    AtomicInteger connectionCounter;
    int selfTcpPort;

    public TcpGatewayListener(AtomicInteger connectionCounter, int selfTcpPort) {
        this.connectionCounter = connectionCounter;
        this.selfTcpPort = selfTcpPort;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(selfTcpPort);
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {
                Socket socket = serverSocket.accept();
                    connectionCounter.incrementAndGet();
                    Runnable serverTask = new ServerTask(socket, connectionCounter);
                    executorService.execute(serverTask);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

