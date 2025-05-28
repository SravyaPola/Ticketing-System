package com.synex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.synex.service.EmailService;

@RestController
@RequestMapping("/ticket")
public class EmailController {

    private final EmailService service;

    @Autowired
    public EmailController(EmailService service) {
        this.service = service;
    }

//    @GetMapping("/sendEventPdf/{id}")
//    public ResponseEntity<String> sendEventPdf(@PathVariable Long id) {
//        try {
//            service.generatePdfAndSendEmail(id);
//            return ResponseEntity.ok("PDF generated and email sent!");
//        } catch (Exception e) {
//            return ResponseEntity.status(500)
//                                 .body("Error: " + e.getMessage());
//        }
//    }
}