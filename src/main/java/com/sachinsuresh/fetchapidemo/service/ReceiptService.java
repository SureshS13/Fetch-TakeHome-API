package com.sachinsuresh.fetchapidemo.service;

import com.sachinsuresh.fetchapidemo.entity.Receipt;
import com.sachinsuresh.fetchapidemo.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public Receipt createReceipt(Receipt receipt){
        return this.receiptRepository.save(receipt);
    }

    // If a receipt with a matching ID is found, we return it, otherwise throw an exception
    public Receipt getReceiptByID(Long ID) throws IllegalArgumentException{
        return this.receiptRepository.findById(ID).orElseThrow(() -> new IllegalArgumentException("Receipt not found with ID: " + ID));
    }

    // WHEN YOU RETURN 16:00 IN THE VIDEO, YOU NEED TO ADD STUFF TO ITEM REPO AS WELL. AND CONTINUE
    // If a receipt is found with this ID, then we update it an save it to the DB, otherwise we return a new blank Receipt object
    public Receipt updateReceipt(Receipt reciept) throws IllegalArgumentException{
        Receipt oldReceipt = new Receipt();

        oldReceipt = this.receiptRepository.findById(reciept.getID()).orElseThrow(() -> new IllegalArgumentException("Receipt not found with ID: " + reciept.getID()));


        oldReceipt.setRetailer(reciept.getRetailer());
        oldReceipt.setPurchaseDate(reciept.getPurchaseDate());
        oldReceipt.setPurchaseTime(reciept.getPurchaseTime());
        oldReceipt.setTotal(reciept.getTotal());
        oldReceipt.setItems(oldReceipt.getItems());
        this.receiptRepository.save(oldReceipt);

        return oldReceipt;
    }
}
