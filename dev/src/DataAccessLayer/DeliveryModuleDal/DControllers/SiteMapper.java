package DataAccessLayer.DeliveryModuleDal.DControllers;

import BusinessLayer.DeliveryModule.Objects.Site;

import java.sql.*;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SiteMapper extends DalController {

    private final String AddressColumnName = "Address";
    private final String ContactNumberColumnName = "ContactNumber";
    private final String ContactNameColumnName = "ContactName";
    private final String ShippingAreaColumnName = "ShippingArea";

    private Map<String, Site> sites;

    public SiteMapper()  {
        super("Sites");
            sites = new ConcurrentHashMap<>();
    }

    public void load() throws Exception {
        try {
            for (Site site : selectAllSites())
                sites.put(site.getAddress(), site);
        }
        catch (Exception e){
            throw new Exception("could not load " + getTableName());
        }
    }

    public void checkSiteExist(String address) throws Exception {
        try {
            getSite(address);
        }
        catch (Exception e){
            throw new Exception("site does not exist " + address);
        }
    }

    public Site getSite(String address) throws Exception {
        if (sites.containsKey(address))
            return sites.get(address);
        Site result = (Site) select("'"+address+"'", "Address");
        sites.put(address, result);
        return result;
    }

    public List<Site> selectAllSites() throws Exception {
        return (List<Site>)(List<?>)select();
    }

    public void insert(Site site) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?,?,?,?)",
                getTableName(), AddressColumnName,ContactNumberColumnName,ContactNameColumnName,ShippingAreaColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, site.getAddress());
            pstmt.setString(2, site.getContactNumber());
            pstmt.setString(3, site.getContactName());
            pstmt.setString(4, site.getShippingArea());
            pstmt.executeUpdate();
            sites.put(site.getAddress(), site);
        }
        catch (Exception e){
            throw new Exception("could not insert site " + site.getAddress());
        }

    }

    public void delete(String address) throws Exception {
        Site site = sites.get(address);
        if (site != null)
            sites.remove(address);
        if(!delete("'"+address+"'", AddressColumnName))
            throw new Exception("could not delete site: " + address);
    }

    @Override
    protected Site ConvertReaderToObject(ResultSet reader) throws Exception {
        return new Site(reader.getString(1), reader.getString(2), reader.getString(3), reader.getString(4));
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<String,Site>> iter = sites.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,Site> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning site "+entry.getValue().getAddress() +" from cache!");
                iter.remove();
            }
        }
    }
}
