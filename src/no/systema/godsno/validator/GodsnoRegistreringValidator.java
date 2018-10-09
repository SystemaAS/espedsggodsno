package no.systema.godsno.validator;

import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.systema.main.util.StringManager;
import no.systema.main.validator.DateValidator;
import no.systema.jservices.common.dao.GodsjfDao;
import org.springframework.validation.ValidationUtils;

/**
 * 
 * @author oscardelatorre
 * @date July 2018
 * 
 *
 */
public class GodsnoRegistreringValidator implements Validator {
	private static final Logger logger = Logger.getLogger(GodsnoRegistreringValidator.class.getName());
	private DateValidator dateValidator = new DateValidator();
	
	//private EmailValidator emailValidator = new EmailValidator();
	private StringManager strMgr = new StringManager();
	/**
	 * 
	 */
	public boolean supports(Class clazz) {
		return GodsnoRegistreringValidator.class.isAssignableFrom(clazz); 
	}
	
	/**
	 * @param obj
	 * @param errors
	 * 
	 */
	public void validate(Object obj, Errors errors) { 
		//Check for Mandatory fields
		GodsjfDao record = (GodsjfDao)obj;
		
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gogren", "systema.godsno.edit.form.update.error.null.grensepassering");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gobiln", "systema.godsno.edit.form.update.error.null.kjennetegn");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gomott", "systema.godsno.edit.form.update.error.null.varemottaker");
	
		//Check rules
		if(record!=null){
			//------
			//dates 
			//------
			if(strMgr.isNotNull(record.getGogrdt())){
				if(!dateValidator.validateDate(record.getGogrdt(), DateValidator.DATE_MASK_NO)){
					errors.rejectValue("gogrdt", "systema.godsno.edit.form.update.error.rule.date.gogrdt.invalid"); 	
				}
			}
			if(strMgr.isNotNull(record.getGolsdt())){
				if(!dateValidator.validateDate(String.valueOf(record.getGolsdt()), DateValidator.DATE_MASK_NO)){
					errors.rejectValue("golsdt", "systema.godsno.edit.form.update.error.rule.date.golsdt.invalid"); 	
				}
			}
			//------
			//times
			//------
			if(record.getGogrkl()!=null && record.getGogrkl()>0){
				if(!dateValidator.validateTime24Hours((String.valueOf(record.getGogrkl())) )){
					errors.rejectValue("gogrkl", "systema.godsno.edit.form.update.error.rule.time.gogrkl.invalid"); 	
				}
			}
			if(record.getGolskl()!=null && record.getGolskl()>0){
				if(!dateValidator.validateTime24Hours((String.valueOf(record.getGolskl())) )){
					errors.rejectValue("golskl", "systema.godsno.edit.form.update.error.rule.time.golskl.invalid"); 	
				}
			}
			
			
		}
	}	
	
}
