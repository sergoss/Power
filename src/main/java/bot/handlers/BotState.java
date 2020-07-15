package bot.handlers;

import bot.BotContext;
import controller.MenuController;
import model.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.MenuItemListHandler;
import service.SQLhandler;

public enum BotState implements InputHandler {





    ORDER {
        private BotState next;
        @Override
        public void enter(BotContext context) {
            System.out.println("Order");
            sendMessage(context, "Подтверди заказ");
        }

        @Override
        public void handleInput(BotContext context) {
            String input = context.getInput();
            if (input.equals(MenuController.getConfirmationOfPayment())) {
                next = CHECKING_PAY;
            } else if (input.equals(MenuController.getRefusePayment())) {
                next = START;
            } else {
                sendMessage(context, MenuController.getWrongInput());
                next = ORDER;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    CHECKING_PAY {
    };


    private static BotState[] states;
    private boolean inputNeeded;

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }
        return states[id];
    }

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public boolean isInputNeeded() {
        return inputNeeded;
    }


    public abstract void enter(BotContext context);

    public abstract BotState nextState();
//todo добавить метод отрисовщик меню

//    public void setMenu(BotContext context) {
//
//    };

//    public void handleInput<T extends MenuItem> (BotContext context, String input) {
//        input = context.getInput();
//        MenuItemListHandler<T> menuItemListHandler = new MenuItemListHandler(SQLhandler.getProductsList());
//        if (menuItemListHandler.checkMenuItemContains(paymentName)) {
//            Product product = menuItemListHandler.getMenuItemByName(paymentName);
//            context.getOrder().setOrderProduct(product);
//            next = CHOOSING_PAYMENT;
//        } else {
//            sendMessage(context, "Empty message");
//            next = CHOOSING_PRODUCT;
//        }
//    }
}
