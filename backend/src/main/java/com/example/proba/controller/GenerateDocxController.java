package com.example.proba.controller;

import com.example.proba.service.GenerateDocxService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestController
public class GenerateDocxController {

    @Autowired
    GenerateDocxService generateDocxService;


    @PostMapping("/generateDocx")
    public void generateDocx(@RequestBody Object object) {

        generateDocxService.Proba(object);
    }










}
