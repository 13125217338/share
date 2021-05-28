package org.ice.demo.common.constant;

/**
 * @作者 ChengShi
 * @日期 2020-09-18 15:14:27
 * @版本 1.0
 * @描述 结果异常枚举
 */
public enum RestException {
	/*登录异常*/
	LOGIN_VERIFY(new WebExcept("登录验证失败，请检查账号密码！")),
	ADD_USER_RESULT(new WebExcept("添加用户异常！")),
	LOGIN_IP_SELECT(new WebExcept("登录查询IP所属地失败，请重试！")),
	/*其他异常*/
	OTHER_EXP(new WebExcept("未知异常！")),
	;
	
	private final WebExcept exp;
	public WebExcept exp(){return this.exp;}
	private RestException(WebExcept exp) {this.exp = exp;}
	
	
	/**
	 * @作者 ChengShi
	 * @日期 2021-08-16 17:47:31
	 * @版本 1.0
	 * @parentClass RestException
	 * @描述 页面异常
	 */
	public static class WebExcept extends RuntimeException{
		private static final long serialVersionUID = 1L;

		private WebExcept(String msg) {super(msg);}
	}
}
