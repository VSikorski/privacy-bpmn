<privacySpecification>

    <!-- Role Mapping -->
    <roleMappings>
        <role id="Alice">
            <transition>t_request_otp</transition>
            <transition>t_verify_otp</transition>
            <transition>t_authenticate</transition>
        </role>
    </roleMappings>

    <!-- DoK Module -->
     <divisionOfKnowledge>
         <knowledgeSet>
             <knowledge id="otp_code">
                 <createdBy>
                     <transition>t_request_otp</transition>
                 </createdBy>
                 <requiredBy>
                     <transition>t_verify_otp</transition>
                 </requiredBy>
             </knowledge>
             <knowledge id="otp_confirmation">
                 <createdBy>
                     <transition>t_verify_otp</transition>
                 </createdBy>
             </knowledge>
             <knowledge id="authentication_token">
                 <createdBy>
                     <transition>t_authenticate</transition>
                 </createdBy>
             </knowledge>
         </knowledgeSet>

         <constraints>
             <constraint id="dok1">
                 <role>Alice</role>
                 <criticalSet>
                     <knowledge>otp_confirmation</knowledge>
                     <knowledge>authentication_token</knowledge>
                 </criticalSet>
             </constraint>
         </constraints>

        </divisionOfKnowledge>
</privacySpecification>
