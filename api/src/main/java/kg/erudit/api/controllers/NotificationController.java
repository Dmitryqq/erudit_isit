package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.resp.DefaultServiceResponse;
import kg.erudit.common.resp.GetListResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@Log4j2
public class NotificationController {
    private final ServiceWrapper serviceWrapper;

    public NotificationController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/list")
    public ResponseEntity<GetListResponse> getNotifications(@RequestParam(required = false, defaultValue = "20") Integer limit) {
        return new ResponseEntity<>(serviceWrapper.getNotifications(limit), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultServiceResponse> deleteNotification(@PathVariable("id") String notificationId) {
        return new ResponseEntity<>(serviceWrapper.deleteNotification(notificationId), HttpStatus.OK);
    }
}
