/*
 * Copyright 2013 Cédric Teyton
 * 
 * This file is part of Scanlib.
 * Scanlib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Scanlib is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Scanlib.  If not, see <http://www.gnu.org/licenses/>.
 */


package fr.labri.scanlib.javascan;


import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

public class JDTRequestorImports extends FileASTRequestor {
	
	private Set<String> qualifiedNames;

	public JDTRequestorImports() {
		super();
		this.qualifiedNames = new HashSet<String>();
	}
	
	public void acceptAST(String sourceFilePath, CompilationUnit ast) {
		ast.accept(new JDTVisitorImports(qualifiedNames));
	}
	
	public Set<String> getImports() {
		return qualifiedNames;
	}

}
