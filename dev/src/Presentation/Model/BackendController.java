package Presentation.Model;

import BusinessLayer.EmployeeModule.Objects.Branch;
import Presentation.Model.DeliveryModuleModel.*;
import Presentation.Model.EmployeeModuleModel.EmployeeModel;
import Presentation.Model.EmployeeModuleModel.ShiftModel;
import Presentation.Model.InventoryModel.CategoryModel;
import Presentation.Model.InventoryModel.DamagedItemModel;
import Presentation.Model.InventoryModel.InventoryItemModel;
import Presentation.Model.InventoryModel.ShortageItemModel;
import Presentation.View.Printer;
import ServiceLayer.Objects.DeliveryObjects.FDelivery;
import ServiceLayer.Objects.DeliveryObjects.FSiteDoc;
import ServiceLayer.Objects.DeliveryObjects.FStockShortness;
import ServiceLayer.Objects.DeliveryObjects.FTruck;
import ServiceLayer.Objects.EmployeeObjects.FEmployee;
import ServiceLayer.Objects.EmployeeObjects.FShift;
import ServiceLayer.Objects.InventoryObjects.*;
import ServiceLayer.Objects.SupplierObjects.SBillOfQuantities;
import ServiceLayer.Objects.SupplierObjects.SSupplier;
import ServiceLayer.Responses.Response;
import ServiceLayer.Responses.ResponseT;
import BusinessLayer.SupplierBusiness.Contact;
import BusinessLayer.SupplierBusiness.DemandOrder;
import BusinessLayer.SupplierBusiness.FixedOrder;
import Presentation.Model.InventoryModel.*;
import Presentation.Model.SupplierModel.*;
import ServiceLayer.ApplicationService;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackendController {
    private final ApplicationService applicationService;
    private EmployeeModel loggedInUser;

    private BackendController() {
        this.applicationService = new ApplicationService();
    }

    public void logout() {
        loggedInUser = null;
    }

    public String getBranchOfManager(int ManagerID) {
        ResponseT<String> response = applicationService.getBranchOfManager(ManagerID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            return response.getValue();
        }
    }

    private static class BackendControllerHolder{
        private static BackendController instance = new BackendController();
    }

    public static void clearBackendControllerForTests() throws Exception {
        BackendControllerHolder.instance = new BackendController();
    }

    public static BackendController getInstance() {
        return BackendController.BackendControllerHolder.instance;
    }


    /////////////////EMPLOYEE/////////////////
    public List<EmployeeModel> getAllEmployees(){
        ResponseT<List<FEmployee>> response = applicationService.getAllEmployees();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> employeeModels = new Vector<>();
            for(FEmployee fEmployee : fEmployees)
                employeeModels.add(new EmployeeModel(fEmployee));
            return employeeModels;
        }
    }
    public EmployeeModel getEmployeeByID(int employeeID) {
        ResponseT<FEmployee> response = applicationService.getEmployee(employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            FEmployee fEmployee = response.getValue();
            return new EmployeeModel(fEmployee);
        }
    }
    public List<EmployeeModel> getAvailableEmployees(String date, String shiftType) {
        ResponseT<List<FEmployee>> response = applicationService.getAvailableEmployeesForShift(date, shiftType);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> employeeModels = new Vector<>();
            for(FEmployee fEmployee : fEmployees)
                employeeModels.add(new EmployeeModel(fEmployee));
            return employeeModels;
        }
    }
    public void addNewEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date, String jobTitle, String employmentDetails) {
        Response response = applicationService.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//
//            System.out.println("added new employee");
//        }
    }

    public void addNewEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date, String jobTitle, String employmentDetails, Vector<String> certifications) {
        Response response = applicationService.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails, certifications);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("added new employee");
//        }
    }

    public void deleteEmployee(int employeeID) {
        Response response = applicationService.removeEmployee(employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            System.out.println("deleted employee" + employeeID);
        }
    }
    public void addJobTitleToEmployee(int employeeID, String newJob) {
        Response response = applicationService.addJobToEmployee(employeeID, newJob);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            System.out.println("added new job to employee");
        }
    }

    public void addJobCertToEmployee(int empID, String jobTitle, String certName) {
        Response response = applicationService.addCertToEmployee(empID, jobTitle, certName);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("added new job Certification to employee");
//        }
    }
    public void removeJobTitleFromEmployee(int employeeID, String jobTitle) {
        Response response = applicationService.removeJobFromEmployee(employeeID, jobTitle);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("removed job from employee");
//        }
    }
    public void addJobConstraint(int id, String date, String shift_type) {
        Response response = applicationService.addJobConstraint(id, date, shift_type);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("added new job constraint to employee");
//        }
    }
    public void updateEmployee(int employeeID, String newInfo , String updatedInfo) {
        switch (updatedInfo) {
            case "name": updateEmployeeName(employeeID, newInfo);
                return;
            case "bank account": updateEmployeeBankAccount(employeeID, newInfo);
                return;
            case "salary": updateEmployeeSalary(employeeID, Integer.parseInt(newInfo));
                return;
            default: updateEmploymentDetails(employeeID, newInfo);
                return;
        }

    }
    public void updateEmployeeName(int employeeID, String newName) {
        Response response = applicationService.updateEmployeeName(employeeID, newName);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("updated employee name");
//        }
    }
    public void updateEmployeeBankAccount(int employeeID, String newBankAccount) {
        Response response = applicationService.updateEmployeeBankAccount(employeeID, newBankAccount);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("updated employee Bank Account");
//        }
    }
    public void updateEmployeeSalary(int employeeID, int newSalary) {
        Response response = applicationService.updateEmployeeSalary(employeeID, newSalary);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//            else{
//                System.out.println("updated employee Salary");
//            }
    }
    public void updateEmploymentDetails(int employeeID, String employmentDetails) {
        Response response = applicationService.updateEmploymentDetails(employeeID, employmentDetails);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("updated employee employmentDetails");
//        }
    }

    public List<Integer> getDriversToSpecificShift(String date) throws Exception
    {
        ResponseT<List<Integer>> response =applicationService.getDriversToSpecificShift(date);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            return response.getValue();
        }
    }


    /////////////////SHIFT/////////////////
    public ShiftModel getShiftByID(int shiftID) {
        ResponseT<FShift> response = applicationService.getShift(shiftID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            FShift fShift = response.getValue();
            return new ShiftModel(fShift);
        }
    }
    public List<ShiftModel> getAllShifts(){
        ResponseT<List<FShift>> response = applicationService.getAllShifts();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FShift> fShifts = response.getValue();
            List<ShiftModel> shiftModels = new Vector<>();
            for(FShift fShift : fShifts)
                shiftModels.add(new ShiftModel(fShift));
            return shiftModels;
        }
    }
    //
    public List<ShiftModel> getEmployeeShifts(int employeeID) {
        ResponseT<List<FShift>> response = applicationService.getShiftsOfEmployee(employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FShift> fShifts = response.getValue();
            List<ShiftModel> shiftModels = new Vector<>();
            for(FShift fShift : fShifts)
                shiftModels.add(new ShiftModel(fShift));
            return shiftModels;
        }
    }
    public void assignNewShift(String branch,String shiftDate, String shiftTime, HashMap<Integer, String> employeesToShift) {
        Response response = applicationService.addShift(branch,shiftDate,shiftTime,employeesToShift);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("shift added");
//        }
    }
    public void deleteShift(int shiftID) {
        Response response = applicationService.removeShift(shiftID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else {
//            System.out.println("shift deleted");
//        }
    }
    public LocalDate validDate(String date) {
        ResponseT<LocalDate> response = new ResponseT<>(LocalDate.parse(date));
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return response.getValue();
        }
    }
    public boolean validEmployee(String jobTitle, String employeeID) {
        ResponseT<Boolean> response = applicationService.validEmployeeHasJob(jobTitle,employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return true;
    }
    public ShiftModel getShiftByDateTypeAndBranch(String date, String typeShift, String branch) {
        ResponseT<FShift> response = applicationService.getShiftByDateTypeAndBranch(date,typeShift, branch);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return new ShiftModel(response.getValue());
        }
    }
    public boolean isEmployeeInShift(ShiftModel shift, int employeeID) {
        ResponseT<FShift> response = (applicationService.getShift(shift.getShiftID()));
        for (int e: response.getValue().getEmployees().keySet())
        {
            if (e==employeeID)
                return true;
        }
        return false;
    }
    public void replaceEmployees(ShiftModel sm, int oldID, String newID , String job) {
        Response response = applicationService.getShift(sm.getShiftID());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            response = applicationService.removeEmployeeFromShift(sm.getShiftID(),oldID);
            if (response.errorOccurred()){
                throw new IllegalArgumentException(response.getErrorMessage());
            }
            else{
                response = applicationService.addEmployeeToShift(sm.getShiftID(),Integer.parseInt(newID),job);
                if (response.errorOccurred()){
                    throw new IllegalArgumentException(response.getErrorMessage());
                }
            }
        }
    }
    public void removeEmployeeFromShift(int shiftID, int employeeID) {
        Response response = applicationService.getShift(shiftID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            response = applicationService.removeEmployeeFromShift(shiftID,employeeID);
            if (response.errorOccurred()){
                throw new IllegalArgumentException(response.getErrorMessage());
            }
        }
    }
    public void addEmployeeToShift(int sID, int eID , String job) {
        Response response = applicationService.addEmployeeToShift(sID,eID , job);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public void validEmployeesInShiftJob(HashMap<Integer, String> employeesToShift) {
        Response response = applicationService.validEmployeesInShiftJob(employeesToShift);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else {
//            System.out.println("employees jobs validated");
//        }
    }
    public void getShiftLastMonth(String morning_evening , int employeeID) {
        ResponseT<List<FShift>> response = applicationService.getShiftLastMonth(morning_evening,employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            for(FShift s : response.getValue())
                System.out.println(s.toString());
        }
    }






    /////////////////DELIVERY/////////////////
    public List<TruckModel> getAllTrucksAvailable(String date) {
        ResponseT<List<FTruck>> response = applicationService.getAllTrucksAvailable(date);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<FTruck> ftrucks = response.getValue();
        List<TruckModel> truckModels = new LinkedList<>();
        for (FTruck fTruck : ftrucks)
            truckModels.add(new TruckModel(fTruck));
        return truckModels;

    }


    public List<EmployeeModel> getDriversRelevant(int maxWeight, String date) {
        ResponseT<List<FEmployee>> response = applicationService.getDriversRelevant(maxWeight, date);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> EmployeeModels = new LinkedList<>();
            for(FEmployee fEmployee : fEmployees)
                EmployeeModels.add(new EmployeeModel(fEmployee));
            return EmployeeModels;
        }
    }

    public List<StockShortnessModel> getAllRelevantStockShortnesses() {
        ResponseT<List<FStockShortness>> response = applicationService.getAllRelevantStockShortnesses();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FStockShortness> fStockShortnesses = response.getValue();
            List<StockShortnessModel> stockShortnessModelList = new LinkedList<>();
            for(FStockShortness fStockShortness : fStockShortnesses)
                stockShortnessModelList.add(new StockShortnessModel(fStockShortness));
            return stockShortnessModelList;
        }
    }

    public DeliveryModel CreateDelivery(String date, String time, TruckModel truckM, EmployeeModel EmployeeM, List<StockShortnessModel> stockShortnessModels, boolean checkShippingArea) throws ParseException {
        List<Integer> stockShortnesses = new LinkedList<>();
        for (StockShortnessModel stockShortnessModel: stockShortnessModels)
            stockShortnesses.add(stockShortnessModel.getId());
        ResponseT<FDelivery> response = applicationService.createDelivery(date,time, truckM.getLicenseNumber(), EmployeeM.getID(), stockShortnesses, checkShippingArea);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            FDelivery fDelivery = response.getValue();
            return new DeliveryModel(fDelivery);
        }
    }

    public List<DeliveryModel> getAllDeliveries() throws ParseException {
        ResponseT<List<FDelivery>> response = applicationService.getAllDeliveries();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FDelivery> fDeliveries = response.getValue();
            List<DeliveryModel> deliveryModels = new LinkedList<>();
            for(FDelivery fDelivery : fDeliveries)
                deliveryModels.add(new DeliveryModel(fDelivery));
            return deliveryModels;
        }
    }

    public TruckModel getTruckByLicense(String truckLicense) {
        ResponseT<FTruck> response = applicationService.getTruckByLicense(truckLicense);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            FTruck fTruck = response.getValue();
            return new TruckModel(fTruck);
        }
    }

//    public DriverModel getDriverById(int driverId) {
//        ResponseT<FEmployee> response = applicationFacade.getDriverById(driverId);
//        if (response.errorOccurred()){
//            throw new IllegalArgumentException(response.getErrorMessage());
//        }
//        else{
//            FDriver fDriver = response.getValue();
//            return new DriverModel(fDriver, this);
//        }
//    }

    public List<SiteDocumentModel> getAllDocumentsForDelivery(int deliveryId) {
        ResponseT<List<FSiteDoc>> response = applicationService.getAllDocumentsForDelivery(deliveryId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FSiteDoc> fSiteDocs = response.getValue();
            List<SiteDocumentModel> siteDocumentModels = new LinkedList<>();
            for(FSiteDoc fSiteDoc : fSiteDocs)
                siteDocumentModels.add(new SiteDocumentModel(fSiteDoc));
            return siteDocumentModels;
        }
    }

    public Integer getLatestAddress(int deliveryId) {
        ResponseT<Integer> response = applicationService.getLatestAddress(deliveryId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return response.getValue();
        }
    }

    public List<SiteDocumentModel> getDocumentForDeliveryAndSite(int deliveryId, String siteAddress) {
        ResponseT<List<FSiteDoc>> response = applicationService.getDocumentForDeliveryAndSite(deliveryId, siteAddress);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FSiteDoc> fSiteDocs = response.getValue();
            List<SiteDocumentModel> siteDocumentModelList = new LinkedList<>();
            for (FSiteDoc fSiteDoc: fSiteDocs) {
                siteDocumentModelList.add(new SiteDocumentModel(fSiteDoc));
            }
            return siteDocumentModelList;
        }
    }

    public List<String> getListOfAddressesForDelivery(int deliveryId) {
        ResponseT<List<String>> response = applicationService.getListOfAddressesForDelivery(deliveryId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return response.getValue();
        }
    }

    public void weightTruck(SiteDocumentModel siteDocumentModel, int weight) {
        Response response = applicationService.weightTruck(siteDocumentModel.getDocumentId(), weight);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public void arrivedToSite(SiteDocumentModel siteDocumentModel) {
        Response response = applicationService.arrivedToSite(siteDocumentModel.getDocumentId());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public boolean isDeliveryFinished(DeliveryModel deliveryModel) {
        ResponseT<Boolean> response = applicationService.isDeliveryFinished(deliveryModel.getId());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        return response.getValue();
    }

    public void changeTruck(int documentId, TruckModel truckM) {
        Response response = applicationService.changeTruck(documentId, truckM.getLicenseNumber());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public void changeSites(int DocumentId, List<StockShortnessModel> stockShortnessModelsAdd, List<StockShortnessModel> stockShortnessModelsDrop, boolean checkShippingArea) {
        List<Integer> addStock = new LinkedList<>();
        for(StockShortnessModel stockShortnessModel: stockShortnessModelsAdd)
            addStock.add(stockShortnessModel.getId());
        List<Integer> dropStock = new LinkedList<>();
        for(StockShortnessModel stockShortnessModel: stockShortnessModelsDrop)
            dropStock.add(stockShortnessModel.getId());
        Response response = applicationService.changeSites(DocumentId, dropStock, addStock, checkShippingArea);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public List<StockShortnessModel> getStockShortnessOfDelivery(DeliveryModel deliveryModel) {
        ResponseT<List<FStockShortness>> response = applicationService.getStockShortnessOfDelivery(deliveryModel.getId());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FStockShortness> fStockShortnesses = response.getValue();
            List<StockShortnessModel> stockShortnessModelList = new LinkedList<>();
            for(FStockShortness fStockShortness : fStockShortnesses)
                stockShortnessModelList.add(new StockShortnessModel(fStockShortness));
            return stockShortnessModelList;
        }
    }

    public List<TruckModel> getAllRelevantTrucks(String dateString, int siteDocId) {
        ResponseT<List<FTruck>> response = applicationService.getAllRelevantTrucks(dateString, siteDocId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FTruck> fTrucks = response.getValue();
            List<TruckModel> truckModels = new LinkedList<>();
            for(FTruck fTruck : fTrucks)
                truckModels.add(new TruckModel(fTruck));
            return truckModels;
        }
    }

    public List<String> getDriverLicencesForDriver(int id) {
        ResponseT<List<String>> response = applicationService.getDriverLicencesForDriver(id);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }

    public List<String> getAllBranchesByAddress() {
        ResponseT<List<String>> response = applicationService.getAllBranchesByAddress();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }
    public List<String> getAllBranchesEmployeeWorksIn(int ID) {
        ResponseT<List<String>> response = applicationService.getAllBranchesEmployeeWorksIn(ID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }

    public String getAddressOfHQ() {
        ResponseT<Branch> response = applicationService.getAddressOfHQ();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue().getBranchAddress();
    }

    public List<EmployeeModel> getAvailableDrivers(String date) {
        ResponseT<List<FEmployee>> response = applicationService.getAvailableDrivers(date);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> employeeModels = new Vector<>();
            for(FEmployee fEmployee : fEmployees)
                employeeModels.add(new EmployeeModel(fEmployee));
            return employeeModels;
        }
    }

    public boolean isDeliveryManager(String input) {
        try {
            Response response = applicationService.getEmployee(Integer.parseInt(input));
            if (response.errorOccurred()){
                return false;
            }
            else {
                return applicationService.validEmployeeHasJob("delivery manager",input).getValue();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public SiteDocumentModel getDocumentForDeliveryAndlocation(int deliveryId, int LocationInAddressList) {
        ResponseT<FSiteDoc> response = applicationService.getDocumentForDeliveryAndlocation(deliveryId, LocationInAddressList);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            FSiteDoc fSiteDoc = response.getValue();
            return new SiteDocumentModel(fSiteDoc);
        }
    }

    public boolean findJobEmployee(String input,String actualJob) {
        try {
            ResponseT<FEmployee> response = applicationService.getEmployee(Integer.parseInt(input));
            if (response.errorOccurred()){
                return false;
            }
            else {
                if (applicationService.validEmployeeHasJob(actualJob,input).getValue())
                {
                    loggedInUser = new EmployeeModel(response.getValue());
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void enterBranch(String branchAddress)
    {
        loggedInUser.setBranch(branchAddress);
    }

    public List<String> pullMessages(String branchAddress , String job) {
        ResponseT<List<String>> response = applicationService.pullMessages(branchAddress,job);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }





    ////////////////////INVENTORY//////////////////////////
    public void removeCategory(String categoryID) {
        Response response = applicationService.removeCategory(loggedInUser.getBranch(), categoryID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void orderShortage() {
        Response response = applicationService.orderShortage(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void buyItem(ItemModel itemModel, int amount) { //that
        Response response = applicationService.removeQuantityFromItem(loggedInUser.getBranch(), itemModel.getItemID(),amount,false);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addDiscount(ItemModel item, String disName, double discountFare, LocalDate fromDate, LocalDate toDate) {
        Response response = applicationService.addDiscount(loggedInUser.getBranch(), item.getItemID(),disName,fromDate,toDate,discountFare);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<Integer> getAllItemFrontShelves(ItemModel itemModel) {
        ResponseT<List<Integer>> response = applicationService.getAllFrontShelvesByItem(loggedInUser.getBranch(), itemModel.getItemID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }

    public List<Integer> getAllItemBackShelves( ItemModel itemModel) {
        ResponseT<List<Integer>> response = applicationService.getAllBackShelvesByItem(loggedInUser.getBranch(), itemModel.getItemID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }

    public void moveItemLocation(int itemID, boolean fromBackRoom, int checkedAmount) {
        Response response = applicationService.moveItemsBetweenRooms(loggedInUser.getBranch(), itemID,fromBackRoom, checkedAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void removeQuantityFromItem(int itemID, boolean inBackRoom, int checkedAmount) {
        Response response = applicationService.removeQuantityFromItem(loggedInUser.getBranch(), itemID, checkedAmount, inBackRoom);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addQuantityToItem(int itemID, boolean inBackRoom, int checkedAmount2) {
        Response response = applicationService.addQuantityToItem(loggedInUser.getBranch(), itemID, checkedAmount2, inBackRoom);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void removeDiscount(ItemModel itemModel, LocalDate fromDate) {
        try {
            applicationService.removeDiscount(loggedInUser.getBranch(), itemModel.getItemID(), fromDate);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void changeItemPrice(int itemID, int checkedAmount) {
        try {
            applicationService.changeItemPrice(loggedInUser.getBranch(), itemID, checkedAmount);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void changeMinimalQuantity(int itemID, int checkedQuantity) {
        applicationService.changeMinimalQuantity(loggedInUser.getBranch(), itemID, checkedQuantity);
    }

    public void changeFullQuantity(int itemID, int checkedQuantity) {
        applicationService.changeFullQuantity(loggedInUser.getBranch(), itemID, checkedQuantity);
    }

    public void addDamagedRecord(int desiredItemID, int amount, PDamageReason reason, int back0Front1) {
        Response response = applicationService.addDamagedRecord(loggedInUser.getBranch(), desiredItemID,amount, FDamageReason.valueOf(reason.toString()),back0Front1);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<Integer> getAllFrontShelves() {
        ResponseT<List<Integer>> response = applicationService.getAllFrontShelves(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }
    public List<Integer> getAllBackShelves() {
        ResponseT<List<Integer>> response = applicationService.getAllBackShelves(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }

    public CategoryModel getCategoryFather(CategoryModel categoryModel) {
        ResponseT<FCategory> response = applicationService.getCategoryFather(loggedInUser.getBranch(), categoryModel.getCatFatherID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return new CategoryModel(response.getValue());
    }

    public ItemModel addItem(String name, String catID, double price, int minimalQuantity, int fullQuantity, String manufacture, List<Integer> backShelves, List<Integer> frontShelves){
        ResponseT<FItem> response = applicationService.addItem(loggedInUser.getBranch(), name, catID, price, minimalQuantity, fullQuantity, manufacture, backShelves, frontShelves);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return new ItemModel(response.getValue());
    }

    public void addShelf(boolean isInBack) {
        Response response = applicationService.addShelf(loggedInUser.getBranch(), isInBack);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addCategory(CategoryModel cat) {
        Response response = applicationService.addCategory(loggedInUser.getBranch(), cat.getCatFatherID(),cat.getName());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addPurchase(int desiredItemID, int orderID, int checkedSupplier, int checkedAmount, double checkedPrice, double checkedDiscount) {
        Response response = applicationService.addPurchaseRecord(loggedInUser.getBranch(), desiredItemID,orderID,checkedSupplier,checkedAmount,checkedPrice,checkedDiscount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public CategoryModel getCategory(CategoryModel categoryModel) {//todo check this func???
        ResponseT<FCategory> response= applicationService.getCategory(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            CategoryModel cat = new CategoryModel (response.getValue());
            return cat;
        }
    }
    public CategoryModel getCategory(String ID) {
        ResponseT<FCategory> response= applicationService.getCategory(loggedInUser.getBranch(), ID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            CategoryModel cat = new CategoryModel (response.getValue());
            return cat;
        }
    }

    public ItemModel getItem(int itemID) {
        ResponseT<FItem> response= applicationService.getItem(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            ItemModel item = new ItemModel(response.getValue());
            return item;
        }
    }

    public CategoryModel [] getSubCat(CategoryModel categoryModel) {
        ResponseT<List<FCategory>> response= applicationService.getAllCategorySubCat(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FCategory> catList = response.getValue();
            CategoryModel[] catArr = new CategoryModel[catList.size()];
            int counter = 0;
            for (FCategory cat : catList) {
                catArr[counter] = new CategoryModel(cat);
                counter++;
            }
            return catArr;
        }
    }

    public void moveCategory(CategoryModel MfatherCat, CategoryModel categoryModel) {
        FCategory fatherCat = new FCategory(MfatherCat.getName(),MfatherCat.getCatID(),MfatherCat.getCatFatherID());
        FCategory category = new FCategory(categoryModel.getName(),categoryModel.getCatID(),categoryModel.getCatFatherID());
        Response response = applicationService.moveCategory(loggedInUser.getBranch(), fatherCat,category);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<ItemModel> getCategoryItems(CategoryModel categoryModel) {
        ResponseT<List<FItem>> response =  applicationService.getAllItemsInCategory(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<ItemModel> items = new LinkedList<>();
        for (FItem it : response.getValue()){
            items.add(new ItemModel(it));
        }
        return items;
    }

    public void setCategoryName(CategoryModel categoryModel) {
        Response response = applicationService.setCategoryName(loggedInUser.getBranch(), categoryModel.getCatID(),categoryModel.getName());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<ShortageItemModel> getShortageReport(){
        ResponseT<List<FInShortageItem>> response = applicationService.getShortageReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<ShortageItemModel> items = new LinkedList<>();
        for(FInShortageItem shortageItem : response.getValue()){
            items.add(new ShortageItemModel(shortageItem));
        }
        return items;
    }


    public List<InventoryItemModel> getInventoryReport() {
        ResponseT<List<FInventoryItem>> response = applicationService.getInventoryReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<InventoryItemModel> items = new LinkedList<>();
        for(FInventoryItem inventoryItem : response.getValue()){
            items.add(new InventoryItemModel(inventoryItem));
        }
        return items;
    }

    public List<InventoryItemModel> getInventoryByCategoryReport(CategoryModel categoryModel) {
        ResponseT<List<FInventoryItem>> response = applicationService.getInventoryReportByCategory(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<InventoryItemModel> items = new LinkedList<>();
        for(FInventoryItem inventoryItem : response.getValue()){
            items.add(new InventoryItemModel(inventoryItem));
        }
        return items;
    }

    public List<DamagedItemModel> getDamagedReport() {
        ResponseT<List<FDamagedItem>> response = applicationService.getDamageReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getDamagedReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FDamagedItem>> response = applicationService.getDamageReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getDamagedReportByItemID(int itemID) {
        ResponseT<List<FDamagedItem>> response = applicationService.getDamageReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyExpiredReport() {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyExpiredReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyDamagedReport() {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyDamageReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyDamagedReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyDamageReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyExpiredReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyExpiredReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyDamagedReportByItemID(int itemID) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyDamageReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyExpiredReportByItemID(int itemID) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyExpiredReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<PurchasedItemModel> getPurchaseReport() {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem purchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(purchasedItem));
        }
        return items;
    }

    public List<PurchasedItemModel>getPurchaseReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem fPurchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(fPurchasedItem));
        }
        return items;
    }

    public List<PurchasedItemModel> getPurchaseReportByItemID(int itemID) {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem fPurchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(fPurchasedItem));
        }
        return items;
    }

    public List<PurchasedItemModel> getPurchaseReportByBusinessNumber(int supplierNumber) {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReportByBusinessNumber(loggedInUser.getBranch(), supplierNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem fPurchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(fPurchasedItem));
        }
        return items;
    }


    public void changeAllItemBackShelves(ItemModel itemModel, int[] idsIntArr) {
        Response response = applicationService.changeItemAppointedShelvesByRoom(loggedInUser.getBranch(), itemModel.getItemID(),true,idsIntArr);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void changeAllItemFrontShelves(ItemModel itemModel, int[] idsIntArr) {
        Response response = applicationService.changeItemAppointedShelvesByRoom(loggedInUser.getBranch(), itemModel.getItemID(),false,idsIntArr);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void giveDiscountToCategory(CategoryModel categoryModel, double discountFare, String discountName, LocalDate fromDate, LocalDate toDate) {
        FDiscount fDiscount = new FDiscount(discountName,fromDate,toDate,discountFare);
        applicationService.giveDiscountToCategory(loggedInUser.getBranch(), categoryModel.getCatID(),fDiscount);
    }

    //    public void orderShortage(List<Pair<Integer, Integer>> itemsToOrder) {
//        applicationService.orderShortage(itemsToOrder);
//    }
//supplier----------------------------------------------------------------------------------
    public List<SupplierModel> getAllSuppliers() {
        ResponseT<List<SSupplier>> response = applicationService.getAllSuppliers();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<SSupplier> sSuppliers = response.getValue();
            List<SupplierModel> supplierModels = new ArrayList<>();
            for (SSupplier sSupplier : sSuppliers)
                supplierModels.add(new SupplierModel(sSupplier));
            return supplierModels;
        }
    }

    public List<ContactModel> getAllContacts(int businessNumber) {
        ResponseT<List<Contact>> response = applicationService.getAllContacts(businessNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<Contact> contacts = response.getValue();
            List<ContactModel> contactModels = new ArrayList<>();
            for (Contact contact : contacts)
                contactModels.add(new ContactModel(contact));
            return contactModels;
        }
    }

    public SupplierModel getSupplier(int supplierBN) {
        ResponseT<SSupplier> response = applicationService.getSupplier(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            SSupplier sSupplier = response.getValue();
            return new SupplierModel(sSupplier);
        }
    }

    public void addSupplier(String name, int businessNumber, int bankAccount, boolean shouldDeliver, String paymentMethodStr, Set<DayOfWeek> daysOfSupply, String address) {
        Response response = applicationService.addSupplier(name, businessNumber, bankAccount, shouldDeliver, paymentMethodStr, daysOfSupply, address);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier added successfully");
    }

    public void removeSupplier(int supplierBN) {
        Response response = applicationService.removeSupplier(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier removed successfully");
    }

    public void updateSupplierName(int supplierBN, String newName) {
        Response response = applicationService.updateSupplierName(supplierBN, newName);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier name updated successfully");
    }

    public void updateSupplierBankAccount(int supplierBN, int newBankAccount) {
        Response response = applicationService.updateSupplierBankAccount(supplierBN, newBankAccount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier bank account updated successfully");
    }

    public void updateSupplierDelivery(int supplierBN, boolean newDeliveryMethod) {
        Response response = applicationService.updateSupplierDelivery(supplierBN, newDeliveryMethod);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier delivery method updated successfully");
    }

    public void updateSupplierPaymentMethod(int supplierBN, String newPaymentMethod) {
        Response response = applicationService.updateSupplierPaymentMethod(supplierBN, newPaymentMethod);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier payment method updated successfully");
    }

    public void addContact(int supplierBN, String contactName, String phoneNumber, String email) {
        Response response = applicationService.addContact(supplierBN, contactName, phoneNumber, email);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier contact added successfully");
    }

    public void removeContact(int supplierBN, int contactId) {
        Response response = applicationService.removeContact(supplierBN, contactId);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier contact removed successfully");
    }

    public void updateContactName(int supplierBN, int contactId, String newContactName) {
        Response response = applicationService.updateContactName(supplierBN, contactId, newContactName);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Contact name updated successfully");
    }

    public void updateContactEmail(int supplierBN, int contactId, String newEmail) {
        Response response = applicationService.updateContactEmail(supplierBN, contactId, newEmail);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Contact email updated successfully");
    }

    public void updateContactPhone(int supplierBN, int contactId, String newPhoneNumber) {
        Response response = applicationService.updateContactPhone(supplierBN, contactId, newPhoneNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Contact Phone updated successfully");
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    //orders-----------------------------------------------------------------------------------------------------------------------
    public void addFixedOrder(int supplierBN, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.addFixedOrder(loggedInUser.getBranch(), supplierBN, orderDate, supplyDays, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Fixed Order added successfully");
    }

    public void addFixedOrders(Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.addFixedOrders(loggedInUser.getBranch(), orderDate, supplyDays, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Fixed Orders added successfully");
    }

    public void addDemandOrders(Date orderDate, Date supplyDay, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.addDemandOrders(loggedInUser.getBranch(), orderDate, supplyDay, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand Orders added successfully");
    }

    public void addProductsToFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.addProductsToFixedOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products added to fixed order successfully");
    }

    public void removeProductsFromFixedOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) {
        Response response = applicationService.removeProductsFromFixedOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products removed from fixed order successfully");
    }

    public void updateProductsOfFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.updateProductsOfFixedOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products of fixed order updated successfully");
    }

    public void addProductsToDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.addProductsToDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products added to fixed order successfully");
    }

    public void removeProductsFromDemandOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) {
        Response response = applicationService.removeProductsFromDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products removed from fixed order successfully");
    }

    public void updateProductsOfDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.updateProductsOfDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products of fixed order updated successfully");
    }

    public void addDemandOrder(int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) {
        Response response = applicationService.addDemandOrder(loggedInUser.getBranch(), supplierBN, orderDate, supplyDate, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand Order added successfully");
    }

    public void removeUnSuppliedDemandOrder(int supplierBN, int orderId) {
        Response response = applicationService.removeUnSuppliedDemandOrder(supplierBN, orderId);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Un supplied demand order removed successfully");
    }

    public List<DemandOrderModel> getAllSupplierDemandOrders(int supplierBN) {
        ResponseT<List<DemandOrder>> response = applicationService.getAllSupplierDemandOrders(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<DemandOrder> demandOrders = response.getValue();
            List<DemandOrderModel> demandOrderModels = new ArrayList<>();
            for (DemandOrder demandOrder : demandOrders)
                demandOrderModels.add(new DemandOrderModel(demandOrder));
            return demandOrderModels;
        }
    }

    public List<DemandOrderModel> getAllDemandOrders() {
        ResponseT<List<DemandOrder>> response = applicationService.getAllDemandOrders();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<DemandOrder> demandOrders = response.getValue();
            List<DemandOrderModel> demandOrderModels = new ArrayList<>();
            for (DemandOrder demandOrder : demandOrders)
                demandOrderModels.add(new DemandOrderModel(demandOrder));
            return demandOrderModels;
        }
    }

    public List<FixedOrderModel> getAllFixedOrders() {
        ResponseT<List<FixedOrder>> response = applicationService.getAllFixedOrders();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FixedOrder> fixedOrders = response.getValue();
            List<FixedOrderModel> fixedOrderModels = new ArrayList<>();
            for (FixedOrder fixedOrder : fixedOrders)
                fixedOrderModels.add(new FixedOrderModel(fixedOrder));
            return fixedOrderModels;
        }
    }

    public List<FixedOrderModel> getAllSupplierFixedOrders(int supplierBN) {
        ResponseT<List<FixedOrder>> response = applicationService.getAllSupplierFixedOrders(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FixedOrder> fixedOrders = response.getValue();
            List<FixedOrderModel> fixedOrderModels = new ArrayList<>();
            for (FixedOrder fixedOrder : fixedOrders)
                fixedOrderModels.add(new FixedOrderModel(fixedOrder));
            return fixedOrderModels;
        }
    }

    public void supplyDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        Response response = applicationService.supplyDemandOrder(supplierBN, orderId, itemIdAndUnSuppliedAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand order supplied successfully");
    }

    public void unActiveFixedOrder(int supplierBN, int orderId, Date date) {
        Response response = applicationService.unActiveFixedOrder(supplierBN, orderId, date);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Fixed order unactivated successfully");
    }

    public void sendPDFDOrder(int supplierBN, int orderId, String email) {
        System.out.print("Sending email");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(notifier, 0, 1, TimeUnit.SECONDS);
        Response response = applicationService.sendPDFDOrder(supplierBN, orderId, email);
        scheduler.shutdownNow();
        System.out.println();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand order sent successfully");
    }


    public List<FixedOrderModel> dailyFixedOrders() {
        ResponseT<List<FixedOrder>> response = applicationService.dailyFixedOrders();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FixedOrder> fixedOrders = response.getValue();
            List<FixedOrderModel> fixedOrderModels = new ArrayList<>();
            for (FixedOrder fixedOrder : fixedOrders)
                fixedOrderModels.add(new FixedOrderModel(fixedOrder));
            return fixedOrderModels;
        }
    }

    public void addSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        Response response = applicationService.addSupplyDayToFixedOrder(supplierBN, orderId, day);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supply day added to fixed order successfully");
    }

    public void removeSupplyDayFromFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        Response response = applicationService.removeSupplyDayToFixedOrder(supplierBN, orderId, day);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supply day removed from fixed order successfully");
    }

    public void createNextWeekFixedOrders() {
        Response response = applicationService.createNextWeekFixedOrders(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("next week orders created successfully");
    }
    //-----------------------------------------------------------------------------------------------------------------------------
    // bill of quantity-----------------------------------------------------------------------------------------------------
    public BillOfQuantityModel getBillOfQuantity(SupplierModel supplierModel) {
        ResponseT<SBillOfQuantities> response = applicationService.getBillOfQuantity(loggedInUser.getBranch(), supplierModel.getBusinessNumber());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            return new BillOfQuantityModel(response.getValue(), supplierModel);
    }

    public void updateItemPrice(int supplierBN, int catalogNumber, double newPrice) {
        Response response = applicationService.updateItemPrice(loggedInUser.getBranch(), supplierBN, catalogNumber, newPrice);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item price updated successfully");
    }

    public Map<Integer, Double> getItemDiscounts(int supplierBN, int catalogNumber) {
        ResponseT<Map<Integer, Double>> response = applicationService.getItemDiscounts(loggedInUser.getBranch(), supplierBN, catalogNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            return response.getValue();
    }

    public void addItemDiscountAccordingToAmount(int supplierBN, int catalogNumber, int amount, double discount) {
        Response response = applicationService.addItemDiscountAccordingToAmount(loggedInUser.getBranch(), supplierBN, catalogNumber, amount, discount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item discount according to amount added successfully");
    }

    public void updateItemDiscountAccordingToAmount(int supplierBN, int catalogNumber, int amount, double newDiscount) {
        Response response = applicationService.updateItemDiscountAccordingToAmount(loggedInUser.getBranch(), supplierBN, catalogNumber, amount, newDiscount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item discount according to amount updated successfully");
    }

    public void removeItemDiscountAccordingToAmount(int supplierBN, int catalogNumber, int amount) {
        Response response = applicationService.removeItemDiscountAccordingToAmount(loggedInUser.getBranch(), supplierBN, catalogNumber, amount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item discount according to amount removed successfully");
    }

    public void updateSupplierCatalog(int supplierBN, int catalogNumber, int newSupplierCatalog) {
        Response response = applicationService.updateSupplierCatalog(loggedInUser.getBranch(), supplierBN, catalogNumber, newSupplierCatalog);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item supplier catalog number updated successfully");
    }

    public void updateItemName(int supplierBN, int catalogNumber, String newName) {
        Response response = applicationService.updateItemName(loggedInUser.getBranch(), supplierBN, catalogNumber, newName);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item name updated successfully");
    }

    public void addItemToSupplier(int supplierBN, int catalogNumber, int supplierCatalog, double price, String name) {
        Response response = applicationService.addItemToSupplier(loggedInUser.getBranch(), supplierBN, catalogNumber, supplierCatalog, price, name);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item added successfully");
    }

    public void removeItemFromSupplier(int supplierBN, int catalogNumber) {
        Response response = applicationService.removeItemFromSupplier(loggedInUser.getBranch(), supplierBN, catalogNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item removed successfully");
    }

    //---------------------------------------------------------------------------------------------------
    public void loadDataForTests() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        try {
            System.out.print("Loading data");
            scheduler.scheduleAtFixedRate(notifier, 0, 1, TimeUnit.SECONDS);
            loadData();
            scheduler.shutdownNow();
            System.out.println();
            System.out.println("Success! uploaded all the fake data for tests");
        }
        catch (Exception e){
            scheduler.shutdownNow();
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem : Returning to report main menu");
        }
    }

    private void loadData() throws Exception {
        loadInventoryData();
        loadSupplierData();
        loadEmployeeData();
        loadDeliveriesData();
    }

    private void loadInventoryData() {
        loadCategoriesData();
        loadShelvesAndItemsData();
        loadItemsDiscountData();
        loadQuantityForItemsData();
        loadDamagedRecordData();
    }

    private void loadCategoriesData() {
        applicationService.addCategory("Yasmin 3 , Dimona", "0", "Drinks"); // 0#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1", "Sparkled Drinks");// 0#1#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#1", "1L");// 0#1#1#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#1", "1.5L");// 0#1#1#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#1", "2L");// 0#1#1#3
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1", "Juices");// 0#1#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#2", "1L");// 0#1#2#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#2", "1.5L");// 0#1#2#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#2", "2L");// 0#1#2#3
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1", "Liquors");// 0#1#3
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#3", "1L");// 0#1#3#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#3", "1.5L");// 0#1#3#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#1#3", "2L");// 0#1#3#3

        applicationService.addCategory("Yasmin 3 , Dimona", "0", "Snacks");// 0#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#2", "Osem");// 0#2#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#2#1", "Big");// 0#2#1#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#2#1", "Small");// 0#2#1#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#2", "Elit");// 0#2#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#2#2", "Big");// 0#2#2#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#2#2", "Small");// 0#2#2#2

        applicationService.addCategory("Yasmin 3 , Dimona", "0", "Vegan");// 03
        applicationService.addCategory("Yasmin 3 , Dimona", "0#3", "Meat Alternatives");// 0#3#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#3#1", "Teva-Deli");// 0#3#1#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#3#1", "Tnuva");// 0#3#1#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#3", "Milk Alternatives");// 0#3#2
        applicationService.addCategory("Yasmin 3 , Dimona", "0#3#2", "Alpro");// 0#3#2#1
        applicationService.addCategory("Yasmin 3 , Dimona", "0#3#2", "Tnuva");// 0#3#2#2
    }

    private void loadShelvesAndItemsData() {
        for (int i = 0; i < 5; i++)
            applicationService.addShelf("BranchTest", true);
        for (int i = 0; i < 5; i++)
            applicationService.addShelf("BranchTest", false);

        List <Integer> snacksBackShelf = new LinkedList<>();
        snacksBackShelf.add(1);
        snacksBackShelf.add(2);
        List <Integer> snacksFrontShelf = new LinkedList<>();
        snacksFrontShelf.add(6);
        snacksFrontShelf.add(7);

        List <Integer> drinksBackShelf = new LinkedList<>();
        drinksBackShelf.add(3);
        drinksBackShelf.add(4);
        List <Integer> drinksFrontShelf = new LinkedList<>();
        drinksFrontShelf.add(8);
        drinksFrontShelf.add(9);

        List <Integer> veganBackShelf = new LinkedList<>();
        veganBackShelf.add(5);
        List <Integer> veganFrontShelf = new LinkedList<>();
        veganFrontShelf.add(10);
        applicationService.addItem("Yasmin 3 , Dimona", "Bamba Big", "0#2#1#1", 10, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Bamba Small", "0#2#1#2", 5, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Bisli Big", "0#2#1#1", 10, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Bisli Small", "0#2#1#2", 5, 20, 50, "Osem",snacksBackShelf,snacksFrontShelf);
        //5
        applicationService.addItem("Yasmin 3 , Dimona", "Coca Cola 1L", "0#1#1#1", 6, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Coca Cola 1.5L", "0#1#1#2", 8, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Coca Cola 2L", "0#1#1#3", 10, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Sprite 1L", "0#1#1#1", 6, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Sprite 1.5L", "0#1#1#2", 8, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Sprite 2L", "0#1#1#3", 10, 20, 50, "Coca Cola",drinksBackShelf,drinksFrontShelf);
        //11
        applicationService.addItem("Yasmin 3 , Dimona", "Iced tea 1L", "0#1#2#1", 6, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Iced tea 1.5L", "0#1#2#2", 8, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "Iced tea 2L", "0#1#2#3", 10, 20, 50, "Nes-Tea",drinksBackShelf,drinksFrontShelf);
        //14
        applicationService.addItem("Yasmin 3 , Dimona", "JagerMeister Liquor", "0#1#3#1", 70, 5, 20, "Jager",drinksBackShelf,drinksFrontShelf);
        applicationService.addItem("Yasmin 3 , Dimona", "FireBall Whisky", "0#1#3#1", 70, 5, 20, "FireBall",drinksBackShelf,drinksFrontShelf);
    }

    private void loadItemsDiscountData() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        int currentDay = now.getDayOfMonth();
        applicationService.addDiscount("Yasmin 3 , Dimona", 14, "JagerMeister Discount Check (30 from today)", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2023, 1, 1), 0.3);
        applicationService.addDiscount("Yasmin 3 , Dimona", 1, "Bamba Big Discount Check (50 from today)", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.75);
        applicationService.addDiscount("Yasmin 3 , Dimona", 8, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        applicationService.addDiscount("Yasmin 3 , Dimona", 9, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        applicationService.addDiscount("Yasmin 3 , Dimona", 10, "Sprite summer Discount", LocalDate.of(currentYear, currentMonth, currentDay), LocalDate.of(2022, 7, 1), 0.5);
        applicationService.addDiscount("Yasmin 3 , Dimona", 3, "Bisli Big Future Discount", LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 20), 0.75);
    }

    private void loadQuantityForItemsData() {
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 1, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 1, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 2, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 2, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 3, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 3, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 4, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 4, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 5, 40, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 5, 40, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 6, 40, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 6, 40, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 7, 40, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 7, 40, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 8, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 8, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 9, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 9, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 10, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 10, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 11, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 11, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 12, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 12, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 13, 30, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 13, 20, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 14, 15, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 14, 5, false);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 15, 15, true);
        applicationService.addQuantityToItem("Yasmin 3 , Dimona", 15, 5, false);
    }

    private void loadDamagedRecordData() {
        applicationService.addDamagedRecord("Yasmin 3 , Dimona", 4, 10,  FDamageReason.Damaged, 1);
        applicationService.addDamagedRecord("Yasmin 3 , Dimona", 2, 10,  FDamageReason.Expired, 0);
    }

    private void loadSupplierData() {
        loadSuppliersData();
        loadSupplierItemsData();
        loadSupplierItemsDiscountData();
    }

    private void loadSuppliersData() {
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>();
        dayOfWeekSet.add(DayOfWeek.SUNDAY);
        dayOfWeekSet.add(DayOfWeek.WEDNESDAY);
        Set<DayOfWeek> dayOfWeekSet2 = new HashSet<>();
        dayOfWeekSet2.add(DayOfWeek.MONDAY);
        applicationService.addSupplier("Coca Cola", 1234, 1111, true, "CreditCard", dayOfWeekSet, "30 yaakov st.");
        applicationService.addSupplier("Osem", 4321, 2222, true, "Cash", dayOfWeekSet2, "23 david st.");
        applicationService.addContact(1234, "Eli Kurin", "0542888449", "eli@gmail.com");
        applicationService.addContact(4321, "Yoav Avital", "0542658742", "yoavital98@gmail.com");
    }

    private void loadSupplierItemsData() {
        //Osem
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",4321, 1, 1111, 8, "Bamba Big");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",4321, 2, 1112, 3, "Bamba Small");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",4321, 3, 1113, 8, "Bisli Big");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",4321, 4, 1114, 3, "Bisli Small");
        //Coca Cola
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 5, 2221, 3, "Coca Cola 1L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 6, 2222, 4, "Coca Cola 1.5L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 7, 2223, 5, "Coca Cola 2L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 8, 2224, 3, "Sprite 1L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 9, 2225, 4, "Sprite 1.5L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 10, 2226, 5, "Sprite 2L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 11, 2227, 3, "Iced Tea 1L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 12, 2228, 4, "Iced Tea 1.5L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 13, 2229, 5, "Iced Tea 2L");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 14, 2230, 40, "JagerMeister Liquor");
        applicationService.addItemToSupplier("Yasmin 3 , Dimona",1234, 15, 2231, 40, "FireBall Whisky");

    }

    private void loadSupplierItemsDiscountData() {
        applicationService.addItemDiscountAccordingToAmount("BranchTest", 1234, 11, 15, 50); //promoting Iced Tea
        applicationService.addItemDiscountAccordingToAmount("BranchTest", 1234, 12, 15, 50); //promoting Iced Tea
        applicationService.addItemDiscountAccordingToAmount("BranchTest", 1234, 13, 15, 50); //promoting Iced Tea
        applicationService.addItemDiscountAccordingToAmount("BranchTest", 4321, 1, 20, 25); //discount on Bamba Big 25% if more than 20
        applicationService.addItemDiscountAccordingToAmount("BranchTest", 4321, 3, 20, 25); //discount on Bisli Big 25% if more than 20
    }

    private void loadEmployeeData() {
        loadEmployeesData();
        loadJobConstraintsData();
        loadBranchesData();
        loadShiftsData();
    }

    private void loadEmployeesData() {
        applicationService.addEmployee("Mankal", 1, "bank1", 1000000, "2010-01-01", "CEO", "employmentDetails1");
        applicationService.addEmployee("cash0", 100, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails100");
        applicationService.addEmployee("cash1", 101, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails101");
        applicationService.addEmployee("cash2", 102, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails102");
        applicationService.addEmployee("cash3", 103, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails103");
        applicationService.addEmployee("usher0", 200, "bank1", 6000, "2022-01-01", "usher", "employmentDetails200");
        applicationService.addEmployee("usher1", 201, "bank1", 6000, "2022-01-01", "usher", "employmentDetails201");
        applicationService.addEmployee("usher2", 202, "bank1", 6000, "2022-01-01", "usher", "employmentDetails202");
        applicationService.addEmployee("usher3", 203, "bank1", 6000, "2022-01-01", "usher", "employmentDetails203");
        applicationService.addEmployee("ware0", 300, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails300");
        applicationService.addEmployee("ware1", 301, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails301");
        applicationService.addEmployee("ware2", 302, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails302");
        applicationService.addEmployee("ware3", 303, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails303");
        applicationService.addEmployee("shify0", 400, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails400");
        applicationService.addEmployee("shify1", 401, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails401");
        applicationService.addEmployee("shify2", 402, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails402");
        applicationService.addEmployee("shify3", 403, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails403");
        applicationService.addEmployee("hairy0", 500, "bank1", 12000, "2022-01-01", "hr", "employmentDetails500");
        applicationService.addEmployee("hairy1", 501, "bank1", 12000, "2022-01-01", "hr", "employmentDetails501");
        applicationService.addEmployee("driver0", 600, "bank1", 8000, "2022-01-01", "driver", "employmentDetails600");
        applicationService.addEmployee("driver1", 601, "bank1", 8000, "2022-01-01", "driver", "employmentDetails601");
        applicationService.addEmployee("driver2", 602, "bank1", 8000, "2022-01-01", "driver", "employmentDetails602");
        applicationService.addEmployee("driver3", 603, "bank1", 8000, "2022-01-01", "driver", "employmentDetails603");
        applicationService.addEmployee("branchManager0", 700, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails700");
        applicationService.addEmployee("branchManager1", 701, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails701");
        applicationService.addEmployee("branchManager2", 702, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails702");
        applicationService.addEmployee("branchManager3", 703, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails703");
        applicationService.addEmployee("deliveryManager0", 800, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails800");
        applicationService.addEmployee("deliveryManager1", 801, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails801");
        applicationService.addEmployee("deliveryManager2", 802, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails802");
        applicationService.addEmployee("deliveryManager3", 803, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails803");
    }

    private void loadJobConstraintsData() {
        applicationService.addJobConstraint(101, "2023-01-07","evening");
        applicationService.addJobConstraint(201, "2023-01-07","evening");
        applicationService.addJobConstraint(301, "2023-01-07","evening");
        applicationService.addJobConstraint(401, "2023-01-07","evening");
        applicationService.addJobConstraint(601, "2023-01-07","evening");
    }

    private void loadBranchesData() {
        applicationService.addbranch("Ringelblum 9 , Beersheba","Center",1);
        applicationService.addbranch("Yasmin 3 , Dimona","South",700);
    }

    private void loadShiftsData() {
        HashMap<Integer, String> jobMap1 = new HashMap<>();
        jobMap1.put(101, "cashier"); jobMap1.put(201, "usher"); jobMap1.put(301, "warehouse");jobMap1.put(401, "shift manager");
        applicationService.addShift("Ringelblum 9 , Beersheba","2023-01-07", "morning", jobMap1);

        HashMap<Integer, String> jobMap2 = new HashMap<>();
        jobMap2.put(100, "cashier"); jobMap2.put(200, "usher"); jobMap2.put(300, "warehouse");jobMap2.put(400, "shift manager");
        applicationService.addShift("Yasmin 3 , Dimona","2023-01-07", "morning", jobMap2);

        applicationService.addShift("Ringelblum 9 , Beersheba","2023-02-07", "morning", jobMap1);
        applicationService.addShift("Yasmin 3 , Dimona","2023-02-07", "morning", jobMap2);
    }

    private void loadDeliveriesData() throws Exception {
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("BeerSheva", "0542658742", "Yoav Avital", "South");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("TelAviv", "0527173626", "yossi", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Haifa", "0593664926", "yoav", "North");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Netanya", "0521044726", "avi", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("RishonLeZion", "053374926", "saar", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("PetahTikwa", "0527364183", "ron", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Ashdod", "0527362746", "yuval", "South");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Naharya", "0527104448", "noa", "North");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Eilat", "0511112726", "matan", "South");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Jerusalem", "0527173999", "david", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Ashkelon", "0593663535", "shlomo", "South");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Hadera", "0521104726", "nadav", "North");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Holon", "0533741000", "ronen", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("Rehovot", "0527362727", "aviv", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("BneiBrak", "0527311846", "ron", "Center");
        applicationService.getApplicationFacade().getDeliveryFacade().getSiteController().addSite("RamatGan", "0511504448", "sigal", "Center");

        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0372", "Kia", 5000, 10000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("7766", "Ford", 4500, 9000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("3434", "Mercedes", 6000, 12000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("1834", "Volvo", 4800, 10000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("7790", "Toyota", 7500, 15000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0175", "Mazda", 7000, 14000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0578", "Ford", 4500, 9000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("1043", "Mercedes", 6000, 12000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("9966", "Ford", 4500, 9000);
        applicationService.getApplicationFacade().getDeliveryFacade().getTruckController().addTruck("0420", "Honda", 2000, 3000);
    }

    Runnable notifier = new Runnable() {
        public void run() {
            System.out.print(".");
        }
    };

    public ApplicationService getApplicationService() {
        return applicationService;
    }
}
