package cn.com.bettle.code.appcontext.context;

import cn.com.bettle.code.exception.ConfigParserException;


public interface Parser {

	public void parser(String namespace,String filePath)throws ConfigParserException;
}
