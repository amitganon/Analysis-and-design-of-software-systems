package Presentation.View.SupplierView;

import Presentation.Model.BackendController;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;

public class SupplierMainMenuView implements View {

    public SupplierMainMenuView() { }

    @Override
    public void printMenu() {
        System.out.println("-----------------Supplier Main Menu-----------------");
        System.out.println("1. View the suppliers menu");
        System.out.println("2. View the orders menu");
        System.out.println("3. Back");
        System.out.println("4. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return new SupplierMenuView();
            case "2":
                return new OrderMenuView();
            case "3":
                return new MainMenuView();
            case "4":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }

}
