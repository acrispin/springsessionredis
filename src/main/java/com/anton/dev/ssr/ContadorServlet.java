package com.anton.dev.ssr;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author anton
 */
@WebServlet(urlPatterns = { "/contadorServlet" })
public class ContadorServlet extends HttpServlet {
    private static final long serialVersionUID = -3450969163801147075L;

    protected static final String CONTADOR = "contador";

    private static final Logger LOG = LoggerFactory.getLogger(ContadorServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        final Integer contador = getContador(request);
        LOG.debug("Se envia como valor de contador {} en la session {} ",
                        contador, request.getSession().getId());
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println(contador);
        } catch (IOException e) {
            LOG.error("Error read outputStram cause {}",e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        final int value = getContador(request) + 1;
        request.getSession().setAttribute(CONTADOR, value);
        LOG.debug("Se guarda como valor de contador {} en la session {} ",
                        value, request.getSession().getId());
    }

    private Integer getContador(HttpServletRequest request) {
        /* JAVA 1.8
        return Optional.ofNullable(request.getSession())
                       .map(session -> (Integer)session.getAttribute(CONTADOR))
                       .orElse(0);
        */
        
        int contador;
        
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(CONTADOR) != null) {
            contador = (Integer) session.getAttribute(CONTADOR);
        } else {
            contador = 0;
        }
        return contador;
    }
}
