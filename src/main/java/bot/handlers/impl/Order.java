package bot.handlers.impl;

import bot.BotContext;
import bot.handlers.AbstractBotHandler;
import bot.handlers.InputHandler;

public class Order extends AbstractBotHandler {
    @Override
    public boolean isInputNeeded() {
        return false;
    }

    @Override
    public void enter(BotContext context) {

    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public InputHandler nextState() {
        return null;
    }
}
