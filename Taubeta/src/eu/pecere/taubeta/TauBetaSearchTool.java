package eu.pecere.taubeta;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.swing.*;

public class TauBetaSearchTool extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	protected static final int MIN_LENGTH = 3;
	protected static final int MAX_LABEL_LENGTH = 75;
	protected static final String INTESTAZIONE = " Strumento di ricerca TauBeta. ";
	protected static final String INTESTAZIONE_RISULTATI = " Risultati della ricerca: ";
	protected static final String LABEL_NOMINATIVO = " Inserisci un nominativo per la ricerca e premi Invio. ";
	protected static final String LABEL_TESTATA = " Inserisci una testata per la ricerca e premi Invio. ";
	protected static final String WARNING = " Inserisci almeno " + MIN_LENGTH + " caratteri. ";
	protected static final String LABEL_SEARCH_MODE = " Seleziona il tipo di ricerca. ";
	protected static final String NOMINATIVO = " Nominativo ";
	protected static final String TESTATA = " Testata ";
	protected static final String RIEPILOGO = " Riepilogo ";
	
	protected HashMap<String, JRadioButton> radioSelection = new HashMap<>();
	protected JTextField textField;
	protected JLabel actionLabel;
	protected JTextArea textArea;
	protected JPanel textControlsPane;
	
	private TauBetaSearchTool() throws Throwable
	{
		String parentPath = TauBetaUtils.parentPath();
		String propertiesFilePathname = parentPath + File.separator + "taubeta.properties";
		File fileProperties = new File( propertiesFilePathname );
		
		if( !fileProperties.exists() )
			throw new RuntimeException( "File [" + propertiesFilePathname + "] non trovato." );
		
		Configuration config = ConfigReader.read( fileProperties );
		SearchManager searchManager = new SearchManager( config );
		Ascoltatore ascoltatore = new Ascoltatore( this, searchManager );
		
		this.setLayout( new BorderLayout() );
		
		// Create radio button section.
		JRadioButton radioNominativo = new JRadioButton( NOMINATIVO, true );
		radioNominativo.setMnemonic( KeyEvent.VK_N );
		radioNominativo.setActionCommand( NOMINATIVO );
		radioNominativo.addActionListener( ascoltatore );
		this.radioSelection.put( NOMINATIVO, radioNominativo );
		
		JRadioButton radioTestata = new JRadioButton( TESTATA );
		radioTestata.setMnemonic( KeyEvent.VK_T );
		radioTestata.setActionCommand( TESTATA );
		radioTestata.addActionListener( ascoltatore );
		this.radioSelection.put( TESTATA, radioTestata );
		
		JRadioButton radioRiepilogo = new JRadioButton( RIEPILOGO );
		radioRiepilogo.setMnemonic( KeyEvent.VK_R );
		radioRiepilogo.setActionCommand( RIEPILOGO );
		radioRiepilogo.addActionListener( ascoltatore );
		this.radioSelection.put( RIEPILOGO, radioRiepilogo );
		
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add( radioNominativo );
		btnGroup.add( radioTestata );
		btnGroup.add( radioRiepilogo );
		
		// Lay out the radio controls and the labels.
		JPanel radioControlsPane = new JPanel();
		GridBagConstraints c1 = new GridBagConstraints();
		radioControlsPane.setLayout( new GridBagLayout() );
		
		c1.gridwidth = GridBagConstraints.HORIZONTAL;
		c1.anchor = GridBagConstraints.WEST;
		c1.weightx = 1.0;
		radioControlsPane.add( radioNominativo, c1 );
		radioControlsPane.add( radioTestata, c1 );
		radioControlsPane.add( radioRiepilogo, c1 );
		radioControlsPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder( LABEL_SEARCH_MODE ),
						BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) );
		
		// Create a regular text field.
		this.textField = new JTextField( 32 );
		this.textField.addKeyListener( ascoltatore );
		
		// Create a label to put messages during an action event.
		this.actionLabel = new JLabel( WARNING );
		this.actionLabel.setBorder( BorderFactory.createEmptyBorder( 10, 0, 0, 0 ) );
		
		// Lay out the text controls and the labels.
		this.textControlsPane = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		textControlsPane.setLayout( new GridBagLayout() );
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		textControlsPane.add( this.textField, gbc );
		
		c.gridwidth = GridBagConstraints.REMAINDER; // last
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		textControlsPane.add( this.actionLabel, c );
		textControlsPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder( LABEL_NOMINATIVO ),
						BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) );
		
		// Create a text area.
		this.textArea = new JTextArea( "" );
		this.textArea.setFont( new Font( "ARIAL", Font.BOLD, 15 ) );
		this.textArea.setEditable( false );
		this.textArea.setLineWrap( false );
		// this.textArea.setWrapStyleWord( true );
		this.textArea.addMouseListener( ascoltatore );
		JScrollPane areaScrollPane = new JScrollPane( this.textArea );
		areaScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		areaScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		areaScrollPane.setPreferredSize( new Dimension( 440, 500 ) );
		areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder( INTESTAZIONE_RISULTATI ),
								BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ),
						areaScrollPane.getBorder() ) );
		
		JPanel principalPanel = new JPanel( new BorderLayout() );
		principalPanel.add( radioControlsPane, BorderLayout.PAGE_START );
		principalPanel.add( textControlsPane, BorderLayout.CENTER );
		principalPanel.add( areaScrollPane, BorderLayout.PAGE_END );
		
		this.add( principalPanel, BorderLayout.LINE_START );
	}
	
	private void focus()
	{
		this.textField.grabFocus();
	}
	
	public static void main( String[] args )
	{
		UIManager.put( "swing.boldMetal", Boolean.FALSE );
		try {
			JFrame frame = new JFrame( INTESTAZIONE );
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			
			TauBetaSearchTool tool = new TauBetaSearchTool();
			frame.add( tool );
			frame.setResizable( false );
			
			URL iconUrl = TauBetaSearchTool.class.getResource( "taubetaMod.jpg" );
			ImageIcon icon = new ImageIcon( iconUrl );
			frame.setIconImage( icon.getImage() );
			
			frame.pack();
			TauBetaUtils.setWindowPosition( frame, 0 );
			frame.setVisible( true );
			
			tool.focus();
			
		} catch( Throwable e ) {
			String errorMessage = e.getMessage();
			errorMessage = errorMessage == null ? "ERRORE SCONOSCIUTO!" : errorMessage;
			JOptionPane.showMessageDialog( null, errorMessage, "ERRORE", JOptionPane.ERROR_MESSAGE );
		}
	}
}
