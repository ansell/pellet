// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package com.clarkparsia.sparqlowl.parser.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.jena.JenaLoader;
import org.mindswap.pellet.test.PelletTestSuite;
import org.mindswap.pellet.test.utils.TestUtils;
import org.mindswap.pellet.utils.FileUtils;

import com.clarkparsia.pellet.sparqldl.parser.ARQParser;
import com.clarkparsia.sparqlowl.parser.arq.ARQTerpParser;
import com.clarkparsia.sparqlowl.parser.arq.TerpSyntax;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 * 
 * @author Evren Sirin
 */
@RunWith(Parameterized.class)
public class ParserTestParentQueries {
    
    @Rule
    public Timeout timeout = new Timeout(10000);
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
    private File testDir;
    
	public static final String base = PelletTestSuite.base + "sparqldl-tests/simple/";
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter( ParserTestParentQueries.class );
	}
	
	@Parameters
	public static Collection<Object[]> getParameters() {
		Collection<Object[]> parameters = new ArrayList<Object[]>();
		parameters.add( new Object[] { "parent1.rq", "parent1.sparqlowl" } );
		parameters.add( new Object[] { "parent2.rq", "parent2.sparqlowl" } );
		parameters.add( new Object[] { "parent3.rq", "parent3.sparqlowl" } );
		parameters.add( new Object[] { "parent4.rq", "parent4.sparqlowl" } );
		parameters.add( new Object[] { "parent5.rq", "parent5.sparqlowl" } );
		parameters.add( new Object[] { "parent6.rq", "parent6.sparqlowl" } );
		parameters.add( new Object[] { "parent7.rq", "parent7.sparqlowl" } );
		parameters.add( new Object[] { "parent8.rq", "parent8.sparqlowl" } );
		parameters.add( new Object[] { "parent9.rq", "parent9.sparqlowl" } );
		//parameters.add( new Object[] { "parent10.rq", "parent10.sparqlowl" } );	//Not supported
		return parameters;
	}

	private KnowledgeBase kb;
	private ARQParser parser;
	
	private String sparqlFile;
	private String sparqlOWLFile;
	
	public ParserTestParentQueries(String sparqlFile, String sparqlOWLFile) {
		this.sparqlFile = sparqlFile;
		this.sparqlOWLFile = sparqlOWLFile;
	}
	
	@BeforeClass
	public static void beforeClass() {
		ARQTerpParser.registerFactory();
		
	}
	
	@AfterClass
	public static void afterClass() {
		ARQTerpParser.unregisterFactory();
	}
	
    @Before
    public void before() throws Exception {
        testDir = tempDir.newFolder("sparqlowl-parsertestparent");
        kb = new JenaLoader().createKB(TestUtils.copyResourceToFile(testDir, base + "parent.ttl"));
        parser = new ARQParser();
    }
	
    @After
    public void after() {
        kb = null;
        parser = null;
    }
    
	@Test
	public void compareQuery() throws Exception {
		Query sparql = QueryFactory.create( FileUtils.readFile( TestUtils.copyResourceToFile(testDir, base + sparqlFile) ), Syntax.syntaxSPARQL );
		com.clarkparsia.pellet.sparqldl.model.Query expected = parser.parse( sparql, kb );
		
		Query sparqlOWL = QueryFactory.create( FileUtils.readFile( TestUtils.copyResourceToFile(testDir, base + sparqlOWLFile) ), TerpSyntax.getInstance() );		
		com.clarkparsia.pellet.sparqldl.model.Query actual = parser.parse( sparqlOWL, kb );
		
		assertEquals( expected.getAtoms(), actual.getAtoms() );
	}
}
