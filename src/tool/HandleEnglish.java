package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleEnglish {
	
	public static List<String> STOPWORDS = new ArrayList<String>();  //ͣ�ôʱ�
	/**
	 * ��Ӣ����ҳ����ת��Сд������ͣ�ôʴ���
	 * ȥ���޹ط���
	 * @param file
	 * @return
	 */
	public static List<String> toLowerCaseWords(File file) {
		Scanner scanner = null;
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		String text = "";
		List<String> wordsList = new ArrayList<>();  //�����б�
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (scanner != null) {
			while (scanner.hasNextLine()) {
				text += scanner.nextLine();
			}
			scanner.close();
		}
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			wordsList.add(matcher.group().toLowerCase()); //תСд
		}
		System.out.println(wordsList.size());
		return wordsList;
	}

	/**
	 * ����Ϣ���浽�ļ���
	 * @param wordsList
	 * @param file
	 */
	public static void HandleFile(List<String> wordsList, File file) {
		//Collections.sort(wordsList);// ����
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			for (String word : wordsList) {
				writer.write(word + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ����Ӣ��ͣ�ôʱ����ɾ������
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
		File in = new File("F:/Eclipse/eclipse/code/SearchEngine/E_StopWord.txt"); //��ȡӢ��ͣ�ôʱ�
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
