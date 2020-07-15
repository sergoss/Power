package bot.handlers.impl;

import bot.BotContext;
import bot.handlers.AbstractBotHandler;
import controller.MenuController;
import model.Product;
import service.MenuItemListHandler;
import service.SQLhandler;

public class ChoosingProduct extends AbstractBotHandler {

    @Override
    public void enter(BotContext context) {
        System.out.println("Product");
        sendMessage(context, "Выбери товар", MenuController.inlineKeyboardMenu(SQLhandler.getProductsList()));
    }

    @Override
    public void handleInput(BotContext context) {
        String input = context.getInput();
        int orderId = context.getUser().getOrder().getId();
        MenuItemListHandler<Product> menuItemListHandler = new MenuItemListHandler(SQLhandler.getProductsList());
        if (menuItemListHandler.isMenuItemContains(input)) {
            SQLhandler.setProductIdToOrder(input, orderId);
            next = new ChoosingPayment();
        } else if (input.equals(MenuController.getBackToMainMenu())) {
            next = new Start();
        } else {
            sendMessage(context, MenuController.getWrongInput());
            next = new ChoosingProduct();
        }
    }

    @Override
    public int id() {
        return 0;
    }

}
