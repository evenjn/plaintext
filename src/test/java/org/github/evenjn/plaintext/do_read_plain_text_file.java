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

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.github.evenjn.file.FileFool;
import org.github.evenjn.inputstream.Bzip2;
import org.github.evenjn.knit.BasicAutoHook;
import org.github.evenjn.unicode.Unicode;
import org.github.evenjn.yarn.AutoHook;

public class do_read_plain_text_file {

	public static void main( String[] args ) {
		try ( AutoHook hook = new BasicAutoHook( ) ) {
			Path path = Paths.get( "./src/test/resources/test.txt" );
			InputStream is = FileFool.nu( ).open( path ).read( hook );
			Unicode.read( hook, is ).consume( System.out::println );

			path = Paths.get( "./src/test/resources/test.txt.bz2" );
			is = FileFool.nu( ).open( path ).read( hook );
			is = Bzip2.decoder( ).get( hook, is );
			Unicode.read( hook, is ).consume( System.out::println );
		}
	}
}
