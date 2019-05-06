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
	public static List<String> STOPWORDS = new ArrayList<String>();  //停用词表
	/**
	 * 根据中文停用词表进行删除处理
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
	 * 初始化停用词表
	 */
	public static void InitStopWord() {
		File in = new File("F:/Eclipse/eclipse/code/SearchEngine/C_StopWord.txt"); //读取中文停用词表
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(in),"utf-8"));
			while(true){
				try {
					String line = br.readLine();
					if(line == null)
						break;
					STOPWORDS.add(line); //添加停用词
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
	 * 只保留文档中的中文字符
	 * @param text 需要处理的字符串
	 * @return
	 */
	public static String RemoveOthers(String text) {
		String regulation = "[^\u4e00-\u9fa5]";  //只保留文档中的中文字符
		text = text.replaceAll(regulation, "");
		return text;
	}
	
	/**
	 * 中文分词
	 * @param text 需要处理的字符串
	 * @return
	 */
	public static List<String> Segment(String text) {
		Analyzer anal = new IKAnalyzer(true);// 创建分词对象
		StringReader reader = new StringReader(text); //将纯网页中文内容分词
		TokenStream ts = anal.tokenStream("", reader);// 分词
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		List<String> rawContent = new ArrayList<String>();
		try {
			while (ts.incrementToken()) {// 遍历分词数据
				rawContent.add(term.toString());  //将分词后的文本转成List去删除停用词
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		reader.close();
		return rawContent;
	}
}
