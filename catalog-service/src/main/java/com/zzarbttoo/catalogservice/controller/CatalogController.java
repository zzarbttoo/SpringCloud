package com.zzarbttoo.catalogservice.controller;

import com.zzarbttoo.catalogservice.jpa.CatalogEntity;
import com.zzarbttoo.catalogservice.service.CatalogService;
import com.zzarbttoo.catalogservice.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service/")
public class CatalogController {

    Environment env;    
    CatalogService catalogService;

    @Autowired
    public CatalogController(Environment env, CatalogService catalogService) {
        this.env = env;
        this.catalogService = catalogService;
    }

    @GetMapping("/health_check")
    public String status(){
        //포트번호를 출력할 수 있도록 변경
        return String.format("It's Working in Catalog Service on Port %s", env.getProperty("local.server.port"));
    }


    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getUsers(){

        Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();


        catalogList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCatalog.class));

        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
