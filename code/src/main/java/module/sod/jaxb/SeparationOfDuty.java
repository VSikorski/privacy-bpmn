import jakarta.xml.bind.annotation.*;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class SeparationOfDuty {
    // specification
    @XmlElementWrapper(name = "duties")
    @XmlElement(name = "duty")
    private List<Duty> duties;

    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    private List<Constraint> constraints;

    // getters
    public List<Duty> getDuties() {return duties;}
    public List<Constraint> getConstraints() {return constraints;}
}