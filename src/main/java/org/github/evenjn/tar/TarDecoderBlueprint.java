/**
 *
 * Copyright 2017 Marco Trevisan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.github.evenjn.tar;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorMapH;
import org.github.evenjn.yarn.EndOfCursorException;
import org.github.evenjn.yarn.Hook;

public class TarDecoderBlueprint {

	public CursorMapH<InputStream, TarEntry> build( ) {
		return TarDecoderBlueprint::decode;
	}

	private static Cursor<TarEntry> decode(
			Hook hook,
			InputStream is ) {
		try {

			final TarArchiveInputStream tais = hook.hook(
					(TarArchiveInputStream) new ArchiveStreamFactory( )
							.createArchiveInputStream( "tar", is ) );

			return new Cursor<TarEntry>( ) {

				@Override
				public TarEntry next( )
						throws EndOfCursorException {
					TarEntry result = new TarEntry( );
					try {
						result.entry = (TarArchiveEntry) tais.getNextTarEntry( );
						if ( result.entry == null ) {
							throw EndOfCursorException.neo( );
						}

						final long size = result.entry.getSize( );

						result.is = new InputStream( ) {

							long left = size;

							@Override
							public int read( )
									throws IOException {
								if ( left == 0 ) {
									return -1;
								}
								int result = tais.read( );
								left = left - 1;
								return result;
							}
						};
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
