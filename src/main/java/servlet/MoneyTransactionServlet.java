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
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NumberFormatException {
        resp.setContentType("text/html;charset=utf-8");
        System.out.println(req.getParameterMap());
        String name = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        System.out.println(req.getParameter("senderPass"));
        String nameTo = req.getParameter("nameTo");
        try {
            Long money = Long.parseLong(req.getParameter("count"));
            BankClient sender = bankClientService.getClientByName(name);
            System.out.println(senderPass + " - " + sender.getPassword());
            if (senderPass.equals(sender.getPassword())) {
                System.out.println("ПАРОЛЬ ПРАВИЛЬНЫЙ");
                if (bankClientService.sendMoneyToClient(sender, nameTo, money)) {
                    System.out.println("переводим бабки");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.sendRedirect("/result?result=success");
                } else {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.sendRedirect("/result?result=rejected");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect("/result?result=rejected");
                System.out.println("Не прошел проверку пароля");
            }
        } catch (NumberFormatException | DBException | SQLException e) {
            System.out.println("Ушло в кэтч~~~~~~~~~~~~~~~~~~");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.sendRedirect("/result?result=rejected");
        }
    }
}
