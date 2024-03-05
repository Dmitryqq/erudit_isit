package kg.erudit.api.controllers;

import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.File;
import kg.erudit.common.inner.Material;
import kg.erudit.common.resp.DefaultServiceResponse;
import kg.erudit.common.resp.SingleItemResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/material")
@Validated
@Log4j2
public class MaterialsController {
    private final ServiceWrapper serviceWrapper;

    public MaterialsController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping(value = "/file/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<File>> getMaterialFile(@PathVariable Integer id) throws IOException {
        return new ResponseEntity<>(serviceWrapper.getMaterialFile(id), HttpStatus.OK);
    }

    @PostMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleItemResponse<Material>> addMaterial(@RequestBody Material material) {
        return new ResponseEntity<>(serviceWrapper.addMaterial(material), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultServiceResponse> deleteMaterial(@PathVariable("id") Integer materialId) {
        return new ResponseEntity<>(serviceWrapper.deleteMaterial(materialId), HttpStatus.OK);
    }
}
