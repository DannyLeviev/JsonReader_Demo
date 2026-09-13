package com.danny.levievs.demo_jsonreader.model;

import java.util.List;

public record Order(Long order_id, Long customer_id, String customer_name, Integer loyalty_points, List<OrderItem> order_items){
}
