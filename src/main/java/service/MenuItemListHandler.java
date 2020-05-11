package service;

import model.MenuItem;

import java.util.List;
public class MenuItemListHandler<T extends MenuItem> {
    private List<T> list;

    public MenuItemListHandler(List<T> list) {
        this.list = list;
    }

    public T getMenuItemByName(String name) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getName().equals(name)) {
                return this.list.get(i);
            }
        }
        return null;
    }

    public T getMenuItemById(int id) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getId() == id) {
                return this.list.get(i);
            }
        }
        return null;
    }


    public boolean checkMenuItemContains(String name) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


}
