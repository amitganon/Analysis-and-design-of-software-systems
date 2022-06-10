package Presentation.View.DeliveryModuleView;

import Presentation.Model.BackendController;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.DeliveryModulViewModel.CreateDeliveryViewModel;

public class DeliveryMenuView implements View {
    private final CreateDeliveryViewModel createDeliveryViewModel;

    public DeliveryMenuView() {
        createDeliveryViewModel= new CreateDeliveryViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Delivery Menu-----------------");
        System.out.println("1.see all deliveries");
        System.out.println("2.Logout");
        BackendController.getInstance().getDriversRelevant(134,"2025-05-05");
    }

    @Override
    public View nextInput(String input) {
        if(input.equals("back"))
            return createDeliveryViewModel.returnToMainMenuView();
        else if(input.equals("1"))
            return createDeliveryViewModel.getAllDeliveriesView();
        else if (input.equals("2"))
            return new MainMenuView();
        else
            return new DeliveryMenuView();
    }
}
