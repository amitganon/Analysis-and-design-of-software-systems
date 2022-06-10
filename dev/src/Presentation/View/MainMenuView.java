package Presentation.View;

import Presentation.Model.BackendController;
import Presentation.ViewModel.MainMenuViewModel;

public class MainMenuView implements View {
    private final MainMenuViewModel mainMenuViewModel;

    public MainMenuView() {
        this.mainMenuViewModel = new MainMenuViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Main Menu-----------------");
        System.out.println("Enter your ID for sign in");
        System.out.println("Enter load for loading fake data");
    }

    @Override
    public View nextInput(String input) {
        if (input.equals("close")) {
            ApplicationView.shouldTerminate = true;
            return this;
        }
        else if (input.equals("load")) {
            BackendController.getInstance().loadDataForTests();
            return this;
        }
        else return mainMenuViewModel.signIn(input);
    }
}
