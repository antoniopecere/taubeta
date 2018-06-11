package eu.pecere.taubeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OdsContent
{
	private String testata;
	private String[] clienti;
	
	public OdsContent( String testata, String[] clienti )
	{
		this.testata = testata;
		this.clienti = clienti;
	}
	
	public int containsNominativo( String nominativoDigitato, Risultati risultati )
	{
		String nominativoInvertito = TauBetaUtils.invertiParole( nominativoDigitato );
		
		List<String> listaClientiTrovati = new ArrayList<String>();
		for( String cliente : clienti ) {
			String clienteInvertito = TauBetaUtils.invertiParole( cliente );
			boolean checkContains = cliente.contains( nominativoDigitato.toLowerCase() ) ||
					cliente.contains( nominativoInvertito.toLowerCase() ) ||
					clienteInvertito.contains( nominativoDigitato.toLowerCase() ) ||
					clienteInvertito.contains( nominativoInvertito.toLowerCase() );
			
			if( checkContains ) {
				risultati.addCliente( cliente );
				listaClientiTrovati.add( cliente );
			}
		}
		
		return listaClientiTrovati.size();
	}
	
	public String getTestata()
	{
		return this.testata;
	}
	
	public String[] getClienti()
	{
		return this.clienti;
	}
	
	@Override
	public String toString()
	{
		String testata = this.testata == null ? "-" : this.testata;
		String clienti = this.clienti == null || this.clienti.length == 0 ? "-" : Arrays.toString( this.clienti );
		return "OdsFile( " + testata + " ): " + clienti + ".";
	}
	
}
