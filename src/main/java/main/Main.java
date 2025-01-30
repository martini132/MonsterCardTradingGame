package main;

import main.rest.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        App app = new App();
        Server server = new Server(app, 10001);
        server.start();
    }
}