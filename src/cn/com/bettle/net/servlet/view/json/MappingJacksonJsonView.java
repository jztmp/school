package cn.com.bettle.net.servlet.view.json;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;

public class MappingJacksonJsonView extends org.springframework.web.servlet.view.json.MappingJacksonJsonView {

	/**
	 * 尴尬的IE不支持application/json类型的数据，这里我们把它转成text/json
	 */
	public static final String DEFAULT_CONTENT_TYPE = "text/plain;charset=UTF-8";

	private String dateFormatStr = "yyyy-MM-dd HH:mm:ss";

	private ObjectMapper objectMapper = new ObjectMapper();

	public MappingJacksonJsonView() {
		setContentType(DEFAULT_CONTENT_TYPE);
		setExposePathVariables(false);
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
		objectMapper.setDateFormat(dateFormat);
		this.setObjectMapper(objectMapper);
	}
}
