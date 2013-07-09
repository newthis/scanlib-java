/*
 * Copyright 2013 Cédric Teyton
 * 
 * This file is part of Scanlib.
 * Scanlib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Praxis is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Praxis.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.labri.scanlib;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import fr.labri.scanlib.javascan.JDTGeneratorImports;
import fr.labri.scanlib.libfinder.ScanLib;

public class Launcher {

	public static void main(String[] args) {
		if(args.length<1) {
			System.out.println("Usage : arg0 path to the directory");
			return;
		}
		File f = new File(args[0]);
		if(!f.exists()) {
			System.err.println("File "+args[0]+" not found");
			return ;
		}
			
		JDTGeneratorImports gen = new JDTGeneratorImports(f.getAbsolutePath());
		System.out.println("******** Libraries Detection on "+args[0]);
		gen.scanDirectory();
		TreeSet<String> libraries = new TreeSet<String>(ScanLib.getInstance().getLibraries(gen.getQualifiedNames()));
		
		System.out.println("-------- Results ");
		for(String lib : libraries) {
			System.out.println(lib);
		}
	}
	
}
