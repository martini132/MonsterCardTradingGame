package main.rest.server;

import java.io.IOException;

public interface ServerApp {
    Response handleRequest(Request request) throws IOException;
}
