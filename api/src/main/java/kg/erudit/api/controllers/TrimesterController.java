package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Trimester;
import kg.erudit.common.resp.GetListResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trimesters")
@Validated
@Log4j2
public class TrimesterController {
    private final ServiceWrapper serviceWrapper;

    public TrimesterController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<Trimester>> getTrimesters() {
        return new ResponseEntity<>(serviceWrapper.getTrimesters(), HttpStatus.OK);
    }
}
