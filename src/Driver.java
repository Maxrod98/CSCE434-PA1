import java.io.FileNotFoundException;
import java.util.HashMap;

public class Driver {

	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: file_location" );
			return;
		}
		
		String code = null;
		try {
			code = FileUtils.readFileToString(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("File not found at: " + args[0]);
			return;
		}
		
		HashMap<String, Token> table = new HashMap<String, Token>();
		table.put("begin", new Token("begin", null));
		table.put("end", new Token("end", null));
		table.put("div", new Token("div", null));
		table.put("mod", new Token("mod", null));
		
		RecursiveParser rp = new RecursiveParser(code, table, new RecursiveParser.Listener() {
			@Override
			public void onFinished(String abstractMachineInstr) {
				System.out.println(abstractMachineInstr);
			}
		});
		
		rp.parse();
	}

}
