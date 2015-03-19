# Introduction #

**Scanlib** is an experimental prototype written in Java designed to scan a Java project and detect the third-party libraries it uses.

# Background #

The principle is straightforward. Given a dictionary of pairs "keyword - library", e.g.
```
   org.apache.commons.logging :: commons-logging
   org.junit. :: junit
```

Scanlib will attempt to match in any source code directory the set of libraries that are used.

We have build a database of regular expressions to detect the libraries, based on the packages name of their JAR files. The current index is available [here](http://www.labri.fr/perso/cteyton/ScanLib/scanlib.html).

# Usage #

## Installation ##

To start using Scanlib, you have to include first the following in the pom.xml :

```
<repositories>
   <repository>
       <id>se</id>
       <name>Sphere repository</name>
       <url>http://se.labri.fr/maven</url>
   </repository>
</repositories>
	
<dependencies>
    <dependency>
	<groupId>fr.labri.scanlib</groupId>
	<artifactId>scanlib</artifactId>
        <version>0.1</version>
    </dependency>
</dependencies>
```

## First usage ##

The basic usage of Scanlib stands in two simple lines :

```
String dir = "/path/to/mydirectory/";
Set<String> libs = ScanLib.getInstance().computeLibraries(dir);
```

First call to "ScanLib.getInstance()" will load the self-contained database.

## Add/Remove Content ##

It is possible to dynamically add or remove entries of the database :

```
ScanLib.getInstance().remove("org.junit.");
ScanLib.getInstance().add("org.testng.","testng");
```

## Search Content ##

You can also search either a keyword or a library in the database :

```
Set<String> res = ScanLib.getInstance().searchKeyword("org.junit.");
Set<String> res = ScanLib.getInstance().searchLibraries("testng");
```

## Build your own Dictionary ##

If you prefer to provide your own database of keywords, you only have to write an XML file with the following format :

```
<scanlib>
	<lib id="JDK">
		<kw>java.util.</kw>
		<kw>java.lang.</kw>
	</lib>
</scanlib>
```

To then use it you code, just consider the following code :

```
ScanLib.buildFromFile("scanlib-dico.xml");
//As previously
Set<String> libs = ScanLib.getInstance().computeLibraries(dir);
```

You can also add and remove entries in the database and make the new dictionary persistent :

```
ScanLib.buildFromFile("mydico.xml");
ScanLib.getInstance().add("org.testng.","testng");
ScanLib.getInstance().saveDB("mydico.xml");
```

You will obtain a new version of the XML File :

```
<scanlib>
	<lib id="JDK">
		<kw>java.util.</kw>
		<kw>java.lang.</kw>
	</lib>
	<lib id="testng">
		<kw>org.testng.</kw>
	</lib>
</scanlib>
```

# Notes #

This utility tool is developed by Cédric Teyton, a Phd student at [Software Engineering research group](http://se.labri.fr), LaBRI, Bordeaux, France.

Feel free to contact us, cteyton AT labri.fr, for any remark, suggestion or feedback.