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
package org.github.evenjn.bzip2;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.github.evenjn.yarn.FunctionH;
import org.github.evenjn.yarn.Hook;

public class Bzip2DecoderBlueprint {

	public FunctionH<InputStream, InputStream> build( ) {
		return Bzip2DecoderBlueprint::decode;
	}

	private static InputStream decode( Hook hook, InputStream stream ) {
		try {
			return hook.hook( new BZip2CompressorInputStream(
					hook.hook( new BufferedInputStream( stream ) ) ) );
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}
}
