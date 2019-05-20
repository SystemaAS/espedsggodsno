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
		
		/*
		if(strMgr.isNotNull(recordToValidate.getAvd())){ urlRequestParams.append("&heavd=" + recordToValidate.getAvd()); }
		if(strMgr.isNotNull(recordToValidate.getOrderNr())){ urlRequestParams.append("&heopd=" + recordToValidate.getOrderNr()); }
		if(strMgr.isNotNull(recordToValidate.getSign())){ urlRequestParams.append("&hesg=" + recordToValidate.getSign()); }
		if(strMgr.isNotNull(recordToValidate.getDate())){ urlRequestParams.append("&hedtop=" + recordToValidate.getDate()); }
		if(strMgr.isNotNull(recordToValidate.getFromDate())){ urlRequestParams.append("&todoFromDate=" + recordToValidate.getFromDate()); }
		if(strMgr.isNotNull(recordToValidate.getToDate())){ urlRequestParams.append("&todoToDate=" + recordToValidate.getToDate()); }
		//From and dates
		if(strMgr.isNotNull(recordToValidate.getSender())){ urlRequestParams.append("&henas=" + recordToValidate.getSender()); }
		if(strMgr.isNotNull(recordToValidate.getReceiver())){ urlRequestParams.append("&henak=" + recordToValidate.getReceiver()); }
		if(strMgr.isNotNull(recordToValidate.getFrom())){ urlRequestParams.append("&hesdf=" + recordToValidate.getFrom()); }
		//To and dates
		if(strMgr.isNotNull(recordToValidate.getTo())){ urlRequestParams.append("&hesdt=" + recordToValidate.getTo()); }
		//other
		if(strMgr.isNotNull(recordToValidate.getStatus())){ urlRequestParams.append("&hest=" + recordToValidate.getStatus()); }
		if(strMgr.isNotNull(recordToValidate.getTtype())){ urlRequestParams.append("&heur=" + recordToValidate.getTtype()); }
		if(strMgr.isNotNull(recordToValidate.getGodsNr())){ urlRequestParams.append("&hegn=" + recordToValidate.getGodsNr()); }
		*/
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
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
    				outputListOfOrders = orderListContainer.getDtoList();
    			}else{
    				for(JsonTrorOrderListRecord record: orderListContainer.getDtoList()){
	    				//remove invalid cases for this UCase
	    				if(pos1_lst.contains(record.getHepos1())){
	    					//already picked hence, do not include
	    				}else{
	    					outputListOfOrders.add(record);
	    				}
	    			}
    			}
	    		
    		}
    	}		

    	logger.info("List size:" + outputListOfOrders.size());
		return outputListOfOrders;
	}
	
	
	
	
}

