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
package org.github.evenjn.file;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;

import org.github.evenjn.yarn.Hook;

public class FileFool {

	public static FileFool nu( ) {
		return new FileFool( );
	}

	public boolean exists( Path path ) {
		return Files.exists( path );
	}

	public FileFoolCreation mold( Path path ) {
		return new FileFoolCreation( path );
	}

	public Path create( FileFoolCreation param ) {
		try {
			if ( param.erase && exists( param.path ) ) {
				delete( param.path );
			}
			if ( !param.as_directory ) {
				Files.createDirectories( param.path.getParent( ) );
				Files.createFile( param.path );
			}
			else {
				Files.createDirectories( param.path );
			}
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
		return param.path;
	}

	static void remove( Path path ) {

		SimpleFileVisitor<Path> simpleFileVisitor = new SimpleFileVisitor<Path>( ) {

			@Override
			public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) {
				try {
					Files.delete( file );
				}
				catch ( IOException e ) {
					new RuntimeException( e );
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed( Path file, IOException exc ) {
				try {
					Files.delete( file );
				}
				catch ( IOException e ) {
					new RuntimeException( e );
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory( Path dir, IOException exc ) {
				if ( exc == null ) {
					try {
						Files.delete( dir );
					}
					catch ( IOException e ) {
						new RuntimeException( e );
					}
					return FileVisitResult.CONTINUE;
				}
				else {
					throw new RuntimeException( exc );
				}
			}
		};

		try {
			Files.walkFileTree( path, simpleFileVisitor );
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	public void delete( Path path ) {
		remove( path.normalize( ) );
	}

	public FileFoolElement open( Path path ) {
		return new FileFoolElement( path );
	}

	public final static OutputStream eraseAndRewind( Hook hook,
			Path p ) {
		FileFool file = nu( );
		return file.open(
				file.create( file.mold( p ).eraseIfExists( ) ) )
				.write( hook );
	}

	public Iterable<Path> find( Path directory, String glob_pattern ) {
		LinkedList<Path> collected = new LinkedList<>( );
		PathMatcher matcher =
				FileSystems.getDefault( ).getPathMatcher( "glob:" + glob_pattern );
		SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>( ) {

			@Override
			public FileVisitResult visitFile( Path file,
					BasicFileAttributes attrs ) {
				Path name =
						directory.toAbsolutePath( ).relativize( file.toAbsolutePath( ) );
				if ( name != null && matcher.matches( name ) ) {
					collected.add( file );
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed( Path file,
					IOException exc ) {
				throw new RuntimeException( exc );
			}
		};
		try {
			Files.walkFileTree( directory, finder );
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
		return collected;
	}
}
