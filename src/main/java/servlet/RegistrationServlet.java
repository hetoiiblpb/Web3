package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class RegistrationServlet extends HttpServlet {
    BankClientService  bankClientService = BankClientService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        try {Long money = Long.parseLong(req.getParameter("money"));
            if (bankClientService.addClient(new BankClient(name,password,money))){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect("/result?result=successAdd");
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect("/result?result=failedAdd");
            }

        } catch (DBException | NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.sendRedirect("/result?result=failedAdd");
        }


    }
}
