package com.yunphone;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 ChengShi
 * @日期 2020-11-07 14:44:21
 * @版本 1.0
 * @描述 数据中心
 */
class DataCenter {
	DataCenter(String...paths) {this.paths = paths;}
	/** 缓存主ip池 */
	final List<Long> cache = new ArrayList<>();
	final String[] paths;
	/** 相同的那部分主ip */
	final List<String> EQUALS = new ArrayList<String>();
	/** 不相同的那部分ip */
	final List<String> NOT_EQUALS = new ArrayList<>();
}
