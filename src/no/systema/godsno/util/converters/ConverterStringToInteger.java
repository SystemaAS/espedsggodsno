package no.systema.godsno.util.converters;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import no.systema.main.util.StringManager;

@Component
public class ConverterStringToInteger implements Converter<String, Integer> {

	public Integer convert(String source) {
		Integer retval = 0;
		if(new StringManager().isNotNull(source)){
			retval = Integer.valueOf(source);
		}
		return retval;
	}
	
	
}
