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
	 * a��ǩ��������ֵ����Ҫ�ж�
	 * 1.ֻ��·����/citylist.html
	 * 2.����js���룺javascript:void(0)
	 * 3.��ַȫ�ƣ�http://www.xuecheyi.com/Info/List-83.html
	 * 4.û�к�׺/Info
	 */
	public static void getUrl(String uri,String filePath,String fileName) throws Exception {
		List<String> list = new ArrayList<>();// ��list����ŵ�ַ
		URL url = new URL(uri);
		String protocol = url.getProtocol();// ��ȡЭ��
		String host = url.getHost();// ��ȡ����
		Document doc = Jsoup.connect(uri).get();// dom����html
		Elements ele = doc.getElementsByTag("a");// ��ȡ��ҳ�е�a��ǩ
		for (Element a : ele) {// ����
			String href = a.attr("href");
			String reg = "[a-zA-z]+://[^\\s]*";
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(href);
			if (m.find()) {// ͨ��������ʽƥ���˵�����http://jx.xuecheyi.com/member/login/index
				DuplicationCheck(list,href);
			} else if (href.indexOf("/") == 0) {// ƥ���һ��������
				String NewURL = protocol + "://" + host + href;
				DuplicationCheck(list,NewURL);// /login/ind 0123456789 ƥ������ĵ�ַ��Ҫ��ǰ�����Э�������
			}
		}
		
		SaveFile(list,filePath,fileName);  //�ļ�����
		//return list;
	}

	
	/**
	 * URLȥ��
	 */
	public static void DuplicationCheck(List<String> list, String url) {
		boolean findURL = false;//���ұ�־λ
		if(list.size()==0) //�б�Ϊ�գ���ֱ�����
			list.add(url);
		else {
			for(String item:list)
				if(item.equals(url)) {  //�Ѿ����ڸ�URL
					findURL = true;
					break;
				}
			if(!findURL)
				list.add(url);
		}
	}
	
	/**
	 * ��URL�����ļ���������
	 * @param list
	 */
	public static void SaveFile(List<String> list,String filePath,String fileName) {
		File file=new File(filePath);		
		if(!file.exists()){//����ļ��в�����			
			file.mkdirs();//�����ļ���	
			//System.out.println("Done");
		}		
		try{//�쳣����					
			BufferedWriter bw=new BufferedWriter(new FileWriter(filePath+"/"+fileName,true));
			for(String item:list)
				bw.write(item+"\n");			
			bw.close();//�ر��ļ�		
			}catch(IOException e){			
				e.printStackTrace();		
		}

	}
}