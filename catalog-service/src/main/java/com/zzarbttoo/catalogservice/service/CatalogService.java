package com.zzarbttoo.catalogservice.service;

import com.zzarbttoo.catalogservice.jpa.CatalogEntity;

public interface CatalogService {

    Iterable<CatalogEntity> getAllCatalogs();
}
