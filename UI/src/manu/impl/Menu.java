//package manu.impl;
//
//import manu.api.MenuItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Menu extends MenuItem { //TODO: PROBABLY NOT GOOD HERE BECAUSE ENGINGE WILL NEED TO IMPLEMENT MENUITEM.. JUST TRIED
//
//    private final String optionZeroTitle;
//    private final String title;
//    private final List<MenuItem> menuItems = new ArrayList<>();
//
//    public Menu(String title, String optionZeroTitle)
//    {
//        this.optionZeroTitle = optionZeroTitle;
//        this.title = title;
//    }
//
//    public void AddMenuItem(MenuItem menuItem)
//    {
//        menuItems.add(menuItem);
//    }
//
//    public void Show()
//    {
//        printMenu();
//        getInputWithValidation(out int userChoice);
//        while(userChoice != 0)
//        {
//            Console.Clear();
//            r_MenuItems[userChoice - 1].WhenChoosed();
//            printMenu();
//            getInputWithValidation(out userChoice);
//        }
//
//        Console.Clear();
//    }
//
//    private void printMenu()
//    {
//        int i = 1;
//
//        System.out.println(GetTitle());
//        System.out.println("-----------------------");
//        for(MenuItem menuItem : menuItems)
//        {
//            System.out.println(i +". "+ menuItem.GetTitle());
//        }
//
//        System.out.println("0. "+ optionZeroTitle);
//        System.out.println("-----------------------");
//        if(menuItems.size() > 0)
//        {
//            System.out.println("Enter you request: (1 to " + menuItems.size() + " or press '0' to " + optionZeroTitle + " ).");
//        }
//        else
//        {
//            System.out.println("Press '0' to " + optionZeroTitle+ ").");
//        }
//    }
//
//
//    @Override
//    public void WhenChoosed() {
//        Show();
//    }
//
//    @Override
//    public String GetTitle() {
//        return title;
//    }
//}
