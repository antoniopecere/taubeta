package eu.pecere.taubeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader
{
	private static final String RIGHE_DA_SALTARE = "RigheDaSaltare";
	
	public static Configuration read( File file )
	{
		try ( BufferedReader br = new BufferedReader( new FileReader( file ) ); ) {
			
			String linea = "";
			
			int righeDaSaltare = 2;
			if( ( linea = br.readLine() ) != null ) {
				if( linea.contains( RIGHE_DA_SALTARE ) ) {
					String[] keyValue = linea.split( "=" );
					righeDaSaltare = Integer.parseInt( keyValue[1] );
				}
			}
			
			List<String> listaPercorsi = new ArrayList<String>();
			while( ( linea = br.readLine() ) != null ) {
				if( "".equals( linea ) )
					continue;
				else
					listaPercorsi.add( linea );
			}
			
			String[] percorsiCartelle = listaPercorsi.toArray( new String[0] );
			
			return new Configuration( righeDaSaltare, percorsiCartelle );
			
		} catch( IOException e ) {
			throw new RuntimeException( e );
		}
	}
	
}
