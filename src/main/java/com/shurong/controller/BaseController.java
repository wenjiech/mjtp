package com.shurong.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shurong.exception.BusinessException;

public class BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 返回成功数据
	 * @param obj
	 * @return
	 */
	protected Map<String, Object> buildSuccessResult(Object obj)
	{
		Map<String, Object> result = new HashMap<>();
		result.put("result", "ok");
		result.put("data", obj);
		return result;
	}
	
	/**
	 * 返回失败数据
	 * @param obj
	 * @return
	 */
	protected Map<String, Object> buildFailResult(Object obj)
	{
		Map<String, Object> result = new HashMap<>();
		result.put("result", "ko");
		result.put("data", obj);
		return result;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> serverError(Exception e) {

		logger.error("Server Internal Error", e);
		Map<String, Object> result = new HashMap<>();
		result.put("result", "ko");
		result.put("errCode", "9999");
		result.put("errMessage", e.getMessage());
		return result;
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> couponError(BusinessException ex) {
		
		Map<String, Object> result = new HashMap<>();
		result.put("result", "ko");
		result.put("errCode", ex.getErrCode());
		result.put("errMessage", ex.getMessage());
		return result;
	}
}
