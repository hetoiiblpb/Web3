package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    private static BankClientService instance;

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) throws DBException {
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<BankClient> getAllClient() throws DBException {
        try {
            return getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean deleteClient(String name) throws DBException {
        try {
            return getBankClientDAO().deleteClient(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean addClient(BankClient client) throws DBException {
        try {
            BankClient bankClient = getBankClientDAO().getClientByName(client.getName());
            if (bankClient == null) {
                getBankClientDAO().addClient(client);
                return true;
            } else { return false;}
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean sendMoneyToClient(BankClient sender, String password, String name, Long value) throws SQLException {
        BankClientDAO dao = getBankClientDAO();
        String senderName = sender.getName();
        String senderPass = sender.getPassword();
        if ((dao.isClientHasSum(senderName,value) & (dao.validateClient(senderName,password)))){
            dao.updateClientsMoney(senderName,senderPass,value*(-1));
            dao.updateClientsMoney(name,dao.getClientByName(name).getPassword(),value);
            return true;
        } else {
            return false;
        }

    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static BankClientService getInstance(){
        if(instance==null){
            instance=new BankClientService();
        }
        return instance;
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("127.0.0.1:").           //host name
                    append("3306/").                //port
                    append("bank?serverTimezone=UTC&").          //db name
                    append("user=root&").          //login
                    append("password=Qwerty3366");       //password

            System.out.println("URL: " + url.toString() + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return BankClientDAO.getInstance(getMysqlConnection());
    }
}
