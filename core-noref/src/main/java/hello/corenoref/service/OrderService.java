package hello.corenoref.service;

import hello.corenoref.member.Member;
import hello.corenoref.order.Order;

public interface OrderService {
    Order createOrder(Member member, String itemName, int price);
}
