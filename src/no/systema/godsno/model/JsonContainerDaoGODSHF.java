package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.GodshfDao;

@Data
public class JsonContainerDaoGODSHF {
	private String user = "";
	private String errMsg = "";
	private Collection<GodshfDao> list = new ArrayList<GodshfDao>();
}
