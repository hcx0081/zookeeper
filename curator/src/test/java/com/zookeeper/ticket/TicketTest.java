package com.zookeeper.ticket;

/**
 * {@code @description:}
 */
public class TicketTest {
    public static void main(String[] args) {
        Ticket12306 ticket12306 = new Ticket12306();
        // 创建客户端
        new Thread(ticket12306, "携程").start();
        new Thread(ticket12306, "飞猪").start();
    }
}