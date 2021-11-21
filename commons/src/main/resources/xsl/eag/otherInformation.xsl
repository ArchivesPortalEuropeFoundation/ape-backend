<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:eag="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions" 
                xmlns:xd="http://www.pnp-software.com/XSLTdoc"
                exclude-result-prefixes="xlink xlink xsi eag ape xd">
    <xd:doc type="stylesheet">
        <xd:short>Page to display the part of Other information.</xd:short>
        <xd:detail>
            This section displays the other information on the relations in the institution.	
        </xd:detail>  
    </xd:doc>
    <xd:doc>
        <xd:short>Display the part of  <code>&lt;Other Information</code>&gt;.</xd:short>
        <xd:detail>
            Display the part of Other Information the information of repository to view.	   
        </xd:detail>	  
    </xd:doc>
    <xsl:template name="otherInformation">
        <table class="aiSection otherDisplay">
            <thead>
                <tr>
                    <th colspan="2">
                        <xsl:value-of select="ape:resource('eag2012.portal.other')"/>
                        <!-- Check if occult parts exists -->
                        <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/text() and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates and ((eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:toDate/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:toDate/text()))">
                            <xsl:text> </xsl:text>
                            <a class="displayLinkSeeMore" href="javascript:seeMore('otherDisplay');">
                                (<xsl:value-of select="ape:resource('eag2012.portal.seemore')"/>)
                            </a>
                            <a class="displayLinkSeeLess" href="javascript:seeLess('otherDisplay');">
                                (<xsl:value-of select="ape:resource('eag2012.portal.seeless')"/>)
                            </a>
<!--                            <xsl:text>)</xsl:text>-->
                        </xsl:if>
                    </th>
                </tr>
            </thead>
            <tbody>
					
                <!-- relationEntry and links to related resources-->
                <xsl:choose>
                    <xsl:when test="(./eag:eag/eag:relations/eag:resourceRelation/@href and ./eag:eag/eag:relations/eag:resourceRelation/@href != '') or (./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text() and ./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text() != '')">
                        <tr>
                            <td class="header">
                                <xsl:value-of select="ape:resource('eag2012.portal.relatedresource')"/>:
                            </td>
                            <td>
                                <xsl:call-template name="multilanguageWithLinkRelatedResources">
                                    <xsl:with-param name="list" select="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry"></xsl:with-param>
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry/text()">
                            <tr>
                                <td class="header">
                                    <xsl:value-of select="ape:resource('eag2012.portal.relatedresource')"/>
                                </td>
                                <td>
                                    <xsl:value-of select="./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry"/>
                                </td>
                            </tr>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>

                <!-- associatedrepositories only shown if there are values-->
                <xsl:choose>
                    <xsl:when test="(./eag:eag/eag:relations/eag:eagRelation/@href and ./eag:eag/eag:relations/eag:eagRelation/@href != '') or (./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry/text() and ./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry/text() != '')">
                        <tr>
                            <td class="header">
							 
                                <xsl:value-of select="ape:resource('eag2012.portal.associatedrepositories')"/>:
                            </td>
                            <td>
                                <xsl:call-template name="multilanguageWithLink">
                                    <xsl:with-param name="list" select="./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry"></xsl:with-param>
                                </xsl:call-template>
                            </td>
                        </tr>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry/text()">
                            <tr>
                                <td class="header">
                                    <xsl:value-of select="ape:resource('eag2012.portal.associatedrepositories')"/>:
                                </td>
                                <td>
                                    <xsl:value-of select="./eag:eag/eag:relations/eag:eagRelation/eag:relationEntry"/>
                                </td>
                            </tr>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>

                <!-- parform only shown if there is a value-->
                <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:parform and ./eag:eag/eag:archguide/eag:identity/eag:parform/text()">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.parformnameofthearchive')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageParform">
                                <xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:identity/eag:parform"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>

                <!-- nonpreform and useDates only shown if there are values-->
                <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:nonpreform and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/text() and ./eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates and ((eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateRange/eag:toDate/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:date/text()) or (eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:fromDate/text() and eag:eag/eag:archguide/eag:identity/eag:nonpreform/eag:useDates/eag:dateSet/eag:dateRange/eag:toDate/text()))">
                    <tr class="longDisplay">
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.alternative')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:call-template name="multilanguageNoperform">
                                <xsl:with-param name="list" select="./eag:eag/eag:archguide/eag:identity/eag:nonpreform"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>

                <!-- repositoryType only shown if there are values-->			
                <xsl:if test="./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() and (./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Business archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Church and religious archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'County/local authority archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Specialised non-governmental archives and archives of other cultural (heritage) institutions' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Media archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Municipal archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'National archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Private persons and family archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Regional archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'Specialised government archives' or ./eag:eag/eag:archguide/eag:identity/eag:repositoryType/text() = 'University and research archives')">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.typeofarchive')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <!-- if there are one or more -->
                            <xsl:for-each select="./eag:eag/eag:archguide/eag:identity/eag:repositoryType">
                                <!-- 								<xsl:if test="not(position() = last())">
                                        <xsl:text>, </xsl:text>
                                </xsl:if> --> 
                                <xsl:choose>
                                    <xsl:when test="current()/text() = 'Business archives'">
                                        <p>
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.businessArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Church and religious archives'">
                                        <p>
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.churchArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'County/local authority archives'">
                                        <p>
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.countyArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Specialised non-governmental archives and archives of other cultural (heritage) institutions'">
                                        <p>
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.culturalArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Media archives'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.mediaArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Municipal archives'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.municipalArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'National archives'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.nationalArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.politicalArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Private persons and family archives'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.privateArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Regional archives'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.regionalArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'Specialised government archives'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.specialisedArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:when test="current()/text() = 'University and research archives'">
                                        <p>	
                                            <xsl:value-of select = "ape:resource('eag2012.options.institutionType.universityArchives')"/>
                                        </p>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text> </xsl:text>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each> 
                        </td>
                    </tr>
                </xsl:if>

                <!-- repositoryType only shown if there are values-->
                <xsl:if test="./eag:eag/eag:control/eag:rightsDeclaration">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.rightsDeclaration')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:for-each select="./eag:eag/eag:control/eag:rightsDeclaration">
                                <div>
                                    <p>
                                        <xsl:variable name="hyperlink"><xsl:value-of select="./eag:citation/@href" /></xsl:variable>
                                        <xsl:if test="./eag:abbreviation">
                                            [<xsl:value-of select="./eag:abbreviation/text()"/>]
                                        </xsl:if>
                                        <a target="_blank" href="{$hyperlink}"><xsl:value-of select="./eag:citation/text()"/></a>
                                    </p>
                                    <xsl:for-each select="./eag:descriptiveNote//eag:p">
                                        <p>
                                            <xsl:value-of select="./text()"/>
                                        </p>
                                    </xsl:for-each>
                                </div>
                                <xsl:if test="not(position() = last())">
                                    <hr/>
                                </xsl:if>
                            </xsl:for-each>
                        </td>
                    </tr>
                </xsl:if>
                <!-- lastupdate only shown if there are values-->
                <xsl:variable name="numberOfMaintenanceEvent" select="count(./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent)"/>
                <xsl:if test="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent[$numberOfMaintenanceEvent]/eag:eventDateTime/text()">
                    <tr>
                        <td class="header">
                            <xsl:value-of select="ape:resource('eag2012.portal.lastupdate')"/>
                            <xsl:text>:</xsl:text>
                        </td>
                        <td>
                            <xsl:value-of select="./eag:eag/eag:control/eag:maintenanceHistory/eag:maintenanceEvent[$numberOfMaintenanceEvent]/eag:eventDateTime"/>
                        </td>
                    </tr>
                </xsl:if>
				
            </tbody>
        </table>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageWithLinkRelatedResources&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents Related Resource(Link) of the institution, <code>"resourceRelation"</code> element has the <code>"href"</code> attribute sets the link, if it has not the <code>"href"</code> attribute sets the plain text.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"relationEntry"</code> element that have repository of the institution, on path ''./eag:eag/eag:relations/eag:resourceRelation/eag:relationEntry'.</xd:param>	  
    </xd:doc>
    <!-- template for language plus link-->
    <xsl:template name="multilanguageWithLinkRelatedResources">
        <xsl:param name="list"/>
        <xsl:for-each select="$list">
            <xsl:variable name="link" select="current()/parent::node()/@href"></xsl:variable>
            <xsl:variable name="text" select="current()/text()"></xsl:variable>
            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
            <xsl:variable name="typeRelation" select="./parent::node()/@resourceRelationType"/>
            <xsl:variable name="position" select="position()"/>
            <xsl:choose>
                <xsl:when test="boolean($list/@xml:lang = $language.selected)">
                    <div id="resource_{$position}">
                        <xsl:choose>
                            <xsl:when test="$currentLang = $language.selected">
                                <xsl:choose>
                                    <xsl:when test="$link and $link != '' and $text and $text != ''">
                                        <xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="$text"/>
                                            </a>
                                            <span id="linkRelation" class="hidden">
                                                <xsl:value-of select="$link"/>
                                            </span>
                                            <span id="typeRelation" class="hidden">
                                                <xsl:value-of select="$typeRelation"/>
                                            </span>
                                            <span id="lang" class="hidden">
                                                <xsl:value-of select="$currentLang"/>
                                            </span>
                                        </xsl:if>
                                        <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                                            <a href="#">
                                                <xsl:attribute name="onclick">
                                                    <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script>
                                                </xsl:attribute>
                                                <xsl:value-of select="$text"/>
                                            </a>
                                        </xsl:if>
                                    </xsl:when>
                                    <xsl:when test="$link and $link != ''">
                                        <xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                            </a>
                                            <span id="linkRelation" class="hidden">
                                                <xsl:value-of select="$link"/>
                                            </span>
                                            <span id="typeRelation" class="hidden">
                                                <xsl:value-of select="$typeRelation"/>
                                            </span>
                                            <span id="lang" class="hidden">
                                                <xsl:value-of select="$currentLang"/>
                                            </span>
                                        </xsl:if>
                                        <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                                            <a href="#">
                                                <xsl:attribute name="onclick">
                                                    <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script>
                                                </xsl:attribute>
                                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                            </a>
                                        </xsl:if>
                                    </xsl:when>
                                    <xsl:when test="$text and $text != ''">
                                        <xsl:value-of select="$text"/>
                                    </xsl:when>
                                </xsl:choose>
                                <xsl:call-template name="multilanguageRelatedResources">
                                    <xsl:with-param name="list" select="current()/parent::node()/eag:descriptiveNote/eag:p"></xsl:with-param>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$link and $link != '' and $text and $text != ''">
                                        <xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                                        </xsl:if>
                                        <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                                            <a href="#">
                                                <xsl:attribute name="onclick">
                                                    <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script>
                                                </xsl:attribute>
                                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                            </a>
                                        </xsl:if>
                                    </xsl:when>
                                    <xsl:when test="$link and $link != ''">
                                        <xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                                            <a href="{$link}" target="_blank">
                                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                            </a>
                                            <span id="linkRelation" class="hidden">
                                                <xsl:value-of select="$link"/>
                                            </span>
                                            <span id="typeRelation" class="hidden">
                                                <xsl:value-of select="$typeRelation"/>
                                            </span>
                                            <span id="lang" class="hidden">
                                                <xsl:value-of select="$currentLang"/>
                                            </span>
                                        </xsl:if>
                                        <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                                            <a href="#">
                                                <xsl:attribute name="onclick">
                                                    <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script>
                                                </xsl:attribute>
                                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                            </a>
                                        </xsl:if>
                                    </xsl:when>
                                    <xsl:when test="$text and $text != ''">
                                        <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    <div id="resource_{$position}">
                        <xsl:choose>
                            <xsl:when test="$link and $link != '' and $text and $text != ''">
                                <xsl:if
                                    test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                                    <a href="{$link}" target="_blank">
                                        <xsl:choose>
                                            <xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </a>
                                    <span id="linkRelation" class="hidden">
                                        <xsl:value-of select="$link"/>
                                    </span>
                                    <span id="typeRelation" class="hidden">
                                        <xsl:value-of select="$typeRelation"/>
                                    </span>
                                    <span id="lang" class="hidden">
                                        <xsl:value-of select="$currentLang"/>
                                    </span>
                                </xsl:if>
                                <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                                    <a href="#">
                                        <xsl:attribute name="onclick">
                                            <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"/>');</script>
                                        </xsl:attribute>
                                        <xsl:choose>
                                            <xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
                                                <xsl:value-of select="$text"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </a>
                                </xsl:if>
                            </xsl:when>
                            <xsl:when test="$link and $link != ''">
                                <xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                                    <a href="{$link}" target="_blank">
                                        <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                    </a>
                                    <span id="linkRelation" class="hidden">
                                        <xsl:value-of select="$link"/>
                                    </span>
                                    <span id="typeRelation" class="hidden">
                                        <xsl:value-of select="$typeRelation"/>
                                    </span>
                                    <span id="lang" class="hidden">
                                        <xsl:value-of select="$currentLang"/>
                                    </span>
                                </xsl:if>
                                <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                                    <a href="#">
                                        <xsl:attribute name="onclick">
                                            <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"/>');</script>
                                        </xsl:attribute>
                                        <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                    </a>
                                </xsl:if>
                            </xsl:when>
                            <xsl:when test="$text and $text != ''">
                                <xsl:choose>
                                    <xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
                                        <xsl:value-of select="$text"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                        </xsl:choose>

                        <xsl:call-template name="multilanguageRelatedResources">
                            <xsl:with-param name="list"
                                select="current()/parent::node()/eag:descriptiveNote/eag:p"/>
                        </xsl:call-template>
                    </div>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;relationEntry&gt;</code>.</xd:short>
        <xd:detail>	   
            Template that displays the contents Associated Repositories(Link) of the institution, <code>"resourceRelation"</code> element has the <code>"href"</code> attribute sets the link, if it has not the <code>"href"</code> attribute sets the plain text.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"relationEntry"</code> element that have repository of the institution, on path './eag:eag/eag:archguide/eag:identity/eag:relationEntry'.</xd:param>
    </xd:doc>
    <!-- template for language plus link-->
    <xsl:template name="multilanguageWithLink">
        <xsl:param name="list"/>
        <xsl:for-each select="$list">
            <xsl:variable name="link" select="current()/parent::node()/@href"></xsl:variable>
            <xsl:variable name="text" select="current()/text()"></xsl:variable>
            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
            <div>
                <xsl:choose>
                    <xsl:when test="$link and $link != '' and $text and $text != ''">
                        <xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                            <a href="{$link}" target="_blank">
                                <xsl:choose>
                                    <xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
                                        <xsl:value-of select="$text"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </a>
                        </xsl:if>
                        <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                            <a href="#">
                                <xsl:attribute name="onclick">
                                    <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script>
                                </xsl:attribute>
                                <xsl:choose>
                                    <xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
                                        <xsl:value-of select="$text"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </a>
                        </xsl:if>
                    </xsl:when>
                    <xsl:when test="$link and $link != ''">
                        <xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
                            <a href="{$link}" target="_blank">
                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                            </a>
                        </xsl:if>
                        <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
                            <a href="#">
                                <xsl:attribute name="onclick">
                                    <script>recoverRelatedInstitution('<xsl:value-of select="ape:related(current()/parent::node()/@href)"></xsl:value-of>');</script>
                                </xsl:attribute>
                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                            </a>
                        </xsl:if>
                    </xsl:when>
                    <xsl:when test="$text and $text != ''">
                        <xsl:choose>
                            <xsl:when test="$currentLang = $language.selected or $currentLang = $language.default">
                                <xsl:value-of select="$text"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="ape:resource('eag2012.portal.linktorelatedresource')"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                </xsl:choose>

                <xsl:call-template name="multilanguageAssociatedrepositories">
                    <xsl:with-param name="list" select="current()/parent::node()/eag:descriptiveNote/eag:p"></xsl:with-param>
                </xsl:call-template>
            </div>
        </xsl:for-each>
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;parform&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents of Name of the Archive (in other languages) of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"parform"</code> element that have repository of the institution, on path './eag:eag/eag:archguide/eag:identity/eag:parform'.
        </xd:param>
    </xd:doc>
    <!-- template for language parform -->
    <xsl:template name="multilanguageParform">
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
                                    <p class="multilanguageParformNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageParformLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;nonpreform&gt;</code>.</xd:short>
        <xd:detail>
            Template that displays the contents of Alternative / previous name(s) of the archive of the institution.
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"nonpreform"</code> element that have repository of the institution, on path './eag:eag/eag:archguide/eag:identity/eag:nonpreform'.
        </xd:param>
    </xd:doc>
    <!-- template for language nonpreform-->
    <xsl:template name="multilanguageNoperform">
        <xsl:param name="list"/>
        <xsl:choose>
            <xsl:when test="count($list) > 1">
                <xsl:choose>
                    <xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="$currentLang = $language.selected or not($currentLang)">
                                <p>
                                    <xsl:apply-templates select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:if test="not($currentLang) or $currentLang = $language.default">
                                <p>
                                    <xsl:apply-templates select="."/>
                                </p>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>  <!-- first language -->
                        <xsl:for-each select="$list">
                            <xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="not($currentLang)">
                                    <p class="nonpreformNoLang">
                                        <xsl:apply-templates select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>nonpreformLang_</xsl:text>
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
    </xsl:template>
    <xd:doc>
        <xd:short>Template for  <code>&lt;multilanguageRelatedResources&gt;</code>.</xd:short>
        <xd:detail>
            Template that is called by the <code>"multilanguageWithLinkRelatedResources"</code> template,<code>"p"</code> element displays the  contents the description of the Related resource only link of the institution.	  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">
            The array "list" containing <code>"p"</code> element that have repository of the institution, on path 'current()/parent::node()/eag:descriptiveNote/eag:p'.
        </xd:param>
    </xd:doc>
    <!-- template for language related resources-->
    <xsl:template name="multilanguageRelatedResources">
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
                                    <p class="multilanguageRelatedResourcesNoLang">
                                        <xsl:value-of select="."/>
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageRelatedResourcesLang_</xsl:text>
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
        <xd:short>Template for  <code>&lt;multilanguageAssociatedrepositories&gt;</code>.</xd:short>
        <xd:detail>
            Template that is called by the "multilanguageWithLink" what the displays the  contents the description of the Multilanguage With Link,associated repositories only shown if there are values of the institution. 	  
        </xd:detail>
        A template with a parameter of the type array .
        <xd:param type="array" name="list">The array "list" to be processed and display different types of nonpreform that have the institution, on path 'current()/parent::node()/eag:descriptiveNote/eag:p'.
        </xd:param>
    </xd:doc>
    <!-- template for language related resources-->
    <xsl:template name="multilanguageAssociatedrepositories">
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
                                    <p class="multilanguageAssociatedrepositoriesNoLang">
                                        <xsl:value-of select="."/>
										
                                    </p>
                                </xsl:when>
                                <xsl:when test="$currentLang">
                                    <xsl:variable name="classValue">
                                        <xsl:text>multilanguageAssociatedrepositoriesLang_</xsl:text>
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
