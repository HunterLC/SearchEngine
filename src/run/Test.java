package run;

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

import crawler.WebDownload;
import tool.HandleChinese;
import tool.HandleEnglish;
import tool.HandleHTML;
import tool.PorterStemmer;

public class Test {

	/**
	 * ��Ӣ����վ������
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			WebDownload.DownloadPage("https://www.apple.com/","http://www.china.com.cn/" );//����Ӣ����վ��������վ
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		//������վԭ��ַ���ر���λ�á���������������λ��
		String htmlPathC="F:/Eclipse/eclipse/code/SearchEngine/WebPage/ChinesePage";
		String nameC="Web_C_";
		String savePathC="F:/Eclipse/eclipse/code/SearchEngine/WebPage/HandledChinesePage";
		
		//Ӣ����վԭ��ַ���ر���λ�á���������������λ��
		String htmlPathE="F:/Eclipse/eclipse/code/SearchEngine/WebPage/EnglishPage";
		String nameE="Web_E_";
		String savePathE="F:/Eclipse/eclipse/code/SearchEngine/WebPage/HandledEnglishPage";
		
		File[] list1 = new File(htmlPathC).listFiles();  //ͳ��ָ������Ŀ¼���ļ���
		int fileCount1 = 0;
		for(File file:list1)
			if(file.isFile())
				fileCount1++;
		
		File[] list2 = new File(htmlPathE).listFiles();  //ͳ��ָ��Ӣ��Ŀ¼���ļ���
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
		HandleChinese.InitStopWord();//��ʼ������ͣ�ôʱ�
		for(int i = 1; i <= fileCount1; i++) {
			String htmlName = nameC+i+".txt";
			System.out.println("���ڶ�"+htmlName+"���б�ǩ�޳����ִʴ���");
			File out = new File(savePathC+"/handled_"+htmlName);  //����ļ�
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"utf-8"));
				String text = HandleHTML.HandleFile(htmlPathC, htmlName);  //�����ҳ���ı�
				text = HandleChinese.RemoveOthers(text);  //����������
				List<String> rawContent = HandleChinese.Segment(text); //���ķִ�
				rawContent = HandleChinese.DeleteStopWord(rawContent); //ɾ������ͣ�ô�
				for(String item:rawContent)//�����ļ��ı���
					try {
						bw.write(item + "\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				System.out.println(htmlName+"�������");
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
		HandleEnglish.InitStopWord();//��ʼ��Ӣ��ͣ�ôʱ�
		for (int i = 1; i <= fileCount2; i++) {
			String htmlName = nameE + i + ".txt";
			System.out.println("���ڶ�" + htmlName + "���б�ǩ�޳����ʸ�����");
			File out = new File(savePathE + "/handled_" + htmlName); // ����ļ�
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
				//Ӣ����ҳתСд��ɾȥ���÷���
				//ɾ��ͣ�ô�
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
				System.out.println(htmlName + "�������");
				temp.delete();//ɾ���м��ĵ�
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//��Ӣ�ĵ��ʽ���PorterStemming
				PorterStemmer.HandlePorterStemmer(savePathE, "/handled_" + htmlName);
				
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
