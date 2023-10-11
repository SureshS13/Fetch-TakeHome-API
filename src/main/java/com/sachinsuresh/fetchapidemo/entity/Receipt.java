package com.sachinsuresh.fetchapidemo.entity;

/*
{
    "retailer": "Target",
    "purchaseDate": "2022-01-02",
    "purchaseTime": "13:13",
    "total": "1.25",
    "items": [
        {"shortDescription": "Pepsi - 12-oz", "price": "1.25"}
    ]
}
 */

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

    // Creates an auto generated ID for each receipt inputted
    @Id
    @GeneratedValue
    private Long ID;

    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    private double total;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<Item> items;
}
