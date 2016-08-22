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
		//分割
		Sparation sp= new Sparation(2048,"e:/test/001.jpg","e:/test/part/");
		//合并
		merge mg = new merge("e:/test/part/","e:/test/merge/001.jpg");
		//清除part文件
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
	 * param blockNum 块的数目
	 * param blockNum 块的大小
	 * param srcPath  块的来源路径
	 * param destPath 块的存放路径的文件夹
	 * param length   文件大小 
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
		//源 src dest
		
		//流
		RandomAccessFile raf;
		BufferedOutputStream  bos ;
		long position=0;
		
		//缓冲数组和长度
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
	 * param destPath 合成的父路径
	 * param srcPath  合成的文件路径
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
		//变量目录下的所有需要合成的文件
		if(!src.isDirectory())
		{
			System.err.println("所要合成的文件目录不对");
			return ;
		}
		//源
		File [] mergeFile = src.listFiles(); 
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest,true));
		BufferedInputStream bis;
		File mergeTemp=null;
		for(int i=0;i<mergeFile.length;i++){
			//寻找需要合并的文件顺序
			for(int k=0;k<mergeFile.length;k++)
			{
				if(mergeFile[k].getName().endsWith(".part"+i)){
					mergeTemp = mergeFile[k];
				}		
			}
			bis = new BufferedInputStream(new FileInputStream(mergeTemp));
			//缓存数组和长度
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
