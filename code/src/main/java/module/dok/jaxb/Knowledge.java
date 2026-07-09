import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Knowledge {
    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlElementWrapper(name = "createdBy")
    @XmlElement(name = "transition")
    private List<String> createdBy;

    @XmlElementWrapper(name = "requiredBy")
    @XmlElement(name = "transition")
    private List<String> requiredBy;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public List<String> getCreatedBy() { return createdBy; }
    public void setCreatedBy(List<String> createdBy) { this.createdBy = createdBy; }

    public List<String> getRequiredBy() { return requiredBy; }
    public void setRequiredBy(List<String> requiredBy) { this.requiredBy = requiredBy; }
}
