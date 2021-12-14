package no.systema.godsno.z.maintenance.main.controller.ajax;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
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

import no.systema.godsno.mapper.url.request.UrlRequestParameterMapper;
import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.model.JsonContainerDaoMERKNF;
import no.systema.godsno.model.JsonContainerDaoGODSJT;
import no.systema.godsno.model.MerknfDto;
import no.systema.godsno.model.GodsjtDto;

import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.jservices.common.dao.GodsafDao;
import no.systema.jservices.common.dao.GodsjfDao;
import no.systema.jservices.common.dao.GodsjtDao;
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
	private static final Logger logger = LogManager.getLogger(GodsnoMerknfAjaxRestController.class.getName());
	private StringManager strMgr = new StringManager();
	private UrlRequestParameterMapper urlRequestParameterMapper = new UrlRequestParameterMapper();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private GodsnoService godsnoService;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(path="/updateSpecificRecord_merknf.do", method = RequestMethod.POST)
	public Collection updateMerknf(@ModelAttribute MerknfDto dto, BindingResult bindingResult ){
		logger.info("Inside: updateSpecificRecord_merknf");
		String mode ="A";
		if(strMgr.isNotNull(dto.getUpdateMerknad_flag())){
			mode = "U";
		}
		
		return this.updateRecordMerknf( dto, mode);
				
		  
	}
	/**
	 * 
	 * @param applicationUser
	 * @param gogn
	 * @param gotrnr
	 * @param gopos
	 * @return
	 */
	@RequestMapping(path="/deleteSpecificRecord_merknf.do", method = RequestMethod.GET)
	public Collection deleteMerknf(@RequestParam String applicationUser, @RequestParam String gogn, @RequestParam String gotrnr,
									@RequestParam String gopos){
		logger.info("Inside: deleteMerknf");
		MerknfDto dto = new MerknfDto();
		dto.setGogn(gogn);
		dto.setApplicationUser(applicationUser);
		dto.setGotrnr(gotrnr);
		dto.setGopos(Integer.valueOf(gopos));
		String mode ="D";
		//
		return this.updateRecordMerknf( dto, mode);
				
		  
	}
	/**
	 * 
	 * @param applicationUser
	 * @param dao
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(path="/getSpecificRecord_merknf.do", method = RequestMethod.GET)
	public Collection getRecord(@RequestParam String applicationUser, @RequestParam String gogn, @RequestParam String gotrnr,
									@RequestParam String gopos){
		logger.info("Inside: getSpecificRecord_merknf");
		logger.info(gogn + " " + gotrnr + " " + gopos);
		Collection<MerknfDao> outputList = new ArrayList<MerknfDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MERKNF_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + applicationUser);
		urlRequestParams.append("&gogn=" + gogn);
		urlRequestParams.append("&gotrnr=" + gotrnr);
		urlRequestParams.append("&gopos=" + gopos);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoMERKNF listContainer = this.godsnoService.getContainerMerknf(jsonPayload);
    		outputList = listContainer.getList();	
    	}		
	    
		return outputList;
		
		  
	}
	
	@RequestMapping(path="/deleteSpecificRecord_godsjt.do", method = RequestMethod.GET)
	public Collection deleteGodsjt(@RequestParam String applicationUser, @RequestParam String gtgn, @RequestParam String gttrnr,
									@RequestParam String gtpos1, @RequestParam String gtpos2 ){
		logger.info("Inside: deleteMerknf");
		GodsjtDto dto = new GodsjtDto();
		dto.setApplicationUser(applicationUser);
		dto.setGtgn(gtgn);
		dto.setGttrnr(gttrnr);
		dto.setGtpos1(gtpos1);
		dto.setGtpos2(Integer.valueOf(gtpos2));
		String mode ="D";
		//
		return this.updateRecordGodsjt( dto, mode);
				
		  
	}
	
	/**
	 * 
	 * @param dto
	 * @param mode
	 * @return
	 */
	private Collection<MerknfDao> updateRecordMerknf(MerknfDto dto, String mode){
		Collection<MerknfDao> outputList = new ArrayList<MerknfDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MERKNF_DML_UPDATE_URL;
		//add URL-parameters
		StringBuffer urlRequestParamsKeys = new StringBuffer();
		urlRequestParamsKeys.append("user=" + dto.getApplicationUser());
		urlRequestParamsKeys.append("&mode=" + mode);
		//adjust this particular field. There are reasons on why it is not Integer...
		if(strMgr.isNull(dto.getGopos2())){ dto.setGopos2("0"); }
		String urlRequestParams = this.urlRequestParameterMapper.getUrlParameterValidString((dto));
		//add params
		urlRequestParams = urlRequestParamsKeys + urlRequestParams;
		
		logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoMERKNF listContainer = this.godsnoService.getContainerMerknf(jsonPayload);
    		if(strMgr.isNull(listContainer.getErrMsg())){
    			MerknfDao tmpDao = new MerknfDao();
    			tmpDao.setGogn("OK");
    			outputList.add(tmpDao);
    		}
    	}		
	    
		return outputList;
	}
	
	/**
	 * 
	 * @param dto
	 * @param mode
	 * @return
	 */
	private Collection<GodsjtDao> updateRecordGodsjt(GodsjtDto dto, String mode){
		Collection<GodsjtDao> outputList = new ArrayList<GodsjtDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJT_DML_UPDATE_URL;
		//add URL-parameters
		StringBuffer urlRequestParamsKeys = new StringBuffer();
		urlRequestParamsKeys.append("user=" + dto.getApplicationUser());
		urlRequestParamsKeys.append("&mode=" + mode);
		//adjust this particular field. There are reasons on why it is not Integer...
		if(strMgr.isNull(dto.getGtpos1())){ dto.setGtpos1("0"); }
		String urlRequestParams = this.urlRequestParameterMapper.getUrlParameterValidString((dto));
		//add params
		urlRequestParams = urlRequestParamsKeys + urlRequestParams;
		
		logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.info(jsonPayload);
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSJT listContainer = this.godsnoService.getContainerGodsjt(jsonPayload);
    		if(strMgr.isNull(listContainer.getErrMsg())){
    			GodsjtDao tmpDao = new GodsjtDao();
    			tmpDao.setGtgn("OK");
    			outputList.add(tmpDao);
    		}
    	}		
	    
		return outputList;
	}
	
		
	
}
