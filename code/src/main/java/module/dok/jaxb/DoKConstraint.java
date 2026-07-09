import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DoKConstraint {
    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlElement(name = "role")
    private String role;

    @XmlElementWrapper(name = "criticalSet")
    @XmlElement(name = "knowledge")
    private List<String> criticalSet;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<String> getCriticalSet() { return criticalSet; }
    public void setCriticalSet(List<String> criticalSet) { this.criticalSet = criticalSet; }
}
