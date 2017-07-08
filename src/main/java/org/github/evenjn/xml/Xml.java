/**
 *
 * Copyright 2017 Marco Trevisan
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package org.github.evenjn.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.github.evenjn.yarn.Cursable;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.Hook;

public class Xml {

	public static XmlDecoderBlueprint decoder( ) {
		return new XmlDecoderBlueprint( );
	}

	static public Cursable<SuppressedXmlStreamElement> fileRead( String file ) {
		Path path = Paths.get( file );
		return new Cursable<SuppressedXmlStreamElement>( ) {

			@Override
			public Cursor<SuppressedXmlStreamElement> pull( Hook hook ) {
				try {
					InputStream is = hook.hook( Files.newInputStream( path ) );
					return new XmlDecoderBlueprint( ).build().get( hook, is );
				}
				catch ( IOException e ) {
					throw new RuntimeException( e );
				}
			}
		};
	}
}
