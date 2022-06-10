package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;
import DataAccessLayer.DataBaseCreator;
import DataAccessLayer.SupplierDAL.*;
import Presentation.Model.BackendController;
import ServiceLayer.Services.InventoryService;

import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.Arrays;

public class CategoryTest {
//    //private static boolean setUpIsDone = false;
//    private static DataBaseCreator dataBaseCreator;
//    private static BackendController cont;
//    private static InventoryService inventoryService;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        DataBaseCreator dataBaseCreator = new DataBaseCreator();
//        dataBaseCreator.deleteAllTables(); // todo we need to make new business every test.
//        dataBaseCreator.CreateAllTables();
//        DALFacade.clearSingletonForTests();
//        BackendController.clearBackendControllerForTests();
//        CacheCleaner cacheCleaner = new CacheCleaner(Arrays.asList(SupplierMapper.getInstance(), DemandOrderMapper.getInstance(), FixedOrderMapper.getInstance(), BillOfQuantityMapper.getInstance(), ContactMapper.getInstance(), OrderProductMapper.getInstance()));
//        cacheCleaner.cleanForTests();
//        dataBaseCreator.CreateAllTables();
//        cont = BackendController.getInstance();
//        inventoryService = cont.getApplicationService().getInventoryService();
//        cont.loadDataForTests();
//    }
////
////    @AfterClass
////    public static void tearDown() throws Exception {
////        dataBaseCreator.deleteAllTables();
////    }
//
//
//    @org.junit.Test
//    public void getItemIDList() {
//        Assert.assertEquals(2, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#1").getItemIDList().size());
//        Assert.assertEquals(2, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#2").getItemIDList().size());
//        Assert.assertEquals(6, (long)inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#2").getItemIDList().get(0));
//    }
//
//    @org.junit.Test
//    public void addSubCat() {
//        Assert.assertEquals(3, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1").getSubCatList().size());
//        Assert.assertEquals(3, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#2").getSubCatList().size());
//        Assert.assertEquals(0, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#3").getSubCatList().size());
//    }
//
//    @org.junit.Test
//    public void setFatherCategory() {
//        Assert.assertEquals(3, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1").getSubCatList().size());
//        Assert.assertEquals(inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1"), inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#2").getFatherCategory());
//        Assert.assertEquals(inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1"), inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#3").getFatherCategory());
//    }
}

