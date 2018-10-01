package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.MerknfDao;

@Data
public class JsonContainerDaoMERKNF {
	private String user = "";
	private String errMsg = "";
	private Collection<MerknfDao> list = new ArrayList<MerknfDao>();
}
