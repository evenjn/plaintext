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
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;

public final class FileFool {

	private Path root;

	public Path getRoot( ) {
		return root;
	}

	private FileFool(Path path) {
		if ( path != null ) {
			this.root = path.toAbsolutePath( ).normalize( );
		}
	}

	public static FileFool nu( Path path ) {
		return nu( path, false );
	}

	public static FileFool nu( Path path, boolean clear ) {
		FileFool ff = new FileFool( path );
		if ( clear ) {
			ff.remove( path, path );
		}
		return ff;
	}

	public static FileFool nu( ) {
		return new FileFool( null );
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
				if ( null != param.path.getParent( ) ) {
					Files.createDirectories( param.path.getParent( ) );
				}
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

	private SimpleFileVisitor<Path>
			createDeletingFileVisitor( Path except_this_directory ) {
		return new SimpleFileVisitor<Path>( ) {

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
					if ( except_this_directory == null
							|| !except_this_directory.equals( dir ) ) {
						try {
							Files.delete( dir );
						}
						catch ( IOException e ) {
							new RuntimeException( e );
						}
					}
					return FileVisitResult.CONTINUE;
				}
				else {
					throw new RuntimeException( exc );
				}
			}
		};
	}

	private void remove( Path path, Path except_this_directory ) {
		try {
			Files.walkFileTree( path,
					createDeletingFileVisitor( except_this_directory ) );
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	public void delete( Path path ) {
		if ( root == null ) {
			throw new IllegalStateException( "This file fool is read-only." );
		}
		path = path.toAbsolutePath( ).normalize( );
		if ( !path.startsWith( root ) ) {
			throw new IllegalStateException(
					"\n This file fool does not allow to write in this path:\n "
							+ path.toString( ) + "\n Writable root is:\n " + root.toString( )
							+ "\n" );
		}
		remove( path, null );
	}

	public FileFoolElement open( Path path ) {
		return new FileFoolElement( path );
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
