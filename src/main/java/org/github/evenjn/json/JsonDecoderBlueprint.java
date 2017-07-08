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
package org.github.evenjn.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.github.evenjn.yarn.FunctionH;
import org.github.evenjn.yarn.Hook;

import com.google.gson.Gson;


public class JsonDecoderBlueprint<T> {

	public FunctionH<InputStream, T> build( ) {
		Charset local_cs = cs;
		Class<T> local_class_of_t = class_of_t;
		
		return new FunctionH<InputStream, T>( ) {

			@Override
			public T get( Hook hook, InputStream is ) {
				Reader reader = hook.hook( new InputStreamReader( is, local_cs ) );
				return new Gson( ).fromJson( reader, local_class_of_t );
			}
		} ;
	}
	
	private Charset cs = Charset.forName( "UTF-8" );

	public JsonDecoderBlueprint<T> setCharset( Charset cs ) {
		this.cs = cs;
		return this;
	}
	
	public JsonDecoderBlueprint(Class<T> class_of_t) {
		this.class_of_t = class_of_t;
	}
	
	private Class<T> class_of_t;

}
