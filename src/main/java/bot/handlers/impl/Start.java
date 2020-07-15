package bot.handlers.impl;

import bot.BotContext;
import bot.handlers.AbstractBotHandler;
import bot.handlers.BotState;
import bot.handlers.InputHandler;

public class Start extends AbstractBotHandler {

    public Start() {
        next = new ChosingCity();
    }

    public boolean isInputNeeded() {
        return false;
    }

    @Override
    public void enter(BotContext context) {
        System.out.println("Start");
        sendMessage(context, "Hello");
    }

    @Override
    public int id() {
        return 0;
    }


}
