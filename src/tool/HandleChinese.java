package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
}
