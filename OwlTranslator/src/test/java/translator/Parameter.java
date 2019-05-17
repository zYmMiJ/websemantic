package translator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parameter {

	private String nom;
	private List<String> valeur = new ArrayList<String>();
	private List<String> nomparam = new ArrayList<String>();
	
	public Parameter(String n,List<String> v,List<String> np){
		
		// par exemple : # Environment parameters 
		this.nom = n;
		// par exemple DESIGNDATE=2018-06-01 np=v
		this.valeur = v;
		this.nomparam = np;
	}

	public String getNom() {
		return this.nom;
	}
	
	public List<String> getValeur() {
		return this.valeur;
	}
	
	public List<String> getNomParam() {
		return this.nomparam;
	}
	
	public Parameter() {
		// TODO Auto-generated constructor stub
	}
	
	public void setNom(String n) {
		this.nom = n;
	}
	
	public void setData(List<String> d) {
		this.nomparam = d;
	}
	
	public void setValeur(List<String> v) {
		this.valeur = v;
	}
	
	public void setValeur(int i,String res) {
		this.valeur.set(i, res);
	}
	
	public boolean isValeurEmpty() {
		return this.valeur.isEmpty();
	}
	
	public void afficher() {
		Iterator<String> it = this.valeur.iterator();
		 
		while (it.hasNext()) {
			String s = it.next();
			System.out.println(s);
		}
	}
	
	public String getOneParam(String p) {
		 Iterator<String> it = this.nomparam.iterator();
		 int i = 0;
		// System.out.println("On recherche p: "+p+" dans :");
		while (it.hasNext()) {
			
			String s = it.next();
			// System.out.println(s);
			if ( s.contains(p) ) {
				// System.out.println("On a trouver s equals p( "+s+" ), on rend :"+this.valeur.get(i));
				return this.valeur.get(i);
			}
			i ++;
		}
		return "erreur";
	}
	/*
	 * Method qui set les parametres d'environnement du script
	 * Entrée le paramètred'env ${nom} et nom
	 * Le nom nous permet d'aller chercher la valeur
	 */
	public void setEnvParameter(String env, String nom) {
		Iterator<String> it = this.valeur.iterator();
		int count = 0;
		
		// Parcours des valeurs
		while (it.hasNext()) {
			it.next();
			String s = this.valeur.get(count);
			// Si une valeurs contient une variable  d'env
			if ( s.contains(env) ) {

				Iterator<String> it2 = this.nomparam.iterator();
				int i = 0;
				// On parcours tous les paramètres pour aller la chercher
				while (it2.hasNext()) {
					String b = it2.next();
					System.out.println("Pour :"+env+" on verifie si "+b+"="+nom);
					if ( nom.equals(b) ) {
						System.out.println("On remplace par "+getOneParam(nom));
						s = s.replace(env, getOneParam(nom));
						this.setValeur(i,s);
						break;
					}
					i++;
				}

			}
			count ++;
		}
	}
	public void replaceEnvParam(String replace) {
		
	}

}



