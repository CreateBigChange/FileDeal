import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class FileString {
public static void main(String arge []){
	File src =new File("e:/test/123.txt");
	File dest = new File("e:/test/456.txt");
	System.out.println("xx");
	copy(src,dest);
}
public static void copy(File src,File dest){
	Reader	src1=null;
	Writer	dest1=null;
try {
	src1  = new FileReader(src);
	dest1 = new FileWriter(dest);
	int len = 0;
	char [] ch =new char[1024];
	while((len = src1.read(ch))!=-1){
		
		dest1.write(ch, 0, ch.length);
		dest1.flush();
	}
} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}finally{
	try {
		src1.close();
		dest1.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

}
}
