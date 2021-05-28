package org.ice.demo.develop.mybatis;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.ice.demo.develop.log.Mybatis;
import org.ice.demo.develop.mybatis.SqlFactory.Factory;

import com.icecream.annotation.AOP;
import com.icecream.annotation.Develop;
import com.icecream.annotation.Init;
import com.icecream.constant.DevelopIn;
import com.icecream.core.IceBean;

/**
 * @作者 ChengShi
 * @日期 2020-04-30 09:06:37
 * @版本 1.0
 * @描述 部署mybatis类
 */
@Develop(level = 0)
public class MyBatisDevelop implements DevelopIn{
	private final ThreadLocal<BatchSession> batchSession = new ThreadLocal<>();
	@Init
	private void init() throws SQLException{setSqlSessionFactory();}
	
	@Override
	public void develop(Set<Class<?>> scanClass, IceBean iceBean, Set<Object> init) throws Throwable {
		/*解析类*/
		for (Class<?> class1 : scanClass) {
			Mapper mapper = class1.getDeclaredAnnotation(Mapper.class);
			if (mapper != null) {
				SqlSessionFactory sqlSessionFactory = SqlFactory.getSqlSessionFactory(mapper.factory());
				sqlSessionFactory.getConfiguration().addMapper(class1);
				Object object = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{class1}, new MyBatisProxy(batchSession, class1, mapper.factory(), sqlSessionFactory));
				iceBean.setBean(object);
			}else{
				if (!class1.isInterface()) {
					Class<?>[] interfaces = class1.getInterfaces();
					if (interfaces != null && interfaces.length > 0) {
						for (Class<?> class2 : interfaces) {
							Method[] declaredMethods = class2.getDeclaredMethods();
							for (Method method : declaredMethods) {
								Transaction transaction = method.getDeclaredAnnotation(Transaction.class);
								if (transaction != null) {
									Object bean = iceBean.getBean(class1);
									/*如果该类有aop且不为代理，则初始化当前类做代理内使用并初始化*/
									AOP aop = bean.getClass().getDeclaredAnnotation(AOP.class);
									if (aop != null && !aop.isProx()) {
										bean = class1.newInstance();
										init.add(bean);
									}
									Object newProxyInstance = Proxy.newProxyInstance(this.getClass().getClassLoader(), interfaces, 
											new MyBatisProxy_(batchSession, bean));
									iceBean.setProxBean(class1, newProxyInstance);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	/*获取Sql会话工厂*/
	private void setSqlSessionFactory() throws SQLException{
		for(Factory sqlFactory : SqlFactory.values()){
			UnpooledDataSource dataSource = sqlFactory.getUnpooledDataSource();
			dataSource.setAutoCommit(false);
			dataSource.setDefaultTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
			Environment environment = new Environment(UUID.randomUUID().toString(), new JdbcTransactionFactory(), dataSource);
			Configuration configuration = new Configuration(environment);
			configuration.setCacheEnabled(true);
			configuration.setLogImpl(Mybatis.class);
			SqlSessionFactoryBuilder sessionFactoryBuilder = new SqlSessionFactoryBuilder();
			sqlFactory.setSqlSessionFactory(sessionFactoryBuilder.build(configuration));
		}
	}
	
	/**
	 * @作者 ChengShi
	 * @日期 2020-04-30 16:45:12
	 * @版本 1.0
	 * @parentClass MyBatisProxy
	 * @描述 批量类
	 */
	static class BatchSession{
		final Map<Class<?>, SqlSession> sqlSession = new HashMap<>(4);
		BatchSession(boolean isTransaction) {this.isTransaction = isTransaction;}
		boolean isTransaction;
		long timeReconnet = System.currentTimeMillis();
	}
}
