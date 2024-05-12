package dut.project.pbl3.utils.httpResponse;

import org.springframework.http.HttpStatus;

public class okResponse extends HttpResponse {
    Object detail;

    public okResponse(String message, Object detail) {
        super(message, HttpStatus.OK.value());
        this.detail = detail;
    }

    public okResponse(Object detail) {
        super(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
        this.detail = detail;
    }


    public okResponse(String message) {
        super(message, HttpStatus.OK.value());
    }

    public okResponse() {
        super(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
    }
}
