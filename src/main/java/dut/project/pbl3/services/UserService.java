package dut.project.pbl3.services;

import dut.project.pbl3.dto.user.CreateUserDto;
import dut.project.pbl3.dto.user.GetUserDto;
import dut.project.pbl3.dto.user.UpdateUserDto;
import dut.project.pbl3.models.ApplicationUser;
import dut.project.pbl3.models.User;
import dut.project.pbl3.repositories.UserRepository;
import dut.project.pbl3.utils.ObjectMapperUtils;
import dut.project.pbl3.utils.httpResponse.exceptions.DuplicateException;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import dut.project.pbl3.utils.httpResponse.exceptions.UnauthorizedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findUserByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s does not exist", email)));

        return new ApplicationUser(user.get());
    }

    public List<GetUserDto> findAll() {
        return ObjectMapperUtils.mapAll(this.userRepository.findAll(), GetUserDto.class);
    }

    public void createOne(CreateUserDto createUserDto) throws DuplicateException {
        Optional<User> foundUser = findUserByEmail(createUserDto.getEmail());

        if (foundUser.isPresent())
            throw new DuplicateException("User (" + createUserDto.getEmail() + ") is existed");

        User user = ObjectMapperUtils.map(createUserDto, User.class);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        this.userRepository.save(user);
    }

    public void deleteOne(Long id) throws UsernameNotFoundException, NotFoundException {
        Optional<User> user = this.userRepository.findById(id);
        user.orElseThrow(NotFoundException::new);
        try {
            this.userRepository.delete(user.get());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (user.get().getDeletedAt() == null) {
                user.get().setDeletedAt(timestamp);
                this.userRepository.save(user.get());
            }
        }
    }

    public void updateOne(Long id, UpdateUserDto updateUserDto) throws NotFoundException, DuplicateException, UnauthorizedException {
        Optional<User> foundUser = this.userRepository.findById(id);
        foundUser.orElseThrow(() -> new NotFoundException("Not found user Id"));

        Optional<User> foundUserByEmail = findUserByEmail(updateUserDto.getEmail());
        if (foundUserByEmail.isPresent() && foundUserByEmail.get().getId() != foundUser.get().getId()) {
            throw new DuplicateException("Email is already existed");
        }

        foundUser.get().setUserName(updateUserDto.getUserName());
        foundUser.get().setEmail(updateUserDto.getEmail());
        foundUser.get().setRole(updateUserDto.getRole());

        if (updateUserDto.getPassword() != null) {
            if (!updateUserDto.getPassword().equals(updateUserDto.getConfirmPassword())) {
                throw new UnauthorizedException("Password doesn't match");
            }
            foundUser.get().setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }

        if (Boolean.TRUE.equals(updateUserDto.getIsRestore()))
            foundUser.get().setDeletedAt(null);

        this.userRepository.save(foundUser.get());
    }

    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }
}