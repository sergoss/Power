package service;

import model.MenuItem;

import java.util.List;
public class MenuItemListHandler<T extends MenuItem> {
    private List<T> list;
    //todo отрефакторить без конструктора через конструкцию <? extends Menu Item>
    public MenuItemListHandler(List<T> list) {
        this.list = list;
    }

    public T getMenuItemByName(String name) {
        return this.list.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    public T getMenuItemById(int id) {
        return this.list.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public boolean isMenuItemContains(String name) {
        return this.list.stream().anyMatch(t -> t.getName().equals(name));
    }


}
