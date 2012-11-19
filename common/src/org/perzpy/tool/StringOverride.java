package org.perzpy.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;

/**
 * 重写类的String方法
 *
 * @author perzer
 * @date Jun 28, 2011
 */
public class StringOverride {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//类当前路径
		String path = "D:/project/test/src/com/perzer/entity";
//		override(path, MemSeat.class);
	}
	
	public static boolean override(String path, Class cls) {
		String packName = cls.getPackage().getName();
		File file = new File(path);
		File[] files = file.listFiles();
		try {
			for (int i = 0; i < files.length; i++) {
				File temp = files[i];
				if (temp != null && temp.getName().endsWith(".java")) {
					String fileName = temp.getName();
					String content = methodCreate(packName + "." + 
							fileName.substring(0, fileName.indexOf(".")));
					System.out.println(writeToFile(temp.getPath(), content));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 产生方法代码
	 * @param className 重写的类名
	 * @return 代码内容
	 * @throws Exception
	 */
	public static String methodCreate(String className) throws Exception {
		Field[] fields = Class.forName(className).getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic String toString() {\n");
		sb.append("\t\treturn \"");
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.getModifiers() != 9 && field.getModifiers() != 23) {
				sb.append("[" + field.getName() + "=\" + this." 
						+ field.getName() + " +\n");
				sb.append("\t\t\t\"]");
			}
		}
		sb.append("\";\n\t}");
		className = className.replaceAll("[.]", "\\\\");
		return sb.toString();
	}
	
	/**
	 * 将产生的代码写入文件
	 * @param path 文件路径
	 * @param content 产生代码
	 * @return 写入结果
	 */
	public static boolean writeToFile(String path, String content) {
		StringBuffer sb = new StringBuffer();
		try {
			//读取文件内容
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String temp = reader.readLine();
			while (temp != null) {
				sb.append(temp + "\n");
				temp = reader.readLine();
			}
			reader.close();
			
			//组装内容
			sb.insert(sb.length() - 2, content + "\n");
			
			//写入文件
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.append(sb.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
