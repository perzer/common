package org.perzpy.baidu;

import java.io.IOException;

import org.perzpy.httpclient.WebClient;
import org.perzpy.httpclient.WebClient.HTTPMethod;

/**
 * 模拟百度登录
 * @author perzpy(perzpy@gmail.com)
 * @date Sep 16, 2011
 *
 */
public class BaiduLogin {
	public static String Login(String username, String password) throws IOException {
    	WebClient web = new WebClient("https://passport.baidu.com/?login", HTTPMethod.POST);
    	web.addPostParameter("password", username);
    	web.addPostParameter("username", password);
    	web.addPostParameter("tpl_ok", "");
    	web.addPostParameter("next_target", "");
    	web.addPostParameter("tpl", "mn");
    	web.addPostParameter("skip_ok", "");
    	web.addPostParameter("aid", "");
    	web.addPostParameter("need_pay", "");
    	web.addPostParameter("need_coin", "");
    	web.addPostParameter("pay_method", "");
    	web.addPostParameter("u", "http://passport.baidu.com/");
    	web.addPostParameter("return_method", "get");
    	web.addPostParameter("more_param", "");
    	web.addPostParameter("return_type", "");
    	web.addPostParameter("psp_tt", "0");
    	web.addPostParameter("safeflg", "0");
    	web.addPostParameter("isphone", "tpl");
    	web.addPostParameter("verifycode", "");
    	web.setUrl("http://hi.baidu.com/perzpy/ihome/ihomefeed");
    	web.setMethod(HTTPMethod.GET);
    	return web.getTextContent();
	}
}
