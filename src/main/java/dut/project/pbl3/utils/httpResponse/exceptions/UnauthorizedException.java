package dut.project.pbl3.utils.httpResponse.exceptions;

import dut.project.pbl3.utils.httpResponse.HttpResponse;

public class UnauthorizedException extends HttpResponse {
    public UnauthorizedException() {
        super("Authentication failure", 401);
    }

    public UnauthorizedException(String message) {
        super(message, 401);
    }

}
