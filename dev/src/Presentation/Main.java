package Presentation;

import DataAccessLayer.DataBaseCreator;
import DataAccessLayer.SupplierDAL.CacheCleaner;
import Presentation.Model.BackendController;
import Presentation.View.ApplicationView;

public class Main {
    public static void main(String[] args) {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        try {
            dataBaseCreator.CreateAllTables();
            BackendController.getInstance(); // create connection with backend
            ApplicationView applicationView = new ApplicationView();
            applicationView.start();
            dataBaseCreator.deleteAllTables();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            //dataBaseCreator.deleteAllTables();
        }
        dataBaseCreator.deleteAllTables();
        CacheCleaner.executor.shutdownNow();
    }
}
