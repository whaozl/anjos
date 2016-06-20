package AdminUi;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.portable.ValueBase;

import Base.BaseWordCut;
import Helper.FileHelper;
import Helper.TfIdfHelper;
import ICTCLAS.I3S.AC.ICTCLAS50;

/**
 * ��̨��������ѵ������ֱ�����м���
 * @author Administrator
 *
 */
public class TrainProcess extends BaseWordCut{
	public static int curFileIndex = 1;//���ڶ�ȡ���ļ�
	/**
	 * ����ѵ�����ִʺ��map
	 */
	HashMap<File, HashMap<String, Integer>> wordsMap = new HashMap<File, HashMap<String, Integer>>();
	/**
	 * wordsMap��Ӧ��tf-idfƵ��
	 */
	HashMap<File, HashMap<String, Double>> tfIdfMap = new HashMap<File, HashMap<String, Double>>();
	/**
	 * ����ѵ�������ɵĴʵ�
	 */
	HashMap<String, Integer> wordsDict = new HashMap<String,Integer>();
	
	public static HashMap<String, Integer> classLabel = new HashMap<String, Integer>();
	
	/*
	 * ��ʼ��   ��ȡ����ļ�
	 */
	public TrainProcess() throws IOException{
		classLabel = loadClassFromFile(new File("trainfile/classLabel.txt"));
	}
	
	/**
	 * ��ѵ�����ж�ȡ�ļ�
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private HashMap<File, String> readFile(String path) throws Exception{
		File baseDir = new File(path);
		File[] catDir = baseDir.listFiles();
		HashMap<File, String> articles = new HashMap<File,String>();
		for (File dir : catDir) {
			if(dir.isDirectory()){
				File[] files = dir.listFiles();
				System.out.print(dir.getName()+" ");
				for (File file : files) {
//					if(FileHelper.getFileExt(file).equals("txt")){
						BufferedReader reader = new BufferedReader(new FileReader(file));
						String temp = null;
						String content = "";
						while((temp = reader.readLine()) != null){
							content += temp;
						}
						articles.put(file, content);
						System.out.print(curFileIndex+" ");
						curFileIndex++;
//					}
				}
				System.out.println();
			}
		}
		return articles;
	}
	
	/**
	 * ͨ���ļ�������� �磺����_1.txt ���ڵ����Ϊ�����Ρ�
	 * @param file ����ѵ�����ļ��Ķ���
	 * @return
	 */
	private int getClassLabel(File file){
		//�ļ���Ŀ¼����������
		String className = file.getParentFile().getName();
		if (classLabel.containsKey(className)) {
			return classLabel.get(className);
		}else{
			return -1;
		}
	}
	
	
	public void cutWord(String path) throws Exception{
		HashMap<File, String> articles = readFile(path);
		Iterator<File> iterator = articles.keySet().iterator();
		while(iterator.hasNext()){
			File file = iterator.next();
			String content = articles.get(file);
			HashMap<String, Integer> temp = doCutWord(content);  //temp (�ؼ��� + ��Ƶ)
			this.wordsMap.put(file, temp);
			
//			System.out.println("�ļ�����"+file.getName());
//			System.out.println("�ļ����ݣ�"+content);
		}
	}

	/**
	 * �����ֵ䣬index item,�磺0 �й�
	 * @param file
	 */
	public void makeDictionary(File outFile){
		try {
			int index = 1;
			PrintWriter writer = new PrintWriter(outFile);
			Iterator<File> classIterator = wordsMap.keySet().iterator();
			while (classIterator.hasNext()) {
				File file = classIterator.next();
				//����=>��Ƶ
				HashMap<String, Integer> itemMap = wordsMap.get(file);
				Iterator<String> itemIterator = itemMap.keySet().iterator();
				while(itemIterator.hasNext()){
					String itemName = itemIterator.next();
					if(!wordsDict.containsKey(itemName)){
						wordsDict.put(itemName, index);
						writer.println(index+ " " +itemName);
						index ++ ;
					}
				}
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ת����libsvm�����ϸ�ʽ
	 */
	public void convertToSvmFormat(File outFile){
		try {
			TfIdfHelper tfIdfHelper = new TfIdfHelper(wordsMap);
			this.tfIdfMap = tfIdfHelper.calculate();
			
			PrintWriter writer = new PrintWriter(outFile);
			Iterator<File> classIterator = tfIdfMap.keySet().iterator();
			while (classIterator.hasNext()) {
				File file = classIterator.next();
				//����=>��Ƶ
				HashMap<String, Double> itemMap = tfIdfMap.get(file);
				Iterator<String> itemIterator = itemMap.keySet().iterator();
				writer.print(getClassLabel(file) + " ");
				System.out.print(getClassLabel(file) + " ");
				while(itemIterator.hasNext()){
					String itemName = itemIterator.next();
					int index = -1;
					if(wordsDict.containsKey(itemName)){
						index = wordsDict.get(itemName);
					}	
					System.out.print(index + ":" + itemMap.get(itemName) + " ");
					writer.print(index + ":" + itemMap.get(itemName) + " ");
				}
				System.out.println();
				writer.println();
				writer.flush();
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		Date begin = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(dateFormat.format(begin));
		
		TrainProcess model = new TrainProcess();
//		model.cutWord("article/");
		model.cutWord("E:\\article\\");
		System.out.println("���ڿ�ʼ�����ֵ�");
		model.makeDictionary(new File("trainfile/dictionary.txt"));//�������дʵ��ֵ�
		System.out.println("�ֵ��������");
		
		System.out.println("��ʼת����libsvm����");
		model.convertToSvmFormat(new File("trainfile/svm.train"));//������ת����libsvm��ģʽ
		System.out.println("ת�����");
		
		Date end = new Date();
		System.out.println(dateFormat.format(begin));
		System.out.println(dateFormat.format(end));
		int min = (int)(end.getTime() - begin.getTime())/(1000*60);
		System.out.println("��ʱ��"+min);
	}
	
	
	
	
	
}

