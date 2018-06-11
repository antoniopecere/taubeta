package eu.pecere.taubeta;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

public class ProgressBar extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JProgressBar progressBar;
	private int valoreFinale;
	
	public ProgressBar( String titolo, String note, int valoreIniziale, int valoreFinale )
	{
		super( titolo );
		this.valoreFinale = valoreFinale;
		this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		Container content = this.getContentPane();
		this.progressBar = new JProgressBar();
		this.progressBar.setValue( valoreIniziale );
		this.progressBar.setStringPainted( true );
		Border border = BorderFactory.createTitledBorder( note );
		this.progressBar.setBorder( border );
		content.add( this.progressBar, BorderLayout.NORTH );
		this.setSize( 450, 90 );
		
		TauBetaUtils.setWindowPosition( this, 0 );
	}
	
	public void close()
	{
		this.setVisible( false );
		this.dispose();
	}
	
	public void setNote( String note )
	{
		Border border = BorderFactory.createTitledBorder( note );
		this.progressBar.setBorder( border );
	}
	
	public void setProgress( int progress )
	{
		int percentage = ( 100 * progress ) / this.valoreFinale;
		this.progressBar.setValue( percentage );
	}
	
}
