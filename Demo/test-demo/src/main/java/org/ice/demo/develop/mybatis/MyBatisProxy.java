package org.ice.demo.develop.mybatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.ice.demo.develop.mybatis.MyBatisDevelop.BatchSession;

import com.http.Web;
import com.icecream.web.Util;
import com.log.Log;
import com.log.LogFactory;
import com.task.Runnable;

/**
 * @作者 ChengShi
 * @日期 2020-04-30 14:32:11
 * @版本 1.0
 * @描述 mapper代理类
 */
final class MyBatisProxy implements InvocationHandler{
	/*超时重连-1小时*/
	private final static long TIME_OUT_RECONNECT = 1000 * 60 * 60;
	private final SqlSessionFactory sqlSessionFactory;
	private final Class<?> mapperClass;
	private final Class<?> factory;
	private final ThreadLocal<BatchSession> batchSession;
	MyBatisProxy(ThreadLocal<BatchSession> batchSession, Class<?> mapperClass, Class<?> factory, SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.mapperClass = mapperClass;
		this.factory = factory;
		this.batchSession = batchSession;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		BatchSession session = batchSession.get();
		SqlSession sqlSession = null;
		try {
			if (session == null) {
				session = new BatchSession(false);
				batchSession.set(session);
			}
			sqlSession = session.sqlSession.get(factory);
			if (sqlSession == null || sqlSession.getConnection().isClosed()) {
				session.sqlSession.put(factory, sqlSessionFactory.openSession());
				sqlSession = session.sqlSession.get(factory);
			}
			/*超时重连*/
			if ((System.currentTimeMillis() - session.timeReconnet) > TIME_OUT_RECONNECT) {
				sqlSession.close();
				session.sqlSession.put(factory, sqlSessionFactory.openSession());
				sqlSession = session.sqlSession.get(factory);
			}
			return method.invoke(sqlSession.getMapper(mapperClass), args);
		} catch (Throwable e) {
			if (!session.isTransaction && sqlSession != null) {sqlSession.rollback();}
			throw Util.getRealExcept(e);
		} finally {
			if (!session.isTransaction && sqlSession != null) {sqlSession.commit();}
			session.timeReconnet = System.currentTimeMillis();
		}
	}
}
/**
 * @作者 ChengShi
 * @日期 2020-05-09 23:13:32
 * @版本 1.0
 * @描述 事物代理类
 */
final class MyBatisProxy_ implements InvocationHandler{
	/*日志*/
	private final Log log = LogFactory.getLog();
	private final Object transaction;
	private final ThreadLocal<BatchSession> batchSession;
	private final Map<String, Object> VALUE = new HashMap<String, Object>();
	MyBatisProxy_(ThreadLocal<BatchSession> batchSession, Object transaction) {
		this.batchSession = batchSession;
		this.transaction = transaction;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Transaction isTransaction = method.getDeclaredAnnotation(Transaction.class);
		/*如果该方法不是事物管理*/
		if (isTransaction == null) {return method.invoke(transaction, args);}
		else{
			/*记录唯一值做获取执行值*/
			String id = UUID.randomUUID().toString();
			final Thread curThread = Thread.currentThread();
			Web.PutTaskSys(new Runnable() {
				@Override
				public void run() throws Throwable {
					/*拷贝线程值*/
					Web.getTv().copy(Web.getTv().getAll(curThread), Thread.currentThread());
					BatchSession session = batchSession.get();
					try {
						if (session == null) {
							session = new BatchSession(true);
							batchSession.set(session);
						}
						session.isTransaction = true;
						VALUE.put(id, method.invoke(transaction, args));
					} catch (Throwable e) {
						if (session != null) {
							for(SqlSession sqlSession : session.sqlSession.values()){
								if (sqlSession != null) {sqlSession.rollback();}
							}
						}
						e = Util.getRealExcept(e);
						log.error("执行事务失败，正在回滚》》》", e);
						VALUE.put(id, e);
					} finally {
						if (session != null) {
							for(SqlSession sqlSession : session.sqlSession.values()){
								if (sqlSession != null) {sqlSession.commit();}
							}
							session.isTransaction = false;
						}
					}
				}
			}, Short.MAX_VALUE);
			/*获取异步线程同步方法值*/
			Object val = VALUE.remove(id);
			if (val instanceof Throwable) {throw (Throwable)val;} else{return val;}
		}
	}
}
