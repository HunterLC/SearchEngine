package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import tool.HandleChinese;
import tool.HandleEnglish;
import tool.HandleHTML;

public class Test {

	/**
	 * 中英文网站处理函数
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String htmlPathC="F:/Eclipse/eclipse/code/SearchEngine/WebPage/ChinesePage";
		String nameC="Web_C_";
		String savePathC="F:/Eclipse/eclipse/code/SearchEngine/WebPage/HandledChinesePage";
		
		String htmlPathE="F:/Eclipse/eclipse/code/SearchEngine/WebPage/EnglishPage";
		String nameE="Web_E_";
		String savePathE="F:/Eclipse/eclipse/code/SearchEngine/WebPage/HandledEnglishPage";
		
		File[] list1 = new File(htmlPathC).listFiles();  //统计指定目录下文件数
		int fileCount1 = 0;
		for(File file:list1)
			if(file.isFile())
				fileCount1++;
		
		File[] list2 = new File(htmlPathE).listFiles();  //统计指定目录下文件数
		int fileCount2 = 0;
		for(File file:list2)
			if(file.isFile())
				fileCount2++;
		
		File file1=new File(savePathC);		
		if(!file1.exists()){//如果文件夹不存在			
			file1.mkdirs();//创建文件夹	
			System.out.println("Done");
		}
		File file2=new File(savePathE);		
		if(!file2.exists()){//如果文件夹不存在			
			file2.mkdirs();//创建文件夹	
			System.out.println("Done");
		}
		/***********************中文网页预处理****************************/
		//处理中文网页
		HandleChinese.InitStopWord();//初始化中文停用词表
		for(int i = 1; i <= fileCount1; i++) {
			String htmlName = nameC+i+".txt";
			System.out.println("正在对"+htmlName+"进行标签剔除、分词处理");
			File out = new File(savePathC+"/handled_"+htmlName);  //输出临时文件
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"utf-8"));
				Analyzer anal = new IKAnalyzer(true);// 创建分词对象
				String text = HandleHTML.HandleFile(htmlPathC, htmlName);
				String reg = "[^\u4e00-\u9fa5]";  //只保留文档中的中文字符
				text = text.replaceAll(reg, "");
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
				rawContent = HandleChinese.DeleteStopWord(rawContent); //删除中文停用词
				for(String item:rawContent)
					try {
						bw.write(item + "\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				System.out.println(htmlName+"处理完毕");
				reader.close();
				try {
					bw.flush();
					bw.close();
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
			
		}	
		
		/***********************英文网页预处理****************************/
		// 处理英文网页
		HandleEnglish.InitStopWord();//初始化英语停用词表
		for (int i = 1; i <= fileCount2; i++) {
			String htmlName = nameE + i + ".txt";
			System.out.println("正在对" + htmlName + "进行标签剔除、分词处理");
			File out = new File(savePathE + "/handled_" + htmlName); // 输出临时文件
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
				//将String转为File
				String text = HandleHTML.HandleFile(htmlPathE, htmlName);
				File temp = new File(savePathE+"/"+"WEB.txt");
				FileWriter rTemp;
				try {
					rTemp = new FileWriter(temp);
					rTemp.write(text);
					rTemp.flush();
					rTemp.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//英文网页转小写并删去无用符号
				//删除停用词
				List<String> wordsList = HandleEnglish.DeleteStopWord(HandleEnglish.toLowerCaseWords(temp));
				System.out.println(wordsList.size());
				for (String word : wordsList) {
					try {
						bw.write(word + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(htmlName + "处理完毕");
				temp.delete();//删除中间文档
				try {
					bw.flush();
					bw.close();
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
		}
	}
}
