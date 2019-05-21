/**
 * 
 */
package no.systema.main.service;

import no.systema.z.main.maintenance.model.jsonjackson.dbtable.JsonMaintMainCundfContainer;

/**
 * 
 * @author oscardelatorre
 * @date Aug 15, 2016
 * 
 *
 */
public interface CundfService {
	public JsonMaintMainCundfContainer getList(String utfPayload);
	public JsonMaintMainCundfContainer doUpdate(String utfPayload);
	
}
