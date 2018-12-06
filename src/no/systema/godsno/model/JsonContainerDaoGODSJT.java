package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.GodsjtDao;

@Data
public class JsonContainerDaoGODSJT {
	private String user = "";
	private String errMsg = "";
	private Collection<GodsjtDao> list = new ArrayList<GodsjtDao>();
}
