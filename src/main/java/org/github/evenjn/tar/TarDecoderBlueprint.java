package org.github.evenjn.tar;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorMapH;
import org.github.evenjn.yarn.Di;
import org.github.evenjn.yarn.Hook;
import org.github.evenjn.yarn.PastTheEndException;

public class TarDecoderBlueprint {

	public CursorMapH<InputStream, Di<TarArchiveEntry, InputStream>> build( ) {
		return TarDecoderBlueprint::decode;
	}

	private static Cursor<Di<TarArchiveEntry, InputStream>> decode(
			Hook hook,
			InputStream is ) {
		try {
			
			final TarArchiveInputStream tis =
					(TarArchiveInputStream) new ArchiveStreamFactory( )
							.createArchiveInputStream( "tar", is );

			return new Cursor<Di<TarArchiveEntry, InputStream>>( ) {

				@Override
				public Di<TarArchiveEntry, InputStream> next( )
						throws PastTheEndException {
					return new Di<TarArchiveEntry, InputStream>( ) {

						@Override
						public TarArchiveEntry front( ) {
							try {
								return (TarArchiveEntry) tis.getNextEntry( );
							}
							catch ( IOException t ) {
								throw new RuntimeException( t );
							}
						}

						@Override
						public InputStream back( ) {
							return tis;
						};
					};
				}
			};
		}
		catch ( ArchiveException t ) {
			throw new RuntimeException( t );
		}
	}
}
