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
package org.github.evenjn.unicode;

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
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.github.evenjn.knit.KnittingItterable;
import org.github.evenjn.knit.KnittingItterator;
import org.github.evenjn.knit.KnittingTuple;
import org.github.evenjn.knit.Suppressor;
import org.github.evenjn.yarn.ArrayMap;
import org.github.evenjn.yarn.CursorUnfoldH;
import org.github.evenjn.yarn.FunctionH;
import org.github.evenjn.yarn.Hook;
import org.github.evenjn.yarn.Itterator;
import org.github.evenjn.yarn.StreamMapH;

class LineWriter implements
		FunctionH<OutputStream, Consumer<String>> {

	final Charset cs;

	LineWriter(Charset cs) {
		this.cs = cs;
	}

	@Override
	public Consumer<String> get( Hook hook, OutputStream output_stream ) {
		return Unicode.write( hook, output_stream, cs );
	}

}

class LineReader implements CursorUnfoldH<InputStream, String> {

	final Charset cs;

	LineReader(Charset cs) {
		this.cs = cs;
	}


	@Override
	public Itterator<String> next( Hook hook, InputStream input ) {
		return KnittingItterator.wrap( Unicode.read( hook, input, cs ).iterator( ) );
	}
}

public class Unicode {

	public static LineReader reader() {
		return new LineReader( Charset.forName( "UTF-8" ) );
	}
	
	public static LineReader reader(Charset cs) {
		return new LineReader( cs );
	}

	public static LineWriter writer() {
		return new LineWriter( Charset.forName( "UTF-8" ) );
	}
	
	public static LineWriter writer(Charset cs) {
		return new LineWriter( cs );
	}

	public static Consumer<String> write( Hook hook, OutputStream os ) {
		return write(hook, os, Charset.forName( "UTF-8" ));
	}
	public static Consumer<String> write( Hook hook, OutputStream os,
			Charset cs ) {
		CharsetEncoder encoder = cs.newEncoder( );
		Writer writer = hook.hook( new OutputStreamWriter( os, encoder ) );
		BufferedWriter buffered_writer =
				hook.hook( new BufferedWriter( writer ) );
		return new Consumer<String>( ) {

			@Override
			public void accept( String t ) {
				try {
					buffered_writer.append( t );
					buffered_writer.flush( );
				}
				catch ( IOException e ) {
					throw Suppressor.quit( e );
				}
			}
		};
	}

	public static KnittingItterator<String> read( Hook hook, InputStream input ) {
		return KnittingItterator
				.wrap( read( hook, input, Charset.forName( "UTF-8" ) ).iterator( ) );
	}

	public static Stream<String> read( Hook hook, InputStream input, Charset cs ) {
		CharsetDecoder decoder = cs.newDecoder( );
		Reader reader = hook.hook( new InputStreamReader( input, decoder ) );
		BufferedReader buffered_reader =
				hook.hook( new BufferedReader( reader ) );
		Stream<String> stream = hook.hook( buffered_reader.lines( ) );
		return stream;
	}

	public static StreamMapH<InputStream, String> streamMapH( Charset cs ) {
		return new StreamMapH<InputStream, String>( ) {

			@Override
			public Stream<String> get( Hook hook, InputStream input ) {
				return read( hook, input, cs );
			}
		};
	}

	public static Integer codepoint( String s ) {
		Integer result = null;
		for ( Integer i : codepoints( s ).once( ) ) {
			if ( result != null )
				throw new IllegalArgumentException(
						"The input string is realized using more than once codepoint." );
			result = i;
		}
		return result;
	}

	public static KnittingItterable<Integer> codepoints( String s ) {
		return KnittingItterable.wrap( new Iterable<Integer>( ) {

			@Override
			public Iterator<Integer> iterator( ) {
				return s.codePoints( ).boxed( ).iterator( );
			}
		} );
	}

	public static KnittingItterable<Integer> codepoints( String s, Form form ) {
		return codepoints( form == null ? s : Normalizer.normalize( s, form ) );
	}

	public static int[] codepointsArray( String input, Form form ) {
		KnittingItterable<Integer> codepoints = codepoints( input, form );
		long count = codepoints.size( );
		int size = 0;
		if ( count < 0 || count > 9999 )
			throw new IllegalArgumentException( );
		size = (int) count;
		int[] result = new int[size];
		int i = 0;
		for ( Integer cp : codepoints.once( ) ) {
			result[i++] = cp;
		}
		return result;

	}

	public static Vector<Integer> codepointsVector( String s ) {
		return codepointsVector( s, null );
	}

	public static Vector<Integer> codepointsVector( String s, Form form ) {
		Vector<Integer> v = new Vector<Integer>( );
		for ( Integer i : codepoints( s, form ).once( ) ) {
			v.add( i );
		}
		return v;
	}

	public static String codepointToString( int codePoint ) {
		StringBuilder sb = new StringBuilder( );
		char[] chars = Character.toChars( codePoint );
		sb.append( chars );
		return sb.toString( );
	}

	public static String codepointsToString( int ... codepoints ) {
		StringBuilder sb = new StringBuilder( );
		for ( int codePoint : codepoints ) {
			char[] chars = Character.toChars( codePoint );
			sb.append( chars );
		}
		return sb.toString( );
	}

	public static String codepointsToString( Iterable<Integer> codepoints ) {
		StringBuilder sb = new StringBuilder( );
		for ( int codePoint : codepoints ) {
			char[] chars = Character.toChars( codePoint );
			sb.append( chars );
		}
		return sb.toString( );
	}

	public static String codepointsIterableToString(
			Iterable<? extends Integer> codepoints ) {
		StringBuilder sb = new StringBuilder( );
		for ( int codePoint : codepoints ) {
			char[] chars = Character.toChars( codePoint );
			sb.append( chars );
		}
		return sb.toString( );
	}

	public static KnittingTuple<Integer> codepointsFequence( String s ) {
		return codepointsFequence( s, null );
	}

	public static KnittingTuple<Integer> codepointsFequence( String s, Form form ) {
		Vector<Integer> v = new Vector<Integer>( );
		for ( Integer i : codepoints( s, form ).once( ) ) {
			v.add( i );
		}
		return KnittingTuple.wrap( v );
	}

	public static String inspectCodepoints( String s ) {
		KnittingItterable<Integer> codepoints = codepoints( s );
		StringBuilder sb = new StringBuilder( );
		String separator = "";
		for ( Integer cp : codepoints.once( ) ) {
			char[] chars = Character.toChars( cp );
			sb.append( separator ).append( asUnicodeHex( cp ) );
			sb.append( " " ).append( chars );
			separator = " ";
		}
		return sb.toString( );
	}

	public static String asUnicodeHex( Integer c ) {
		String s =
				String.format( "%6s", Integer.toHexString( c ) ).replace( ' ', '0' )
						.toUpperCase( );
		if ( s.startsWith( "00" ) ) {
			return s.substring( 2 );
		}
		return s;
	}

	public static String pad( String s, int to ) {
		StringBuilder sb = new StringBuilder( );
		sb.append( s );
		for ( int length = s.length( ); length < to; length++ ) {
			sb.append( " " );
		}
		return sb.toString( );
	}

	public static Function<String, String> replacer( Pattern pattern,
			String replacement ) {
		return new Function<String, String>( ) {

			@Override
			public String apply( String t ) {
				return pattern.matcher( t ).replaceAll( replacement );
			}
		};

	}

	public static Function<String, Boolean> matcher( Pattern pattern ) {
		return new Function<String, Boolean>( ) {

			@Override
			public Boolean apply( String object ) {
				return pattern.matcher( object ).matches( );
			}
		};
	}

	public static ArrayMap<String, String> splitter( Pattern pattern ) {
		return new ArrayMap<String, String>( ) {

			@Override
			public String[] get( String input ) {
				return pattern.split( input );
			}
		};

	}
}