package BusinessLayer.DeliveryModule.Controllers;

import BusinessLayer.DeliveryModule.Objects.Delivery;
import BusinessLayer.DeliveryModule.Objects.StockShortness;
import DataAccessLayer.DeliveryModuleDal.DControllers.StockShortnessMapper;

import java.util.*;

public class StockShortnessController {
    private final StockShortnessMapper mapper;
    private int stockShortnessId;


    public StockShortnessController(StockShortnessMapper stockShortnessMapper){
        mapper = stockShortnessMapper;
        StockShortness stockShortness = (StockShortness) mapper.selectLastId("Id");
        if (stockShortness == null){
            stockShortnessId = 0;
        }
        else
            stockShortnessId = stockShortness.getId() + 1;
        loadData();
    }

    private void createStockShortness(int id, String branchAddress, String itemName, int itemCatalogNumber,
                                     int amount, String supplierAddress, int orderId) throws Exception {
        mapper.insert(new StockShortness(id,branchAddress,itemName,itemCatalogNumber,amount,supplierAddress, orderId));
    }

    public List<StockShortness> getAllRelevantStockShortnesses() throws Exception {
        return mapper.getAllUnboundedStockShortnesses();
    }

    public List<StockShortness> getStockShortnessOfDelivery(int deliveryId) throws Exception {
        return mapper.getAllStockShortnessesOfDelivery(deliveryId);
    }

    public void boundToDelivery(int deliveryId, List<Integer> items) throws Exception {
        for (int stockShortnessId: items) {
            mapper.boundStockShortnessToDelivery(stockShortnessId, deliveryId);
        }
    }

    public void unbound(int stockShortnessId) throws Exception {
            mapper.unboundStockShortnessFromDelivery(stockShortnessId);
    }

    public StockShortness getStockShortness(int stockShortnessid) throws Exception {
        return mapper.getStockShortness(stockShortnessid);
    }

    public void boundLoadSiteDoc(int stockIdLoad, int siteDocId) throws Exception {
        mapper.boundLoadSiteDoc(stockIdLoad, siteDocId);
    }

    public void boundUnLoadSiteDoc(int stockIdUnload, int siteDocId) throws Exception {
        mapper.boundUnLoadSiteDoc(stockIdUnload, siteDocId);
    }

    public void unboundUnloadDoc(int StockId) throws Exception {
        mapper.unboundUnloadDoc(StockId);

    }

    public void unboundLoadDoc(int StockId) throws Exception {
        mapper.unboundLoadDoc(StockId);
    }

    public void deleteAllStockBoundedToUnloadDoc(int documentId) throws Exception {
        mapper.deleteAllStockBoundedToUnloadDoc(documentId);
    }

    private void loadData() {
        try{
//            createStockShortness(0, "RamatGan", "apples", 0, 30, "TelAviv");
//            createStockShortness(1, "Eilat", "pears", 1, 25, "BeerSheva");
//            createStockShortness(2, "Ashdod", "milk", 2, 15, "Ashkelon");
//            createStockShortness(3, "Ashkelon", "coke", 3, 30, "TelAviv");
//            createStockShortness(4, "PetahTikwa", "yogurt", 4, 7, "TelAviv");
//            createStockShortness(5, "RishonLeZion", "apples", 0, 60, "RamatGan");
//            createStockShortness(6, "TelAviv", "apples", 0, 30, "Netanya");
//            createStockShortness(7, "RamatGan", "pears", 1, 41, "Haifa");
//            createStockShortness(8, "Haifa", "tuna", 5, 80, "Naharya");
//            createStockShortness(9, "Naharya", "meat", 6, 200, "Hadera");
//            createStockShortness(10, "Jerusalem", "tuna", 5, 55, "TelAviv");
//            createStockShortness(11, "RamatGan", "beans", 7, 88, "TelAviv");
//            createStockShortness(12, "RamatGan", "melons", 8, 77, "Netanya");
//            createStockShortness(13, "TelAviv", "oats", 9, 38, "RamatGan");
//            createStockShortness(14, "Ashdod", "milk", 2, 93, "BeerSheva");
//            createStockShortness(15, "RamatGan", "coke", 3, 78, "BneiBrak");
        }
        catch (Exception ignored){
        }
    }

    public void resetData() {
        mapper.deleteAllDataFromDbs();
        loadData();
    }

    public int createStockShortness(String branchAddress, String name, int productId, int amount, String supplierAddress, int orderId) throws Exception {
        mapper.insert(new StockShortness(stockShortnessId, branchAddress, name, productId, amount, supplierAddress, orderId));
        stockShortnessId++;
        return stockShortnessId-1;
    }

    public List<Integer> getStockShortnessOfOrder(int oldOrderId) {
        return mapper.getStockShortnessOfOrder(oldOrderId);
    }

    public void finisheOrder(int documentId) {
        List<Integer> finishedOrderIds = mapper.getOrderOfDocument(documentId);
        for (int orderId: finishedOrderIds)
            mapper.deleteOrderStock(orderId);
    }
}
