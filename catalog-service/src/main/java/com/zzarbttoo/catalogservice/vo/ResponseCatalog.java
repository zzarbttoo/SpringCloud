package com.zzarbttoo.catalogservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //null 값은 반환하지 않는다
public class ResponseCatalog {

    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;


}
