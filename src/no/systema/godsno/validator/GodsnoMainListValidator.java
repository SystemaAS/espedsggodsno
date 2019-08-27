package no.systema.godsno.validator;

import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.systema.main.util.StringManager;
import no.systema.main.validator.DateValidator;
import no.systema.godsno.filter.SearchFilterGodsnoMainList;
import org.springframework.validation.ValidationUtils;

/**
 * 
 * @author oscardelatorre
 * @date July 2018
 * 
 *
 */
public class GodsnoMainListValidator implements Validator {
	private static final Logger logger = Logger.getLogger(GodsnoMainListValidator.class.getName());
	private DateValidator dateValidator = new DateValidator();
	
	//private EmailValidator emailValidator = new EmailValidator();
	private StringManager strMgr = new StringManager();
	/**
	 * 
	 */
	public boolean supports(Class clazz) {
		return GodsnoMainListValidator.class.isAssignableFrom(clazz); 
	}
	
	/**
	 * @param obj
	 * @param errors
	 * 
	 */
	public void validate(Object obj, Errors errors) { 
		//Check for Mandatory fields
		SearchFilterGodsnoMainList record = (SearchFilterGodsnoMainList)obj;
		
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gogren", "systema.godsno.edit.form.update.error.null.grensepassering");
		
		//Check rules
		if(record!=null){
			
			if(strMgr.isNull(record.getGogn()) && strMgr.isNull(record.getGotrnr()) && strMgr.isNull(record.getGomott()) && 
					strMgr.isNull(record.getGoturn()) && strMgr.isNull(record.getGobiln()) && strMgr.isNull(record.getFromDay()) &&
					(strMgr.isNull(record.getFromDayUserInput()) || "null".equals(record.getFromDayUserInput())) ){
				errors.rejectValue("gogn", "systema.godsno.edit.form.update.error.rule.at.least.one.filter.value"); 	
			}
			/*
			if( (strMgr.isNull(record.getFromDay()) || "null".equals(record.getFromDay())) ){
				errors.rejectValue("gogn", "systema.godsno.edit.form.update.error.rule.at.least.one.filter.value");
			}*/
			
			/*
			if("0".equals(record.getFromDay())){
				//OK since this is the redirect from DashboardController (after login)	
			}else{
				
				if( (strMgr.isNull(record.getFromDayUserInput()) || "null".equals(record.getFromDayUserInput())) ){
					
					errors.rejectValue("gogn", "systema.godsno.edit.form.update.error.rule.at.least.one.filter.value");

				}
			}*/
		}
	}	
	
}
