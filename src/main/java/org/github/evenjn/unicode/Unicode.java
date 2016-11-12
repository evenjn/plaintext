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

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Iterator;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.evenjn.knit.KnittingCursable;
import org.github.evenjn.knit.KnittingCursor;
import org.github.evenjn.knit.KnittingTuple;
import org.github.evenjn.yarn.ArrayMap;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorUnfold;
import org.github.evenjn.yarn.PastTheEndException;
import org.github.evenjn.yarn.Tuple;

public class Unicode {

	public static CursorUnfold<String, String> extractor(
			Pattern opening_delimiter,
			Pattern closing_delimiter ) {
		return new CursorUnfold<String, String>( ) {

			@Override
			public Cursor<String> next( String input ) {
				final Matcher opening_delimiter_matcher =
						opening_delimiter.matcher( input );
				final Matcher closing_delimiter_matcher =
						closing_delimiter.matcher( input );
				return new Cursor<String>( ) {

					private int frontier;

					@Override
					public String next( )
							throws PastTheEndException {

						boolean opening_delimiter_found =
								opening_delimiter_matcher.find( frontier );
						if ( !opening_delimiter_found )
							throw PastTheEndException.neo;
						int opening_delimiter_end = opening_delimiter_matcher.end( );
						boolean closing_delimiter_found =
								closing_delimiter_matcher.find( opening_delimiter_end );
						if ( !closing_delimiter_found )
							throw new IllegalArgumentException(
									"found opening delimiter, but no closing delimiter." );
						int closing_delimiter_start = closing_delimiter_matcher.start( );
						frontier = closing_delimiter_matcher.end( );
						return input.substring( opening_delimiter_end,
								closing_delimiter_start );
					}

				};
			}


		};
	}

	public static Integer codepoint( String s ) {
		Integer result = null;
		for ( Integer i : KnittingCursor.wrap(codepoints( s ).iterator( )).once( ) ) {
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

	public static Iterable<Integer> codepoints( String s, Form form ) {
		return codepoints( form == null ? s : Normalizer.normalize( s, form ) );
	}

	public static int[] codepointsArray( String input, Form form ) {
		Iterable<Integer> codepoints = codepoints( input, form );
		long count =  KnittingCursable.wrap(codepoints).size( );
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

	public static Vector<Integer> codepointsVector( String s ) {
		return codepointsVector( s, null );
	}

	public static Vector<Integer> codepointsVector( String s, Form form ) {
		Vector<Integer> v = new Vector<Integer>( );
		for ( Integer i : codepoints( s, form ) ) {
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

	public static Tuple<Integer> codepointsTuple( String s ) {
		return codepointsTuple( s, null );
	}

	public static Tuple<Integer> codepointsTuple( String s, Form form ) {
		Vector<Integer> v = new Vector<Integer>( );
		for ( Integer i : codepoints( s, form ) ) {
			v.add( i );
		}
		return KnittingTuple.wrap( v );
	}

	public static String aboutCodepoint( Integer cp ) {
		StringBuilder sb = new StringBuilder( );
		char[] chars = Character.toChars( cp );
		sb.append( asUnicodeHex( cp ) );
		sb.append( " " ).append( chars );
		return sb.toString( );
	}

	public static String inspectCodepoints( String s ) {
		if ( s.isEmpty( ) ) {
			return "the empty string";
		}
		Iterable<Integer> codepoints = codepoints( s );

		StringBuilder sb = new StringBuilder( );
		sb.append( KnittingCursor.wrap(codepoints( s )).size( ) );
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

	public static Predicate<String> matcher( Pattern pattern ) {
		return new Predicate<String>( ) {

			@Override
			public boolean test( String object ) {
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
