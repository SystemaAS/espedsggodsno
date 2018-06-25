/**
 * 
 */
package no.systema.godsno.service;

import org.springframework.stereotype.Service;

import no.systema.godsno.mapper.JsonGodsnoMainListMapper;
import no.systema.godsno.model.JsonGenericContainerDao;

/**
 * 
 * @author oscardelatorre
 * @date Jun 2018
 * 
 * 
 */
@Service
public class GodsnoMainListService {

	/**
	 * 
	 */
	public JsonGenericContainerDao getMainListContainer(String utfPayload) {
		JsonGenericContainerDao container = null;
		try{
			JsonGodsnoMainListMapper mapper = new JsonGodsnoMainListMapper();
			container = mapper.getContainer(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	

}
