package com.sachinsuresh.fetchapidemo.service;

import com.sachinsuresh.fetchapidemo.entity.Receipt;
import com.sachinsuresh.fetchapidemo.repository.ReceiptRepository;
import com.sun.jdi.InconsistentDebugInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Map;
import java.util.List;

@Service
public class ReceiptService {

    @Autowired
    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository){
        this.receiptRepository = receiptRepository;
    }

    public Receipt createReceipt(Receipt receipt){
        return this.receiptRepository.save(receipt);
    }

    // If a receipt with a matching ID is found, we return it, otherwise throw an exception
    public Receipt getReceiptByID(Long ID) throws IllegalArgumentException{
        return this.receiptRepository.findById(ID).orElseThrow(() -> new IllegalArgumentException("Receipt not found with ID: " + ID));
    }

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


    // Method to calculate the points for a receipt (not items), and return the total
    public int calculateReceiptPoints(Receipt receipt){
        int points = 0;

        // One point for every alphanumeric character in the retailer name
        for (char i : receipt.getRetailer().toCharArray()){
            if (Character.isLetterOrDigit(i)){
                points++;
            }
        }

        // 50 points if the total is a round dollar amount with no cents
        if (Math.floor(receipt.getTotal()) == receipt.getTotal()) {
            points += 50;
        }

        // 25 points if the total is a multiple of 0.25
        if (receipt.getTotal() % 0.25 == 0){
            points += 25;
        }

        // 6 points if the day in the purchase date is odd
        String[] pieces = receipt.getPurchaseDate().split("-");
        int date = 0;

        if (pieces[2].charAt(0) == '0'){
            date  = Integer.valueOf(pieces[2].charAt(1));

            if (date % 2 != 0){
                points += 6;
            }
        } else {
            date  = Integer.valueOf(pieces[2]);

            if (date % 2 != 0){
                points += 6;
            }
        }

        // 10 points if the time of purchase is after 2:00pm and before 4:00pm (24 Hour Time)
        pieces = receipt.getPurchaseTime().split(":");

        LocalTime time = LocalTime.of(Integer.valueOf(pieces[0]), Integer.valueOf(pieces[1]));
        LocalTime startTime = LocalTime.of(14, 0); // 2:00 PM
        LocalTime endTime = LocalTime.of(16, 0);   // 4:00 PM

        if (time.isAfter(startTime) && time.isBefore(endTime)) {
            points += 10;
        }

        return points;
    }
}
