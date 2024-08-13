package com.bside.Errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String datail;

    public ErrorResponse(String message, String detail) {
        this.message = message;
        this.datail = detail;
    }
}
