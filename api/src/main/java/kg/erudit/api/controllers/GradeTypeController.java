package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.GradeType;
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
@RequestMapping("/api/v1/gradetypes")
@Validated
@Log4j2
public class GradeTypeController {
    private final ServiceWrapper serviceWrapper;

    public GradeTypeController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<GradeType>> getGradeTypes() {
        return new ResponseEntity<>(serviceWrapper.getGradeTypes(), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<GradeType>> addGradeType(@RequestBody GradeType gradeType) {
        return new ResponseEntity<>(serviceWrapper.addGradeType(gradeType), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateGradeType(@PathVariable("id") Integer gradeTypeId,
                                                              @RequestBody GradeType gradeType) {
        gradeType.setId(gradeTypeId);
        return new ResponseEntity<>(serviceWrapper.updateGradeType(gradeType), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteGradeType(@PathVariable("id") Integer gradeTypeId) {
        return new ResponseEntity<>(serviceWrapper.deleteGradeType(gradeTypeId), HttpStatus.OK);
    }
}
