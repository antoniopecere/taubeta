package eu.pecere.taubeta;

public class Configuration
{
	private int righeDaSaltare;
	
	private String[] percorsiCartelle;
	
	public Configuration( int righeDaSaltare, String[] percorsiCartelle )
	{
		this.righeDaSaltare = righeDaSaltare;
		this.percorsiCartelle = percorsiCartelle;
	}
	
	public int getRigheDaSaltare()
	{
		return this.righeDaSaltare;
	}
	
	public String[] getPercorsiCartelle()
	{
		return this.percorsiCartelle;
	}
}
