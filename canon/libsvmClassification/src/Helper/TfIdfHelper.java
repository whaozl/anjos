package Helper;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class TfIdfHelper {
	
	/**
	 * ����ѵ�����ִʺ��map
	 */
	HashMap<File, HashMap<String, Integer>> wordsMap = new HashMap<File, HashMap<String, Integer>>();
	/**
	 * wordsMap��Ӧ��tf-idfƵ��
	 */
	HashMap  tfIdfMap = new HashMap<File, HashMap<String, Double>>();
	
	public TfIdfHelper(HashMap<File, HashMap<String, Integer>> wordsMap){
		this.wordsMap = wordsMap;
	}
	
	/**
	 * ���㵥�ʵ�tf = ��Ƶ/���´�����
	 * @param item
	 * @param article
	 * @return
	 */
	private double getTf(String item, HashMap<String, Integer> article){
		int count = article.get(item);//�ôʳ��ֵĴ��� ����Ƶ
		int sum = 0;//���еĴʵĴ���
		Iterator<String> iterator = article.keySet().iterator();
		while (iterator.hasNext()) {
			String itemName =  iterator.next();
			sum += article.get(itemName);
		}
		return ((double)count)/sum;
	}
	
	/**
	 * �����ĵ���idf
	 * @param item
	 * @return
	 */
	private double getIdf(String item){
		Iterator<File> fIterator = wordsMap.keySet().iterator();
		int count = 0;
		int sum = wordsMap.size();  //�ĵ�����
		while(fIterator.hasNext()){
			File file = fIterator.next();
			HashMap<String, Integer> itemMap = wordsMap.get(file);
			if(itemMap.containsKey(item)){
				count ++ ;
			}
		}
		return Math.log(((double)sum/(double)count));
	}
	
	/**
	 * ���㵥�ʵ�tf-idf
	 * @param item
	 * @param article
	 * @return
	 */
	private double getTfIdf(String item, HashMap<String, Integer> article){
		double tf = getTf(item, article);
		double idf = getIdf(item);
		return tf*idf;
	}
	
	/**
	 * �����е��ʽ���tfidf���㲢���ؼ���Ľ��
	 * @return
	 */
	public HashMap<File, HashMap<String, Double>> calculate(){
		Iterator<File> fIterator = wordsMap.keySet().iterator();
		while(fIterator.hasNext()){
			File file = fIterator.next();
			//ԭ����ÿƪ���µķִ�
			HashMap<String, Integer> article = wordsMap.get(file);
			//�����tf-idf��ķִ�
			HashMap<String, Double> tempMap = new HashMap<String, Double>();
			Iterator<String> itemIterator = article.keySet().iterator();
			while (itemIterator.hasNext()) {
				String item = itemIterator.next();
				double tfIdf = getTfIdf(item, article);
				tempMap.put(item, tfIdf);
			}
			tfIdfMap.put(file, tempMap);
		}
		
		return this.tfIdfMap;
	}
	
	
}
