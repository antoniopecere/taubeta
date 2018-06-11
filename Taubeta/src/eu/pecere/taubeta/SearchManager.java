package eu.pecere.taubeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class SearchManager
{
	private int cartelleEsistenti;
	private Configuration config;
	private List<File> totalOdsFiles;
	private OdsContent[] odsContents;
	
	public SearchManager( Configuration config )
	{
		this.config = config;
		this.cartelleEsistenti = 0;
		
		this.totalOdsFiles = new ArrayList<File>();
		for( String percorsoCartella : config.getPercorsiCartelle() ) {
			File cartella = new File( percorsoCartella );
			if( cartella.exists() && cartella.isDirectory() ) {
				this.cartelleEsistenti++;
				File[] listaFileOds = cartella.listFiles( new OdsFilter() );
				this.totalOdsFiles.addAll( Arrays.asList( listaFileOds ) );
			}
		}
		
		this.loadData();
	}
	
	private void loadData()
	{
		int fileTotali = this.totalOdsFiles.size();
		int cartelleElaborate = this.cartelleEsistenti;
		int cartellePreviste = this.config.getPercorsiCartelle().length;
		
		ProgressBar bar = new ProgressBar( "Caricamento file in corso...", "Analizzati: 0 files.", 0, fileTotali );
		bar.setVisible( true );
		List<OdsContent> odsContentList = new ArrayList<OdsContent>();
		for( int i = 0; i < fileTotali; i++ ) {
			File odsFile = this.totalOdsFiles.get( i );
			String fileName = odsFile.getName();
			bar.setNote( "Avanzamento: " + ( i + 1 ) + "/" + fileTotali + ". Lettura file [" + fileName + "] in corso." );
			OdsContent odsContent = OdsReader.read( odsFile, config.getRigheDaSaltare() );
			odsContentList.add( odsContent );
			bar.setProgress( i + 1 );
		}
		bar.close();
		
		if( cartelleElaborate != cartellePreviste ) {
			String message = "Caricati " + fileTotali + " file da " + cartelleElaborate + " cartelle su " + cartellePreviste + " previste.";
			String titolo = cartelleElaborate == cartellePreviste ? "INFO" : "AVVISO";
			int optionType = cartelleElaborate == cartellePreviste ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
			JOptionPane.showMessageDialog( bar, message, titolo, optionType );
		}
		
		this.odsContents = odsContentList.toArray( new OdsContent[0] );
	}
	
	public Risultati searchByNominativo( String nominativoDigitato )
	{
		Risultati risultati = new Risultati();
		
		for( OdsContent odsContent : this.odsContents ) {
			if( odsContent.containsNominativo( nominativoDigitato, risultati ) > 0 ) {
				String testata = odsContent.getTestata();
				if( testata != null )
					risultati.addTestata( testata );
			}
		}
		
		return risultati;
	}
	
	public Risultati searchByTestata( String testataDigitata )
	{
		Risultati risultati = new Risultati();
		
		for( OdsContent odsContent : this.odsContents ) {
			String testata = odsContent.getTestata();
			if( testata.contains( testataDigitata ) ) {
				risultati.addTestata( testata );
				risultati.addClienti( odsContent.getClienti() );
			}
		}
		
		return risultati;
	}
	
	public Risultati searchForRiepilogo()
	{
		Risultati risultati = new Risultati();
		
		for( OdsContent odsContent : this.odsContents ) {
			String testata = odsContent.getTestata();
			
			Integer totClienti = odsContent.getClienti().length;
			if( totClienti < 1 )
				continue;
			
			String sTotClienti = String.format( "%1$3s", totClienti );
			risultati.addTestata( sTotClienti + " --> " + testata );
		}
		
		return risultati;
	}
	
}
