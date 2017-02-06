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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorMapH;
import org.github.evenjn.yarn.Hook;
import org.github.evenjn.yarn.EndOfCursorException;

public class LineReaderBlueprint {

	public CursorMapH<InputStream, String> build( ) {
		final Charset local_cs = cs;
		final Pattern local_delimiter = delimiter;
		return new CursorMapH<InputStream, String>( ) {

			@Override
			public Cursor<String> get( Hook h, InputStream input ) {
				return LineReaderBlueprint.read( h, input, local_cs, local_delimiter );
			}
		};
	}

	private Charset cs = Charset.forName( "UTF-8" );
	
	private Pattern delimiter = delimiter_pattern;

	public LineReaderBlueprint setCharset( Charset cs ) {
		this.cs = cs;
		return this;
	}
	
	public  LineReaderBlueprint setDelimiter( Pattern delimiter ) {
		this.delimiter = delimiter;
		return this;
	}

	private static final Pattern delimiter_pattern =
			Pattern.compile( "[\\x0D]?[\\x0A]" );

	private static Cursor<String> read(
			Hook hook,
			InputStream input,
			Charset cs,
			Pattern delimiter ) {
		Reader reader = hook.hook( new InputStreamReader( input, cs ) );
		BufferedReader buffered_reader =
				hook.hook( new BufferedReader( reader ) );

		Scanner scanner = hook.hook( new Scanner( buffered_reader ) );
		scanner.useDelimiter( delimiter );
		return new Cursor<String>( ) {

			@Override
			public String next( )
					throws EndOfCursorException {
				for ( ;; ) {
					try {
						boolean hasNext = scanner.hasNext( );
						if ( scanner.ioException( ) != null ) {
							throw scanner.ioException( );
						}
						if ( hasNext ) {

							String next = scanner.next( );
							if ( scanner.ioException( ) != null ) {
								throw scanner.ioException( );
							}
							return next;
						}
						throw EndOfCursorException.neo();
					}
					catch ( IOException e ) {
						throw new RuntimeException( e );
					}
				}
			}
		};
	}
}
