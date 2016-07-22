package wordsimilarity;

public class WordSimilarityTests {
	public static void main(String[] args) {
		WordSimilarity sim = new WordSimilarity();
		sim.loadGlossary();
		//test_disPrimitive(sim);
		//test_simPrimitive(sim);
		test_simWord(sim);
	}
	
    /**
     * test the method {@link WordSimilarity#disPrimitive(String, String)}.
     */
    public static void test_disPrimitive(WordSimilarity sim){
        int dis = sim.disPrimitive("����", "�׶�");
        System.out.println("���� and �׶� dis : "+ dis);
    }
    
    public static void test_simPrimitive( WordSimilarity sim ){
        double simP = sim.simPrimitive("����", "�׶�");
        System.out.println("���� and �׶� sim : "+ simP);
    }
    
    public static void test_simWord( WordSimilarity sim ){
   	 System.out.println( "������=������"+sim.getSimilarity("������", "������") );
	 
     System.out.println("����=����"+sim.getSimilarity("����", "����"));
     
     System.out.println("����=Ⱥ��"+sim.getSimilarity("����", "Ⱥ��"));
     
     System.out.println("����=����"+sim.getSimilarity("����", "����"));
     
     System.out.println("����=ͬ־"+sim.getSimilarity("����", "ͬ־"));
     
     System.out.println("����=����"+sim.getSimilarity("����", "����"));
     
     System.out.println("����=־Ը��"+sim.getSimilarity("����", "־Ը��"));
    
    System.out.println("����=��ϸ"+sim.getSimilarity("����", "��ϸ"));
    
    System.out.println("����=����"+sim.getSimilarity("����", "����"));
    
    System.out.println("����=�ʻ�"+sim.getSimilarity("����", "�ʻ�"));
    
    System.out.println("����Ч��=��������"+sim.getSimilarity("����Ч��", "��������"));
    
    System.out.println("����=����Ч��"+sim.getSimilarity("����", "����Ч��"));
    
    System.out.println("�Ϻ�=ħ��"+sim.getSimilarity("�Ϻ�", "ħ��"));
    
    System.out.println("���±�=����Ч��"+sim.getSimilarity("���±�", "����Ч��"));
    
    System.out.println("���±�=����"+sim.getSimilarity("���±�", "����"));
    
    System.out.println("����Ч��=����ʱ��"+sim.getSimilarity("����Ч��", "����ʱ��"));
    
    System.out.println("���¶�=����Ч��"+sim.getSimilarity("����Ч��", "���¶�"));
    
    System.out.println("����=����Ч��"+sim.getSimilarity("����Ч��", "����"));
    
    System.out.println("����=���¶�"+sim.getSimilarity("����", "���¶�"));
    
    System.out.println("���¶�=��������"+sim.getSimilarity("��������", "���¶�"));
    
    System.out.println("���±�=������"+sim.getSimilarity("���±�", "������"));
    
    System.out.println("����=����"+sim.getSimilarity("����", "����"));
    
    System.out.println("����=����"+sim.getSimilarity("����", "����"));
    
    System.out.println("����=ʱ��"+sim.getSimilarity("����", "ʱ��"));
    
    System.out.println("������Ӻܺÿ�=������ۺ�Ư��"+sim.getSimilarity("������Ӻܺÿ�", "������ۺ�Ư��"));
    
    System.out.println("���ӱ���Ч�������=���ӱ������ܺܰ���"+sim.getSimilarity("���ӱ���Ч�������", "���ӱ������ܺܰ���"));
    
    System.out.println("�������=������ۺ�Ư��"+sim.getSimilarity("�������", "������ۺ�Ư��"));
    
    System.out.println("���ӱ���Ч���治��=���ӱ��·ǳ���"+sim.getSimilarity("���ӱ���Ч���治��", "���ӱ��·ǳ���"));
    
    System.out.println("���ӱ���Ч�����=���ӱ��·ǳ���"+sim.getSimilarity("���ӱ���Ч�����", "���ӱ��·ǳ���"));
    
    System.out.println("���Ӻܱ���=���Ӻܳ�"+sim.getSimilarity("���Ӻܱ���", "���Ӻܳ�"));
    
	System.out.println("ʪ=��" + sim.getSimilarity("ʪ", "��"));
	
	 System.out.println("��=��"+sim.getSimilarity("��", "��"));
	
	 System.out.println("�ܺ�=��"+sim.getSimilarity("�ܺ�", "��"));
	 
	 System.out.println("�ǳ���=�ܺ�"+sim.getSimilarity("�ǳ���", "�ܺ�"));
	 
	 System.out.println("�ǳ���=��"+sim.getSimilarity("�ǳ���", "��"));
	 
	 System.out.println("����=��"+sim.getSimilarity("����", "��"));
	 
	 System.out.println("�ǳ���=�ǳ���"+sim.getSimilarity("�ǳ���", "�ǳ���"));
	 
	 System.out.println("�ǳ���=�ܺ�"+sim.getSimilarity("�ǳ���", "�ܺ�"));
	 
	 System.out.println("�ǳ���=����"+sim.getSimilarity("�ǳ���", "����"));
	 
	 System.out.println("�ǳ���=��"+sim.getSimilarity("�ǳ���", "��"));

	 System.out.println("����=�ܺ�"+sim.getSimilarity("����", "�ܺ�"));
	 
	 System.out.println("����=�ǳ���"+sim.getSimilarity("����", "�ǳ���"));
	 
	 System.out.println("����=����"+sim.getSimilarity("����", "����"));
	 
	 System.out.println("����=�ܲ�"+sim.getSimilarity("����", "�ܲ�"));
	 
	 System.out.println("����=��"+sim.getSimilarity("����", "��"));
	 
	 System.out.println("һ��=��"+sim.getSimilarity("һ��", "��"));
    }
    
}
