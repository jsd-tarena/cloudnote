package com.tarena.cloudnote.util;

import java.util.UUID;

/**
 * UUID 生成工具
 * @author YHT
 *
 */
public class UUIDUtil {
	public static String getUId(){
		return UUID.randomUUID().toString();
	}
}