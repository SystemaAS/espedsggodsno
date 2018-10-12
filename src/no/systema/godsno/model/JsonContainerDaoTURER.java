package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.TurerDao;

@Data
public class JsonContainerDaoTURER {
	private String user = "";
	private String errMsg = "";
	private Collection<TurerDao> list = new ArrayList<TurerDao>();
}
