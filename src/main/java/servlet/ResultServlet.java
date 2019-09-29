package servlet;

import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String result = req.getParameter("result");
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        if (result.equals("successAdd")) {
            pageVariables.put("message", "Add client successful");
        }if (result.equals("failedAdd")) {
            pageVariables.put("message", "Client not add");
        }if (result.equals("success")) {
            pageVariables.put("message", "The transaction was successful");
        }if (result.equals("rejected")) {
            pageVariables.put("message", "transaction rejected");
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html",pageVariables));
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
//        pageVariables.put("email", request.getParameter("email"));
//        pageVariables.put("password", request.getParameter("password"));
//        pageVariables.put("pathInfo", request.getPathInfo());
        return pageVariables;
    }
}
