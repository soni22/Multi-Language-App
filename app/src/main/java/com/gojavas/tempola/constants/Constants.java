package com.gojavas.tempola.constants;

/**
 * Created by gjs331 on 7/3/2015.
 */
public class Constants {


    public static final String PREFS_NAME = "com.gojavas.tempola";

    public static final String TOKEN="token";
    public static final String USERID="id";
    public static final String REQUESTID="requestid";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";



    public static final String CURRENT_STATE="current_state";

    public static final String REQUEST_ID="request_id";
    public static final String REQUEST_ID_TIME="requesttime";


    public static final String NO_REQUEST_ID="";
    public static final String REQUEST_ARRIVED="10";
    public static final String REJECTED_STATE="0";
    public static final String ACCEPTED_STATE="1";
    public static final String DRIVER_ARRIVED_STATE="3";
    public static final String TRIP_STARTRED_STATE="4";
    public static final String TRIP_COMPLETED_STATE="5";
    public static final String PROVIDER_RATING_STATE="6";




    public static final String GET_VEHICAL_TYPES = "http://tempola.in/api/public/application/types";
    public static final String BASE_URL="http://tempola.in/api/public/provider/";
    public static final String LOGIN_URL=BASE_URL+"login";
    public static final String REQUEST_RESPONSE_URL=BASE_URL+"respondrequest";
    public static final String UPDATE_PROVIDER_LOCATION = BASE_URL+ "location";
    public static final String GET_ALL_REQUESTS = BASE_URL + "getrequests?";
    public static final String CHECK_REQUEST_STATUS = BASE_URL+"getrequest?";




    public static final String TOGGLE_STATE = BASE_URL + "togglestate";

}
