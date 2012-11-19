package org.perzpy.qd;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class ClientCustomSSL {

    public final static void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	//keytool -import -noprompt -keystore d:/tmp/my.truststore -alias *.sdo.com -file d:\tmp\sdo.cer
            KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream instream = new FileInputStream(new File("my.truststore"));
            try {
                trustStore.load(instream, "123456".toCharArray());
            } finally {
                try { instream.close(); } catch (Exception ignore) {}
            }

            SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
            Scheme sch = new Scheme("https", 443, socketFactory);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);

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
    				.add(new BasicNameValuePair(
    						"service",
    						"http://me.qidian.com/Login.aspx?ReturnUrl=http://me.qidian.com/Index.aspx"));
    		formparams.add(new BasicNameValuePair("code", "2"));
    		formparams.add(new BasicNameValuePair("pageType", "0"));
    		formparams.add(new BasicNameValuePair("autoLogin", "0"));
    		formparams.add(new BasicNameValuePair("saveTime", "0"));
    		formparams.add(new BasicNameValuePair("loginCustomerUrl",
    				"http://me.qidian.com/Login.aspx?ReturnUrl=http://me.qidian.com/Index.aspx"));
    		formparams.add(new BasicNameValuePair("encryptFlag", "0"));

    		UrlEncodedFormEntity params = new UrlEncodedFormEntity(formparams,
    				"UTF-8");
    		HttpPost post = new HttpPost("https://cas.sdo.com/dplogin");
    		post.setEntity(params);
    		post.setHeader("Host", "cas.sdo.com");
    		post.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.4) Gecko/20100611 Firefox/3.6.4");
    		post.setHeader("Referer", "http://login.sdo.com/sdo/Login/LoginFrame.php");
            System.out.println("executing request" + post.getRequestLine());
            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            	
            String result = "";
            String toAddress = "";
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(entity));
            Header[] headers = response.getAllHeaders();
	          for (int i = 0; i < headers.length; i++) {
	          	System.out.println(headers[i].getName() + ":" + headers[i].getValue());
	          	if ("Location".equals(headers[i].getName())) {
	          		toAddress = headers[i].getValue();
	  			}
	          }
	          System.out.println(toAddress);
//            if (entity != null) {
//                result = EntityUtils.toString(entity);
//            }
//            if (result == null || "".equals(result)) {
//    			System.out.println("无响应！");
//    		}
//    		if (result.contains("display")) {
//    			System.out.println("需要输入验证码");
//    			toAddress = result.substring(result.lastIndexOf("http://"), result.lastIndexOf("';"));
//    		} else {
//    			toAddress = result.substring(result.indexOf("http://"), result.lastIndexOf("\";") - 1);
//    		}
//    		System.out.println(toAddress);
//    		response = httpclient.execute(new HttpGet(toAddress));
//    		System.out.println(EntityUtils.toString(response.getEntity(), "gbk"));
//            Header[] headers = response.getAllHeaders();
//            for (int i = 0; i < headers.length; i++) {
//            	System.out.println(headers[i].getName() + ":" + headers[i].getValue());
//            	if ("Location".equals(headers[i].getName())) {
//            		toAddress = headers[i].getValue();
//    			}
//            }
//            if (!"".equals(toAddress.trim())) {
//            	response = httpclient.execute(new HttpGet(toAddress));
//     			System.out.println(EntityUtils.toString(response.getEntity()));
//     			System.out.println("----------------------------------------");
//            }
//    		if (result == null || "".equals(result)) {
//    			System.out.println("无响应！");
//    			return;
//    		}
//    		if (result.contains("display")) {
//    			System.out.println("需要输入验证码");
//    			toAddress = result.substring(result.lastIndexOf("http://"), result.lastIndexOf("';"));
//    		} else {
//    			toAddress = result.substring(result.indexOf("http://"), result.lastIndexOf("\";") - 1);
//    		}
//            System.out.println(EntityUtils.toString(entity));

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }

}
