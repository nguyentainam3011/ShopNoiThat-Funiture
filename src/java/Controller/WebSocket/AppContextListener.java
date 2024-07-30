/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller.WebSocket;

import Util.OrderCleanupScheduler;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    private OrderCleanupScheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = new OrderCleanupScheduler();
        scheduler.start();
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.stop();
            
        }
    }
}



