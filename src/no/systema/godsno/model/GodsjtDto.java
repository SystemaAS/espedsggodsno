package no.systema.godsno.model;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import lombok.Data;


@Data
public class GodsjtDto   {
	private String applicationUser;
	private String updateMerknad_flag;
	
	private String gtgn; 		//15 VARCHAR
	private String gttrnr; 		//20 VARCHAR
	private String gtpos1; 		//7 VARCHAR
	private Integer gtpos2 = 0; 		//4 SONET
	
	
}
