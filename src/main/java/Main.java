import bot.Bot;
import model.User;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import service.SQLhandler;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            System.out.println("Connection dataBase");
            SQLhandler.setConnection();
            SQLhandler.createStatement();
            SQLhandler.resetUserStateId();
            System.out.println("DB connected");
            botsApi.registerBot(new Bot());
            System.out.println("Бот стартанул");
        } catch (SQLException | TelegramApiRequestException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        } finally {
//            System.out.println("finaly");
//            SQLhandler.closeConnection();
//        }
    }
}
