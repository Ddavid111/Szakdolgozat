package com.example.proba.controller;

import com.example.proba.entity.Theses;
import com.example.proba.entity.User;
import com.example.proba.service.ThesesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
public class ThesisController {
    @Autowired
    ThesesService thesesService;

    @GetMapping("/getThesesList")
    public List<Theses> getThesesList() {
        return thesesService.getThesesList();

    }

    @GetMapping("/getThesesListToDisplay")
    public List<Theses> getThesesListToDisplay() {
        return thesesService.getThesesListToDisplay();

    }

    @GetMapping("/findThesesById")
    public Optional<Theses> findThesesById(Integer id) {
        return thesesService.findThesesById(id);

    }


    @PostMapping("/addTheseses")
    public Theses addTheseses(@RequestBody Theses theses) throws ParseException {

        return thesesService.addTheses(theses);


    }


    @PutMapping("/updateTheses")
    public Theses updateTheses(@RequestBody Theses theses)
    {
        System.out.println(theses.toString());
        return thesesService.updateTheseses(theses);
    }



    @DeleteMapping("/deleteTheses")
    public void deleteTheses(@RequestParam Integer id){
        thesesService.deleteThesesById(id);

    }




}
