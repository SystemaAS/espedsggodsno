package no.systema.godsno.z.maintenance.main.controller.ajax;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.jservices.common.dao.GodsjfDao;
import no.systema.jservices.common.dao.MerknfDao;
import no.systema.jservices.common.dao.services.BridfDaoService;
import no.systema.jservices.common.dao.services.GodsafDaoService;
import no.systema.jservices.common.util.StringUtils;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.util.StringManager;


/**
 * 
 * @author oscardelatorre
 * @date Oct 2018
 */
@RestController
public class GodsnoMerknfAjaxRestController {
	private static final Logger logger = Logger.getLogger(GodsnoMerknfAjaxRestController.class.getName());
	private StringManager strMgr = new StringManager();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private GodsnoService godsnoService;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(path="/updateMerknad.do", method = RequestMethod.POST)
	public Collection updateMerknf(@ModelAttribute MerknfDao dao, BindingResult bindingResult ){
		logger.info("Inside: updateMerknf");
		logger.info(dao.getGomkod());
		
		List result = new ArrayList(); 
		result.add(dao);
		return result;
				
		  
	}
	
	
}
