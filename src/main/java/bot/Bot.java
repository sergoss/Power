package bot;

import model.Order;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.SQLhandler;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return BotSettings.getBOT_TOKEN();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Update");
        final long chatId = update.getMessage().getChatId();
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                final String text = update.getMessage().getText();
                User user;
                user = SQLhandler.findUserByChatId(chatId);
                BotContext context;
                BotState state;
                if (user == null) {
                    state = BotState.getInitialState();
                    user = new User(chatId, state.ordinal(), true);
                    user.getOrder().setOrderUser(user);
                    SQLhandler.addNewOrder(user.getOrder());
                    SQLhandler.addNewUser(user);
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
                SQLhandler.updateUser(user);
            }
        } else if (update.hasCallbackQuery()) {
            final String getCalback = update.getCallbackQuery().getData();

        }
//        if (update.getMessage().getText().equals("/start"));


    }

    @Override
    public String getBotUsername() {
        return BotSettings.getBOT_NAME();
    }
}
