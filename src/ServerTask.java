import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerTask implements Runnable {
    private final Socket socket;
    private final AtomicInteger tcpConnectionsCounter;

    public ServerTask(Socket socket, AtomicInteger tcpConnectionsCounter) {
        this.socket = socket;
        this.tcpConnectionsCounter = tcpConnectionsCounter;
    }

    @Override
    public void run() {
        try (PrintStream toClient = new PrintStream(socket.getOutputStream());
             BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String lineFromClient;

            while ((lineFromClient = fromClient.readLine()) != null) {
                String response = "response from server" + lineFromClient;
                toClient.println(response);
            }
            tcpConnectionsCounter.decrementAndGet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
