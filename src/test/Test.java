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
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import tool.HandleEnglish;
import tool.HandleHTML;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String htmlPathC="F:/Eclipse/eclipse/code/SearchEngine/WebPage/ChinesePage";
		String nameC="Web_C_";
		String savePathC="F:/Eclipse/eclipse/code/SearchEngine/WebPage/HandledChinesePage";
		
		String htmlPathE="F:/Eclipse/eclipse/code/SearchEngine/WebPage/EnglishPage";
		String nameE="Web_E_";
		String savePathE="F:/Eclipse/eclipse/code/SearchEngine/WebPage/HandledEnglishPage";
		
		File[] list1 = new File(htmlPathC).listFiles();  //ͳ��ָ��Ŀ¼���ļ���
		int fileCount1 = 0;
		for(File file:list1)
			if(file.isFile())
				fileCount1++;
		
		File[] list2 = new File(htmlPathE).listFiles();  //ͳ��ָ��Ŀ¼���ļ���
		int fileCount2 = 0;
		for(File file:list2)
			if(file.isFile())
				fileCount2++;
		
		File file1=new File(savePathC);		
		if(!file1.exists()){//����ļ��в�����			
			file1.mkdirs();//�����ļ���	
			System.out.println("Done");
		}
		File file2=new File(savePathE);		
		if(!file2.exists()){//����ļ��в�����			
			file2.mkdirs();//�����ļ���	
			System.out.println("Done");
		}
		/***********************������ҳԤ����****************************/
		//����������ҳ
		for(int i = 1; i <= fileCount1; i++) {
			String htmlName = nameC+i+".txt";
			System.out.println("���ڶ�"+htmlName+"���б�ǩ�޳����ִʴ���");
			File out = new File(savePathC+"/handled_"+htmlName);  //�����ʱ�ļ�
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"utf-8"));
				Analyzer anal = new IKAnalyzer(true);// �����ִʶ���
				String text = HandleHTML.HandleFile(htmlPathC, htmlName);
				String reg = "[^\u4e00-\u9fa5]";  //ֻ�����ĵ��е������ַ�
				text = text.replaceAll(reg, "");
				StringReader reader = new StringReader(text); //������ҳ�������ݷִ�
				TokenStream ts = anal.tokenStream("", reader);// �ִ�
				CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
				try {
					while (ts.incrementToken()) {// �����ִ�����
						bw.write(term.toString() + "\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				System.out.println(htmlName+"�������");
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
		
		/***********************Ӣ����ҳԤ����****************************/
		// ����Ӣ����ҳ
		for (int i = 1; i <= fileCount2; i++) {
			String htmlName = nameE + i + ".txt";
			System.out.println("���ڶ�" + htmlName + "���б�ǩ�޳����ִʴ���");
			File out = new File(savePathE + "/handled_" + htmlName); // �����ʱ�ļ�
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
				//��StringתΪFile
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
				List<String> wordsList = HandleEnglish.toLowerCaseWords(temp);//Ӣ����ҳתСд��ɾȥ���÷��ţ�����ͣ�ô�ɾ������
				System.out.println(wordsList.size());
				for (String word : wordsList) {
					try {
						System.out.println(word);
						bw.write(word + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(htmlName + "�������");
				temp.delete();//ɾ���м��ĵ�
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
