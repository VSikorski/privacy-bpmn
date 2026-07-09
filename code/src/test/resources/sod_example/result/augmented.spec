<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<bpmSpecification>
    <elementGroups>
        <group id="group_T_SOD_VIOLATION_sod1_Alice">
            <elements>
                <element>T_SOD_VIOLATION_sod1_Alice</element>
            </elements>
        </group>
        <group id="group_T_SOD_VIOLATION_sod2_Bob">
            <elements>
                <element>T_SOD_VIOLATION_sod2_Bob</element>
            </elements>
        </group>
    </elementGroups>
    <specificationSets>
        <specificationSet>
            <conditions/>
            <specifications>
                <specification id="check_T_SOD_VIOLATION_sod1_Alice" type="SoDReachability">
                    <inputElements>
                        <inputElement target="p">group_T_SOD_VIOLATION_sod1_Alice</inputElement>
                    </inputElements>
                </specification>
                <specification id="check_T_SOD_VIOLATION_sod2_Bob" type="SoDReachability">
                    <inputElements>
                        <inputElement target="p">group_T_SOD_VIOLATION_sod2_Bob</inputElement>
                    </inputElements>
                </specification>
            </specifications>
        </specificationSet>
    </specificationSets>
    <specificationTypes>
        <specificationType id="SoDReachability">
            <formulas>
                <formula language="CTLSPEC">AG !p</formula>
            </formulas>
            <inputs>
                <input type="and">p</input>
            </inputs>
            <message fail="SOD Violation is reachable" hold="SOD Violation is unreachable"/>
        </specificationType>
    </specificationTypes>
</bpmSpecification>
