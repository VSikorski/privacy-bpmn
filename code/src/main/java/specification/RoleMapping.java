import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class RoleMapping {
    @XmlAttribute(name = "id", required = true)
    private String id;
    @XmlElement(name = "transition")
    private List<String> transition;

    public String getId() {return id;}
    public List<String> getTransition() {return transition;}
}
