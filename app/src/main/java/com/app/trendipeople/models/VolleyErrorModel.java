package com.app.trendipeople.models;

import org.json.JSONObject;

/****************************************************************
 * VolleyErrorModel.java
 * Created on July 3, 2017
 * Copyright (c) 2014, NETGEAR, Inc.
 * 350 East Plumeria, San Jose California, 95134, U.S.A.
 * All rights reserved.
 * This software is the confidential and proprietary information of
 * NETGEAR, Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with NETGEAR.
 *
 * @author VVDN
 *         Class Name: VolleyErrorModel
 *         Description: This class is used for getting/setting the type of volloey error and model its message
 ****************************************************************/
public class VolleyErrorModel {
    boolean isResponseNeedToParse=false;
    String strMessage="";
    JSONObject mJsonObject =new JSONObject();

    public JSONObject getmJsonObject() {
        return mJsonObject;
    }

    public void setmJsonObject(JSONObject mJsonObject) {
        this.mJsonObject = mJsonObject;
    }

    public boolean isResponseNeedToParse() {
        return isResponseNeedToParse;
    }

    public void setResponseNeedToParse(boolean responseNeedToParse) {
        isResponseNeedToParse = responseNeedToParse;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }



}
