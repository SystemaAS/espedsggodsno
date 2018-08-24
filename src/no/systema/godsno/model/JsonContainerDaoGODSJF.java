package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.GodsjfDao;

@Data
public class JsonContainerDaoGODSJF {
	private String user = "";
	private String errMsg = "";
	private Collection<GodsjfDao> list = new ArrayList<GodsjfDao>();
}
