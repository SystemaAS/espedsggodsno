/**
 * 
 */
package no.systema.godsno.z.maintenance.main.model;
import java.util.*;
import lombok.Data;
//import no.systema.godsno.model.JsonContainerDaoGODSAF;
//import no.systema.jservices.common.dao.GodsafDao;
/**
 * @author oscardelatorre
 * @date Sep , 2018
 * 
 */
@Data
public class MaintenanceMainListObject  {
	
	private String id = null; 
	private Integer idInt = 0; 
	private String code = null; 
	private String subject = null; 
	private String text = null; 
	private String status = null; 
	private String description = null; 
	private String dbTable = null; 
	private String pgm = null; 
	
	
}
