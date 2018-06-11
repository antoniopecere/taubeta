package eu.pecere.taubeta;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

public class Ascoltatore extends AbstractAscoltatore
{
	private TauBetaSearchTool tauBetaSearchTool;
	private SearchManager searchManager;
	
	public Ascoltatore( TauBetaSearchTool tauBetaSearchTool, SearchManager searchManager )
	{
		this.tauBetaSearchTool = tauBetaSearchTool;
		this.searchManager = searchManager;
	}
	
	// Evento per lanciare la ricerca al premere del tasto ENTER
	@Override
	public void keyPressed( KeyEvent e )
	{
		if( e.getKeyCode() != KeyEvent.VK_ENTER )
			return;
		
		this.search();
	}
	
	// Evento per caricare il riepilogo delle testate
	@Override
	public void actionPerformed( ActionEvent e )
	{
		if( this.tauBetaSearchTool.radioSelection.get( TauBetaSearchTool.RIEPILOGO ).isSelected() ) {
			this.tauBetaSearchTool.textControlsPane.setEnabled( false );
			this.tauBetaSearchTool.textField.setEnabled( false );
			this.tauBetaSearchTool.actionLabel.setEnabled( false );
			this.searchForRiepilogo();
		} else {
			String actualLabel = TauBetaSearchTool.LABEL_NOMINATIVO;
			if( this.tauBetaSearchTool.radioSelection.get( TauBetaSearchTool.TESTATA ).isSelected() ) {
				actualLabel = TauBetaSearchTool.LABEL_TESTATA;
			}
			
			this.tauBetaSearchTool.textControlsPane.setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder( actualLabel ),
							BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) );
			
			this.tauBetaSearchTool.textControlsPane.setEnabled( true );
			this.tauBetaSearchTool.textField.setEnabled( true );
			this.tauBetaSearchTool.actionLabel.setEnabled( true );
			this.reset();
			this.search();
		}
	}
	
	// Evento per evitare la ricerca sotto i 3 caratteri e resettare la textarea.
	@Override
	public void keyReleased( KeyEvent e )
	{
		this.reset();
	}
	
	private void reset()
	{
		String testoDigitato = this.tauBetaSearchTool.textField.getText();
		
		if( testoDigitato == null )
			return;
		
		testoDigitato = TauBetaUtils.innerTrim( testoDigitato );
		if( testoDigitato.length() >= TauBetaSearchTool.MIN_LENGTH )
			return;
		
		this.tauBetaSearchTool.actionLabel.setText( TauBetaSearchTool.WARNING );
		this.tauBetaSearchTool.textArea.setText( "" );
	}
	
	private void search()
	{
		String testoDigitato = this.tauBetaSearchTool.textField.getText();
		
		if( testoDigitato == null )
			return;
		
		testoDigitato = TauBetaUtils.innerTrim( testoDigitato );
		if( testoDigitato.length() < TauBetaSearchTool.MIN_LENGTH )
			return;
		
		if( this.tauBetaSearchTool.radioSelection.get( TauBetaSearchTool.NOMINATIVO ).isSelected() ) {
			this.searchByNominativo( testoDigitato );
		} else if( this.tauBetaSearchTool.radioSelection.get( TauBetaSearchTool.TESTATA ).isSelected() ) {
			this.searchByTestata( testoDigitato );
		} else
			return;
	}
	
	private void searchByNominativo( String testoRicerca )
	{
		final String nominativo = testoRicerca;
		Runnable runner = new Runnable() {
			@Override
			public void run()
			{
				Risultati risultati = searchManager.searchByNominativo( nominativo );
				String nominativiTrovati = risultati.getRigaClienti();
				String testateTrovate = risultati.getElencoTestate();
				
				String labelMessage = "Nominativi trovati: " + nominativiTrovati;
				labelMessage = labelMessage.length() > TauBetaSearchTool.MAX_LABEL_LENGTH
						? labelMessage.substring( 0, TauBetaSearchTool.MAX_LABEL_LENGTH - 1 ) + "..." : labelMessage;
				
				tauBetaSearchTool.actionLabel.setText( labelMessage );
				tauBetaSearchTool.textArea.setText( testateTrovate );
			}
		};
		Thread t = new Thread( runner, "Search Executer" );
		t.start();
	}
	
	private void searchByTestata( String testoRicerca )
	{
		final String testata = testoRicerca;
		Runnable runner = new Runnable() {
			@Override
			public void run()
			{
				Risultati risultati = searchManager.searchByTestata( testata );
				String nominativiTrovati = risultati.getElencoClienti();
				String testateTrovate = risultati.getRigaTestate();
				
				String labelMessage = "Testate trovate: " + testateTrovate;
				labelMessage = labelMessage.length() > TauBetaSearchTool.MAX_LABEL_LENGTH
						? labelMessage.substring( 0, TauBetaSearchTool.MAX_LABEL_LENGTH - 1 ) + "..." : labelMessage;
				
				tauBetaSearchTool.actionLabel.setText( labelMessage );
				tauBetaSearchTool.textArea.setText( nominativiTrovati );
			}
		};
		Thread t = new Thread( runner, "Search Executer" );
		t.start();
	}
	
	private void searchForRiepilogo()
	{
		Runnable runner = new Runnable() {
			@Override
			public void run()
			{
				Risultati risultati = searchManager.searchForRiepilogo();
				String riepilogoTestate = risultati.getRiepilogo();
				
				tauBetaSearchTool.textArea.setText( riepilogoTestate );
			}
		};
		Thread t = new Thread( runner, "Search Executer" );
		t.start();
	}
	
	// Evento per copiare negli appunti il contenuto della textarea.
	@Override
	public void mousePressed( MouseEvent e )
	{
		String textAreaContent = this.tauBetaSearchTool.textArea.getText();
		
		if( textAreaContent == null )
			return;
		
		if( textAreaContent.trim().isEmpty() )
			return;
		
		StringSelection textAreaSelection = new StringSelection( textAreaContent );
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents( textAreaSelection, null );
		JOptionPane.showMessageDialog( this.tauBetaSearchTool, "Elenco copiato negli appunti.", "INFO", JOptionPane.INFORMATION_MESSAGE );
	}
	
}
