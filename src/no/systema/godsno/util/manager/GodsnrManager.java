package no.systema.godsno.util.manager;

import java.util.Collection;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import no.systema.jservices.common.dao.GodsafDao;
import no.systema.main.util.DateTimeManager;
import no.systema.main.util.JsonDebugger;
import no.systema.main.util.StringManager;

/**
 * Pure manager class. Not possible to use as a wired serviced due to bean related class attributes
 * 
 * @author oscardelatorre
 * @date 2018 Aug
 * 
 */
public class GodsnrManager {

	private static Logger logger = Logger.getLogger(GodsnrManager.class.getName());
	private static final JsonDebugger jsonDebugger = new JsonDebugger(3000);
	private StringManager strMgr = new StringManager();
	DateTimeManager dateMgr = new DateTimeManager();
	
	private String godsNr;
	public String getGodsNr(){ return this.godsNr;}
	public void setGodsNr(String value){ this.godsNr=value;}
	
	public void setGodsNrWithBevKode(String godsnrBevKode){
		StringBuffer bf = new StringBuffer();
		bf.append(dateMgr.getYear());
		bf.append(this.adjustBevKode(godsnrBevKode));
		bf.append(dateMgr.getDayNrOfYear());
		this.godsNr = bf.toString();
	}
	
	private String godsNrBevKode;
	public String getGodsNrBevKode(){ return this.godsNrBevKode;}
	public void setGodsNrBevKode(String value){ this.godsNrBevKode=value;}
	
	
	private String stdEnhetsKode;
	public String getStdEnhetsKode(){ return this.stdEnhetsKode;}
	public void setStdEnhetsKode(String value){ this.stdEnhetsKode=value;}
	
	
	
	/**
	 * 
	 * @param avd
	 * @param list
	 */
	public void getGodsnrBevKode_PatternA(String avdParam, Collection<GodsafDao> list){
		boolean match = false;
		String avd = this.adjustAvd(avdParam);
		
		//STEP 1 - perfect match
		for (GodsafDao record: list){
			logger.info(record.getGflavd() + " " + record.getGflbko());
			match = Pattern.matches("\\d{4}", record.getGflavd());
			if(match){ 
				if(this.compareAvdsPatternA(record.getGflavd(), avd, 0)){
					logger.info("Match STEP 1:" + record.getGflavd());
					this.godsNrBevKode = record.getGflbko();
					this.stdEnhetsKode = record.getGfenh();
					break;
				}
			}
		}
		//STEP 2 - X000 match
		if(!match){
			for (GodsafDao record: list){
				//logger.info(record.getGflavd() + " " + record.getGflbko());
				match = Pattern.matches("[X]\\d{3}", record.getGflavd());
				if(match){ 
					if(this.compareAvdsPatternA(record.getGflavd(), avd, 1)){
						logger.info("Match STEP 2:" + record.getGflavd());
						this.godsNrBevKode = record.getGflbko();
						this.stdEnhetsKode = record.getGfenh();
						break;
					}
				}
			}
		}
		//STEP 3 - XX00 match
		if(!match){
			for (GodsafDao record: list){
				//logger.info(record.getGflavd() + " " + record.getGflbko());
				match = Pattern.matches("[X][X]\\d{2}", record.getGflavd());
				if(match){ 
					if(this.compareAvdsPatternA(record.getGflavd(), avd, 2)){
						logger.info("Match STEP 3:" + record.getGflavd());
						this.godsNrBevKode = record.getGflbko();
						this.stdEnhetsKode = record.getGfenh();
						break;
					}
				}
			}
		}
		//STEP 4 - XXX0 match
		if(!match){
			for (GodsafDao record: list){
				//logger.info(record.getGflavd() + " " + record.getGflbko());
				match = Pattern.matches("[X][X][X]\\d{1}", record.getGflavd());
				if(match){ 
					if(this.compareAvdsPatternA(record.getGflavd(), avd, 3)){
						logger.info("Match STEP 4:" + record.getGflavd());
						this.godsNrBevKode = record.getGflbko();
						this.stdEnhetsKode = record.getGfenh();
						break;
					}
				}
			}
		}
		
		
		
	}
	/**
	 * 
	 * @param avd
	 * @param list
	 */
	public void getGodsnrBevKode_PatternB(String avdParam, Collection<GodsafDao> list){
		boolean match = false;
		
		String avd = this.adjustAvd(avdParam);
		
		//STEP 1 - perfect match
		for (GodsafDao record: list){
			logger.info(record.getGflavd() + " " + record.getGflbko());
			match = Pattern.matches("\\d{4}", record.getGflavd());
			if(match){ 
				if(this.compareAvdsPatternB(record.getGflavd(), avd, 0)){
					logger.info("Match STEP 1:" + record.getGflavd());
					this.godsNrBevKode = record.getGflbko();
					this.stdEnhetsKode = record.getGfenh();
					break;
				}
			}
		}
		//STEP 2 - 111X match
		if(!match){
			for (GodsafDao record: list){
				//logger.info(record.getGflavd() + " " + record.getGflbko());
				match = Pattern.matches("\\d{3}[X]", record.getGflavd());
				if(match){ 
					if(this.compareAvdsPatternB(record.getGflavd(), avd, 3)){
						logger.info("Match STEP 2:" + record.getGflavd());
						this.godsNrBevKode = record.getGflbko();
						this.stdEnhetsKode = record.getGfenh();
						break;
					}
				}
			}
		}
		//STEP 3 - 11XX match
		if(!match){
			for (GodsafDao record: list){
				//logger.info(record.getGflavd() + " " + record.getGflbko());
				match = Pattern.matches("\\d{2}[X][X]", record.getGflavd());
				if(match){ 
					if(this.compareAvdsPatternB(record.getGflavd(), avd, 2)){
						logger.info("Match STEP 3:" + record.getGflavd());
						this.godsNrBevKode = record.getGflbko();
						this.stdEnhetsKode = record.getGfenh();
						break;
					}
				}
			}
		}
		//STEP 4 - 1XXX match
		if(!match){
			for (GodsafDao record: list){
				//logger.info(record.getGflavd() + " " + record.getGflbko());
				match = Pattern.matches("\\d{1}[X][X][X]", record.getGflavd());
				if(match){ 
					if(this.compareAvdsPatternB(record.getGflavd(), avd, 1)){
						logger.info("Match STEP 4:" + record.getGflavd());
						this.godsNrBevKode = record.getGflbko();
						this.stdEnhetsKode = record.getGfenh();
						break;
					}
				}
			}
		}
		
	}
	
	/**
	 * PATTERN A : Wildcards at the beginning of the value e.g.: X001, XX01, XXX1
	 * @param record
	 * @param avd
	 * @param index
	 * @return
	 */
	private boolean compareAvdsPatternA (String record, String avd, int index){
		boolean retval = false;
		if(index == 0){ 
			int a = Integer.parseInt(record);
			int b = Integer.parseInt(avd);
			if(a == b){
				retval = true;
			}		
		}else{
			String tmp = record.replaceAll("X", "");
			int tmpInt = Integer.parseInt(tmp);
			//prepare by chopping "X"
			String avdRebuilt = avd;
			if(avd.length() == 4){
				avdRebuilt = avd.substring(index);
				
			}
			int avdRebuildInt = Integer.parseInt(avdRebuilt);
			//now compare by number comparison
			if(tmpInt == avdRebuildInt){
				retval = true;
			}
		}
		return retval;
	}
	/**
	 * PATTERN B : Wildcards at the end of the value e.g.: 1XXX, 10XX, 100X
	 * @param record
	 * @param avd
	 * @param index
	 * @return
	 */
	private boolean compareAvdsPatternB (String record, String avd, int index){
		boolean retval = false;
		if(index == 0){ 
			int a = Integer.parseInt(record);
			int b = Integer.parseInt(avd);
			if(a == b){
				retval = true;
			}		
		}else{
			String tmp = record.replaceAll("X", "");
			int tmpInt = Integer.parseInt(tmp);
			//prepare by chopping "X"
			String avdRebuilt = avd;
			if(avd.length() == 4){
				avdRebuilt = avd.substring(0,index);
			}
			int avdRebuildInt = Integer.parseInt(avdRebuilt);
			//now compare by number comparison
			if(tmpInt == avdRebuildInt){
				retval = true;
			}
		}
		return retval;
	}
	
	/**
	 * All avd-strings MUST be 4-chars long to ensure the PATTERN (regex) operations
	 * @param avd
	 * @return
	 */
	public String adjustAvd(String value){
		String adjustedValue = value;
		if(strMgr.isNotNull(value) && value.length()<4){
			if(value.length()==3){
				adjustedValue = "0" + value;
			
			}else if(value.length()==2){
				adjustedValue = "00" + value;
				
			}else if(value.length()==1){
				adjustedValue = "000" + value;
			}
		}
		return adjustedValue;		
	}
	
	/**
	 * All bev.kode-strings MUST be 5-chars long to ensure the validity of a Norwegian godsnr
	 * @param value
	 * @return
	 */
	public String adjustBevKode(String value){
		String adjustedValue = value;
		if(strMgr.isNotNull(value) && value.length()<5){
			if(value.length()==4){
				adjustedValue = "0" + value;
			
			}else if(value.length()==3){
				adjustedValue = "00" + value;
				
			}else if(value.length()==2){
				adjustedValue = "000" + value;
			}else if(value.length()==1){
				adjustedValue = "0000" + value;
			}
		}
		return adjustedValue;		
	}
	
	
}
