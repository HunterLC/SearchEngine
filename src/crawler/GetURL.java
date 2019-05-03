package crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetURL {
	/**
	 * a标签中有四种值，需要判断
	 * 1.只有路径：/citylist.html
	 * 2.含有js代码：javascript:void(0)
	 * 3.网址全称：http://www.xuecheyi.com/Info/List-83.html
	 * 4.没有后缀/Info
	 */
	public static void getUrl(String uri,String filePath,String fileName) throws Exception {
		List<String> list = new ArrayList<>();// 用list来存放地址
		URL url = new URL(uri);
		String protocol = url.getProtocol();// 获取协议
		String host = url.getHost();// 获取域名
		Document doc = Jsoup.connect(uri).get();// dom解析html
		Elements ele = doc.getElementsByTag("a");// 获取网页中的a标签
		for (Element a : ele) {// 遍历
			String href = a.attr("href");
			String reg = "[a-zA-z]+://[^\\s]*";
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(href);
			if (m.find()) {// 通过正则表达式匹配了第三种http://jx.xuecheyi.com/member/login/index
				DuplicationCheck(list,href);
			} else if (href.indexOf("/") == 0) {// 匹配第一、四两种
				String NewURL = protocol + "://" + host + href;
				DuplicationCheck(list,NewURL);// /login/ind 0123456789 匹配出来的地址需要在前面加上协议和域名
			}
		}
		
		SaveFile(list,filePath,fileName);  //文件保存
		//return list;
	}

	
	/**
	 * URL去重
	 */
	public static void DuplicationCheck(List<String> list, String url) {
		boolean findURL = false;//查找标志位
		if(list.size()==0) //列表为空，则直接添加
			list.add(url);
		else {
			for(String item:list)
				if(item.equals(url)) {  //已经存在该URL
					findURL = true;
					break;
				}
			if(!findURL)
				list.add(url);
		}
	}
	
	/**
	 * 将URL利用文件保存起来
	 * @param list
	 */
	public static void SaveFile(List<String> list,String filePath,String fileName) {
		File file=new File(filePath);		
		if(!file.exists()){//如果文件夹不存在			
			file.mkdirs();//创建文件夹	
			//System.out.println("Done");
		}		
		try{//异常处理					
			BufferedWriter bw=new BufferedWriter(new FileWriter(filePath+"/"+fileName,true));
			for(String item:list)
				bw.write(item+"\n");			
			bw.close();//关闭文件		
			}catch(IOException e){			
				e.printStackTrace();		
		}

	}
}