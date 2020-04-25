package bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public enum BotState {
    START {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Hello");
        }

        @Override
        public BotState nextState() {
            return CHOOSING_CITY;
        }
    },

    CHOOSING_CITY {
        private BotState next;
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Выбери город");
        }
        @Override
        public void handleInput(BotContext context) {
            if ("Сибирь".equals(context.getInput())) {
                next = CHOOSING_DISTRICT;
            }
        }
        @Override
        public BotState nextState() {
            return CHOOSING_DISTRICT;
        }
    },

    CHOOSING_DISTRICT {
        @Override
        public void enter(BotContext context) {

        }

        @Override
        public BotState nextState() {
            return CHOOSING_PAYMENT;
        }
    },

    CHOOSING_PAYMENT {
        @Override
        public void enter(BotContext context) {

        }

        @Override
        public BotState nextState() {
            return START;
        }
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

    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context) {

    }
    public abstract void enter(BotContext context);
    public abstract BotState nextState();
}
