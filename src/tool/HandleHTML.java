package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class HandleHTML {
	/**
	 * 提取纯文本
	 * @param inputString
	 * @return
	 */
	public static String HtmlToText(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		// 剔除空格行
		textStr = textStr.replaceAll("[ ]+", " ");
		textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
		return textStr;// 返回文本字符串
	}
	
	/**
	 *剔除无关标签后的网页文件字符串
	 * @param htmlPath  目标文件路径
	 * @param htmlName  目标文件名
	 */
	public static String HandleFile(String htmlPath,String htmlName) {
		File webFile = new File(htmlPath+"/"+htmlName);
		try {
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(new FileInputStream(webFile), "utf-8"));
			String content = "";
			String line = "";
			try {
				while((line = urlReader.readLine())!=null) {
					content += line;
					content += "\n";
				}
				urlReader.close();
				return HtmlToText(content); //处理网页
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *处理并保存剔除无关标签后的文件
	 * @param htmlPath  目标文件路径
	 * @param htmlName  目标文件名
	 * @param savePath  保存路径
	 */
	/*public static void HandleFile(String htmlPath,String htmlName,String savePath) {
		File webFile = new File(htmlPath+"/"+htmlName);
		try {
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(new FileInputStream(webFile), "utf-8"));
			String content = "";
			String line = "";
			try {
				while((line = urlReader.readLine())!=null) {
					content += line;
					content += "\n";
				}
				File out = new File(savePath+"/handled_"+htmlName);  //输出临时文件
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"utf-8"));
				String result = HtmlToText(content);
				bw.write(result);
				urlReader.close();
				bw.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				urlReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
