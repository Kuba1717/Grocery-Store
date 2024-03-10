package com.example.sklep.controller.command;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class AddProductCommand implements Command {
    private final Product product;
    private final Timer timer;

    public AddProductCommand(Product product, long delayInSeconds) {
        this.product = product;
        product.setDeliveryTime(LocalDateTime.now().plus(delayInSeconds, ChronoUnit.SECONDS));
        this.timer = new Timer();
        timer.schedule(new AddProductTask(), delayInSeconds  * 1000);
    }

    @Override
    public void execute() {
        DBUtils.addProduct(product);
        System.out.println("Product added to the database: " + product.getProductName());
        timer.cancel();
    }

    @Override
    public void cancel() {
        timer.cancel();
        System.out.println("Product addition canceled: " + product.getProductName());
    }

    private class AddProductTask extends TimerTask {
        @Override
        public void run() {
            execute();
        }
    }
}

