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
package org.github.evenjn.plaintext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.github.evenjn.yarn.Cursable;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.Hook;

public class PlainText {

	public static LineReaderBlueprint reader( ) {
		return new LineReaderBlueprint( );
	}

	public static LineWriterBlueprint writer( ) {
		return new LineWriterBlueprint( );
	}

	public static Cursable<String> fileRead( String file ) {
		Path path = Paths.get( file );
		Cursable<String> cursable = new Cursable<String>( ) {

			@Override
			public Cursor<String> pull( Hook hook ) {
				try {
					InputStream is = hook.hook( Files.newInputStream( path ) );
					Cursor<String> cursor = PlainText
							.reader( )
							.build( )
							.get( hook, is );
					return cursor;
				}
				catch ( IOException e ) {
					throw new RuntimeException( e );
				}
			}
		};
		return cursable;
	}

	/*
	 * For safety reasons, this method does not erase/overwrite existing files.
	 * 
	 * @Deprecated static public Consumer<String> fileWrite( Hook hook, String
	 * file ) { Charset cs = Charset.forName( "UTF-8" ); String delimiter = "\n";
	 * Path path = Paths.get( file ); FileFool ff = FileFool.nu( ); if (
	 * ff.exists( path ) ) { throw new IllegalStateException( "File exists: [" +
	 * file + "]" ); } ff.create( ff.mold( path ) ); OutputStream os = ff.open(
	 * path ).write( hook ); return write( hook, os, cs, delimiter, true ); }
	 */

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
					throw new RuntimeException( e );
				}
			}
		};
	}

	public static boolean isDecodableAs( InputStream is, Charset cs ) {
		try (
			InputStreamReader isr = new InputStreamReader( is, cs );
			BufferedReader r = new BufferedReader( isr ); ) {
			for ( ;; ) {
				int read = r.read( );
				if ( read == -1 ) {
					break;
				}
			}
		}
		catch ( java.nio.charset.MalformedInputException e ) {
			return false;
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
		return true;
	}

}
