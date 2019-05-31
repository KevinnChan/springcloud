package com.kevin.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kevin on 2019/5/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroovyZuulFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键ID
	private Integer id;
	//过滤器id
	private String filterId;
	//版本
	private Integer revision;
	//创建时间
	private Date createTime;
	//是否是活跃
	private Boolean isActive;
	//是否是灰度
	private Boolean isCanary;
	//filter代码
	private String filterCode;
	//filter类型
	private String filterType;
	//名称
	private String filterName;
	//禁用属性
	private String disablePropertyName;
	//顺序
	private String filterOrder;
	//应用名称
	private String applicationName;
}
