package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class HandleChinese {
	public static List<String> STOPWORDS = new ArrayList<String>();  //ͣ�ôʱ�
	/**
	 * ��������ͣ�ôʱ����ɾ������
	 * @param wordsList
	 * @return
	 */
	public static List<String> DeleteStopWord(List<String> wordsList) {
		List<String> s=new ArrayList<String>();		
		HashSet hs1 = new HashSet(wordsList);
		HashSet hs2 = new HashSet(STOPWORDS);
		hs1.removeAll(hs2);
		s.addAll(hs1);
		return s;
	}
	
	/**
	 * ��ʼ��ͣ�ôʱ�
	 */
	public static void InitStopWord() {
		File in = new File("F:/Eclipse/eclipse/code/SearchEngine/C_StopWord.txt"); //��ȡ����ͣ�ôʱ�
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(in),"utf-8"));
			while(true){
				try {
					String line = br.readLine();
					if(line == null)
						break;
					STOPWORDS.add(line); //���ͣ�ô�
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ֻ�����ĵ��е������ַ�
	 * @param text ��Ҫ������ַ���
	 * @return
	 */
	public static String RemoveOthers(String text) {
		String regulation = "[^\u4e00-\u9fa5]";  //ֻ�����ĵ��е������ַ�
		text = text.replaceAll(regulation, "");
		return text;
	}
	
	/**
	 * ���ķִ�
	 * @param text ��Ҫ������ַ���
	 * @return
	 */
	public static List<String> Segment(String text) {
		Analyzer anal = new IKAnalyzer(true);// �����ִʶ���
		StringReader reader = new StringReader(text); //������ҳ�������ݷִ�
		TokenStream ts = anal.tokenStream("", reader);// �ִ�
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		List<String> rawContent = new ArrayList<String>();
		try {
			while (ts.incrementToken()) {// �����ִ�����
				rawContent.add(term.toString());  //���ִʺ���ı�ת��Listȥɾ��ͣ�ô�
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		reader.close();
		return rawContent;
	}
}
