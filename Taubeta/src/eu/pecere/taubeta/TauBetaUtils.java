package eu.pecere.taubeta;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

public class TauBetaUtils
{
	public static String invertiParole( String frase )
	{
		if( frase == null )
			return null;
		
		if( frase.length() == 0 )
			return frase;
		
		String fraseInvertita = "";
		
		frase = TauBetaUtils.innerTrim( frase );
		String[] parole = frase.split( " " );
		
		if( parole.length > 1 ) {
			List<String> listaParole = Arrays.asList( parole );
			Collections.reverse( listaParole );
			parole = listaParole.toArray( new String[0] );
			
			for( String parola : parole ) {
				fraseInvertita += parola + " ";
			}
			
			fraseInvertita = fraseInvertita.trim();
		} else {
			fraseInvertita = frase;
		}
		
		return fraseInvertita;
	}
	
	public static String innerTrim( String nominativo )
	{
		return nominativo.trim().replaceAll( "\\s++", " " );
	}
	
	public static void checkAndAddLowercase( List<String> list, String value, boolean checkInvertedToo )
	{
		if( value == null )
			return;
		
		value = value.toLowerCase();
		if( list.contains( value ) )
			return;
		
		if( checkInvertedToo ) {
			String invertedValue = TauBetaUtils.invertiParole( value );
			if( list.contains( invertedValue ) )
				return;
		}
		
		list.add( value );
	}
	
	public static void setWindowPosition( JFrame window, int screen )
	{
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] allDevices = env.getScreenDevices();
		int topLeftX, topLeftY, screenX, screenY, windowPosX, windowPosY;
		
		if( screen < allDevices.length && screen > -1 ) {
			topLeftX = allDevices[screen].getDefaultConfiguration().getBounds().x;
			topLeftY = allDevices[screen].getDefaultConfiguration().getBounds().y;
			
			screenX = allDevices[screen].getDefaultConfiguration().getBounds().width;
			screenY = allDevices[screen].getDefaultConfiguration().getBounds().height;
		} else {
			topLeftX = allDevices[0].getDefaultConfiguration().getBounds().x;
			topLeftY = allDevices[0].getDefaultConfiguration().getBounds().y;
			
			screenX = allDevices[0].getDefaultConfiguration().getBounds().width;
			screenY = allDevices[0].getDefaultConfiguration().getBounds().height;
		}
		
		windowPosX = ( ( screenX - window.getWidth() ) / 2 ) + topLeftX;
		windowPosY = ( ( screenY - window.getHeight() ) / 2 ) + topLeftY;
		
		window.setLocation( windowPosX, windowPosY );
	}
	
	public static String parentPath() throws Throwable
	{
		URL jarUrl = TauBetaUtils.class.getProtectionDomain().getCodeSource().getLocation();
		String jarPathname = jarUrl.toURI().getPath();
		File jarFile = new File( jarPathname );
		String jarDir = jarFile.getParentFile().getPath();
		return jarDir;
	}
	
	public static String listToString( List<String> list, boolean isList )
	{
		return TauBetaUtils.listToString( list, isList, false );
	}
	
	public static String listToString( List<String> list, boolean isList, boolean reverse )
	{
		if( list == null || list.size() == 0 )
			return "[ NESSUNA CORRISPONDENZA ]";
		
		Collections.sort( list );
		if (reverse)
			Collections.reverse( list );
		
		String elenco = "";
		if( isList ) {
			for( String value : list ) {
				elenco += value + "\r\n";
			}
		} else {
			elenco = Arrays.toString( list.toArray( new String[0] ) );
		}
		return elenco;
	}
}
