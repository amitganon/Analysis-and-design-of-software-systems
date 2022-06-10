package Presentation.View.SupplierView;

import Presentation.View.ApplicationView;
import Presentation.View.InventoryView.InventoryMainMenuView;
import Presentation.View.View;

public class OrderMenuView implements View {

    public OrderMenuView() { }

    @Override
    public void printMenu() {
        System.out.println("-----------------Orders Menu-----------------");
        System.out.println("1- View the fixed order menu");
        System.out.println("2- View the demand order menu");
        System.out.println("3. Back");
        System.out.println("4. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return new FixedOrderMenuView();
            case "2":
                return new DemandOrderMenuView();
            case "3":
                return new SupplierMainMenuView();
            case "4":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
