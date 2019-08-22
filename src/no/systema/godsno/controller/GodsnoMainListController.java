package no.systema.godsno.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//application imports
import no.systema.main.service.UrlCgiProxyService;
import no.systema.main.validator.LoginValidator;
import no.systema.main.util.AppConstants;
import no.systema.main.util.DateTimeManager;
import no.systema.main.util.JsonDebugger;
import no.systema.main.util.io.PayloadContentFlusher;
import no.systema.main.model.SystemaWebUser;
import no.systema.main.util.StringManager;

import no.systema.jservices.common.dao.GodsjfDao;
import no.systema.jservices.common.dao.MerknfDao;
import no.systema.jservices.common.dao.GodsjtDao;

//GODSNO
import no.systema.godsno.service.GodsnoService;
import no.systema.godsno.service.GodsnoMainOrderListService;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import no.systema.godsno.url.store.GodsnoUrlDataStore;
import no.systema.godsno.util.GodsnoConstants;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.model.JsonContainerDaoGODSJT;
import no.systema.godsno.model.JsonContainerDaoMERKNF;
import no.systema.godsno.model.JsonContainerOrderListContainer;
import no.systema.godsno.model.JsonContainerOrderListRecord;
import no.systema.godsno.util.manager.CodeDropDownMgr;
import no.systema.godsno.validator.GodsnoMainListValidator;

import java.util.function.Predicate;
/**
 * Godsregistrering-NO main list Controller 
 * 
 * @author oscardelatorre
 * @date 2018 June
 * 
 */

@Controller
public class GodsnoMainListController {
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private static Logger logger = Logger.getLogger(GodsnoMainListController.class.getName());
	private ModelAndView loginView = new ModelAndView("redirect:logout.do");
	private LoginValidator loginValidator = new LoginValidator();
	private StringManager strMgr = new StringManager();
	private PayloadContentFlusher payloadContentFlusher = new PayloadContentFlusher();
	private DateTimeManager dateTimeMgr = new DateTimeManager();
	
	@Autowired
	private UrlCgiProxyService urlCgiProxyService;
	
	@Autowired
	private CodeDropDownMgr codeDropDownMgr;
	
	@Autowired
	private GodsnoService godsnoService;
	
	@Autowired
	private GodsnoMainOrderListService godsnoMainOrderListService;

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
	@RequestMapping(value="godsno_mainlist.do", params="action=doFind",  method={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView doFind(@ModelAttribute ("record") SearchFilterGodsnoMainList recordToValidate, BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		Collection mainList = new ArrayList();
		logger.info("Inside: doFind");
		Map model = new HashMap();
		
		ModelAndView successView = new ModelAndView("godsno_mainlist");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		String redirect = request.getParameter("rd");
		//check user (should be in session already)
		if(appUser==null){
			return loginView;
		
		}else{
			
			appUser.setActiveMenu(SystemaWebUser.ACTIVE_MENU_GODSREGNO);
			logger.info(Calendar.getInstance().getTime() + " CONTROLLER start - timestamp");
			//logger.info("####!!!" + recordToValidate.getFromDay());
			//-----------
			//Validation
			//-----------
			if(redirect!=null && !"".equals(redirect)){
				//no validation since we have the searchFilter in session already
			}else{
				GodsnoMainListValidator validator = new GodsnoMainListValidator();
				logger.info("Host via HttpServletRequest.getHeader('Host'): " + request.getHeader("Host"));
				validator.validate(recordToValidate, bindingResult);
			}
			
		    //check for ERRORS
			if(bindingResult.hasErrors()){
	    		logger.info("[ERROR Validation] search-filter does not validate)");
	    		//put domain objects and do go back to the successView from here
	    		//drop downs
			
	    		successView.addObject(GodsnoConstants.DOMAIN_MODEL, model);
	    		successView.addObject("searchFilter", recordToValidate);
				return successView;
	    		
		    }else{
				logger.info("TRNR:" + recordToValidate.getGotrnr());
	    		Map maxWarningMap = new HashMap<String,String>();
	    		SearchFilterGodsnoMainList searchFilter = (SearchFilterGodsnoMainList)session.getAttribute(GodsnoConstants.SESSION_SEARCH_FILTER);
    			if(redirect!=null && !"".equals(redirect)){
    				recordToValidate = searchFilter;
	    		
    			}else{
	    			this.populateDefaultValues(recordToValidate);
    				logger.info(recordToValidate.getFromDayUserInput());
	    		}
    			
    			//get list
    			mainList = this.getList(appUser, recordToValidate, model);
    			
    			//--------------------------------------
	    		//Final successView with domain objects
	    		//--------------------------------------
	    		//drop downs
    			this.setCodeDropDownMgr(appUser, model);
				model.put(GodsnoConstants.DOMAIN_LIST, mainList);
				
				successView.addObject(GodsnoConstants.DOMAIN_MODEL , model);
	    		
	    		//domain and search filter
	    		//Put list for upcomming view (PDF, Excel, etc)
	    		if(mainList!=null && (redirect==null || "".equals(redirect)) ){
	    			session.setAttribute(session.getId() + GodsnoConstants.SESSION_LIST, mainList);
	    			session.setAttribute(GodsnoConstants.SESSION_SEARCH_FILTER, recordToValidate);
	    		}
	    		if(redirect!=null && !"".equals(redirect)){
	    			//when this function is called with a redirect in another function
	    			successView.addObject("searchFilter", searchFilter);
	    		}else{
	    			//default
	    			successView.addObject("searchFilter", recordToValidate);
	    		}

	    		logger.info(Calendar.getInstance().getTime() + " CONTROLLER end - timestamp");
	    		return successView;
		    }
		}
	}
	
	/**
	 * 
	 * @param searchFilter
	 */
	private void populateDefaultValues(SearchFilterGodsnoMainList record){
		if(strMgr.isNotNull(record.getFromDay())){
			if("0".equals(record.getFromDay()) && strMgr.isNull(record.getFromDayUserInput()) ){
				LocalDate localDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
				record.setFromDayUserInput(localDate.format(formatter));
			}
		}
	}
	
	/**
	 * General method to render a file in a browser
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="godsno_renderFile.do", method={ RequestMethod.GET })
	public ModelAndView doRenderFile(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		logger.info("Inside doRenderFile...");
		SystemaWebUser appUser = this.loginValidator.getValidUser(session);
		
		if(appUser==null){
			return this.loginView;
			
		}else{
			
			String filePath = request.getParameter("fp");
			if(filePath!=null && !"".equals(filePath)){
				
                String absoluteFilePath = filePath;
                
                //must know the file type in order to put the correct content type on the Servlet response.
                String fileType = this.payloadContentFlusher.getFileType(filePath);
                if(AppConstants.DOCUMENTTYPE_PDF.equals(fileType)){
                		response.setContentType(AppConstants.HTML_CONTENTTYPE_PDF);
                }else if(AppConstants.DOCUMENTTYPE_TIFF.equals(fileType) || AppConstants.DOCUMENTTYPE_TIF.equals(fileType)){
            			response.setContentType(AppConstants.HTML_CONTENTTYPE_TIFF);
                }else if(AppConstants.DOCUMENTTYPE_JPEG.equals(fileType) || AppConstants.DOCUMENTTYPE_JPG.equals(fileType)){
                		response.setContentType(AppConstants.HTML_CONTENTTYPE_JPEG);
                }else if(AppConstants.DOCUMENTTYPE_TXT.equals(fileType)){
            			response.setContentType(AppConstants.HTML_CONTENTTYPE_TEXTHTML);
                }else if(AppConstants.DOCUMENTTYPE_DOC.equals(fileType)){
            			response.setContentType(AppConstants.HTML_CONTENTTYPE_WORD);
                }else if(AppConstants.DOCUMENTTYPE_XLS.equals(fileType)){
            			response.setContentType(AppConstants.HTML_CONTENTTYPE_EXCEL);
                }
                //--> with browser dialogbox: response.setHeader ("Content-disposition", "attachment; filename=\"edifactPayload.txt\"");
                response.setHeader ("Content-disposition", "filename=\"fileDocument." + fileType + "\"");
                
                logger.info("Start flushing file payload...");
                //send the file output to the ServletOutputStream
                try{
                		this.payloadContentFlusher.flushServletOutput(response, absoluteFilePath);
                		//payloadContentFlusher.flushServletOutput(response, "plain text test...".getBytes());
                	
                }catch (Exception e){
                		e.printStackTrace();
                }
            }
			//this to present the output in an independent window
            return(null);
			
		}
			
	}	
	
	
	
	/**
	 * 
	 * @param appUser
	 * @param recordToValidate
	 * @param fromDay
	 * @param model
	 * @return
	 */
	private Collection<GodsjfDao> getList(SystemaWebUser appUser, SearchFilterGodsnoMainList recordToValidate, Map model){
		Collection<GodsjfDao> outputList = new ArrayList();
		String defaultDaysBack = "0";
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MAIN_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		
		if(strMgr.isNotNull(recordToValidate.getGogn()) ){
			urlRequestParams.append("&gogn=" + recordToValidate.getGogn());
		}
		if(strMgr.isNotNull(recordToValidate.getGogn2()) ){
			urlRequestParams.append("&gogn2=" + recordToValidate.getGogn2());
		}
		
		//Special case with dates since there are some combinations
		if(strMgr.isNotNull(recordToValidate.getFromDayUserInput()) ){
			// Range = End date - Start date
	        Long range = this.dateTimeMgr.getRangeOfDaysBetweenDates(recordToValidate.getFromDayUserInput(), DateTimeManager.NO_FORMAT);
	        urlRequestParams.append("&dftdg=" + String.valueOf(range));
	        
	        //now check if there is a toDate in order to search between an interval of dates
	        if(strMgr.isNotNull(recordToValidate.getToDayUserInput()) ){
	        	// Range = End date - Start date
		        range = this.dateTimeMgr.getRangeOfDaysBetweenDates(recordToValidate.getToDayUserInput(), DateTimeManager.NO_FORMAT);
		        urlRequestParams.append("&dftdg2=" + String.valueOf(range));
	        }
	        
		}else{
			if(strMgr.isNotNull(recordToValidate.getFromDay()) ){
				if(!"null".equals(recordToValidate.getFromDay())){
				  urlRequestParams.append("&dftdg=" + recordToValidate.getFromDay());
				}
			}else{
				urlRequestParams.append("&dftdg=" + defaultDaysBack);
				recordToValidate.setFromDay(defaultDaysBack);
			}
		}
		//
		if(strMgr.isNotNull(recordToValidate.getGomott()) ){
			urlRequestParams.append("&gomott=" + recordToValidate.getGomott());
		}
		if(strMgr.isNotNull(recordToValidate.getGotrnr()) ){
			urlRequestParams.append("&gotrnr=" + recordToValidate.getGotrnr());
		}
		if(strMgr.isNotNull(recordToValidate.getGoturn()) ){
			urlRequestParams.append("&goturn=" + recordToValidate.getGoturn());
		}
		if(strMgr.isNotNull(recordToValidate.getGobiln()) ){
			urlRequestParams.append("&gobiln=" + recordToValidate.getGobiln());
		}
	
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSJF listContainer = this.godsnoService.getContainerGodsjf(jsonPayload);
    		outputList = listContainer.getList();
    		Map<String, String> mapGreenPosisjoner = new HashMap<String,String>();
    		Map<String, String> mapRedPosisjoner = new HashMap<String,String>();
    		//aux
    		List<GodsjfDao> auxRawListPerGodsnr = new ArrayList<GodsjfDao>();
    		Map<String, List<GodsjfDao>> auxMapPerGodsnr = new HashMap<String, List<GodsjfDao>>();
    		int mainListCounter = 0;
    		
    		//(1) Main list
    		String previousRecord = "";
    		for(GodsjfDao record : outputList){
    			mainListCounter ++;
    			this.adjustFieldsForFetch(record);
    			//limit ... just in case
    			if(outputList.size()<=250){
    				
    				this.getListOfExistingMerknf(appUser, record.getGogn(), record.getGotrnr(), model);
    				this.getListOfExistingPosisjoner(appUser, record.getGogn(), record.getGotrnr(), model);
    				//Check if there is already a posisjon in used and if so: mark it in a map (the map is at a godsnr-level)
    				String mapKey = record.getGogn() + record.getGotrnr() + "pos";
    				String tmpPosFlag = (String)model.get(mapKey);
    				if(strMgr.isNotNull(tmpPosFlag)){
    					mapGreenPosisjoner.put(record.getGogn() + record.getGotrnr(), record.getGogn() + record.getGotrnr());
    				}
    				
    				//this population of different lists per unique-godsnr is required in order to save a map-per-godsnr for future used in this same method
	    			if(mainListCounter==1){
	    				previousRecord = record.getGogn();
	    				auxRawListPerGodsnr.add(record);
    				}else{
    					if(previousRecord.equals(record.getGogn())){
    						auxRawListPerGodsnr.add(record);
    					}else{
    						//new list
    						previousRecord = record.getGogn();
    						//new list
    						auxRawListPerGodsnr = new ArrayList<GodsjfDao>();
    						auxRawListPerGodsnr.add(record);
    					}
    				}
	    			auxMapPerGodsnr.put(record.getGogn(), auxRawListPerGodsnr);
	    			
    			}
    		}
    		//DEBUG
    		// using for-each loop for iteration over Map.entrySet()
    		/*DEBUG
            for (Map.Entry<String,List<GodsjfDao>> entry : auxMapPerGodsnr.entrySet()){ 
                logger.info("########Key = " + entry.getKey()); //+ "_Value = " + entry.getValue()); 
                List<GodsjfDao> tmp = (List<GodsjfDao>)entry.getValue();
                for(GodsjfDao rec : tmp){
                	logger.info("-->GODSNR:" + rec.getGogn() + "  -->TRANSNR:" + rec.getGotrnr());
                }
            }*/
            //DEBUG
            /*for (Map.Entry<String,String> entry : mapGreenPosisjoner.entrySet()){ 
                logger.info("########Key mapGreenPosisjoner = " + entry.getKey()); //+ "_Value = " + entry.getValue()); 
  
            }*/
            
    		//(2) This loop is ONLY to catch a warning regarding posisjoner. These will be marked in the GUI as "warnings"
    		//Check if there are posisjoner (use the mapGreenPosisjoner-above as help) that MUST be used/chosen and have not been yet. If so: mark them
    		String previousGognRecord = "";
            for(GodsjfDao record : outputList){
    			if(!mapGreenPosisjoner.containsKey(record.getGogn() + record.getGotrnr())){
    				logger.info("NO GREEN !!!");
    				
    				//this check is done to avoid redundant check (SQL) since the godsnr has previously been checked... 
					//At this point we know now that no posisjon is present at a godsnr-level. Check if it should be at least one.
    				if(!previousGognRecord.equals(record.getGogn())){
    					
    					//Check if the GODSNR. has more than 1 occurrences
    					long nrOfGodsnrOccurrences = outputList.stream().filter(c -> c.getGogn().equals(record.getGogn())).count();
    					//logger.info("GODSNR:" + record.getGogn() + " Number of Matching Element: "+l);
    			        if(nrOfGodsnrOccurrences > 1){
		    				if(this.posExists(appUser, record.getGogn())){
		    					//DEBUG 
		    					logger.info("########--->Red marks on godsnr:" + record.getGogn());
		    					this.getListOfExistingPosisjonerPerGodsnr(appUser, record.getGogn(), model);
		    				}
    			        }
    				}
    				
    			}else{
    				logger.info("GREEN !!!");
    				if(this.posExists(appUser, record.getGogn())){
    					if(this.isValidForRedMark(record, mapGreenPosisjoner, auxMapPerGodsnr, mapRedPosisjoner)){
    						//DEBUG
    						for (Map.Entry<String,String> entry : mapRedPosisjoner.entrySet()){ 
    			                logger.info("########Key mapRedPosisjoner (valid for mark) = " + entry.getKey());  
    			                model.put(entry.getKey()  + "posMissing", record.getGogn() + record.getGotrnr());//will be used in the JSP
    			            }
    					}
    				}
    			}
    			previousGognRecord = record.getGogn();
    		}
    		
    	}		
	    
		return outputList;
	}
	
	/**
	 * 
	 * @param godsjfDao
	 * @param mapGreenPosisjoner
	 * @param auxMapPerGodsnr
	 * @return
	 */
	private boolean isValidForRedMark( GodsjfDao godsjfDao, Map<String, String> mapGreenPosisjoner, Map<String, List<GodsjfDao>> auxMapPerGodsnr, Map mapRedPosisjoner ){
		boolean retval = false;
		logger.info("Inside: isValidForRedMark");
		// using for-each loop for iteration over Map.entrySet() 
        outerLoop: for (Map.Entry<String,List<GodsjfDao>> entry : auxMapPerGodsnr.entrySet()){ 
            if(entry.getKey().equals(godsjfDao.getGogn())){
            	logger.info("########Key (godsnr) = " + entry.getKey());
                //at this point we are now in the specific list of godsnr-gotrnr
            	List<GodsjfDao> listSpecificGodsnr = (List<GodsjfDao>)entry.getValue();
            	int greenRecords = 0;
                for(GodsjfDao rec : listSpecificGodsnr){
                	//logger.info("##### CHASING RED MARK #####");
	            	//logger.info("GODSNR:" + rec.getGogn() + "  TRANSNR:" + rec.getGotrnr());
	            	String mapKey = rec.getGogn() + rec.getGotrnr();
            		if(mapGreenPosisjoner.containsKey(mapKey)){
            			//green marked records
            			greenRecords ++;
            			
            		}else{
            			
            			String key = rec.getGogn() + rec.getGotrnr();
            			logger.info("##### MATCH mapRedPosisjoner: " + key );
            			mapRedPosisjoner.put(key, rec.getGogn());//will be used in the JSP
            		}

                }
                if(listSpecificGodsnr!=null && listSpecificGodsnr.size()>0){
                	if(listSpecificGodsnr.size() - greenRecords > 1){
                		logger.info("Valid for red mark (list-greenRecords > 1)");
                		retval = true;
                	}
                }
                break outerLoop;
            }
        }
        
		return retval;
	}
	/**
	 * 
	 * @param recordToValidate
	 */
	private void adjustFieldsForFetch(GodsjfDao recordToValidate){
		recordToValidate.setGogrdt(this.convertToDate_NO(recordToValidate.getGogrdt()));
		recordToValidate.setGolsdt(this.convertToDate_NO(recordToValidate.getGolsdt()));
		recordToValidate.setGotrdt(this.convertToDate_NO(recordToValidate.getGotrdt()));
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String convertToDate_NO (String value){
		
		DateTimeManager dateMgr = new DateTimeManager();
		return dateMgr.getDateFormatted_NO(value, DateTimeManager.ISO_FORMAT);
	}
	
	/**
	 * 
	 * @param appUser
	 * @param gogn
	 * @param gotrnr
	 * @return
	 */
	private Collection<GodsjtDao> getListOfExistingPosisjoner(SystemaWebUser appUser, String gtgn, String gttrnr, Map model){
		Collection<GodsjtDao> outputList = new ArrayList<GodsjtDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJT_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		urlRequestParams.append("&gtgn=" + gtgn);
		urlRequestParams.append("&gttrnr=" + gttrnr);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	//logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	//logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoGODSJT listContainer = this.godsnoService.getContainerGodsjt(jsonPayload);
    		outputList = listContainer.getList();
    		if(outputList!=null && !outputList.isEmpty()){
    			String key = gtgn + gttrnr + "pos";
    			model.put(key, gtgn);//will be used in the JSP
    		}
    	}		
	    
		return outputList;
	}
	
	/**
	 * 
	 * @param appUser
	 * @param gtgn
	 * @param model
	 * @return
	 */
	private Collection<GodsjtDao> getListOfExistingPosisjonerPerGodsnr(SystemaWebUser appUser, String gtgn, Map model){
		Collection<GodsjtDao> outputList = new ArrayList<GodsjtDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_GODSJT_LIST_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		urlRequestParams.append("&gtgn=" + gtgn);
		
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
    			//OK
    		}else{
    			//Meaning that no Pos. have been chosen for this Godsnr. 
    			String key = gtgn + "posg";
    			model.put(key, gtgn);//will be used in the JSP
    		}
    	}		
	    
		return outputList;
	}
	/**
	 * 
	 * @param appUser
	 * @param gogn
	 * @param gotrnr
	 * @param model
	 * @return
	 */
	private Collection<MerknfDao> getListOfExistingMerknf(SystemaWebUser appUser, String gogn, String gotrnr, Map model){
		Collection<MerknfDao> outputList = new ArrayList<MerknfDao>();
		//---------------
    	//Get main list
		//---------------
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MERKNF_COUNT_URL;
		//add URL-parameters
		StringBuffer urlRequestParams = new StringBuffer();
		urlRequestParams.append("user=" + appUser.getUser());
		urlRequestParams.append("&gogn=" + gogn);
		urlRequestParams.append("&gotrnr=" + gotrnr);
		
		//session.setAttribute(TransportDispConstants.ACTIVE_URL_RPG_TRANSPORT_DISP, BASE_URL + "==>params: " + urlRequestParams.toString()); 
    	logger.info(Calendar.getInstance().getTime() + " CGI-start timestamp");
    	logger.info("URL: " + BASE_URL);
    	//logger.info("URL PARAMS: " + urlRequestParams);
    	String jsonPayload = this.urlCgiProxyService.getJsonContent(BASE_URL, urlRequestParams.toString());
    	//Debug --> 
    	//logger.debug(jsonDebugger.debugJsonPayloadWithLog4J(jsonPayload));
    	//logger.info(Calendar.getInstance().getTime() +  " CGI-end timestamp");
    	if(jsonPayload!=null){
    		JsonContainerDaoMERKNF listContainer = this.godsnoService.getContainerMerknf(jsonPayload);
    		outputList = listContainer.getList();
    		if(outputList!=null && !outputList.isEmpty()){
    			
    			String key = gogn + gotrnr;
    			model.put(key, gogn);//will be used in the JSP
    		}
    	}		
	    
		return outputList;
	}
	
	/**
	 * 
	 * @param appUser
	 * @param model
	 */
	private void setCodeDropDownMgr(SystemaWebUser appUser, Map model){
		//Sign / AVD
		model.put("signatureList", this.codeDropDownMgr.getSignatures(appUser.getUser()) );
		model.put("avdList", this.codeDropDownMgr.getAvdList(appUser.getUser()) );
		
	}
	/**
	 * 
	 * @param appUser
	 * @param godsno
	 * @param model
	 * @return
	 */
	private boolean posExists(SystemaWebUser appUser, String godsno){
		boolean retval = false;
		
		final String BASE_URL = GodsnoUrlDataStore.GODSNO_BASE_MAIN_ORDER_LIST_HEADF_URL;
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
    		JsonContainerOrderListContainer orderListContainer = this.godsnoMainOrderListService.getMainListContainer(jsonPayload);
    		if(orderListContainer!=null && (orderListContainer.getDtoList()!=null && orderListContainer.getDtoList().size()>0) ){
    			
    			String strPos = this.getGodsnrPosString(appUser, godsno);
    			if(strMgr.isNotNull(strPos)){
	    			for(JsonContainerOrderListRecord record: orderListContainer.getDtoList()){
	    				if(strMgr.isNotNull(strPos) && strPos.contains(record.getHepos1())){
	    					//exclude this record
	    				}else{
	    					retval = true;
	    					break;
	    				}
	    			}
    			}else{
    				//at this point we do have records in HEADF but none in GODSJT
    				retval = true;
    			}
    			
    		}
    	}		
    	
    	return retval;
	}
	/**
	 * 
	 * @param appUser
	 * @param godsno
	 * @return
	 */
	private String getGodsnrPosString(SystemaWebUser appUser, String godsno){
		String retval = null;
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
    	
	    if(sbPos!=null && sbPos.length()>0){
	    	retval = sbPos.toString();
	    }
		return retval;
		
    	
	}
	
	
}

