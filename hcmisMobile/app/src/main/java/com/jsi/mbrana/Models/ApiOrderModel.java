package com.jsi.mbrana.Models;

import java.util.ArrayList;

/**
 * Created by pc-6 on 6/15/2016.
 */
public class ApiOrderModel {
    private OrderModel Order;
    private ArrayList<ItemModel> OrderDetail;

    public void setOrder(OrderModel order) {
        Order = order;
    }

    public void setOrderDetail(ArrayList<ItemModel> orderDetail) {
        OrderDetail = orderDetail;
    }

    public OrderModel getOrder() {
        return Order;
    }

    public ArrayList<ItemModel> getOrderDetail() {
        return OrderDetail;
    }
}
