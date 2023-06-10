package com.example.schoolproject.utils;

public class URLs {

    private static final String ROOT_URL = "http://192.168.100.63/SchoolProject/testTwo/admin/utils/apiLogin.php?apicall=";

    private static final String ALERT_CRUD_URL = "http://192.168.100.63/SchoolProject/testTwo/admin/service/push/api/utils/";

    private static final String NOTIFICATION_URL = "http://192.168.100.63/SchoolProject/testTwo/admin/service/push/api/utils/";

    private static final String PROFILE_URL = "http://192.168.100.63/SchoolProject/testTwo/admin/service/users/api/utils/";


    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_UPDATE_USER_DATA= PROFILE_URL + "updateUser.php";
    public static final String URL_UPDATE_USER_PASSWORD= PROFILE_URL + "updatePassword.php";
    public static final String URL_GET_USER_DATA = PROFILE_URL + "getUserData.php";
    public static final String URL_DELETE_USER_DATA = PROFILE_URL + "deleteUser.php";
    public static final String URL_GET_USER_COUNT_DATA = PROFILE_URL + "getNotificationsCount.php";
    public static final String URL_UPDATE_USER_STATUS = PROFILE_URL + "updateStatus.php";
    public static final String URL_UPDATE_USER_LOCATION = PROFILE_URL + "updateLocation.php";


    public static final String URL_CREATE_ALERT = ALERT_CRUD_URL + "create.php";
    public static final String URL_READ_ALERT = ROOT_URL + "read.php";
    public static final String URL_SINGLE_ALERT = ROOT_URL + "single.php";
    public static final String URL_UPDATE_ALERT = ROOT_URL + "update.php";
    public static final String URL_DELETE_ALERT = NOTIFICATION_URL + "delete.php";
    public static final String URL_USERS_ALERT = NOTIFICATION_URL + "userAlerts.php";
    public static final String URL_OPTIONS = NOTIFICATION_URL + "alertOptions.php";


    public static final String URL_GET_NOTIFICATIONS = NOTIFICATION_URL + "notificationsSent.php";
    public static final String URL_MARK_NOTIFICATION_RED = NOTIFICATION_URL + "markAsRead.php";
    public static final String URL_DELETE_NOTIFICATION = NOTIFICATION_URL + "deleteNotificationSent.php";

}
