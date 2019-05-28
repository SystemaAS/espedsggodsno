package no.systema.godsno.controller;

import java.util.*;
import javax.annotation.PostConstruct;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//application imports
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainCundfContainer;
import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainCundfRecord;
import no.systema.main.service.CundfService;
import no.systema.z.main.maintenance.url.store.MaintenanceMainUrlDataStore;
import no.systema.main.util.AppConstants;
import no.systema.main.util.JsonDebugger;
import no.systema.main.util.io.PayloadContentFlusher;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;

//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.service.TrorMainOrderListService;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.model.JsonContainerDaoGODSJT;
import no.systema.godsno.model.JsonTrorOrderListContainer;
import no.systema.godsno.model.JsonTrorOrderListRecord;
import no.systema.godsno.util.manager.CodeDropDownMgr;
import no.systema.jservices.common.dao.GodsjtDao;
import no.systema.jservices.common.dao.services.CundfDaoService;

/**
 * Godsregistrering-NO Childwindow Controller 
 * 
 * @author oscardelatorre
 * @date 2018 Dec
 * 
 */

@Controller
public class GodsnoControllerChildWindow {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(GodsnoControllerChildWindow.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	private PayloadContentFlusher payloadContentFlusher = new PayloadContentFlusher();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private CodeDropDownMgr codeDropDownMgr;
	
	@Autowired
	private GodsnoService godsnoService;
	
	@Autowired
	private TrorMainOrderListService trorMainOrderListService;
	
	@Autowired
	CundfService cundfService;
	
	@PostConstruct
	public void initIt() throws Exception {
		if("DEBUG".equals(AppConstants.LOG4J_LOGGER_LEVEL)){
			logger.setLevel(Level.DEBUG);
		}
	}
		
	/**
	 * 
	 * @param recordToValidate
	 * @param bindingResult
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsno_childwindow_uppdragslist.do", params="action=doFind",  method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doFind(HttpSession session, HttpServletRequest request){
		Collection mainList = new ArrayList();
		logger.info("Inside: doFind");
		Map model = new HashMap();
		
		ModelAndView successView = new ModelAndView("godsno_childwindow_uppdragslist");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		
		String redirect = request.getParameter("rd");
		String hegn = request.getParameter("hegn");
		String gotrnr = request.getParameter("gotrnr");
		//String pos1_lst = request.getParameter("pos1_lst");
		model.put("hegn", hegn);
		model.put("gotrnr", gotrnr);
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			
			appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO);
			//get list
			mainList = this.getList(appUser, hegn, model);
			
			//--------------------------------------
    		//Final successView with domain objects
    		//--------------------------------------
    		model.put(GodsnoConstants.DOMAIN_LIST, mainList);
			successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
    	 	
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER end - timestamp");
    		return successView;
		}
	}
	
	
	
	
	/**
	 * 
	 * @param appUser
	 * @param godsno
	 * @param model
	 * @param godsreg
	 * @return
	 */
	private Collection<JsonTrorOrderListRecord> getList(SystemaWebUser appUser, String godsno, Map model){
		Collection<JsonTrorOrderListRecord> outputListOfOrders = new ArrayList();
		
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MAIN_ORDER_LIST_HEADF_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser() + "&hegn=" + godsno + "&godsreg=1");
		
		//user parameter dftdg (go esped-->8 (parameters).
		//if(strMgr.isNotNull(appUser.getDftdg())){
			//urlRequestParams.append("&dftdg=" + appUser.getDftdg());
		//}
		
		logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonTrorOrderListContainer orderListContainer = this.trorMainOrderListService.getMainListContainer(jsonPayload);
    		if(orderListContainer!=null && (orderListContainer.getDtoList()!=null && orderListContainer.getDtoList().size()>0) ){
    			String strPos = this.getGodsnrPosString(appUser, godsno);
    			//
    			String previousHekna = "";
    			String previousHeknaName = "";
    			for(JsonTrorOrderListRecord record: orderListContainer.getDtoList()){
    				if(strMgr.isNotNull(strPos) && strPos.contains(record.getHepos1())){
    					//exclude this record
    				}else{
    					//this is only to avoid an extra round-trip to the SQL-back-end
    					if(!previousHekna.equals(record.getHekna())){
    						this.getHeknaName(appUser, record);
    						previousHekna = record.getHekna();
    						previousHeknaName = record.getHeknaName();
    					}else{
    						record.setHeknaName(previousHeknaName);
    					}
    					outputListOfOrders.add(record);
    				}
    			}
    		}
    	}		

    	logger.info("Godsnr/Posisjoner list size:" + outputListOfOrders.size());
		return outputListOfOrders;
	}
	/**
	 * 
	 * @param appUser
	 * @param orderListrecord
	 */
	private void getHeknaName(SystemaWebUser appUser, JsonTrorOrderListRecord orderListrecord){
		String BASE_URL = MaintenanceMainUrlDataStore.MAINTENANCE_MAIN_BASE_SYCUNDFR_GET_LIST_URL;
		String firm = appUser.getCompanyCode();
		if(strMgr.isNull(firm)){
			firm = appUser.getFallbackCompanyCode();
		}
		String urlRequestParamsKeys = "user=" + appUser.getUser() + "&kundnr=" + orderListrecord.getHekna() + "&firma=" + firm;
		//this.getRequestUrlKeyParametersForSearchCustomer(appUser.getUser(), customerName, customerNr, firma, syrg);
		logger.info("URL: " + BASE_URL);
		logger.info("PARAMS: " + urlRequestParamsKeys);
		logger.info(Calendar.getInstance().getTime() +  " CGI-start timestamp");
		String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParamsKeys);
		//debugger
		logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
		logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		jsonPayload = jsonPayload.replaceFirst("Customerlist", "customerlist");
    		JsonMaintMainCundfContainer container = this.cundfService.getList(jsonPayload);
    		if(container!=null){
    			if (container.getList()!=null && container.getList().size()>0){
    				for(JsonMaintMainCundfRecord  record : container.getList()){
    					orderListrecord.setHeknaName(record.getKnavn());
    				}
    			}
    		}
    	}
		
	}
	
	/**
	 * 
	 * @param appUser
	 * @param godsno
	 * @return
	 */
	private String getGodsnrPosString(SystemaWebUser appUser, String godsno){
		StringBuffer sbPos = new StringBuffer();
		String RECORD_SEPARATOR = ";";
		
		Collection<GodsjtDao> outputList = new ArrayList<GodsjtDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJT_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		urlRequestParams.append("&gtgn=" + godsno);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	//logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSJT listContainer = this.godsnoService.getContainerGodsjt(jsonPayload);
    		outputList = listContainer.getList();
    		if(outputList!=null && !outputList.isEmpty()){
    			logger.info("TRUE - Godsnr-Pos1");
    			for(GodsjtDao rec : outputList){
    				sbPos.append(rec.getGtpos1() + RECORD_SEPARATOR);
    			}
    		}
    	}		
	    
		return sbPos.toString();
	}
	
}

