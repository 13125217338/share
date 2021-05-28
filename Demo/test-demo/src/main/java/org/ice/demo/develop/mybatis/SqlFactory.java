package org.ice.demo.develop.mybatis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;

import com.icecream.annotation.Init;
import com.icecream.annotation.Start;
import com.icecream.annotation.Value;

/**
 * @作者 ChengShi
 * @日期 2020-10-26 10:04:31
 * @版本 1.0
 * @描述 Sql工厂选择
 */
@Start(level = 0)
public final class SqlFactory {
	/*第一参数*/
	@Value(value = "${jdbc1.url}")
	private static String url1;
	@Value(value = "${jdbc1.driver}")
	private static String driver1;
	@Value(value = "${jdbc1.username}")
	private static String username1;
	@Value(value = "${jdbc1.password}")
	private static String password1;
	/*第二参数*/
	@Value(value = "${jdbc2.url}")
	private static String url2;
	@Value(value = "${jdbc2.driver}")
	private static String driver2;
	@Value(value = "${jdbc2.username}")
	private static String username2;
	@Value(value = "${jdbc2.password}")
	private static String password2;
	
	/*数据源初始化*/
	private static Map<Class<?>, Factory> datas = new HashMap<>();
	@Init
	private static void init(){
		datas.put(Mysql1.class, new Mysql1());
		datas.put(Mysql2.class, new Mysql2());
	}
	
	/**
	 * @描述 获取所有数据
	 * @return 工厂集合
	 */
	public static Collection<Factory> values(){return datas.values();}
	/**
	 * @描述 获取类对应的sql会话工厂
	 * @param factory 对应类
	 * @return 对应sql会话工厂
	 */
	public static SqlSessionFactory getSqlSessionFactory(Class<?> factory){return datas.get(factory).getSqlSessionFactory();}
	
	/**
	 * @作者 ChengShi
	 * @日期 2020-10-26 15:24:57
	 * @版本 1.0
	 * @parentClass SqlFactory
	 * @描述 第一个Mysql工厂
	 */
	public static class Mysql1 implements Factory{
		private Mysql1() {}
		private SqlSessionFactory sqlSessionFactory;
		@Override
		public SqlSessionFactory getSqlSessionFactory() {return this.sqlSessionFactory;}
		@Override
		public UnpooledDataSource getUnpooledDataSource() {return new UnpooledDataSource(driver1, url1, username1, password1);}
		@Override
		public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}
		
	}
	/**
	 * @作者 ChengShi
	 * @日期 2020-10-26 15:25:07
	 * @版本 1.0
	 * @parentClass SqlFactory
	 * @描述 第二个Mysql工厂
	 */
	public static class Mysql2 implements Factory{
		private Mysql2() {}
		private SqlSessionFactory sqlSessionFactory;
		@Override
		public SqlSessionFactory getSqlSessionFactory() {return this.sqlSessionFactory;}
		@Override
		public UnpooledDataSource getUnpooledDataSource() {return new UnpooledDataSource(driver2, url2, username2, password2);}
		@Override
		public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {this.sqlSessionFactory = sqlSessionFactory;}
		
	}
	
	/*工厂共有接口*/
	public interface Factory{
		public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
		public SqlSessionFactory getSqlSessionFactory();
		public UnpooledDataSource getUnpooledDataSource();
	}
}
