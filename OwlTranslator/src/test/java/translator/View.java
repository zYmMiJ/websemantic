package translator;

import java.util.Scanner;

public class View {
	
	public static String main(String arg[]) {
		
		
		
		return null;
	}
	
	public static boolean fileAssociation(String nameFile) {
		System.out.println(nameFile+" will be replaced, continue ? (y/n) ");
		Scanner sc = new Scanner(System.in);
		String str=sc.nextLine();
		
		if(str.contentEquals("y")) {
			System.out.println("Press ENTER when you have completed "+nameFile+".");
			sc.nextLine();
			return true;
		}
			
		else
			return false;
	}
	
}
