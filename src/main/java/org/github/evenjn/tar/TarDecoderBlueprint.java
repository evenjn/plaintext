package org.github.evenjn.tar;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorMapH;
import org.github.evenjn.yarn.Hook;
import org.github.evenjn.yarn.PastTheEndException;

public class TarDecoderBlueprint {

	public static class Entry {
		public TarArchiveEntry entry;
		public InputStream is;
	}
	
	public CursorMapH<InputStream, Entry> build( ) {
		return TarDecoderBlueprint::decode;
	}

	private static Cursor<Entry> decode(
			Hook hook,
			InputStream is ) {
		try {
			
			final TarArchiveInputStream tis =
					(TarArchiveInputStream) new ArchiveStreamFactory( )
							.createArchiveInputStream( "tar", is );

			return new Cursor<Entry>( ) {

				@Override
				public Entry next( )
						throws PastTheEndException {
					Entry result = new Entry( );
					try {
						result.entry = (TarArchiveEntry) tis.getNextEntry( );
						if (result.entry == null) {
							throw PastTheEndException.neo;
						}
						result.is = tis;
					}
					catch ( IOException t ) {
						throw new RuntimeException( t );
					}
					return result;
				}
			};
		}
		catch ( ArchiveException t ) {
			throw new RuntimeException( t );
		}
	}
}
