import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class FileSparationMerge {

	public static void main(String args[]) throws IOException, ClassNotFoundException {
		//�ָ�
		Sparation sp= new Sparation(2048,"e:/test/001.jpg","e:/test/part/");
		//�ϲ�
		merge mg = new merge("e:/test/part/","e:/test/merge/001.jpg");
		//���part�ļ�
		//deleteTempFie("e:/test/part/");
	}

	public static void deleteTempFie(String path){
		File tempFile = new File(path);
		File [] Files = tempFile.listFiles();
		for(int i=0;i<Files.length;i++){
			Files[i].delete();
		}
	}

}

class Sparation {
	/*
	 * param blockNum �����Ŀ
	 * param blockNum ��Ĵ�С
	 * param srcPath  �����Դ·��
	 * param destPath ��Ĵ��·�����ļ���
	 * param length   �ļ���С 
	 */
	protected int blockNum;
	protected int blockSize;
	protected File src;
	protected File dest;
	protected long length; 
	protected List <String>  blockName;
	public Sparation(){
		
	}
	public Sparation(String srcPath, String destPath) throws IOException{
		this(1024,srcPath,destPath);
	}
	public Sparation(int blockSize, String srcPath, String destPath) throws IOException {
		this();
		this.blockSize = blockSize;
		this.src   = new File(srcPath);
		this.dest  = new File(destPath);
		init();
	}
	public void init() throws IOException{
		length =  src.length();
		
		if(this.blockSize>this.length){
			this.blockSize=(int) this.length;
		}
		this.blockNum =(int)Math.ceil((length*1.0)/this.blockSize);
		
		System.out.println(this.blockSize);
		System.out.println(this.length);
		System.out.println(this.blockNum);
		sparat(this.src,this.dest);
	}

	void sparat(File src,File dest ) throws IOException{
		//Դ src dest
		
		//��
		RandomAccessFile raf;
		BufferedOutputStream  bos ;
		long position=0;
		
		//��������ͳ���
		int len=0;
		byte [] flush = new byte[1024];
		
		try {
			raf = new RandomAccessFile(src,"r");
			long reallSize=0;
			for(int i = 0 ;i<this.blockNum;i++){
			position = i*this.blockSize;
				raf.seek(position);
			if(i<this.blockNum-1)
			{
				 reallSize=this.blockSize;
			}
			else
			{
				reallSize = (this.length-(i)*this.blockSize);
			}
				bos = new BufferedOutputStream(new FileOutputStream(new File(dest,src.getName()+".part"+i),true));
				
				while(-1!=(len = raf.read(flush))){
					//System.out.println(new String(flush));
					if(reallSize>len){
						bos.write(flush,0,len);
						reallSize -=len;
					}
					else{
						bos.write(flush,0,(int)reallSize);
						break;
					}
					bos.flush();
				}
				bos.close();
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}

class merge{
	/*
	 * param destPath �ϳɵĸ�·��
	 * param srcPath  �ϳɵ��ļ�·��
	 * 
	 * 
	 */
	
	protected File src;
	protected File dest;
	public merge(String srcPath,String destPath) throws IOException{
		this.dest= new File(destPath);
		this.src = new File(srcPath);
		merger();
	}
	public void merger() throws IOException{
		//����Ŀ¼�µ�������Ҫ�ϳɵ��ļ�
		if(!src.isDirectory())
		{
			System.err.println("��Ҫ�ϳɵ��ļ�Ŀ¼����");
			return ;
		}
		//Դ
		File [] mergeFile = src.listFiles(); 
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest,true));
		BufferedInputStream bis;
		File mergeTemp=null;
		for(int i=0;i<mergeFile.length;i++){
			//Ѱ����Ҫ�ϲ����ļ�˳��
			for(int k=0;k<mergeFile.length;k++)
			{
				if(mergeFile[k].getName().endsWith(".part"+i)){
					mergeTemp = mergeFile[k];
				}		
			}
			bis = new BufferedInputStream(new FileInputStream(mergeTemp));
			//��������ͳ���
			byte[] tempByte = new byte[1024];
			System.out.println(mergeFile[i].getName());
			int len=0;
			while(-1!=(len = bis.read(tempByte))){
				bos.write(tempByte, 0, len);
				bos.flush();
			}
			bis.close();
		}
		bos.close();
	}
	
	
	
}
