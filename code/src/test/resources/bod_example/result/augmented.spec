<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<bpmSpecification>
    <elementGroups>
        <group id="group_duty_set_login_Alice_bod1">
            <elements>
                <element>t_set_login</element>
            </elements>
        </group>
        <group id="group_detector_Alice_bod1">
            <elements>
                <element>T_BOD_VIOLATION_bod1_Alice</element>
            </elements>
        </group>
        <group id="group_duty_set_password_Alice_bod1">
            <elements>
                <element>t_set_password</element>
            </elements>
        </group>
        <group id="group_duty_set_login_Alice_bod2">
            <elements>
                <element>t_set_login</element>
            </elements>
        </group>
        <group id="group_detector_Alice_bod2">
            <elements>
                <element>T_BOD_VIOLATION_bod2_Alice</element>
            </elements>
        </group>
        <group id="group_duty_set_password_Alice_bod2">
            <elements>
                <element>t_set_password</element>
            </elements>
        </group>
        <group id="group_duty_confirm_email_Alice_bod2">
            <elements>
                <element>t_confirm_email</element>
            </elements>
        </group>
    </elementGroups>
    <specificationSets>
        <specificationSet>
            <conditions/>
            <specifications>
                <specification id="check_bod_t_set_login_Alice_bod1" type="AlwaysResponse">
                    <inputElements>
                        <inputElement target="p">group_duty_set_login_Alice_bod1</inputElement>
                        <inputElement target="q">group_detector_Alice_bod1</inputElement>
                    </inputElements>
                </specification>
                <specification id="check_bod_t_set_password_Alice_bod1" type="AlwaysResponse">
                    <inputElements>
                        <inputElement target="p">group_duty_set_password_Alice_bod1</inputElement>
                        <inputElement target="q">group_detector_Alice_bod1</inputElement>
                    </inputElements>
                </specification>
                <specification id="check_bod_t_set_login_Alice_bod2" type="AlwaysResponse">
                    <inputElements>
                        <inputElement target="p">group_duty_set_login_Alice_bod2</inputElement>
                        <inputElement target="q">group_detector_Alice_bod2</inputElement>
                    </inputElements>
                </specification>
                <specification id="check_bod_t_set_password_Alice_bod2" type="AlwaysResponse">
                    <inputElements>
                        <inputElement target="p">group_duty_set_password_Alice_bod2</inputElement>
                        <inputElement target="q">group_detector_Alice_bod2</inputElement>
                    </inputElements>
                </specification>
                <specification id="check_bod_t_confirm_email_Alice_bod2" type="AlwaysResponse">
                    <inputElements>
                        <inputElement target="p">group_duty_confirm_email_Alice_bod2</inputElement>
                        <inputElement target="q">group_detector_Alice_bod2</inputElement>
                    </inputElements>
                </specification>
            </specifications>
        </specificationSet>
    </specificationSets>
    <specificationTypes/>
</bpmSpecification>
