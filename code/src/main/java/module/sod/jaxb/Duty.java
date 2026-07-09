import jakarta.xml.bind.annotation.*;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class Duty {
    // specification
    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlElementWrapper(name = "createdBy")
    @XmlElement(name = "transition")
    private List<String> createdBy;

    // getters
    public String getId() {return id;}
    public List<String> getCreatedBy() {return createdBy;}
}
