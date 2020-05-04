package service;

import model.*;

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

    public static void createStatement() throws SQLException {
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

    public static User findUserByChatId(long chatId) {
        User user = null;
        try {
            resultSet = statement.executeQuery("SELECT id, chat_id, state_id, notify FROM users WHERE chat_id = " + chatId);
//        User(String nick, Long chatId, Integer stateId, Boolean notify)
            user = null;
            while (resultSet.next()) {
                if (chatId == resultSet.getLong(2)) {
                    user = new User(resultSet.getInt(1),
                            resultSet.getLong(2),
                            resultSet.getInt(3),
                            resultSet.getBoolean(4));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void addNewUser(User user) {
        try {
            if (user.equals(findUserByChatId(user.getChatId()))) return;
            long chatId = user.getChatId();
            int stateId = user.getStateId();
            boolean notify = user.getNotify();
            String query = "INSERT INTO users (chat_id, state_id, notify /*order_id*/) VALUES ((?), (?), (?)/*, (?)*/)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, chatId);
            preparedStatement.setInt(2, stateId);
            preparedStatement.setBoolean(3, notify);
//            preparedStatement.setInt(4, user.getOrder().getId());
//        resultSet = preparedStatement.executeQuery();
//        if (resultSet.next()) return;
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(User user) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE users SET state_id = ? WHERE chat_id = " + user.getChatId());
            preparedStatement.setInt(1, user.getStateId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addNewOrder(Order order) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO orders (user_chat_id), (order_details) VALUES ((?), (?))");
            preparedStatement.setLong(1, order.getOrderUser().getChatId());
            preparedStatement.setInt(2, order.getOrderDetails());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateOrder(Order order) {
        try {
            //todo Отредактировать запрос, потому что выбивает exception SQL ошибка синтаксиса ","
            preparedStatement = connection.prepareStatement("UPDATE orders SET city_id = ?, district_id = ?, product_id = ?, payment_id = ?   WHERE id = " + order.getId());
            preparedStatement.setInt(1, order.getOrderCity().getId());
            preparedStatement.setInt(2, order.getOrderDistrict().getId());
            preparedStatement.setInt(3, order.getOrderProduct().getId());
            preparedStatement.setInt(4, order.getOrderPayment().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<City> getCitiesList() {
        List<City> cities = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name FROM city");
            while (resultSet.next()) {
                cities.add(new City(resultSet.getInt(1),
                        resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public static List<District> getDistrictsList() {
        List<District> districts = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name, city_id FROM district");
            while (resultSet.next()) {
                districts.add(new District(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return districts;
    }

    public static List<Product> getProductsList() {
        List<Product> products = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name, price, weight, city_id, district_id FROM product");
            while (resultSet.next()) {
                products.add(new Product(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5),
                        resultSet.getInt("district_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static List<Payment> getPaymentsList() {
        List<Payment> payments = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name, details  FROM payment");
            while (resultSet.next()) {
                payments.add(new Payment(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public static List<User> getUsersList() {
        List<User> users = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, chat_id chatId FROM users");
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt(1), resultSet.getLong(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
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
