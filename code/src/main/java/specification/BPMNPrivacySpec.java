import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "privacySpecification")
@XmlAccessorType(XmlAccessType.FIELD)
public class BPMNPrivacySpec {
    // Role to Transition Mapping
    @XmlElementWrapper(name = "roleMappings")
    @XmlElement(name = "role")
    private List<RoleMapping> roleMappings;
    public List<RoleMapping> getRoleMappings() {return roleMappings;}

    // Separation of Duty
    @XmlElement(name = "separationOfDuty")
    private SeparationOfDuty separationOfDuty;
    public SeparationOfDuty getSeparationOfDuty() { return separationOfDuty; }

    // Binding of Duty
    @XmlElement(name = "bindingOfDuty")
    private BindingOfDuty bindingOfDuty;
    public BindingOfDuty getBindingOfDuty() { return bindingOfDuty; }

    // Division of Knowledge
    @XmlElement(name = "divisionOfKnowledge")
    private DivisionOfKnowledge divisionOfKnowledge;
    public DivisionOfKnowledge getDivisionOfKnowledge() { return divisionOfKnowledge; }
}
