<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 
                xmlns:xd="http://www.pnp-software.com/XSLTdoc"
                exclude-result-prefixes="xlink xlink xsi eag ape  xd">	
    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
    <xd:doc type="stylesheet">
        <xd:short>Page to display the part of Archives and Holdings Description.</xd:short>
        <xd:detail>
            This section displays the data related with descriptions (building, holding, etc) and administrative structure.
        </xd:detail>  
    </xd:doc>
    <xd:doc>
        <xd:short>Display the part of  <code>&lt;Holdings&gt;</code>.</xd:short>
        <xd:detail>
            Display the part of Holdings the Information of repository to view.	   
        </xd:detail>
        A template with a parameter of the type string .
        <xd:param type="string" name="id">The string "id" is the selected repository.</xd:param>
    </xd:doc>
    <xsl:template name="holdings">
        <xsl:param name="id"/>		
        <table class="aiSection archivesDisplay">
            <thead>
                <tr>
                    <th colspan="2">
                        <xsl:value-of select="ape:resource('eag2012.portal.archivesandholdings')"/>
                        <!-- Check if occult parts exists -->
                        <xsl:if test="eag:holdings/eag:descriptiveNote/eag:p/text() or eag:repositorhist/eag:descriptiveNote/eag:p/text() or eag:repositorfound/eag:date/text() or eag:repositorfound/eag:rule/text() or eag:repositorsup/eag:date/text() or eag:repositorsup/eag:rule/text() or eag:adminhierarchy/eag:adminunit/text() or eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text() or eag:buildinginfo/eag:repositorarea/eag:num/text() or eag:buildinginfo/eag:lengthshelf/eag:num/text()">
                            <xsl:text> </xsl:text>
                            <a class="displayLinkSeeMore" href="javascript:seeMore('archivesDisplay','{$id}');">
                                (<xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>)
                            </a>
                            <a class="displayLinkSeeLess" href="javascript:seeLess('archivesDisplay','{$id}');">
                                (<xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>)
                            </a>
<!--                            <xsl:text>)</xsl:text>-->
                        </xsl:if>
                    </th>
                </tr>
            </thead>
            <tbody>
                <!-- holdings only shown if there are values-->
                <xsl:if test="eag:holdings/eag:descriptiveNote/eag:p/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.holdings')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageHoldings">
                                <xsl:with-param name="list" select="eag:holdings/eag:descriptiveNote/eag:p"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- extent only shown if there are values-->
                <xsl:if test="eag:holdings/eag:extent/eag:num/text()">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.extentholdings')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:apply-templates select="eag:holdings/eag:extent/eag:num"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- dates of holdings only shown if there are values-->
                <xsl:if test="eag:holdings/eag:date/text() or (eag:holdings/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateRange/eag:toDate/text()) or (eag:holdings/eag:dateSet and (eag:holdings/eag:dateSet/eag:date/text() or (eag:holdings/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateSet/eag:dateRange/eag:toDate/text())))">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.datesholdings')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <!-- when there are only 1 dateSet -->
                            <xsl:if test="eag:holdings/eag:dateSet and ((eag:holdings/eag:dateSet/eag:dateRange and eag:holdings/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateSet/eag:dateRange/eag:toDate/text()) or (eag:holdings/eag:dateSet/eag:date and eag:holdings/eag:dateSet/eag:date/text()))">
                                <xsl:apply-templates select="eag:holdings/eag:dateSet"/>
                            </xsl:if>
                            <!-- when there are only 1 dateRange -->
                            <xsl:if test="eag:holdings/eag:dateRange and eag:holdings/eag:dateRange/eag:fromDate/text() and eag:holdings/eag:dateRange/eag:toDate/text()">
                                <xsl:apply-templates select="eag:holdings/eag:dateRange"/>
                            </xsl:if>
                            <!-- when there are only 1 date -->
                            <xsl:if test="eag:holdings/eag:date and eag:holdings/eag:date/text()">
                                <xsl:apply-templates select="eag:holdings/eag:date"/>
                            </xsl:if>
							
                        </td>
                    </tr>
                </xsl:if>
                <!-- repositorhist only shown if there are values-->
                <xsl:if test="eag:repositorhist/eag:descriptiveNote/eag:p/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.historyofthearchives')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageRepositorhist">
                                <xsl:with-param name="list" select="eag:repositorhist/eag:descriptiveNote/eag:p"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- date of repositorfound only shown if there are values-->
                <xsl:if test="eag:repositorfound/eag:date/text()">
                    <tr class="longDisplay">
                        <td class="subHeader">
                            <xsl:value-of select="ape:resource('eag2012.portal.daterepositorfound')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:value-of select="eag:repositorfound/eag:date"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- rule of repositorfound only shown if there are values-->
                <xsl:if test="eag:repositorfound/eag:rule/text()">
                    <tr class="longDisplay">
                        <td class="subHeader">
                            <xsl:value-of select="ape:resource('eag2012.portal.rulerepositorfound')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageRepositorfound">
                                <xsl:with-param name="list" select="eag:repositorfound/eag:rule"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- date of repositorsup only shown if there are values-->
                <xsl:if test="eag:repositorsup/eag:date/text()">
                    <tr class="longDisplay">
                        <td class="subHeader">
                            <xsl:value-of select="ape:resource('eag2012.portal.daterepositorsup')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:value-of select="eag:repositorsup/eag:date"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- rule of repositorsup only shown if there are values-->
                <xsl:if test="eag:repositorsup/eag:rule/text()">
                    <tr class="longDisplay">
                        <td class="subHeader">
                            <xsl:value-of select="ape:resource('eag2012.portal.rulerepositorsup')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageRepositorsup">
                                <xsl:with-param name="list" select="eag:repositorsup/eag:rule"></xsl:with-param>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- adminunit only shown if there are values-->
                <xsl:if test="eag:adminhierarchy/eag:adminunit/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.archivedepartment')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageAdminunit">
                                <xsl:with-param name="list" select="eag:adminhierarchy/eag:adminunit"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- building only shown if there are values-->
                <xsl:if test="eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.archivebuilding')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageBuilding">
                                <xsl:with-param name="list" select="eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <!-- repositorarea only shown if there are values-->
                <xsl:if test="eag:buildinginfo/eag:repositorarea/eag:num/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.buildingarea')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:apply-templates select="eag:buildinginfo/eag:repositorarea/eag:num"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- lengthshelf only shown if there are values-->
                <xsl:if test="eag:buildinginfo/eag:lengthshelf/eag:num/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.lengthshelfavailable')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:apply-templates select="eag:buildinginfo/eag:lengthshelf/eag:num"/>
                        </td>
                    </tr>
                </xsl:if>
            </tbody>
        </table>			
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageHoldings&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Archival and Other Holdings in the part of Archives and Holdings Description of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"p"</code> element that have repository of the institution, on path 'eag:holdings/eag:descriptiveNote/eag:p'. 
        </xd:param>
    </xd:doc>
    <!-- template for language description holdings -->
    <xsl:template name="multilanguageHoldings">
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
                                    <p class="multilanguageHoldingsNoLang">
                                        <xsl:value-of select="."/>
										
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageHoldingsLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;multilanguageRepositorhist&gt;</code>.</xd:short>
        <xd:detail>
            Template that if the contents the description of the History of the Archive in the part of Archives and Holdings Description of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"p"</code> element that have repository of the institution, on path 'eag:repositorhist/eag:descriptiveNote/eag:p'.
        </xd:param>
    </xd:doc>
    <!-- template for language repositorhist -->
    <xsl:template name="multilanguageRepositorhist">
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
                                    <p class="multilanguageRepositorhistNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageRepositorhistLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;rule&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Archives act in the part of Archives and Holdings Description of repository of the institution. 
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"rule"</code> element that have repository of the institution, on path 'eag:repositorfound/eag:rule'.
        </xd:param>
    </xd:doc>
    <!-- template for language repositorfound -->
    <xsl:template name="multilanguageRepositorfound">
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
                                    <p class="multilanguageRepositorfoundNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageRepositorfoundLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;multilanguageBuilding&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Archive Building in the part of Archives and Holdings Description of repository of the institution. 
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"p"</code> element that have repository of the institution, on path 'eag:buildinginfo/eag:building/eag:descriptiveNote/eag:p'. 
        </xd:param>
    </xd:doc>
    <!-- template for language building -->
    <xsl:template name="multilanguageBuilding">
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
                                    <p class="multilanguageBuildingNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageBuildingLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;rule&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Rule of repositorsup only shown if there are values in the part of Archives and Holdings Description of repository of the institution.   
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"rule"</code> element that have repository of the institution, on path 'eag:repositorsup/eag:rule'.
        </xd:param>
    </xd:doc>
    <!-- template for language repositorsup -->
    <xsl:template name="multilanguageRepositorsup">
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
                                    <p class="multilanguageRepositorsupNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageRepositorsupLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;adminunit&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Archive Departments in the part of Archives and Holdings Description of repository of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"adminunit"</code> element that have repository of the institution, on path 'eag:adminhierarchy/eag:adminunit'.</xd:param>
    </xd:doc>
    <!-- template for language adminunit -->
    <xsl:template name="multilanguageAdminunit">
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
                                    <p class="multilanguageAdminunitNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageAdminunitLang_</xsl:text>
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
</xsl:stylesheet>
