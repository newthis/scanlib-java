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

package fr.labri.scanlib.javascan;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

public class JDTGeneratorImports {

	protected static Logger logger = Logger.getLogger("fr.labri.scanlib.javascan.JDTGeneratorImports");
	
	private String localpath = "";
	
	public JDTGeneratorImports(String path) {
		localpath = path;
		qualifiedNames = new HashSet<String>();
	}

	private Set<String> qualifiedNames;

	public Set<String> getQualifiedNames() {
		return qualifiedNames;
	}


	@SuppressWarnings("unchecked")
	public Set<String> generate(String file) {
	
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		String dir = new File(file).getAbsoluteFile().getParentFile().getAbsolutePath();
        parser.setEnvironment(new String[]{dir},new String[]{dir},null,true);
		parser.setResolveBindings(false);
		parser.setBindingsRecovery(false);
		parser.setStatementsRecovery(true);
		parser.setIgnoreMethodBodies(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		Map<String,String> pOptions = JavaCore.getOptions();
		pOptions.put(JavaCore.COMPILER_COMPLIANCE,JavaCore.VERSION_1_7);
		pOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,JavaCore.VERSION_1_7);
		pOptions.put(JavaCore.COMPILER_SOURCE,JavaCore.VERSION_1_7);
		parser.setCompilerOptions(pOptions);

		JDTRequestorImports req = new JDTRequestorImports();
		parser.createASTs(new String[] { file },null,new String[] {},req,null);
		return req.getImports();
	}

	public boolean handleFile(String file) {
		return file.endsWith(".java");
	}
	
	public void scanDirectory() {
		scanDirectory(this.localpath);
	}
	
	public void scanDirectory(String path) {
		javaxt.io.Directory directory = new javaxt.io.Directory(path);
		javaxt.io.File[] files = directory.getFiles("*.java", true);
		for(javaxt.io.File file : files){
			if(!file.isHidden())
				generate(file.getPath()+file.getName());
		}
	}



}
