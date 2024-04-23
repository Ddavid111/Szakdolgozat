package com.example.proba.controller;

import com.example.proba.entity.Thesis;
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
    public List<Thesis> getThesesList() {
        return thesesService.getThesesList();

    }

    @GetMapping("/getThesesListToDisplay")
    public List<Thesis> getThesesListToDisplay() {
        return thesesService.getThesesListToDisplay();

    }

    @GetMapping("/findThesesById")
    public Thesis findThesesById(Integer id) {
        return thesesService.findThesesById(id);

    }


    @PostMapping("/addTheseses")
    public Thesis addTheseses(@RequestBody Thesis thesis) throws ParseException {

        return thesesService.addTheses(thesis);


    }


    @PutMapping("/updateTheses")
    public Thesis updateTheses(@RequestBody Thesis thesis)
    {
        System.out.println(thesis.toString());
        return thesesService.updateTheseses(thesis);
    }



    @DeleteMapping("/deleteTheses")
    public void deleteTheses(@RequestParam Integer id){
        thesesService.deleteThesesById(id);

    }

    @GetMapping("/findThesesByUserId")
    public List<Thesis> findThesesByUserId(@RequestParam Integer userId)
    {
        return thesesService.findThesesByUserId(userId);
    }




}
