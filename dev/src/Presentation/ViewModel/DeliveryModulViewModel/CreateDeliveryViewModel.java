package Presentation.ViewModel.DeliveryModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.EmployeeModuleModel.EmployeeModel;
import Presentation.View.View;
import Presentation.Model.DeliveryModuleModel.DeliveryModel;
import Presentation.Model.DeliveryModuleModel.StockShortnessModel;
import Presentation.Model.DeliveryModuleModel.TruckModel;
import Presentation.View.DeliveryModuleView.AllDeliveriesView;
import Presentation.View.DeliveryModuleView.DeliveryMenuView;
import Presentation.View.DeliveryModuleView.DeliveryView;
import Presentation.View.MainMenuView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class CreateDeliveryViewModel {
    private final BackendController backendController;

    public CreateDeliveryViewModel() {
        this.backendController = BackendController.getInstance();
    }

//    public View createNewDelivery() {
//        try {
//            System.out.println("please enter a date for the delivery (in format of 'YYYY-MM-DD'):");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
//            LocalDate date = checkDateValidity(input);
//            System.out.println("please enter a time for the delivery (in format of 'hh:mm'):");
//            input = scanner.nextLine();
//            Date time = checkTimeValidity(input);
//
//            System.out.println("please choose a truck for the delivery:");
//            List<TruckModel> trucks = backendController.getAllTrucksAvailable(dateToString(date));
//            if(trucks.size()==0) {
//                System.out.println("There are no trucks available");
//                return new DeliveryMenuView(); }
//            for (int i=0; i<trucks.size(); i++)
//                System.out.println(i+1 + ") "+ trucks.get(i).toString());
//            input = scanner.nextLine();
//            TruckModel truckM = trucks.get(Integer.parseInt(input)-1);
//
//            System.out.println("please choose a driver for the delivery:");
//            List<EmployeeModel> drivers = backendController.getDriversRelevant(truckM.getMaxWeight(), dateToString(date));
//            if(drivers.size()==0) {
//                System.out.println("There are no available drivers fit for this truck");
//                return new DeliveryMenuView(); }
//            for (int i=0; i<drivers.size(); i++)
//                System.out.println(i+1 + ") "+ drivers.get(i).driverToString());
//            input = scanner.nextLine();
//            EmployeeModel employeeModel = drivers.get(Integer.parseInt(input)-1);
//            System.out.println("please choose which stock shortnesses you want to solve today (in format n,n,...):");
//            List<StockShortnessModel> stockShortnessModelList = backendController.getAllRelevantStockShortnesses();
//            if(stockShortnessModelList.size()==0) {
//                System.out.println("There are no stock shortnesses in any branches");
//                return new DeliveryMenuView(); }
//            for (int i=0; i<stockShortnessModelList.size(); i++)
//                System.out.println(i+1 + ") "+ stockShortnessModelList.get(i).toString());
//            input = scanner.nextLine();
//            String[] str = input.split(",");
//            List<StockShortnessModel> stockShortnessModels = new LinkedList<>();
//            for (String s: str) {
//                stockShortnessModels.add(stockShortnessModelList.get(Integer.parseInt(s)-1));
//            }
//            try {
//                DeliveryModel deliveryModel = backendController.CreateDelivery(dateToString(date), timeToString(time), truckM, employeeModel, stockShortnessModels);
//                return new DeliveryView(deliveryModel);
//            }
//            catch (Exception e){
//                if (e.getMessage().equals("the delivery contains multiple shipping areas"))
//                {
//                    System.out.println("the delivery contains multiple shipping area, do you wish to continue?");
//                    input = scanner.nextLine();
//                    if (input.equals("yes"))
//                    {
//                        DeliveryModel deliveryModel = backendController.CreateDelivery(dateToString(date), timeToString(time), truckM, employeeModel, stockShortnessModels);
//                        return new DeliveryView(deliveryModel);
//                    }
//                    else  return new DeliveryMenuView();
//                }
//                else throw new Exception(e.getMessage());
//            }
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            return new DeliveryMenuView();
//        }
//    }

//    private LocalDate checkDateValidity(String input) {
//        try {
//            return LocalDate.parse(input);
//        }
//        catch (Exception e){
//            System.out.println("not legal date format.");
//            System.out.println("please enter a date for the delivery (in format of 'YYYY-MM-DD'):");
//            Scanner scanner = new Scanner(System.in);
//            input = scanner.nextLine();
//            return checkDateValidity(input);
//        }
//    }

//    private Date checkTimeValidity(String input) {
//        try {
//            return new SimpleDateFormat("HH:mm").parse(input);
//        }
//        catch (Exception e){
//            System.out.println("not legal time format.");
//            System.out.println("please enter a time for the delivery (in format of 'hh:mm'):");
//            Scanner scanner = new Scanner(System.in);
//            input = scanner.nextLine();
//            return checkTimeValidity(input);
//        }
//    }

//    private String dateToString(LocalDate date){
//        String year = String.valueOf(date.getYear());
//        String month = String.valueOf(date.getMonthValue());
//        String day = String.valueOf(date.getDayOfMonth());
//        if (day.length()==1) day="0"+day;
//        if (month.length()==1) month="0"+month;
//        return  year + "-" + month + "-" +day;
//    }
//
//    private String timeToString(Date time){
//        String hour = String.valueOf(time.getHours());
//        if (hour.length()==1) hour="0"+hour;
//        String minute = String.valueOf(time.getMinutes());
//        if (minute.length()==1) minute="0"+minute;
//        return hour + ":" + minute;
//    }

    public View invalidInput() {
        System.out.println("invalid input!");
        return new DeliveryMenuView();
    }

    public View returnToMainMenuView() {
        return new MainMenuView();
    }

    public View getAllDeliveriesView() {
        return new AllDeliveriesView();
    }
}