<privacySpecification>

    <!-- Role Mapping -->
    <roleMappings>
        <role id="Alice">
            <transition>t_create_invoice</transition>
            <transition>t_send_invoice</transition>
        </role>
        <role id="Bob">
            <transition>t_sign_invoice</transition>
            <transition>t_stamp_invoice</transition>
        </role>
    </roleMappings>

    <!-- SoD Module -->
    <separationOfDuty>
        <duties>
            <duty id="duty_create_invoice">
                <createdBy>
                    <transition>t_create_invoice</transition>
                </createdBy>
            </duty>
            <duty id="duty_sign_invoice">
                <createdBy>
                    <transition>t_sign_invoice</transition>
                </createdBy>
            </duty>
            <duty id="duty_stamp_invoice">
                <createdBy>
                    <transition>t_stamp_invoice</transition>
                </createdBy>
            </duty>
            <duty id="duty_send_invoice">
                <createdBy>
                    <transition>t_send_invoice</transition>
                </createdBy>
            </duty>
        </duties>

        <constraints>
            <constraint id="sod1">
                <conflictingDuties>
                    <duty>duty_create_invoice</duty>
                    <duty>duty_send_invoice</duty>
                </conflictingDuties>
            </constraint>

            <constraint id="sod2">
                <conflictingDuties>
                    <duty>duty_sign_invoice</duty>
                    <duty>duty_stamp_invoice</duty>
                </conflictingDuties>
            </constraint>
        </constraints>

    </separationOfDuty>
</privacySpecification>
