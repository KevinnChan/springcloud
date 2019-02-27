package com.kevin.login.config;

import com.kevin.customer.dto.AccountDTO;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import net.minidev.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2018/12/13.
 */
public class JWTConfig {
	/**
	 * token秘钥，请勿泄露，请勿随便修改
	 */
	public static final byte[] SECRET = "jwtSecretjwtSecretjwtSecretjwtSecret".getBytes();

	/**
	 * token 过期时间: 10天
	 */
	public static final int calendarField = Calendar.DATE;
	public static final int calendarInterval = 10;

	// header 类型以及签名所用的算法
	public static final JWSHeader header = new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null);

	/**
	 * z
	 * JWT生成Token.<br/>
	 * <p>
	 * JWT构成: header, payload, signature
	 */
	public static String createToken(AccountDTO accountDTO) throws Exception {
		// expire time
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(calendarField, calendarInterval);
		Long expiresDate = nowTime.getTime().getTime();
		String token = null;

		/**
		 iat(issued at): 在什么时候签发的
		 ext(expires): 什么时候过期，这里是一个Unix时间戳
		 */
		Map<String, Object> payloadMap = new HashMap<>();
		payloadMap.put("uid", accountDTO.getId());
		payloadMap.put("uName", accountDTO.getLoginName());
		payloadMap.put("iat", new Date());
		payloadMap.put("ext", expiresDate);

		// 创建一个 JWS object
		JWSObject jwsObject = new JWSObject(header, new Payload(new JSONObject(payloadMap)));
		try {
			// 将jwsObject 进行HMAC签名
			jwsObject.sign(new MACSigner(SECRET));
			token = jwsObject.serialize();
		} catch (JOSEException e) {
			System.err.println("签名失败:" + e.getMessage());
			e.printStackTrace();
		}
		return token;
	}

	/**
	 * 校验token是否合法，返回Map集合,集合中主要包含    state状态码   data鉴权成功后从token中提取的数据
	 * 该方法在过滤器中调用，每次请求API时都校验
	 *
	 * @param token
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> validToken(String token) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			JWSObject jwsObject = JWSObject.parse(token);
			Payload payload = jwsObject.getPayload();
			JWSVerifier verifier = new MACVerifier(SECRET);

			if (jwsObject.verify(verifier)) {
				JSONObject jsonOBj = payload.toJSONObject();
				// token校验成功（此时没有校验是否过期）
				resultMap.put("state", TokenState.VALID.toString());
				// 若payload包含ext字段，则校验是否过期
				if (jsonOBj.containsKey("ext")) {
					long extTime = Long.valueOf(jsonOBj.get("ext").toString());
					long curTime = new Date().getTime();
					// 过期了
					if (curTime > extTime) {
						resultMap.clear();
						resultMap.put("state", TokenState.EXPIRED.toString());
					}
				}
				resultMap.put("data", jsonOBj);

			} else {
				// 校验失败
				resultMap.put("state", TokenState.INVALID.toString());
			}

		} catch (Exception e) {
			//e.printStackTrace();
			// token格式不合法导致的异常
			resultMap.clear();
			resultMap.put("state", TokenState.INVALID.toString());
		}
		return resultMap;
	}

/*  public static void main(String[] args) {
		try {
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.setId(10000L);
			accountDTO.setLoginName("admin");


			String token = createToken(accountDTO);
			System.out.println(token);
			Thread.sleep(1000);
			System.out.println(validToken(token));
			System.out.println(validToken("token"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
