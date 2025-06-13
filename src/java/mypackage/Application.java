package mypackage;

import java.util.Date;

public class Application {
    private String appId;
    private String fullName;
    private Date dob;
    private String nationality;
    private String policeVerification;
    private String approvalStatus;
    private String passportNo;

    // Getter and setter methods
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }
    
    public String getPassportNo() {
        return passportNo;
    }
    
    

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPoliceVerification() {
        return policeVerification;
    }

    public void setPoliceVerification(String policeVerification) {
        this.policeVerification = policeVerification;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
