package no.systema.godsno.z.maintenance.main.controller;

import java.util.*;
import javax.annotation.PostConstruct;


 
import org.slf4j.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
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

/**
 * Godsregistrering-NO maintenance gate Controller 
 * 
 * @author oscardelatorre
 * @date 2018 Sep
 * 
 */

@Controller
@SessionAttributes(AppConstants.SYSTEMA_WEB_USER_KEY)
@Scope("session")
public class GodsnoMaintenanceGateController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(2000);
	private static Logger logger = LoggerFactory.getLogger(GodsnoMaintenanceGateController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
		
	@PostConstruct
	public void initIt() throws Exception {
		if("DEBUG".equals(AppConstants.LOG4J_LOGGER_LEVEL)){
			 
		}
	}
		
	/**
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsnomaintenancegate.do", method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doInit(HttpSession session, HttpServletRequest request){
		logger.info("Inside: doInit");
		Map model = new HashMap();
		
		ModelAndView successView = new ModelAndView("godsnomaintenancegate");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			
			appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO_MAINTENANCE);
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			
			//successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
	    		
			return successView;
		    }
		}
	
}

