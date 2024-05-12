package dut.project.pbl3.configurations;

import com.google.gson.Gson;
import dut.project.pbl3.utils.httpResponse.exceptions.BadRequestException;
import dut.project.pbl3.utils.httpResponse.exceptions.DuplicateException;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import dut.project.pbl3.utils.httpResponse.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerModule {

    @ExceptionHandler(value = { NotFoundException.class })
    @ResponseBody
    public String handleNotFoundException(NotFoundException error, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(error.getStatus());
        return new Gson().toJson(error);
    }

    @ExceptionHandler(value = { BadRequestException.class })
    @ResponseBody
    public String handleBadRequestException(BadRequestException error, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(error.getStatus());
        return new Gson().toJson(error);
    }

    @ExceptionHandler(value = { DuplicateException.class })
    @ResponseBody
    public String handleDuplicateException(DuplicateException error, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(error.getStatus());
        return new Gson().toJson(error);
    }

    @ExceptionHandler(value = { UnauthorizedException.class })
    @ResponseBody
    public String handleUnauthorizedException(UnauthorizedException error, HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(error.getStatus());
        return new Gson().toJson(error);
    }

    @ExceptionHandler(value = { EmptyResultDataAccessException.class })
    @ResponseBody
    public String handleEmptyResultDataAccessException(EmptyResultDataAccessException error, HttpServletResponse httpServletResponse) {
        NotFoundException notFoundException = new NotFoundException("Not found");
        httpServletResponse.setStatus(notFoundException.getStatus());
        return new Gson().toJson(notFoundException);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public String handleNotValidException(MethodArgumentNotValidException error, HttpServletResponse httpServletResponse) {
        String errorMessage =  Objects.requireNonNull(error.getBindingResult().getFieldError()).getDefaultMessage();
        BadRequestException badRequestException = new BadRequestException(errorMessage);
        httpServletResponse.setStatus(badRequestException.getStatus());
        return new Gson().toJson(badRequestException);
    }
}
