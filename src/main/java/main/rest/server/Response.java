package main.rest.server;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.rest.http.ContentType;
import main.rest.http.HttpStatus;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class Response {
    private int statusCode;
    private String statusMessage;
    private String contentType;
    private String content;

    public Response(HttpStatus httpStatus, ContentType contentType, String content) {
        setStatusCode(httpStatus.getCode());
        setContentType(contentType.getType());
        setStatusMessage(httpStatus.getMessage());
        setContent(content);
    }

    public String build() {
        return "HTTP/1.1 " + getStatusCode() + " " + getStatusMessage() + "\r\n" +
                "Content-Type: " + getContentType() + "\r\n" +
                "Content-Length: " + getContent().length() + "\r\n" +
                "\r\n" +
                getContent();
    }
}