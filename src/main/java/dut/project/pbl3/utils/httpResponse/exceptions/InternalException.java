package dut.project.pbl3.utils.httpResponse.exceptions;

import dut.project.pbl3.utils.httpResponse.HttpResponse;
import org.springframework.http.HttpStatus;

public class InternalException extends HttpResponse {

    public InternalException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public InternalException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
