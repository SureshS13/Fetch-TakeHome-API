package com.sachinsuresh.fetchapidemo.service;


import java.util.List;
import com.sachinsuresh.fetchapidemo.entity.Item;
import com.sachinsuresh.fetchapidemo.entity.Receipt;
import com.sachinsuresh.fetchapidemo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }


    public Item createItem(Item item){
        return this.itemRepository.save(item);
    }

    // If an item with a matching ID is found, we return it, otherwise throw an exception
    public Item getItemByID(Long ID) throws IllegalArgumentException{
        return this.itemRepository.findById(ID).orElseThrow(() -> new IllegalArgumentException("Item not found with ID: " + ID));
    }

    // Get all items associated with a particular Receipt ID
    public List<Item> getAllItemsByReceipt(Long receiptId) throws IllegalArgumentException{
        List<Item> items = this.itemRepository.findByReceiptID(receiptId);

        if (items.isEmpty()){
            throw new IllegalArgumentException("No items were found with receiptId " + receiptId);
        }

        return items;
    }


    // Updates an existing item with new information. If no associated item with the given item is found, throw an exception
    public Item updateItem(Item item) throws IllegalArgumentException{
        Item oldItem = new Item();

        oldItem = this.itemRepository.findById(item.getID()).orElseThrow(() -> new IllegalArgumentException("Item not found with ID: " + item.getID()));

        oldItem.setShortDescription(item.getShortDescription());
        oldItem.setPrice(item.getPrice());
        oldItem.setReceipt(item.getReceipt());

        return oldItem;
    }

    // Method to calculate the points for items in a receipt, and return the total
    public int calculateItemPoints(List<Item> items){
        int points = 0;

        // 5 points for every two items on the receipt
        if (items.size() % 2 == 0){
            points += (items.size() / 2) * 5;
        } else {
            points += ((items.size() - 1) / 2) * 5;
        }

        // If the trimmed length of the item description is a multiple of 3,
        // multiply the price by 0.2 and round up to the nearest integer.
        // The result is the number of points earned.
        for (Item item : items){
            String tempDescription = item.getShortDescription().trim();

            if (tempDescription.length() % 3 == 0) {
                double price = item.getPrice() * 0.2;
                points += (int) Math.ceil(price);
            }
        }

        return points;
    }
}
