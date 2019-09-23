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
import java.sql.SQLException;
import java.util.HashMap;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService  bankClientService = BankClientService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NumberFormatException {
        String name = req.getParameter("senderName");
        String password = req.getParameter("senderPassword");
        String nameTo = req.getParameter("nameTo");
        try {
            Long money = Long.parseLong(req.getParameter("count"));
            BankClient sender = bankClientService.getClientByName(name);
            if (bankClientService.sendMoneyToClient(sender, password, nameTo, money)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect("/result?result=success");
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect("/result?result=rejected");
            }
        } catch (NumberFormatException | DBException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.sendRedirect("/result?result=rejected");
        }
    }
}
