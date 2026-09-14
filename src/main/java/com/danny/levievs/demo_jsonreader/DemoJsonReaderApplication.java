package com.danny.levievs.demo_jsonreader;

import com.danny.levievs.demo_jsonreader.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.danny.levievs.demo_jsonreader.model.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class DemoJsonReaderApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoJsonReaderApplication.class, args);

        OrderService orderService = context.getBean(OrderService.class);

        List<Order> orders = orderService.loadDataFromFile();

        orderService.printFirstCustomersOrderSize(orders);

        orderService.getTotalSpendByCustomer(orders);

        orderService.getMostExpensiveItemByCustomer(orders);

        orderService.saveOrdersToFile(orders, "src/main/resources/orders-export-" + LocalDate.now() + ".json");
    }
}
