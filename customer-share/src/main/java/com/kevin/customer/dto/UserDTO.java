package com.kevin.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Kevin on 2018/12/11.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long id;
	private Long accountId;
	private String userName;
	private String nickName;
	private String gender;
	private String age;

}
