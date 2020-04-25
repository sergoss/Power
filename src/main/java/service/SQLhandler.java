package service;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLhandler {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static PreparedStatement preparedStatement;

    public static void setConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:power.db");
    }

    public static void createStatement() throws SQLException{
        statement = connection.createStatement();
    }

    public static void closeConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static User findUserByChatId(long chatId) throws SQLException {
        resultSet = statement.executeQuery("SELECT chatId, stateId, notify FROM user WHERE chatId = " + chatId);
//        User(String nick, Long chatId, Integer stateId, Boolean notify)
        User user = null;
        while (resultSet.next()) {
            if (chatId == resultSet.getLong(1)) {
                user = new User (resultSet.getLong(1), resultSet.getInt(2), resultSet.getBoolean(3));
                break;
            }
        }
        return user;
    }

    public static void addNewUser(User user) throws SQLException {
        if (user.equals(findUserByChatId(user.getChatId()))) return;
        long chatId = user.getChatId();
        int stateId = user.getStateId();
        boolean notify = user.getNotify();
        String query = "INSERT INTO user (chatId, stateId, notify) VALUES ((?), (?), (?))";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, chatId);
        preparedStatement.setInt(2, stateId);
        preparedStatement.setBoolean(3, notify);
//        resultSet = preparedStatement.executeQuery();
//        if (resultSet.next()) return;
        preparedStatement.executeUpdate();
    }

    public static void updateUser(User user) throws SQLException {
        preparedStatement = connection.prepareStatement("UPDATE user SET stateId = ? WHERE chatId = " + user.getChatId());
        preparedStatement.setInt(1, user.getStateId());
        preparedStatement.executeUpdate();
    }




//    private String check(String first_name, String last_name, int user_id, String username) {
//        try {
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
//        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
//        MongoClient mongoClient = new MongoClient(connectionString);
//        MongoDatabase database = mongoClient.getDatabase("db_name");
//        MongoCollection<Document> collection = database.getCollection("users");
//        long found = collection.count(Document.parse("{id : " + Integer.toString(user_id) + "}"));
//
//        if (found == 0) {
//            Document doc = new Document("first_name", first_name)
//                    .append("last_name", last_name)
//                    .append("id", user_id)
//                    .append("username", username);
//            collection.insertOne(doc);
//            mongoClient.close();
//            System.out.println("User not exists in database. Written.");
//            return "no_exists";
//        } else {
//            System.out.println("User exists in database.");
//            mongoClient.close();
//            return "exists";
//        }
//    }
}
