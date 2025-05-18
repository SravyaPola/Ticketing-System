package com.synex.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileDownloadController {

	@GetMapping("/download")
	public ResponseEntity<Resource> download(@RequestParam String path) throws MalformedURLException {
		if (path.contains("..")) {
			return ResponseEntity.badRequest().build();
		}
		Path fullPath = Paths.get(path).normalize();
		Resource resource = new UrlResource(fullPath.toUri());
		if (!resource.exists()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
