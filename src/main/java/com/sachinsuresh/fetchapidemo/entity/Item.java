package com.sachinsuresh.fetchapidemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue
    private Long ID;

    private String shortDescription;
    private double price;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;
}
