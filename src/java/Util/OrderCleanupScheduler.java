/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author ADMIN
 */
import Controller.WebSocket.OrderUpdateEndpoint;
import DAL.OrderDAO;
import DAL.OrderDetailDAO;
import DAL.ProductDAO;
import DAL.ProductDetailDAO;
import Models.OrderDetail;
import Models.Product;
import Models.ProductDetail;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderCleanupScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final OrderDAO orderDAO = new OrderDAO();

    public void start() {
        final Runnable cleanupTask = new Runnable() {
            @Override
            public void run() {
                try {
                    List<Integer> orders = orderDAO.cancelOverdueOrders();
                    if(!orders.isEmpty()){
                        OrderUpdateEndpoint.sendUpdate("change");
                    }
                    for (int order : orders) {
                        List<OrderDetail> orderDetails = new OrderDetailDAO().getOrderDetailsByOrderId(order);
                        for (OrderDetail orderDetail : orderDetails) {
                            ProductDetail productDetail = new ProductDetailDAO().getProductDetail(orderDetail.getProductdetail_id());
                            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
                            new ProductDetailDAO().updateProductDetail(productDetail);
                            Product product = new ProductDAO().getProductByID(productDetail.getProduct_id());
                            product.setQuantity(product.getQuantity() + orderDetail.getQuantity());
                            new ProductDAO().updateProduct(product);
                        }
                    }

                    orderDAO.updateOrdersSale();

                } catch (SQLException e) {

                }
            }
        };

        // Lên lịch công việc chạy mỗi phút
        scheduler.scheduleAtFixedRate(cleanupTask, 0, 10, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

}
