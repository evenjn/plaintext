package org.github.evenjn.tar;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.github.evenjn.knit.Suppressor;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorMapH;
import org.github.evenjn.yarn.Hook;
import org.github.evenjn.yarn.PastTheEndException;

public class TarDecoderBlueprint {

	public CursorMapH<InputStream, TarArchiveEntry> build( ) {
		return TarDecoderBlueprint::decode;
	}

	private static Cursor<TarArchiveEntry> decode( Hook hook, InputStream is ) {
		try {
			final TarArchiveInputStream tis =
					(TarArchiveInputStream) new ArchiveStreamFactory( )
							.createArchiveInputStream( "tar", is );

			Cursor<TarArchiveEntry> result = new Cursor<TarArchiveEntry>( ) {

				@Override
				public TarArchiveEntry next( )
						throws PastTheEndException {
					try {
						return (TarArchiveEntry) tis.getNextEntry( );
					}
					catch ( IOException t ) {
						throw Suppressor.quit( t );
					}
				}
			};
			return result;
		}
		catch ( ArchiveException t ) {
			throw Suppressor.quit( t );
		}
	}
}
