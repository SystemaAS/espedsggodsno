/**
 * 
 */
package no.systema.godsno.service;

import org.springframework.stereotype.Service;

import no.systema.godsno.mapper.JsonGodsnoMapper;
import no.systema.godsno.model.JsonGenericContainerDao;

/**
 * 
 * @author oscardelatorre
 * @date Jun 2018
 * 
 * 
 */
@Service
public class GodsnoService {

	/**
	 * 
	 */
	public JsonGenericContainerDao getContainer(String utfPayload) {
		JsonGenericContainerDao container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainer(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	

}
