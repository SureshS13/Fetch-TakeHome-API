package com.sachinsuresh.fetchapidemo.controller;

import com.sachinsuresh.fetchapidemo.entity.Item;
import com.sachinsuresh.fetchapidemo.entity.Receipt;
import com.sachinsuresh.fetchapidemo.service.ItemService;
import com.sachinsuresh.fetchapidemo.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.time.Year;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    public final ReceiptService receiptService;

    @Autowired
    public final ItemService itemService;

    public ReceiptController(ReceiptService receiptService, ItemService itemService){
        this.receiptService = receiptService;
        this.itemService = itemService;
    }

    // Takes in a JSON receipt and returns a JSON object with a new receipt ID
    @PostMapping("/process")
    public ResponseEntity<?> addReceipt(@RequestBody Map<String, Object> payload){
        try {
            // If the payload fails validation, we return error code and message
            if (this.validatePayload(payload) == false) {
                return new ResponseEntity<>("The receipt is invalid", HttpStatus.BAD_REQUEST);
            }

            // Parse the payload to create the receipt
            String retailer = String.valueOf(payload.get("retailer"));
            String purchaseDate = String.valueOf(payload.get("purchaseDate"));
            String purchaseTime = String.valueOf(payload.get("purchaseTime"));
            String totalString = String.valueOf(payload.get("total"));
            double total = Double.valueOf(totalString);

            Receipt receipt = new Receipt(retailer, purchaseDate, purchaseTime, total);

            // Save the receipt first, without the items using the Receipt constructor
            Receipt savedReceipt = this.receiptService.createReceipt(receipt);

            // Parse the given JSON payload to create the items
            List<Item> items = ((List<Map<String, Object>>) payload.get("items")).stream()
                    .map(item -> {
                        String shortDescription = String.valueOf(item.get("shortDescription"));
                        double price = Double.parseDouble((String) item.get("price"));
                        return new Item(shortDescription, price, savedReceipt);
                    })
                    .collect(Collectors.toList());

            // Save each item
            for (Item item : items) {
                this.itemService.createItem(item);
            }

            // Update the receipt with parsed items
            savedReceipt.setItems(items);
            this.receiptService.updateReceipt(savedReceipt);

            return new ResponseEntity<>(savedReceipt.getID().toString(), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>("The receipt is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    // A Getter endpoint that looks up the receipt by the ID and returns an object specifying the points awarded
    @GetMapping(value = "/{id}/points")
    public ResponseEntity<?> getPoints(@PathVariable("id") Long id) {
        try {
            // Checks if receipt exists in DB, otherwise throws and catches an exception, returning a 400 HTTP Bad Request status code
            Receipt receipt = this.receiptService.getReceiptByID(id);

            // Call point calculator method and return points
            int receiptPoints = this.receiptService.calculateReceiptPoints(receipt);
            int itemPoints = this.itemService.calculateItemPoints(receipt.getItems());
            int totalPoints = receiptPoints + itemPoints;

            return new ResponseEntity<>(totalPoints, HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>("No receipt found for that id", HttpStatus.BAD_REQUEST);
        }
    }

    // Validates Payload to make sure all attributes are included and all values are included AND of are correct type
    public boolean validatePayload(Map<String, Object> payload){
        // Check if the payload contains all the expected fields and that they are of the correct type
        if (!payload.containsKey("retailer") || !(payload.get("retailer") instanceof String) ||
                !payload.containsKey("purchaseDate") || !(payload.get("purchaseDate") instanceof String) ||
                !payload.containsKey("purchaseTime") || !(payload.get("purchaseTime") instanceof String) ||
                !payload.containsKey("total") || !(payload.get("total") instanceof String) ||
                !payload.containsKey("items") || !(payload.get("items") instanceof List)) {
            return false;
        }

        try{
            double test = Double.valueOf(String.valueOf((payload.get("total"))));
        } catch (Exception e){
            return false;
        }

        String[] pieces = (String.valueOf(payload.get("purchaseDate"))).split("-");

        if (pieces.length != 3){
            return false;
        }

        int year, month, day;

        try {
            year = Integer.valueOf(pieces[0]);
            month = Integer.valueOf(pieces[1]);
            day = Integer.valueOf(pieces[2]);

            LocalDate date = LocalDate.of(year, month, day);
        } catch (Exception e) {
            return false;
        }

        pieces = ((String.valueOf(payload.get("purchaseTime")))).split(":");

        if (pieces.length != 2){
            return false;
        }

        try {
            LocalTime time = LocalTime.of(Integer.valueOf(pieces[0]), Integer.valueOf(pieces[1]));
        } catch (Exception e) {
            return false;
        }


        // Validate each item in the "items" list
        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");

        for (Map<String, Object> item : items){

            if (!item.containsKey("shortDescription") || !item.containsKey("price") || !(item.get("shortDescription") instanceof String) || !((item.get("price")) instanceof String)){
                return false;
            }

            try{
                double test = Double.valueOf(String.valueOf((item.get("price"))));
            } catch (Exception e){
                return false;
            }
        }

        return true;
    }

}
