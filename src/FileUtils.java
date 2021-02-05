import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class FileUtils {
	public static String readFileToString(String filePath) throws FileNotFoundException {
		String result = "";
		      File myObj = new File(filePath);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        result += data + '\n';
		      }
		      myReader.close();
		     
		      
		
		return result;
	}
	
}
