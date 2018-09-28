/**
 * 
 */
package no.systema.godsno.util;

/**
 * 
 * All type of system constants in general
 * 
 * @author oscardelatorre
 * @date June 2018
 * 
 *
 */
public final class GodsnoConstants {
	
	//session constants
	public static final String ACTIVE_URL_RPG_GODSNO = "activeUrlRPG_Godsno";
	public static final String ACTIVE_URL_RPG_UPDATE_GODSNO = "activeUrlRPGUpdate_Godsno";
	public static final String ACTIVE_URL_RPG_FETCH_ITEM_GODSNO = "activeUrlRPGFetchItem_Godsno"; //Ajax
	public static final String ACTIVE_URL_RPG_INITVALUE = "=)";
	
	//actions
	public static final String EDIT_ACTION_ON_TOPIC = "editActionOnTopic";
	public static final String EDIT_ACTION_ON_TOPIC_ITEM = "editActionOnTopicItem";
	
	public static final String ACTION_FETCH = "doFetch";
	public static final String ACTION_UPDATE = "doUpdate";
	public static final String ACTION_CREATE = "doCreate";
	public static final String ACTION_DELETE = "doDelete";
	public static final String ACTION_SEND = "doSend";
	
	//update modes
	public static final String MODE_UPDATE = "U";
	public static final String MODE_UPDATE_TRANSITT_KEY = "UTR";
	public static final String MODE_ADD = "A";
	public static final String MODE_DELETE = "D";
	public static final String MODE_SEND = "S";
	
	//url
	public static final String URL_CHAR_DELIMETER_FOR_URL_WITH_HTML_REQUEST_GET = "?";
	public static final String URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST = "&"; //Used for GET and POST
	//base path for resource files (for drop-downs or other convenient files
	public static final String RESOURCE_FILES_PATH = "/WEB-INF/resources/files/";
	public static final String RESOURCE_MODEL_KEY_YEAR_LIST = "yearList";
	public static final String RESOURCE_MODEL_KEY_MONTH_LIST = "monthList";
	public static final String RESOURCE_MODEL_KEY_CURRENCY_CODE_LIST = "currencyCodeList";
	public static final String RESOURCE_MODEL_KEY_COUNTRY_CODE_LIST = "countryCodeList";
	public static final String RESOURCE_MODEL_KEY_SIGNATURE_LIST = "signatureList";
	public static final String RESOURCE_MODEL_KEY_AVD_LIST = "avdList";
	
	/*N/A at the moment
	public static final String RESOURCE_MODEL_KEY_LANGUAGE_LIST = "languageList";
	public static final String RESOURCE_MODEL_KEY_HOURS_LIST = "hoursList";
	public static final String RESOURCE_MODEL_KEY_MINUTES_LIST = "minutesList";
	public static final String RESOURCE_MODEL_KEY_UOM_LIST = "uomList";
	*/
	
	//domain objects for model-view passing values
	public static final String DOMAIN_MODEL = "model";
	public static final String DOMAIN_RECORD = "record";
	public static final String DOMAIN_CONTAINER = "container";
	public static final String DOMAIN_RECORD_TOPIC_GODSNO = "recordGodsnoDisp";
	
	public static final String DOMAIN_LIST = "list";
	public static final String SESSION_LIST = "sessionList";
	public static final String SESSION_SEARCH_FILTER = "searchFilter";
	
	
	//aspects in view (such as errors, logs, other
	public static final String ASPECT_ERROR_MESSAGE = "errorMessage";
	public static final String ASPECT_ERROR_META_INFO = "errorInfo";
		   
}
