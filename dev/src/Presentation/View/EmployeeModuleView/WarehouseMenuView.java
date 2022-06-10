package Presentation.View.EmployeeModuleView;

import Presentation.View.ApplicationView;
import Presentation.View.InventoryView.InventoryMainMenuView;
import Presentation.View.SupplierView.SupplierMainMenuView;
import Presentation.View.View;

public class WarehouseMenuView implements View {

    public WarehouseMenuView() {

    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Warehouse Menu-----------------");
        System.out.println("1. View the supplier model menu");
        System.out.println("2. View the inventory model menu");
        System.out.println("3. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return new SupplierMainMenuView();
            case "2":
                return new InventoryMainMenuView();
            case "3":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
