package no.systema.godsno.model;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import lombok.Data;


@Data
public class MerknfDto   {
	private String applicationUserMerknf;
	private String updateMerknad_flag;
	
	private String gogn; 		//15 VARCHAR
	private String gotrnr; 		//20 VARCHAR
	private Integer gopos = 0; 		//4 SONET
	private Integer goantk = 0; 	//5 SONET
	private String govsla; 		//18 VARCHAR
	private String gopos2; 		//4 SONET
	private String gosted; 		//18 VARCHAR
	private String gomotm; 		//28 VARCHAR
	private String gomerk; 		//20 VARCHAR
	private String gomerb; 		//20 VARCHAR
	private String gomerc; 		//20 VARCHAR
	private String gomerd; 		//20 VARCHAR
	private String gomer1; 		//19 VARCHAR
	private String gomkod;		//2 VARCHAR
	
	
	
}
