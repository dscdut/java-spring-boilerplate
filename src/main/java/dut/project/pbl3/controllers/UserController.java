package dut.project.pbl3.controllers;

import com.google.gson.Gson;
import dut.project.pbl3.dto.user.CreateUserDto;
import dut.project.pbl3.dto.user.GetUserDto;
import dut.project.pbl3.dto.user.UpdateUserDto;
import dut.project.pbl3.services.UserService;
import dut.project.pbl3.utils.httpResponse.NoContentResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @SneakyThrows
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String createOne(@Valid @RequestBody CreateUserDto createUserDto) {
        this.userService.createOne(createUserDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String updateOne(@Valid @RequestBody UpdateUserDto updateUserDto,
                            @PathVariable("id") Long id) {
        this.userService.updateOne(id, updateUserDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String deleteOne(@PathVariable("id") Long id) {
        this.userService.deleteOne(id);
        return new Gson().toJson(new NoContentResponse());
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public List<GetUserDto> getUser() {
        return this.userService.findAll();
    }
}
