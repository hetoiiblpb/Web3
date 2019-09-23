package dao;

import com.sun.deploy.util.SessionState;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BankClientDAO {

    private static BankClientDAO instance;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    private static Connection connection;

    public static BankClientDAO getInstance(Connection mysqlConnection) {
        if (instance == null) {
            instance = new BankClientDAO(mysqlConnection);
        }
        return instance;
    }


    public List<BankClient> getAllBankClient() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client;");
        ResultSet result = stmt.getResultSet();
        List<BankClient> allClients = new ArrayList<>();
//        result.next();
        while (result.next()) {
            allClients.add(new BankClient(result.getLong("id"),
                    result.getString("name"),
                    result.getString("password"),
                    result.getLong("money")));
        }
        return allClients.stream().collect(Collectors.toList());
    }

    public boolean validateClient(String name, String password) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select * from bank_client where name=?");
        stmt.setString(1,name);
        stmt.executeQuery();
      //  stmt.execute("select * from bank_client where name='" + name + "';");
        ResultSet result = stmt.getResultSet();
        if (result.next()) {
            String pass = result.getString("password");
            result.close();
            stmt.close();
            return pass.equals(password);
        } else {
            result.close();
            stmt.close();
            return false;
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        if (validateClient(name, password)) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE bank.bank_client set money = money + ? where name = ?");
            stmt.setLong(1,transactValue);
            stmt.setString(2,name);
            stmt.executeUpdate();
            stmt.close();
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where id='" + id + "';");
        ResultSet result = stmt.getResultSet();
        return getBankClient(stmt, result);
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        Statement stmt = connection.createStatement();
        if (expectedSum <= 0) {return false;}
        stmt.execute("select * from bank_client where name='" + name + "';");
        ResultSet result = stmt.getResultSet();
        if (result.next()) {
            Long clSum = result.getLong("money");
            result.close();
            stmt.close();
            return (clSum.compareTo(expectedSum) >= 0);
        } else {
            System.out.println("У отправителя мало денег!");
            result.close();
            stmt.close();
            return false;
        }
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "';");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * from bank_client where name=?");
        stmt.setString(1,name);
        stmt.executeQuery();
        ResultSet result = stmt.getResultSet();
        return getBankClient(stmt, result);

    }

    private BankClient getBankClient(Statement stmt, ResultSet result) throws SQLException {
        if (result.next()) {
            BankClient bankClient = new BankClient(
                    result.getLong("id"),
                    result.getString("name"),
                    result.getString("password"),
                    result.getLong("money"));
            System.out.println("DAO getbankClient " + bankClient.getId() + " " + bankClient.getName() + " " + bankClient.getPassword() + " " + bankClient.getMoney());
            result.close();
            stmt.close();
            return bankClient;
        } else {
            result.close();
            stmt.close();
            return null;
        }
    }

    public void addClient(BankClient client) throws SQLException {
        System.out.println("try to add");
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT  INTO bank.bank_client (name, password, money) values(?,?,?)");
        stmt.setString(1, client.getName());
        stmt.setString(2, client.getPassword());
        stmt.setLong(3, client.getMoney());
        stmt.executeUpdate();
        stmt.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment," +
                " name varchar(256)," +
                " password varchar(256)," +
                " money bigint, primary key (id));");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client;");
        stmt.close();
    }

    public boolean deleteClient(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        if (stmt.executeUpdate("DELETE FROM bank_client where name = '" + name + "';") == 1) {
            stmt.close();
            return true;
        } else {
            stmt.close();
            return false;
        }
    }
}
