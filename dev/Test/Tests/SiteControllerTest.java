package Tests;

import BusinessLayer.DeliveryModule.Controllers.SiteController;
import DataAccessLayer.DeliveryModuleDal.DControllers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SiteControllerTest {

    SiteController siteController;

    @BeforeEach
    void setUp() {
        try {
            siteController = new SiteController(new SiteMapper());
        }
        catch (Exception ignored){}
    }

    @Test
    void checkShippingAreaValidity(){
        try {
            List<String> sites = new LinkedList<>();
            sites.add("TelAviv"); sites.add("PetahTikwa");
            sites.add("Haifa"); sites.add("BeerSheva");

            siteController.checkShippingAreaValidity(sites);
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }

        try {
            List<String> sites = new LinkedList<>();
            sites.add("TelAviv"); sites.add("Netanya");
            sites.add("Jerusalem"); sites.add("Rehovot");

            siteController.checkShippingAreaValidity(sites);
            assertTrue(true);
        }
        catch (Exception e){
            fail();
        }

        try {
            List<String> sites = new LinkedList<>();
            sites.add("RamatGan"); sites.add("Eilat");
            sites.add("Hadera"); sites.add("Ashdod");

            siteController.checkShippingAreaValidity(sites);
            fail();
        }
        catch (Exception e){
            assertTrue(true);
        }
    }
}
