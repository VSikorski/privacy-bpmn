import nl.rug.ds.bpm.pnml.ptnet.jaxb.ptnet.Net;
import nl.rug.ds.bpm.pnml.ptnet.jaxb.ptnet.Pnml;
import nl.rug.ds.bpm.petrinet.ptnet.PlaceTransitionNet;
import nl.rug.ds.bpm.petrinet.ptnet.element.Transition;
import nl.rug.ds.bpm.petrinet.ptnet.element.Place;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.io.OutputStream;
import java.util.Set;
import java.util.HashSet;


/**
 * Class for marshalling an augmented petri net to pnml file
 */
public class AugmentedPTNetMarshaller {
    public AugmentedPTNetMarshaller(PlaceTransitionNet net, File file) {
        try {
            System.out.println("\n==== AugmentedPTNetMarshaller DEBUG ====");
            System.out.println("Places in net: " + net.getPlaces().size());
            for (Place p : net.getPlaces()) {
                System.out.println("  - Place: " + p.getId() + " (tokens: " + p.getTokens() + ")");
            }
            System.out.println("Transitions in net: " + net.getTransitions().size());
            for (Transition t : net.getTransitions()) {
                System.out.println("  - Transition: " + t.getId());
            }
            System.out.println("Arcs in net: " + net.getArcs().size());

            Net jaxbNet = (Net) net.getXmlElement();
            Pnml pnml = new Pnml();
            Set<Net> nets = new HashSet<>();
            nets.add(jaxbNet);
            pnml.setNets(nets);

            JAXBContext context = JAXBContext.newInstance(Pnml.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(pnml, file);

            System.out.println("PNML successfully written to " + file.getName());
            System.out.println("===============================\n");

        } catch (JAXBException e) {
            System.err.println("ERROR writing PNML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
