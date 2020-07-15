package bot.handlers.impl;

import bot.BotContext;
import bot.handlers.AbstractBotHandler;
import bot.handlers.InputHandler;
import controller.MenuController;

public class CheckingPay extends AbstractBotHandler {
    @Override
    public boolean isInputNeeded() {
        return false;
    }


    @Override
    public void enter(BotContext context) {
        System.out.println("Проверка оплаты!");
        sendMessage(context, "Ваша оплата проверяется");
    }

    @Override
    public int id() {
        return 0;
    }


    @Override
    public void handleInput(BotContext context) {
        String input = context.getInput();

        if (input.equals(MenuController.getBackToMainMenu())) {
            next = new Start();
        } else {
            next = new CheckingPay();
        }
    }


    @Override
    public InputHandler nextState() {
        return next;
    }
}
