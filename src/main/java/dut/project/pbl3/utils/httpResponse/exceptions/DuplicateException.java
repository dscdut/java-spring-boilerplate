package dut.project.pbl3.utils.httpResponse.exceptions;

import dut.project.pbl3.utils.httpResponse.HttpResponse;
import org.springframework.http.HttpStatus;

public class DuplicateException extends HttpResponse {
    public DuplicateException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }

    public DuplicateException() {
        super(HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
    }
}
