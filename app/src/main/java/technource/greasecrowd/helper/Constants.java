package technource.greasecrowd.helper;

import android.content.Intent;
import android.widget.Toast;

/**
 * Created by technource on 17/7/17.
 */
public class Constants {

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";
    //  public static final String PASSWORD_PATTERN = "((?=.*\\d)(^[A-Za-z0-9!@.#$%^&*-_]{8,15}$))";
    public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[@#$%^&+=]).{8,15})";
    public static final String NUMBER_PATTERN = "[0-9]+";
    public static final String FIXDATE_01_01_1970 = "01/01/1970";
    public static final String MMddyyyyFormat = "MM/dd/yyyy";
    public static final String TIME_STAMP_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public static final String TIME_STAMP_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SPLIT = "/";
    public static final String MESSAGE = "message";
    public static final String IS_LOGGEDIN = "is_loggedin";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9._]{4,20}$";
    public static final String GCM_ID = "gcm_id";
    public static final String TYPE_NORMAL = "1";
    public static final String TYPE_FACEBOOK = "2";
    public static final String TYPE_GOOGLEPLUS = "3";
    public static final String TYPE_TWITTER = "4";
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 227;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_TO = 722;
    public static final String OLD_PASSOWRD = "old_password";
    public static final String NEW_PASSWORD = "new_password";
    public static final int INTENT_FLAGS =
            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
    public static final String FACEBOOK = "facebook";
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOTER = 2;

    public static final int TIMEOUT = 60 * 1000;
    public static final int RETRIES = 0;
    public static final int TOAST_TIME = Toast.LENGTH_SHORT;
    public static final String EMAIL_ = "email";
    public static final String PASSWORD = "password";
    public static final String UDID = "ud_id";
    public static final String DEVICE_TYPE_ = "device_type";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String STATUS = "status";
    public static final String MOT_MEM_IMG = "mot_mem_img";
    public static final String LOAD_MORE = "next";
    public static final String DEVICE_TYPE = "2";
    public static final String LoginUserDetails = "LoginUserDetails";
    public static final String JobDetails = "JobDetails";
    public static final String SERVICE_NAME = "service_name";
    public static final String SOCIAL = "social";
    public static final String SOCIALDATA = "socialdata";
    public static final String VEHICLEDATA = "vehicledata";
    public static final String TIME_SPLIT = ":";
    public static final String TRIP_DATA = "tripdata";
    public static final String FROMPROFILE = "fromProfile";
    public static final int PROFILE_REQUEST_CODE = 2207;
    public static final int REQUEST_CODE = 1234;
    public static final float MAP_ROUTE_WIDTH = 5;
    public static final String MAX_PRICE = "max_price";
    public static final int PHONE_LENGTH = 15;
    public static final String PRIVACY_POLICY = "privacy_policy";
    public static final String TOS = "tos";
    public static final String HOW_IT_WORKS = "how_it_works";
    public static final String CONTACT_EMAIL = "contact_email";
    public static final String WITHOUT_SS_TIME_STAMP_FORMAT = "dd/MM/yyyy HH:mm";
    public static final long LOCATION_INTERVAL = 10000;
    public static final String DRIVER = "1";
    public static final String PASSENGER = "2";
    public static final String MSG = "msg";
    public static final String REF_CODE = "ref_code";
    public static final String REF_URL = "ref_url";
    public final static String CONDENSED_FONT = "poppins_medium.TTF";
    public static final String USERTYPE = "usertype";
    public static final String CAR_OWNER = "0";
    public static final String GERAGE_OWNER = "1";
    public static String job_idBaseActivity;

    public static String Job_idBaseActivity;
    public static String cat_idBaseActivity;


    public class Fonts {

        public static final String POPONS_REGULAR = "sf_ui_display_light.otf";
        public static final String POPPINS_REGULAR = "poppins_regular.otf";
        public static final String POPPINS_BOLD = "poppins_bold.otf";
    }

    public class NotificationTags {

        public static final String SHOW_NOTI = "show_noti";
        public static final String BODY = "body";
        public static final String CHAT_NOTI = "CHAT_NOTI_PRIVATE";
        public static final String CHAT_NOTI_PUBLIC = "CHAT_NOTI_PUBLIC";
    }

    public class LoginType {

        public static final String SOCIAL = "social";
        public static final String NORMAL = "normal";
        public static final String SOCIAL_ID = "social_id";
        public static final String DEVICE_TYPE = "device_type";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String SERVICE_NAME = "service_name";
        public static final String LOGIN_TYPE = "login_type";
        public static final String USER_TYPE = "user_type";
        public static final String IMAGE = "logo_image";
        public static final String USERNAME = "un";
        public static final String ID = "id";
        public static final String PASSWORD = "pwd";
        public static final String MAKE_ID = "make_id";
        public static final String MODEL_ID = "model_id";
        public static final String CAR_TRANS = "car_trans";
        public static final String BADGE_ID = "badge_id";
        public static final String REG_NO = "reg_no";
        public static final String YEAR = "year";
    }


    public class Reviews {

        public static final String BN = "bn";
        public static final String IMG = "img";
        public static final String AVG_RATING = "avg_rating";
        public static final String REVIEWS = "reviews";
        public static final String JOB_ID = "job_id";
        public static final String REVIEW = "review";
        public static final String RATING = "rating";
        public static final String TITLE = "title";
        public static final String IMAGE = "image";
        public static final String NAME = "name";
        public static final String LoC = "loc";
        public static final String JOB_USER_REVIEW_REPLIES = "job_user_review_replies";
    }

    public class SIGN_UP {

        public static final String SERVICE_NAME = "service_name";
        public static final String SCRN = "scrn";
        public static final String USER_ID = "user_id";
        public static final String SOCIAL_IMG = "social_img";
        public static final String isallmakes = "is_all_makes";
        public static final String isselmakes = "is_sel_makes";
        public static final String SIGNUP_TYPE = "signup_type";
        public static final String selmakes = "sel_makes";
        public static final String keywords = "keywords";
        public static final String facilities = "facilities";
        public static final String hours = "tr_hrs";
        public static final String FN = "fn";
        public static final String LN = "ln";
        public static final String EMAIL = "email";
        public static final String MOBILE = "mobile";
        public static final String UN = "un";
        public static final String STATE = "state";
        public static final String SOCIAL_ID = "social_id";
        public static final String PWD = "pwd";
        public static final String SUB = "sub";
        public static final String IS_NEWSLETTER = "is_newsletter";
        public static final String BNAME = "bname";
        public static final String BN = "bn";
        public static final String TEL = "tel";
        public static final String ABN_NO = "abn_no";
        public static final String STREET = "street";
        public static final String ZIP = "zip";
        public static final String REF_CODE = "ref_code";
        public static final String STEP = "step";
        public static final String LOGIN_TOKEN = "jwt";

    }

    public class GALLARY {

        public static final String SERVICE_NAME = "service_name";
        public static final String ACTION = "action";
        public static final String GALLERY_IMG = "gallery_img";
        public static final String IMG_KEY = "img_key";
        public static final String MEMBERSHIP_IMG = "membership_img";
    }

    public class USER_DETAILS {

        public static final String DOB = "birth_year";
        public static final String COUNTRY_CODE = "country_code";
        public static final String DRIVER_AGE = "driver_age";
        public static final String SIGNUP_TYPE = "signup_type";
        public static final String OFFERED_RIDES = "offered_rides";
        public static final String MEMBER_SINCE = "member_since";
        public static final String FNAME = "fname";
        public static final String LNAME = "lname";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String USER_ID = "user_id";
        public static final String JWT_TOKEN = "jwt_token";
        public static final String SELLERS = "sellers";
        public static final String BUYERS = "buyers";
        public static final String DATE = "date";
        public static final String CITY_ID = "city_id";
        public static final String MODEL = "model";
        public static final String PASSWORD = "password";
        public static final String GROUP_MANAGER = "group_manager";
        public static final String LOCATION = "location";
        public static final String FIRSTNAME = "first_name";
        public static final String BUSINESS_NAME = "business_name";
        public static final String VERIFY_CODE = "verify_code";
        public static final String VERIFICATION_CODE = "verification_code";
        public static final String LASTNAME = "last_name";
        public static final String GENDER = "gender";
        public static final String IMAGE = "image";
        public static final String DATA = "data";
        public static final String REGISTERDATE = "register_from";
        public static final String MOBILE = "mobile";
        public static final String SUBURB = "suburb";
        public static final String STATE = "state";
        public static final String POSTCODE = "postcode";
        public static final String PROFILE_IMAGE = "prof_img";

        public static final String BIO = "bio";

    }

    public class SocialSignUp {

        public static final String FNAME = "fname";
        public static final String LNAME = "lname";
        public static final String COUNTRY_CODE = "country_code";
        public static final String CONTACT_NO = "contact_no";
        public static final String SOCIAL_ID = "social_id";
        public static final String SIGNUP_TYPE = "signup_type";
        public static final String DEVICE_TYPE = "device_type";
        public static final String NORMAL = "1";
        public static final String FACEBOOK = "2";
        public static final String GOOGLE = "3";
        public static final String TWITTER = "4";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String CONTRY_CODE = "country_code";
        public static final String PASSWORD = "password";
        public static final String VERIFICATION_CODE = "verification_code";
        public static final String USER_ID = "user_id";
        public static final String PAGE = "page";
        public static final String START = "start";
        public static final String PROFILE_IMAGE = "profile_image";
    }

    public class Splashscreen {

        public static final String SERVICE_NAME = "service_name";
        public static final String SPLASH = "splash";
        public static final String SERVICES = "services";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String IMAGE = "image";
        public static final String SPLASHUSERDETAILS = "SplashUserDetails";
        public static final String SPLASH_VIDEO = "splash_video";
        public static final String VIDEO = "video";
        public static final String SPLASHUSERVIDEODETAILS = "SplashUservideoDetails";
        public static final String SAFETY_REPORT = "SafetyReport";
    }

    public class Forgotpassword {

        public static final String SERVICE_NAME = "service_name";
        public static final String FORGOT_PWD = "forgot_pwd";
        public static final String USER_TYPE = "user_type";
        public static final String EMAIL = "email";
        public static final String OTP = "otp";
    }

    public class Changepassword {

        public static final String SERVICE_NAME = "service_name";
        public static final String USER_TYPE = "user_type";
        public static final String CHG_USR_PWD = "chg_usr_pwd";
        public static final String CPWD = "cpwd";
        public static final String NPWD = "npwd";

    }

    public class Paypal {

        public static final String SERVICE_NAME = "service_name";
        public static final String ACTION = "action";
        public static final String SET = "set";
        public static final String GET = "get";
        public static final String PAYPAL_INFO = "paypal_info";
        public static final String PAYPAL_ID = "paypal_id";


    }

    public class categories {

        public static final String SERVICE_NAME = "service_name";
        public static final String CATEGORIES = "categories";
        public static final String SERVICES = "services";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String PLACEHOLDER = "placeholder";
        public static final String IMAGE = "image";
        public static final String ITEXT = "itext";

    }

    public class ResetPassword {

        public static final String SERVICE_NAME = "service_name";
        public static final String RESET_PWD = "reset_pwd";
        public static final String OTP = "otp";
        public static final String USER_TYPE = "user_type";
        public static final String EMAIL = "email";
        public static final String PWD = "pwd";
    }

    public class Garageownersignup {

        public static final String params1 = "signup_garage";
        public static final String params_fixed = "is_fixed";
        public static final String params_mobile = "is_mobile";
        public static final String params_services = "services";
    }

    public class NotificationSetting {

        public static final String PUSH_NOTIFICATION = "push_flag";
        public static final String EMAIL_NOTIFICATION = "email_flag";
        public static final String SMS_NOTIFICATION = "sms_flag";
        public static final String Params = "user_type";
    }

    public class PostNewJob {
        public static final String STEP = "step";
        public static final String JWT = "jwt";
        public static final String JT = "jt";
        public static final String CAR_ID = "car_id";
        public static final String JID = "jid";
        public static final String ASSIGNED_TO_GARAGE = "assigned_to_garage";
        public static final String CAT_ID = "cat_id";
        public static final String SC_ID = "sc_id";
        public static final String PR_ID = "pr_id";
        public static final String ADD_INCL = "add_incl";
        public static final String DROP_LOC = "drop_loc";
        public static final String TY_BRAN = "ty_bran";
        public static final String TY_MOD = "ty_mod";
        public static final String TY_SPEC = "ty_spec";
        public static final String NO_TY = "no_ty";
        public static final String THUMBNAIL = "thumbnail";
        public static final String CAR_VIDEO = "car_video";
        public static final String CAR_IMAGE = "car_image";


        public static final String EMERGENCY = "emergency";
        public static final String HELP = "help";
        public static final String INSURANCE = "insurance";
        public static final String DROP_TIME = "drop_time";
        public static final String PICK_TIME = "pick_time";
        public static final String FLEXIBILITY = "flexibility";
        public static final String JOB_DESC = "job_des";
        public static final String INS_COMP = "ins_comp";
        public static final String POLICY_NUM = "policy_num";
        public static final String CLAIM_NUM = "claim_num";
        public static final String CU_TOW_LOC = "cu_tow_loc";
        public static final String DES_TOW_LOC = "des_tow_loc";
        public static final String IN_RO_ASS = "in_ro_ass";
        public static final String IN_STD_LOG = "in_std_log";
        public static final String in_std_log = "no_veh";

    }

    public class SEARCH_CROWD {

        public static final String PAGE_NUMBER = "page_number";
        public static final String USER_TYPE = "user_type";
        public static final String KEYWORD = "keyword";
        public static final String CATEGORY = "category";
        public static final String MAKE = "make";
        public static final String MODEL = "model";
        public static final String SORT_BY = "sort_by";
    }

    public class HOMEGARAGEOWNER {

        public static final String PAGE_NUMBER = "page_number";
        public static final String USER_TYPE = "user_type";
        public static final String KEYWORD = "keyword";
        public static final String CAT = "category";
        public static final String MAKE = "make";
        public static final String MODEL = "model";
        public static final String SUBCAT = "subcategory";
        public static final String GARANGE_ID = "garange_id";
        public static final String DISTANCE = "distance";
        public static final String SORTBY = "sortby";
        public static final String RECORDS = "records";
    }

    public class QUOTEJOBDETAILS {

        public static final String PAGE_NUMBER = "page_number";
        public static final String USER_TYPE = "user_type";
        public static final String JOB_ID = "job_id";
        public static final String CAT = "category";
        public static final String MAKE = "make";
        public static final String MODEL = "model";
        public static final String SUBCAT = "subcategory";
        public static final String GARANGE_ID = "garange_id";
        public static final String DISTANCE = "distance";
        public static final String SORTBY = "sortby";
        public static final String RECORDS = "records";


    }

    public class JOB_ACTIONS {
        public static final String ACTION = "action";
        public static final String PRICE = "price";
        public static final String ADD_OFFER = "add_offer";
        public static final String JID = "jid";
        public static final String AWARDED_GARAGE_ID = "awarded_garage_id";
        public static final String SEND_NEW_TIME = "send_new_time";
        public static final String WORK = "work";
        public static final String AMOUNT = "amount";
        public static final String car_id = "car_id";
        public static final String RECO = "reco";
        public static final String RECO_DETAIL = "reco_detail";
        public static final String GENERAL_COMMENTS = "general_comments";
        public static final String TYPE = "type";

    }

    public class NOTIFICATIONS {
        public static final String NOTIFICATION_ID = "notification_id";
    }

    public class SUBMIT_BIDS {
        public static final String JOB_ID = "job_id";
        public static final String GARAGE_ID = "garage_id";
        public static final String BID_PRICE = "bid_price";
        public static final String BID_COMMENT = "bid_comment";
        public static final String ADD_OFFER = "add_offer";
        public static final String ADD_OFFER_PRICE = "add_offer_price";
        public static final String TOTAL = "total";
        public static final String SERVICES = "services";
        public static final String SERVICE_ID = "service_id";
        public static final String INCLUSION = "inclusion";
        public static final String FLEET_ARR = "fleet_arr";
        public static final String DATETIME1 = "datetime1";
        public static final String DATETIME2 = "datetime2";
    }

    public class FREE_INCLUSION {

        public static final String FREE_INCLUSION = "free_inclusion";
        //***
    }
    public class TRANSACTION_HISTORY {

        public static final String MONTH = "month";
        public static final String YEAR = "year";
        public static final String PAGE_NUMBER = "page_number";
        public static final String NUMBER_OF_RECORDS = "number_of_records";
        //***
    }
    public class BRAINTREE_PAYMENT {

        public static final String JU_ID = "ju_id";
        public static final String CURRENCY = "currency";
        public static final String AMT = "amt";
        public static final String GARAGE_ID = "garage_id";
        public static final String PAYMENTMETHODNONCE = "paymentMethodNonce";
        //***
    }





}

