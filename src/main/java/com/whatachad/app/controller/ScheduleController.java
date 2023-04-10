package com.whatachad.app.controller;

import com.whatachad.app.api.ScheduleApi;
import com.whatachad.app.model.dto.ScheduleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ScheduleController implements ScheduleApi {

    @Override
    public ResponseEntity<ScheduleDto> getSchedule() {
        return null; //new ResponseEntity<>(new ScheduleDto("schedule"), HttpStatus.OK);
    }

}
