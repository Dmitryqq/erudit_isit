package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Subject;
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
@RequestMapping("/api/v1/subjects")
@Validated
@Log4j2
public class SubjectController {
    private final ServiceWrapper serviceWrapper;

    public SubjectController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<Subject>> getSubjects(@RequestParam(required = false, defaultValue = "true") Boolean teacherRequired) {
        return new ResponseEntity<>(serviceWrapper.getSubjects(teacherRequired), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Subject>> addSubject(@RequestBody Subject subject) {
        return new ResponseEntity<>(serviceWrapper.addSubject(subject), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateSubject(@PathVariable("id") Integer subjectId,
                                                              @RequestBody Subject subject) {
        subject.setId(subjectId);
        return new ResponseEntity<>(serviceWrapper.updateSubject(subject), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteSubject(@PathVariable("id") Integer subjectId) {
        return new ResponseEntity<>(serviceWrapper.deleteSubject(subjectId), HttpStatus.OK);
    }
}
