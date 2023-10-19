package kg.erudit.api.controllers;

import jakarta.annotation.security.RolesAllowed;
import kg.erudit.api.config.IsPwdChangeNotRequired;
import kg.erudit.api.service.ServiceWrapper;
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

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Schedule>> addSchedule(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(serviceWrapper.addSchedule(schedule), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/fill",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleDay>> fillSchedule(@PathVariable("id") Integer scheduleId,
                                                                     @RequestBody List<ScheduleDayBase> scheduleDayList) {
        return new ResponseEntity<>(serviceWrapper.fillSchedule(scheduleId, scheduleDayList), HttpStatus.OK);
    }

    @PutMapping(value = "/day/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateDay(@PathVariable("id") Integer dayId,
                                                           @RequestBody ScheduleDay scheduleDay) {
        scheduleDay.setId(dayId);
        return new ResponseEntity<>(serviceWrapper.updateDay(scheduleDay), HttpStatus.OK);
    }

    @GetMapping(value = "/item_types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleItemType>> getScheduleItemTypes() {
        return new ResponseEntity<>(serviceWrapper.getScheduleItemTypes(), HttpStatus.OK);
    }

    @RolesAllowed("STUDENT")
    @GetMapping(value = "/template", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleItem>> getScheduleItemTemplate() {
        return new ResponseEntity<>(serviceWrapper.getScheduleItemTemplate(), HttpStatus.OK);
    }
}
