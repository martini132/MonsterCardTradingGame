package main.rest.server;

import lombok.Getter;
import lombok.Setter;
import main.App;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Getter
@Setter
public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private App app;
    private int port;

    public Server(App app, int port) {
        setApp(app);
        setPort(port);
    }

    public void start() throws IOException {
        setServerSocket(new ServerSocket(getPort()));
        while (true) {
            setClientSocket(getServerSocket().accept());

            Thread t = new Thread(new RequestHandler(app, clientSocket));
            t.start();

        }
    }
}
