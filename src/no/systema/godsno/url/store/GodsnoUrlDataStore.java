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
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSGF_U.do?user=OSCAR&mode=U/A/D&gogn=1234567890123...etc
	static public String GODSNO_BASE_GODSGF_DML_UPDATE_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSGF_U.do";
	
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSFI_U.do?user=OSCAR&mode=U/A/D&gflbko=123&gflbs1...etc
	static public String GODSNO_BASE_GODSFI_DML_UPDATE_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSFI_U.do";
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSAF_U.do?user=OSCAR&mode=U/A/D&gflbko=123&gflavd...etc
	static public String GODSNO_BASE_GODSAF_DML_UPDATE_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSAF_U.do";
			
	//--------------------------------------------
	//[1] AUX LISTs for GODSREG.Nr functionality
	//--------------------------------------------
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSAF.do?user=OSCAR
	static public String GODSNO_BASE_GODSAF_LIST_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSAF.do";
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSGF.do?user=OSCAR&gggn1=2018010622390
	static public String GODSNO_BASE_GODSGF_LIST_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSGF.do";
	//http://gw.systema.no:8080/syjservicesgodsno/syjsSYGODSFI.do?user=OSCAR
	static public String GODSNO_BASE_GODSFI_LIST_URL = AppConstants.HTTP_ROOT_SERVLET_JSERVICES + "/syjservicesgodsno/syjsSYGODSFI.do";
		
	
}
