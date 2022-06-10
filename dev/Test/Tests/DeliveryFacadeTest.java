package Tests;

import BusinessLayer.DeliveryModule.Objects.Delivery;
import BusinessLayer.DeliveryModule.DeliveryFacade;
import BusinessLayer.DeliveryModule.Objects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

class DeliveryFacadeTest {

    DeliveryFacade deliveryFacade;
    List<Integer> stockShortnessList;


    @BeforeEach
    void setUp() {
        try {
            deliveryFacade = new DeliveryFacade();
            List<StockShortness> stockShortnesses = deliveryFacade.getAllRelevantStockShortnesses();
            stockShortnessList = new LinkedList<>();
            for (StockShortness stockShortness: stockShortnesses) {
                stockShortnessList.add(stockShortness.getId());
            }
            deliveryFacade.deleteAllData();
        }
        catch (Exception e){
            fail();
        }
    }

    @AfterEach
    void tearDown() {
      deliveryFacade.deleteAllData();
    }

    @Test
    void weightTruck() {
        try {
            deliveryFacade.createDelivery("12/12/2020", "12:12", "0372", 1, stockShortnessList, false);
            deliveryFacade.weightTruck(0, 2000);
            SiteDoc siteDoc = deliveryFacade.getDocumentForDeliveryAndlocation(0, 0);
            assertEquals(2000, siteDoc.getWeight());
            assertEquals(0, siteDoc.getLogList().size());

            try {
                deliveryFacade.weightTruck(0, 15000);
            } catch (Exception e) {
                siteDoc = deliveryFacade.getDocumentForDeliveryAndlocation(0, 0);
                assertEquals(15000, siteDoc.getWeight());
                assertEquals(1, siteDoc.getLogList().size());
            }
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void getAllRelevantTrucks() {
        try {
             List<Integer> s1 = new LinkedList<>();
             s1.add(stockShortnessList.remove(0));
             deliveryFacade.createDelivery("12/12/2020", "12:12", "7766", 1, stockShortnessList, false);
             deliveryFacade.weightTruck(0, 5000);

             List<Truck> temp = new LinkedList<>();
             temp.add(deliveryFacade.getTruckByLicense("0372"));
             temp.add(deliveryFacade.getTruckByLicense("3434"));
             temp.add(deliveryFacade.getTruckByLicense("1834"));
             temp.add(deliveryFacade.getTruckByLicense("1043"));

             List<Truck> trucks = deliveryFacade.getAllRelevantTrucks("12/12/2020", 0);
             List<String> licenses = new LinkedList<>();
            for (Truck truck: trucks) {
                licenses.add(truck.getLicenseNumber());
            }
             for (Truck truck: temp) {
                 assertTrue(licenses.contains(truck.getLicenseNumber()));
             }

            deliveryFacade.createDelivery("12/12/2020", "12:12", "0372", 2, s1, false);
            trucks = deliveryFacade.getAllRelevantTrucks("12/12/2020", 0);
            licenses = new LinkedList<>();
            for (Truck truck: trucks) {
                licenses.add(truck.getLicenseNumber());
            }
            assertFalse(licenses.contains("0372"));
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void arrivedToSite() {
        try {
            deliveryFacade.createDelivery("12/12/2020", "12:12", "7766", 1, stockShortnessList, false);

            deliveryFacade.arrivedToSite(0);
            assertEquals(16, deliveryFacade.getStockShortnessOfDelivery(0).size());

            deliveryFacade.arrivedToSite(1);
            assertEquals(16, deliveryFacade.getStockShortnessOfDelivery(0).size());

            deliveryFacade.arrivedToSite(2);
            assertEquals(14, deliveryFacade.getStockShortnessOfDelivery(0).size());

            deliveryFacade.arrivedToSite(3);
            assertEquals(13, deliveryFacade.getStockShortnessOfDelivery(0).size());

            deliveryFacade.arrivedToSite(4);
            assertEquals(13, deliveryFacade.getStockShortnessOfDelivery(0).size());
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void getLatestAddress() {
        try {
            Delivery delivery = deliveryFacade.createDelivery("12/12/2020", "12:12", "0372", 1, stockShortnessList, false);

            int latestIndex1 = deliveryFacade.getLatestAddress(0);
            String latest1 = deliveryFacade.getDocumentForDeliveryAndlocation(0, latestIndex1).getSiteAddress();
            assertEquals(delivery.getSourceAddress(), latest1);

            deliveryFacade.weightTruck(0, 3000);
            int latestIndex2 = deliveryFacade.getLatestAddress(0);
            String latest2 = deliveryFacade.getDocumentForDeliveryAndlocation(0, latestIndex2).getSiteAddress();
            assertEquals(delivery.getListOfAddresses().get(1), latest2);

            try {
                deliveryFacade.weightTruck(1, 30000);
            }
            catch (Exception e) {
                int latestIndex3 = deliveryFacade.getLatestAddress(0);
                String latest3 = deliveryFacade.getDocumentForDeliveryAndlocation(0, latestIndex3).getSiteAddress();
                assertEquals(delivery.getListOfAddresses().get(1), latest3);
            }
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void createSiteAddresses(){
        try {
            List<StockShortness> stockShortnesses = deliveryFacade.getAllRelevantStockShortnesses();
            List<String> test = deliveryFacade.createSiteAddresses(stockShortnesses);
            for (StockShortness stockShortness : stockShortnesses) {
                String branch = stockShortness.getBranchAddress();
                boolean flag = false;
                for (int i = test.lastIndexOf(branch); i >= 0; i--) {
                    if (test.get(i).equals(stockShortness.getSupplierAddresses())) {
                        flag = true;
                        break;
                    }
                }
                assertTrue(flag);
            }
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void checkNewTruckWeight(){
        try {
            deliveryFacade.createDelivery("12/12/2020", "12:12", "0372", 1, stockShortnessList, false);
            deliveryFacade.weightTruck(0, 6000);
            int weight1 = deliveryFacade.checkNewTruckWeight(0, "7766", "12/12/2020");
            assertEquals(5500, weight1);

            deliveryFacade.weightTruck(1, 8000);
            int weight2 = deliveryFacade.checkNewTruckWeight(1, "9966", "12/12/2020");
            assertEquals(7500, weight2);

            deliveryFacade.weightTruck(2, 9000);
            int weight3 = deliveryFacade.checkNewTruckWeight(2, "3434", "12/12/2020");
            assertEquals(10000, weight3);

            deliveryFacade.weightTruck(3, 7500);
            int weight4 = deliveryFacade.checkNewTruckWeight(3, "7790", "12/12/2020");
            assertEquals(10000, weight4);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void changeTruck(){
        try {
            deliveryFacade.createDelivery("12/12/2020", "12:12", "0372", 1, stockShortnessList, false);
            deliveryFacade.changeTruck(0, "7766");
            List<Truck> trucks = deliveryFacade.getAllTrucksAvailable("12/12/2020");

            Truck truck1 = deliveryFacade.getTruckByLicense("7766");
            assertFalse(trucks.contains(truck1));
            assertEquals(8, trucks.size());

            trucks = deliveryFacade.getAllTrucksAvailable("13/12/2020");
            assertEquals(10, trucks.size());

            deliveryFacade.changeTruck(1, "1043");
            trucks = deliveryFacade.getAllTrucksAvailable("12/12/2020");
            Truck truck2 = deliveryFacade.getTruckByLicense("1043");
            assertFalse(trucks.contains(truck2));
            assertEquals(7, trucks.size());

            List<SiteDoc> siteDocs = deliveryFacade.getAllDocumentsForDelivery(0);
            for (SiteDoc siteDoc: siteDocs) {
                assertTrue(siteDoc.getLogList().contains(problemSiteVisit.changedTruck));
            }
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void createSiteDocs(){
        try {
            deliveryFacade.createDelivery("12/12/2020", "12:12", "0372", 1, stockShortnessList, false);
            List<StockShortness> stockShortnesses = deliveryFacade.getStockShortnessOfDelivery(0);

            for (StockShortness stockShortness: stockShortnesses) {
                assertEquals(0, stockShortness.getDeliveryBound());
            }

            assertEquals(16, stockShortnesses.size());
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void deleteAllData(){
        try {
            List<Integer> stockShortnesses1 = new LinkedList<>();
            stockShortnesses1.add(stockShortnessList.get(0)); stockShortnesses1.add(stockShortnessList.get(1)); stockShortnesses1.add(stockShortnessList.get(2));
            List<Integer> stockShortnesses2 = new LinkedList<>();
            stockShortnesses2.add(stockShortnessList.get(3)); stockShortnesses2.add(stockShortnessList.get(4)); stockShortnesses2.add(stockShortnessList.get(5));
            List<Integer> stockShortnesses3 = new LinkedList<>();
            stockShortnesses3.add(stockShortnessList.get(6)); stockShortnesses3.add(stockShortnessList.get(7)); stockShortnesses3.add(stockShortnessList.get(8));

            deliveryFacade.createDelivery("12/12/2020", "12:12", "0372", 1, stockShortnesses1, false);
            deliveryFacade.createDelivery("12/12/2020", "12:12", "7766", 2, stockShortnesses2, false);
            deliveryFacade.createDelivery("12/12/2020", "12:12", "3434", 3, stockShortnesses3, false);

            deliveryFacade.deleteAllData();

            List<SiteDoc> siteDocs1 = deliveryFacade.getAllDocumentsForDelivery(0);
            assertEquals(0, siteDocs1.size());
            List<SiteDoc> siteDocs2 = deliveryFacade.getAllDocumentsForDelivery(1);
            assertEquals(0, siteDocs2.size());
            List<SiteDoc> siteDocs3 = deliveryFacade.getAllDocumentsForDelivery(2);
            assertEquals(0, siteDocs3.size());

            List<Delivery> deliveries = deliveryFacade.getAllDeliveries();
            assertEquals(0, deliveries.size());

            List<Truck> trucks = deliveryFacade.getAllTrucksAvailable("12/12/2020");
            assertEquals(10, trucks.size());

            List<StockShortness> stockShortnesses = deliveryFacade.getAllRelevantStockShortnesses();
            assertEquals(16, stockShortnesses.size());
        }
        catch (Exception e){
            fail();
        }
    }
}