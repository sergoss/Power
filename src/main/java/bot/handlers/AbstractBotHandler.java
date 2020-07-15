package bot.handlers;

import bot.BotContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class AbstractBotHandler implements InputHandler {
    protected InputHandler next;
    protected boolean isInputNeeded = true;

    @Override
    public void handleInput(BotContext context) {
        //do nothing
    }

    @Override
    public InputHandler nextState() {
        return next;
    }

    @Override
    public boolean isInputNeeded() {
        return isInputNeeded;
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(BotContext context, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text).setReplyMarkup(inlineKeyboardMarkup);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
