package no.systema.godsno.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import no.systema.main.model.jsonjackson.general.JsonAbstractGrandFatherRecord;

@Data
public class JsonContainerOrderListRecord extends JsonAbstractGrandFatherRecord {

	/* Avdeling */
	private int heavd; 
	/* Ordernr */
	private int heopd;
	/* Signatur */
	private String hesg;
	/*  Oppdragsdato  */
	private String hedtop;
	/* Avsender */
	private String henas;
	/* Mottaker */
	private String henak;
	/* Antall */
	private int hent;
	/* Vekt */
	private int hevkt;
	/* M3 */
	private BigDecimal hem3;
	/* Landkode selger */
	private String helks;
	/* Postnr selger */
	private String hepns;
	/* Landkode kjøper */
	private String helkk;
	/* Postnr kjøper */
	private String hepnk;
	
	private String heur;
	private String hepro;
	private String hegn;
	private String hest;
	private String hepk3;
	private String hepk4;
	private String hepos1;
	private String hepos2;
	private String hekna;
	private String heknaName;
	
	
	/**
	 * Used for java reflection in other classes
	 * @return
	 * @throws Exception
	 */
	
	public List<Field> getFields() throws Exception{
		Class cl = Class.forName(this.getClass().getCanonicalName());
		Field[] fields = cl.getDeclaredFields();
		List<Field> list = Arrays.asList(fields);
		
		return list;
	}

}
