package bot;

import bot.handlers.BotState;
import bot.handlers.InputHandler;
import model.Order;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.SQLhandler;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return BotSettings.getBOT_TOKEN();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Update");
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                final long chatId = update.getMessage().getChatId();
                final String text = update.getMessage().getText();
                botUpdate(text, chatId);
            }
        } else if (update.hasCallbackQuery()) {
            final long chatId = update.getCallbackQuery().getMessage().getChatId();
            final String callbackQuery = update.getCallbackQuery().getData();
            botUpdate(callbackQuery, chatId);
        }
    }

    //todo переименовать метод
    private void botUpdate(String input, long chatId) {
        User user;
        user = SQLhandler.findUserByChatId(chatId);
        BotContext context;
        InputHandler state;
        Order order;
        order = SQLhandler.findOrderByUserChatId(chatId);

        if (user == null) {
            state = BotState.getInitialState();
            user = new User(chatId, state.id(), true);
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
//            SQLhandler.updateOrder(order);
            context = BotContext.of(this, user, input);
            state = BotState.byId(user.getStateId());
        }
        state.handleInput(context);
        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());
        user.setStateId(state.id());
        SQLhandler.updateUser(user);
    }

    @Override
    public String getBotUsername() {
        return BotSettings.getBOT_NAME();
    }
}
