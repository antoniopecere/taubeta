package eu.pecere.taubeta;

import java.util.ArrayList;
import java.util.List;

public class Risultati
{
	private List<String> clienti;
	private List<String> testate;
	
	public Risultati()
	{
		this.clienti = new ArrayList<String>();
		this.testate = new ArrayList<String>();
	}
	
	public void addCliente( String cliente )
	{
		TauBetaUtils.checkAndAddLowercase( this.clienti, cliente, true );
	}
	
	public void addClienti( String[] clienti )
	{
		for( String cliente : clienti ) {
			this.addCliente( cliente );
		}
	}
	
	public void addTestata( String testata )
	{
		TauBetaUtils.checkAndAddLowercase( this.testate, testata, false );
	}
	
	public String getElencoClienti()
	{
		return TauBetaUtils.listToString( this.clienti, true );
	}
	
	public String getRigaClienti()
	{
		return TauBetaUtils.listToString( this.clienti, false );
	}
	
	public String getElencoTestate()
	{
		return TauBetaUtils.listToString( this.testate, true );
	}
	
	public String getRigaTestate()
	{
		return TauBetaUtils.listToString( this.testate, false );
	}
	
	public String getRiepilogo()
	{
		return TauBetaUtils.listToString( this.testate, true, true );
	}
}
