package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Diary;
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
@RequestMapping("/api/v1/diary")
@Validated
@Log4j2
public class DiaryController {
    private final ServiceWrapper serviceWrapper;

    public DiaryController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<Diary>> getDiary(@RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date fromDate,
                                                          @RequestParam @DateTimeFormat(pattern="dd.MM.yyyy") Date toDate) {
        return new ResponseEntity<>(serviceWrapper.getDiary(fromDate, toDate), HttpStatus.OK);
    }
}
