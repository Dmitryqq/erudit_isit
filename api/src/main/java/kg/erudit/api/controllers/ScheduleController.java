package kg.erudit.api.controllers;

import jakarta.annotation.security.RolesAllowed;
import kg.erudit.api.config.IsPwdChangeNotRequired;
import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.exceptions.FillScheduleException;
import kg.erudit.common.inner.*;
import kg.erudit.common.resp.DefaultServiceResponse;
import kg.erudit.common.resp.GetListResponse;
import kg.erudit.common.resp.SingleItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@IsPwdChangeNotRequired
@RestController
@RequestMapping("/api/v1/schedules")
@Validated
@Log4j2
public class ScheduleController {
    private final ServiceWrapper serviceWrapper;

    public ScheduleController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<ScheduleCompleted>> getSchedule(@RequestParam Integer classId, @RequestParam Integer trimesterId,
                                                              @RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date fromDate,
                                                             @RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date toDate) {
        ScheduleCompleted schedule = new ScheduleCompleted(classId, trimesterId);
        return new ResponseEntity<>(serviceWrapper.getSchedule(schedule, fromDate, toDate), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping(value = "/individual", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<ScheduleCompleted>> getIndividualSchedule(@RequestParam Integer studentId, @RequestParam Integer trimesterId,
                                                                             @RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date fromDate,
                                                                             @RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date toDate) {
        ScheduleCompleted schedule = new ScheduleCompleted();
        schedule.setStudentId(studentId);
        schedule.setTrimesterId(trimesterId);
        return new ResponseEntity<>(serviceWrapper.getIndividualSchedule(schedule, fromDate, toDate), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Schedule>> addSchedule(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(serviceWrapper.addSchedule(schedule), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/fill",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleDay>> fillSchedule(@PathVariable("id") Integer scheduleId,
                                                                     @RequestBody List<ScheduleDayBase> scheduleDayList) throws FillScheduleException {
        return new ResponseEntity<>(serviceWrapper.fillSchedule(scheduleId, scheduleDayList), HttpStatus.OK);
    }

    @PutMapping(value = "/day",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateDay(@RequestBody ScheduleDay scheduleDay) {
        return new ResponseEntity<>(serviceWrapper.updateDay(scheduleDay), HttpStatus.OK);
    }

    @PutMapping(value = "/individual/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateIndividualDay(@PathVariable("id") Integer scheduleId, @RequestBody ScheduleDay scheduleDay) {
        return new ResponseEntity<>(serviceWrapper.updateIndividualDay(scheduleId, scheduleDay), HttpStatus.OK);
    }

    @GetMapping(value = "/item_types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleItemType>> getScheduleItemTypes() {
        return new ResponseEntity<>(serviceWrapper.getScheduleItemTypes(), HttpStatus.OK);
    }

    @GetMapping(value = "/{classId}/subject_teachers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<SubjectTeacher>> getSubjectTeachers(@PathVariable("classId") Integer classId) {
        return new ResponseEntity<>(serviceWrapper.getSubjectTeachersForClass(classId), HttpStatus.OK);
    }

    @RolesAllowed("STUDENT")
    @GetMapping(value = "/template", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleItem>> getScheduleItemTemplate() {
        return new ResponseEntity<>(serviceWrapper.getScheduleItemTemplate(), HttpStatus.OK);
    }

    @GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<GetListResponse<StudentItem>> getScheduleItemById(@PathVariable("scheduleItemId") Integer scheduleItemId) {
    public ResponseEntity<GetListResponse<ClassItem>> getScheduleItemByIds(@RequestParam("ids") String scheduleItemIds) {
        return new ResponseEntity<>(serviceWrapper.getScheduleItemByIds(scheduleItemIds), HttpStatus.OK);
    }

    @PutMapping(value = "/item/{scheduleItemId}/visit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> setStudentVisit(@PathVariable("scheduleItemId") Integer scheduleItemId,
                                                                        @RequestParam Integer studentId) {
        return new ResponseEntity<>(serviceWrapper.setStudentVisit(scheduleItemId, studentId), HttpStatus.OK);
    }

    @PutMapping(value = "/item/{scheduleItemId}/unvisit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> setStudentUnvisit(@PathVariable("scheduleItemId") Integer scheduleItemId,
                                                                  @RequestParam Integer studentId) {
        return new ResponseEntity<>(serviceWrapper.setStudentUnvisit(scheduleItemId, studentId), HttpStatus.OK);
    }
}
