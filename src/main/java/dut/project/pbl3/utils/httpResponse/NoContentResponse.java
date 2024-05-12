package dut.project.pbl3.utils.httpResponse;

import org.springframework.http.HttpStatus;

public class NoContentResponse extends HttpResponse {
    public NoContentResponse() {
        super(HttpStatus.NO_CONTENT.getReasonPhrase(), HttpStatus.NO_CONTENT.value());
    }

}
