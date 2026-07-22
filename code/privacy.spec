<privacySpecification>

    <!-- Role Mapping -->
    <roleMappings>
        <role id="Alice">
            <transition>t_set_login</transition>
            <transition>t_set_password</transition>
            <transition>t_confirm_email</transition>
        </role>
    </roleMappings>

    <!-- BoD Module -->
    <bindingOfDuty>
        <duties>
            <duty id="duty_set_login">
                <createdBy>
                    <transition>t_set_login</transition>
                </createdBy>
            </duty>
            <duty id="duty_set_password">
                <createdBy>
                    <transition>t_set_password</transition>
                </createdBy>
            </duty>
            <duty id="duty_confirm_email">
                <createdBy>
                    <transition>t_confirm_email</transition>
                </createdBy>
            </duty>
        </duties>

        <constraints>
            <constraint id="bod1">
                <boundDuties>
                    <duty>duty_set_login</duty>
                    <duty>duty_set_password</duty>
                </boundDuties>
            </constraint>

            <constraint id="bod2">
                <boundDuties>
                    <duty>duty_set_login</duty>
                    <duty>duty_set_password</duty>
                    <duty>duty_confirm_email</duty>
                </boundDuties>
            </constraint>
        </constraints>

    </bindingOfDuty>
</privacySpecification>
