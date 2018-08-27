package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.GodsgfDao;

@Data
public class JsonContainerDaoGODSGF {
	private String user = "";
	private String errMsg = "";
	private Collection<GodsgfDao> list = new ArrayList<GodsgfDao>();
}
