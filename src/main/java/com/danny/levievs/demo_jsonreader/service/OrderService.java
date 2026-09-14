package com.danny.levievs.demo_jsonreader.service;

import com.danny.levievs.demo_jsonreader.model.OrderItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.danny.levievs.demo_jsonreader.model.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ObjectMapper jacksonMapper;

    public List<Order> loadDataFromFile() {

        List<Order> orders;
        long startTime = System.currentTimeMillis();

        Resource resource = new ClassPathResource("orders.json");

        try(InputStream inputStream = resource.getInputStream()) {
            orders = jacksonMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    });

        } catch (IOException e) {
            log.error("Error reading orders.json file. Details: {}", e.getMessage());
            return Collections.emptyList();
        }
        log.info("{} orders loaded successfully. Total load time {}", orders.size(),  System.currentTimeMillis() - startTime + "msec.");
        return orders;
    }


    public void printFirstCustomersOrderSize(List<Order> orders) {
        if(CollectionUtils.isEmpty(orders)) {
            System.out.println("No orders found.");
        }

        Order firstOrder = orders.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if(Objects.isNull(firstOrder)) {
            System.out.println("No valid orders found");
        } else {
            System.out.println(orders.getFirst().customer_name() + "has " + orders.getFirst().order_items().size());
        }
    }

    public Map<String, Double> getTotalSpendByCustomer(List<Order> orders) {
        if(Objects.isNull(orders)) {
            System.out.println("No orders found.");
            return Map.of();
        }

        Map<String, Double> customersSpends = new HashMap<>();


        orders.forEach(order -> {
            Double sumSpend = order.order_items()
                    .stream()
                    .mapToDouble(OrderItem::unit_price)
                    .sum();

            customersSpends.merge(order.customer_name(), sumSpend, Double::sum);
        });

        customersSpends.forEach((s, totalSpend) -> System.out.println(s + " " + totalSpend));
        return customersSpends;
    }


    public Map<String, OrderItem> getMostExpensiveItemByCustomer(List<Order> orders) {
        if (Objects.isNull(orders)) {
            System.out.println("No orders found.");
            return Map.of();
        }

        Map<String, OrderItem> customerMaxOrderItem = new HashMap<>();
        orders.forEach(order ->
            order.order_items()
                    .stream()
                    .max(Comparator.comparingDouble(OrderItem::unit_price))
                    .ifPresent(maxOrder ->
                        customerMaxOrderItem.merge(order.customer_name(), maxOrder, (a, b) -> {
                            if(a.unit_price() > b.unit_price()) return a;
                            else return b;
                        })));
        customerMaxOrderItem.forEach((s, totalSpend) -> System.out.println(s + " " + totalSpend));

        return customerMaxOrderItem;
    }

    public void saveOrdersToFile(List<Order> orders, String filePath) {

        if (Objects.isNull(orders)) {
            log.warn("No orders to write — skipping file output.");
            return;
        }

        try {
            jacksonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(filePath), orders);
            log.info("Wrote {} orders to {}", orders.size(), filePath);
        } catch (IOException e) {
            log.error("Error writing orders to file {}", filePath, e);
        }
    }
}
