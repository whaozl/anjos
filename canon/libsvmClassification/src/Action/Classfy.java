package Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import svmHelper.svm_predict;
import svmHelper.svm_scale;

public class Classfy {

	/**
	 * ���÷���Ĺ����ӿ�
	 * @return �������б�
	 * @throws IOException
	 */
	public static ArrayList<Double> run() throws IOException{
		//���ࣺ�����ļ�+���д���
		String[] parg = {"testfile/svmscale.test","trainfile/svm.model","testfile/result.txt"};
		svm_predict.main(parg);
		
		//����Ĵ����Ƕ�ȡresult.txt�еĽ�����浽result��
		ArrayList<Double> result = new ArrayList<Double>();
		File file = new File("testfile/result.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = null;
		while((temp = reader.readLine()) != null){
			System.out.println(Double.parseDouble(temp));
			result.add(Double.parseDouble(temp));
		}
		return result;
	}
	
	/**
	 * ����������Ϣ
	 */
	private static HashMap<Double, String> classMap = new HashMap<>();
	private static void loadClassFromFile() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(new File("trainfile/classLabel.txt")));
		String temp = null;
		while((temp = reader.readLine()) != null){
			String[] str = temp.split(" ");
			classMap.put(Double.parseDouble(str[0]), str[1]);
			System.out.println(Double.parseDouble(str[0]) + " " + str[1]);
		}
	}
	
	private static String getClassByLabel(double label){
		if (classMap.containsKey(label)) {
			return classMap.get(label);
		}else{
			System.out.println(label);
			return "����";
		}
	}
	
	/**
	 * 1.����   2.���ش���[������+��Ӧ���ļ�list]��Map   
	 * @param sourceFiles  ԭʼ�ļ�����
	 * @return HashMap<String, ArrayList<File>> result �洢[������+��Ӧ���ļ�list]
	 * @throws IOException
	 */
	public static HashMap<String, ArrayList<File>> run(File[] sourceFiles) throws IOException{
		//������ʼ����
		String[] parg = {"testfile/svmscale.test","trainfile/svm.model","testfile/result.txt"};
		svm_predict.main(parg);
		
		/*
		 * �Է���������ת��:�����  <-> ���ǩ
		 */
		loadClassFromFile();
		//��result.txt��ȡ���������浽tempResult
		File file = new File("testfile/result.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		ArrayList<Double> tempResult = new ArrayList<>();
		String temp = null;
		while((temp = reader.readLine()) != null){
			double label = Double.parseDouble(temp);
			tempResult.add(label);
			System.out.println("label:"+label);
		}
		
//		System.out.println(sourceFiles.length);
//		System.out.println(tempResult.size());
		if (sourceFiles.length != tempResult.size()) {
			throw new IOException("Classify-->runfile,������ļ����鳤�Ȳ�ƥ��");
		}
		
		HashMap<String, ArrayList<File>> result = new HashMap<>();
		for(int i=0; i<tempResult.size(); i++){
			double label = tempResult.get(i);
			String className = getClassByLabel(label);
			//��result<String, ArrayList<File>>�У���ͬ���ǩ���ļ��Ƿ���һ��tmpList��
			if (!result.containsKey(className)) {   
				ArrayList<File> tmpList = new ArrayList<>();
				tmpList.add(sourceFiles[i]);
//				System.out.println("sourceFiles[i]:"+sourceFiles[i]);
				result.put(className, tmpList);
			}else{
				ArrayList<File> tmpList = result.get(className);
				tmpList.add(sourceFiles[i]);
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) throws IOException{
		run();
//		loadClassFromFile();
//		run(new File[]{new File("article"),new File("article"),new File("article")});
//
		File[] files = new File[]{
				new File("article2/���η���/zzfl_1.txt"),
				new File("article2/���η���/zzfl_2.txt"),
				new File("article2/����/ys_1.txt")
				};
		HashMap<String, ArrayList<File>> result=run(files);
		for(String key:result.keySet()){
			System.out.print(key+" : ");
			ArrayList<File> tmpList=result.get(key);
			for(File file:tmpList){
				System.out.println(file.getName());
			}
		}
	}
}
