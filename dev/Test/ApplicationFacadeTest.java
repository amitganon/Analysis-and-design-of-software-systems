
import BusinessLayer.ApplicationFacade;
import BusinessLayer.EmployeeModule.Objects.Employee;
import BusinessLayer.EmployeeModule.Objects.Shift;

import org.junit.*;

import java.time.LocalDate;
import java.util.HashMap;

public class ApplicationFacadeTest {

    ApplicationFacade facade;

    @Before
    public void setUp() throws Exception {
        facade = new ApplicationFacade();
        resetDB();
    }

    public void resetDB(){
        try {
            facade = new ApplicationFacade();
            facade.deleteAllData();

            //adding employees
            facade.addEmployee("Mankal", 1, "bank1", 1000000, "2010-01-01", "CEO", "employmentDetails1");
            facade.addEmployee("cash0", 100, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails100");
            facade.addEmployee("cash1", 101, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails101");
            facade.addEmployee("cash2", 102, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails102");
            facade.addEmployee("cash3", 103, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails103");
            facade.addEmployee("usher0", 200, "bank1", 6000, "2022-01-01", "usher", "employmentDetails200");
            facade.addEmployee("usher1", 201, "bank1", 6000, "2022-01-01", "usher", "employmentDetails201");
            facade.addEmployee("usher2", 202, "bank1", 6000, "2022-01-01", "usher", "employmentDetails202");
            facade.addEmployee("usher3", 203, "bank1", 6000, "2022-01-01", "usher", "employmentDetails203");
            facade.addEmployee("ware0", 300, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails300");
            facade.addEmployee("ware1", 301, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails301");
            facade.addEmployee("ware2", 302, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails302");
            facade.addEmployee("ware3", 303, "bank1", 7000, "2022-01-01", "warehouse", "employmentDetails303");
            facade.addEmployee("shify0", 400, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails400");
            facade.addEmployee("shify1", 401, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails401");
            facade.addEmployee("shify2", 402, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails402");
            facade.addEmployee("shify3", 403, "bank1", 9000, "2022-01-01", "shift manager", "employmentDetails403");
            facade.addEmployee("hairy0", 500, "bank1", 12000, "2022-01-01", "hr", "employmentDetails500");
            facade.addEmployee("hairy1", 501, "bank1", 12000, "2022-01-01", "hr", "employmentDetails501");
            facade.addEmployee("driver0", 600, "bank1", 8000, "2022-01-01", "driver", "employmentDetails600");
            facade.addEmployee("driver1", 601, "bank1", 8000, "2022-01-01", "driver", "employmentDetails601");
            facade.addEmployee("driver2", 602, "bank1", 8000, "2022-01-01", "driver", "employmentDetails602");
            facade.addEmployee("driver3", 603, "bank1", 8000, "2022-01-01", "driver", "employmentDetails603");
            facade.addEmployee("branchManager0", 700, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails700");
            facade.addEmployee("branchManager1", 701, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails701");
            facade.addEmployee("branchManager2", 702, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails702");
            facade.addEmployee("branchManager3", 703, "bank1", 5000, "2022-01-01", "branch manager", "employmentDetails703");
            facade.addEmployee("deliveryManager0", 800, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails800");
            facade.addEmployee("deliveryManager1", 801, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails801");
            facade.addEmployee("deliveryManager2", 802, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails802");
            facade.addEmployee("deliveryManager3", 803, "bank1", 5000, "2022-01-01", "logistics", "employmentDetails803");

            //add job constraints
            facade.addJobConstraint(101, "2023-01-07","evening");
            facade.addJobConstraint(201, "2023-01-07","evening");
            facade.addJobConstraint(301, "2023-01-07","evening");
            facade.addJobConstraint(401, "2023-01-07","evening");
            facade.addJobConstraint(601, "2023-01-07","evening");

            //add shifts
            HashMap<Integer, String> jobMap1 = new HashMap<>();
            jobMap1.put(101, "cashier"); jobMap1.put(201, "usher"); jobMap1.put(301, "warehouse");jobMap1.put(401, "shift manager");
            facade.addShift("Ringelblum 9 , Beersheba","2023-01-07", "morning", jobMap1);

            HashMap<Integer, String> jobMap2 = new HashMap<>();
            jobMap2.put(100, "cashier"); jobMap2.put(200, "usher"); jobMap2.put(300, "warehouse");jobMap2.put(400, "shift manager");
            facade.addShift("Yasmin 3 , Dimona","2023-01-07", "morning", jobMap2);

            facade.addShift("Ringelblum 9 , Beersheba","2023-02-07", "morning", jobMap1);
            facade.addShift("Yasmin 3 , Dimona","2023-02-07", "morning", jobMap2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addEmployee(){//v
        try {
            //adding normal employees
            Assert.assertNull(facade.getEmployee(12345));
            facade.addEmployee("avi", 12345, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails1");
            Assert.assertNotNull(facade.getEmployee(12345));
            facade.removeEmployee(12345);
            Assert.assertNull(facade.getEmployee(12345));

        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void removeEmployee(){//V
        try {
            //removing employee has future shifts
            facade.addEmployee("avi", 12345, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails1");
            facade.addEmployeeToShift(1, 12345, "cashier");
            facade.removeEmployee(12345); //removal should fail
            Assert.fail();
        }
        catch (Exception e)
        {

        }
        try
        {
            facade.removeEmployeeFromShift(1,12345);
            facade.removeEmployee(12345); //removal should succeed
            Assert.assertNull(facade.getEmployee(12345));
        }
        catch (Exception e)
        {
            Assert.fail();
        }

    }
    @Test
    public void addJobToEmployee(){//V
        try
            {
                Employee em = facade.getEmployee(100);
                Assert.assertNotNull(em);
                Assert.assertFalse(facade.validEmployeeHasJob("usher","100"));
                facade.addJobToEmployee(100,"usher");
                Assert.assertTrue(facade.validEmployeeHasJob("usher","100"));
                facade.removeJobFromEmployee(100,"usher");
                Assert.assertFalse(facade.validEmployeeHasJob("usher","100"));
            }
            catch (Exception e)
            {
                Assert.fail();
            }
    }
    @Test
    public void RemoveJobFromEmployee() {//V
        try
        {
            Employee em = facade.getEmployee(100);
            facade.addJobToEmployee(100,"usher");
            Assert.assertTrue(facade.validEmployeeHasJob("usher","100"));
            facade.removeJobFromEmployee(100,"usher");
            Assert.assertFalse(facade.validEmployeeHasJob("usher","100"));
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void addShift() {
        HashMap<Integer, String> jobMap = new HashMap<>();
        jobMap.put(100, "cashier"); jobMap.put(200, "usher");
        try
        {
            facade.addShift("Ringelblum 9 , Beersheba","2023-07-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("employee list does not contain shift manager", e.getMessage());
        }
        jobMap.put(400, "shift manager");
        try
        {
            facade.addShift("Ringelblum 9 , Beersheba","2023-07-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("cannot save shift , there are jobs missing", e.getMessage());
        }
        jobMap.put(300, "warehouse");
        try{
            facade.addShift("Ringelblum 9 , Beersheba","2023-07-05", "morning", jobMap);
        }
        catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void removeShift() {
        try
        {
            Assert.assertNotNull(facade.getShift(1));
            facade.removeShift(1);
            facade.getShift(1);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Shift with ID: 1 does not exist", e.getMessage());
        }
    }
    @Test
    public void updateEmployeeDetails() {//V
        try {
            //update employee name
            facade.updateEmployeeName(100, "newName");
            Assert.assertEquals("newName", facade.getEmployee(100).getName());
            facade.updateEmployeeName(100, "cash");
            Assert.assertEquals("cash", facade.getEmployee(100).getName());

            //update employee Salary
            facade.updateEmployeeSalary(500, 10000);
            Assert.assertEquals(10000, facade.getEmployee(500).getSalary());
            facade.updateEmployeeSalary(500, 7000);
            Assert.assertEquals(7000, facade.getEmployee(500).getSalary());

            //update employee Bank Account
            facade.updateEmployeeBankAccount(200, "newBank");
            Assert.assertEquals("newBank", facade.getEmployee(200).getBankAccountDetails());
            facade.updateEmployeeBankAccount(200, "bank1");
            Assert.assertEquals("bank1", facade.getEmployee(200).getBankAccountDetails());
        }
        catch (Exception e)
        {
            Assert.fail();
        }

    }
    @Test
    public void addEmployeeToShift()  {
        try {
            Shift shift1 = facade.getShift(1);
            String date1 = shift1.getDate().toString();
            String shiftType1 = shift1.getShiftType();

            //adding employees with constraints on the shift date
            facade.addEmployee("aaron", 987, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails1");
            facade.addEmployeeToShift(1, 987, "cashier");
            Assert.assertTrue(facade.getShift(1).getEmployees().containsKey(987));

            facade.removeEmployeeFromShift(1,987);
            Assert.assertFalse(facade.getShift(1).getEmployees().containsKey(987));

            //now the employee has constraint on the date of shift 1
            facade.addJobConstraint(987, date1, shiftType1);

            try {
                facade.addEmployeeToShift(1, 987, "cashier");
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("Employee with ID: 987 cannot be assigned to this shift", e.getMessage());
            }
            Assert.assertFalse(facade.getShift(1).getEmployees().containsKey(987));


            //adding employees with wrong jobs
            try {
                facade.addEmployeeToShift(1, 987, "usher");
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("Employee hasn't qualified to work at this job", e.getMessage());
            }
            Assert.assertFalse(facade.getShift(1).getEmployees().containsKey(987));


            //adding employee who is already assigned on a shift in a different branch
            HashMap<Integer, String> jobMap1 = new HashMap<>();
            jobMap1.put(101, "cashier"); jobMap1.put(201, "usher"); jobMap1.put(301, "warehouse");jobMap1.put(401, "shift manager");
            facade.addShift("Ringelblum 9 , Beersheba","2023-08-08", "morning", jobMap1);

            HashMap<Integer, String> jobMap2 = new HashMap<>();
            jobMap2.put(100, "cashier"); jobMap2.put(200, "usher"); jobMap2.put(300, "warehouse");jobMap2.put(400, "shift manager");
            facade.addShift("Yasmin 3 , Dimona","2023-08-08", "morning", jobMap2);

            int newShiftID1 = facade.getShiftByDateTypeAndBranch("2023-08-08", "morning", "Ringelblum 9 , Beersheba").getShiftID();
            int newShiftID2 = facade.getShiftByDateTypeAndBranch("2023-08-08", "morning", "Yasmin 3 , Dimona").getShiftID();

            facade.addEmployeeToShift(newShiftID1, 987, "cashier"); //should work
            Assert.assertTrue(facade.getShift(newShiftID1).getEmployees().containsKey(987));
            try {
                facade.addEmployeeToShift(newShiftID2, 987, "cashier"); //should not work
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("employee already assigned to a shift on a different branch", e.getMessage());
            }
            Assert.assertFalse(facade.getShift(newShiftID2).getEmployees().containsKey(987));
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void removeEmployeeFromShift()  {
        try {
            facade.addEmployee("aaron", 9000, "bank1", 5000, "2023-01-01", "cashier", "employmentDetails1");
            facade.addEmployee("aaron", 9001, "bank1", 5000, "2023-01-01", "cashier", "employmentDetails1");

            HashMap<Integer, String> jobMap1 = new HashMap<>();
            jobMap1.put(9000, "cashier"); jobMap1.put(201, "usher"); jobMap1.put(301, "warehouse");jobMap1.put(401, "shift manager");
            facade.addShift("Ringelblum 9 , Beersheba","2023-08-08", "morning", jobMap1);
            int newShiftID1 = facade.getShiftByDateTypeAndBranch("2023-08-08", "morning", "Ringelblum 9 , Beersheba").getShiftID();

            facade.addEmployeeToShift(newShiftID1, 9001, "cashier");

            //now we remove employee 9000 making employee 9001 the only cashier in shift, so we can't remove him now.
            facade.removeEmployeeFromShift(newShiftID1, 9000); //will succeed
            Assert.assertFalse(facade.getShift(1).getEmployees().containsKey(9000));

            try {
                facade.removeEmployeeFromShift(newShiftID1, 9001); //will fail
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("cannot save shift , there are jobs missing", e.getMessage());
            }
            Assert.assertTrue(facade.getShift(newShiftID1).getEmployees().containsKey(9001));
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void getAvailableEmployees()  {
        try
        {
            int before = facade.getAvailableEmployeesForShift("2022-12-12","morning").size();
            facade.addJobConstraint(100,"2022-12-12","morning");
            int after = facade.getAvailableEmployeesForShift("2022-12-12","morning").size();
            Assert.assertEquals(before-1,after);
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void addJobCertificationToEmployee()  {
        try
        {
            facade.addCertToEmployee(400,"shift manager","team leader");
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Employee already has this job Certification", e.getMessage());
        }
        try
        {
            facade.addEmployee("tomer", 87654, "bank1", 88880, "2022-01-01", "driver", "None");
            facade.addCertToEmployee(87654,"driver","A");
            facade.removeJobFromEmployee(87654,"driver");
            facade.removeEmployee(87654);
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void AssignDriverToShift()  {
        HashMap<Integer, String> jobMap = new HashMap<>();
        jobMap.put(100, "cashier"); jobMap.put(200, "usher");jobMap.put(400, "shift manager"); jobMap.put(600, "driver");
        try{
            facade.addShift("Ringelblum 9 , Beersheba","2023-07-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals("Drivers can only be assigned to shifts in the HQ", e.getMessage());
        }
        try
        {
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(602, "driver");
            facade.addShift("Ringelblum 9 , Beersheba","2023-07-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Drivers can only be assigned to shifts in the HQ", e.getMessage());
        }
        try
        {
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(401, "shift manager");
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "morning", jobMap);
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "evening", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("shift manager of drivers is the CEO", e.getMessage());
        }
        try
        {
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(1, "CEO");
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "morning", jobMap);
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "evening", jobMap);
            facade.removeShift(5);
            facade.removeShift(6);
        }
        catch (Exception e)
        {
            Assert.fail();
        }


    }
    @Test
    public void RemoveDriverFromShift()  {
        try
        {
            HashMap<Integer, String> jobMap = new HashMap<>();
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(602, "driver");
            jobMap.put(1, "CEO");
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "morning", jobMap);
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "evening", jobMap);

            facade.removeEmployeeFromShift(5,100);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Employee in shift is not exists in the database!",e.getMessage());
        }
        try
        {
            facade.removeEmployeeFromShift(5,1);
            Assert.fail();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            Assert.assertEquals("CEO is shift manager of drivers , cant remove him from shift",e.getMessage());
        }
        try
        {
            facade.removeEmployeeFromShift(5,600);
            facade.removeShift(5);
            facade.removeShift(6);

        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }

    @Test
    public void getShiftsLastMonth() {
        try
        {
            Assert.assertEquals(facade.getShiftLastMonth("morning", 103).size(), 0);

            String date1 = LocalDate.now().minusDays(5).toString();
            String date2 = LocalDate.now().minusDays(6).toString();
            String date3 = LocalDate.now().minusDays(7).toString();
            String date4 = LocalDate.now().minusDays(50).toString();

            HashMap<Integer, String> jobMap1 = new HashMap<>();
            jobMap1.put(103, "cashier"); jobMap1.put(203, "usher"); jobMap1.put(303, "warehouse");jobMap1.put(403, "shift manager");
            facade.addShift("Ringelblum 9 , Beersheba",date1, "morning", jobMap1);
            facade.addShift("Ringelblum 9 , Beersheba",date2, "morning", jobMap1);
            facade.addShift("Yasmin 3 , Dimona",date3, "morning", jobMap1);
            facade.addShift("Yasmin 3 , Dimona",date4, "morning", jobMap1);

            Assert.assertEquals(facade.getShiftLastMonth("morning", 103).size(), 3);

            facade.removeShift(facade.getShiftByDateTypeAndBranch(date3, "morning", "Yasmin 3 , Dimona").getShiftID());

            Assert.assertEquals(facade.getShiftLastMonth("morning", 203).size(), 2);
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }

    @Test
    public void getShiftByDateTypeAndBranch() {
        HashMap<Integer, String> jobMap = new HashMap<>();
        jobMap.put(100, "cashier");
        jobMap.put(200, "usher");
        jobMap.put(400, "shift manager");
        jobMap.put(300, "warehouse");
        try {
            Assert.assertNull(facade.getShiftByDateTypeAndBranch("Ringelblum 9 , Beersheba", "2023-07-05", "morning"));
        } catch (Exception e) {
            Assert.assertEquals("Shift with given date, type and branch was not found",e.getMessage());
        };
        try{
            facade.addShift("Ringelblum 9 , Beersheba","2023-07-05", "morning", jobMap);
        }
        catch (Exception e){
            Assert.fail();
        }
    }
}