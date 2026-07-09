import nl.rug.ds.bpm.petrinet.ptnet.PlaceTransitionNet;
import nl.rug.ds.bpm.specification.jaxb.BPMSpecification;
import nl.rug.ds.bpm.util.exception.MalformedNetException;
import nl.rug.ds.bpm.specification.jaxb.BPMSpecification;


public abstract class AbstractPrivacyModule implements IPrivacyModule {
    @Override
    public void apply(PlaceTransitionNet net, BPMSpecification spec) throws MalformedNetException {
        augmentPNML(net);
        augmentSpecification(spec);
    }
}
