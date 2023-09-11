package ru.viz.clinic.config;

import com.vaadin.flow.server.VaadinServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;


@WebServlet(value = "/*", asyncSupported = true)
public class MyVaadinServlet extends VaadinServlet {
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
//        getService().addSessionInitListener(event -> {
//            event.getSession().addRequestHandler(new HttpsForwardingHandler());
//        });
    }
}