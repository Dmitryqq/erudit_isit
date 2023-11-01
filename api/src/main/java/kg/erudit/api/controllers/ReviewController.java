package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.Review;
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
@RequestMapping("/api/v1/reviews")
@Validated
@Log4j2
public class ReviewController {
    private final ServiceWrapper serviceWrapper;

    public ReviewController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetListResponse<Review>> getReviews() throws IOException {
        return new ResponseEntity<>(serviceWrapper.getReviews(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Review>> getReview(@PathVariable("id") Integer reviewId) throws IOException {
        return new ResponseEntity<>(serviceWrapper.getReview(reviewId), HttpStatus.OK);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Review>> addReview(@RequestBody Review review) {
        return new ResponseEntity<>(serviceWrapper.addReview(review), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> updateReview(@PathVariable("id") Integer reviewId,
                                                              @RequestBody Review review) {
        review.setId(reviewId);
        return new ResponseEntity<>(serviceWrapper.updateReview(review), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteReview(@PathVariable("id") Integer reviewId) throws IOException {
        return new ResponseEntity<>(serviceWrapper.deleteReview(reviewId), HttpStatus.OK);
    }
}
