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

import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringWithNumberComparator implements
		Comparator<String> {

	public static final String digitsGroup = "([0-9]{0,10})";

	/**
	 * @param pattern
	 *          A pattern that identifies a single subgroup, and the subgroup is a
	 *          sequence of digits (StringWithNumberComparator.digitsGroup).
	 */
	public static Comparator<String> nu( Pattern pattern ) {
		return new StringWithNumberComparator( pattern );
	}

	private Pattern pattern;

	private StringWithNumberComparator(Pattern pattern) {
		this.pattern = pattern;
	}

	private int extractInt( String s ) {
		Matcher matcher1 = pattern.matcher( s );
		if ( !matcher1.matches( ) ) {
			throw new IllegalArgumentException( );
		}
		String digits1 = matcher1.group( 1 );
		int i1 = Integer.parseInt( digits1 );
		return i1;
	}

	private static Optional<Integer> compareNullFirst( Object o1, Object o2 ) {
		if ( o1 == null ) {
			if ( o2 == null )
				return Optional.of( 0 );
			return Optional.of( Integer.valueOf( -1 ) );
		}
		if ( o2 == null )
			return Optional.of( Integer.valueOf( 1 ) );
		return Optional.empty( );
	}

	@Override
	public int compare( String o1, String o2 ) {
		Optional<Integer> compareNullFirst = compareNullFirst( o1, o2 );
		if ( compareNullFirst.isPresent( ) ) {
			return compareNullFirst.get( );
		}
		return Integer.compare( extractInt( o1 ), extractInt( o2 ) );
	}

}
