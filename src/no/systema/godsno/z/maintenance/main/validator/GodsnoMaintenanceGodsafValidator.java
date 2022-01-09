package no.systema.godsno.z.maintenance.main.validator;

import java.util.*;
import org.slf4j.*;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.systema.main.util.StringManager;
import no.systema.main.validator.DateValidator;
import no.systema.jservices.common.dao.GodsafDao;
import org.springframework.validation.ValidationUtils;

/**
 * 
 * @author oscardelatorre
 * @date Sep 2018
 * 
 *
 */
public class GodsnoMaintenanceGodsafValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(GodsnoMaintenanceGodsafValidator.class.getName());
	private DateValidator dateValidator = new DateValidator();
	
	
	//private EmailValidator emailValidator = new EmailValidator();
	private StringManager strMgr = new StringManager();
	/**
	 * 
	 */
	public boolean supports(Class clazz) {
		return GodsnoMaintenanceGodsafValidator.class.isAssignableFrom(clazz); 
	}
	
	/**
	 * @param obj
	 * @param errors
	 * 
	 */
	public void validate(Object obj, Errors errors) { 
		//Check for Mandatory fields
		GodsafDao record = (GodsafDao)obj;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gflavd", "systema.godsno.maintenance.godsaf.edit.form.update.error.null.gflavd");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gflbko", "systema.godsno.maintenance.godsaf.edit.form.update.error.null.gflbko");
		
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
