package bot.handlers.impl;

import bot.BotContext;
import bot.handlers.AbstractBotHandler;
import controller.MenuController;
import model.Payment;
import service.MenuItemListHandler;
import service.SQLhandler;

public class ChoosingPayment extends AbstractBotHandler {

    @Override
    public void enter(BotContext context) {
        System.out.println("Payment");
        sendMessage(context, "Выбери город", MenuController.inlineKeyboardMenu(SQLhandler.getPaymentsList()));
    }

    @Override
    public void handleInput(BotContext context) {
        String input = context.getInput();
        int orderId = context.getUser().getOrder().getId();
        MenuItemListHandler<Payment> menuItemListHandler = new MenuItemListHandler(SQLhandler.getPaymentsList());
        if (menuItemListHandler.isMenuItemContains(input)) {
            SQLhandler.setPaymentIdToOrder(input, orderId);
            next = new Order();
        } else if (input.equals(MenuController.getBackToMainMenu())) {
            next = new Start();
        } else {
            sendMessage(context, MenuController.getWrongInput());
            next = new ChoosingPayment();
        }
    }

    @Override
    public int id() {
        return 0;
    }

}
