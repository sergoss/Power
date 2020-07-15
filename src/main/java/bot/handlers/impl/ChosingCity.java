package bot.handlers.impl;

import bot.BotContext;
import bot.handlers.AbstractBotHandler;
import controller.MenuController;
import model.City;
import service.MenuItemListHandler;
import service.SQLhandler;

public class ChosingCity extends AbstractBotHandler {


    //todo понять как пердавать наше меню в метод sendMessage или в context
    @Override
    public void enter(BotContext context) {
        System.out.println("City");
        sendMessage(context, "Выбери город", MenuController.inlineKeyboardMenu(SQLhandler.getCitiesList()));
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void handleInput(BotContext context) {
        String input = context.getInput();

        int orderId = context.getUser().getOrder().getId();
        MenuItemListHandler<City> menuItemListHandler = new MenuItemListHandler(SQLhandler.getCitiesList());
        if (menuItemListHandler.isMenuItemContains(input)) {
            SQLhandler.setCityIdToOrder(input, orderId);
            next = new ChoosingDistrict();
        } else if (input.equals(MenuController.getBackToMainMenu())) {
            next = new Start();
        } else {
            sendMessage(context, MenuController.getWrongInput());
            next = this;
        }
    }

    @Override
    public boolean isInputNeeded() {
        return true;
    }

}
