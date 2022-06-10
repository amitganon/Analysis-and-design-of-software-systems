package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

public class JobMessages extends TimeStampChecker {
    private String job;
    private String message;

    private String branch;

    public JobMessages(String branch , String job, String mes)
    {
        this.job = job;
        this.message = mes;
    }

    public String getJob() {
        return job;
    }

    public String getMessage() {
        return message;
    }

    public String getBranch() {
        return branch;
    }
}
