import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class FileUtils {
	public static String readFile(String filePath) {
		String result = "";
		try {
		      File myObj = new File(filePath);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        result += data + '\n';
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		return result;
	}
	
}
