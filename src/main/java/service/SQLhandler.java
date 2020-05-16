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
        connection = DriverManager.getConnection("jdbc:sqlite:power_update.db");
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
//        finally {
//            try {
//                resultSet.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        return user;
    }

    public static void addNewUser(User user) {
        try {
            if (user.equals(findUserByChatId(user.getChatId()))) return;
            long chatId = user.getChatId();
            int stateId = user.getStateId();
            boolean notify = user.getNotify();
            String query = "INSERT INTO users (chat_id, state_id, notify, order_id) VALUES ((?), (?), (?), (?))";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, chatId);
            preparedStatement.setInt(2, stateId);
            preparedStatement.setBoolean(3, notify);
            //todo шляпа и костыль из-за того что не получается изменить default значение в базе и уобрать флаг NOT NULL
            preparedStatement.setInt(4, 0);
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

    public static void resetUserStateId() {
        System.out.println("Пользователи сброшены");
        try {
            preparedStatement = connection.prepareStatement("UPDATE users SET state_id = 0 WHERE state_id > 0");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Order findOrderByUserChatId(long chatId) {
        Order order = null;
        try {
//            preparedStatement = connection.prepareStatement("SELECT id, order_details, completed, city_id, district_id, product_id, payment_id FROM orders WHERE user_chat_id = " + chatId + " AND completed = false")


            resultSet =  statement.executeQuery("SELECT id, order_details, completed, city_id, district_id, product_id, payment_id FROM orders WHERE user_chat_id = " + chatId + " AND completed = false");
//            while (resultSet.next()) {
//
//            }
            //todo укладывать юзера в ордер в этом методе иначе он будет NULL всегда
            int orderId = resultSet.getInt("id");
            int orderDetails = resultSet.getInt("order_details");
            boolean completed = resultSet.getBoolean("completed");
            int cityId = resultSet.getInt("city_id");
            int districtId = resultSet.getInt("district_id");
            int productId = resultSet.getInt("product_id");
            int paymentId = resultSet.getInt("payment_id");
            resultSet.close();
            order = new Order(orderId, orderDetails, completed);

            if (cityId == 0) {
                order.setOrderCity(new MenuItemListHandler<>(SQLhandler.getCitiesList()).getMenuItemById(cityId));
            }
            if (districtId == 0) {
                order.setOrderDistrict(new MenuItemListHandler<>(SQLhandler.getDistrictsList()).getMenuItemById(districtId));
            }
            if (productId == 0) {
                order.setOrderProduct(new MenuItemListHandler<>(SQLhandler.getProductsList()).getMenuItemById(productId));
            }
            if (paymentId == 0) {
                order.setOrderPayment(new MenuItemListHandler<>(SQLhandler.getPaymentsList()).getMenuItemById(paymentId));
            }

//            order = new Order(resultSet.getInt("id"), resultSet.getInt("order_details"), resultSet.getBoolean("completed"));
//            if (resultSet.getInt("city_id") == 0) {
//                order.setOrderCity(new MenuItemListHandler<>(SQLhandler.getCitiesList()).getMenuItemById(resultSet.getInt("city_id")));
//            }
//            if (resultSet.getInt("district_id") == 0) {
//                order.setOrderDistrict(new MenuItemListHandler<>(SQLhandler.getDistrictsList()).getMenuItemById(resultSet.getInt("distrcit_id")));
//            }
//            if (resultSet.getInt("product_id") == 0) {
//                order.setOrderProduct(new MenuItemListHandler<>(SQLhandler.getProductsList()).getMenuItemById(resultSet.getInt("product_id")));
//            }
//            if (resultSet.getInt("payment_id") == 0) {
//                order.setOrderPayment(new MenuItemListHandler<>(SQLhandler.getPaymentsList()).getMenuItemById(resultSet.getInt("payment_id")));
//            }
//            while (resultSet.next()) {
//
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        finally {
//            try {
//                resultSet.close();
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//        }
        return order;
    }

    public static void addNewOrder(Order order) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO orders (user_chat_id, order_details, completed) VALUES (?, ?, ?)");
            preparedStatement.setLong(1, order.getOrderUser().getChatId());
            preparedStatement.setInt(2, order.getOrderDetails());
            preparedStatement.setBoolean(3, order.getOrderComplete());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateOrder(Order order) {
        try {
            //todo Отредактировать запрос, потому что выбивает exception SQL ошибка синтаксиса ","
            int defaultId = 0;
            preparedStatement = connection.prepareStatement("UPDATE orders SET city_id = ?, district_id = ?, product_id = ?, payment_id = ?   WHERE id = " + order.getId());
            if (order.getOrderCity() != null) {
                preparedStatement.setInt(1, order.getOrderCity().getId());
            } else {
                preparedStatement.setInt(1, defaultId);
            }
            if (order.getOrderDistrict() != null) {
                preparedStatement.setInt(2, order.getOrderDistrict().getId());
            } else {
                preparedStatement.setInt(2, defaultId);
            }
            if (order.getOrderProduct() != null) {
                preparedStatement.setInt(3, order.getOrderProduct().getId());
            } else {
                preparedStatement.setInt(3, defaultId);
            }
            if (order.getOrderPayment() != null) {
                preparedStatement.setInt(4, order.getOrderPayment().getId());
            } else {
                preparedStatement.setInt(4, defaultId);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<City> getCitiesList() {
        List<City> cities = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name FROM cities");
            while (resultSet.next()) {
                cities.add(new City(resultSet.getInt(1),
                        resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cities;
    }

    public static List<District> getDistrictsList() {
        List<District> districts = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name, city_id FROM districts");
            while (resultSet.next()) {
                districts.add(new District(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return districts;
    }

    public static List<Product> getProductsList() {
        List<Product> products = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name, price, weight, city_id, district_id FROM products");
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
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return products;
    }

    public static List<Payment> getPaymentsList() {
        List<Payment> payments = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, name, details  FROM payments");
            while (resultSet.next()) {
                payments.add(new Payment(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return payments;
    }

    public static void setCityIdToOrder(String cityName, int orderId) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET city_id = (SELECT id FROM cities WHERE name = '" + cityName +"') WHERE id = " + orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setDistrictIdToOrder(String districtName, int orderId) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET district_id = (SELECT id FROM districts WHERE name = '" + districtName +"') WHERE id = " + orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setProductIdToOrder(String productName, int orderId) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET product_id = (SELECT id FROM products WHERE name = '" + productName +"') WHERE id = " + orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setPaymentIdToOrder(String paymentName, int orderId) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE orders SET payment_id = (SELECT id FROM payments WHERE name = '" + paymentName +"') WHERE id = " + orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<User> getUsersList() {
        List<User> users = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id, chat_id chatId FROM users");
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt(1),
                        resultSet.getLong(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
