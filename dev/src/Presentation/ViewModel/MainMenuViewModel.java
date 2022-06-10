package Presentation.ViewModel;

import Presentation.Model.BackendController;
import Presentation.View.DeliveryModuleView.DeliveryMenuView;
import Presentation.View.EmployeeModuleView.*;
import Presentation.View.MainMenuView;
import Presentation.View.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainMenuViewModel {
    private final BackendController backendController;

    private int index=0;

    private List<String> printMenuOptions;

    private HashMap<Integer, View> viewOptions;

    public MainMenuViewModel(){
        this.backendController= BackendController.getInstance();
        printMenuOptions = new LinkedList<>();
        viewOptions = new HashMap<>();
    }

    public View signIn(String input) {
        if (!isEmployeeExist(input))
        {
            System.out.println("Invalid Id, Please Try Again!");
            return new MainMenuView();
        }
        if (isHRManagerRegistration(input)) {
            printMenuOptions.add(index + 1 + ". Managing shifts and defining shifts roles");
            viewOptions.put(index+1,new BranchSelectView(Integer.parseInt(input),"hr"));
            index = index+1;
        }

        if(isWarehouseRegistration(input))
        {
            printMenuOptions.add(index+1+". Inventory management and satisfactory orders");
            viewOptions.put(index+1,new BranchSelectView(Integer.parseInt(input),"warehouse"));
            index = index+1;
            printMenuOptions.add(index+1+". Supplier card management");
            viewOptions.put(index+1,new BranchSelectView(Integer.parseInt(input),"warehouse"));
            index = index+1;
        }
        if(isLogisticsManagerRegistration(input))
        {
            printMenuOptions.add(index+1+". Management and ordering of trucking");
            viewOptions.put(index+1,new DeliveryMenuView());
            index = index+1;
        }
        if(isBranchManagerRegistration(input))
        {

            printMenuOptions.add(index+1+". View reports and data");
            viewOptions.put(index+1,new BranchManagerMenuView(Integer.parseInt(input),backendController.getBranchOfManager(Integer.parseInt(input))));
            index = index+1;
        }
        if(index==0)
        {
            return new EmployeeMenuView(Integer.parseInt(input));
        }
        printMenuOptions.add(index+1+". Back");
        viewOptions.put(index+1,null);
        index = index+1;
        viewOptions.put(index+1,null);
        printMenuOptions.add(index+1+". Logout");
        index = index+1;

        System.out.println("-----------------Select Menu-----------------");
        for(String opt : printMenuOptions)
        {
            System.out.println(opt);
        }
        Scanner in = new Scanner(System.in);
        int op = in.nextInt();
        if (!viewOptions.containsKey(op))
        {
            System.out.println("Invalid Id, Please Try Again!");
            return new MainMenuView();
        }
        else
        {
            if ( viewOptions.get(op) == null)
                return logout();
            else  return viewOptions.get(op);
        }
    }
    private boolean isWarehouseRegistration(String input) {
        return backendController.findJobEmployee(input , "warehouse");
    }
    private boolean isBranchManagerRegistration(String input) {
        return backendController.findJobEmployee(input , "branch manager");
    }
    private boolean isLogisticsManagerRegistration(String input) {
        return backendController.findJobEmployee(input , "logistics");
    }
    private boolean isHRManagerRegistration(String input)
    {
        return backendController.findJobEmployee(input , "hr");
    }
    private boolean isEmployeeExist(String input)
    {
        try{
            return backendController.getEmployeeByID(Integer.parseInt(input))!=null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private View logout()
    {
        backendController.logout();
        return new MainMenuView();
    }

}
