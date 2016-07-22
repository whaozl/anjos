package Widget;

import java.io.File;

import javax.swing.JButton;

public class ImageButton extends JButton{

	private File file;
	public ImageButton(File file){
		super();
		this.file = file;
	}
	
	/**
	 * ����file
	 * @param file
	 */
	public void setFile(File file){
		this.file = file;
	}
	
	/**
	 * �õ�file
	 * @return
	 */
	public File getFile(){
		return this.file;
	}
}
