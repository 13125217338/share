package com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import com.yunphone.IpFactory;

public class text1 {
	
	public static void main(String[] args) throws Exception{
		/*主已配ip*/
		IpFactory factory = new IpFactory(
				"E:\\work\\云手机\\ip配置\\104.168.84.114.txt",
				"E:\\work\\云手机\\ip配置\\172.245.99.66.txt",
				"E:\\work\\云手机\\ip配置\\66.225.194.162.txt",
				"E:\\work\\云手机\\ip配置\\206.217.134.26.txt",
				"E:\\work\\云手机\\ip配置\\205.234.153.122.txt",
				"E:\\work\\云手机\\ip配置\\23.94.66.186.txt",
				"E:\\work\\云手机\\ip配置\\23.94.29.210.txt",
				"E:\\work\\云手机\\ip配置\\192.227.146.42.txt",
				"E:\\work\\云手机\\ip配置\\23.95.109.170.txt");
		/*输入源*/
		List<String> mainValue = factory.getFileValues("E:\\work\\云手机\\ip配置\\in\\ip.txt");
		List<String> source = factory.getFileValues("E:\\work\\云手机\\ip配置\\in\\source.txt");
		/*写命令*/
		writeIptables(factory, mainValue, source);
		writeFirewall(factory, mainValue);
		/*删除命令*/
//		writeDelIptables(factory, mainValue, source);
//		writeDelFirewall(factory, mainValue);
		/*最后写重复ip*/
		factory.writeEqualDatas("E:\\work\\云手机\\ip配置\\out\\equals.txt");
		/*追加到ip池（用于检验重复ip，实际追加到哪里就写哪个地址）*/
		factory.writePathDatas("E:\\work\\云手机\\ip配置\\66.225.194.162.txt");     
	}
	private static void writeIptables(IpFactory factory, List<String> mainValue, List<String> source) throws Exception{
		/*获取一行一行的值*/
		List<String> one = factory.getOne("iptables -t nat -A POSTROUTING -s {0}/32 -j SNAT --to-source {1}", 
				mainValue, true, source);
		/*一次写五十个*/
		int limit = 50;
		for (int i = 0,j = (one.size() / limit); i < j; i++) {
			try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\iptables"+i+".txt"))){
				List<String> subList = one.subList(i * limit, i * limit + limit);
				for (String value : subList) {bufferedWriter.write(value + "\r\n");}
			}
		}
		/*最后写剩余*/
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\iptables_last.txt"))){
			List<String> subList = one.subList(one.size() - one.size() % limit, one.size());
			for (String value : subList) {bufferedWriter.write(value + "\r\n");}
		}
	}
	private static void writeFirewall(IpFactory factory, List<String> mainValue) throws Exception{
		List<String> range = factory.getRange("source-address range {0}", mainValue, true);
		/*一次写五十个*/
		int limit = 50;
		for (int i = 0,j = (range.size() / limit); i < j; i++) {
			try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\firewall"+i+".txt"))){
				List<String> subList = range.subList(i * limit, i * limit + limit);
				for (String value : subList) {bufferedWriter.write(value + "\r\n");}
			}
		}
		/*最后写剩余*/
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\firewall_last.txt"))){
			List<String> subList = range.subList(range.size() - range.size() % limit, range.size());
			for (String value : subList) {bufferedWriter.write(value + "\r\n");}
		}
	}
	private static void writeDelFirewall(IpFactory factory, List<String> mainValue) throws Exception{
		List<String> range = factory.getRange("undo source-address range {0}", mainValue, false);
		/*一次写五十个*/
		int limit = 50;
		for (int i = 0,j = (range.size() / limit); i < j; i++) {
			try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\Delfirewall"+i+".txt"))){
				List<String> subList = range.subList(i * limit, i * limit + limit);
				for (String value : subList) {bufferedWriter.write(value + "\r\n");}
			}
		}
		/*最后写剩余*/
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\Delfirewall_last.txt"))){
			List<String> subList = range.subList(range.size() - range.size() % limit, range.size());
			for (String value : subList) {bufferedWriter.write(value + "\r\n");}
		}
	}
	private static void writeDelIptables(IpFactory factory, List<String> mainValue, List<String> source) throws Exception{
		/*获取一行一行的值*/
		List<String> one = factory.getOne("iptables -t nat -D POSTROUTING -s {0}/32 -j SNAT --to-source {1}", 
				mainValue, false, source);
		/*一次写五十个*/
		int limit = 50;
		for (int i = 0,j = (one.size() / limit); i < j; i++) {
			try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\DELiptables"+i+".txt"))){
				List<String> subList = one.subList(i * limit, i * limit + limit);
				for (String value : subList) {bufferedWriter.write(value + "\r\n");}
			}
		}
		/*最后写剩余*/
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\work\\云手机\\ip配置\\out\\DELiptables_last.txt"))){
			List<String> subList = one.subList(one.size() - one.size() % limit, one.size());
			for (String value : subList) {bufferedWriter.write(value + "\r\n");}
		}
	}
}

