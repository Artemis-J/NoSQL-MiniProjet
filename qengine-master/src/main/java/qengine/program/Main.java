package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
final class Main {
	static final String baseURI = null;

	/**
	 * Votre répertoire de travail où vont se trouver les fichiers à lire
	 */
	static final String workingDir = "data/";

	/**
	 * Fichier contenant les requêtes sparql
	 */
	static final String queryFile = workingDir + "sample_query.queryset";

	/**
	 * Fichier contenant des données rdf
	 */
	static final String dataFile = workingDir + "sample_data.nt";

	static  Map<String,Integer> myDictionary;
	static Map<Integer,Map<Integer, ArrayList<Integer>>> SPO;
	static Map<Integer,Map<Integer, ArrayList<Integer>>> POS;
	static Map<Integer,Map<Integer, ArrayList<Integer>>> OSP;
	static Map<Integer,Map<Integer, ArrayList<Integer>>> SOP;
	static Map<Integer,Map<Integer, ArrayList<Integer>>> PSO;
	static Map<Integer,Map<Integer, ArrayList<Integer>>> OPS;


	// ========================================================================

	/**
	 * Méthode utilisée ici lors du parsing de requête sparql pour agir sur l'objet obtenu.
	 */
	/**
	 * REQUETE:
	 * SELECT ?v0 WHERE {
	 *     ?v0 <http://schema.org/birthDate> "1988-09-24" .
	 *     ?v0 <http://db.uwaterloo.ca/~galuc/wsdbm/follows> <http://db.uwaterloo.ca/~galuc/wsdbm/User240>}
	 */
	public static void processAQuery(ParsedQuery query) {

		Set<Integer> s1 = new HashSet<>();
		Set<Integer> s2 = new HashSet<>();
		Map<String, Integer> myDict = MyRDFHandler.getMyDictionary();
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());

		Integer o1 = myDict.get(patterns.get(0).getObjectVar().getValue().toString());
		Integer p1 = myDict.get(patterns.get(0).getPredicateVar().getValue().toString());
		s1.addAll(OPS.get(o1).get(p1));

		Integer o2 = myDict.get(patterns.get(1).getObjectVar().getValue().toString());
		Integer p2 = myDict.get(patterns.get(1).getPredicateVar().getValue().toString());
		s2.addAll(OPS.get(o2).get(p2));

		System.out.println("L'ensemble de s1:"+s1);
		System.out.println("L'ensemble de s2:"+s2);

		s2.retainAll(s1);

		System.out.println("Le résultat de requête est:"+s2);

	}

	/**
	 * Entrée du programme
	 */
	public static void main(String[] args) throws Exception {
		myData();
		parseQueries();

//		System.out.println("Dictionary:"+myDictionary);
//		System.out.println(SPO);
//		System.out.println(POS);
//		System.out.println(OSP);
//		System.out.println(SOP);
//		System.out.println(PSO);
//		System.out.println(OPS);
	}

	// ========================================================================

	/**
	 * Traite chaque requête lue dans {@link #queryFile} avec {@link #processAQuery(ParsedQuery)}.
	 */
	private static void parseQueries() throws FileNotFoundException, IOException {
		/**
		 * Try-with-resources
		 * 
		 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
		 */
		/*
		 * On utilise un stream pour lire les lignes une par une, sans avoir à toutes les stocker
		 * entièrement dans une collection.
		 */
		try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
			SPARQLParser sparqlParser = new SPARQLParser();
			Iterator<String> lineIterator = lineStream.iterator();
			StringBuilder queryString = new StringBuilder();

			while (lineIterator.hasNext())
			/*
			 * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par un '}'
			 * On considère alors que c'est la fin d'une requête
			 */
			{
				String line = lineIterator.next();
				queryString.append(line);
				if (line.trim().endsWith("}")) {
					ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);
					processAQuery(query); // Traitement de la requête, à adapter/réécrire pour votre programme
					queryString.setLength(0); // Reset le buffer de la requête en chaine vide
				}
			}
		}
	}

	/**
	 * Traite chaque triple lu dans {@link #dataFile} avec {@link MainRDFHandler}.
	 */
	private static void parseData() throws FileNotFoundException, IOException {

		try (Reader dataReader = new FileReader(dataFile)) {
			// On va parser des données au format ntriples
			RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

			// On utilise notre implémentation de handler
			rdfParser.setRDFHandler(new MainRDFHandler());

			// Parsing et traitement de chaque triple par le handler
			rdfParser.parse(dataReader, baseURI);
		}
	}

	private static void myData() throws FileNotFoundException, IOException {

		try (Reader dataReader = new FileReader(dataFile)) {
			// On va parser des données au format ntriples
			RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

			// On utilise notre implémentation de handler
			rdfParser.setRDFHandler(new MyRDFHandler());
			myDictionary = MyRDFHandler.getMyDictionary();

			// Parsing et traitement de chaque triple par le handler
			rdfParser.parse(dataReader, baseURI);
		}









	}

}
