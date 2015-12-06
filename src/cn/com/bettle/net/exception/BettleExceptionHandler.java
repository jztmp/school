package cn.com.bettle.net.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class BettleExceptionHandler implements HandlerExceptionResolver {

	protected final Logger log = LoggerFactory.getLogger(BettleExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {

		log.error(ex.getMessage(), ex);

		ModelAndView modelAndView = null;
		// 用于展示异常详情
		BettleExceptionBean exception = new BettleExceptionBean(ex, log.isDebugEnabled());
		modelAndView = new ModelAndView("error");
		if (ex instanceof MaxUploadSizeExceededException) {
			// 上传文件时超出上传文件限制
			MaxUploadSizeExceededException musee = (MaxUploadSizeExceededException) ex;
			modelAndView = new ModelAndView("error_fileupload");
			exception.setMessage("您的文件太大了，允许的最大文件：" + musee.getMaxUploadSize());
		} else if (ex instanceof DataAccessResourceFailureException) {
			exception.setMessage("连接数据库异常");
		}
		modelAndView.addObject("exception", exception);

		// TODO 这里将返回状态修改为500
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		// 根据不同错误转向不同页面
		return modelAndView;
	}

}
