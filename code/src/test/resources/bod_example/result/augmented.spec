<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<bpmSpecification>
    <elementGroups>
        <group id="group_T_BOD_VIOLATION_bod1_Alice">
            <elements>
                <element>T_BOD_VIOLATION_bod1_Alice</element>
            </elements>
        </group>
        <group id="group_T_BOD_VIOLATION_bod2_Alice">
            <elements>
                <element>T_BOD_VIOLATION_bod2_Alice</element>
            </elements>
        </group>
    </elementGroups>
    <specificationSets>
        <specificationSet>
            <conditions/>
            <specifications>
                <specification id="check_T_BOD_VIOLATION_bod1_Alice" type="BoDReachability">
                    <inputElements>
                        <inputElement target="p">group_T_BOD_VIOLATION_bod1_Alice</inputElement>
                    </inputElements>
                </specification>
                <specification id="check_T_BOD_VIOLATION_bod2_Alice" type="BoDReachability">
                    <inputElements>
                        <inputElement target="p">group_T_BOD_VIOLATION_bod2_Alice</inputElement>
                    </inputElements>
                </specification>
            </specifications>
        </specificationSet>
    </specificationSets>
    <specificationTypes>
        <specificationType id="BoDReachability">
            <formulas>
                <formula language="CTLSPEC">EF p</formula>
            </formulas>
            <inputs>
                <input type="and">p</input>
            </inputs>
            <message fail="BoD VDetector Transition is not unreachable (duties are not bound)" hold="BoD Detector Transition is reachable (duties are bound)"/>
        </specificationType>
    </specificationTypes>
</bpmSpecification>
