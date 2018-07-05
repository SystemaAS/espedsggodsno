/**
 * 
 */
package no.systema.godsno.mapper;

//jackson library
import org.apache.log4j.Logger;

//application library
import no.systema.godsno.model.JsonGenericContainerDao;
import no.systema.jservices.common.dao.GodsjfDao;
import no.systema.main.mapper.jsonjackson.general.ObjectMapperAbstractGrandFather;


/**
 * @author oscardelatorre
 * @date Jun 22, 2015
 * 
 */
public class JsonGodsnoMapper extends ObjectMapperAbstractGrandFather {
	private static final Logger logger = Logger.getLogger(JsonGodsnoMapper.class.getName());
	/**
	 * 
	 * @param utfPayload
	 * @return
	 * @throws Exception
	 */
	public JsonGenericContainerDao getContainer(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonGenericContainerDao container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonGenericContainerDao.class); 
		logger.info("[JSON-String payload status=OK]  " + container.getUser());
		for (GodsjfDao record : container.getList()){
			//DEBUG
		}
		return container;
	}
	
}
