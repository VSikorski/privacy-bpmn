import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class BoDConstraint {
    @XmlAttribute(name = "id")
    private String id;

    @XmlElementWrapper(name = "boundDuties")
    @XmlElement(name = "duty")
    private List<String> boundDuties = new ArrayList<>();

    public String getId() {
        return id;
    }

    public List<String> getBoundDuties() {
        return boundDuties;
    }
}
