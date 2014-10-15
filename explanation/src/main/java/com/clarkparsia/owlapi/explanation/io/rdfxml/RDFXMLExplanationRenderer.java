// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package com.clarkparsia.owlapi.explanation.io.rdfxml;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.RDFXMLRenderer;

import com.clarkparsia.owlapi.explanation.io.ExplanationRenderer;
import com.clarkparsia.owlapiv3.OWL;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 * 
 * @author Evren Sirin
 */
public class RDFXMLExplanationRenderer implements ExplanationRenderer {
	private Set<OWLAxiom>	axioms;

	private Writer			writer;

	@Override
    public void startRendering(Writer writer) {
		this.writer = writer;
		axioms = new HashSet<OWLAxiom>();
	}

	@Override
    public void render(OWLAxiom axiom, Set<Set<OWLAxiom>> explanations) {		
		axioms.add( axiom );
		
		for( Set<OWLAxiom> explanation : explanations ) {
			axioms.addAll( explanation );
		}
	}

	@Override
    public void endRendering() throws IOException {
		OWLOntology ontology = OWL.Ontology( axioms );
        RDFXMLRenderer renderer = new RDFXMLRenderer(ontology, writer);
		renderer.render();
	}

}
