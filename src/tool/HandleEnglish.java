package tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleEnglish {
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
	 * 将转为小写的网页信息保存到文件里
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
	
	public static List<String> DeleteStopWord(List<String> wordsList) {
		
		return wordsList;
	}
}
