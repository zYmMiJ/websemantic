package translator;

import java.util.Iterator;
import java.util.List;

public class View {
	
	/*
	 * Affiche une liste
	 */
	public static void printList(List<Parameter> list) {
		Iterator<Parameter> it = list.iterator();
		 
		while (it.hasNext()) {
			Parameter s = it.next();
			System.out.println(s.getNom());
			System.out.println(s.getNomParam());
			System.out.println(s.getValeur());
			System.out.println("-------------------");
		}
	}
	
}
