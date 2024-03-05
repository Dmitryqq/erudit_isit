package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Event;
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
@RequestMapping("/api/v1/events")
@Validated
@Log4j2
public class EventsController {
    private final ServiceWrapper serviceWrapper;

    public EventsController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<Event>> getEvents() {
        return new ResponseEntity<>(serviceWrapper.getEvents(), HttpStatus.OK);
    }

    @PostMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Event>> addEvent(@RequestBody Event event) {
        return new ResponseEntity<>(serviceWrapper.addEvent(event), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateEvent(@PathVariable("id") Integer eventId,
                                                              @RequestBody Event event) {
        event.setId(eventId);
        return new ResponseEntity<>(serviceWrapper.updateEvent(event), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteEvent(@PathVariable("id") Integer eventId) {
        return new ResponseEntity<>(serviceWrapper.deleteEvent(eventId), HttpStatus.OK);
    }
}
