package bot;

import controller.MenuController;
import model.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.MenuItemListHandler;
import service.SQLhandler;

public enum BotState {

    START(false) {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            System.out.println("Start");
//            context.getUser().setStateId(0);
            sendMessage(context, "Hello");
        }

//        @Override
//        public void handleInput(BotContext context) {
//            if (context.getInput().equals("/start")) {
////                context.getUser().setOrder(new Order(context.getUser()));
////                SQLhandler.addNewOrder(context.getUser().getOrder());
//                next = CHOOSING_CITY;
//            }
//        }

        @Override
        public BotState nextState() {
            return CHOOSING_CITY;
        }
    },

    CHOOSING_CITY {
        private BotState next;

        //todo понять как пердавать наше меню в метод sendMessage или в context
        @Override
        public void enter(BotContext context) {
            System.out.println("City");
            sendMessage(context, "Выбери город", MenuController.inlineKeyboardMenu(SQLhandler.getCitiesList()));
        }

        @Override
        public void handleInput(BotContext context) {
            String input = context.getInput();
            int orderId = context.getUser().getOrder().getId();
            MenuItemListHandler<City> menuItemListHandler = new MenuItemListHandler(SQLhandler.getCitiesList());
            if (menuItemListHandler.checkMenuItemContains(input)) {
                SQLhandler.setCityIdToOrder(input, orderId);
                next = CHOOSING_DISTRICT;
            } else if (input.equals(MenuController.getBackToMainMenu())) {
                next = START;
            } else {
                sendMessage(context, MenuController.getWrongInput());
                next = CHOOSING_CITY;
            }
        }
//        @Override
//        public void handleInput(BotContext context) {
//            String cityName = context.getInput();
//            MenuItemListHandler<City> menuItemListHandler = new MenuItemListHandler(SQLhandler.getCitiesList());
//            if (menuItemListHandler.checkMenuItemContains(cityName)) {
//                City city = menuItemListHandler.getMenuItemByName(cityName);
//                context.getUser().getOrder().setOrderCity(city);
//                next = CHOOSING_DISTRICT;
//            } else {
//                sendMessage(context, "Empty message");
//                next = CHOOSING_CITY;
//            }
//        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    CHOOSING_DISTRICT {
        private BotState next;

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
            if (menuItemListHandler.checkMenuItemContains(input)) {
                SQLhandler.setDistrictIdToOrder(input, orderId);
                next = CHOOSING_PRODUCT;
            } else if (input.equals(MenuController.getBackToMainMenu())) {
                next = START;
            } else {
                sendMessage(context, MenuController.getWrongInput());
                next = CHOOSING_DISTRICT;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    CHOOSING_PRODUCT {
        private BotState next;

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
            if (menuItemListHandler.checkMenuItemContains(input)) {
                SQLhandler.setProductIdToOrder(input, orderId);
                next = CHOOSING_PAYMENT;
            } else if (input.equals(MenuController.getBackToMainMenu())) {
                next = START;
            } else {
                sendMessage(context, MenuController.getWrongInput());
                next = CHOOSING_PRODUCT;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    CHOOSING_PAYMENT {
        private BotState next;

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
            if (menuItemListHandler.checkMenuItemContains(input)) {
                SQLhandler.setPaymentIdToOrder(input, orderId);
                next = ORDER;
            } else if (input.equals(MenuController.getBackToMainMenu())) {
                next = START;
            } else {
                sendMessage(context, MenuController.getWrongInput());
                next = CHOOSING_PAYMENT;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    ORDER {
        private BotState next;
        @Override
        public void enter(BotContext context) {
            System.out.println("Order");
            sendMessage(context, "Подтверди заказ");
        }

        @Override
        public void handleInput(BotContext context) {
            String input = context.getInput();
            if (input.equals(MenuController.getConfirmationOfPayment())) {
                next = CHECKING_PAY;
            } else if (input.equals(MenuController.getRefusePayment())) {
                next = START;
            } else {
                sendMessage(context, MenuController.getWrongInput());
                next = ORDER;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    CHECKING_PAY {
        private BotState next;
        @Override
        public void enter(BotContext context) {
            System.out.println("Проверка оплаты!");
            sendMessage(context, "Ваша оплата проверяется");
        }

        @Override
        public void handleInput(BotContext context) {
            String input = context.getInput();

            if (input.equals(MenuController.getBackToMainMenu())) {
                next = START;
            } else {
                next = CHECKING_PAY;
            }
        }


        @Override
        public BotState nextState() {
            return next;
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

    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context) {

    }

    public abstract void enter(BotContext context);

    public abstract BotState nextState();
//todo добавить метод отрисовщик меню

//    public void setMenu(BotContext context) {
//
//    };

//    public void handleInput<T extends MenuItem> (BotContext context, String input) {
//        input = context.getInput();
//        MenuItemListHandler<T> menuItemListHandler = new MenuItemListHandler(SQLhandler.getProductsList());
//        if (menuItemListHandler.checkMenuItemContains(paymentName)) {
//            Product product = menuItemListHandler.getMenuItemByName(paymentName);
//            context.getOrder().setOrderProduct(product);
//            next = CHOOSING_PAYMENT;
//        } else {
//            sendMessage(context, "Empty message");
//            next = CHOOSING_PRODUCT;
//        }
//    }
}
