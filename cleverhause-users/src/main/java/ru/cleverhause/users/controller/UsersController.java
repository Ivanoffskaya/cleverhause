package ru.cleverhause.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cleverhause.users.dto.request.UserInfoRequest;
import ru.cleverhause.users.dto.request.UserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;
import ru.cleverhause.users.service.UsersService;
import ru.cleverhause.users.validation.ValidUserInfo;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService userService;

    @GetMapping("{user}/userInfo")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam("user") String user) {
        log.info("Input request getUsersInfo for user: {}", user);
        UserInfoResponse userInfoResponse = userService.userInfo(user);
        log.info("User '{}' info: {}", user, userInfoResponse);
        return ResponseEntity.ok(userInfoResponse);
    }

    @PostMapping("/user")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Input request addUser with body: {}", userRequest);
        UserInfoResponse userInfoResponse = userService.addUser(userRequest);
        log.info("Added user {} info: {}", userRequest.getUserId(), userInfoResponse);
        return ResponseEntity.ok(userInfoResponse);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Input request updateUser with body: {}", userRequest);
        boolean isUpdated = userService.updateUser(userRequest);
        if (isUpdated) {
            log.info("User {} was updated", userRequest.getUserId());
        } else {
            log.info("User {} was not updated", userRequest.getUserId());
        }
        return ResponseEntity.ok(Map.of("updated", isUpdated));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String userId) {
        log.info("Input request deleteUser with id: {}", userId);
        UserInfoResponse userInfoResponse = userService.deleteUser(userId);
        log.info("User {} was deleted", userId);
        return ResponseEntity.ok(userInfoResponse);
    }
}