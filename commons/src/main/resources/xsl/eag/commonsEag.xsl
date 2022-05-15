<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 
                xmlns:xd="http://www.pnp-software.com/XSLTdoc"
                exclude-result-prefixes="xlink xlink xsi eag ape xd">
    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
    <xd:doc type="stylesheet">
        <xd:short>Page with template commons for all pages the EAG 2012 XML.</xd:short>
        <xd:detail>
 
        </xd:detail>  
    </xd:doc>

    <xd:doc>
        <xd:short>Template for <code>&lt;email&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the E-mail Address in the part of Contact Details or Access and Service Information (in Searchroom Contact) of the institution. If the E-mail Address has the <code>"href"</code> attribute sets the text with link, if it has not the <code>"href"</code> attribute sets the plain text.
            <br>Template commons with contact.xsl, accessService.xsl and aidetails-preview.xsl.</br>
        </xd:detail>
        A template with a parameter of the type element .
        <xd:param type="element" name="parent">The element <code>"parent"</code>.</xd:param>
        A template with a parameter of the type style .
        <xd:param type="style" name="class">
            The style <code>"class"</code> to give the cell style.
        </xd:param>
        A template with a parameter of the type style .
        <xd:param type="style" name="trClass">The style <code>"trClass"</code> to give the file style.</xd:param>
    </xd:doc>
    <!-- template for email-->
    <xsl:template name="email">
        <xsl:param name="parent" select="current()"/>
        <xsl:param name="class"/>
        <xsl:param name="trClass" select="''"/>
        <xsl:if test="$parent/eag:email/@href and $parent/eag:email/@href != ''">
            <tr class="{$trClass}">
                <td class="{$class}">
                    <xsl:value-of select="ape:resource('eag2012.portal.email')"/>
                    <xsl:text>:</xsl:text>
                </td>
                <td class="gel_email gel_contactDetails">
                    <xsl:variable name="list" select="$parent/eag:email"></xsl:variable>
                    <xsl:choose>
                        <xsl:when test="count($list) > 1">
                            <xsl:choose>
								
                                <xsl:when test="$list[@xml:lang = $language.selected]">
                                    <xsl:for-each select="$list">
                                        <xsl:variable name="email" select="current()/@href"></xsl:variable>
                                        <xsl:variable name="text" select="current()/text()"></xsl:variable>
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="(not($currentLang) or $currentLang = $language.selected) and ($email or $text)">
                                            <div>
                                                <xsl:choose>
                                                    <xsl:when test="$email and $email != '' and $text and $text != ''">
                                                        <a href="mailto:{$email}" target="_blank">
                                                            <xsl:value-of select="$text"/>
                                                        </a>
                                                    </xsl:when>
                                                    <xsl:when test="$email and $email != ''">
                                                        <a href="mailto:{$email}" target="_blank">
                                                            <xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
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
                                        <xsl:variable name="email" select="current()/@href"></xsl:variable>
                                        <xsl:variable name="text" select="current()/text()"></xsl:variable>
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:if test="(not($currentLang) or $currentLang = $language.default) and ($email or $text)">
                                            <div>
                                                <xsl:choose>
                                                    <xsl:when test="$email and $email != '' and $text and $text != ''">
                                                        <a href="mailto:{$email}" target="_blank">
                                                            <xsl:value-of select="$text"/>
                                                        </a>
                                                    </xsl:when>
                                                    <xsl:when test="$email and $email != ''">
                                                        <a href="mailto:{$email}" target="_blank">
                                                            <xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
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
                                        <xsl:variable name="email" select="current()/@href"></xsl:variable>
                                        <xsl:variable name="text" select="current()/text()"></xsl:variable>
                                        <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                        <xsl:choose>
                                            <xsl:when test="not($currentLang)">
                                                <div class="emailNoLang">
                                                    <xsl:choose>
                                                        <xsl:when test="$email and $email != '' and $text and $text != ''">
                                                            <a href="mailto:{$email}" target="_blank">
                                                                <xsl:value-of select="$text"/>
                                                            </a>
                                                        </xsl:when>
                                                        <xsl:when test="$email and $email != ''">
                                                            <a href="mailto:{$email}" target="_blank">
                                                                <xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
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
                                                    <xsl:text>emailLang_</xsl:text>
                                                    <xsl:value-of select="$currentLang"></xsl:value-of>
                                                </xsl:variable>
                                                <div class="{$classValue}">
                                                    <xsl:choose>
                                                        <xsl:when test="$email and $email != '' and $text and $text != ''">
                                                            <a href="mailto:{$email}" target="_blank">
                                                                <xsl:value-of select="$text"/>
                                                            </a>
                                                        </xsl:when>
                                                        <xsl:when test="$email and $email != ''">
                                                            <a href="mailto:{$email}" target="_blank">
                                                                <xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
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
                        <xsl:otherwise>  <!-- There is only one element in the list -->
                            <xsl:for-each select="$list">
                                <xsl:variable name="email" select="current()/@href"></xsl:variable>
                                <xsl:variable name="text" select="current()/text()"></xsl:variable>
                                <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                                <div>
                                    <xsl:choose>
                                        <xsl:when test="$email and $email != '' and $text and $text != ''">
                                            <a href="mailto:{$email}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:when>
                                        <xsl:when test="$email and $email != ''">
                                            <a href="mailto:{$email}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.contactUsByEmail')"/>
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
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;webpage&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Webpage calling the template "multilaguageWebpage" in the part of Contact Details or Access and Service Information (in Searchroom Contact) of the institution.If the Webpage has the <code>"href"</code> attribute sets the text with link, if it has not the <code>"href"</code> attribute sets the plain text.
            <br>Template commons with contact.xsl , accessService.xsl and aidetails-preview.xsl.</br>
        </xd:detail>
        A template with a parameter of the type element .
        <xd:param type="element" name="parent">The element <code>"parent"</code>.</xd:param>
        A template with a parameter of the type style .
        <xd:param type="style" name="class">
            The style <code>"class"</code> to give the cell style.
        </xd:param>
        A template with a parameter of the type style .
        <xd:param type="style" name="trClass">The style <code>"trClass"</code> to give the file style.</xd:param>
     	  
    </xd:doc>
    <!-- template for webpage-->
    <xsl:template name="webpage">
        <xsl:param name="parent" select="current()"/>
        <xsl:param name="class"/>
        <xsl:param name="trClass" select="''"/>
        <xsl:choose>
            <xsl:when test="$parent/eag:webpage/@href and $parent/eag:webpage/@href != ''">
                <tr class="{$trClass}">
                    <td class="{$class}">
                        <xsl:value-of select="ape:resource('eag2012.portal.webpage')"/>
                        <xsl:text>:</xsl:text>
                    </td>
                    <td class="gel_webpage gel_contactDetails">
                        <xsl:call-template name="multilaguageWebpage">
                            <xsl:with-param name="list" select="$parent/eag:webpage"/>
                        </xsl:call-template>
                    </td>
                </tr>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;telephone&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Telephone in the part of Contact Details or Access and Service Information (in Searchroom Contact) of the institution. If the Telephone has the <code>"href"</code> attribute sets the text with link, if it has not the <code>"href"</code> attribute sets the plain text.
            <br>Template commons with contact.xsl, accessService.xsl and aidetails-preview.xsl.</br>
        </xd:detail>
        A template with a parameter of the type element .
        <xd:param type="element" name="parent">The element <code>"parent"</code>.</xd:param>
        A template with a parameter of the type style .
        <xd:param type="style" name="class">
            The style <code>"class"</code> to give the cell style.
        </xd:param>
        A template with a parameter of the type style .
        <xd:param type="style" name="trClass">The style <code>"trClass"</code> to give the file style.</xd:param>
	  
    </xd:doc>
    <!-- template for telephone-->
    <xsl:template name="telephone">
        <xsl:param name="parent" select="current()"/>
        <xsl:param name="class"/>
        <xsl:param name="trClass" select="''"/>
        <xsl:if test="$parent/eag:telephone and $parent/eag:telephone/text()">
            <tr class="{$trClass}">
                <td class="{$class}">
                    <xsl:value-of select="ape:resource('eag2012.portal.tel')"/>
                    <xsl:text>:</xsl:text>
                </td>
                <td class="gel_telephone gel_contactDetails">
                    <xsl:for-each select="$parent/eag:telephone">
                        <div>
                            <xsl:value-of select="."/>
                        </div>
                    </xsl:for-each>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;multilaguageWebpage&gt;</code>.</xd:short>
        <xd:detail>
            Template that is called by the <code>"webpage"</code> template,<code>"webpage"</code> element has the <code>"href"</code> attribute sets the link, if it has not the <code>"href"</code> attribute sets the plain text, for the displays the contents of multilanguage for webpage in the part of Contact Details or Access and Service Information (in Searchroom Contact) of the institution.
        </xd:detail>
        A template with a parameter of the type array.
        <xd:param type="array" name="list">
            The array "list" containing <code>"webpage"</code> element that have the institution, on path '$parent/eag:webpage'. 		 
        </xd:param>
    </xd:doc>
    <!-- template for multilanguage for webpage -->
    <xsl:template name="multilaguageWebpage">
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
                                    <div class="webpageNoLang">
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
                                        <xsl:text>webpageLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;multilanguageAddress&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Visitors Address(municipalityPostalcode and street) in the part of Contact Details of the institution, only shown if there are values.
            <br>Template commons with contact.xsl and aidetails-preview.xsl</br>
        </xd:detail>
        A template with a parameter of the type array.

        <xd:param type="array" name="list">
            The array "list" containing <code>"municipalityPostalcode"</code> element or <code>"street"</code> element that have the institution, on path 'location'. 
            <br>The  <code>location</code> element having an <code>"localType"</code> attribute='visitors address' or it havingn't the <code>"localType"</code> attribute in location</br>
        </xd:param>
    </xd:doc>
    <!-- template for language for visitors address. -->
    <xsl:template name="multilanguageAddress">
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
                                    <p class="addressNoLang">
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
                                        <xsl:text>addressLang_</xsl:text>
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
    <xd:doc>
        <xd:short>Template for <code>&lt;multilanguage&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Country and District and County/Local authority and Region/Autonomous authority in the part of Contact Details of the institution (in the preview only display the contents of country), only shown if there are values.
            <br>Template commons with contact.xsl and aidetails-preview.xsl</br>
        </xd:detail>
        A template with a parameter of the type array.
        <xd:param type="array" name="list">
            The array "list" containing <code>"localentity"</code> element or <code>"country"</code> element or <code>"firstdem"</code> element or <code>"secondem"</code> element  that have the institution, on path 'location'. 
            <br>The  <code>location</code> element having an <code>"localType"</code> attribute='visitors address' or it havingn't the <code>"localType"</code> attribute in location</br>
        </xd:param>
    </xd:doc>
    <!-- template for language -->
    <xsl:template name="multilanguage">
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
                                    <p class="multilanguageNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;closing&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Closing Dates in the part of Access and Service Information of the institution.
            <br>Template commons with accessService.xsl and aidetails-preview.xsl.</br>  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" containing <code>"closing"</code> element that have the institution, on path 'eag:timetable/eag:closing'.</xd:param>
    </xd:doc>
    <!-- template for language closing hours -->
    <xsl:template name="multilanguageClosing">
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
                                    <p class="multilanguageClosingNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageClosingLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;opening&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents the description of the Opening Hours in the part of Access and Service Information of the institution.
            <br>
                Template common accessService.xsl and aidetails-preview.xsl.
            </br>
        </xd:detail>
        A template with a parameter of the type string .
        <xd:param type="array" name="list">The array "list" containing <code>"opening"</code> element that have the institution, on path 'eag:timetable/eag:opening'.</xd:param>
    </xd:doc>
    <!-- template for language opening hours -->
    <xsl:template name="multilanguageOpening">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
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
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="link" select="current()/@href"></xsl:variable>
                            <xsl:variable name="text" select="current()/text()"></xsl:variable>
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
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
                                </p>
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
                                    <p class="multilanguageOpeningNoLang">
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
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageOpeningLang_</xsl:text>
                                        <xsl:value-of select="$currentLang"></xsl:value-of>
                                    </xsl:variable>
                                    <p class="{$classValue}">
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
                                    </p>
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
                    <p>
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
                    </p>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;repositoryName&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the title of the institution and the title of the repositories of the institution.
            <br>
                Template common aidetails.xsl and aidetails-preview.xsl.
            </br>
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" containing <code>"repositoryName"</code> element that have the institution, on path './eag:repositoryName'.</xd:param>
    </xd:doc>
    <!-- template for language only one element -->
    <xsl:template name="multilanguageOnlyOne">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list[@xml:lang = $language.selected]">
                            <xsl:if test="position() = 1">
                                <xsl:value-of select="."/>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list[@xml:lang = $language.default]">
                            <xsl:if test="position() = 1">
                                <xsl:value-of select="."/>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:variable name="currentText" select="current()/text()"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <xsl:if test="position() = 1">
                                        <xsl:value-of select="$currentText"/>
                                    </xsl:if>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="$currentLang = $language.first">
                                        <xsl:if test="position() = 1">
                                            <xsl:value-of select="$currentText"/>
                                        </xsl:if>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$list">
                    <xsl:value-of select="."/>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;nonpreform&gt;</code>.</xd:short>
        <xd:detail>
	  
            Template with <code>"nonpreform"</code> element,containing tree elements:<code>"dateSet"</code> element,<code>"dateRange"</code> and <code>"date"</code> element. Display the Date Range, Data Set and Date,that has the repository of the institution.
            <br>
                Template common aidetails.xsl and aidetails-preview.xsl.
            </br>
        </xd:detail>
    </xd:doc>
    <!-- template for nonpreform-->
    <xsl:template match="eag:nonpreform">
        <xsl:if test="./text() and ./text() != ''">
            <xsl:value-of select="text()"/>
            <!-- when there are only 1 dateSet -->
            <xsl:if test="./eag:useDates/eag:dateSet and ((./eag:useDates/eag:dateSet/eag:dateRange and ./eag:useDates/eag:dateSet/eag:dateRange/eag:fromDate/text() and ./eag:useDates/eag:dateSet/eag:dateRange/eag:toDate/text()) or (./eag:useDates/eag:dateSet/eag:date and ./eag:useDates/eag:dateSet/eag:date/text()))">
                <xsl:text> (</xsl:text>
                <xsl:apply-templates select="./eag:useDates/eag:dateSet"/>
                <xsl:text>)</xsl:text>
            </xsl:if>
            <!-- when there are only 1 dateRange -->
            <xsl:if test="./eag:useDates/eag:dateRange and ./eag:useDates/eag:dateRange/eag:fromDate/text() and ./eag:useDates/eag:dateRange/eag:toDate/text()">
                <xsl:text> (</xsl:text>
                <xsl:apply-templates select="./eag:useDates/eag:dateRange"/>
                <xsl:text>)</xsl:text>
            </xsl:if>
            <!-- when there are only 1 date -->
            <xsl:if test="./eag:useDates/eag:date and ./eag:useDates/eag:date/text()">
                <xsl:text> (</xsl:text>
                <xsl:apply-templates select="./eag:useDates/eag:date"/>
                <xsl:text>)</xsl:text>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;dateSet&gt;</code>.</xd:short>
        <xd:detail>
            Template with <code>"dateSet"</code> element,containing two elements:<code>"dateRange"</code> element and <code>"date"</code> element. Display the Date Range and Date,that has the repository of the institution.
            <br>
                Template common aidetails.xsl,otherInformation.xsl and archivesHolding.xsl.
            </br>
        </xd:detail>
    </xd:doc>
    <!-- template for dateSet -->
    <xsl:template match="eag:dateSet">
        <xsl:if test="eag:dateRange or eag:date">
            <xsl:for-each select="eag:date">
                <xsl:if test="current()/text()">
                    <xsl:value-of select="."/>
                    <xsl:if test="position() != last()">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                </xsl:if>
            </xsl:for-each>
            <xsl:if test="eag:dateRange and eag:dateRange/eag:fromDate/text() and eag:dateRange/eag:toDate/text() and eag:date/text()">
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:for-each select="eag:dateRange">
                <xsl:if test="./eag:fromDate/text() and ./eag:toDate/text()">
                    <xsl:value-of select="./eag:fromDate"/>
                    <xsl:variable name="var" select="./eag:toDate"></xsl:variable>
                    <xsl:choose>
                        <xsl:when test="string(number(substring($var,1,2)))!='NaN'">
                            <xsl:text> - </xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text> </xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:value-of select="./eag:toDate"/>
                    <xsl:if test="position() != last()">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;date&gt;</code>.</xd:short>
        <xd:detail>
            Template with <code>"date"</code> element, display the value of "date" that has the repository of the institution.
            <br>
                Template common aidetails.xsl,otherInformation.xsl and archivesHolding.xsl.
            </br>
        </xd:detail>
    </xd:doc>
    <xsl:template match="eag:date">
        <xsl:if test="./text()">
            <xsl:if test="position() != 1">
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:value-of select="text()"/>
            <xsl:for-each select="eag:date">
                <xsl:if test="current()/text()">
                    <xsl:value-of select="."/>
                    <xsl:if test="position() != last()">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                    <xsl:value-of select="current()"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;dateRange&gt;</code>.</xd:short>
        <xd:detail>
            Template with <code>"dateRange"</code> element,containing two elements:<code>"fromDate"</code> element and <code>"toDate"</code> element. Display the Date Range, display the the value of "fromDate" and the value of "toDate",that has the repository of the institution.
            <br>
                Template common aidetails.xsl,otherInformation.xsl and archivesHolding.xsl.
            </br>
        </xd:detail>
    </xd:doc>
    <!-- template for dateRange -->
    <xsl:template match="eag:dateRange">
        <xsl:if test="./eag:fromDate/text() and ./eag:toDate/text()">
            <xsl:if test="position() != 1">
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:value-of select="./eag:fromDate"/>
            <xsl:variable name="var" select="./eag:toDate"></xsl:variable>
            <xsl:choose>
                <xsl:when test="string(number(substring($var,1,2)))!='NaN'">
                    <xsl:text> - </xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text> </xsl:text>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="./eag:toDate"/>
            <xsl:for-each select="eag:dateRange">
                <xsl:if test="current()/eag:fromDate/text() and current()/eag:toDate/text()">
                    <xsl:value-of select="./eag:fromDate"/>
                    <xsl:variable name="var" select="./eag:toDate"></xsl:variable>
                    <xsl:value-of select="./eag:toDate"/>
                    <xsl:if test="position() != last()">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when test="string(number(substring($var,1,2)))!='NaN'">
                            <xsl:text> - </xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text> </xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for <code>&lt;num&gt;</code>.</xd:short>
        <xd:detail>
            Template with <code>"num"</code> element and <code>"unit"</code> attribute, display the value of "num" and "unit" that has the repository of the institution.
            <br>
                Template common aidetails.xsl, accessService.xsl and archivesHolding.xsl.
            </br>
        </xd:detail>
    </xd:doc>
    <!-- template for numeric values-->
    <xsl:template match="eag:num">
        <xsl:variable name="unit" select="concat('eag2012.portal.num.unit.', ./@unit)"/>
        <xsl:value-of select="text()"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="ape:resource($unit)" disable-output-escaping="yes"/>
    </xsl:template>
	
</xsl:stylesheet>
