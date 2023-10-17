package kg.erudit.api.controllers;

import jakarta.validation.Valid;
import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.exceptions.AuthenticateException;
import kg.erudit.common.req.AuthRequest;
import kg.erudit.common.resp.AuthResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@Log4j2
public class AuthController {
    private final ServiceWrapper serviceWrapper;

    public AuthController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> auth(@Valid @RequestBody AuthRequest authRequest) throws AuthenticateException {
        AuthResponse authResponse = serviceWrapper.authenticate(authRequest);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
