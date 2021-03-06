package crawler;

import java.net.*;
import java.io.*;
public class WebDownload {
	private String webURL; //网页URL
	private String webName; //网页文件命名
	
	public WebDownload(String webURL,String webName) {
		this.webURL = webURL;
		this.webName = webName;
	}
	
	public int DownLoad(int count) throws Exception {
		try {
			URL webPage = new URL(webURL);  //构造URL
			URLConnection con = webPage.openConnection();
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
			String content = "";
			String line = "";
			while((line = urlReader.readLine())!=null) {
				content += line;
				content += "\n";
			}
			urlReader.close();
			File out = new File(webName);  //输出临时文件
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"utf-8"));
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			count--;
		}
		return count;
	}
	
	public static void DownloadPage(String urlE,String urlC) throws Exception {

		String urlFilePath = "F:/Eclipse/eclipse/code/SearchEngine/WebPage/URL";
		String urlFileNameE = "URL1.txt";
		String urlFileNameC = "URL2.txt";
		GetURL.getUrl(urlE,urlFilePath,urlFileNameE);
		GetURL.getUrl(urlC,urlFilePath,urlFileNameC);
		
		//创建中英文网页文档目录
		String webFilePathE = "F:/Eclipse/eclipse/code/SearchEngine/WebPage/EnglishPage";
		String webFilePathC = "F:/Eclipse/eclipse/code/SearchEngine/WebPage/ChinesePage";
		File file1=new File(webFilePathE);		
		if(!file1.exists()){//如果文件夹不存在			
			file1.mkdirs();//创建文件夹	
			System.out.println("创建文件夹成功");
		}
		File file2=new File(webFilePathC);
		if(!file2.exists()){//如果文件夹不存在			
			file2.mkdirs();//创建文件夹	
			System.out.println("创建文件夹成功");
		}
		
		//下载url中的网站内容
		for (int i = 1; i <= 2; i++) {
			File urlFile = new File(urlFilePath + "/URL"+i+".txt");
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(new FileInputStream(urlFile), "utf-8"));
			int count = 0;
			while (true) {
				try {
					String line = urlReader.readLine();
					if (line == null)
						break;
					else {
						System.out.println("正在从"+ line + "下载内容");
						String webName = null;
						if(i == 1)
							webName = webFilePathE+"/Web_E_" + (++count)+ ".txt";
						else if(i == 2)
							webName = webFilePathC+"/Web_C_" + (++count)+ ".txt";
						WebDownload web = new WebDownload(line, webName);
						count = web.DownLoad(count);  //下载网页，如果有异常则回滚该编号
						if(i == 1)
							System.out.println("Web_E_"+count + "下载完毕");
						else if(i == 2)
							System.out.println("Web_C_"+count + "下载完毕");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			urlReader.close();
		}
	}
}
