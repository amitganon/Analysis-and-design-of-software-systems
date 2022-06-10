package DataAccessLayer.EmployeesModuleDal.Mappers;

import BusinessLayer.EmployeeModule.Objects.Employee;
import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmployeeMapper extends DalController {

    private Map<Integer, Employee> EmployeeMapper;
    public EmployeeMapper()
    {
        super("Employees");
        EmployeeMapper = new ConcurrentHashMap<>();


    }


    public void load() throws Exception {
        for (Employee dEmployee: selectAllEmployees())
            EmployeeMapper.put(dEmployee.getID(),dEmployee);
    }

    public Employee getEmployee(int ID)throws Exception
    {
        if (EmployeeMapper.get(ID)==null) {
            Employee e = (Employee)  select(ID,"ID");
            if (e==null)
                return  null;
            else
                EmployeeMapper.put(ID, (Employee)  e);
        }
        return EmployeeMapper.get(ID);
    }


    public List<Employee> selectAllEmployees() throws Exception {
        return (List<Employee>)(List<?>)select();
    }


    public boolean insert (int id, String name,int salary, String bankAccount, String employeeStartDate, String employeeDetails)
    {
        Employee dEmployee = new Employee(name,id, bankAccount,salary,LocalDate.parse(employeeStartDate),employeeDetails);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4} , {5}, {6}) VALUES(?,?,?,?,?,?)",
                getTableName(),"ID","Name","BankDetails","Salary","EmployeeStartDate","EmployeeDetails");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dEmployee.getID());
            pstmt.setString(2, dEmployee.getName());
            pstmt.setString(3, dEmployee.getBankAccountDetails());
            pstmt.setInt(4, dEmployee.getSalary());
            pstmt.setString(5, dEmployee.getEmploymentStartDate().toString());
            pstmt.setString(6, dEmployee.getEmploymentDetails());
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        EmployeeMapper.put(id, dEmployee);
        return true;
    }
    public void deleteEmployee(int id) {
        checkDTOExists(id);
        Employee dEmployee = EmployeeMapper.get(id);
        delete(dEmployee.getID(), "ID");
        EmployeeMapper.remove(id);
    }
    public void updateEmployee(int employeeID , int otherAttribute , String attributeColumn) {
        update(employeeID,"ID",otherAttribute,attributeColumn);
        EmployeeMapper.get(employeeID).setSalary(otherAttribute);
    }
    public void updateEmployee(int employeeID , String otherAttribute , String attributeColumn) {
        if (update(employeeID,"ID",otherAttribute,attributeColumn)) {
            if (attributeColumn.equals("Name"))
                EmployeeMapper.get(employeeID).setName(otherAttribute);
            else if (attributeColumn.equals("BankDetails"))
                EmployeeMapper.get(employeeID).setBankAccountDetails(otherAttribute);
            else
                EmployeeMapper.get(employeeID).setEmploymentDetails(otherAttribute);
        }
    }



    private void checkDTOExists(int id){
        if(!EmployeeMapper.containsKey(id))
            throw new IllegalArgumentException("Employee is not exists in the database!");
    }

    @Override
    protected Object ConvertReaderToObject(ResultSet reader) {
        Employee result = null;
        try {
            result = new Employee(reader.getString(2),reader.getInt(1),reader.getString(3),reader.getInt(4), LocalDate.parse(reader.getString(5)),reader.getString(6));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, Employee>> iter = EmployeeMapper.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Employee> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning employee "+entry.getValue().getID() +" from cache!");
                iter.remove();
            }
        }
    }
}
