package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.DamagedItemViewModel;

public class DamagedReportView implements View {

    private final DamagedItemViewModel damagedItemViewModel;

    public DamagedReportView(){
        damagedItemViewModel= new DamagedItemViewModel();
    }
    @Override
    public void printMenu() {
        System.out.println("-----------------Damaged applicationFacade Menu-----------------");
        System.out.println("1. View damaged applicationFacade");
        System.out.println("2. View damaged applicationFacade by date");
        System.out.println("3. View damaged applicationFacade by item");
        System.out.println("4. Add a damaged record");
        System.out.println("5. Back");
        System.out.println("6. Back");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return damagedItemViewModel.getReport();
            case "2":
                return damagedItemViewModel.getReportByDate();
            case "3":
                return damagedItemViewModel.getReportByItemID();
            case "4":
                return damagedItemViewModel.addDamagedItem();
            case "5":
                return new ReportsMenuView();
            case "6":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
