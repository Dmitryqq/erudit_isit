package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.News;
import kg.erudit.common.inner.NewsSingle;
import kg.erudit.common.resp.GetListResponse;
import kg.erudit.common.resp.HomePageResponse;
import kg.erudit.common.resp.SingleItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/landing")
@Validated
@Log4j2
public class HomePageController {
    private final ServiceWrapper serviceWrapper;

    public HomePageController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HomePageResponse> home() throws IOException {
        return new ResponseEntity<>(serviceWrapper.getHomePage(), HttpStatus.OK);
    }

    @GetMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<News>> getAllNews() throws IOException {
        return new ResponseEntity<>(serviceWrapper.getNews(1), HttpStatus.OK);
    }

    @GetMapping(value = "/news/{url}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<NewsSingle>> getSingleNewsItem(@PathVariable String url) throws IOException {
        return new ResponseEntity<>(serviceWrapper.getNewsSingleItem(url), HttpStatus.OK);
    }
}
