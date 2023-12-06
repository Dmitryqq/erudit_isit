package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.User;
import kg.erudit.common.inner.UserExtended;
import kg.erudit.common.resp.DefaultServiceResponse;
import kg.erudit.common.resp.GetListResponse;
import kg.erudit.common.resp.SingleItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Log4j2
public class UserController {
    private final ServiceWrapper serviceWrapper;

    public UserController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<User>> getUsers(@RequestParam(required = false) Integer roleId) {
        return new ResponseEntity<>(serviceWrapper.getUsers(roleId), HttpStatus.OK);
    }

    @GetMapping(value = "/students", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<User>> getStudents(@RequestParam(required = false) Integer classId) {
        return new ResponseEntity<>(serviceWrapper.getStudents(classId), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<UserExtended>> getUserDetails(@PathVariable("id") Integer userId) {
        return new ResponseEntity<>(serviceWrapper.getUserDetails(userId), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<UserExtended>> addUser(@RequestBody UserExtended user) {
        return new ResponseEntity<>(serviceWrapper.addUser(user), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateUser(@PathVariable("id") Integer userId,
                                                              @RequestBody UserExtended user) {
        user.setId(userId);
        return new ResponseEntity<>(serviceWrapper.updateUser(user), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/lock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> lockUser(@PathVariable("id") Integer userId) {
        return new ResponseEntity<>(serviceWrapper.lockUser(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/unlock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> unlockUser(@PathVariable("id") Integer userId) {
        return new ResponseEntity<>(serviceWrapper.unlockUser(userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteUser(@PathVariable("id") Integer userId) {
        return new ResponseEntity<>(serviceWrapper.deleteUser(userId), HttpStatus.OK);
    }
}
