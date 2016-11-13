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

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.evenjn.yarn.ArrayMap;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorUnfold;
import org.github.evenjn.yarn.PastTheEndException;

public class Strings {

	public static String pad( String s, int to ) {
		StringBuilder sb = new StringBuilder( );
		sb.append( s );
		for ( int length = s.length( ); length < to; length++ ) {
			sb.append( " " );
		}
		return sb.toString( );
	}

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

	public static ArrayMap<String, String> splitter( Pattern pattern ) {
		return new ArrayMap<String, String>( ) {

			@Override
			public String[] get( String input ) {
				return pattern.split( input );
			}
		};

	}
}
