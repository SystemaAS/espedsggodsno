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
	
	private String gogn2 = null;
	public void setGogn2(String value) {  this.gogn2 = value; }
	public String getGogn2() { return this.gogn2;}
	
	private String gomott = null;
	public void setGomott(String value) {  this.gomott = value; }
	public String getGomott() { return this.gomott;}
	
	private String gotrnr = null;
	public void setGotrnr(String value) {  this.gotrnr = value; }
	public String getGotrnr() { return this.gotrnr;}
	
	private String goturn = null;
	public void setGoturn(String value) {  this.goturn = value; }
	public String getGoturn() { return this.goturn;}
	
	private String gobiln = null;
	public void setGobiln(String value) {  this.gobiln = value; }
	public String getGobiln() { return this.gobiln;}
	
	private String fromDay = null;
	public void setFromDay(String value) {  this.fromDay = value; }
	public String getFromDay() { return this.fromDay;}
	
	private String fromDayUserInput = null;
	public void setFromDayUserInput(String value) {  this.fromDayUserInput = value; }
	public String getFromDayUserInput() { return this.fromDayUserInput;}
	
	private String toDayUserInput = null;
	public void setToDayUserInput(String value) {  this.toDayUserInput = value; }
	public String getToDayUserInput() { return this.toDayUserInput;}
	
}
