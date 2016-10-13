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

import java.text.Normalizer;

import org.github.evenjn.unicode.Unicode;
import org.github.evenjn.yarn.SkipException;
import org.github.evenjn.yarn.SkipMap;

public class GraphlerEN {

	public static SkipMap<String, Graphle> skipMap( ) {
		return GraphlerEN::graphle;
	}

	public static Graphle graphle( String string )
			throws SkipException {
		int[] codepointsArray = Unicode.codepointsArray( string, Normalizer.Form.NFD );
		int[] result = new int[codepointsArray.length];
		int len = 0;
		for ( int j = 0; j < codepointsArray.length; j++ ) {
			int codepoint = codepointsArray[j];
			int tmp = 0;
			switch ( codepoint ) {

				/* ' */
				case 0x0027:
				case 0x2019:
					tmp = 0x0027;
					break;

				/* A */
				case 0x0041:
				case 0x0061:
					tmp = 0x0041;
					break;

				/* B */
				case 0x0042:
				case 0x0062:
					tmp = 0x0042;
					break;

				/* C */
				case 0x0043:
				case 0x0063:
					tmp = 0x0043;
					break;

				/* D */
				case 0x0044:
				case 0x0064:
					tmp = 0x0044;
					break;

				/* E */
				case 0x0045:
				case 0x0065:
					tmp = 0x0045;
					break;

				/* F */
				case 0x0046:
				case 0x0066:
					tmp = 0x0046;
					break;

				/* G */
				case 0x0047:
				case 0x0067:
					tmp = 0x0047;
					break;

				/* H */
				case 0x0048:
				case 0x0068:
					tmp = 0x0048;
					break;

				/* I */
				case 0x0049:
				case 0x0069:
					tmp = 0x0049;
					break;

				/* J */
				case 0x004A:
				case 0x006A:
					tmp = 0x004A;
					break;

				/* K */
				case 0x004B:
				case 0x006B:
					tmp = 0x004B;
					break;

				/* L */
				case 0x004C:
				case 0x006C:
					tmp = 0x004C;
					break;

				/* M */
				case 0x004D:
				case 0x006D:
					tmp = 0x004D;
					break;

				/* N */
				case 0x004E:
				case 0x006E:
					tmp = 0x004E;
					break;

				/* O */
				case 0x004F:
				case 0x006F:
					tmp = 0x004F;
					break;

				/* P */
				case 0x0050:
				case 0x0070:
					tmp = 0x0050;
					break;

				/* Q */
				case 0x0051:
				case 0x0071:
					tmp = 0x0051;
					break;

				/* R */
				case 0x0052:
				case 0x0072:
					tmp = 0x0052;
					break;

				/* S */
				case 0x0053:
				case 0x0073:
					tmp = 0x0053;
					break;

				/* T */
				case 0x0054:
				case 0x0074:
					tmp = 0x0054;
					break;

				/* U */
				case 0x0055:
				case 0x0075:
					tmp = 0x0055;
					break;

				/* V */
				case 0x0056:
				case 0x0076:
					tmp = 0x0056;
					break;

				/* W */
				case 0x0057:
				case 0x0077:
					tmp = 0x0057;
					break;

				/* X */
				case 0x0058:
				case 0x0078:
					tmp = 0x0058;
					break;

				/* Y */
				case 0x0059:
				case 0x0079:
					tmp = 0x0059;
					break;

				/* Z */
				case 0x005A:
				case 0x007A:
					tmp = 0x005A;
					break;

				default:
					throw SkipException.neo;
			}
			result[len++] = tmp;
		}

		return Graphle.fromCodepoints( len, result );
	}
}
