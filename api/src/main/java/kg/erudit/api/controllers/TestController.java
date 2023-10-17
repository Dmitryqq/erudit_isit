//package kg.erudit.api.controllers;
//
//import jakarta.activation.FileTypeMap;
//import jakarta.servlet.http.HttpServletRequest;
//import kg.erudit.api.service.ServiceWrapper;
//import kg.erudit.api.util.FileUtil;
//import kg.erudit.common.inner.Trimester;
//import kg.erudit.common.resp.GetListResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//@RestController
////@RequestMapping("/images")
//@Validated
////@Log4j2
//public class TestController {
//    @Autowired
//    private FileUtil fileUtil;
//    private final ServiceWrapper serviceWrapper;
//
//    public TestController(ServiceWrapper serviceWrapper) {
//        this.serviceWrapper = serviceWrapper;
//    }
//
//
//    @GetMapping(value = "/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] getImage(HttpServletRequest request) throws IOException {
//        String path = request.getRequestURI().split(request.getContextPath() + "/images/")[1];
//        File file = fileUtil.getFile(path);
//        return Files.readAllBytes(file.toPath());
////        File img = new File("src/main/resources/static/test.jpg");
////        return ResponseEntity.ok()
////                .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file)))
////                .body(Files.readAllBytes(file.toPath()));
//    }
//}
