/**
 * 
 */
package no.systema.godsno.mapper;

//jackson library
import org.slf4j.*;
//application library
import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.model.JsonContainerDaoGODSHF;
import no.systema.godsno.model.JsonContainerDaoGODSFI;
import no.systema.godsno.model.JsonContainerDaoGODSGF;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.model.JsonContainerDaoGODSJT;
import no.systema.godsno.model.JsonContainerDaoMERKNF;
import no.systema.godsno.model.JsonContainerDaoLLMRF;
import no.systema.godsno.model.JsonContainerDaoTURER;
import no.systema.godsno.model.JsonContainerDaoPrintGogn;


import no.systema.main.mapper.jsonjackson.general.ObjectMapperAbstractGrandFather;


/**
 * @author oscardelatorre
 * @date Jun 22, 2015
 * 
 */
public class JsonGodsnoMapper extends ObjectMapperAbstractGrandFather {
	private static final Logger logger = LoggerFactory.getLogger(JsonGodsnoMapper.class.getName());
	/**
	 * 
	 * @param utfPayload
	 * @return
	 * @throws Exception
	 */
	public JsonContainerDaoGODSJF getContainerGodsjf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonContainerDaoGODSJF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSJF.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		return container;
	}
	
	public JsonContainerDaoGODSJT getContainerGodsjt(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonContainerDaoGODSJT container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSJT.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		return container;
	}

	public JsonContainerDaoGODSAF getContainerGodsaf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonContainerDaoGODSAF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSAF.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		return container;
	}
	public JsonContainerDaoGODSGF getContainerGodsgf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonContainerDaoGODSGF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSGF.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		return container;
	}
	
	public JsonContainerDaoGODSFI getContainerGodsfi(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		JsonContainerDaoGODSFI container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSFI.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		return container;
	}
	
	public JsonContainerDaoGODSHF getContainerGodshf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		//logger.info("PAYLOAD:" + utfPayload);
		JsonContainerDaoGODSHF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoGODSHF.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		
		return container;
	}
	public JsonContainerDaoMERKNF getContainerMerknf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		//logger.info("PAYLOAD:" + utfPayload);
		JsonContainerDaoMERKNF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoMERKNF.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		
		return container;
	}
	public JsonContainerDaoPrintGogn getContainerPrintGogn(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		//logger.info("PAYLOAD:" + utfPayload);
		JsonContainerDaoPrintGogn container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoPrintGogn.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		
		return container;
	}
	public JsonContainerDaoLLMRF getContainerLlmrf(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		//logger.info("PAYLOAD:" + utfPayload);
		JsonContainerDaoLLMRF container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoLLMRF.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		
		return container;
	}
	//Turer as light-weight select
	public JsonContainerDaoTURER getContainerTurer(String utfPayload) throws Exception{
		//At this point we now have an UTF-8 payload
		//logger.info("PAYLOAD:" + utfPayload);
		JsonContainerDaoTURER container = super.getObjectMapper().readValue(utfPayload.getBytes(), JsonContainerDaoTURER.class); 
		//logger.info("[JSON-String payload status=OK]  " + container.getUser());
		//logger.info(container.getList().size());
		
		return container;
	}
}
