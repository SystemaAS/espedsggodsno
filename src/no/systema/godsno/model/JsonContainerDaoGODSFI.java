package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.GodsfiDao;

@Data
public class JsonContainerDaoGODSFI  {
	private String user = "";
	private String errMsg = "";
	private Collection<GodsfiDao> list = new ArrayList<GodsfiDao>();
}
