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
	
	public static List<String> STOPWORDS = new ArrayList<String>();  //停用词表
	/**
	 * 把英文网页内容转成小写，便于停用词处理
	 * 去掉无关符号
	 * @param file
	 * @return
	 */
	public static List<String> toLowerCaseWords(File file) {
		Scanner scanner = null;
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		String text = "";
		List<String> wordsList = new ArrayList<>();  //单词列表
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
			wordsList.add(matcher.group().toLowerCase()); //转小写
		}
		System.out.println(wordsList.size());
		return wordsList;
	}

	/**
	 * 将信息保存到文件里
	 * @param wordsList
	 * @param file
	 */
	public static void HandleFile(List<String> wordsList, File file) {
		//Collections.sort(wordsList);// 排序
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
	 * 根据英文停用词表进行删除处理
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
		File in = new File("F:/Eclipse/eclipse/code/SearchEngine/E_StopWord.txt"); //读取英文停用词表
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
}
