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
	 * ��ȡ���ı�
	 * @param inputString
	 * @return
	 */
	public static String HtmlToText(String inputString) {
		String htmlStr = inputString; // ��html��ǩ���ַ���
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // ����script��������ʽ{��<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // ����style��������ʽ{��<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_html = "<[^>]+>"; // ����HTML��ǩ��������ʽ
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // ����script��ǩ
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // ����style��ǩ
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // ����html��ǩ
			textStr = htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		// �޳��ո���
		textStr = textStr.replaceAll("[ ]+", " ");
		textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
		return textStr;// �����ı��ַ���
	}
	
	/**
	 *�޳��޹ر�ǩ�����ҳ�ļ��ַ���
	 * @param htmlPath  Ŀ���ļ�·��
	 * @param htmlName  Ŀ���ļ���
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
				return HtmlToText(content); //������ҳ
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
	 *���������޳��޹ر�ǩ����ļ�
	 * @param htmlPath  Ŀ���ļ�·��
	 * @param htmlName  Ŀ���ļ���
	 * @param savePath  ����·��
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
				File out = new File(savePath+"/handled_"+htmlName);  //�����ʱ�ļ�
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
