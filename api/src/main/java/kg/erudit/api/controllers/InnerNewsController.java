package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.InnerNews;
import kg.erudit.common.resp.DefaultServiceResponse;
import kg.erudit.common.resp.GetListResponse;
import kg.erudit.common.resp.SingleItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/inner_news")
@Validated
@Log4j2
public class InnerNewsController {
    private final ServiceWrapper serviceWrapper;

    public InnerNewsController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<InnerNews>> getInnerNews() {
        return new ResponseEntity<>(serviceWrapper.getInnerNews(), HttpStatus.OK);
    }

    @PostMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<InnerNews>> addInnerNews(@RequestBody InnerNews news) {
        return new ResponseEntity<>(serviceWrapper.addInnerNews(news), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateInnerNews(@PathVariable("id") Integer newsId,
                                                              @RequestBody InnerNews news) {
        news.setId(newsId);
        return new ResponseEntity<>(serviceWrapper.updateInnerNews(news), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteInnerNews(@PathVariable("id") Integer newsId) throws IOException {
        return new ResponseEntity<>(serviceWrapper.deleteInnerNews(newsId), HttpStatus.OK);
    }
}
