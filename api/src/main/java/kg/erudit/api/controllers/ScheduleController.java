package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Class;
import kg.erudit.common.inner.*;
import kg.erudit.common.resp.GetListResponse;
import kg.erudit.common.resp.SingleItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@Validated
@Log4j2
public class ScheduleController {
    private final ServiceWrapper serviceWrapper;

    public ScheduleController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<Class>> getClasses() {
        return new ResponseEntity<>(serviceWrapper.getClasses(), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Schedule>> addSchedule(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(serviceWrapper.addSchedule(schedule), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/fill",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleDay>> fillSchedule(@PathVariable("id") Integer scheduleId,
                                                                     @RequestBody List<ScheduleDayBase> scheduleDayList) {
        return new ResponseEntity<>(serviceWrapper.fillSchedule(scheduleId, scheduleDayList), HttpStatus.OK);
    }

    @GetMapping(value = "/item_types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleItemType>> getScheduleItemTypes() {
        return new ResponseEntity<>(serviceWrapper.getScheduleItemTypes(), HttpStatus.OK);
    }

    @GetMapping(value = "/template", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleItem>> getScheduleItemTemplate() {
        return new ResponseEntity<>(serviceWrapper.getScheduleItemTemplate(), HttpStatus.OK);
    }

//    @PostMapping (value = "/classes", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<SingleItemResponse<Class>> addClass(@RequestBody Class clazz) {
//        return new ResponseEntity<>(serviceWrapper.addClass(clazz), HttpStatus.OK);
//    }
//
//    @PutMapping(value = "/classes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<DefaultServiceResponse> updateClass(@PathParam("id") Integer classId,
//                                                              @RequestBody Class clazz) {
//        clazz.setId(classId);
//        return new ResponseEntity<>(serviceWrapper.updateClass(clazz), HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/classes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<DefaultServiceResponse> deleteClass(@PathParam("id") Integer classId) {
//        return new ResponseEntity<>(serviceWrapper.deleteClass(classId), HttpStatus.OK);
//    }
}
