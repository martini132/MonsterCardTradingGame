package main.rest.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.rest.http.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class Request {
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final String CONTENT_TYPE = "Content-Type: ";
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final String CONTENT_LENGTH = "Content-Length: ";


    private Method method;
    private String pathname;
    private String params;
    private String contentType;
    private Integer contentLength;

    private String authorizationToken;
    private String body = "";

    public Request(BufferedReader inputStream) {
        buildRequest(inputStream);
    }

    private void buildRequest(BufferedReader inputStream) {

        try {
            String line = inputStream.readLine();

            if (line != null) {

                String[] splitFirstLine = line.split(" ");
                Boolean hasParams = splitFirstLine[1].contains("?");
                System.out.println(hasParams);


                method = getMethodFromInputLine(splitFirstLine);
                pathname = getPathnameFromInputLine(splitFirstLine, hasParams);
                params = getParamsFromInputLine(splitFirstLine, hasParams);


                String contentTypeFromInputLine = "";
                contentLength = 0;
                while (!line.isEmpty()) {
                    line = inputStream.readLine();
                    if (line.startsWith(CONTENT_LENGTH)) {
                        contentLength = getContentLengthFromInputLine(line);
                    }
                    if (line.startsWith(CONTENT_TYPE)) {
                        contentType = getContentTypeFromInputLine(line);
                    }

                    if (line.startsWith("Authorization:")) {
                        authorizationToken = getAuthorizationTokenFromInputLine(line);
                    }
                }

                if (method == Method.POST || method == Method.PUT) {
                    int asciChar;
                    for (int i = 0; i < contentLength; i++) {
                        asciChar = inputStream.read();

                        body += ((char) asciChar);
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Method getMethodFromInputLine(String[] splitFirstLine) {
        return Method.valueOf(splitFirstLine[0].toUpperCase(Locale.ROOT));
    }

    private String getPathnameFromInputLine(String[] splitFirstLine, Boolean hasParams) {
        if (hasParams) {
            return splitFirstLine[1].split("\\?")[0];
        }

        return splitFirstLine[1];
    }

    private String getParamsFromInputLine(String[] splittedFirstLine, Boolean hasParams) {
        if (hasParams) {
            return splittedFirstLine[1].split("\\?")[1];
        }

        return "";
    }

    private Integer getContentLengthFromInputLine(String line) {
        return Integer.parseInt(line.substring(CONTENT_LENGTH.length()));
    }

    private String getContentTypeFromInputLine(String line) {
        return line.substring(CONTENT_TYPE.length());
    }

    private String getAuthorizationTokenFromInputLine(String line) {
        return line.split(" ")[2];
    }
}
