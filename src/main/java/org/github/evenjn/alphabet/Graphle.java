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
package org.github.evenjn.alphabet;

import java.util.HashSet;
import java.util.Vector;

import org.github.evenjn.knit.KnittingTuple;
import org.github.evenjn.unicode.Unicode;
import org.github.evenjn.yarn.Tuple;

/**
 * A graphe is an atomic script element. Every graphe is an unicode codepoint,
 * but not every unicode codepoint is a graphe.
 * 
 * A graphle represents a sequence of graphes, which corresponds to the notion
 * of a ortographic word stripped of certain details such as uppercase and
 * lowercase.
 *
 */
public class Graphle implements
		Tuple<Integer> {

	private Graphle() {
	}

	static Graphle fromCodepoints( int length, int ... codepoints ) {
		Graphle runeTuple = new Graphle( );
		for ( int i = 0; i < length; i++ ) {
			runeTuple.codepoints.add( codepoints[i] );
			runeTuple.codepoints_set.add( codepoints[i] );
		}
		return runeTuple;
	}

	private Vector<Integer> codepoints = new Vector<>( );

	private HashSet<Integer> codepoints_set = new HashSet<>( );

	public String toString( ) {
		return Unicode.codepointsToString( codepoints );
	}

	@Override
	public int size( ) {
		return codepoints.size( );
	}

	public boolean isInAlphabet( Tuple<Integer> alphabet ) {
		for ( Integer i : codepoints ) {
			if ( !KnittingTuple.wrap( alphabet ).contains( i ) )
				return false;
		}
		return true;
	}

	public boolean contains( Object object ) {
		return codepoints_set.contains( object );
	}

	public boolean equals( Object other ) {
		return KnittingTuple.wrap( this ).equals( other );
	}

	public int hashCode( ) {
		return KnittingTuple.wrap( this ).hashCode( );
	}

	@Override
	public Integer get( int index ) {
		return codepoints.elementAt( index );
	}

}
