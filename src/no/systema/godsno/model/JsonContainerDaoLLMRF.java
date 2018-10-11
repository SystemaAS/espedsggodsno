package no.systema.godsno.model;
import lombok.Data;
import java.util.*;
import no.systema.jservices.common.dao.LlmrfDao;

@Data
public class JsonContainerDaoLLMRF {
	private String user = "";
	private String errMsg = "";
	private Collection<LlmrfDao> list = new ArrayList<LlmrfDao>();
}
