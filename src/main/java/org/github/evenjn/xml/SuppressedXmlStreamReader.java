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

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class SuppressedXmlStreamReader implements
		XMLStreamReader,
		SuppressedXmlStreamElement {

	private XMLStreamReader wrapped;

	public SuppressedXmlStreamReader(XMLStreamReader reader) {
		this.wrapped = reader;
	}

	@Override
	public Object getProperty( String name )
			throws IllegalArgumentException {
		return wrapped.getProperty( name );
	}

	@Override
	public int next( ) {
		try {
			return wrapped.next( );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public void require( int type, String namespaceURI, String localName ) {
		try {
			wrapped.require( type, namespaceURI, localName );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}

	}

	@Override
	public String getElementText( ) {
		try {
			return wrapped.getElementText( );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public int nextTag( ) {
		try {
			return wrapped.nextTag( );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public boolean hasNext( ) {
		try {
			return wrapped.hasNext( );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public void close( ) {
		try {
			wrapped.close( );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public String getNamespaceURI( String prefix ) {
		return wrapped.getNamespaceURI( prefix );
	}

	@Override
	public boolean isStartElement( ) {
		return wrapped.isStartElement( );
	}

	@Override
	public boolean isEndElement( ) {
		return wrapped.isEndElement( );
	}

	@Override
	public boolean isCharacters( ) {
		return wrapped.isCharacters( );
	}

	@Override
	public boolean isWhiteSpace( ) {
		return wrapped.isWhiteSpace( );
	}

	@Override
	public String getAttributeValue( String namespaceURI, String localName ) {
		return wrapped.getAttributeValue( namespaceURI, localName );
	}

	@Override
	public int getAttributeCount( ) {
		return wrapped.getAttributeCount( );
	}

	@Override
	public QName getAttributeName( int index ) {
		return wrapped.getAttributeName( index );
	}

	@Override
	public String getAttributeNamespace( int index ) {
		return wrapped.getAttributeNamespace( index );
	}

	@Override
	public String getAttributeLocalName( int index ) {
		return wrapped.getAttributeLocalName( index );
	}

	@Override
	public String getAttributePrefix( int index ) {
		return wrapped.getAttributePrefix( index );
	}

	@Override
	public String getAttributeType( int index ) {
		return wrapped.getAttributeType( index );
	}

	@Override
	public String getAttributeValue( int index ) {
		return wrapped.getAttributeValue( index );
	}

	@Override
	public boolean isAttributeSpecified( int index ) {
		return wrapped.isAttributeSpecified( index );
	}

	@Override
	public int getNamespaceCount( ) {
		return wrapped.getNamespaceCount( );
	}

	@Override
	public String getNamespacePrefix( int index ) {
		return wrapped.getNamespacePrefix( index );
	}

	@Override
	public String getNamespaceURI( int index ) {
		return wrapped.getNamespaceURI( index );
	}

	@Override
	public NamespaceContext getNamespaceContext( ) {
		return wrapped.getNamespaceContext( );
	}

	@Override
	public int getEventType( ) {
		return wrapped.getEventType( );
	}

	public String getEventTypeAsString( ) {
		switch ( wrapped.getEventType( ) ) {
			case XMLStreamConstants.ATTRIBUTE:
				return "ATTRIBUTE";
			case XMLStreamConstants.CDATA:
				return "CDATA";
			case XMLStreamConstants.CHARACTERS:
				return "CHARACTERS";
			case XMLStreamConstants.COMMENT:
				return "COMMENT";
			case XMLStreamConstants.DTD:
				return "DTD";
			case XMLStreamConstants.END_DOCUMENT:
				return "END_DOCUMENT";
			case XMLStreamConstants.END_ELEMENT:
				return "END_ELEMENT";
			case XMLStreamConstants.ENTITY_DECLARATION:
				return "ENTITY_DECLARATION";
			case XMLStreamConstants.ENTITY_REFERENCE:
				return "ENTITY_REFERENCE";
			case XMLStreamConstants.NAMESPACE:
				return "NAMESPACE";
			case XMLStreamConstants.NOTATION_DECLARATION:
				return "NOTATION_DECLARATION";
			case XMLStreamConstants.PROCESSING_INSTRUCTION:
				return "PROCESSING_INSTRUCTION";
			case XMLStreamConstants.SPACE:
				return "SPACE";
			case XMLStreamConstants.START_DOCUMENT:
				return "START_DOCUMENT";
			case XMLStreamConstants.START_ELEMENT:
				return "START_ELEMENT";
			default:
				throw new IllegalArgumentException( );
		}
	}

	@Override
	public String getText( ) {
		return wrapped.getText( );
	}

	@Override
	public char[] getTextCharacters( ) {
		return wrapped.getTextCharacters( );
	}

	@Override
	public int getTextCharacters( int sourceStart, char[] target,
			int targetStart, int length ) {
		try {
			return wrapped.getTextCharacters( sourceStart, target, targetStart,
					length );
		}
		catch ( XMLStreamException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public int getTextStart( ) {
		return wrapped.getTextStart( );
	}

	@Override
	public int getTextLength( ) {
		return wrapped.getTextLength( );
	}

	@Override
	public String getEncoding( ) {
		return wrapped.getEncoding( );
	}

	@Override
	public boolean hasText( ) {
		return wrapped.hasText( );
	}

	@Override
	public Location getLocation( ) {
		return wrapped.getLocation( );
	}

	@Override
	public QName getName( ) {
		return wrapped.getName( );
	}

	@Override
	public String getLocalName( ) {
		return wrapped.getLocalName( );
	}

	@Override
	public boolean hasName( ) {
		return wrapped.hasName( );
	}

	@Override
	public String getNamespaceURI( ) {
		return wrapped.getNamespaceURI( );
	}

	@Override
	public String getPrefix( ) {
		return wrapped.getPrefix( );
	}

	@Override
	public String getVersion( ) {
		return wrapped.getVersion( );
	}

	@Override
	public boolean isStandalone( ) {
		return wrapped.isStandalone( );
	}

	@Override
	public boolean standaloneSet( ) {
		return wrapped.standaloneSet( );
	}

	@Override
	public String getCharacterEncodingScheme( ) {
		return wrapped.getCharacterEncodingScheme( );
	}

	@Override
	public String getPITarget( ) {
		return wrapped.getPITarget( );
	}

	@Override
	public String getPIData( ) {
		return wrapped.getPIData( );
	}

}
