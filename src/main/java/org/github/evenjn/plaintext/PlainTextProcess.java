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
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.github.evenjn.yarn.AutoHook;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.EndOfCursorException;

public class PlainTextProcess {

	public static boolean run(
			Supplier<AutoHook> hf,
			Path workingDirectory,
			ProcessBuilder process_builder,
			int timeout_minutes )
			throws IOException,
			InterruptedException {
		return run(
				hf,
				workingDirectory,
				process_builder,
				standardOutput( true ),
				standardError( true ),
				timeout_minutes );
	}

	public static boolean print_debug = true;

	public static boolean run(
			Supplier<AutoHook> hf,
			Path workingDirectory,
			ProcessBuilder process_builder,
			Consumer<String> out_putter,
			Consumer<String> err_putter,
			int timeout_minutes )
			throws IOException, InterruptedException {

		process_builder.directory( workingDirectory.toFile( ) );
		if ( print_debug ) {
			System.out.println( "In working directory:\n\n" );
			System.out.println( workingDirectory.toString( ) );
			System.out.println( "\n" );
			System.out.println( "command:\n\n" );
			for ( String cl : process_builder.command( ) )
				System.out.println( cl + " \\" );
			System.out.println( "\n\n" );
		}

		Process process = process_builder.start( );
		final String[] bell = {};

		Thread thread1 = new Thread( new Runnable( ) {

			@Override
			public void run( ) {
				try ( AutoHook hook = hf.get( ) ) {
					Cursor<String> cursor = PlainText.reader( ).build( ).get( hook,
							process.getErrorStream( ) );

					for ( ;; ) {
						try {
							String next = cursor.next( );
							synchronized ( bell ) {
								err_putter.accept( next );
							}
						}
						catch ( EndOfCursorException e ) {
							break;
						}
					}
				}
				catch ( Throwable t ) {
					t.printStackTrace( System.err );
				}
			}
		} );

		Thread thread2 = new Thread( new Runnable( ) {

			@Override
			public void run( ) {
				try ( AutoHook hook = hf.get( ) ) {
					Cursor<String> cursor = PlainText.reader( ).build( ).get( hook,
							process.getInputStream( ) );

					for ( ;; ) {
						try {
							String next = cursor.next( );
							synchronized ( bell ) {
								out_putter.accept( next );
							}
						}
						catch ( EndOfCursorException e ) {
							break;
						}
					}
				}
				catch ( Throwable t ) {
					t.printStackTrace( System.err );
				}
			}
		} );

		thread2.start( );
		thread1.start( );
		boolean terminated =
				process.waitFor( timeout_minutes, TimeUnit.MINUTES );

		if ( !terminated ) {
			thread1.interrupt( );
			thread2.interrupt( );
			System.out.println( "Timeout! Trying to kill the process.." );
			process.destroy( );
			Thread.sleep( 2000 );
			if ( process.isAlive( ) ) {
				process.destroyForcibly( );
				Thread.sleep( 2000 );
			}
			if ( process.isAlive( ) ) {
				System.out.println( "Failed to kill the process." );
			}
			else {
				System.out.println( "Successfully killed the process." );
			}
			return false;
		}
		else {
			thread1.join( );
			thread2.join( );
			int exitvalue = process.exitValue( );
			if ( print_debug ) {
				System.out.println( "result is .. " + exitvalue );
			}
			return exitvalue == 0;
		}
	}

	private static Consumer<String> standardOutput( boolean insert_newline ) {
		return new Consumer<String>( ) {

			@Override
			public void accept( String value ) {
				if ( insert_newline ) {
					System.out.println( value );
				}
				else {
					System.out.print( value );
				}
				System.out.flush( );
			}
		};
	}

	private static Consumer<String> standardError( boolean insert_newline ) {
		return new Consumer<String>( ) {

			@Override
			public void accept( String value ) {
				if ( insert_newline ) {
					System.err.println( value );
				}
				else {
					System.err.print( value );
				}
				System.err.flush( );
			}
		};
	}
}
