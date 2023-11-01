
package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.ScheduleItemDashboard;
import kg.erudit.common.inner.StudentItem;
import kg.erudit.common.resp.GetListResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/teacher")
@Validated
@Log4j2
public class TeacherDashboardController {
    private final ServiceWrapper serviceWrapper;

    public TeacherDashboardController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<ScheduleItemDashboard>> mainPage(@RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date date) {
        return new ResponseEntity<>(serviceWrapper.getTeacherScheduleDashboard(date), HttpStatus.OK);
    }

    @GetMapping(value = "/diary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<StudentItem>> getDiary(@RequestParam("classId") Integer classId,
                                                                 @RequestParam("subjectId") Integer subjectId) {
        return new ResponseEntity<>(serviceWrapper.getDiaryByClassAndSubjectIds(classId, subjectId), HttpStatus.OK);
    }
}
