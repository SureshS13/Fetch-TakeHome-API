package com.sachinsuresh.fetchapidemo.repository;

import com.sachinsuresh.fetchapidemo.entity.Item;
import com.sachinsuresh.fetchapidemo.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByReceiptID(Long receiptId);

}