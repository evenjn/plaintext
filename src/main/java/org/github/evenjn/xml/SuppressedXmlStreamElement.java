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

public interface SuppressedXmlStreamElement {

	Object getProperty( String name );

	String getElementText( );

	boolean isStartElement( );

	boolean isEndElement( );

	boolean isCharacters( );

	boolean isWhiteSpace( );

	String getAttributeValue( String namespaceURI, String localName );

	int getAttributeCount( );

	QName getAttributeName( int index );

	String getAttributeNamespace( int index );

	String getAttributeLocalName( int index );

	String getAttributePrefix( int index );

	String getAttributeType( int index );

	String getAttributeValue( int index );

	boolean isAttributeSpecified( int index );

	int getNamespaceCount( );

	String getNamespacePrefix( int index );

	String getNamespaceURI( int index );

	NamespaceContext getNamespaceContext( );

	int getEventType( );

	String getEventTypeAsString( );

	String getText( );

	char[] getTextCharacters( );

	int getTextCharacters( int sourceStart, char[] target,
			int targetStart, int length );

	int getTextStart( );

	int getTextLength( );

	String getEncoding( );

	boolean hasText( );

	Location getLocation( );

	QName getName( );

	String getLocalName( );

	boolean hasName( );

	String getNamespaceURI( );

	String getPrefix( );

	String getVersion( );

	boolean isStandalone( );

	boolean standaloneSet( );

	String getCharacterEncodingScheme( );

	String getPITarget( );

	String getPIData( );

}
