package bot.handlers.impl;

import bot.BotContext;
import bot.handlers.AbstractBotHandler;
import bot.handlers.InputHandler;
import controller.MenuController;
import model.District;
import service.MenuItemListHandler;
import service.SQLhandler;

public class ChoosingDistrict extends AbstractBotHandler {

    @Override
    public void enter(BotContext context) {
        System.out.println("District");
        sendMessage(context, "Выбери район", MenuController.inlineKeyboardMenu(SQLhandler.getDistrictsList()));
    }

    @Override
    public void handleInput(BotContext context) {

        String input = context.getInput();
        int orderId = context.getUser().getOrder().getId();
        MenuItemListHandler<District> menuItemListHandler = new MenuItemListHandler(SQLhandler.getDistrictsList());
        if (menuItemListHandler.isMenuItemContains(input)) {
            SQLhandler.setDistrictIdToOrder(input, orderId);
            next = new ChoosingProduct();
        } else if (input.equals(MenuController.getBackToMainMenu())) {
            next = new Start();
        } else {
            sendMessage(context, MenuController.getWrongInput());
            next = new ChoosingDistrict();
        }
    }

    @Override
    public int id() {
        return 0;
    }

}
