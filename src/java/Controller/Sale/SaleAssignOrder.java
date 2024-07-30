/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Sale;

import DAL.OrderDAO;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ADMIN
 */
public class SaleAssignOrder extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderID = request.getParameter("selectedOrderId");
        String saleID = request.getParameter("selectedSaleId");
        OrderDAO orderDAO = new OrderDAO();
        try {
            orderDAO.updateOrderSale(Integer.parseInt(orderID), Integer.parseInt(saleID));
        } catch (NumberFormatException e) {
        }
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("res", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObject.toString());
    }

}
