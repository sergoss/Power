package bot;

import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.SQLhandler;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot {

//    @Override
//    public void onUpdatesReceived(List<Update> updates) {
//
//    }

    @Override
    public String getBotToken() {
        return BotSettings.getBOT_TOKEN();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Update");
        if (!update.hasMessage() || !update.getMessage().hasText()) return;
//        if (update.getMessage().getText().equals("/start"));
        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();
        User user = null;
        try {
            user = SQLhandler.findUserByChatId(chatId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        BotContext context;
        BotState state;
        if (user == null) {
            state = BotState.getInitialState();

            user = new User(chatId, state.ordinal(), true);
            try {
                SQLhandler.addNewUser(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            context = BotContext.of(this, user, text);
            state.enter(context);
        } else {
            context = BotContext.of(this, user, text);
            state = BotState.byId(user.getStateId());
        }

        state.handleInput(context);

        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());

        user.setStateId(state.ordinal());
        try {
            SQLhandler.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BotSettings.getBOT_NAME();
    }
}
