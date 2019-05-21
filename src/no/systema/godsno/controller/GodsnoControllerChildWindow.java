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
import no.systema.godsno.model.JsonTrorOrderListContainer;
import no.systema.godsno.model.JsonTrorOrderListRecord;
import no.systema.godsno.util.manager.CodeDropDownMgr;
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
		String pos1_lst = request.getParameter("pos1_lst");
		model.put("hegn", hegn);
		model.put("gotrnr", gotrnr);
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			
			appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO);
			//get list
			mainList = this.getList(appUser, hegn, model, pos1_lst);
			
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
	 * @return
	 */
	private Collection getList(SystemaWebUser appUser, String godsno, Map model, String pos1_lst){
		Collection outputListOfOrders = new ArrayList();
		
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MAIN_ORDER_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser() + "&hegn=" + godsno);
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
    		if(orderListContainer!=null){
    			if(strMgr.isNull(pos1_lst)){
    				//outputListOfOrders = orderListContainer.getDtoList();
    				for(JsonTrorOrderListRecord record: orderListContainer.getDtoList()){
    					this.getHeknaName(appUser, record);
    					outputListOfOrders.add(record);
    				}
    			}else{
    				for(JsonTrorOrderListRecord record: orderListContainer.getDtoList()){
	    				//remove invalid cases for this UCase
	    				if(pos1_lst.contains(record.getHepos1())){
	    					//already picked hence, do not include
	    				}else{
	    					this.getHeknaName(appUser, record);
	    					outputListOfOrders.add(record);
	    				}
	    			}
    			}
	    		
    		}
    	}		

    	logger.info("List size:" + outputListOfOrders.size());
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
	
	
}

