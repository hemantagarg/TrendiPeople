package com.app.trendipeople.models;

import java.util.ArrayList;

/**
 * Created by hemanta on 29-07-2017.
 */

public class ModelCategory {

    private String CategoryId, Username, Usermobile, UserEmail, ServiceTime, ServiceDate;

    private String ServicePrice;
    private String CategoryName;
    private String CategoryImage;
    private String SubCategoryId;
    private String SubCategoryName;
    private String SubCategoryImage;
    private String ServiceId;
    private String ServiceName, comment;
    private String ServiceImage, OrderId;

    public ArrayList<Images> getImagesArrayList() {
        return imagesArrayList;
    }

    public void setImagesArrayList(ArrayList<Images> imagesArrayList) {
        this.imagesArrayList = imagesArrayList;
    }

    private ArrayList<Images> imagesArrayList;
    private int rowType = 0;
    private String freelancer_mile;
    private String freelancer_id, date, message;
    private String freelancer_name, GenderType, freelancer_image, freelancer_service_rate, freelancer_rating;
    private String porfolioImage, ConverId;
    private String imageId;
    private String servies, UserImage;
    private String bussinessDetailsArray;
    private String portfolioDetailsArray, review;
    private String about, VendorName, VendorMobile, VendorEmail, VendorImage, ratingArray,Vendor_Id;
    private boolean selected;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public String getVendorMobile() {
        return VendorMobile;
    }

    public void setVendorMobile(String vendorMobile) {
        VendorMobile = vendorMobile;
    }

    public String getVendorEmail() {
        return VendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        VendorEmail = vendorEmail;
    }

    public String getVendorImage() {
        return VendorImage;
    }

    public void setVendorImage(String vendorImage) {
        VendorImage = vendorImage;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUsermobile() {
        return Usermobile;
    }

    public void setUsermobile(String usermobile) {
        Usermobile = usermobile;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getServiceTime() {
        return ServiceTime;
    }

    public void setServiceTime(String serviceTime) {
        ServiceTime = serviceTime;
    }

    public String getServiceDate() {
        return ServiceDate;
    }

    public void setServiceDate(String serviceDate) {
        ServiceDate = serviceDate;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }


    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }


    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getSubCategoryImage() {
        return SubCategoryImage;
    }

    public void setSubCategoryImage(String subCategoryImage) {
        SubCategoryImage = subCategoryImage;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServiceImage() {
        return ServiceImage;
    }

    public void setServiceImage(String serviceImage) {
        ServiceImage = serviceImage;
    }

    public void setFreelancer_id(String freelancer_id) {
        this.freelancer_id = freelancer_id;
    }

    public void setFreelancer_name(String freelancer_name) {
        this.freelancer_name = freelancer_name;
    }

    public void setFreelancer_mile(String freelancer_mile) {
        this.freelancer_mile = freelancer_mile;
    }

    public String getFreelancer_mile() {
        return freelancer_mile;
    }

    public String getFreelancer_id() {
        return freelancer_id;
    }

    public String getFreelancer_name() {
        return freelancer_name;
    }

    public String getGenderType() {
        return GenderType;
    }

    public void setGenderType(String genderType) {
        GenderType = genderType;
    }

    public String getPorfolioImage() {
        return porfolioImage;
    }

    public void setPorfolioImage(String porfolioImage) {
        this.porfolioImage = porfolioImage;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setServies(String servies) {
        this.servies = servies;
    }

    public void setBussinessDetailsArray(String bussinessDetailsArray) {
        this.bussinessDetailsArray = bussinessDetailsArray;
    }

    public void setPortfolioDetailsArray(String portfolioDetailsArray) {
        this.portfolioDetailsArray = portfolioDetailsArray;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getServies() {
        return servies;
    }

    public String getBussinessDetailsArray() {
        return bussinessDetailsArray;
    }

    public String getPortfolioDetailsArray() {
        return portfolioDetailsArray;
    }

    public String getAbout() {
        return about;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFreelancer_image() {
        return freelancer_image;
    }

    public void setFreelancer_image(String freelancer_image) {
        this.freelancer_image = freelancer_image;
    }

    public String getFreelancer_service_rate() {
        return freelancer_service_rate;
    }

    public void setFreelancer_service_rate(String freelancer_service_rate) {
        this.freelancer_service_rate = freelancer_service_rate;
    }

    public String getFreelancer_rating() {
        return freelancer_rating;
    }

    public void setFreelancer_rating(String freelancer_rating) {
        this.freelancer_rating = freelancer_rating;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getRatingArray() {
        return ratingArray;
    }

    public void setRatingArray(String ratingArray) {
        this.ratingArray = ratingArray;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConverId() {
        return ConverId;
    }

    public void setConverId(String converId) {
        ConverId = converId;
    }

    public String getVendor_Id() {
        return Vendor_Id;
    }

    public void setVendor_Id(String vendor_Id) {
        Vendor_Id = vendor_Id;
    }
}
