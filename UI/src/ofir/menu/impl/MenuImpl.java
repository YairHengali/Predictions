package ofir.menu.impl;

import ofir.menu.api.Menu;
import ofir.menu.api.MenuItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuImpl implements Menu {
    String menuName;
    List<MenuItem> items;
    int itemsCounter;

    public MenuImpl(String menuName) {
        this.menuName = menuName;
        this.itemsCounter = 0;
        items = new ArrayList<>();
    }

    @Override
    public Collection<MenuItem> getItems() {
        return this.items;
    }

    @Override
    public MenuItem getMenuItemBySerialNum(int itemSerial) {
        for(MenuItem item : this.items){
            if(item.getSerialNum() == itemSerial)
                return item;
        }
        throw new IndexOutOfBoundsException("Serial number: " + itemSerial + " does not exist!");
    }

    @Override
    public void addItem(String itemTitle) {
        this.itemsCounter++;
        this.items.add(new MenuItemImpl(this.itemsCounter, itemTitle));
    }

    @Override
    public void showMenu() {
        this.items.forEach(MenuItem::showItem);
    }

    @Override
    public String getMenuName() {
        return this.menuName;
    }

    @Override
    public boolean isInRange(int choiceNum) {
        if(itemsCounter == 0)
            return false;
        else return choiceNum > 0 && choiceNum <= itemsCounter;
    }
}
