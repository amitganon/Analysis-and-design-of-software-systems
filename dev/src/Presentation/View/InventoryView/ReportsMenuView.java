package Presentation.View.InventoryView;


import Presentation.View.ApplicationView;
import Presentation.View.View;

public class ReportsMenuView implements View {

    @Override
    public void printMenu() {
        System.out.println("-----------------applicationFacadeMaker Menu-----------------");
        System.out.println("1. View Inventory applicationFacadeMaker");
        System.out.println("2. View Shortage applicationFacadeMaker");
        System.out.println("3. View & add Damaged applicationFacadeMaker");
        System.out.println("4. View & add Purchased applicationFacadeMaker");
        System.out.println("5. Back");
        System.out.println("6. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return new InventoryReportView();
            case "2":
                return new ShortageReportView();
            case "3":
                return new DamagedReportView();
            case "4":
                return new PurchasedReportView();
            case "5":
                return new InventoryMainMenuView();
            case "6":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
