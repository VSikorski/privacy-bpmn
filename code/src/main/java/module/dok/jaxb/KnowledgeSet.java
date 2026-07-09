import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class KnowledgeSet {
    @XmlElement(name = "knowledge")
    private List<Knowledge> knowledge;

    public List<Knowledge> getKnowledge() { return knowledge; }
    public void setKnowledge(List<Knowledge> knowledge) { this.knowledge = knowledge; }
}
