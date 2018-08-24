/**
 * 
 */
package no.systema.godsno.mapper;

//jackson library
import org.apache.log4j.Logger;

import no.systema.godsno.model.JsonContainerDaoGODSAF;
//application library
import no.systema.godsno.model.JsonContainerDaoGODSJF;
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
	public JsonContainerDaoGODSJF getContainerGodsjf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonContainerDaoGODSJF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSJF.class); 
		logger.info("[JSON-String payload status=OK]  " + container.getUser());
		logger.info(container.getList().size());
		return container;
	}
	
	public JsonContainerDaoGODSAF getContainerGodsaf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonContainerDaoGODSAF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSAF.class); 
		logger.info("[JSON-String payload status=OK]  " + container.getUser());
		logger.info(container.getList().size());
		return container;
	}
	
}
