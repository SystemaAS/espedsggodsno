package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.GodsafDao;

@Data
public class JsonContainerDaoGODSAF  {
	private String user = "";
	private String errMsg = "";
	private Collection<GodsafDao> list = new ArrayList<GodsafDao>();
}
