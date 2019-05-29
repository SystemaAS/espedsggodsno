/**
 * 
 */
package no.systema.godsno.service;

import org.springframework.stereotype.Service;

import no.systema.godsno.mapper.JsonGodsnoOrderListMapper;
import no.systema.godsno.model.JsonContainerOrderListContainer;

/**
 * 
 * @author oscardelatorre
 * @date Dec 2018
 * 
 * 
 */
@Service
public class GodsnoMainOrderListService {

	/**
	 * 
	 */
	public JsonContainerOrderListContainer getMainListContainer(String utfPayload) {
		JsonContainerOrderListContainer container = null;
		try{
			JsonGodsnoOrderListMapper mapper = new JsonGodsnoOrderListMapper();
			container = mapper.getContainer(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}


}
