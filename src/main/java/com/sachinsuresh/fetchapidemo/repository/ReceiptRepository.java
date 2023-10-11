package com.sachinsuresh.fetchapidemo.repository;

import com.sachinsuresh.fetchapidemo.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
