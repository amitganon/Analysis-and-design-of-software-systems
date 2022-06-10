package BusinessLayer.InventoryBusiness;
import DataAccessLayer.InventoryDAL.DALFacade;
import DataAccessLayer.DataBaseCreator;
import DataAccessLayer.SupplierDAL.*;
import Presentation.Model.BackendController;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.Arrays;

public class ItemQuantityTest {
//    //private static boolean setUpIsDone = false;
//    private static BackendController cont;
//    private static ItemController itemController;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        DataBaseCreator dataBaseCreator = new DataBaseCreator();
//        dataBaseCreator.deleteAllTables();
//        dataBaseCreator.CreateAllTables();
//        DALFacade.clearSingletonForTests();
//        BackendController.clearBackendControllerForTests();
//        CacheCleaner cacheCleaner = new CacheCleaner(Arrays.asList(SupplierMapper.getInstance(), DemandOrderMapper.getInstance(), FixedOrderMapper.getInstance(), BillOfQuantityMapper.getInstance(), ContactMapper.getInstance(), OrderProductMapper.getInstance()));
//        cacheCleaner.cleanForTests();
//        cont = BackendController.getInstance();
//        itemController = cont.getApplicationService().getInventoryService().getItemController();
//        cont.loadDataForTests();
//    }
//
//    @org.junit.Test
//    public void addAmountToBack() {
//        Item bigBamba = itemController.getItem(branchAddress, 1);
//        bigBamba.getQuantity().setAmountInBackRoom(20, bigBamba.getItemID());
//        bigBamba.getQuantity().setAmountInFrontRoom(10, bigBamba.getItemID());
//        bigBamba.getQuantity().addAmountToBack(20, bigBamba.getItemID());
//        Assert.assertEquals(40, bigBamba.getQuantity().getAmountInBackRoom());
//        Assert.assertEquals(10, bigBamba.getQuantity().getAmountInFrontRoom());
//        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().addAmountToBack(-100, bigBamba.getItemID()));
//    }
//
//    @org.junit.Test
//    public void removeAmountFromFront() {
//        Item bigBamba = itemController.getItem(branchAddress, 1);
//        bigBamba.getQuantity().setAmountInBackRoom(100, bigBamba.getItemID());
//        bigBamba.getQuantity().setAmountInFrontRoom(50, bigBamba.getItemID());
//        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().removeAmountFromBack(-100, bigBamba.getItemID()));
//        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().removeAmountFromBack(101, bigBamba.getItemID()));
//        bigBamba.getQuantity().removeAmountFromBack(49, bigBamba.getItemID());
//        Assert.assertEquals(51, bigBamba.getQuantity().getAmountInBackRoom());
//    }
//
//    @org.junit.Test
//    public void moveItemsBetweenRooms() {
//        Item bigBamba = itemController.getItem(branchAddress, 1);
//        bigBamba.getQuantity().setAmountInBackRoom(100, bigBamba.getItemID());
//        bigBamba.getQuantity().setAmountInFrontRoom(50, bigBamba.getItemID());
//        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().moveItemsBetweenRooms(true, -100, bigBamba.getItemID()));
//        bigBamba.getQuantity().moveItemsBetweenRooms(true, 50, bigBamba.getItemID());
//        Assert.assertEquals(50, bigBamba.getQuantity().getAmountInBackRoom());
//        Assert.assertEquals(100, bigBamba.getQuantity().getAmountInFrontRoom());
//        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().moveItemsBetweenRooms(true, 150, bigBamba.getItemID()));
//    }
//
//    @org.junit.Test
//    public void isInShortage() {
//        Item bigBamba = itemController.getItem(branchAddress, 1);
//        bigBamba.getQuantity().setAmountInBackRoom(100, bigBamba.getItemID());
//        bigBamba.getQuantity().setAmountInFrontRoom(50, bigBamba.getItemID());
//        Assert.assertFalse(bigBamba.getQuantity().isInShortage());
//        bigBamba.getQuantity().removeAmountFromBack(90, bigBamba.getItemID());
//        bigBamba.getQuantity().removeAmountFromFront(50, bigBamba.getItemID());
//        Assert.assertTrue(bigBamba.getQuantity().isInShortage());
//        bigBamba.getQuantity().addAmountToBack(1000, bigBamba.getItemID());
//        bigBamba.getQuantity().addAmountToFront(200, bigBamba.getItemID());
//        Assert.assertFalse(bigBamba.getQuantity().isInShortage());
//    }
}