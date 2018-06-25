/**
 * 
 */
package no.systema.godsno.filter;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.log4j.Logger;

import no.systema.main.util.DateTimeManager;

/**
 * 
 * @author oscardelatorre
 * @date   June 2018
 * 
 */
public class SearchFilterGodsnoMainList {
	private static final Logger logger = Logger.getLogger(SearchFilterGodsnoMainList.class.getName());
	
	private String avd = null;
	public void setAvd(String value) {  this.avd = value; }
	public String getAvd() { return this.avd;}
	
	private String sign = null;
	public void setSign(String value) {  this.sign = value; }
	public String getSign() { return this.sign;}
	
	private String gogn = null;
	public void setGogn(String value) {  this.gogn = value; }
	public String getGogn() { return this.gogn;}
	
	private String fromDate = null;
	public void setFromDate(String value) {  this.fromDate = value; }
	public String getFromDate() { return this.fromDate;}
	
	
	
}
