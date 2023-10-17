package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Class;
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
@RequestMapping("/api/v1/classes")
@Validated
@Log4j2
public class ClassController {
    private final ServiceWrapper serviceWrapper;

    public ClassController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<Class>> getClasses() {
        return new ResponseEntity<>(serviceWrapper.getClasses(), HttpStatus.OK);
    }

    @PostMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Class>> addClass(@RequestBody Class clazz) {
        return new ResponseEntity<>(serviceWrapper.addClass(clazz), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateClass(@PathVariable("id") Integer classId,
                                                              @RequestBody Class clazz) {
        clazz.setId(classId);
        return new ResponseEntity<>(serviceWrapper.updateClass(clazz), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteClass(@PathVariable("id") Integer classId) {
        return new ResponseEntity<>(serviceWrapper.deleteClass(classId), HttpStatus.OK);
    }
}
