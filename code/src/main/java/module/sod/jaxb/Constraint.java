import jakarta.xml.bind.annotation.*;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class Constraint {
    // specification
    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlElementWrapper(name = "conflictingDuties")
    @XmlElement(name = "duty")
    private List<String> conflictingDuties;

    // getters
    public String getId() {return id;}
    public List<String> getConflictingDuties() {return conflictingDuties;}
}
