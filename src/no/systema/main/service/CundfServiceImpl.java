/**
 * 
 */
package no.systema.main.service;

import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainCundfContainer;
import no.systema.z.main.maintenance.mapper.jsonjackson.dbtable.MaintMainCundfMapper;

/**
 * 
 * @author oscardelatorre
 * @date Aug 15, 2016
 * 
 * 
 */
public class CundfServiceImpl implements CundfService {
	/**
	 * 
	 */
	public JsonMaintMainCundfContainer getList(String utfPayload) {
		JsonMaintMainCundfContainer container = null;
		try{
			MaintMainCundfMapper mapper = new MaintMainCundfMapper();
			container = mapper.getContainer(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return container;
		
	}
	/**
	 * 
	 * @param utfPayload
	 * @return
	 */
	public JsonMaintMainCundfContainer doUpdate(String utfPayload) {
		JsonMaintMainCundfContainer container = null;
		try{
			MaintMainCundfMapper mapper = new MaintMainCundfMapper();
			container = mapper.getContainer(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return container;
		
	}

}
