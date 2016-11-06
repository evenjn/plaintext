/**
 *
 * Copyright 2016 Marco Trevisan
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
package org.github.evenjn.plaintext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.github.evenjn.file.FileFool;
import org.github.evenjn.knit.KnittingCursable;
import org.github.evenjn.knit.KnittingCursor;
import org.github.evenjn.knit.Suppressor;
import org.github.evenjn.yarn.Cursable;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.Hook;

public class PlainText {

	public static LineReader reader( ) {
		return new LineReader( );
	}

	public static LineWriter writer( ) {
		return new LineWriter( );
	}

	static public Cursable<String> fileRead( String file ) {
		Cursable<String> cursable = new Cursable<String>( ) {

			@Override
			public Cursor<String> pull( Hook hook ) {
				InputStream is = FileFool.nu( ).open( Paths.get( file ) ).read( hook );
				Stream<String> stream = read( hook, is, Charset.forName( "UTF-8" ) );
				return KnittingCursor.wrap( stream );
			}
		};
		return KnittingCursable.wrap( cursable );
	}

	static public Consumer<String> fileWrite( Hook hook, String file ) {
		boolean erase = true;
		Charset cs = Charset.forName( "UTF-8" );
		String delimiter = "\n";
		Path path = Paths.get( file );
		FileFool ff = FileFool.nu( );
		if ( ff.exists( path ) ) {
			if ( erase ) {
				ff.delete( path );
			} else {
				throw new IllegalStateException( "File exists: [" + file + "]" );
			}
		}
		ff.create( ff.mold( path ) );
		OutputStream os = ff.open( path ).write( hook );
		return write( hook, os, cs, delimiter, true );
	}

	static Consumer<String> write(
			Hook hook,
			OutputStream os,
			Charset cs,
			String delimiter,
			boolean force_flush ) {
		CharsetEncoder encoder = cs.newEncoder( );
		Writer writer = hook.hook( new OutputStreamWriter( os, encoder ) );
		BufferedWriter buffered_writer =
				hook.hook( new BufferedWriter( writer ) );
		return new Consumer<String>( ) {

			@Override
			public void accept( String t ) {
				try {
					buffered_writer.append( t );
					if ( delimiter != null ) {
						buffered_writer.append( delimiter );
					}
					if ( force_flush ) {
						buffered_writer.flush( );
					}
				}
				catch ( IOException e ) {
					throw Suppressor.quit( e );
				}
			}
		};
	}

	static Stream<String> read( Hook hook, InputStream input, Charset cs ) {
		CharsetDecoder decoder = cs.newDecoder( );
		Reader reader = hook.hook( new InputStreamReader( input, decoder ) );
		BufferedReader buffered_reader =
				hook.hook( new BufferedReader( reader ) );
		Stream<String> stream = hook.hook( buffered_reader.lines( ) );
		return stream;
	}

}
