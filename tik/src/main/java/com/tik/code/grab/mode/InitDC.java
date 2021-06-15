package com.tik.code.grab.mode;

/**
 * @作者 ChengShi
 * @日期 2021年1月11日
 * @版本 1.0
 * @描述 
 */
public class InitDC {
	private final String _json_att = "";
	public String get_json_att() {
		return _json_att;
	}
	
	/**
	 * @作者 ChengShi
	 * @日期 2021年1月11日
	 * @版本 1.0
	 * @描述 DC数据
	 */
	public static class DCData{
		private String globalRepeatSubmitToken;
		private String key_check_isChange;
		
		public String getGlobalRepeatSubmitToken() {
			return globalRepeatSubmitToken;
		}
		public void setGlobalRepeatSubmitToken(String globalRepeatSubmitToken) {
			this.globalRepeatSubmitToken = globalRepeatSubmitToken;
		}
		public String getKey_check_isChange() {
			return key_check_isChange;
		}
		public void setKey_check_isChange(String key_check_isChange) {
			this.key_check_isChange = key_check_isChange;
		}
		
	}
}
