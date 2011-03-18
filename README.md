popHealth CCR Importer
======================

This repository provides a set of tools for importing ASTM CCR's into popHealth

Building
--------

This Project is built with Maven.  	

	You need to add the LVG dist jar to your local maven repository
	Replace ${lvg-install} with the path to where you saved lvg jar
	mvn install:install-file -Dfile=${lvg-install}/lvg2011dist.jar -DgroupId=gov.nih.nlm -DartifactId=lvg.dist -Dversion=2011 -Dpackaging=jar

 > mvn package

NLM LVG
-------

The jar file in the nlm folder is public domain code from the [US National Library of Medicine](http://www.nlm.nih.gov/) [Lexical Systems Group](http://lexsrv3.nlm.nih.gov/LexSysGroup/Summary/lexicalTools.html). For more detailed information on their copyright and privacy policies, please see the [NLM disclaimer](http://www.nlm.nih.gov/disclaimer.html).


License
-------

Copyright 2011 The MITRE Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
