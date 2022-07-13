<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 
                xmlns:xd="http://www.pnp-software.com/XSLTdoc"
                exclude-result-prefixes="xlink xlink xsi eag ape xd" >

    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
    <xd:doc type="stylesheet">
        <xd:short>Page to display the part of Access and service information.</xd:short>
        <xd:detail>
            This section displays the data related to the access to the institution and the services offered.
        </xd:detail>  
    </xd:doc>
    <xd:doc>
        <xd:short>Display the part of  <code>access and service</code>.</xd:short>
        <xd:detail>
            Display the part of access and service the information of repository to view.   
        </xd:detail>
        A template with a parameter of the type string .
        <xd:param name="id" type="string">The string "id" is the selected repository.</xd:param>
    </xd:doc>
    <xsl:template name="accessAndService">
        <xsl:param name="id"/>
        <table class="aiSection accessDisplay">
            <thead>
                <tr>
                    <th colspan="2">
                        <xsl:value-of select="ape:resource('eag2012.portal.accessandserviceinfo')"/>
                        <xsl:text> </xsl:text>
                        <a class="displayLinkSeeMore" href="javascript:seeMore('accessDisplay','{$id}');">
                            (<xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>)
                        </a>
                        <a class="displayLinkSeeLess" href="javascript:seeLess('accessDisplay','{$id}');">
                            (<xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>)
                        </a>
<!--                        <xsl:text>)</xsl:text>-->
                    </th>
                </tr>
            </thead>
            <tbody>
                <!-- opening only shown if there are values-->
                <xsl:if test="eag:timetable/eag:opening/text()">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.openinghours')"></xsl:value-of>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageOpening">
                                <xsl:with-param name="list" select="eag:timetable/eag:opening"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- closing only shown if there are values-->
                <xsl:if test="eag:timetable/eag:closing/text()">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.closingdates')"></xsl:value-of>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageClosing">
                                <xsl:with-param name="list" select="eag:timetable/eag:closing"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- directions and citations goes to template-->
                <xsl:if test="eag:directions and eag:directions/text() or (eag:directions/eag:citation and eag:directions/eag:citation/@href and eag:directions/eag:citation/@href != '') ">
                    <xsl:call-template name="multilanguageWithChilds">
                        <xsl:with-param name="title">
                            <xsl:value-of select="ape:resource('eag2012.portal.directions')"/>
                            <xsl:text>:</xsl:text>
                        </xsl:with-param>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                        <xsl:with-param name="list" select="eag:directions"/>
						
                    </xsl:call-template>
                </xsl:if>
                <!-- accessconditions only shown if there are values-->
                <xsl:if test="eag:access">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.accessconditions')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:access[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.accesspublic')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.accesspermission')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- restaccess only shown if there are values-->
                <xsl:if test="eag:access/eag:restaccess/text()">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.accessinformation')"/>
                            <xsl:text>:</xsl:text>
							 
                        </td>
                        <td>
						    
                            <xsl:call-template name="multilanguageRestaccess">
                                <xsl:with-param name="list" select="eag:access/eag:restaccess"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- accessibility only shown if there are values-->
                <xsl:if test="eag:accessibility">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.disabledaccess')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:accessibility[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.facilities')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.nofacilities')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- accessibility text only shown if there are values-->
                <xsl:if test="eag:accessibility/text()">
                    <tr title="repository">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.facilitiesfordisabledpersons')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageAccessibility">
                                <xsl:with-param name="list" select="eag:accessibility"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- termsOfUse only shown if there are values-->
                <xsl:if test="eag:access/eag:termsOfUse and eag:access/eag:termsOfUse/text()">
                    <xsl:call-template name="multilanguageTermsofuse">
                        <xsl:with-param name="title">
                            <xsl:value-of select="ape:resource('eag2012.portal.termsofuse')"/>
                            <xsl:text>:</xsl:text>
                        </xsl:with-param>
                        <xsl:with-param name="list" select="eag:access/eag:termsOfUse"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                </xsl:if>
                <!-- readersTicket shown template-->
                <xsl:if test="eag:services/eag:searchroom/eag:readersTicket">
                    <xsl:call-template name="multilanguageReadersTicket">
                        <xsl:with-param name="title">
                            <xsl:value-of select="ape:resource('eag2012.portal.readersticket')"/>
                            <xsl:text>:</xsl:text>
                        </xsl:with-param>
                        <xsl:with-param name="list" select="eag:services/eag:searchroom/eag:readersTicket"/>
                    </xsl:call-template>
                </xsl:if>
                <!-- searchroom only shown if there are values-->
                <xsl:if test="eag:services/eag:searchroom/eag:workPlaces/eag:num/text()">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.searchroom')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:apply-templates select="eag:services/eag:searchroom/eag:workPlaces/eag:num"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- advancedOrders only shown if there are values-->
                <xsl:if test="eag:services/eag:searchroom/eag:advancedOrders">
                    <xsl:call-template name="multilanguageAdvancedOrders">
                        <xsl:with-param name="title">
                            <xsl:value-of select="ape:resource('eag2012.portal.orderingdocuments')"/>
                            <xsl:text>:</xsl:text>
                        </xsl:with-param>
                        <xsl:with-param name="list" select="eag:services/eag:searchroom/eag:advancedOrders"/>
                    </xsl:call-template>
                </xsl:if>

                <!-- contact searchroom only shown if there are values-->
                <xsl:if test="eag:services/eag:searchroom/eag:contact and (eag:services/eag:searchroom/eag:contact/eag:telephone/text() or eag:services/eag:searchroom/eag:contact/eag:email/@href != '' or eag:services/eag:searchroom/eag:webpage/@href != '')">
                    <tr class="longDisplay">
                        <td class="header subInfoHeader" colspan="2">
                            <xsl:value-of select="ape:resource('eag2012.portal.searchroomcontact')"/>
                        </td>
                    </tr>
                    <xsl:call-template name="email">
                        <xsl:with-param name="parent" select="eag:services/eag:searchroom/eag:contact"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                    <xsl:call-template name="webpage">
                        <xsl:with-param name="parent" select="eag:services/eag:searchroom"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                    <xsl:call-template name="telephone">
                        <xsl:with-param name="parent" select="eag:services/eag:searchroom/eag:contact"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                </xsl:if>
                <!-- researchServices only shown if there are values-->
                <xsl:if test="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.archivesresearchservice')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageResearchServices">
                                <xsl:with-param name="list" select="eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- computerPlaces only shown if there are values-->
                <xsl:if test="eag:services/eag:searchroom/eag:computerPlaces and ((eag:services/eag:searchroom/eag:computerPlaces/eag:num and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() != '') or (eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() != ''))">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.computerplaces')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:if test="eag:services/eag:searchroom/eag:computerPlaces/eag:num and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:num/text() != ''">
                                <xsl:apply-templates select="eag:services/eag:searchroom/eag:computerPlaces/eag:num"/>
                            </xsl:if>
                            <xsl:if test="eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() and eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p/text() != ''">
                                <xsl:call-template name="multilanguageComputerPlaces">
                                    <xsl:with-param name="list" select="eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p"/>
                                </xsl:call-template>
                            </xsl:if>
                        </td>
                    </tr>
                </xsl:if>
                <!-- microfilmPlaces only shown if there are values-->
                <xsl:if test="eag:services/eag:searchroom/eag:microfilmPlaces/eag:num/text() and eag:services/eag:searchroom/eag:microfilmPlaces/eag:num/text() != ''">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.microfilmplaces')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:apply-templates select="eag:services/eag:searchroom/eag:microfilmPlaces/eag:num"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- photographAllowance only shown if there are values-->
                <xsl:if test="eag:services/eag:searchroom/eag:photographAllowance/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.photographAllowance')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:variable name="photo"  select="eag:services/eag:searchroom/eag:photographAllowance/text()"></xsl:variable>
                            <xsl:for-each select="$photo">
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$photo = 'no'">
                                            <xsl:value-of select = "ape:resource('eag2012.commons.no')"/>
                                        </xsl:when>
                                        <xsl:when test="$photo = 'yes'">
                                            <xsl:value-of select = "ape:resource('eag2012.commons.yes')"/>
                                        </xsl:when>
                                        <xsl:when test="$photo = 'depending on the material'">
                                            <xsl:value-of select = "ape:resource('eag2012.options.photograph.depending')"/>
                                        </xsl:when>
                                        <xsl:when test="$photo = 'Yes (without flash)'">
                                            <xsl:value-of select = "ape:resource('eag2012.options.photograph.without')"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text></xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </div>
                            </xsl:for-each>
                        </td>
                    </tr>
                </xsl:if>				
                <!-- internetAccess only shown if there are values-->
                <xsl:if test="eag:services/eag:internetAccess">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.publicinternetAccess')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:services/eag:internetAccess[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.available')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>

                    <xsl:if test="eag:services/eag:internetAccess[@question='yes']/eag:descriptiveNote/eag:p/text()">
                        <tr>
                            <td class="header"></td>
                            <td>
                                <xsl:call-template name="multilanguageInternetaccess">
                                    <xsl:with-param name="list" select="eag:services/eag:internetAccess[@question='yes']/eag:descriptiveNote/eag:p"/>
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:if>
                </xsl:if>
                <!-- library only shown if there are values-->
                <xsl:if test="eag:services/eag:library and (eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text() != '' or eag:services/eag:library[@question='yes']/eag:serialpub/eag:num/text() != '')">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.library')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:if test="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text()">
                                <xsl:apply-templates select="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num"/>
                            </xsl:if>
                            <xsl:if test="eag:services/eag:library[@question='yes']/eag:serialpub/eag:num/text()">
                                <xsl:if test="eag:services/eag:library[@question='yes']/eag:monographicpub/eag:num/text()">
                                    <xsl:text>, </xsl:text>
                                </xsl:if>
                                <xsl:apply-templates select="eag:services/eag:library[@question='yes']/eag:serialpub/eag:num"/>
                            </xsl:if>
                            <xsl:if test="eag:services/eag:library[@question='no']">
                                <xsl:value-of select="ape:resource('eag2012.portal.nolibrary')"/>
                            </xsl:if>
                        </td>
                    </tr>
                    <!-- contact library shown only if there is library-->
                    <xsl:if test="eag:services/eag:library/eag:contact and (eag:services/eag:library/eag:contact/eag:telephone/text() or eag:services/eag:library/eag:contact/eag:email/@href != '' or eag:services/eag:library/eag:webpage/@href != '')">
                        <tr class="longDisplay">
                            <td class="header subInfoHeader" colspan="2">
                                <xsl:value-of select="ape:resource('eag2012.portal.librarycontact')"/>
                            </td>
                        </tr>
                        <xsl:call-template name="email">
                            <xsl:with-param name="parent" select="eag:services/eag:library/eag:contact"/>
                            <xsl:with-param name="class" select="'subHeader'"/>
                            <xsl:with-param name="trClass" select="'longDisplay'"/>
                        </xsl:call-template>
                        <xsl:call-template name="webpage">
                            <xsl:with-param name="parent" select="eag:services/eag:library"/>
                            <xsl:with-param name="class" select="'subHeader'"/>
                            <xsl:with-param name="trClass" select="'longDisplay'"/>
                        </xsl:call-template>
                        <xsl:call-template name="telephone">
                            <xsl:with-param name="parent" select="eag:services/eag:library/eag:contact"/>
                            <xsl:with-param name="class" select="'subHeader'"/>
                            <xsl:with-param name="trClass" select="'longDisplay'"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:if>
                <!-- reproductionser only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:reproductionser">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.reproductionsservices')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:services/eag:techservices/eag:reproductionser[@question='yes'] and eag:services/eag:techservices/eag:reproductionser[@question='yes']/eag:descriptiveNote/eag:p/text()">
                                    <xsl:call-template name="multilanguageReproductionser">
                                        <xsl:with-param name="list" select="eag:services/eag:techservices/eag:reproductionser/eag:descriptiveNote/eag:p"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="eag:services/eag:techservices/eag:reproductionser[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.yesreproductionser')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.noreproductionser')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- digitalser only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.digitisationservice')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:digitalser[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.available')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- photocopyser only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:photocopyser">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.photocopyingservice')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:services/eag:techservices//eag:reproductionser/eag:photocopyser[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.available')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- photographser only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:photographser">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.photographicservice')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:photographser[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.available')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- microformser only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:microformser">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.microfilmservice')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:services/eag:techservices/eag:reproductionser/eag:microformser[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.available')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.notavailable')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- contact reproductionser only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:reproductionser/eag:contact and (eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:telephone/text() or eag:services/eag:techservices/eag:reproductionser/eag:contact/eag:email/@href != '' or eag:services/eag:techservices/eag:reproductionser/eag:webpage/@href != '')">
                    <tr class="longDisplay">
                        <td class="header subInfoHeader" colspan="2">
                            <xsl:value-of select="ape:resource('eag2012.portal.reproductionsservicecontact')"/>
                        </td>
                    </tr>
                    <xsl:call-template name="email">
                        <xsl:with-param name="parent" select="eag:services/eag:techservices/eag:reproductionser/eag:contact"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                    <xsl:call-template name="webpage">
                        <xsl:with-param name="parent" select="eag:services/eag:techservices/eag:reproductionser"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                    <xsl:call-template name="telephone">
                        <xsl:with-param name="parent" select="eag:services/eag:techservices/eag:reproductionser/eag:contact"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                </xsl:if>
                <!-- restorationlab only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:restorationlab">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.conservationlaboratory')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="eag:services/eag:techservices/eag:restorationlab[@question='yes'] and eag:services/eag:techservices/eag:restorationlab[@question='yes']/eag:descriptiveNote/eag:p/text()">
                                    <xsl:call-template name="multilanguageConservationLaboratory">
                                        <xsl:with-param name="list" select="eag:services/eag:techservices/eag:restorationlab/eag:descriptiveNote/eag:p"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="eag:services/eag:techservices/eag:restorationlab[@question='yes']">
                                    <xsl:value-of select="ape:resource('eag2012.portal.yesrestorationlab')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="ape:resource('eag2012.portal.norestorationlab')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                    </tr>
                </xsl:if>
                <!-- contact restorationlab only shown if there are values-->
                <xsl:if test="eag:services/eag:techservices/eag:restorationlab/eag:contact and eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:telephone/text() and eag:services/eag:techservices/eag:restorationlab/eag:contact/eag:email/@href != '' and eag:services/eag:techservices/eag:restorationlab/eag:webpage/@href != ''">
                    <tr class="longDisplay">
                        <td class="header subInfoHeader" colspan="2">
                            <xsl:value-of select="ape:resource('eag2012.portal.restorationlabcontact')"/>
                        </td>
                    </tr>
                    <xsl:call-template name="email">
                        <xsl:with-param name="parent" select="eag:services/eag:techservices/eag:restorationlab/eag:contact"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                    <xsl:call-template name="webpage">
                        <xsl:with-param name="parent" select="eag:services/eag:techservices/eag:restorationlab"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                    <xsl:call-template name="telephone">
                        <xsl:with-param name="parent" select="eag:services/eag:techservices/eag:restorationlab/eag:contact"/>
                        <xsl:with-param name="class" select="'subHeader'"/>
                        <xsl:with-param name="trClass" select="'longDisplay'"/>
                    </xsl:call-template>
                </xsl:if>
                <!-- refreshment only shown if there are values-->
                <xsl:if test="eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.refreshmentarea')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageRefreshment">
                                <xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- exhibition only shown if there are values-->
                <xsl:if test="eag:services/eag:recreationalServices/eag:exhibition and (eag:services/eag:recreationalServices/eag:exhibition/eag:webpage/@href != '' or eag:services/eag:recreationalServices/eag:exhibition/eag:descriptiveNote/eag:p/text())">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.exhibition')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageRecreationalServicesExhibition">
                                <xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:exhibition"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- toursSession only shown if there are values-->
                <xsl:if test="eag:services/eag:recreationalServices/eag:toursSessions and (eag:services/eag:recreationalServices/eag:toursSessions/eag:webpage/@href != '' or eag:services/eag:recreationalServices/eag:toursSessions/eag:descriptiveNote/eag:p/text())">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.guidedtour')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageRecreationalServicesToursSessions">
                                <xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:toursSessions"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- otherServices only shown if there are values-->
                <xsl:if test="eag:services/eag:recreationalServices/eag:otherServices and (eag:services/eag:recreationalServices/eag:otherServices/eag:webpage/@href != '' or eag:services/eag:recreationalServices/eag:otherServices/eag:descriptiveNote/eag:p/text())">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.otherservices')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageRecreationalServicesOtherServices">
                                <xsl:with-param name="list" select="eag:services/eag:recreationalServices/eag:otherServices"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
            </tbody>
        </table>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;exhibition&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Exhibition in the part of Access and Service Information of repository of the institution.  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" containing <code>"exhibition"</code> element that have repository of the institution, on path 'eag:services/eag:recreationalServices/eag:exhibition'.</xd:param>
    </xd:doc>
    <!-- template for language exhibition -->
    <xsl:template name="multilanguageRecreationalServicesExhibition">
        <xsl:param name="list"/>
        <xsl:for-each select="$list">
            <div>
                <xsl:variable name="descriptionList" select="current()/eag:descriptiveNote/eag:p"></xsl:variable>
                <xsl:variable name="webList" select="current()/eag:webpage"></xsl:variable>
                <xsl:call-template name="multilanguageExhibition">
                    <xsl:with-param name="list" select="$descriptionList"/>
                </xsl:call-template>
                <xsl:call-template name="multilanguageWebpageExhibition">
                    <xsl:with-param name="list" select="$webList"/>
                </xsl:call-template>
            </div>
        </xsl:for-each> 
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageExhibition&gt;</code>.</xd:short>
        <xd:detail>
            Template that is called by the "multilanguageRecreationalServicesExhibition" what the displays the  contents the description of the Exhibition in the part of Access and Service Information of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" containing <code>"p"</code> element that have repository of the institution, on path 'current()/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language Exhibition-->
    <xsl:template name="multilanguageExhibition">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageExhibitionNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageExhibitionLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageToursSessions</code>&gt;.</xd:short>
        <xd:detail>
            Template that is called by the "multilanguageRecreationalServicesToursSessions" what the displays the  contents the description of the Guided tours in the part of Access and Service Information of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" containing <code>"p"</code> element that have repository of the institution, on path 'current()/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language ToursSessions-->
    <xsl:template name="multilanguageToursSessions">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageToursSessionsNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageToursSessionsLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;toursSessions&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Guided tours in the part of Access and Service Information repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" containing <code>"toursSessions"</code> element that have repository of the institution, on path 'eag:services/eag:recreationalServices/eag:toursSessions'.</xd:param>
    </xd:doc>
    <!-- template for language toursSessions -->
    <xsl:template name="multilanguageRecreationalServicesToursSessions">
        <xsl:param name="list"/>
        <xsl:for-each select="$list">
            <div>
                <xsl:variable name="descriptionList" select="current()/eag:descriptiveNote/eag:p"></xsl:variable>
                <xsl:variable name="webList" select="current()/eag:webpage"></xsl:variable>
                <xsl:call-template name="multilanguageToursSessions">
                    <xsl:with-param name="list" select="$descriptionList"/>
                </xsl:call-template>
                <xsl:call-template name="multilanguageWebpageToursSessions">
                    <xsl:with-param name="list" select="$webList"/>
                </xsl:call-template>
            </div>
        </xsl:for-each>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;otherServices&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Exhibition in the part of Access and Service Information of repository of the institution.
            Template that displays the contents of multilanguage Recreationa Services of Other Services repository of the institution  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of Other Services that repository of have the institution, on path 'eag:services/eag:recreationalServices/eag:otherServices'.</xd:param>
    </xd:doc>
    <!-- template for language otherServices -->
    <xsl:template name="multilanguageRecreationalServicesOtherServices">
        <xsl:param name="list"/>
        <xsl:for-each select="$list">
            <div>
                <xsl:variable name="descriptionList" select="current()/eag:descriptiveNote/eag:p"></xsl:variable>
                <xsl:variable name="webList" select="current()/eag:webpage"></xsl:variable>
                <xsl:call-template name="multilanguageOtherServices">
                    <xsl:with-param name="list" select="$descriptionList"/>
                </xsl:call-template>
                <xsl:call-template name="multilanguageWebpageOtherServices">
                    <xsl:with-param name="list" select="$webList"/>
                </xsl:call-template>
            </div>
        </xsl:for-each>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;advancedOrders&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Ordering Documents in advance in the part of Access and Service Information of repository of the institution.	  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="title">The array "title" to be processed and display different types of title advanced Orders that have repository of the institution, on path 'ape:resource('eag2012.portal.orderingdocuments')'.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="class">The array "class" to be processed.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="trClass">The array "trclass" to be processed and long display different types of multilanguage advanced Orders that have repository of the institution.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of advanced Orders that have repository of the institution, on path 'eag:services/eag:searchroom/eag:advancedOrders'.</xd:param>
    </xd:doc>
    <!-- template for multilanguage advancedOrders -->
    <xsl:template name="multilanguageAdvancedOrders">
        <xsl:param name="title"/>
        <xsl:param name="class" select="'header'"/>
        <xsl:param name="trClass" select="''"/>
        <xsl:param name="list"/>
        <xsl:if test="$list">
            <tr class="{$trClass}">
                <td class="{$class}">
                    <xsl:value-of select="$title"/>
                </td>
                <td>
                    <xsl:choose>
                        <xsl:when test="count($list) > 1">
                            <xsl:choose>
                                <xsl:when test="$list[@xml:lang = $language.selected]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when test="$list[@xml:lang = $language.default]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise> <!-- first language -->
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:choose>
                                            <xsl:when test="not($currentLang)">
                                                <p class="multilanguageAdvancedOrdersNoLang">
                                                    <xsl:apply-templates select="."/>
                                                </p>
                                            </xsl:when>
                                            <xsl:when test="$currentLang">
                                                <xsl:variable name="classValue">
                                                    <xsl:text>multilanguageAdvancedOrdersLang_</xsl:text>
                                                    <xsl:value-of select="$currentLang"></xsl:value-of>
                                                </xsl:variable>
                                                <p class="{$classValue}">
                                                    <xsl:apply-templates select="."/>
                                                </p>
                                            </xsl:when>
                                        </xsl:choose>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:for-each select="$list">
                                <p>
                                    <xsl:apply-templates select="."/>
                                </p>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;readersTicket&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Reader's Ticket in the part of Access and Service Information of repository of the institution.  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="title">The array "title" to be processed and display different types of title readers Ticket that have repository of the institution, on path 'ape:resource('eag2012.portal.readersticket')'.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="class">The array "class" to be processed.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="trClass">The array "trclass" to be processed and long display different types of multilanguage readers Ticket that have repository of the institution.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of readers Ticket that have repository of the institution, on path 'eag:services/eag:searchroom/eag:readersTicket'.</xd:param>
    </xd:doc>
    <!-- template for multilanguage readersTicket -->
    <xsl:template name="multilanguageReadersTicket">
        <xsl:param name="title"/>
        <xsl:param name="class" select="'header'"/>
        <xsl:param name="trClass" select="''"/>
        <xsl:param name="list"/>
        <xsl:if test="$list">
            <tr class="{$trClass}">
                <td class="{$class}">
                    <xsl:value-of select="$title"/>
                </td>
                <td>
                    <xsl:choose>
                        <xsl:when test="count($list) > 1">
                            <xsl:choose>
                                <xsl:when test="$list[@xml:lang = $language.selected]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when test="$list[@xml:lang = $language.default]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise> <!-- first language -->
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:choose>
                                            <xsl:when test="not($currentLang)">
                                                <p class="multilanguageReadersTicketNoLang">
                                                    <xsl:apply-templates select="."/>
                                                </p>
                                            </xsl:when>
                                            <xsl:when test="$currentLang">
                                                <xsl:variable name="classValue">
                                                    <xsl:text>multilanguageReadersTicketLang_</xsl:text>
                                                    <xsl:value-of select="$currentLang"></xsl:value-of>
                                                </xsl:variable>
                                                <p class="{$classValue}">
                                                    <xsl:apply-templates select="."/>
                                                </p>
                                            </xsl:when>
                                        </xsl:choose>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:for-each select="$list">
                                <p>
                                    <xsl:apply-templates select="."/>
                                </p>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;termsOfUse&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Terms of Use in the part of Access and Service Information of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="title">The array "title" to be processed and display different types of title terms of use that have repository of the institution, on path 'ape:resource('eag2012.portal.termsofuse')'.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="class">The array "class" to be processed.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="trClass">The array "trclass" to be processed and long display different types of multilanguage terms of use that have repository of the institution.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of terms of use that have repository of the institution, on path 'eag:access/eag:termsOfUse'.</xd:param>
    </xd:doc>
    <!-- template for multilanguage terms of use -->
    <xsl:template name="multilanguageTermsofuse">
        <xsl:param name="title"/>
        <xsl:param name="class" select="'header'"/>
        <xsl:param name="trClass" select="''"/>
        <xsl:param name="list"/>
        <xsl:if test="$list">
            <tr class="{$trClass}">
                <td class="{$class}">
                    <xsl:value-of select="$title"/>
                </td>
                <td>
                    <xsl:choose>
                        <xsl:when test="count($list) > 1">
                            <xsl:choose>
                                <xsl:when test="$list[@xml:lang = $language.selected]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when test="$list[@xml:lang = $language.default]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise> <!-- first language -->
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:choose>
                                            <xsl:when test="not($currentLang)">
                                                <p class="multilanguageTermsofuseNoLang">
                                                    <xsl:apply-templates select="."/>
                                                </p>
                                            </xsl:when>
                                            <xsl:when test="$currentLang">
                                                <xsl:variable name="classValue">
                                                    <xsl:text>multilanguageTermsofuseLang_</xsl:text>
                                                    <xsl:value-of select="$currentLang"></xsl:value-of>
                                                </xsl:variable>
                                                <p class="{$classValue}">
                                                    <xsl:apply-templates select="."/>
                                                </p>
                                            </xsl:when>
                                        </xsl:choose>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:for-each select="$list">
                                <p>
                                    <xsl:apply-templates select="."/>
                                </p>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageWithChilds&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Directions in the part of Access and Service Information of repository of the institution.  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="title">The array "title" to be processed and display different types of title with childs that have repository of the institution, on path 'ape:resource('eag2012.portal.directions')'.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="class">The array "class" to be processed.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="trClass">The array "trclass" to be processed and longdisplay different types of multilanguage with childs that have repository of the institution.</xd:param>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of multilanguage with childs that have repository of the institution, on path 'eag:directions'.</xd:param>
    </xd:doc>
    <!-- template for multilanguage with childs -->
    <xsl:template name="multilanguageWithChilds">
        <xsl:param name="title"/>
        <xsl:param name="class" select="'header'"/>
        <xsl:param name="trClass" select="''"/>
        <xsl:param name="list"/>
        <xsl:if test="$list">
            <tr class="{$trClass}">
                <td class="{$class}">
                    <xsl:value-of select="$title"/>
                </td>
                <td>
                    <xsl:choose>
                        <xsl:when test="count($list) > 1">
                            <xsl:choose>
                                <xsl:when test="$list[@xml:lang = $language.selected]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when test="$list[@xml:lang = $language.default]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                            <p>
                                                <xsl:apply-templates select="."/>
                                            </p>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise> <!-- first language -->
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:choose>
                                            <xsl:when test="not($currentLang)">
                                                <p class="multilanguageWithChildsNoLang">
                                                    <xsl:apply-templates select="."/>
                                                </p>
                                            </xsl:when>
                                            <xsl:when test="$currentLang">
                                                <xsl:variable name="classValue">
                                                    <xsl:text>multilanguageWithChildsLang_</xsl:text>
                                                    <xsl:value-of select="$currentLang"></xsl:value-of>
                                                </xsl:variable>
                                                <p class="{$classValue}">
                                                    <xsl:apply-templates select=".">
                                                        <xsl:with-param name="directions" select="$classValue"/>
                                                    </xsl:apply-templates>
                                                </p>
                                            </xsl:when>
                                        </xsl:choose>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:for-each select="$list">
                                <p>
                                    <xsl:apply-templates select="."/>
                                </p>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageReproductionser&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Reproductions Services in the part of Access and Service Information of repository of the institution. 
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array " name="list">The array "list" to be processed and display different types of reproductionser that have repository of the institution, on path 'eag:services/eag:techservices/eag:reproductionser/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language reproductionser -->
    <xsl:template name="multilanguageReproductionser">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageReproductionserNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageReproductionserLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;restaccess&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Access Information in the part of Access and Service Information of repository of the institution.   
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of rest access that have repository of the institution, on path 'eag:access/eag:restaccess'.</xd:param>
    </xd:doc>
    <!-- template for language restaccess -->
    <xsl:template name="multilanguageRestaccess">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageRestaccessNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageRestaccessLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;accessibility&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description fo the Facilities for disabled persons in the part of Access and Service Information of repository of the institution. 
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of facilities for disabled persons that have repository of the institution, on path 'eag:accessibility'.</xd:param>
    </xd:doc>
    <!-- template for language Accessibility -->
    <xsl:template name="multilanguageAccessibility">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageAccessibilityNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageAccessibilityLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageResearchServices;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Archives Research Service in the part of Access and Service Information of repository of the institution. 
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of archives research service that have repository of the institution, on path 'eag:services/eag:searchroom/eag:researchServices/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language researchServices -->
    <xsl:template name="multilanguageResearchServices">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageResearchServicesNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageResearchServicesLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;multilanguageComputerPlaces&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Computer places in the part of Access and Service Information of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of computer places that have repository of the institution, on path 'eag:services/eag:searchroom/eag:computerPlaces/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language computerPlaces -->
    <xsl:template name="multilanguageComputerPlaces">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageComputerPlacesNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageComputerPlacesLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;multilanguageInternetaccess&gt;</code>.</xd:short>
        <xd:detail>
            Template that if the repository of the institution has Public internet access, display the  contents the description of the conditions of Public internet access in the part of Access and Service Information of repository of the institution. 
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types the description of the Internet Access that have repository of the institution, on path 'eag:services/eag:internetAccess[@question='yes']/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language description internet access -->
    <xsl:template name="multilanguageInternetaccess">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageInternetaccessNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageInternetaccessLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;webpage&gt;</code>.</xd:short>
        <xd:detail>
            Template that is called by the "multilanguageInternetaccess" what the display the  contents the description of the conditions of Public internet access in the part of Access and Service Information of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of webpage that have repository of the institution, on path 'current()/eag:webpage'.</xd:param>
    </xd:doc>
    <!-- template for multilanguage for webpage Exhibition-->
    <xsl:template name="multilanguageWebpageExhibition">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected]">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$link and $link != '' and $text and $text != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$link and $link != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$text and $text != ''">
                                            <xsl:value-of select="$text"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </div>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default]">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$link and $link != '' and $text and $text != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$link and $link != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$text and $text != ''">
                                            <xsl:value-of select="$text"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </div>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <div class="webpageExhibitionNoLang">
                                        <xsl:choose>
                                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="$text"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$link and $link != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$text and $text != ''">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                        </xsl:choose>
                                    </div>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>webpageExhibitionLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <div class="{$classValue}">
                                        <xsl:choose>
                                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="$text"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$link and $link != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$text and $text != ''">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                        </xsl:choose>
                                    </div>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <xsl:variable name="link" select="current()/@href"></xsl:variable>
                    <xsl:variable name="text" select="current()/text()"></xsl:variable>
                    <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                    <div>
                        <xsl:choose>
                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                <a href="{$link}" target="_blank">
                                    <xsl:value-of select="$text"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="$link and $link != ''">
                                <a href="{$link}" target="_blank">
                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="$text and $text != ''">
                                <xsl:value-of select="$text"/>
                            </xsl:when>
                        </xsl:choose>
                    </div>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;webpage&gt;</code>.</xd:short>
        <xd:detail>
            Template that is called by the "multilanguageRecreationalServicesToursSessions" what the displays the  contents the description of the Guided tours in the part of Access and Service Information of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of webpage Tours Sessions that have repository of the institution, on path 'current()/eag:webpage'.</xd:param>
    </xd:doc>	
    <!-- template for multilanguage for webpage ToursSessions-->
    <xsl:template name="multilanguageWebpageToursSessions">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected]">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$link and $link != '' and $text and $text != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$link and $link != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$text and $text != ''">
                                            <xsl:value-of select="$text"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </div>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default]">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$link and $link != '' and $text and $text != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$link and $link != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$text and $text != ''">
                                            <xsl:value-of select="$text"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </div>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <div class="webpageToursSessionsNoLang">
                                        <xsl:choose>
                                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="$text"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$link and $link != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$text and $text != ''">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                        </xsl:choose>
                                    </div>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>webpageToursSessionsLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <div class="{$classValue}">
                                        <xsl:choose>
                                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="$text"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$link and $link != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$text and $text != ''">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                        </xsl:choose>
                                    </div>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <xsl:variable name="link" select="current()/@href"></xsl:variable>
                    <xsl:variable name="text" select="current()/text()"></xsl:variable>
                    <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                    <div>
                        <xsl:choose>
                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                <a href="{$link}" target="_blank">
                                    <xsl:value-of select="$text"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="$link and $link != ''">
                                <a href="{$link}" target="_blank">
                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="$text and $text != ''">
                                <xsl:value-of select="$text"/>
                            </xsl:when>
                        </xsl:choose>
                    </div>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;webpage&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents of multilanguage for webpage of Other Services of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of webpage of Other Services of the repository that have the institution, on path 'current()/eag:webpage'.</xd:param>
    </xd:doc>
    <!-- template for multilanguage for webpage Other Services-->
    <xsl:template name="multilanguageWebpageOtherServices">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected]">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($link or $text)">
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$link and $link != '' and $text and $text != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$link and $link != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$text and $text != ''">
                                            <xsl:value-of select="$text"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </div>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default]">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($link or $text)">
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$link and $link != '' and $text and $text != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$link and $link != ''">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$text and $text != ''">
                                            <xsl:value-of select="$text"/>
                                        </xsl:when>
                                    </xsl:choose>
                                </div>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <div class="webpageOtherServicesNoLang">
                                        <xsl:choose>
                                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="$text"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$link and $link != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$text and $text != ''">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                        </xsl:choose>
                                    </div>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>webpageOtherServicesLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <div class="{$classValue}">
                                        <xsl:choose>
                                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="$text"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$link and $link != ''">
                                                <a href="{$link}" target="_blank">
                                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                                </a>
                                            </xsl:when>
                                            <xsl:when test="$text and $text != ''">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                        </xsl:choose>
                                    </div>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <xsl:variable name="link" select="current()/@href"></xsl:variable>
                    <xsl:variable name="text" select="current()/text()"></xsl:variable>
                    <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                    <div>
                        <xsl:choose>
                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                <a href="{$link}" target="_blank">
                                    <xsl:value-of select="$text"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="$link and $link != ''">
                                <a href="{$link}" target="_blank">
                                    <xsl:value-of select="ape:resource('eag2012.portal.gotothewebsite')"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="$text and $text != ''">
                                <xsl:value-of select="$text"/>
                            </xsl:when>
                        </xsl:choose>
                    </div>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageRefreshment&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents of refreshment of repository of the institution  
        </xd:detail>
        A template with a parameter of the type string .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of refreshment the repository of that have the institution, on path 'eag:services/eag:recreationalServices/eag:refreshment/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language refreshment -->
    <xsl:template name="multilanguageRefreshment">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageRefreshmentNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageRefreshmentLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageOtherServices&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents of language OtherServices of repository of the institution. 
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of language OtherServices that have repository of the institution, on path 'current()/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language OtherServices-->
    <xsl:template name="multilanguageOtherServices">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageOtherServicesNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageOtherServicesLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;multilanguageConservationLaboratory&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Conservation laboratory in the part of Access and Service Information of repository of the institution.
	  	   
        </xd:detail>
        A template with a parameter of the type string .
        <xd:param type="string" name="list">The array "list" containing <code>"p"</code> element that have repository of the institution, on path 'eag:services/eag:techservices/eag:restorationlab/eag:descriptiveNote/eag:p'.</xd:param>
    </xd:doc>
    <!-- template for language description conservationLaboratory -->
    <xsl:template name="multilanguageConservationLaboratory">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:value-of select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="multilanguageConservationLaboratoryNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageConservationLaboratoryLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>	  
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <p>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>		
    <xd:doc>
        <xd:short>Template for <code>&lt;advancedOrders&gt;</code>.</xd:short>
        <xd:detail>
            Template to see if the <code>"advancedOrders"</code> element has the <code>"href"</code> attribute sets the text with link, if it has not the <code>"href"</code> attribute sets the plain text.
        </xd:detail>
    </xd:doc>
    <!-- template for advancedOrders -->
    <xsl:template match="eag:advancedOrders">
        <xsl:choose>
            <xsl:when test="./@href and ./@href != ''">
                <xsl:variable name="href" select="./@href"/>
                <a href="{$href}" target="_blank">
                    <xsl:choose>
                        <xsl:when test="./text() and ./text() != ''">
                            <xsl:value-of select="."/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="ape:resource('eag2012.portal.advancedorderslink')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;readersTicket&gt;</code>.</xd:short>
        <xd:detail>
            Template to see if the <code>"readersTicket"</code> element  has the <code>"href"</code> attribute sets the text with link, if it has not the <code>"href"</code> attribute sets the plain text.
        </xd:detail>
    </xd:doc>
    <!-- template for readersTicket -->
    <xsl:template match="eag:readersTicket">
        <xsl:choose>
            <xsl:when test="./@href and ./@href != ''">
                <xsl:variable name="href" select="./@href"/>
                <a href="{$href}" target="_blank">
                    <xsl:choose>
                        <xsl:when test="./text() and ./text() != ''">
                            <xsl:value-of select="."/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="ape:resource('eag2012.portal.readersticketlink')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;termsOfUse&gt;</code>.</xd:short>
        <xd:detail>
            Template to see if the <code>"termsOfUse"</code> element has the <code>"href"</code> attribute sets the text with link, if it has not the <code>"href"</code> attribute sets the plain text.
        </xd:detail>
    </xd:doc>
    <!-- template for termsOfUse -->
    <xsl:template match="eag:termsOfUse">
        <xsl:choose>
            <xsl:when test="./@href and ./@href != ''">
                <xsl:variable name="href" select="./@href"/>
                <a href="{$href}" target="_blank">
                    <xsl:choose>
                        <xsl:when test="text() and text() != ''">
                            <xsl:value-of select="."/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="ape:resource('eag2012.portal.termsofuselink')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;citation&gt;</code>.</xd:short>
        <xd:detail>
            Template that is called by the <code>"citation"</code> template , <code>"citation"</code> element has the <code>"href"</code> attribute sets the link, if it has not the <code>"href"</code> attribute sets the plain text.
        </xd:detail>
    </xd:doc>
    <!-- template for citation -->
    <xsl:template match="eag:citation">
        <xsl:choose>
            <xsl:when test="./@href and ./@href != ''">
                <xsl:variable name="href" select="./@href"/>
                <a href="{$href}" target="_blank">
                    <xsl:value-of select="ape:resource('eag2012.portal.directionscitation')"/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;directions&gt;</code>.</xd:short>
        <xd:detail>
            Template with <code>"directions"</code> element that calls the <code>Citation</code> template and displays the <code>"directions"</code> that has the repository of the institution.
        </xd:detail>	 
        A template with a parameter of the type element .
        <xd:param type="element" name="directions">The <code>"directions"</code> element  to be processed and display directions that has the repository of the institution, on path './eag:repositoryName'.</xd:param>
    </xd:doc>
    <!-- template for citation-->
    <xsl:template match="eag:directions">
        <xsl:param name="directions"/>
        <xsl:value-of select="text()"/>
        <xsl:for-each select="eag:citation">
            <p class="{$directions}">
                <xsl:apply-templates select="."/>
            </p>
        </xsl:for-each>
    </xsl:template> 
</xsl:stylesheet>
