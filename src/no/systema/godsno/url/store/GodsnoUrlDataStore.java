/**
 * 
 */
package no.systema.godsno.url.store;
import no.systema.main.util.AppConstants;
/**
 * 
 * Static URLs
 * @author oscardelatorre
 * @date June 2018
 * 
 * 
 */
public final class GodsnoUrlDataStore {
	
	//----------------------
	//[1] FETCH MAIN LIST
	//----------------------
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSJF.do?user=OSCAR&dftdg=10
	static public String GODSNO_BASE_MAIN_LIST_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSJF.do";

	//-------------------------
	//[2] UPDATE record (U/A/D
	//-------------------------
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSJF_U.do?user=OSCAR&mode=U/A/D&gogn=1234567890123...etc
	static public String GODSNO_BASE_GODSJF_DML_UPDATE_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSJF_U.do";

	
}
