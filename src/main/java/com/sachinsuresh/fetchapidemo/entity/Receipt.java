package com.sachinsuresh.fetchapidemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Receipt {

    @Id
    @GeneratedValue
    private Long ID;

    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    private double total;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<Item> items;

    public Receipt(String retailer, String purchaseDate, String purchaseTime, double total){
        this.retailer = retailer;
        this.purchaseDate = purchaseDate;
        this.purchaseTime = purchaseTime;
        this.total = total;
    }
}
