package Presentation.Model.EmployeeModuleModel;

import ServiceLayer.Objects.EmployeeObjects.FEmployee;

import java.time.LocalDate;
import java.util.Map;

public class EmployeeModel {
    private String name;
    private int ID;
    private String bankAccountDetails;
    private int salary;
    private LocalDate employmentStartDate;
    private String employmentDetails;
    private Map<LocalDate, String> jobConstraints;
//    private Vector<Job> jobs;

    private String branch;

    public EmployeeModel(FEmployee employee){
        this.name = employee.getName();
        this.ID = employee.getID();
        this.bankAccountDetails = employee.getBankAccountDetails();
        this.salary = employee.getSalary();
        this.employmentStartDate = employee.getEmploymentStartDate();
        this.employmentDetails = employee.getEmploymentDetails();
        this.jobConstraints = employee.getJobConstraints();
//        this.jobs = employee.getJobs();
    }

    public String getName() {
        return name;
    }
    public int getID() {
        return ID;
    }
    public String getBankAccountDetails() {
        return bankAccountDetails;
    }
    public int getSalary() {
        return salary;
    }
    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }
    public String getEmploymentDetails() {
        return employmentDetails;
    }
    public Map<LocalDate, String> getJobConstraints() {
        return jobConstraints;
    }
//    public Vector<Job> getJobs() {
//        return jobs;
//    }
public String toString() {
        //
    return
            "Name=" + name +" | "+
                    ", ID=" + ID +" | "+
                    ", bankAccountDetails=" + bankAccountDetails.toString() +" | "+
                    ", Salary=" + salary +" | "+
                    ", EmploymentStartDate=" + employmentStartDate.toString()
            ;
}

    public String driverToString(){
        return "Name: "+name+"    Id: "+ID;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }
}
