/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.Customer;

import DAL.CartItemDAO;
import DAL.OrderDAO;
import DAL.OrderDetailDAO;
import DAL.ProductDAO;
import DAL.ProductDetailDAO;
import Models.CartItem;
import Models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class UpdateToOrder extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private CartItemDAO cartItemDAO = new CartItemDAO();
    private ProductDetailDAO productDetailDAO = new ProductDetailDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] productDetailIds = request.getParameterValues("productDId");
        String[] quantitys = request.getParameterValues("quantitypt");
        String[] prices = request.getParameterValues("p");
        int orderid = Integer.parseInt(request.getParameter("orderid"));
        HttpSession Session = request.getSession(false);
        User customer = (User) Session.getAttribute("customer");

        try {
            cartItemDAO.updateCartItemStatus("unselected");
        } catch (SQLException ex) {
            Logger.getLogger(UpdateToOrder.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < productDetailIds.length; i++) {
            CartItem cartItem = new CartItem();
            try {
                if (cartItemDAO.getCartItemByProductIdAndCustomerId(Integer.parseInt(productDetailIds[i]), customer.getId()) != null) {

                    cartItem = cartItemDAO.getCartItemByProductIdAndCustomerId(Integer.parseInt(productDetailIds[i]), customer.getId());

                    cartItemDAO.updateCartItemStatus(cartItem.getId(), "selected");
                    if ((Integer.parseInt(quantitys[i])) > productDetailDAO.getProductDetail(Integer.parseInt(productDetailIds[i])).getQuantity()) {
                        cartItemDAO.updateCartItemQuantity(cartItem.getId(), productDetailDAO.getProductDetail(Integer.parseInt(productDetailIds[i])).getQuantity(),
                                productDetailDAO.getProductDetail(Integer.parseInt(productDetailIds[i])).getQuantity() * cartItem.getTotalcost() / cartItem.getQuantity());
                    } else {
                        cartItemDAO.updateCartItemQuantity(cartItem.getId(), Integer.parseInt(quantitys[i]), Double.parseDouble(prices[i]));
                    }

                } else {
                    if (Integer.parseInt(quantitys[i]) > productDetailDAO.getProductDetail(Integer.parseInt(productDetailIds[i])).getQuantity()) {
                        cartItem.setQuantity(productDetailDAO.getProductDetail(Integer.parseInt(productDetailIds[i])).getQuantity());
                    } else {
                        cartItem.setQuantity(Integer.parseInt(quantitys[i]));
                    }
                    cartItem.setCustomer_id(customer.getId());
                    cartItem.setProduct_id(Integer.parseInt(productDetailIds[i]));

                    cartItem.setTotalcost((long) (Double.parseDouble(prices[i])));

                    cartItem.setStatus("selected");

                    try {
                        cartItemDAO.addCartItem(cartItem);
                    } catch (SQLException ex) {
                        Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (SQLException | NumberFormatException ex) {
                Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        
        response.sendRedirect("CartDetail?orderupdateid="+orderid);

    }

}
