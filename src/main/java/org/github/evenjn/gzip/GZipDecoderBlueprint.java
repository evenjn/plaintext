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
package org.github.evenjn.gzip;

import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.github.evenjn.knit.Suppressor;
import org.github.evenjn.yarn.FunctionH;
import org.github.evenjn.yarn.Hook;

public class GZipDecoderBlueprint {

	public FunctionH<InputStream, InputStream> build( ) {
		return GZipDecoderBlueprint::decode;
	}

	private static InputStream decode( Hook hook, InputStream is ) {
		try {
			/**
			 * This never throws, it's not closeable.
			 */
			CompressorStreamFactory compressorStreamFactory =
					new CompressorStreamFactory( );

			/**
			 * This may throw. The result is autocloseable.
			 */
			CompressorInputStream createCompressorInputStream =
					compressorStreamFactory
							.createCompressorInputStream( CompressorStreamFactory.GZIP, is );

			return hook.hook( createCompressorInputStream );
		}
		catch ( CompressorException e ) {
			throw Suppressor.quit( e );
		}
	}
}
