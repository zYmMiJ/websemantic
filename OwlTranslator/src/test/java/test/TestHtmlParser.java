package test;

import org.junit.Test;

import junit.framework.TestCase;
import translator.HtmlParser;

public class TestHtmlParser extends TestCase {

	public TestHtmlParser(String name) {
		super(name);
	}

	@Test
	public final void testinitListLinkXP() {
		
		HtmlParser h = new HtmlParser("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments");// Init Good page

		int nbminimumofXP = 54;// Number minimum of experience
		String generalLink = "https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/";// Default path of Experience
		
		for( String s : h.getListLink() ) {
			assertTrue("Mauvais lien", s.contains(generalLink) && s.contains("-NOOR"));	
		}
		assertFalse("Nombre d'expérience trop peu nombreuse", nbminimumofXP > h.getListLink().size() );		
	}
}
