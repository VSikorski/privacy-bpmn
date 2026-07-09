import nl.rug.ds.bpm.petrinet.ptnet.PlaceTransitionNet;
import nl.rug.ds.bpm.util.exception.MalformedNetException;
import nl.rug.ds.bpm.specification.jaxb.BPMSpecification;


public interface IPrivacyModule {
    void apply(PlaceTransitionNet net, BPMSpecification spec) throws MalformedNetException;
    void augmentPNML(PlaceTransitionNet net) throws MalformedNetException;
    void augmentSpecification(BPMSpecification spec);
}
