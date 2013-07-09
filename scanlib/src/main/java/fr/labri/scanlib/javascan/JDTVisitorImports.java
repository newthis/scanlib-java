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

import org.eclipse.jdt.core.dom.*;

import java.util.Set;

public class JDTVisitorImports extends ASTVisitor {

	private Set<String> qualifiedNames;

	public JDTVisitorImports(Set<String> actions) {
		super();
		this.qualifiedNames = actions;
	}

	public boolean visit(ImportDeclaration cic) {
		if(!cic.getName().getFullyQualifiedName().startsWith("java.") && !cic.getName().getFullyQualifiedName().startsWith("javax."))
			this.qualifiedNames.add(cic.getName().getFullyQualifiedName());
		return true;
	}

	public boolean visit(QualifiedName cic) {
		if(!cic.getName().getFullyQualifiedName().startsWith("java.") && !cic.getName().getFullyQualifiedName().startsWith("javax."))
			this.qualifiedNames.add(cic.getName().getFullyQualifiedName());
		return true;
	}

}
