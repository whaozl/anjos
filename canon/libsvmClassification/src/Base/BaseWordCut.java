package Base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Action.WordCut;
import ICTCLAS.I3S.AC.ICTCLAS50;

public class BaseWordCut {
	
	public ICTCLAS50 ictclas50;
	public BaseWordCut(){
		init();
	}
	
	/**
	 * ��ʼ���ִ�
	 */
	private void init(){
		try {
			ictclas50 = new ICTCLAS50();
			String argu = ".";
			if (ictclas50.ICTCLAS_Init(argu.getBytes("gbk"))==false) {
				System.out.println("init false");
			}else{
				System.out.println("init true");
			}
			//���ô��Ա�ע��(0 ������������ע����1 ������һ����ע����2 ���������ע����3 ����һ����ע��)
			ictclas50.ICTCLAS_SetPOSmap(2);
			String userdict = "userdict.txt";
			int nCount = ictclas50.ICTCLAS_ImportUserDictFile(userdict.getBytes(), 0);
			System.out.println("�û��ʵ���Ŀ��"+nCount);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * �ͷŷִ�
	 */
	public void releaseWord(){
		//�����û��ֵ�
		ictclas50.ICTCLAS_SaveTheUsrDic();
		//�ͷŷִ������Դ
		ictclas50.ICTCLAS_Exit();
	}
	
	/**
	 * ����ִʣ�����item:value��hashmap
	 * @param content
	 * @return
	 */
	public HashMap<String, Integer> doCutWord(String content){
		
		//content �ִ�ǰÿһƪ���µ��������ݣ����±��⣬ժҪ��ʱ�䣬�ؼ��ʣ����ģ�����
		//���̣��ִ�  ->  �޳����ô�  ->  ��ʣ�¹ؼ��ʽ��д�Ƶͳ��  ->  ���ؼ���+��Ƶ���뵽resultMap��
		//resultMap �洢���յķִʽ������ʽΪ���ؼ���+��Ƶ  => String+Integer
		
		HashMap<String, Integer> resultMap = new HashMap<String,Integer>();
		try {
			byte[] nativeBytes1 = ictclas50.ICTCLAS_ParagraphProcess(content.getBytes("gbk"), 0, 1);
			String nativeStr1 = new String(nativeBytes1);  //nativeStr1 �洢���ηִʺ�����ݣ�������
//			System.out.println(nativeStr1);
			
//			Pattern pattern = Pattern.compile("( ([^ ])*?)(/n(\\w)*) ");
//			Matcher matcher = pattern.matcher(nativeStr1);	
//			while (matcher.find()) {
//				String word = matcher.group(1).trim();
//				if(resultMap.containsKey(word)){//��������ڸ������Ӹ�����Ѵ�Ƶ��Ϊ1
//					resultMap.put(word, resultMap.get(word)+1);
//				}else{//����Ѿ����ڸ�����Ƶ+1
//					resultMap.put(word, 1);
//				}
//			}
			
//			String[] arr = nativeStr1.split(" ");
//			for (String temp : arr) {
//				String[] wt = temp.split("/");
//				if (wt.length != 2) {
//					continue;
//				}
//				String item = wt[0];
//				String ext = wt[1];
//				if (ext.startsWith("n") || ext.startsWith("un") || ext.startsWith("v")) {
//					addWord(resultMap,item.trim());
//				}
//			}
			//System.out.println("nativeStr1:"+nativeStr1);
			
			String[] arr = nativeStr1.split(" ");
			for (String temp : arr) {
				String[] wt = temp.split("/");
				if (wt.length != 2) {
					continue;
				}
				String item = wt[0]; //item ��ʾ�ؼ���
				String ext = wt[1];  //ext ��ʾ����
				//ѡ�����Ϊ[����]��[����]��[un]�Ĺؼ��ʣ��������޳���
				if (ext.startsWith("n") || ext.startsWith("un") || ext.startsWith("v")) {
					addWord(resultMap,item.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return resultMap;
	}
	
	/**
	 * ��resultMap �����word
	 * @param resultMap
	 * @param word
	 */
	private void addWord(HashMap<String, Integer> resultMap,String word){
//		System.out.println(word);
		if(resultMap.containsKey(word)){//����Ѿ����ڸ�����Ƶ+1
			resultMap.put(word, resultMap.get(word)+1);
		}else{//��������ڸ������Ӹ�����Ѵ�Ƶ��Ϊ1
			resultMap.put(word, 1);
		}
	}
	
	/**
	 * ���ļ��м��ط���������Ϣ�����ظ�ʽ�� �����=>����
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public HashMap<String, Integer> loadClassFromFile(File file) throws IOException{
		HashMap<String, Integer> result = new HashMap<String,Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = null;
		while((temp = reader.readLine()) != null){
			String[] str = temp.split(" ");
			result.put(str[1], Integer.parseInt(str[0]));
			System.out.println(str[1] + " " + str[0]);
		}
		return result;
	}
}
