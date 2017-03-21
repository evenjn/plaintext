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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.EndOfCursorException;
import org.github.evenjn.yarn.IteratorMap;

public class Strings {

	public static String zeropad( int length, int value ) {
		if ( length < 1 || length > 30 ) {
			throw new IllegalArgumentException(
					"Length must be at least 1 and at most 30." );
		}
		String result =
				"000000000000000000000000000000" + Integer.toString( value );
		result = result.substring( result.length( ) - length );
		return result;

	}

	public static String pad( String s, int to ) {
		StringBuilder sb = new StringBuilder( );
		sb.append( s );
		for ( int length = s.length( ); length < to; length++ ) {
			sb.append( " " );
		}
		return sb.toString( );
	}

	public static IteratorMap<String, String> extractor(
			Pattern opening_delimiter,
			Pattern closing_delimiter ) {
		return new IteratorMap<String, String>( ) {

			@Override
			public Iterator<String> get( String input ) {

				final Matcher opening_delimiter_matcher =
						opening_delimiter.matcher( input );
				final Matcher closing_delimiter_matcher =
						closing_delimiter.matcher( input );
				Cursor<String> curs = new Cursor<String>( ) {

					private int frontier;

					@Override
					public String next( )
							throws EndOfCursorException {
						boolean opening_delimiter_found =
								opening_delimiter_matcher.find( frontier );
						if ( !opening_delimiter_found )
							throw EndOfCursorException.neo();
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
				
				return new Iterator<String>( ) {
					String cached = null;
					boolean is_cached = false;
					boolean has_next = false;
					@Override
					public boolean hasNext( ) {
						if (!is_cached) {
							is_cached = true;
							try {
								cached = curs.next( );
								has_next = true;
							}
							catch ( EndOfCursorException e ) {
								has_next = false;
							}
						}
						return has_next;
					}

					@Override
					public String next( ) {
						if (hasNext( )) {
							return cached;
						}
						throw new NoSuchElementException( );
					}
				};
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

	public static Function<String, String> replacer( Pattern pattern,
			String replacement ) {
		return new Function<String, String>( ) {

			@Override
			public String apply( String t ) {
				return pattern.matcher( t ).replaceAll( replacement );
			}
		};

	}

}
