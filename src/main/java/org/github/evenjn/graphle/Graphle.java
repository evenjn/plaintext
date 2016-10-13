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
package org.github.evenjn.graphle;

import java.util.HashSet;
import java.util.Vector;

import org.github.evenjn.knit.KnittingTuple;
import org.github.evenjn.unicode.Unicode;
import org.github.evenjn.yarn.Tuple;

/**
 * A graphle represents a tuple of graphes, which corresponds to the notion of a
 * ortographic word stripped of certain details such as uppercase and lowercase.
 * 
 * It is invariant.
 * 
 * A graphe represents an atomic script element.
 * Every graphe is an unicode codepoint, but not every unicode codepoint is a graphe.
 *
 */
public final class Graphle implements
		Tuple<Integer>,
		Comparable<Graphle> {

	private final Vector<Integer> codepoints_vector = new Vector<>( );

	private final HashSet<Integer> codepoints_set = new HashSet<>( );

	private Graphle() {
	}

	public static Graphle fromCodepoints( int length, int ... codepoints ) {
		Graphle graphle = new Graphle( );
		for ( int i = 0; i < length; i++ ) {
			graphle.codepoints_vector.add( codepoints[i] );
			graphle.codepoints_set.add( codepoints[i] );
		}
		return graphle;
	}

	/**
	 * Creates a Graphle using the given string.
	 * It does not enforce any constraints on the codepoints.
	 * This is used to decode a graphle that was encoded into a string.
	 * Use language-specific functions to make sure the graphle does not contain
	 * undesired codepoints.
	 */
	public static Graphle decode( String s ) {
		Graphle graphle = new Graphle( );
		for ( int cp : Unicode.codepoints( s ).once( ) ) {
			graphle.codepoints_vector.add( cp );
			graphle.codepoints_set.add( cp );
		}
		return graphle;
	}

	public boolean contains( Object object ) {
		return codepoints_set.contains( object );
	}

	@Override
	public int compareTo( Graphle other ) {
		return encode( ).compareTo( other.encode( ) );
	}

	public String encode( ) {
		return Unicode.codepointsToString( codepoints_vector );
	}

	public boolean equals( Object other ) {
		return KnittingTuple.wrap( this ).equals( other );
	}

	public int hashCode( ) {
		return KnittingTuple.wrap( this ).hashCode( );
	}

	@Override
	public Integer get( int index ) {
		return codepoints_vector.elementAt( index );
	}

	@Override
	public int size( ) {
		return codepoints_vector.size( );
	}

	public String toString( ) {
		return encode( );
	}

}
