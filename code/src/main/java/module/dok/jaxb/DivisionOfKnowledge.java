import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DivisionOfKnowledge {
    @XmlElement(name = "knowledgeSet")
    private KnowledgeSet knowledgeSet;

    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    private List<DoKConstraint> constraints;

    public KnowledgeSet getKnowledgeSet() { return knowledgeSet; }
    public void setKnowledgeSet(KnowledgeSet knowledgeSet) { this.knowledgeSet = knowledgeSet; }

    public List<DoKConstraint> getConstraints() { return constraints; }
    public void setConstraints(List<DoKConstraint> constraints) { this.constraints = constraints; }
}
