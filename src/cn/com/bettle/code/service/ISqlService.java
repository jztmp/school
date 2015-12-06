package cn.com.bettle.code.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.service.IService;
import cn.com.bettle.code.utils.json.JsonSerializeUtil;


public interface ISqlService extends IService {

	public Object sqlMap(LinkedHashMap inp)throws Exception;
}
