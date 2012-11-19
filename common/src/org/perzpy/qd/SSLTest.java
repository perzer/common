package org.perzpy.qd;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 * 模拟HTTPS（不处理SSL）
 * @author perzpy(perzpy@gmail.com)
 * @date Sep 17, 2011
 *
 */
public class SSLTest {

	public static void main(String[] args) throws Exception {
		TrustManager easyTrustManager = new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {

			}
		};
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, new TrustManager[] { easyTrustManager }, null);
		Scheme http = new Scheme("http", PlainSocketFactory.getSocketFactory(),
				80);
		SSLSocketFactory sf = new SSLSocketFactory(sc);
		Scheme https = new Scheme("https", sf, 443);

		DefaultHttpClient client = new DefaultHttpClient();
		client.getConnectionManager().getSchemeRegistry().register(http);
		client.getConnectionManager().getSchemeRegistry().register(https);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username", ""));
		formparams.add(new BasicNameValuePair("password", ""));
		formparams.add(new BasicNameValuePair("ptname", ""));
		formparams.add(new BasicNameValuePair("ptpwd", ""));
		formparams.add(new BasicNameValuePair("templateId", ""));
		formparams.add(new BasicNameValuePair("sdid", ""));
		formparams.add(new BasicNameValuePair("infoEx", ""));
		formparams.add(new BasicNameValuePair("uid", ""));
		formparams.add(new BasicNameValuePair("appArea", "0"));
		formparams.add(new BasicNameValuePair("appId", "201"));
		formparams
				.add(new BasicNameValuePair("service",
						"http://me.qidian.com/Login.aspx?ReturnUrl=http://me.qidian.com/Index.aspx"));
		formparams.add(new BasicNameValuePair("code", "2"));
		formparams.add(new BasicNameValuePair("pageType", "0"));
		formparams.add(new BasicNameValuePair("autoLogin", "0"));
		formparams.add(new BasicNameValuePair("saveTime", "0"));
		formparams
				.add(new BasicNameValuePair("loginCustomerUrl",
						"http://me.qidian.com/Login.aspx?ReturnUrl=http://me.qidian.com/Index.aspx"));
		formparams.add(new BasicNameValuePair("encryptFlag", "0"));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
				"UTF-8");
		HttpPost post = new HttpPost("http://cas.sdo.com/dplogin");
		post.setEntity(entity);
		post.setHeader("Host", "cas.sdo.com");
		post.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.4) Gecko/20100611 Firefox/3.6.4");
		post.setHeader("Referer",
				"https://login.sdo.com/sdo/Login/LoginFrame.php");
		HttpResponse response = client.execute(post);
		System.out.println(response.getStatusLine());
		String result = EntityUtils.toString(response.getEntity(), "gbk");
		System.out.println("----------------------------------------");
		String toAddress = "";
		 Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
            	System.out.println(headers[i].getName() + ":" + headers[i].getValue());
            	if ("Location".equals(headers[i].getName())) {
            		toAddress = headers[i].getValue();
    			}
            }
        response = client.execute(new HttpGet(toAddress));
		System.out.println(EntityUtils.toString(response.getEntity()));
		System.out.println("----------------------------------------");
		System.out.println(result);
	}

}
