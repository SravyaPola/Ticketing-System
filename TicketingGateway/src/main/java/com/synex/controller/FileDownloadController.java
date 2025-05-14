package com.synex.controller;


import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FileDownloadController {

    @Value("${file.base.dir}")
    private String baseDir; 

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String path) throws MalformedURLException {
        if (path.contains("..")) {
            return ResponseEntity.badRequest().build();
        }
        Path fullPath = Paths.get(baseDir, path).normalize();
        Resource resource = new UrlResource(fullPath.toUri());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
    
}
