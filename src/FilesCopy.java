import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesCopy {
	public static void main(String args [])
	{
		File src  =new File("E:/test");
		File dest = new File("E:/test1");
		//dest.mkdirs();
		copy(src,dest);
	}
	
	public static void copy(File src,File dest){
		dest.mkdirs();
		//System.out.println("cofinish");
		File temp=null;
		for(int i=0;i<src.list().length;i++){
			temp = src.listFiles()[i];
			if(temp.isDirectory()){
				//在dest文件夹里面创建新的文件夹
				File tempDir=new File(dest,temp.getName());
				tempDir.mkdir();
				copy(temp,tempDir);
			}
			else if(temp.isFile()){
				//文件的拷贝
				File destFile= new File(dest,temp.getName());
				InputStream is =null;
				OutputStream os = null;
				try {
					is =new FileInputStream(temp);
					os = new FileOutputStream(destFile,true);
					byte[] b = new byte[1024];
					int len=0;
					while((len=is.read(b))!=-1){
						os.write(b, 0, b.length);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
