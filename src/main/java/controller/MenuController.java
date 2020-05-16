package controller;

import bot.BotSettings;
import model.City;
import model.MenuItem;
import model.Order;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MenuController {
    private static String backToMainMenu = "Вернуться в главное меню";
    private static String confirmationOfPayment = "Я оплатил";
    private static String refusePayment = "Отказаться от оплаты";
    private static String wrongInput = "Empty Message";

//    public static SendMessage menu(long chatId, List<? extends MenuItem> menuItem, String menuName) {
//        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
//        int j = 0;
//        for (MenuItem a : menuItem) {
//            inlineKeyboardButtons.add(j, new InlineKeyboardButton());
//            inlineKeyboardButtons.get(j).setText(a.getName());
//            inlineKeyboardButtons.get(j).setCallbackData(a.getName());
//            j++;
//        }
//        inlineKeyboardButtons.add(new InlineKeyboardButton().setText(backToMainMenu).setCallbackData(backToMainMenu));
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//        for (int i = 0; i < inlineKeyboardButtons.size(); i++) {
//            rowList.add(new ArrayList<>());
//        }
//
//        for (int i = 0; i < inlineKeyboardButtons.size(); i++) {
//            rowList.get(i/2).add(inlineKeyboardButtons.get(i));
//        }
//        inlineKeyboardMarkup.setKeyboard(rowList);
//        return new SendMessage().setChatId(chatId).setText(new StringBuilder().append("Вас приветсвует магазин " + BotSettings.getShopName()).append(System.lineSeparator()).append(menuName).toString()).setReplyMarkup(inlineKeyboardMarkup);
//    }

    public static InlineKeyboardMarkup inlineKeyboardMenu(List<? extends MenuItem> menuItem) {
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        int j = 0;
        for (MenuItem a : menuItem) {
            inlineKeyboardButtons.add(j, new InlineKeyboardButton());
            inlineKeyboardButtons.get(j).setText(a.getName());
            inlineKeyboardButtons.get(j).setCallbackData(a.getName());
            j++;
        }
        inlineKeyboardButtons.add(new InlineKeyboardButton().setText(backToMainMenu).setCallbackData(backToMainMenu));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (int i = 0; i < inlineKeyboardButtons.size(); i++) {
            rowList.add(new ArrayList<>());
        }

        for (int i = 0; i < inlineKeyboardButtons.size(); i++) {
            rowList.get(i/2).add(inlineKeyboardButtons.get(i));
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

//    public static SendMessage orderMenu(long chatId, Order order) {
//
//        return new SendMessage().setChatId(chatId).setText(new StringBuilder().append("Вас приветсвует магазин" + BotSettings.getShopName()).append(System.lineSeparator()).append(menuName).toString()).setReplyMarkup(inlineKeyboardMarkup);
//    }

    public static String getBackToMainMenu() {
        return backToMainMenu;
    }

    public static String getConfirmationOfPayment() {
        return confirmationOfPayment;
    }

    public static String getRefusePayment() {
        return refusePayment;
    }

    public static String getWrongInput() {
        return wrongInput;
    }
}
