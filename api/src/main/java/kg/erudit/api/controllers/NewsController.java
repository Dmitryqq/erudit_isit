package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.News;
import kg.erudit.common.inner.NewsSingle;
import kg.erudit.common.inner.NewsType;
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
@RequestMapping("/api/v1/news")
@Validated
@Log4j2
public class NewsController {
    private final ServiceWrapper serviceWrapper;

    public NewsController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<News>> getNews() throws IOException {
        return new ResponseEntity<>(serviceWrapper.getNews(1), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<NewsSingle>> getSingleNewsItem(@PathVariable Integer id) throws IOException {
        return new ResponseEntity<>(serviceWrapper.getNewsSingleItem(id), HttpStatus.OK);
    }

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<NewsType>> getNewsType() {
        return new ResponseEntity<>(serviceWrapper.getNewsType(), HttpStatus.OK);
    }

    @PostMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<NewsSingle>> addNews(@RequestBody NewsSingle news) {
        return new ResponseEntity<>(serviceWrapper.addNews(news), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateNews(@PathVariable("id") Integer newsId,
                                                              @RequestBody NewsSingle newsSingle) throws IOException {
        newsSingle.setId(newsId);
        return new ResponseEntity<>(serviceWrapper.updateNews(newsSingle), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteNews(@PathVariable("id") Integer newsId) throws IOException {
        return new ResponseEntity<>(serviceWrapper.deleteNews(newsId), HttpStatus.OK);
    }
}
