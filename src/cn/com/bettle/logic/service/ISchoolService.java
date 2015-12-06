package cn.com.bettle.logic.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.com.bettle.code.data.BServiceData;
import cn.com.bettle.code.database.entity.PageInfo;
import cn.com.bettle.code.exception.DBException;
import cn.com.bettle.code.exception.ServiceException;
import cn.com.bettle.code.service.IService;
import cn.com.bettle.code.utils.json.JsonSerializeUtil;


public interface ISchoolService extends IService {

	public List getUser(HashMap params)throws RuntimeException;
	
	
	public int saveParents(LinkedHashMap params)throws RuntimeException;
	
	public int saveTeacher(LinkedHashMap params) throws RuntimeException;
	
	public int saveStudent(LinkedHashMap params) throws RuntimeException;
	
	public int saveUser(LinkedHashMap params)throws RuntimeException;
	
	public int saveSchool(LinkedHashMap params) throws RuntimeException;
	
	public int deleteUser(LinkedHashMap params)throws RuntimeException;
	
	public int saveDriver(LinkedHashMap params)throws RuntimeException;
	
	public int deleteDriver(LinkedHashMap params)throws RuntimeException;
	
	public int deleteBus(LinkedHashMap params)throws RuntimeException;
	
	public int deleteStudnets(LinkedHashMap params)throws RuntimeException;
	
	public int saveGPS(HashMap params) throws RuntimeException;
	
	public void saveGpsMemory(LinkedHashMap params) throws RuntimeException;
	
	public int saveBus(LinkedHashMap params) throws RuntimeException;
	
	public void initSchoolMemory() throws RuntimeException;
	
	public void sendMessage(HashMap params)throws ServiceException;
	
	public void sendMessageAllUser(LinkedHashMap params)throws ServiceException;
	
	public void clearSms(HashMap params)throws ServiceException;
	
	public Map<String,Object> getLocation(LinkedHashMap params) throws RuntimeException;

	public Map<String,String> getAllDistance(LinkedHashMap params)throws RuntimeException;
	
	public boolean stopService(LinkedHashMap params)throws RuntimeException;
	
	public boolean startService(LinkedHashMap params)throws RuntimeException;
	
	public boolean pauseService(LinkedHashMap params) throws RuntimeException;
	
	public void saveGPS2(String gpsString) throws RuntimeException; 
	
	public PageInfo getodb2HistryDate(LinkedHashMap params) throws RuntimeException;
	
	public void initBus(LinkedHashMap p);
	
	public  List<Map<String, Object>> getodb2Date(LinkedHashMap params) throws RuntimeException;
	
	public Map getodb2CurrentDate(LinkedHashMap params) throws RuntimeException;
	
	public Map getbusbyparentsID(LinkedHashMap params) throws RuntimeException;


	int saveDefaultCar(LinkedHashMap params) throws RuntimeException;


	int saveBusRelation(LinkedHashMap params) throws RuntimeException;


	int deleteBusRelation(LinkedHashMap params) throws RuntimeException;


	void sendMessageParents(LinkedHashMap params) throws ServiceException;
}
