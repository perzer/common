package org.perzpy.qd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 将http开头的文本替换成超链接的HTML
 * @author perzpy(perzpy@gmail.com)
 * @date Sep 13, 2011
 *
 */
public class TextToLink {

	private void execute(String path) throws Exception {
		File file = new File(path);
		StringBuffer sb = new StringBuffer();

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String tmp = null;
		while ((tmp = reader.readLine()) != null) {
			if (tmp.indexOf("http") != -1) {
				sb.append("<a href='" + tmp + "'>" + tmp + "</a>");
				System.out.println(tmp);
			}
			sb.append("<br />");
		}
		sb.append("</html>");
		reader.close();
		sb.insert(0, "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head>");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file.getParent() + File.separator
				+ System.currentTimeMillis() + ".html"));
		writer.append(sb);
		writer.flush();
		writer.close();
	}

	public static void main(String[] args) {
		TextToLink to = new TextToLink();
		try {
			to.execute("/tmp/qd2.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
