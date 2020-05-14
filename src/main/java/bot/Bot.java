package bot;

import model.Order;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.MenuItemListHandler;
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
                botUpdate(text, chatId);
            }
        } else if (update.hasCallbackQuery()) {
            final String callbackQuery = update.getCallbackQuery().getData();
            botUpdate(callbackQuery, chatId);

        }
//        if (update.getMessage().getText().equals("/start"));


    }

    private void botUpdate(String input, long chatId) {
        User user;
        user = SQLhandler.findUserByChatId(chatId);
        BotContext context;
        BotState state;
        Order order;
        order = SQLhandler.findOrderByUserChatId(user.getChatId());

        if (user == null) {
            state = BotState.getInitialState();
            user = new User(chatId, state.ordinal(), true);
            SQLhandler.addNewUser(user);
            order = new Order(user);
            user.setOrder(order);
            SQLhandler.addNewOrder(user.getOrder());
            context = BotContext.of(this, user, input);
            state.enter(context);
        } else {
            if (order == null) {
                order = new Order(user);
                SQLhandler.addNewOrder(order);
            }
            user.setOrder(order);
            SQLhandler.updateOrder(order);

            context = BotContext.of(this, user, input);
            state = BotState.byId(user.getStateId());
        }

//                else if (order == null){
//                    order = new Order(user);
//                    user.setOrder(order);
//                    SQLhandler.addNewOrder(order);
//                    context = BotContext.of(this, user, text);
//                    state = BotState.byId(user.getStateId());
//                } else {
//                    order = user.getOrder();
//                    SQLhandler.updateOrder(order);
//                    context = BotContext.of(this, user, text);
//                    state = BotState.byId(user.getStateId());
//                }


//                user.setOrder(order);
//                if (order == null) {
//                    order = new Order(user);
//                    SQLhandler.addNewOrder(order);
//                }
//                if (order.getOrderComplete() == true) {
//                    user.getOrder().setOrderUser(user);
//                    SQLhandler.addNewOrder(user.getOrder());
//                } else {
//                    SQLhandler.updateOrder(order);
//                }

        state.handleInput(context);

        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());

        user.setStateId(state.ordinal());
//                SQLhandler.updateUser(user);
    }

    @Override
    public String getBotUsername() {
        return BotSettings.getBOT_NAME();
    }
}
