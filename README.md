GUIDES: 
    -   http://saml.xml.org/saml-specifications
    
    -   http://docs.oasis-open.org/security/saml/v2.0/saml-core-2.0-os.pdf
    
    -   https://www.oasis-open.org/committees/download.php/35391/sstc-saml-metadata-errata-2.0-wd-04-diff.pdf
    
    -   http://docs.oasis-open.org/security/saml/v2.0/saml-authn-context-2.0-os.pdf
    
    -   https://www.oasis-open.org/committees/download.php/35711/sstc-saml-core-errata-2.0-wd-06-diff.pdf
    
    - https://support.secureauth.com/hc/en-us/articles/229711267-Encrypt-SAML-Assertion
    

1) An unsigned SAML Response
    - with an unsigned Assertion
    - with a signed Assertion
    
    - with an encrypted Assertion
    - with an encrypted signed Assertion

2) An signed SAML Response
    - with an unsigned Assertion
    - with a signed Assertion
    
    - with an encrypted Assertion
    - with an encrypted signed Assertion


<samlp:Response> samlp:ResponseType extends samlp:StatusResponseType

    Inherited from samlp:StatusResponseType

        @ID (required)                      USADO
        @InResponseTo (optional)            USADO
        @Version (required)                 USADO
        @IssueInstant (required)            USADO
        @Destination (optional)             USADO
        @Consent (optional)                 NÃO USADO

        Inherited from samlp:RequestAbstractType

            <saml:Issuer> (zero or more) saml:NameIDType extends string         USADO [ Em todos os casos | valor: Endereço do metadata do IDP]
                @NameQualifier (optional)                                       NÃO USADO
                @SPNameQualifier (optional)                                     NÃO USADO
                @Format (optional)                                              NÃO USADO
                @SPProvidedID (optional)                                        NÃO USADO
                    Content: string


            <ds:Signature> (zero or more)                                       USADO [ Apenas onde é 'An Signed SAML Response' 2)] 
            <samlp:Extensions> (zero or more) samlp:ExtensionsType,             NÃO USADO
            ##other namespace
            <samlp:Status> (one)                                                USADO [ Em todos os casos]
                <samlp:StatusCode> (one)                                        USADO [ Em todos os casos]
                    @Value (required)                                           USADO [ Em todos os casos]
                    <samlp:StatusCode> (zero or more) nested…
                <samlp:StatusMessage> (zero or more)                            NÃO USADO
                    Content: string
                <samlp:StatusDetail> (zero or more)                             NÃO USADO
                    Content: elements from ##other namespace            
        
        Added as samlp:ResponseType

            <saml:Assertion> (zero or more) saml:AssertionType (alternatively   USADO SEMPRE [ QUANDO É ENCRYPTION ASSERTION É USADO ENCRYPTION ASSERTION SE NÃO APENAS O ASSERTION]
            <saml:EncryptedAssertion>)                                          USADO SEMPRE [ QUANDO É ENCRYPTION ASSERTION É USADO ENCRYPTION ASSERTION SE NÃO APENAS O ASSERTION]
                @ID (required)                                                  USADO SOMENTE QUANDO É ASSERTION
                @Version (required)                                             USADO SOMENTE QUANDO É ASSERTION
                @IssueInstant (required)                                        USADO SOMENTE QUANDO É ASSERTION
                <saml:Issuer> (zero or more) saml:NameIDType extends            USADO SOMENTE QUANDO É ASSERTION
                string  
                    @NameQualifier (optional)                                   NÃO USADO
                    @SPNameQualifier (optional)                                 NÃO USADO
                    @Format (optional)                                          NÃO USADO
                    @SPProvidedID (optional)                                    NÃO USADO
                        Content: string
                <ds:Signature> (zero or more)                                   USADO SOMENTE QUANDO É with a signed Assertion
                <saml:Subject> (zero or more) saml:SubjectType                  USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    <saml:NameID> saml:NameIDType (could also be either         USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    BaseID or EncryptedID) (optional)
                        @NameQualifier (optional)                               NÃO USADO
                        @SPNameQualifier (optional)                             USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        @Format (optional)                                      USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        @SPProvidedID (optional)                                NÃO USADO
                        Content: string                                         USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    <saml:SubjectConfirmation> (zero or more)                   USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    saml:SubjectConfirmationType extends
                        @Method (required)                                      USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        <saml:NameID> saml:NameIDType (could also be            NÃO USADO
                        either BaseID or EncryptedID) (optional)                NÃO USADO
                            @NameQualifier (optional)                           NÃO USADO
                            @SPNameQualifier (optional)                         NÃO USADO
                            @Format (optional)                                  NÃO USADO
                            @SPProvidedID (optional)                            NÃO USADO
                            Content: string                                     NÃO USADO
                        <saml:SubjectConfirmationData>                          USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        saml:SubjectConfirmationDataType
                            @NotBefore (optional)                               NÃO USADO
                            @NotOnOrAfter (optional)                            USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                            @Recipient (optional)                               USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                            @InResponseTo (optional)                            USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                            @Address (optional)                                 NÃO USADO
                            Content: any element or attribute in ##other namespace NÃO USADO
                <saml:Conditions> (zero or more) saml:ConditionsType            USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    @NotBefore (optional)                                       USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    @NotOnOrAfter (optional)                                    USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    <saml:AudienceRestriction> (zero or more)                   USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    saml:AudienceRestrictionType
                        <saml:Audience> saml:Audience                           USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                            Content: URI
                    <saml:OneTimeUse> (zero or more)                            NÃO USADO
                    saml:OneTimeUseType extends 
                    saml:ConditionAbstractType
                    <saml:ProxyRestriction> (zero or more)                      NÃO USADO
                    saml:ProxyRestrictionType extends 
                    saml:ConditionAbstractType
                        @Count (optional)                                       NÃO USADO
                        <saml:Audience> saml:Audience                           NÃO USADO
                            Content: URI
                    Any element extending saml:ConditionAbstractType
                <saml:Advice> (zero or more)                                    NÃO USADO
                <saml:AuthnStatement> (zero or more) saml:AuthnStatementType extends    USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                saml:StatementAbstractType
                    @AuthnInstant (required)                                            USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    @SessionIndex (optional)                                            USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    @SessionNotOnOrAfter (optional)                                     USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    <saml:SubjectLocality> (zero or more) saml:SubjectLocalityType      NÃO USADO
                        @Address (optional)
                        @DNSName (optional)
                    <saml:AuthnContext> (zero or more) saml:AuthnContextType            USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        <saml:AuthnContextClassRef> (optional)                          USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        <saml:AuthnContextDecl> (zero or more)                          NÃO USADO
                        <saml:AuthnContextDeclRef> (zero or more)                       NÃO USADO
                        <saml:AuthenticatingAuthority> (zero or more)                   NÃO USADO
                        AuthnContext MUST contain at least one of AuthnContextClassRef, 
                        AuthnContextDecl and AuthnContextDeclRef.
                <saml:AttributeStatement> (zero or more) saml:AttributeStatementType    USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    <saml:Attribute> (zero or more) saml:AttributeType alternatively    USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                    (saml:EncryptedAttribute)
                        @Name (required)                                                USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        @NameFormat (optional)                                          USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
                        @FriendlyName (optional)                                        NÃO USADO
                        <saml:AttributeValue> (zero or more)                            USADO SOMENTE QUANDO NÃO É ENCRYPTED ASSERTION
