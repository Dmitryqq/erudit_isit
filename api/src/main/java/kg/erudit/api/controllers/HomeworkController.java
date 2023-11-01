package kg.erudit.api.controllers;

import kg.erudit.api.config.IsPwdChangeNotRequired;
import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Homework;
import kg.erudit.common.inner.HomeworkDate;
import kg.erudit.common.resp.GetListResponse;
import kg.erudit.common.resp.SingleItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@IsPwdChangeNotRequired
@RestController
@RequestMapping("/api/v1/homework")
@Validated
@Log4j2
public class HomeworkController {
    private final ServiceWrapper serviceWrapper;

    public HomeworkController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Homework>> addHomework(@RequestBody Homework homework) {
        return new ResponseEntity<>(serviceWrapper.addHomework(homework), HttpStatus.OK);
    }

    @GetMapping(value = "/next_lessons/{scheduleItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<HomeworkDate>> getNextLessonsDates(@PathVariable("scheduleItemId") Integer scheduleItemId) {
        return new ResponseEntity<>(serviceWrapper.getNextLessonsDates(scheduleItemId), HttpStatus.OK);
    }
}
