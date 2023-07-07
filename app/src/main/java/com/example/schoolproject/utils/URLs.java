package com.example.schoolproject.utils;

public class URLs {

    private static final String ROOT_IP_ADDRESS = "192.168.100.18";
    //private static final String ROOT_IP_ADDRESS = "192.168.100.63";

    private static final String ROOT_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/utils/apiLogin.php?apicall=";

    private static final String ALERT_CRUD_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/push/api/utils/";

    private static final String NOTIFICATION_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/push/api/utils/";

    private static final String PROFILE_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/users/api/utils/";

    private static final String FEEDBACK_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/contact/api/utils/";

    private static final String CROP_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/cropsDistribution/api/utils/";
    private static final String EXPENSE_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/cropsDistribution/api/utils/";
    private static final String DISTRIBUTION_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/cropsDistribution/api/utils/";
    private static final String SEASON_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/cropsDistribution/api/utils/";

    private static final String DASHBOARD_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/cropsDistribution/api/utils/";

    private static final String NEWS_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/news/api/utils/";
    private static final String NEWS_IMAGE_URL = "http://"+ROOT_IP_ADDRESS+"/SchoolProject/testTwo/admin/service/news";


    //USER PROFILE
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_UPDATE_USER_DATA= PROFILE_URL + "updateUser.php";
    public static final String URL_UPDATE_USER_PASSWORD= PROFILE_URL + "updatePassword.php";
    public static final String URL_GET_USER_DATA = PROFILE_URL + "getUserData.php";
    public static final String URL_DELETE_USER_DATA = PROFILE_URL + "deleteUser.php";
    public static final String URL_GET_USER_COUNT_DATA = PROFILE_URL + "getNotificationsCount.php";
    public static final String URL_UPDATE_USER_STATUS = PROFILE_URL + "updateStatus.php";
    public static final String URL_UPDATE_USER_LOCATION = PROFILE_URL + "updateLocation.php";

    public static final String URL_GET_ACTIVE_WEATHER_DATA = PROFILE_URL + "getActiveWeatherData.php";

    //ALERTS
    public static final String URL_CREATE_ALERT = ALERT_CRUD_URL + "create.php";
    public static final String URL_READ_ALERT = ROOT_URL + "read.php";
    public static final String URL_SINGLE_ALERT = ROOT_URL + "single.php";
    public static final String URL_UPDATE_ALERT = ROOT_URL + "update.php";
    public static final String URL_DELETE_ALERT = NOTIFICATION_URL + "delete.php";
    public static final String URL_USERS_ALERT = NOTIFICATION_URL + "userAlerts.php";
    public static final String URL_OPTIONS = NOTIFICATION_URL + "alertOptions.php";


    //NOTIFICATIONS
    public static final String URL_GET_NOTIFICATIONS = NOTIFICATION_URL + "notificationsSent.php";
    public static final String URL_MARK_NOTIFICATION_RED = NOTIFICATION_URL + "markAsRead.php";
    public static final String URL_DELETE_NOTIFICATION = NOTIFICATION_URL + "deleteNotificationSent.php";

    //FEEDBACK
    public static final String URL_POST_FEEDBACK = FEEDBACK_URL + "createFeedback.php";

    //CROPDistributsion
    public static final String URL_CREATE_CROP = CROP_URL + "createCrop.php";
    public static final String URL_GET_CROP = CROP_URL + "getCrops.php";
    public static final String URL_CROP_UPDATE = CROP_URL + "updateCrop.php";
    public static final String URL_CROP_DELETE = CROP_URL + "deleteCrop.php";
    public static final String URL_CREATE_EXPENSE = CROP_URL + "createExpense.php";
    public static final String URL_EXPENSE_UPDATE = CROP_URL + "updateExpense.php";
    public static final String URL_EXPENSE_DELETE = CROP_URL + "deleteExpense.php";
    public static final String URL_CREATE_DISTRIBUTION = CROP_URL + "createDistribution.php";
    public static final String URL_GET_DISTRIBUTION = CROP_URL + "getDistribution.php";
    public static final String URL_DISTRIBUTION_UPDATE = CROP_URL + "updateDistribution.php";
    public static final String URL_DISTRIBUTION_DELETE = CROP_URL + "deleteDistribution.php";
    public static final String URL_CREATE_SEASON = CROP_URL + "createSeason.php";
    public static final String URL_GET_SEASON = CROP_URL + "getSeason.php";
    public static final String URL_SEASON_UPDATE = CROP_URL + "updateSeason.php";
    public static final String URL_SEASON_DELETE = CROP_URL + "deleteSeason.php";

    //NEWS
    public static final String URL_GET_NEWS = NEWS_URL + "getNewsInformation.php";


    //IMAGE URL
    public static final String URL_GET_IMAGE = NEWS_IMAGE_URL;

    //DASHBOARDdATA
    public static final String URL_GET_DASHBOARD_GRAPH = DASHBOARD_URL + "getUserDashboard.php";


}
