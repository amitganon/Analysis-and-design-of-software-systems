package BusinessLayer.DeliveryModule.Controllers;

import BusinessLayer.DeliveryModule.Objects.Site;
import DataAccessLayer.DeliveryModuleDal.DControllers.SiteMapper;

import java.util.LinkedList;
import java.util.List;

public class SiteController {

    private final SiteMapper mapper;

    public SiteController(SiteMapper mapper){
        this.mapper = mapper;
        //loadData();
    }

    public void addSite(String address, String contactNumber, String contactName, String shippingArea) throws Exception {
        mapper.insert(new Site(address, contactNumber, contactName, shippingArea));
    }

    private Site getSite(String address) throws Exception {
        mapper.checkSiteExist(address);
        return mapper.getSite(address);
    }

    public void checkShippingAreaValidity(List<String> siteAddresses) throws Exception {
        List<String> ShippingAreas = new LinkedList<>();
        for (String siteAddress: siteAddresses) {
            String ShippingArea = getSite(siteAddress).getShippingArea();
            if (!ShippingAreas.contains(ShippingArea)) ShippingAreas.add(ShippingArea);
        }
        if(ShippingAreas.size()>1) {
            throw new Exception("the delivery contains multiple shipping areas");
        }
    }

    public String getContactNumber(String address) throws Exception {
        return getSite(address).getContactNumber();
    }

    public String getContactName(String address) throws Exception {
        return getSite(address).getContactName();
    }

    private void loadData() {
        try {
            addSite("BeerSheva", "0527312726", "eli", "South");
            addSite("TelAviv", "0527173626", "yossi", "Center");
            addSite("Haifa", "0593664926", "yoav", "North");
            addSite("Netanya", "0521044726", "avi", "Center");
            addSite("RishonLeZion", "053374926", "saar", "Center");
            addSite("PetahTikwa", "0527364183", "ron", "Center");
            addSite("Ashdod", "0527362746", "yuval", "South");
            addSite("Naharya", "0527104448", "noa", "North");
            addSite("Eilat", "0511112726", "matan", "South");
            addSite("Jerusalem", "0527173999", "david", "Center");
            addSite("Ashkelon", "0593663535", "shlomo", "South");
            addSite("Hadera", "0521104726", "nadav", "North");
            addSite("Holon", "0533741000", "ronen", "Center");
            addSite("Rehovot", "0527362727", "aviv", "Center");
            addSite("BneiBrak", "0527311846", "ron", "Center");
            addSite("RamatGan", "0511504448", "sigal", "Center");
        }
        catch (Exception ignored){
        }
    }


}
