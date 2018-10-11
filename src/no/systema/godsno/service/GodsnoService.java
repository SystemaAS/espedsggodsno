/**
 * 
 */
package no.systema.godsno.service;

import org.springframework.stereotype.Service;

import no.systema.godsno.mapper.JsonGodsnoMapper;
import no.systema.godsno.model.JsonContainerDaoGODSJF;
import no.systema.godsno.model.JsonContainerDaoGODSAF;
import no.systema.godsno.model.JsonContainerDaoGODSGF;
import no.systema.godsno.model.JsonContainerDaoGODSFI;
import no.systema.godsno.model.JsonContainerDaoGODSHF;
import no.systema.godsno.model.JsonContainerDaoMERKNF;
import no.systema.godsno.model.JsonContainerDaoLLMRF;
import no.systema.godsno.model.JsonContainerDaoPrintGogn;


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
	public JsonContainerDaoGODSJF getContainerGodsjf(String utfPayload) {
		JsonContainerDaoGODSJF container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerGodsjf(utfPayload);
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
	public JsonContainerDaoGODSAF getContainerGodsaf(String utfPayload) {
		JsonContainerDaoGODSAF container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerGodsaf(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	
	public JsonContainerDaoGODSGF getContainerGodsgf(String utfPayload) {
		JsonContainerDaoGODSGF container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerGodsgf(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	public JsonContainerDaoGODSFI getContainerGodsfi(String utfPayload) {
		JsonContainerDaoGODSFI container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerGodsfi(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	
	public JsonContainerDaoGODSHF getContainerGodshf(String utfPayload) {
		JsonContainerDaoGODSHF container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerGodshf(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	
	public JsonContainerDaoMERKNF getContainerMerknf(String utfPayload) {
		JsonContainerDaoMERKNF container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerMerknf(utfPayload);
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
	public JsonContainerDaoPrintGogn getContainerPrintGogn(String utfPayload) {
		JsonContainerDaoPrintGogn container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerPrintGogn(utfPayload);
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
	public JsonContainerDaoLLMRF getContainerLlmrf(String utfPayload) {
		JsonContainerDaoLLMRF container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerLlmrf(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	//Only to execute the payload somehow. Usually when you don't care about the return and just want to execute an AS400-service
	//I just borrow the mapper from LLMRF... could have been another one
	public JsonContainerDaoLLMRF getContainerPhantom(String utfPayload) {
		JsonContainerDaoLLMRF container = null;
		try{
			JsonGodsnoMapper mapper = new JsonGodsnoMapper();
			container = mapper.getContainerLlmrf(utfPayload);
		}catch(Exception e){
			e.printStackTrace();
		}
		return container;
	}
	

}
