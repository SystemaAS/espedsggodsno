package no.systema.godsno.z.maintenance.main.validator;

import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.systema.main.util.StringManager;
import no.systema.main.validator.DateValidator;
import no.systema.jservices.common.dao.GodsfiDao;
import org.springframework.validation.ValidationUtils;

/**
 * 
 * @author oscardelatorre
 * @date Sep 2018
 * 
 *
 */
public class GodsnoMaintenanceGodsfiValidator implements Validator {
	private static final Logger logger = Logger.getLogger(GodsnoMaintenanceGodsfiValidator.class.getName());
	private DateValidator dateValidator = new DateValidator();
	
	//private EmailValidator emailValidator = new EmailValidator();
	private StringManager strMgr = new StringManager();
	/**
	 * 
	 */
	public boolean supports(Class clazz) {
		return GodsnoMaintenanceGodsfiValidator.class.isAssignableFrom(clazz); 
	}
	
	/**
	 * @param obj
	 * @param errors
	 * 
	 */
	public void validate(Object obj, Errors errors) { 
		//Check for Mandatory fields
		GodsfiDao record = (GodsfiDao)obj;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gflbko", "systema.godsno.maintenance.edit.form.update.error.null.gflbko");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gflbs1", "systema.godsno.maintenance.edit.form.update.error.null.gflbs1");
		
		//Check rules
		if(record!=null){
			
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
