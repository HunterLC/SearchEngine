package crawler;

import java.net.*;
import java.io.*;
public class WebDownload {
	private String webURL; //��ҳURL
	private String webName; //��ҳ�ļ�����
	
	public WebDownload(String webURL,String webName) {
		this.webURL = webURL;
		this.webName = webName;
	}
	
	public void DownLoad() throws IOException {
		try {
			URL webPage = new URL(webURL);  //����URL
			URLConnection con = webPage.openConnection();
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
			String content = "";
			String line = "";
			while((line = urlReader.readLine())!=null) {
				content += line;
				content += "\n";
			}
			urlReader.close();
			File out = new File(webName);  //�����ʱ�ļ�
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"utf-8"));
			bw.write(content);
			bw.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		//����URL�����浽ָ��λ��
		String urlE = "https://www.apple.com"; //Ӣ����վ
		String urlC = "https://www.163.com";//������վ
		String urlFilePath = "F:/Eclipse/eclipse/code/SearchEngine/WebPage/URL";
		String urlFileNameE = "URL1.txt";
		String urlFileNameC = "URL2.txt";
		GetURL.getUrl(urlE,urlFilePath,urlFileNameE);
		GetURL.getUrl(urlC,urlFilePath,urlFileNameC);
		
		//������Ӣ����ҳ�ĵ�Ŀ¼
		String webFilePathE = "F:/Eclipse/eclipse/code/SearchEngine/WebPage/EnglishPage";
		String webFilePathC = "F:/Eclipse/eclipse/code/SearchEngine/WebPage/ChinesePage";
		File file1=new File(webFilePathE);		
		if(!file1.exists()){//����ļ��в�����			
			file1.mkdirs();//�����ļ���	
			System.out.println("Done");
		}
		File file2=new File(webFilePathC);
		if(!file2.exists()){//����ļ��в�����			
			file2.mkdirs();//�����ļ���	
			System.out.println("Done");
		}
		for (int i = 1; i >= 1; i--) {
			File urlFile = new File(urlFilePath + "/URL"+i+".txt");
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(new FileInputStream(urlFile), "utf-8"));
			int count = 0;
			while (true) {
				try {
					String line = urlReader.readLine();
					if (line == null)
						break;
					else {
						String webName = null;
						if(i == 1)
							webName = webFilePathE+"/Web_E_" + (++count)+ ".txt";
						else if(i == 2)
							webName = webFilePathC+"/Web_C_" + (++count)+ ".txt";
						WebDownload web = new WebDownload(line, webName);
						web.DownLoad();
						System.out.println(count + "�������");
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
