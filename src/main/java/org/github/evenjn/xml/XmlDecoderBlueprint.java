/**
 *
 * Copyright 2017 Marco Trevisan
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
package org.github.evenjn.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.CursorMapH;
import org.github.evenjn.yarn.Hook;

public class XmlDecoderBlueprint {

	public CursorMapH<InputStream, SuppressedXmlStreamElement> build( ) {
		Charset local_cs = cs;
		return new CursorMapH<InputStream, SuppressedXmlStreamElement>( ) {

			@Override
			public Cursor<SuppressedXmlStreamElement> get( Hook h,
					InputStream input ) {
				return new XmlCursor(
						h.hook( new InputStreamReader( input, local_cs ) ) );
			}

		};
	}

	private Charset cs = Charset.forName( "UTF-8" );

	public XmlDecoderBlueprint setCharset( Charset cs ) {
		this.cs = cs;
		return this;
	}

}
