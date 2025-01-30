package main.rest.http;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "CREATED"),
    NO_CONTENT(204, "NO Content"),
    BAD_REQUEST(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    Forbidden(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    NOT_ALLOWED(405,"Not Allowed"),
    Conflict(409, "Conflict"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int code;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String message;

    HttpStatus(int code, String message) {
        setCode(code);
        setMessage(message);
    }
}
