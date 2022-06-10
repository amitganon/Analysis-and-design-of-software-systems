package Presentation.View.EmployeeModuleView;

import Presentation.Model.BackendController;
import Presentation.View.MainMenuView;
import Presentation.View.View;

import java.util.List;

public class BranchSelectView implements View {
    private final int ID;
    private List<String> branches;

    private String jobWindow;

    private String back;

    private String logout;

    public BranchSelectView(int input , String nextWindow) {
        this.ID = input;
        if(!BackendController.getInstance().findJobEmployee(input+"","hr"))
            this.branches = BackendController.getInstance().getAllBranchesEmployeeWorksIn(ID);
        else
            this.branches = BackendController.getInstance().getAllBranchesByAddress();
        this.jobWindow = nextWindow;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Select Branch To Manage-----------------");
        int index = 1;
        for (String branch : branches){
            System.out.println((index++)+"."+branch);
        }
        back = (index++)+"";
        System.out.println(back + ". Back");
        logout = index+"";;
        System.out.println(logout + ". Logout");
    }

    @Override
    public View nextInput(String input) {
        try {
            if (input.equals(back)||input.equals(logout))
                return new MainMenuView();
            else {
                BackendController.getInstance().enterBranch(branches.get(Integer.parseInt(input) - 1));
                if (jobWindow.equals("hr"))
                    return new HRManagerMenuView(ID, branches.get(Integer.parseInt(input) - 1));
                else
                    return new WarehouseMenuView();
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new BranchSelectView(ID , jobWindow);
        }
    }
}

