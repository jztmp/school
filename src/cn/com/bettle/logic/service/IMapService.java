package cn.com.bettle.logic.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.exception.DBException;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.service.IService;
import cn.com.bettle.code.utils.json.JsonSerializeUtil;


public interface IMapService extends IService {

	public Map getUser(HashMap params)throws Exception;
	
}
