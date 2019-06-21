package test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import translator.DataParsed;
import translator.HtmlParser;  


public class TestHtmlParser extends TestCase { 


    @Test
    public final void testConnnectionHTMLinDataToList() {
        List<DataParsed> dataparsedHTML_goodLink = new ArrayList<DataParsed>();
        List<DataParsed> dataparsedHTML_existentLink  = new ArrayList<DataParsed>();

        HtmlParser h_goodLink = new HtmlParser("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/20180828-NOOR");
        dataparsedHTML_goodLink = h_goodLink.dataToList();
        assertFalse("Bon lien genere une liste vide.", dataparsedHTML_goodLink.isEmpty() );
        assertFalse("Bon lien ne genere pas de liste.", dataparsedHTML_goodLink == null );

    }

    @Test(expected = IOException.class)
    public final void testshould_throw_IOException_testdataToList() throws IOException{

      /*  HtmlParser h_existentLink = new HtmlParser("https://gforge.inria.fr");
        HtmlParser h_badLink = new HtmlParser("mauvaisLiens");
        HtmlParser h_nullLink = new HtmlParser(null);*/

    }


}