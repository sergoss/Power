package bot.handlers;

import bot.BotContext;

public interface InputHandler {
    void handleInput(BotContext context);

    boolean isInputNeeded();

    void enter(BotContext context);

    int id();

    InputHandler nextState();
}
