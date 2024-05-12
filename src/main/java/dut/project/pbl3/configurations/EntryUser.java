package dut.project.pbl3.configurations;

import dut.project.pbl3.dto.user.CreateUserDto;
import dut.project.pbl3.services.UserService;
import dut.project.pbl3.utils.httpResponse.exceptions.DuplicateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static dut.project.pbl3.common.enums.RoleEnum.*;

@AllArgsConstructor
@Component
@Slf4j
public class EntryUser {

    private final UserService userService;

    @Bean
    public void createEntryUser() {
        CreateUserDto entryApplicationUser = new CreateUserDto(
                "admin@gmail.com",
                "admin",
                "admin123",
                "admin123",
                ADMIN.name()
        );
        CreateUserDto staff = new CreateUserDto(
                "staff@gmail.com",
                "staff",
                "staff123",
                "staff123",
                STAFF.name()
        );
        try {
            log.info("Creating entry user");
            this.userService.createOne(entryApplicationUser);
            this.userService.createOne(staff);
        } catch (Exception | DuplicateException error) {
            log.error(error.getMessage());
        }
    }
}
