package com.example.proba.controller;

import com.example.proba.entity.Theses;
import com.example.proba.service.ThesesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class ThesisController {
    @Autowired
    ThesesService thesesService;

    @GetMapping("/getThesesList")
    public List<Object> getThesesList() {
        return thesesService.getThesesList();

    }


    @PostMapping("/addTheseses")
    public Theses addTheseses(@RequestBody Theses theses) throws ParseException {

        return thesesService.addTheses(theses);


    }


    @PutMapping("/updateTheses")
    public Theses updateTheses(@RequestBody Theses theses)
    {

        return thesesService.updateTheseses(theses);
    }



    @DeleteMapping("/deleteTheses")
    public void deleteTheses(@RequestParam Integer id){
        thesesService.deleteThesesById(id);

    }


}
