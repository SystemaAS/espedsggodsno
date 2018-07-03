package no.systema.godsno.validator;

import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.systema.main.util.StringManager;
import no.systema.main.validator.DateValidator;
import no.systema.jservices.common.dao.GodsjfDao;


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
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hereff", "systema.ebooking.orders.form.update.error.null.from.hereff");
		
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
			
			/*
			//Godsnr (the number can not have empty fields in the precedent field. If field 2 is filled up then field 2 MUST be there ...
			if(strMgr.isNull(record.getOwnHegn1()) && (strMgr.isNotNull(record.getOwnHegn2()) || strMgr.isNotNull(record.getOwnHegn3()) ) ){
				errors.rejectValue("hegn", "systema.tror.orders.form.update.error.rule.godsnr.invalid");
			}else{
				if( (strMgr.isNotNull(record.getOwnHegn1()) && strMgr.isNull(record.getOwnHegn2()) ) && strMgr.isNotNull(record.getOwnHegn3()) ){	
					errors.rejectValue("hegn", "systema.tror.orders.form.update.error.rule.godsnr.invalid");
				}
			}
			*/
		}
	}	
	
}
