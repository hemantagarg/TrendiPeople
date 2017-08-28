package com.app.trendipeople.models;

/**
 * Created by hemanta on 11-08-2017.
 */

public class NotificationModel {

    private String NotificationId, SenderId, ServiceId, ReceiverId,
            CreateDate, Message, SenderName, ReceiverName, Debit, ServiceName, Date, Credit,startTime,endTime;
    private int rowType;

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(String notificationId) {
        NotificationId = notificationId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    public int getRowType() {
        return rowType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
