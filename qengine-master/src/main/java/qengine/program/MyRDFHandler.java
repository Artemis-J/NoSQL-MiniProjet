package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import java.util.HashMap;
import java.util.Map;


public class MyRDFHandler extends AbstractRDFHandler {

    /**
     * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un traitement pour chaque élément lu par le parseur.
     *
     * <p>
     * Ce qui servira surtout dans le programme est la méthode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
     * </p>
     * <p>
     * À adapter/réécrire selon vos traitements.
     * </p>
     */

    static Map<String,Integer> myDictionary = new HashMap<>();
    static Integer cpt = 0;
    Index spo = new Index();
    Index pos = new Index();
    Index osp = new Index();
    Index sop = new Index();
    Index pso = new Index();
    Index ops = new Index();

    public static Map<String, Integer> getMyDictionary() {
        return myDictionary;
    }

    public Index getSpo() {
        return spo;
    }

    public Index getPos() {
        return pos;
    }

    public Index getOsp() {
        return osp;
    }

    public Index getSop() {
        return sop;
    }

    public Index getPso() {
        return pso;
    }

    public Index getOps() {
        return ops;
    }

    @Override
    public void handleStatement(Statement st) {
        String subject = st.getSubject().toString();
        String predicate = st.getPredicate().toString();
        String object = st.getObject().toString();
        myDictionary.putIfAbsent(subject,cpt);
        cpt++;
        myDictionary.putIfAbsent(predicate,cpt);
        cpt++;
        myDictionary.putIfAbsent(object,cpt);
        cpt++;

        spo.fill(Main.myDictionary.get(subject),Main.myDictionary.get(predicate),Main.myDictionary.get(object));
        Main.SPO = spo.getIndex();
        pos.fill(Main.myDictionary.get(predicate),Main.myDictionary.get(object),Main.myDictionary.get(subject));
        Main.POS = pos.getIndex();
        osp.fill(Main.myDictionary.get(object),Main.myDictionary.get(subject),Main.myDictionary.get(predicate));
        Main.OSP = osp.getIndex();

        sop.fill(Main.myDictionary.get(subject),Main.myDictionary.get(object),Main.myDictionary.get(predicate));
        Main.SOP = sop.getIndex();
        ops.fill(Main.myDictionary.get(object),Main.myDictionary.get(predicate),Main.myDictionary.get(subject));
        Main.OPS = ops.getIndex();
        pso.fill(Main.myDictionary.get(predicate),Main.myDictionary.get(subject),Main.myDictionary.get(object));
        Main.PSO = pso.getIndex();
    };



}