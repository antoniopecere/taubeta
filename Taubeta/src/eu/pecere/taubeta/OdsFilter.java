package eu.pecere.taubeta;

import java.io.File;
import java.io.FileFilter;

public class OdsFilter implements FileFilter
{
	@Override
	public boolean accept( File file )
	{
		return file.getName().toLowerCase().endsWith( ".ods" );
	}
}
