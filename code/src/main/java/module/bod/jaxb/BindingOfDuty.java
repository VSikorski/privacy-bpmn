import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "bindingOfDuty")
@XmlAccessorType(XmlAccessType.FIELD)
public class BindingOfDuty {

    @XmlElementWrapper(name = "duties")
    @XmlElement(name = "duty")
    private List<BoDDuty> duties = new ArrayList<>();

    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    private List<BoDConstraint> constraints = new ArrayList<>();

    public List<BoDDuty> getDuties() {
        return duties;
    }

    public List<BoDConstraint> getConstraints() {
        return constraints;
    }
}
