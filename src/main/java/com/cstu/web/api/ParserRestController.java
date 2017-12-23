package com.cstu.web.api;

import com.cstu.service.ParserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api", produces={"application/json; charset=windows-1251"})
public class ParserRestController {

    private final ParserService parserService;

    @Inject
    public ParserRestController(ParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping("/parser/{group}/{sdate}/{edate}")
    public ResponseEntity<?> getSchedule (@PathVariable String group,
                                                          @PathVariable String sdate,
                                                          @PathVariable String edate) {
        return ResponseEntity.ok()
                .body(parserService
                        .getSchedule(group, sdate, edate));
    }
}
