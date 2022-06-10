package Tests;

import BusinessLayer.DeliveryModule.Controllers.DeliveryController;
import DataAccessLayer.DeliveryModuleDal.DControllers.DeliveryMapper;
import DataAccessLayer.DeliveryModuleDal.DControllers.DestMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryControllerTest {

    DeliveryController deliveryController;

    @BeforeEach
    void setUp() {
        deliveryController = new DeliveryController(new DeliveryMapper(new DestMapper()));
        deliveryController.deleteAllData();
    }

    @AfterEach
    void tearDown() {
        deliveryController.deleteAllData();
    }

    @Test
    void addDelivery() {
        try {
            int id = deliveryController.addDelivery("12/12/2020", "12:12", "2322", 4, "TelAviv", new LinkedList<>());
            assertEquals(0, id);
            assertEquals("2322", deliveryController.getDeliveryById(0).getTruckLicense());
            assertEquals(4, deliveryController.getDeliveryById(0).getDriverId());
            assertEquals("TelAviv", deliveryController.getDeliveryById(0).getSourceAddress());

            id = deliveryController.addDelivery("12/12/2020", "12:12", "8982", 54, "RamatGan", new LinkedList<>());
            assertEquals(1, id);
            assertEquals("8982", deliveryController.getDeliveryById(1).getTruckLicense());
            assertEquals(54, deliveryController.getDeliveryById(1).getDriverId());
            assertEquals("RamatGan", deliveryController.getDeliveryById(1).getSourceAddress());

            List<String> dest = new LinkedList<>();
            dest.add("Aco");
            id = deliveryController.addDelivery("12/12/2020", "12:12", "1029", 6, "Haifa", dest);
            assertEquals(2, id);
            assertEquals("1029", deliveryController.getDeliveryById(2).getTruckLicense());
            assertEquals(6, deliveryController.getDeliveryById(2).getDriverId());
            assertEquals("Haifa", deliveryController.getDeliveryById(2).getSourceAddress());
            for (String d : deliveryController.getDeliveryById(2).getDestAddresses()) {
                assertEquals("Aco", d);
            }
        }
        catch (Exception e){
            fail();
        }
    }

}