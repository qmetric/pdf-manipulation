<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> May 9, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> Mike Kelly</xd:p>
            <xd:p>Welcome Letter Pre-Processing</xd:p>
            <xd:p>MK15012012 - Reworked to cater for payment details</xd:p>
            <xd:p>MK23052011 - Amended address line mapping to check for empty nodes for story
                13689721</xd:p>
        </xd:desc>
    </xd:doc>

    <xsl:strip-space elements="*"/>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <!-- Build document sections -->
    <xsl:template match="/*">
        <welcome-letter>
            <xsl:call-template name="DocumentDetails"/>
            <xsl:call-template name="DocumentTitle"/>
            <xsl:call-template name="Recipient"/>
            <xsl:call-template name="Reference"/>
            <xsl:call-template name="Salutation"/>
            <xsl:call-template name="LetterBody"/>
            <xsl:call-template name="Valediction"/>
            <xsl:call-template name="Footer"/>
        </welcome-letter>
    </xsl:template>

    <xsl:template name="DocumentDetails">
        <document-details>
            <product>
                <xsl:value-of select="policyDetails/product"/>
            </product>
            <external-images>
                <xsl:value-of select="resources/baseAssetPath"/>
            </external-images>
        </document-details>
    </xsl:template>

    <xsl:template name="DocumentTitle">
        <title>Hello and Welcome</title>
    </xsl:template>

    <xsl:template name="Recipient">
        <recipient>
            <name>
                <xsl:apply-templates select="policyHolderDetails/title"/>
                <xsl:apply-templates select="policyHolderDetails/firstName"/>
                <xsl:apply-templates select="policyHolderDetails/lastName"/>
            </name>
            <address>
                <xsl:apply-templates select="correspondenceAddressDetails/addressLine1[. != '']"/>
                <xsl:apply-templates select="correspondenceAddressDetails/addressLine2[. != '']"/>
                <xsl:apply-templates select="correspondenceAddressDetails/addressLine3[. != '']"/>
                <xsl:apply-templates select="correspondenceAddressDetails/addressLine4[. != '']"/>
                <xsl:apply-templates select="correspondenceAddressDetails/addressTown[. != '']"/>
                <xsl:apply-templates select="correspondenceAddressDetails/postcode[. != '']"/>
            </address>
        </recipient>
    </xsl:template>

    <xsl:template name="Reference">
        <reference>
            <policy-online>
                <line>To access your online account:</line>
                <line>
                    <xsl:text>visit </xsl:text>
                    <xsl:apply-templates select="contactDetails/siteUrl"/>
                    <xsl:text> and log in</xsl:text>
                </line>
            </policy-online>
            <policy-change>
                <line>Need to change your policy?</line>
                <line>
                    <xsl:text>Call: </xsl:text>
                    <xsl:apply-templates select="contactDetails/helpline"/>
                </line>
            </policy-change>
            <email-address>
                <line>Email us on:</line>
                <line>
                    <xsl:apply-templates select="contactDetails/companyEmail"/>
                </line>
            </email-address>
            <account-number>
                <label>Your customer reference:</label>
                <number>
                    <xsl:apply-templates select="policyHolderDetails/customerId"/>
                </number>
            </account-number>
            <letter-date>
                <label>Date:</label>
                <date>
                    <xsl:value-of select="format-dateTime(current-dateTime(), '[D] [MNn] [Y]')"/>
                </date>
            </letter-date>
        </reference>
    </xsl:template>

    <xsl:template name="Salutation">
        <salutation>
            <greeting>Dear</greeting>
            <name>
                <xsl:apply-templates select="policyHolderDetails/title"/>
                <xsl:apply-templates select="policyHolderDetails/lastName"/>
                <xsl:text>,</xsl:text>
            </name>
        </salutation>
    </xsl:template>

    <xsl:template name="LetterBody">
        <letter-body>
            <paragraph>
                <para>
                    <xsl:text>We're delighted you've chosen to purchase your </xsl:text>
                    <!-- only include if product single product -->
                    <xsl:if test="not(count(policyDetails/boltOns) gt 0)">
                        <xsl:apply-templates select="policyDetails/product"/>
                    </xsl:if>
                    <xsl:text> insurance with Policy Expert. Please find enclosed your policy schedule.</xsl:text>
                </para>
                <para>All of your policy documents are available in your online account for you to view, download and print.</para>
            </paragraph>

            <paragraph>
                <para>
                    <emphasis>It's important to check through all of these documents carefully to
                        understand exactly what's covered and any restrictions that may
                        apply.</emphasis>
                </para>
            </paragraph>

            <paragraph>
                <table>
                    <title>What you've bought</title>
                    <table-header>
                        <row>
                            <entry>Product name</entry>
                            <entry>Policy number</entry>
                            <entry>Insurer</entry>
                            <entry>Policy start date</entry>
                            <entry>If you need to claim</entry>
                        </row>
                    </table-header>
                    <table-body>
                        <xsl:apply-templates select="policyDetails"/>
                        <xsl:apply-templates select="policyDetails/boltOns"/>
                    </table-body>
                </table>
            </paragraph>

            <xsl:apply-templates select="paymentDetails"/>

            <paragraph>
                <para>Please get in touch if you need to make changes to your policy or update your
                    information.</para>
                <para>We look forward to looking after your insurance needs for many years to come.</para>
            </paragraph>
        </letter-body>
    </xsl:template>

    <xsl:template name="Valediction">
        <valediction>
            <sign-off>Kind Regards</sign-off>
            <xsl:apply-templates select="signOffDetails/name"/>
            <xsl:apply-templates select="signOffDetails/title"/>
        </valediction>
    </xsl:template>

    <xsl:template name="Footer">
        <footer>
            <para>
                <xsl:apply-templates select="contactDetails/companyTradingName"/>
            </para>
            <para>
                <xsl:apply-templates select="contactDetails/companyNumber"/>
            </para>
            <para>
                <xsl:apply-templates select="contactDetails/companyFsaInfo"/>
            </para>
            <para>
                <xsl:apply-templates select="contactDetails/companyFsaNumber"/>
                <xsl:text> Visit www.fsa.gov.uk/Pages/Register for more information.</xsl:text>
            </para>
        </footer>
    </xsl:template>

    <xsl:template match="paymentDetails">
        <payment-info>
            <title>How you've paid</title>
            <xsl:apply-templates select="paymentType"/>
        </payment-info>
    </xsl:template>

    <xsl:template match="paymentDetails/paymentType[. = 'ANNUAL']">
        <payment-method>Annual</payment-method>
        <card-details>
            <title>Paid in full by card</title>
            <full>
                <label>Amount paid:</label>
                <amount>
                    <xsl:text>£</xsl:text>
                    <xsl:value-of
                            select="format-number(sum(//policyDetails/grossPremium), '#,###.00')"
                            />
                </amount>
            </full>
            <card-number>
                <label>Paid by card number:</label>
                <number>
                    <xsl:apply-templates select="../cardNumberMask"/>
                </number>
            </card-number>
        </card-details>
    </xsl:template>

    <xsl:template match="paymentDetails/paymentType[. = 'DIRECT_DEBIT']">
        <payment-method>Direct Debit</payment-method>
        <card-details>
            <title>Paid by card</title>
            <deposit>
                <label>Amount paid:</label>
                <amount>
                    <xsl:apply-templates select="../directDebitPaymentInformation/deposit"/>
                </amount>
            </deposit>
            <card-number>
                <label>Paid by card number:</label>
                <number>
                    <xsl:apply-templates select="../cardNumberMask"/>
                </number>
            </card-number>
        </card-details>
        <xsl:apply-templates select="../directDebitPaymentInformation"/>
    </xsl:template>

    <xsl:template match="paymentDetails/directDebitPaymentInformation">
        <direct-debit>
            <title>Balance still to pay by direct debit</title>
            <installments>
                <label>10 monthly payments:</label>
                <amount><xsl:apply-templates select="installmentAmount"/></amount>
            </installments>
            <finance-company>
                <label>Finance provided by:</label>
                <name><xsl:apply-templates select="../../contactDetails/financeCompanyName"/></name>
            </finance-company>
            <questions>
                <label>Questions on finance:</label>
                <tel>Please call <xsl:apply-templates select="../../contactDetails/financeCompanyTelephone"/></tel>
            </questions>
        </direct-debit>
    </xsl:template>

    <!-- remove created fromoutput -->
    <xsl:template match="paymentDetails/created"/>

    <xsl:template match="policyDetails">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="boltOns">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="policyDetails | boltOn">
        <row>
            <entry>
                <xsl:apply-templates select="product"/>
                <xsl:text> Insurance</xsl:text>
                <xsl:if test="productClass != ''">
                    <xsl:text> - </xsl:text>
                    <xsl:apply-templates select="productClass"/>
                </xsl:if>
            </entry>
            <entry>
                <xsl:apply-templates select="policyNumber"/>
            </entry>
            <entry>
                <xsl:apply-templates select="insurerName"/>
            </entry>
            <entry>
                <xsl:apply-templates select="startDate"/>
                <xsl:text> at </xsl:text>
                <xsl:apply-templates select="startTime"/>
            </entry>
            <entry>
                <xsl:text>Call </xsl:text>
                <xsl:apply-templates select="/welcomeLetterDocument/claimsTelephoneNumber"/>
            </entry>
        </row>
    </xsl:template>

    <xsl:template match="title">
        <xsl:apply-templates/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="firstName">
        <xsl:apply-templates/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="lastName">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="addressLine1|addressLine2|addressLine3|addressLine4|addressTown|postcode">
        <line>
            <xsl:apply-templates/>
        </line>
    </xsl:template>

    <xsl:template match="helpline">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="claimsTelephoneNumber">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="customerId">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="product">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="productClass">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="policyNumber">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="myAccountUrl">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="insurerName">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="startDate">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="startTime">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="expiryTime">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="expiryDate">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="communityInfo">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="companyTradingName">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="companyNumber">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="companyEmail">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="companyFsaInfo">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="companyFsaNumber">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="installmentAmount">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="deposit">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="financeCompanyName">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="financeCompanyTelephone">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="cardNumberMask">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="signOffDetails/name">
        <name>
            <xsl:attribute name="signature-img">
                <xsl:value-of select="../signatureImageName"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </name>
    </xsl:template>

    <xsl:template match="signOffDetails/title">
        <job-title>
            <xsl:apply-templates/>
        </job-title>
    </xsl:template>
</xsl:stylesheet>
