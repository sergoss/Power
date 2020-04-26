package bot;

import model.Order;
import model.User;
import service.SQLhandler;

public class BotContext {
    private Bot bot;
    private User user;
    private String input;

    public static BotContext of(Bot bot, User user, String text) {
        return new BotContext(bot, user, text);
    }

    private BotContext(Bot bot, User user, String input) {
        this.bot = bot;
        this.user = user;
        this.input = input;

    }

    public User getUser() {
        return user;
    }

    public Bot getBot() {
        return bot;
    }

    public String getInput() {
        return input;
    }


}
