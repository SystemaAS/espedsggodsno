package no.systema.godsno.z.maintenance.main.controller;

import java.util.*;
import javax.annotation.PostConstruct;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//application imports
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.main.util.AppConstants;
import no.systema.main.util.JsonDebugger;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;

import no.systema.jservices.common.dao.GodsjfDao;

//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.util.manager.CodeDropDownMgr;
import no.systema.godsno.z.maintenance.main.model.MaintenanceMainListObject;

/**
 * Godsregistrering-NO maintenance Bevill.koder Controller 
 * 
 * @author oscardelatorre
 * @date  Sep 2018
 * 
 */

@Controller
@SessionAttributes(AppConstants.SYSTEMA_WEB_USER_KEY)
@Scope("session")
public class GodsnoMaintenanceBevillkoderController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(2000);
	private static Logger logger = Logger.getLogger(GodsnoMaintenanceBevillkoderController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
		
	@PostConstruct
	public void initIt() throws Exception {
		if("DEBUG".equals(AppConstants.LOG4J_LOGGER_LEVEL)){
			logger.setLevel(Level.DEBUG);
		}
	}
		
	/**
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsnomaintenance_bevillkoder.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doInit(ModelMap model, HttpSession session, HttpServletRequest request){
		logger.info("Inside: doInit");
		
		ModelAndView successView = new ModelAndView("godsnomaintenance_bevillkoder");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			List list = this.populateMaintenanceMainList();
			model.addAttribute("list", list);
			appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO_MAINTENANCE_ONE);
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			
			successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
	    		
			return successView;
		    }
		}
	
	/**
	 * 
	 * @return
	 */
	private List<MaintenanceMainListObject> populateMaintenanceMainList(){
		List<MaintenanceMainListObject> listObject = new ArrayList<MaintenanceMainListObject>();
		
		MaintenanceMainListObject object = new  MaintenanceMainListObject();
		        
		object.setId("1");
		object.setSubject("Lage bevill.koder");
		//object.setCode("SADI_AVD");
		//object.setText("SKFTAAA / STANDI - Ref.til Generelle Avd");
		//object.setDbTable("STANDI");
		object.setStatus("G");
		object.setPgm("xxx");
		listObject.add(object);
		//
		object = new  MaintenanceMainListObject();
		object.setId("2");
		object.setSubject("Låse avd/bevill.koder");
		//object.setCode("SADI_AVD");
		//object.setText("SKFTAAA / STANDI - Ref.til Generelle Avd");
		//object.setDbTable("STANDI");
		object.setStatus("G");
		object.setPgm("yyy");
		listObject.add(object);
		
		return listObject;
	}
}

