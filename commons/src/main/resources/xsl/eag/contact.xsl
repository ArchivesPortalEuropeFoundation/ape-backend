<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 
                xmlns:xd="http://www.pnp-software.com/XSLTdoc"
                exclude-result-prefixes="xlink xlink xsi eag ape xd">

    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
    <!-- <xsl:template match="/"> -->
    <xd:doc type="stylesheet">
        <xd:short>Page to display the part of Contact details.</xd:short>
        <xd:detail>
            This section displays information related to the different addresses it can take an institution (visitors address, etc). Addition shall include all contact information and related web pages. This section is available for those institutions with one or more repositories.
        </xd:detail>  
    </xd:doc>
    <xd:doc>
        <xd:short>Display the part of  <code>Contact</code>.</xd:short>
        <xd:detail>
            Display the part of contact the information of repository to view.	   
        </xd:detail>
        A template with a parameter of the type string .
        <xd:param type="string" name="id">The string "id" is the selected repository.</xd:param>
    </xd:doc>
    <!-- CONTACT -->
    <!-- starts loop -->		
    <xsl:template name="contact">
        <xsl:param name="id"/>
        <table class="aiSection contactDisplay">
            <thead>
                <tr>
                    <th colspan="2">
                        <xsl:value-of select="ape:resource('eag2012.portal.contact')"/>
                        <!-- Check if occult parts exists -->
                        <xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:localentity/text() or eag:location[not(@localType) or @localType='visitors address']/eag:secondem/text() or eag:location/eag:firstdem/text() or eag:location[@localType='postal address'] or eag:fax and eag:fax/text() != ''">
                            <xsl:text> (</xsl:text>
                            <a class="displayLinkSeeMore" href="javascript:seeMore('contactDisplay','{$id}');">
                                <xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>
                            </a>
                            <a class="displayLinkSeeLess" href="javascript:seeLess('contactDisplay','{$id}');">
                                <xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>
                            </a>
                            <xsl:text>)</xsl:text>
                        </xsl:if>
                    </th>
                </tr>
            </thead>
            <tbody>
                <!-- visitors Address only shown if there are values -->
                <xsl:if test="eag:location[not(@localType) or @localType='visitors address'] and eag:location[not(@localType) or @localType='visitors address']/eag:street/text() and eag:location[not(@localType) or @localType='visitors address']/eag:municipalityPostalcode/text()">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.visitorsaddress')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td class="address gel_address gel_contactDetails">
                            <xsl:call-template name="multilanguageAddress">
                                <xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- local entity only shown if there are values-->
                <xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:localentity/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.district')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td class="gel_district gel_contactDetails">
                            <xsl:call-template name="multilanguage">
                                <xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:localentity"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- secondem only shown if there are values-->
                <xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:secondem/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.countylocalauthority')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td class="gel_county gel_contactDetails">
                            <xsl:call-template name="multilanguage">
                                <xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:secondem"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- firstdem only shown if there are values-->
                <xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:firstdem/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.regionautonomousauthority')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td class="gel_region gel_contactDetails">
                            <xsl:call-template name="multilanguage">
                                <xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:firstdem"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- postal address only shown if there are values-->
                <xsl:if test="eag:location[@localType='postal address'] and eag:location[@localType='postal address']/eag:street/text() and eag:location[@localType='postal address']/eag:municipalityPostalcode/text() and eag:location[@localType='postal address']/eag:municipalityPostalcode/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.postaladdress')"></xsl:value-of>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td class="postalAddress gel_postalAddress gel_contactDetails">
                            <xsl:call-template name="multilanguagePostalAddress">
                                <xsl:with-param name="list" select="eag:location[@localType='postal address']"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- country only shown if there are values-->
                <xsl:if test="eag:location[not(@localType) or @localType='visitors address']/eag:country and eag:location[not(@localType) or @localType='visitors address']/eag:country/text() != ''">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.country')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td class="gel_country gel_contactDetails">
                            <xsl:call-template name="multilanguage">
                                <xsl:with-param name="list" select="eag:location[not(@localType) or @localType='visitors address']/eag:country"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
					
                    <xsl:call-template name="email">
                        <xsl:with-param name="class" select="'header'"/>
                    </xsl:call-template>
                    <xsl:call-template name="webpage">
                        <xsl:with-param name="class" select="'header'"/>
                    </xsl:call-template>
                    <xsl:call-template name="telephone">
                        <xsl:with-param name="class" select="'header'"/>
                    </xsl:call-template>
					
                </xsl:if>
                <!-- fax only shown if there are values-->
                <xsl:if test="eag:fax and eag:fax/text() != ''">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.fax')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td class="gel_fax gel_contactDetails">
                            <xsl:for-each select="eag:fax">
                                <div>										
                                    <xsl:value-of select="."/>
                                </div>
                            </xsl:for-each>
                        </td>
                    </tr>
                </xsl:if>
                <!-- roleofthearchive only shown if there are values-->
                <xsl:if test="current()/eag:repositoryRole and current()/eag:repositoryRole/text() != ''  and (current()/eag:repositoryRole/text() = 'Branch' or current()/eag:repositoryRole/text() = 'Head quarter' or current()/eag:repositoryRole/text() = 'Interim archive')">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.roleofthearchive')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:variable name="role" select="current()/eag:repositoryRole/text()"></xsl:variable>
                            <div>
                                <xsl:choose>
                                    <xsl:when test="$role = 'Branch'">
                                        <xsl:value-of select = "ape:resource('eag2012.options.role.branch')"/>
                                    </xsl:when>
                                    <xsl:when test="$role = 'Head quarter'">
                                        <xsl:value-of select = "ape:resource('eag2012.options.role.headquarters')"/>
                                    </xsl:when>
                                    <xsl:when test="$role = 'Interim archive'">
                                        <xsl:value-of select = "ape:resource('eag2012.options.role.interimArchive')"/>
                                    </xsl:when>	
                                    <xsl:otherwise>
                                        <xsl:text></xsl:text>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </td>
                    </tr>
                </xsl:if>
            </tbody>
        </table>
    </xsl:template>				
    <xd:doc>
        <xd:short>Template for <code>&lt;multilanguagePostalAddress&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Postal Address  in the part of Contact Details of repository of the institution, only shown if there are values.	
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"location"</code> element with <code>"localType"</code> attribute with value='postal address' that have repository of the institution, on path "eag:location[@localType='postal address']". 
        </xd:param>
    </xd:doc>
    <!-- template for language postal address. -->
    <xsl:template name="multilanguagePostalAddress">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list/eag:street[@xml:lang = $language.selected] and $list/eag:street[@xml:lang = $language.selected]/text() and $list/eag:street[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
                            <xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
                            <xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($currentStreetText or $currentMunicipalityText)">
                                <p>
                                    <xsl:if test="$currentStreetText and $currentStreetText != ''">
                                        <xsl:value-of select="$currentStreetText"/>
                                        <xsl:if test="$currentMunicipalityText">
                                            <xsl:text>, </xsl:text>
                                        </xsl:if>
                                    </xsl:if>
                                    <xsl:if test="$currentMunicipalityText and $currentMunicipalityText != ''">
                                        <xsl:value-of select="$currentMunicipalityText"/>
                                    </xsl:if>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list/eag:street[@xml:lang = $language.default] and $list/eag:street[@xml:lang = $language.default]/text() and $list/eag:street[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
                            <xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
                            <xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
                            <xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($currentStreetText or $currentMunicipalityText)">
                                <p>
                                    <xsl:if test="$currentStreetText and $currentStreetText != ''">
                                        <xsl:value-of select="$currentStreetText"/>
                                        <xsl:if test="$currentMunicipalityText">
                                            <xsl:text>, </xsl:text>
                                        </xsl:if>
                                    </xsl:if>
                                    <xsl:if test="$currentMunicipalityText and $currentMunicipalityText != ''">
                                        <xsl:value-of select="$currentMunicipalityText"/>
                                    </xsl:if>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise> <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/eag:street/@xml:lang"></xsl:variable>
                            <xsl:variable name="currentStreetText" select="current()/eag:street/text()"></xsl:variable>
                            <xsl:variable name="currentMunicipalityText" select="current()/eag:municipalityPostalcode/text()"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="postalAddressNoLang">
                                        <xsl:if test="current()/eag:street/text()">
                                            <xsl:value-of select="current()/eag:street/text()"/>
                                            <xsl:if test="current()/eag:municipalityPostalcode/text()">
                                                <xsl:text>, </xsl:text>
                                            </xsl:if>
                                        </xsl:if>
                                        <xsl:if test="current()/eag:municipalityPostalcode/text()">
                                            <xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
                                        </xsl:if>
                                    </p>	
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>postalAddressLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
                                        <xsl:if test="current()/eag:street/text()">
                                            <xsl:value-of select="current()/eag:street/text()"/>
                                            <xsl:if test="current()/eag:municipalityPostalcode/text()">
                                                <xsl:text>, </xsl:text>
                                            </xsl:if>
                                        </xsl:if>
                                        <xsl:if test="current()/eag:municipalityPostalcode/text()">
                                            <xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
                                        </xsl:if>
                                    </p> 
                                </xsl:when>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <xsl:if test="current()/eag:street/text()">
                        <xsl:value-of select="current()/eag:street/text()"/>
                        <xsl:if test="current()/eag:municipalityPostalcode/text()">
                            <xsl:text>, </xsl:text>
                        </xsl:if>
                    </xsl:if>
                    <xsl:if test="current()/eag:municipalityPostalcode/text()">
                        <xsl:value-of select="current()/eag:municipalityPostalcode/text()"/>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
