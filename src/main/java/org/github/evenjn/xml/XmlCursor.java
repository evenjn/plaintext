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
package org.github.evenjn.xml;

import java.io.Reader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.EndOfCursorException;

class XmlCursor implements
		Cursor<SuppressedXmlStreamElement> {

	private final SuppressedXmlStreamReader streamReader;

	public XmlCursor(Reader reader) {
		XMLInputFactory factory = XMLInputFactory.newInstance( );
		try {
			streamReader = new SuppressedXmlStreamReader(
					factory.createXMLStreamReader( reader ) );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public SuppressedXmlStreamElement next( )
			throws EndOfCursorException {
		if ( streamReader.hasNext( ) ) {
			streamReader.next( );
			return streamReader;
		}
		throw EndOfCursorException.neo();
	}
}
