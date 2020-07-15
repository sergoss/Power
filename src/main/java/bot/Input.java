package bot;

public enum Input {
    BACK_TO_MAIN_MENU("Вернуться в главное меню"),
    CONFIRMATION_OF_PAYMENT("Я оплатил"),
    REFUSE_PAYMENT("Отказаться от оплаты"),
    WRONG_INPUT("Empty Message"),
    UNKNOWN("");

    private final String bla;

    Input(String bla) {
        this.bla = bla;
    }

    public static Input of(String input) {
        for (Input value : values()) {
            if (value.bla.equals(input)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
