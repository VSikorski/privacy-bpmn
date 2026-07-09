<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<bpmSpecification>
    <elementGroups>
        <group id="group_T_DOK_VIOLATION_dok1_Alice">
            <elements>
                <element>T_DOK_VIOLATION_dok1_Alice</element>
            </elements>
        </group>
    </elementGroups>
    <specificationSets>
        <specificationSet>
            <conditions/>
            <specifications>
                <specification id="check_T_DOK_VIOLATION_dok1_Alice" type="DoKReachability">
                    <inputElements>
                        <inputElement target="p">group_T_DOK_VIOLATION_dok1_Alice</inputElement>
                    </inputElements>
                </specification>
            </specifications>
        </specificationSet>
    </specificationSets>
    <specificationTypes>
        <specificationType id="DoKReachability">
            <formulas>
                <formula language="CTLSPEC">AG !p</formula>
            </formulas>
            <inputs>
                <input type="and">p</input>
            </inputs>
            <message fail="DoK Violation is reachable" hold="DoK Violation is unreachable"/>
        </specificationType>
    </specificationTypes>
</bpmSpecification>
