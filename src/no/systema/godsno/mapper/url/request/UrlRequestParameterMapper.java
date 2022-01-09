/**
 * 
 */
package no.systema.godsno.mapper.url.request;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;

import org.slf4j.*;
import no.systema.main.model.jsonjackson.general.JsonAbstractGrandFatherRecord;
import no.systema.godsno.util.GodsnoConstants;
/**
 * @author oscardelatorre
 * @param Jul 4, 2017
 * 
 */
public class UrlRequestParameterMapper {
	private static final Logger logger = LoggerFactory.getLogger(UrlRequestParameterMapper.class.getName());
	
	/**
	 * Builds the final url parameter list (to send with a GET or POST form method)
	 * @param object
	 * @return
	 * 
	 */
	public String getUrlParameterValidString(JsonAbstractGrandFatherRecord object){
		StringBuffer sb = new StringBuffer();
		
		try{
			for(Field field: object.getFields()){
				try{
					field.setAccessible(true);//we must do this in order to access private fields
					Object fieldType = field.getType();
					//System.out.println(fieldType);
					if(fieldType.equals(int.class)){
						int value = (int)field.get(object);
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
						
					}else if(fieldType.equals(Integer.class)){
						//logger.info("XXX:" + field.get(object));
						Integer value = (Integer)field.get(object);
						//logger.info(field.getName() + ":" + value);
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
						
					}else if(fieldType.equals(BigDecimal.class)){
						BigDecimal value = (BigDecimal)field.get(object); 
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
					
					}else if(fieldType.equals(Double.class)){
						Double value = (Double)field.get(object); 
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
					}else{
						String value = (String)field.get(object); 
						if(value==null){
							sb.append("");
						}else{
							//CRUCIAL! to encode the value in order to handle all special characters (%,&,",',()...) before JSON-call
							//& will be converted into "%26", %="%25", etc. 
							//Refer to URLEncode special characters for further info)
							value = URLEncoder.encode(value, "UTF-8");
							
							sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
							sb.append(value.trim());
						}
					}
				}catch(Exception e){
					logger.info(" [INFO]data type not yet supported...");
					//add more instances if you need...					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public String getUrlParameterValidString(Object object){
		StringBuffer sb = new StringBuffer();
		Field[] fields = null;
		if (object.getClass().getSuperclass().equals(Object.class)) {
			fields = object.getClass().getDeclaredFields();
		} else {
			fields = object.getClass().getSuperclass().getDeclaredFields();
		}
		
		try{
			for(Field field: fields){
				//logger.info(field.getName());
				try{
					field.setAccessible(true);//we must do this in order to access private fields
					Object fieldType = field.getType();
					//logger.info("TYPE:" + fieldType);
					if(fieldType.equals(int.class)){
						int value = (int)field.get(object);
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
						
					}else if(fieldType.equals(Integer.class)){
						//logger.info("XXX:" + field.get(object));
						Integer value = (Integer)field.get(object);
						//logger.info(field.getName() + ":" + value);
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
						
					}else if(fieldType.equals(BigDecimal.class)){
						BigDecimal value = (BigDecimal)field.get(object); 
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
					
					}else if(fieldType.equals(Double.class)){
						Double value = (Double)field.get(object); 
						sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
						sb.append(value);
					
					}else{
						String value = (String)field.get(object); 
						if(value==null){
							sb.append("");
						}else{
							//CRUCIAL! to encode the value in order to handle all special characters (%,&,",',()...) before JSON-call
							//& will be converted into "%26", %="%25", etc. 
							//Refer to URLEncode special characters for further info)
							value = URLEncoder.encode(value, "UTF-8");
							
							sb.append(GodsnoConstants.URL_CHAR_DELIMETER_FOR_PARAMS_WITH_HTML_REQUEST + field.getName() + "=");
							sb.append(value.trim());
						}
					}
				}catch(Exception e){
					
					logger.info(" [INFO]data type not yet supported... field:" + field.getName() + " error Message:" + e.getMessage());
					//add more instances if you need...					
				    continue;
				    	
					
				}
			}
		}catch(Exception e){
			logger.info("Error", e);
			e.printStackTrace();
		}
		//logger.info(sb.toString());
		return sb.toString();
	}
	
}
