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
package org.github.evenjn.unicode;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import org.github.evenjn.yarn.FunctionH;
import org.github.evenjn.yarn.Hook;

public class LineWriter implements
		FunctionH<OutputStream, Consumer<String>> {

	private Charset cs = Charset.forName( "UTF-8" );

	private String delimiter = null;

	public LineWriter setCharset( Charset cs ) {
		this.cs = cs;
		return this;
	}

	public LineWriter setDelimiter( String delimiter ) {
		this.delimiter = delimiter;
		return this;
	}

	@Override
	public Consumer<String> get( Hook hook, OutputStream output_stream ) {
		return Unicode.write_3( hook, output_stream, cs, delimiter );
	}

}
