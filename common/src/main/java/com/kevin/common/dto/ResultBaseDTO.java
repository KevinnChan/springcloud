package com.kevin.common.dto;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Kevin on 2018/12/11.
 */
@Data
@ToString
public class ResultBaseDTO<T>{
	private Integer code;
	private String errorMassage;
	/**
	 * 请求是否成功
	 */
	private Boolean isSuccess;
	private T data;

	public static <T> ResultBaseDTO<T> success(T data){
		ResultBaseDTO<T> resultBaseDTO = new ResultBaseDTO<>();
		resultBaseDTO.setCode(200);
		resultBaseDTO.setIsSuccess(true);
		resultBaseDTO.setData(data);
		return resultBaseDTO;
	}

	public static <T> ResultBaseDTO<T> success(T data,Integer code){
		ResultBaseDTO<T> resultBaseDTO = new ResultBaseDTO<>();
		resultBaseDTO.setCode(code);
		resultBaseDTO.setIsSuccess(true);
		resultBaseDTO.setData(data);
		return resultBaseDTO;
	}

	public static <T> ResultBaseDTO<T> failure(String errorMassage){
		ResultBaseDTO<T> resultBaseDTO = new ResultBaseDTO<>();
		resultBaseDTO.setCode(500);
		resultBaseDTO.setIsSuccess(false);
		resultBaseDTO.setErrorMassage(errorMassage);
		return resultBaseDTO;
	}

	public static <T> ResultBaseDTO<T> failure(String errorMassage,Integer code){
		ResultBaseDTO<T> resultBaseDTO = new ResultBaseDTO<>();
		resultBaseDTO.setCode(code);
		resultBaseDTO.setIsSuccess(false);
		resultBaseDTO.setErrorMassage(errorMassage);
		return resultBaseDTO;
	}
}
