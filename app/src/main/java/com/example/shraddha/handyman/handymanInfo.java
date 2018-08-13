package com.example.shraddha.handyman;

public class handymanInfo {
    public String email;
    public String password;
    public String handyManName;
    public String handymanPhone;
    public String occupation;
    public String address;
    public String dateJoined;
    public String tokenId;

    public handymanInfo(){}

    public handymanInfo(String email, String password, String handyManName, String handymanPhone, String occupation, String address, String dateJoined, String tokenId) {
        this.email = email;
        this.password = password;
        this.handyManName = handyManName;
        this.handymanPhone = handymanPhone;
        this.occupation = occupation;
        this.address = address;
        this.dateJoined = dateJoined;
        this.tokenId=tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getEmail() {
        return email;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getPassword() {
        return password;
    }

    public String getHandyManName() {
        return handyManName;
    }

    public String getHandymanPhone() {
        return handymanPhone;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getAddress() {
        return address;
    }

    public String getDateJoined() {
        return dateJoined;
    }
}
