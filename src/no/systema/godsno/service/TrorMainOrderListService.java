/**
 * 
 */
package no.systema.godsno.service;

import org.springframework.stereotype.Service;

import no.systema.godsno.mapper.JsonTrorOrderListMapper;
import no.systema.godsno.model.JsonTrorOrderListContainer;

/**
 * 
 * @author oscardelatorre
 * @date Dec 2018
 * 
 * 
 */
@Service
public class TrorMainOrderListService {

	/**
	 * 
	 */
	public JsonTrorOrderListContainer getMainListContainer(String utfPayload) {
		JsonTrorOrderListContainer container = null;
		try{
			JsonTrorOrderListMapper mapper = new JsonTrorOrderListMapper();
			container = mapper.getContainer(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}


}
