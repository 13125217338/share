package com.yunphone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.util.MyUtil;

/**
 * @作者 ChengShi
 * @日期 2020-11-07 14:43:00
 * @版本 1.0
 * @描述 IP工厂（非线程安全）
 */
public class IpFactory {
	private final DataCenter center;
	private final String POINT = "\\.";
	private final String FKL = "{";
	private final String FKR = "}";
	private final String SPACE = " ";
	private final String ZERO = "{0}";
	private final String ENTER = "\r\n";
	/**
	 * @param paths 初始化ip文件地址获取主ip集合
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public IpFactory(String...paths) throws FileNotFoundException, IOException {
		this.center = new DataCenter(paths);
		init();
	}
	/*初始化*/
	private void init() throws FileNotFoundException, IOException{
		for (String path : this.center.paths) {
			List<String> fileValues = getFileValues(path);
			for (String value : fileValues) {
				Long val = getVal(value.split(POINT));
				if (val != null) {this.center.cache.add(val);}
			}
		}
	}
	/**
	 * @描述 将文件中的一行一行值转换为集合值
	 * @param path 待转换的文件地址
	 * @return 转换后的集合值
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> getFileValues(String path) throws FileNotFoundException, IOException{
		List<String> values = new ArrayList<String>();
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
			String value = null;
			while((value = bufferedReader.readLine()) != null) {values.add(value.trim());}
		}
		return values;
	}
	/*获取值*/
	private Long getVal(String[] vals){
		if (vals.length > 3) {
			long val = Long.parseLong(vals[0]) << 24;
			val += (Long.parseLong(vals[1]) << 16);
			val += (Long.parseLong(vals[2]) << 8);
			val += (Long.parseLong(vals[3]));
			return val;
		}else {return null;}
	}
	
	/**
	 * @描述 获取主地址一行一行的集合值
	 * @param template 模板（如：iptables {0} -A POSTROUTING {1} -to-source，一定要有一个{0}主地址）
	 * @param mainValue 主ip地址集合值
	 * @param equalSkip 如果为true，主ip地址值如果与初始化内的值一致会跳过该ip 
	 * @param values 每个额外数组值
	 * @return 一行集合值
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("all")
	public List<String> getOne(String template, List<String> mainValue, boolean equalSkip, List<String>...values) throws FileNotFoundException, IOException{
		this.center.EQUALS.clear();
		this.center.NOT_EQUALS.clear();
		List<Long> notEqualTemp = new ArrayList<>();
		List<String> vals = new ArrayList<>();
		/*获取文件值*/
		values = values == null ? new ArrayList[0] : values;
		Object[][] temps = new Object[(values.length+1)][]; 
		temps[0] = mainValue.toArray();
		for (int i = 0; i < values.length; i++) {
			temps[(i+1)] = values[i].toArray();
		}
		/*获取包含值，但移除主要地址*/
		Map<String, String> containValue = MyUtil.getContainValue(FKL, FKR, template);
		containValue.remove(ZERO);
		for (int i = 0,j = 0,z = temps[0].length; i < z; i++) {
			String temp = template;
			Long val = getVal(temps[0][i].toString().split(POINT));
			if (val == null) {continue;}
			if (this.center.cache.contains(val) || notEqualTemp.contains(val)){
				this.center.EQUALS.add(temps[0][i].toString());
				if (equalSkip) {continue;}
			}else{this.center.NOT_EQUALS.add(temps[0][i].toString());notEqualTemp.add(val);}
			temp = temp.replace(ZERO, temps[0][i].toString());
			for (Entry<String, String> entry : containValue.entrySet()) {temp = temp.replace(entry.getKey(), temps[Integer.parseInt(entry.getValue())][j].toString());}
			vals.add(temp);j++;
		}
		return vals;
	}
	
	/**
	 * @描述 获取一个主地址范围的ip值，二个范围值用空格分开
	 * @param template 模板（如：iptables {0} -A POSTROUTING {1} -to-source，一定要有一个{0}主地址）
	 * @param mainValue 主ip地址集合值
	 * @param equalSkip 如果为true，主ip地址值如果与初始化内的值一致会跳过该ip 
	 * @param values 每个额外数组值
	 * @return 范围值
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("all")
	public List<String> getRange(String template, List<String> mainValue, boolean equalSkip, List<String>...values) throws FileNotFoundException, IOException{
		this.center.EQUALS.clear();
		this.center.NOT_EQUALS.clear();
		List<String> vals = new ArrayList<>();
		/*获取文件值*/
		values = values == null ? new ArrayList[0] : values;
		Object[][] temps = new Object[(values.length+1)][]; 
		temps[0] = getMainValue(mainValue, equalSkip);
		for (int i = 0; i < values.length; i++) {
			temps[(i+1)] = values[i].toArray();
		}
		/*获取包含值，但移除主要地址*/
		Map<String, String> containValue = MyUtil.getContainValue(FKL, FKR, template);
		containValue.remove(ZERO);
		for (int i = 0,j = 0,z = temps[0].length; i < z; i++) {
			String temp = template;
			temp = temp.replace(ZERO, temps[0][i].toString());
			for (Entry<String, String> entry : containValue.entrySet()) {temp = temp.replace(entry.getKey(), temps[Integer.parseInt(entry.getValue())][j].toString());}
			vals.add(temp);j++;
		}
		return vals;
	}
	/*获取范围主要数组*/
	private Object[] getMainValue(List<String> mainValues, boolean equalSkip){
		List<Long> notEqualTemp = new ArrayList<>();
		List<String> temps = new ArrayList<>();
		Map<Long, String> values = new TreeMap<Long, String>();
		/*顺序排序*/
		for (String value : mainValues) {
			Long val = getVal(value.split(POINT));
			if (val == null) {continue;}
			if (this.center.cache.contains(val) || notEqualTemp.contains(val)) {
				this.center.EQUALS.add(value);
				if (equalSkip) {continue;}
			}else{this.center.NOT_EQUALS.add(value);notEqualTemp.add(val);}
			values.put(val, value);
		}
		Entry<Long, String> prep = null;
		String temp = null;
		for (Entry<Long, String> entry : values.entrySet()) {
			if (prep == null) {prep = entry;temp = prep.getValue().trim();}
			else{
				if ((entry.getKey() - prep.getKey()) != 1) {
					temps.add(temp + SPACE + prep.getValue().trim());
					temp = entry.getValue().trim();
				}
				prep = entry;
			}
		}
		/*结束来一次*/
		if (temp != null) {temps.add(temp + SPACE + prep.getValue().trim());}
		return temps.toArray();
	}
	
	
	/**
	 * @描述 追加写入ip地址相同的那部分数据
	 * @param path 待追加写入相同ip的地址文件
	 * @throws IOException
	 */
	public void writeEqualDatas(String path) throws IOException{
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))){
			for (String value : this.center.EQUALS) {bufferedWriter.write(value + ENTER);}
		}
	}
	
	/**
	 * @描述 追加写入ip地址不相同的那部分数据
	 * @param path 待追加写入不相同ip的地址文件
	 * @throws IOException
	 */
	public void writePathDatas(String path) throws IOException{
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))){
			for (String value : this.center.NOT_EQUALS) {
				bufferedWriter.write(value + ENTER);
				Long val = getVal(value.split(POINT));
				if (val != null) {this.center.cache.add(val);}
			}
		}
	}
	
}
