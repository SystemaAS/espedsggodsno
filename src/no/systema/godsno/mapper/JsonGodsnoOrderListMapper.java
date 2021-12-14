/**
 * 
 */
package no.systema.godsno.mapper;

//jackson library
import org.apache.logging.log4j.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper; 
//application library
import no.systema.godsno.model.JsonContainerOrderListContainer;
import no.systema.godsno.model.JsonContainerOrderListRecord;



/**
 * @author oscardelatorre
 * @date Dec 2018
 * 
 */
public class JsonGodsnoOrderListMapper {
	private static final Logger logger = LogManager.getLogger(JsonGodsnoOrderListMapper.class.getName());
	/**
	 * 
	 * @param utfPayload
	 * @return
	 * @throws Exception
	 */
	public JsonContainerOrderListContainer getContainer(String utfPayload) throws Exception{
		ObjectMapper mapper = new ObjectMapper();  
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		//At this point we now have an UTF-8 payload
		JsonContainerOrderListContainer container = mapper.readValue(utfPayload.getBytes(), JsonContainerOrderListContainer.class); 
		logger.info("[JSON-String payload status=OK]  " + container.getUser());
		for (JsonContainerOrderListRecord record : container.getDtoList()){
			//DEBUG
		}
		
		return container;
	}
	
}
