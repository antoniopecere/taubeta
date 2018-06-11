package eu.pecere.taubeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class OdsReader
{
	public static OdsContent read( File fileOds, int righeDaSaltare )
	{
		List<String> listaClienti = new ArrayList<String>();
		
		try {
			Sheet sheet = SpreadSheet.createFromFile( fileOds ).getSheet( 0 );
			
			int nRowCount = sheet.getRowCount();
			
			MutableCell<?> primaCella = sheet.getCellAt( 0, 0 );
			String testata = TauBetaUtils.innerTrim( primaCella.getTextValue() ).toLowerCase();
			
			int righeVuote = 0;
			for( int nRowIndex = righeDaSaltare; nRowIndex < nRowCount; nRowIndex++ ) {
				MutableCell<?> cell = sheet.getCellAt( 0, nRowIndex );
				String cellValue = cell.getTextValue();
				if( cellValue != null && !cellValue.isEmpty() ) {
					String cliente = TauBetaUtils.innerTrim( cell.getTextValue() );
					TauBetaUtils.checkAndAddLowercase( listaClienti, cliente, true );
				} else {
					if( ++righeVuote > 4 ) {
						break;
					}
					continue;
				}
			}
			
			String[] clienti = listaClienti.toArray( new String[0] );
			
			return new OdsContent( testata, clienti );
		} catch( Throwable e ) {
			throw new RuntimeException( e );
		}
	}
}
