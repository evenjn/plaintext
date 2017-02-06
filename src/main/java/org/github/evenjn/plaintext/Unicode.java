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

import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Iterator;
import java.util.Vector;

import org.github.evenjn.yarn.Tuple;

public class Unicode {

	public static Integer codepoint( String s ) {
		Integer result = null;
		for ( Integer i : codepoints( s ) ) {
			if ( result != null )
				throw new IllegalArgumentException(
						"The input string is realized using more than once codepoint." );
			result = i;
		}
		return result;
	}

	public static Iterable<Integer> codepoints( String s ) {
		return new Iterable<Integer>( ) {

			@Override
			public Iterator<Integer> iterator( ) {
				return s.codePoints( ).boxed( ).iterator( );
			}
		};
	}

	private static long size( Iterable<?> iterable ) {
		long result = 0;
		for ( Iterator<?> i = iterable.iterator( ); i.hasNext( ); i.next( ) ) {
			result++;
		}
		return result;
	}

	public static Iterable<Integer> codepoints( String s, Form form ) {
		return codepoints( form == null ? s : Normalizer.normalize( s, form ) );
	}

	public static int[] codepointsArray( String input, Form form ) {
		Iterable<Integer> codepoints = codepoints( input, form );
		long count = size( codepoints );
		int size = 0;
		if ( count < 0 || count > 9999 )
			throw new IllegalArgumentException( );
		size = (int) count;
		int[] result = new int[size];
		int i = 0;
		for ( Integer cp : codepoints ) {
			result[i++] = cp;
		}
		return result;
	}

	public static Tuple<Integer> codepointsTuple( String s, Form form ) {
		Vector<Integer> v = new Vector<Integer>( );
		for ( Integer i : codepoints( s, form ) ) {
			v.add( i );
		}
		return new Tuple<Integer>( ) {

			@Override
			public int size( ) {
				return v.size( );
			}

			@Override
			public Integer get( int index ) {
				return v.get( index );
			}
		};
	}

	public static Vector<Integer> codepointsVector( String s, Form form ) {
		Vector<Integer> v = new Vector<Integer>( );
		for ( Integer i : codepoints( s, form ) ) {
			v.add( i );
		}
		return v;
	}

	public static String string( int ... codepoints ) {
		StringBuilder sb = new StringBuilder( );
		for ( int codePoint : codepoints ) {
			char[] chars = Character.toChars( codePoint );
			sb.append( chars );
		}
		return sb.toString( );
	}

	public static String string( Iterable<Integer> codepoints ) {
		StringBuilder sb = new StringBuilder( );
		for ( int codePoint : codepoints ) {
			char[] chars = Character.toChars( codePoint );
			sb.append( chars );
		}
		return sb.toString( );
	}

	public static String aboutCodepoint( Integer cp ) {
		StringBuilder sb = new StringBuilder( );
		char[] chars = Character.toChars( cp );
		sb.append( asUnicodeHex( cp ) );
		sb.append( " " ).append( chars );
		sb.append( " " ).append( Character.getName( cp ) );
		return sb.toString( );
	}

	public static String inspectCodepoints( String s ) {
		if ( s.isEmpty( ) ) {
			return "the empty string";
		}
		Iterable<Integer> codepoints = codepoints( s );

		StringBuilder sb = new StringBuilder( );
		sb.append( size( codepoints( s ) ) );
		sb.append( " " );
		sb.append( s );
		sb.append( " " );
		String separator = "";
		for ( Integer cp : codepoints ) {
			char[] chars = Character.toChars( cp );
			sb.append( separator ).append( "U+" ).append( asUnicodeHex( cp ) );
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
	
	public static boolean isCodepointEncodableInCharset(Integer c, Charset cs) {
		return cs.newEncoder( ).canEncode( Unicode.string( c ) );
	}

}
