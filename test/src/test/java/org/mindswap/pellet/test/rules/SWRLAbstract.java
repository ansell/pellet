// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package org.mindswap.pellet.test.rules;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.mindswap.pellet.test.utils.TestUtils;
import org.semanticweb.owlapi.model.IRI;

import com.clarkparsia.owlapiv3.OntologyUtils;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * Title: SWRLAbstract
 * </p>
 * <p>
 * Description: Abstract class that is extended by SWRL test suites
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 * 
 * @author Markus Stocker
 */
public class SWRLAbstract {

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
	protected static String base;
    private File testDir;

	protected void test(String test) throws Exception {
		testJena(url(test + "-premise.rdf"), url(test + "-conclusion.rdf"));
		testOWLAPIv3(url(test + "-premise.rdf"), url(test + "-conclusion.rdf"));
	}

	private void testJena(String premiseURI, String conclusionURI) {
		OntModel premise = ModelFactory
				.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		premise.read(premiseURI);
		premise.prepare();

		Model conclusion = ModelFactory.createDefaultModel();
		conclusion.read(conclusionURI);
		
		StmtIterator stmtIter = conclusion.listStatements();

		while (stmtIter.hasNext()) {
			Statement s = stmtIter.nextStatement();
			assertTrue(premise.contains(s));
		}
	}
	
	private void testOWLAPIv3(String premiseURI, String conclusionURI) {
		org.semanticweb.owlapi.model.OWLOntologyManager manager = null;

		try {
			manager = org.semanticweb.owlapi.apibinding.OWLManager.createOWLOntologyManager();
			org.semanticweb.owlapi.model.OWLOntology premise = manager.loadOntologyFromOntologyDocument( IRI
					.create( premiseURI ) );
			manager = org.semanticweb.owlapi.apibinding.OWLManager.createOWLOntologyManager();
			org.semanticweb.owlapi.model.OWLOntology conclusion = manager.loadOntology( IRI
					.create( conclusionURI ) );

			PelletReasoner reasoner = new com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory()
					.createReasoner( premise );
			assertTrue( reasoner.isEntailed( conclusion.getAxioms() ) );
		} catch( org.semanticweb.owlapi.model.OWLOntologyCreationException e ) {
			throw new RuntimeException( e );
		}

	}

	private String url(String filename) throws Exception {
	    return "file:" + TestUtils.copyResourceToFile(testDir, base + filename);
	}
	
	@Before
	public void before() throws Exception {
	    testDir = tempDir.newFolder("swrlabstract");
	}
	
	@After
	public void after() {
		OntologyUtils.clearOWLOntologyManager();
	}
}
