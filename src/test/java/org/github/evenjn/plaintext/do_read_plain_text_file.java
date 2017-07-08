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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.github.evenjn.bzip2.Bzip2;
import org.github.evenjn.yarn.AutoHook;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.EndOfCursorException;

public class do_read_plain_text_file {

	public static void main( String[] args ) throws IOException {
		try ( AutoHook hook = new BasicAutoHook( ) ) {
			Path path = Paths.get( "./src/test/resources/test.txt" );
			InputStream is = hook.hook( Files.newInputStream( path ) );
			Cursor<String> stream = PlainText.reader( ).build( ).get( hook, is );
			for ( ;; ) {
				try {
					System.out.println( stream.next( ) );
				}
				catch ( EndOfCursorException e ) {
					break;
				}
			}

			path = Paths.get( "./src/test/resources/test.txt.bz2" );
			is = hook.hook( Files.newInputStream( path ) );
			is = Bzip2.decoder( ).build( ).get( hook, is );
			stream = PlainText.reader( ).build( ).get( hook, is );
			for ( ;; ) {
				try {
					System.out.println( stream.next( ) );
				}
				catch ( EndOfCursorException e ) {
					break;
				}
			}
		}
	}
}
