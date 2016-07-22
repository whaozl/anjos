package Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;

import svmHelper.svm_scale;

import Base.BaseWordCut;
import Helper.FileHelper;
import Helper.TfIdfHelper;
import UserUi.HomeFrame;

public class WordCut extends BaseWordCut {
	/**
	 * ��ŷִʺ��������µĵ���
	 */
	private HashMap<File, HashMap<String, Integer>> articleWordsMap = new HashMap<File, HashMap<String, Integer>>();

	/**
	 * articleWorsMap��Ӧtf-idf��ʽ
	 */
	private HashMap<File, HashMap<String, Double>> tfIdfMap = new HashMap<File, HashMap<String, Double>>(); 
	
	/**
	 * �ֵ�����д����label 
	 */
	private HashMap<String, Integer> wordsDict = new HashMap<String, Integer>();
	
	private HashMap<String, Integer> classLabel = new HashMap<String, Integer>();
	
	/**
	 * ���������
	 */
	private HomeFrame homeFrame = null; 	
	public WordCut(HomeFrame homeFrame) throws IOException{
		this.homeFrame = homeFrame;
		this.loadWordsDict(new File("trainfile/dictionary.txt"));
		this.classLabel = super.loadClassFromFile(new File("trainfile/classLabel.txt"));
	}
	
	/**
	 * ���ļ��м��ص��� �ֵ�
	 * @param file
	 */
	private void loadWordsDict(File file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = null;
			while((temp = reader.readLine()) != null){
				String[] str = temp.split(" ");
				wordsDict.put(str[1], Integer.parseInt(str[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HashMap<File, String> readFile(File[] files) throws Exception{
		int curIndex = 0;
		HashMap<File, String> articles = new HashMap<File, String>();
		for (File file : files) {
			String content = FileHelper.readTxtOrDoc(file);
			articles.put(file, content);
			curIndex ++;
			if(homeFrame != null){
				homeFrame.updateProgressBar(curIndex);
			}
		}
		return articles;
	}
	
	/**
	 * ͨ���ļ���������� �磺����_1.txt ���ڵ����Ϊ�����Ρ�
	 * @param className
	 * @return
	 */
	private int getClassLabel(String className){
		String[] arr = className.split("_");
		if (classLabel.containsKey(arr[0])) {
			return classLabel.get(arr[0]);
		}else{
			return -1;
		}
	}
	
	/**
	 * ͨ���ļ��ĸ�Ŀ¼������� �磺����/1.txt ���ڵ����Ϊ�����Ρ�
	 * @param className
	 * @return
	 */
	private int getClassLabel(File file){
		//�ļ���Ŀ¼����������
//		System.out.println(file.getPath().toString());   //getPath() ��ȡ������·�� ��article\���η���\1.txt
//														 //getPath()����ֵ��һ��String
//		System.out.println(file.getParentFile().toString()); //getParentFile() ��ȡ��Ŀ¼��article\���η���
		                                                     //getParentFile()����ֵ��һ��file
		
		String className = file.getParentFile().getName();
		if (classLabel.containsKey(className)) {
			return classLabel.get(className);
		}else{
			return -1;
		}
	}
	
	/**
	 * ���������½��зִʴ���   �������ս������� articleWordsMap��file+artWords
	 * @param files
	 * @throws Exception
	 */
	private void cutWord(File[] files) throws Exception{
		HashMap<File, String> articles = this.readFile(files); 
		Iterator<File> artIterator = articles.keySet().iterator();
		while (artIterator.hasNext()) {
			File file = artIterator.next();
			String name = file.getName();
			String content = articles.get(file);
			HashMap<String, Integer> artWords = this.doCutWord(content);
			this.articleWordsMap.put(file, artWords);
//			System.out.println("��file��:"+file.getName());
//			System.out.println("��content����"+content);
//			for(String key:artWords.keySet()){
//				System.out.println("key:"+key+" value:"+artWords.get(key));
//			}
			//System.out.println(name);
		}
	}
	
	/**
	 * ת��svm���ϸ�ʽ
	 * @param outFile �����·��
	 */
	private void convertToSvmFormat(File outFile){
		try {
//			TfIdfHelper tfIdfHelper = new TfIdfHelper(articleWordsMap);
//			this.tfIdfMap = tfIdfHelper.calculate();
			PrintWriter writer = new PrintWriter(outFile);
			Iterator<File> artIterator = articleWordsMap.keySet().iterator();
			while (artIterator.hasNext()) {
				File file = artIterator.next();
				//д��svm���ϵ����� : Integer
				writer.print(getClassLabel(file) + " ");
//				System.out.println("[����Ϊ] "+getClassLabel(file) + " ");
				
//				writer.print(getClassLabel(file.getName()) + " ");
//				System.out.println(file.getParentFile().getName()+" ");
				
//				HashMap<String, Double> artWords = tfIdfMap.get(file);
				HashMap<String, Integer> artWords = articleWordsMap.get(file);
				Iterator<String> wordsIterator = artWords.keySet().iterator();
				while (wordsIterator.hasNext()) {
					String item = (String) wordsIterator.next();
					int index = -1;
					if(wordsDict.containsKey(item)){
						index = wordsDict.get(item);  //index ��ʾ�ùؼ�����wordDict�ʵ��ж�Ӧ�ġ���š�
						double tfIdf = artWords.get(item);  //ֱ���ô�Ƶ��Ϊ �ؼ��ʵ�tf-idfֵ
						//д��index��value
						writer.print(index + ":" + tfIdf + " ");
//						System.out.println(item+": "+index + ":" + tfIdf + " ");
					}				
				}
				writer.println();//д�뻻�з�
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ִʡ�����libsvm���ϵĹ����ӿ�
	 * ��svm���ϱ�����testfile/svm.test��
	 * @param files
	 * @return �������ɵ�svm.test�����ļ�
	 * @throws Exception
	 */
	public static File run(File[] files,HomeFrame homeFrame) throws Exception{
		WordCut model = new WordCut(homeFrame);
		model.cutWord(files);
		File outFile = new File("testfile/svmscale.test");
		model.convertToSvmFormat(new File("testfile/svm.test"));  //����ת��libsvm��ʽ����д�뵽svm.test
		//scale ����
		String[] sarg = {"-l","0","-r","trainfile/svm.scale","-o","testfile/svmscale.test","testfile/svm.test"};
		svm_scale.main(sarg);   //svm���ţ��渴��
		return outFile;
	}
	
	public static void main(String[] args) throws Exception{

//		File[] files = new File[]{
//		new File("article/���η���/1.txt"),
//		new File("article/���η���/2.txt"),
//		new File("article/����/1.txt")
//};
		
		File[] files = new File[]{
				new File("article2/���η���/zzfl_1.txt"),
				new File("article2/���η���/zzfl_2.txt"),
				new File("article2/����/ys_1.txt")
				};
		run(files,null);
//		BaseWordCut model = new BaseWordCut();
//		model.doCutWord("����һ���й��ˣ��Ұ���������ϲ��������Ͷ�����سǻ��(21-12)����ʤ��ֹ���ȷ��廢ֻ��1�˵÷���˫��Ǯ����-��ɭ˹15�֣��»���-������9��9�����壬ղķ˹[΢��]-����8�֣������[΢��]6��3�����塣�油�����İ�ķ��-��˹��15�֣���¡-��³��˹[΢��]17�֡�");
	}
	
	
	
}
