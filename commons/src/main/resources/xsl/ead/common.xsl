<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
                exclude-result-prefixes="xlink xlink xsi ead ape fn #default">

    <xsl:param name="eacUrl"/>
    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8"/>
    <xsl:template match="ead:head" name="head" mode="#all">
        <h4>
            <xsl:apply-templates mode="#current"/>
        </h4>
    </xsl:template>
    <xsl:template match="ead:p" mode="#all">
        <p>
            <xsl:apply-templates mode="#current"/>
        </p>
    </xsl:template>
    <xsl:template match="text()" mode="scopecontent">
        <xsl:value-of select="ape:highlight(., 'scopecontent')" disable-output-escaping="yes"/>
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template match="text()" mode="title">
        <xsl:value-of select="fn:normalize-space(ape:highlight(., 'title'))"
                      disable-output-escaping="yes"/>
    </xsl:template>
    <xsl:template match="text()" mode="other">
        <xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes"/>
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template match="text()" mode="otherwithoutwhitespace">
        <xsl:value-of select="ape:highlight(., 'otherwithoutwhitespace')"
                      disable-output-escaping="yes"/>
    </xsl:template>
    <xsl:template match="text()" mode="otherwithcoma">
        <xsl:value-of select="fn:normalize-space(ape:highlight(., 'otherwithcoma'))"
                      disable-output-escaping="yes"/>
        <!-- Extent option -->
        <xsl:if test="string-length(../@unit) > 0">
            <!--  <xsl:text> (</xsl:text>-->
            <xsl:text> </xsl:text>
            <xsl:value-of select="../@unit"/>
            <!-- <xsl:text>)</xsl:text> -->
        </xsl:if>
        <xsl:if test="position() != last()">
            <xsl:text>, </xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="text()" mode="alterdate">
        <xsl:value-of select="ape:highlight(., 'alterdate')" disable-output-escaping="yes"/>
    </xsl:template>
    <xsl:template match="text()" mode="notsearchable">
        <xsl:value-of select="."/>
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template match="ead:unitdate" mode="#all">
        <xsl:apply-templates mode="#current"/>
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template match="ead:lb" mode="#all">
        <xsl:text disable-output-escaping="yes">&lt;br&gt;</xsl:text>
    </xsl:template>
    <xsl:template match="ead:emph[@render = 'bold']" mode="#all">
        <xsl:variable name="emphValue">
            <xsl:apply-templates mode="#current"/>
        </xsl:variable>
        <b>
            <xsl:value-of select="fn:normalize-space($emphValue)"/>
        </b>
    </xsl:template>
    <xsl:template match="ead:emph[@render = 'italic']" mode="#all">
        <xsl:variable name="emphValue">
            <xsl:apply-templates mode="#current"/>
        </xsl:variable>
        <i>
            <xsl:value-of select="fn:normalize-space($emphValue)"/>
        </i>
    </xsl:template>
    <xsl:template match="ead:list[@type = 'ordered']" mode="#all">
        <p>
            <ol>
                <xsl:apply-templates mode="#current"/>
            </ol>
        </p>
    </xsl:template>
    <xsl:template match="ead:list[@type = 'marked'] | ead:list[not(@type)]" mode="#all">
        <p>
            <ul>
                <xsl:apply-templates mode="#current"/>
            </ul>
        </p>
    </xsl:template>
    <xsl:template match="ead:item" mode="#all">
        <li>
            <xsl:apply-templates mode="#current"/>
        </li>
    </xsl:template>
    <xsl:template match="ead:dao" mode="#all">
        <xsl:variable name="href" select="./@xlink:href"/>
        <a href="{$href}" target="_blank">
            <xsl:variable name="dao.title">
                <xsl:choose>
                    <xsl:when test="./@xlink:title">
                        <xsl:value-of select="./@xlink:title"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="ape:resource('eadcontent.dao.notitle')"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="./@xlink:role">
                    <xsl:variable name="type" select="fn:lower-case(./@xlink:role)"/>
                    <xsl:choose>
                        <xsl:when
                            test='$type eq "text" or $type eq "image" or $type eq "sound" or $type eq "video" or $type eq "3d"'>
                            <img width="200px"
                                 src="/Portal-theme/images/ape/icons/dao_types/normal/{$type}.png"/>
                            <span>
                                <xsl:value-of select="$dao.title"/>
                            </span>
                        </xsl:when>
                        <xsl:otherwise>
                            <img width="200px"
                                 src="/Portal-theme/images/ape/icons/dao_types/normal/unspecified.png"/>
                            <span>
                                <xsl:value-of select="$dao.title"/>
                            </span>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <img width="200px"
                         src="/Portal-theme/images/ape/icons/dao_types/normal/unspecified.png"/>
                    <span>
                        <xsl:value-of select="$dao.title"/>
                    </span>
                </xsl:otherwise>
            </xsl:choose>
        </a>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="ead:dao" mode="thumbnail">
        <xsl:variable name="href" select="./@xlink:href"/>
        <img width="200px" src="{$href}"/>
    </xsl:template>
    <xsl:template name="string-replace-all">
        <xsl:param name="text" />
        <xsl:param name="replace" />
        <xsl:param name="by" />
        <xsl:choose>
            <xsl:when test="$text = '' or $replace = ''or not($replace)" >
                <!-- Prevent this routine from hanging -->
                <xsl:value-of select="$text" />
            </xsl:when>
            <xsl:when test="contains($text, $replace)">
                <xsl:value-of select="substring-before($text,$replace)" />
                <xsl:value-of select="$by" />
                <xsl:call-template name="string-replace-all">
                    <xsl:with-param name="text" select="substring-after($text,$replace)" />
                    <xsl:with-param name="replace" select="$replace" />
                    <xsl:with-param name="by" select="$by" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="ead:extref" mode="otherfindingaids">
        <div class="otherfindingaids">
            <xsl:variable name="internalHref" select="./@xlink:href"/>
            <xsl:variable name="href">
                <xsl:choose>
                    <xsl:when test="contains(./@xlink:href, 'ead3')">
                        <xsl:call-template name="string-replace-all">
                            <xsl:with-param name="text" select="$internalHref" />
                            <xsl:with-param name="replace" select="'ead3:'" />
                            <xsl:with-param name="by" select="''" />
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="./@xlink:href" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:variable name="prefix">
                <xsl:choose>
                    <xsl:when test="contains(./@xlink:href, 'ead3') and contains($eadcontent.extref.prefix, '/fa/')">
                        <xsl:call-template name="string-replace-all">
                            <xsl:with-param name="text" select="$eadcontent.extref.prefix" />
                            <xsl:with-param name="replace" select="'/fa/'" />
                            <xsl:with-param name="by" select="'/ead3/'" />
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$eadcontent.extref.prefix" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="starts-with($href, 'http')">
                    <div class="externalLink">
                        <xsl:choose>
                            <xsl:when test="./@xlink:title and text()">
                                <xsl:variable name="title" select="./@xlink:title"/>
                                <xsl:variable name="initTitle"
                                              select="ape:otherfindingaid('link', 'external')"/>
                                <a href="{$href}" target="_blank" title="{$initTitle} '{$title}'">
                                    <xsl:value-of select="ape:otherfindingaid('link', 'external')"/>
                                    <xsl:text> '</xsl:text>
                                    <xsl:value-of select="text()"/>
                                    <xsl:text>' </xsl:text>
                                </a>
                            </xsl:when>
                            <xsl:when test="./@xlink:title">
                                <a href="{$href}" target="_blank">
                                    <xsl:value-of select="ape:otherfindingaid('link', 'external')"/>
                                    <xsl:text> '</xsl:text>
                                    <xsl:value-of select="./@xlink:title"/>
                                    <xsl:text>' </xsl:text>
                                </a>
                            </xsl:when>
                            <xsl:when test="text()">
                                <a href="{$href}" target="_blank">
                                    <xsl:value-of select="ape:otherfindingaid('link', 'external')"/>
                                    <xsl:text> '</xsl:text>
                                    <xsl:value-of select="text()"/>
                                    <xsl:text>' </xsl:text>
                                </a>
                            </xsl:when>
                            <xsl:otherwise>
                                <a href="{$href}" target="_blank">
                                    <xsl:value-of select="ape:otherfindingaid('link', 'external')"/>
                                </a>
                            </xsl:otherwise>
                        </xsl:choose>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="ape:linked($internalHref) = 'indexed'">
                            <xsl:variable name="encodedHref"
                                          select="ape:encodeSpecialCharacters($href)"/>
                            <div class="linkButton">
                                <xsl:choose>
                                    <xsl:when test="./@xlink:title and text()">
                                        <xsl:variable name="title" select="./@xlink:title"/>
                                        <xsl:variable name="initTitle"
                                                      select="ape:otherfindingaid('link', 'internal')"/>
                                        <a href="{$prefix}{$encodedHref}" target="{$encodedHref}"
                                           title="{$initTitle} '{$title}'">
                                            <xsl:value-of
                                                select="ape:resource('seconddisplay.view.fa.internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:when test="./@xlink:title">
                                        <a href="{$prefix}{$encodedHref}" target="{$encodedHref}">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="./@xlink:title"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:when test="text()">
                                        <a href="{$prefix}{$encodedHref}" target="{$encodedHref}">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <a href="{$prefix}{$encodedHref}" target="{$encodedHref}">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                        </a>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </xsl:when>
                        <xsl:when test="ape:linked($href) = 'indexed-preview'">
                            <xsl:variable name="extref.warning"
                                          select="ape:resource('error.user.second.display.indexed.preview')"/>
                            <div class="linkButton">
                                <xsl:choose>
                                    <xsl:when test="./@xlink:title and text()">
                                        <xsl:variable name="title" select="./@xlink:title"/>
                                        <xsl:variable name="initTitle"
                                                      select="ape:otherfindingaid('link', 'internal')"/>
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')"
                                           title="{$initTitle} '{$title}'">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:when test="./@xlink:title">
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="./@xlink:title"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:when test="text()">
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                        </a>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </xsl:when>
                        <xsl:when test="ape:linked($href) = 'notindexed'">
                            <xsl:variable name="extref.warning"
                                          select="ape:resource('error.user.second.display.notindexed')"/>
                            <div class="linkButton">
                                <xsl:choose>
                                    <xsl:when test="./@xlink:title and text()">
                                        <xsl:variable name="title" select="./@xlink:title"/>
                                        <xsl:variable name="initTitle"
                                                      select="ape:otherfindingaid('link', 'internal')"/>
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')"
                                           class="notIndexed" title="{$initTitle} '{$title}'">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:when test="./@xlink:title">
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')"
                                           class="notIndexed">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="./@xlink:title"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:when test="text()">
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')"
                                           class="notIndexed">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </a>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <a href="javascript:void(0)"
                                           onclick="window.alert('{$extref.warning}')"
                                           class="notIndexed">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                        </a>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </xsl:when>
                        <xsl:otherwise>
                            <div class="linkButton">
                                <xsl:choose>
                                    <xsl:when test="./@xlink:title and text()">
                                        <xsl:variable name="title" select="./@xlink:title"/>
                                        <xsl:variable name="initTitle"
                                                      select="ape:resource('seconddisplay.view.fa.internal')"/>
                                        <div class="nolink">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </div>
                                    </xsl:when>
                                    <xsl:when test="./@xlink:title">
                                        <div class="nolink">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="./@xlink:title"/>
                                            <xsl:text>' </xsl:text>
                                        </div>
                                    </xsl:when>
                                    <xsl:when test="text()">
                                        <div class="nolink">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                            <xsl:text> '</xsl:text>
                                            <xsl:value-of select="text()"/>
                                            <xsl:text>' </xsl:text>
                                        </div>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <div class="nolink">
                                            <xsl:value-of
                                                select="ape:otherfindingaid('link', 'internal')"/>
                                        </div>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text> </xsl:text>
        </div>
    </xsl:template>

    <xsl:template match="ead:extref" mode="other notsearchable scopecontent">
        <xsl:variable name="href" select="./@xlink:href"/>
        <xsl:variable name="prefix" select="$eadcontent.extref.prefix"/>
        <xsl:choose>
            <xsl:when test="starts-with($href, 'http')">
                <xsl:choose>
                    <xsl:when test="./@xlink:title and text()">
                        <xsl:variable name="title" select="./@xlink:title"/>
                        <a href="{$href}" target="_blank" title="{$title}">
                            <xsl:value-of select="text()"/>
                        </a>
                    </xsl:when>
                    <xsl:when test="./@xlink:title">
                        <a href="{$href}" target="_blank">
                            <xsl:value-of select="./@xlink:title"/>
                        </a>
                    </xsl:when>
                    <xsl:when test="text()">
                        <a href="{$href}" target="_blank">
                            <xsl:value-of select="text()"/>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <a href="{$href}" target="_blank">
                            <xsl:variable name="extref.notitle"
                                          select="ape:resource('eadcontent.extref.notitle')"/>
                            <span class="icon_notitle" title="{$extref.notitle}">
                                <xsl:value-of select="$extref.notitle"/>
                            </span>
                        </a>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="ape:linked($href) = 'indexed'">
                        <xsl:choose>
                            <xsl:when test="./@xlink:title and text()">
                                <xsl:variable name="title" select="./@xlink:title"/>
                                <a href="{$prefix}&amp;eadid={$href}" target="{$href}"
                                   title="{$title}">
                                    <xsl:value-of select="text()"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="./@xlink:title">
                                <a href="{$prefix}&amp;eadid={$href}" target="{$href}">
                                    <xsl:value-of select="./@xlink:title"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="text()">
                                <a href="{$prefix}&amp;eadid={$href}" target="{$href}">
                                    <xsl:value-of select="text()"/>
                                </a>
                            </xsl:when>
                            <xsl:otherwise>
                                <a href="{$prefix}&amp;eadid={$href}" target="{$href}">
                                    <xsl:variable name="extref.notitle"
                                                  select="ape:resource('eadcontent.extref.notitle')"/>
                                    <span class="icon_notitle" title="{$extref.notitle}">
                                        <xsl:value-of select="$extref.notitle"/>
                                    </span>
                                </a>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="ape:linked($href) = 'notindexed'">
                        <xsl:variable name="extref.warning"
                                      select="ape:resource('error.user.second.display.notindexed')"/>
                        <xsl:choose>
                            <xsl:when test="./@xlink:title and text()">
                                <xsl:variable name="title" select="./@xlink:title"/>
                                <a href="javascript:void(0)"
                                   onclick="window.alert('{$extref.warning}')" title="{$title}">
                                    <xsl:value-of select="text()"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="./@xlink:title">
                                <a href="javascript:void(0)"
                                   onclick="window.alert('{$extref.warning}')">
                                    <xsl:value-of select="./@xlink:title"/>
                                </a>
                            </xsl:when>
                            <xsl:when test="text()">
                                <a href="javascript:void(0)"
                                   onclick="window.alert('{$extref.warning}')">
                                    <xsl:value-of select="text()"/>
                                </a>
                            </xsl:when>
                            <xsl:otherwise>
                                <a href="javascript:void(0)"
                                   onclick="window.alert('{$extref.warning}')">
                                    <xsl:variable name="extref.notitle"
                                                  select="ape:resource('eadcontent.extref.notitle')"/>
                                    <span class="icon_notitle" title="{$extref.notitle}">
                                        <xsl:value-of select="$extref.notitle"/>
                                    </span>
                                </a>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="ape:linked($href) = 'notavailable'">
                        <xsl:choose>
                            <xsl:when test="./@xlink:title and text()">
                                <xsl:value-of select="text()"/>
                            </xsl:when>
                            <xsl:when test="./@xlink:title">
                                <xsl:value-of select="./@xlink:title"/>
                            </xsl:when>
                            <xsl:when test="text()">
                                <xsl:value-of select="text()"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$href != ''">
                                    <xsl:variable name="extref.notitle" select="ape:resource('eadcontent.extref.notitle')"/>
                                    <span class="icon_notitle" title="{$extref.notitle}">
                                        <xsl:value-of select="$extref.notitle"/>
                                    </span>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="text()">
                            <xsl:value-of select="text()"/>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="ead:table" mode="#all">
        <table>
            <xsl:if test="./ead:head">
                <caption>
                    <i>
                        <xsl:apply-templates select="./ead:head/child::node()" mode="#current"/>
                    </i>
                </caption>
            </xsl:if>
            <xsl:apply-templates mode="#current"/>
        </table>
    </xsl:template>
    <xsl:template match="ead:tgroup" mode="#all">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="ead:thead" mode="#all">
        <thead>
            <xsl:apply-templates mode="#current">
                <xsl:with-param name="type" select="'head'"/>
            </xsl:apply-templates>
        </thead>
    </xsl:template>
    <xsl:template match="ead:tbody" mode="#all">
        <tbody>
            <xsl:apply-templates mode="#current">
                <xsl:with-param name="type" select="'body'"/>
            </xsl:apply-templates>
        </tbody>
    </xsl:template>
    <xsl:template match="ead:row" mode="#all">
        <xsl:param name="type"/>
        <tr>
            <xsl:apply-templates mode="#current">
                <xsl:with-param name="type" select="$type"/>
            </xsl:apply-templates>
        </tr>
    </xsl:template>
    <xsl:template match="ead:entry" mode="#all">
        <xsl:param name="type"/>
        <xsl:choose>
            <xsl:when test="$type = 'head'">
                <th class="thEntry">
                    <xsl:apply-templates mode="#current"/>
                </th>
            </xsl:when>
            <xsl:otherwise>
                <td class="tdEntry">
                    <xsl:apply-templates mode="#current"/>
                </td>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>
    <xsl:template match="ead:corpname" mode="#all">
        <xsl:choose>
            <xsl:when test="@authfilenumber">
                <xsl:choose>
                    <xsl:when test="ape:linkedEacCpf(@authfilenumber) = 'indexed'">
                        <xsl:call-template name="createEacLinkInternal">
                            <xsl:with-param name="element" select="."/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="starts-with(@authfilenumber, 'http')">
                        <xsl:call-template name="createEacLinkExternal">
                            <xsl:with-param name="element" select="."/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates mode="#current"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates mode="#current"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="ead:persname" mode="#all">
        <xsl:choose>
            <xsl:when test="./@authfilenumber">
                <xsl:choose>
                    <xsl:when test="ape:linkedEacCpf(./@authfilenumber) = 'indexed'">
                        <xsl:call-template name="createEacLinkInternal">
                            <xsl:with-param name="element" select="."/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="starts-with(./@authfilenumber, 'http')">
                        <xsl:call-template name="createEacLinkExternal">
                            <xsl:with-param name="element" select="."/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates mode="#current"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates mode="#current"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="ead:famname" mode="#all">
        <xsl:choose>
            <xsl:when test="./@authfilenumber">
                <xsl:choose>
                    <xsl:when test="ape:linkedEacCpf(./@authfilenumber) = 'indexed'">
                        <xsl:call-template name="createEacLinkInternal">
                            <xsl:with-param name="element" select="."/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="starts-with(./@authfilenumber, 'http')">
                        <xsl:call-template name="createEacLinkExternal">
                            <xsl:with-param name="element" select="."/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates mode="#current"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates mode="#current"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="ead:name" mode="#all">
        <br/>
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="ead:geogname" mode="#all">
        <br/>
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="ead:function" mode="#all">
        <br/>
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="ead:title" mode="#all">
        <br/>
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="ead:subject" mode="#all">
        <br/>
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="ead:occupation" mode="#all">
        <br/>
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="ead:address" mode="#all">
        <div class="defaultlayout">
            <xsl:text> (</xsl:text>
            <xsl:variable name="addressCount" select="count(ead:addressline)"/>
            <xsl:for-each select="ead:addressline">
                <xsl:value-of select="."/>
                <xsl:if test="$addressCount > position()">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
            <xsl:text>)</xsl:text>
        </div>
    </xsl:template>
    <xsl:template name="langmaterial">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.language')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:langmaterial">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="repository">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.repository')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:repository">
                <p>
                    <xsl:apply-templates mode="other"/>
                    <xsl:text> </xsl:text>
                </p>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="physloc">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.physloc')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:physloc">
                <xsl:if test="./text()">
                    <xsl:if test="./@label">
                        <xsl:value-of select="./@label"/>
                        <xsl:text>: </xsl:text>
                    </xsl:if>
                    <xsl:apply-templates mode="otherwithoutwhitespace"/>
                    <xsl:if test="position() != last() and not(./text()[position() + 1 = last()])">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>

    <xsl:template name="container">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.container')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:container">
                <xsl:if test="./text()">
                    <xsl:if test="./@type">
                        <xsl:value-of select="./@type"/>
                        <xsl:text>: </xsl:text>
                    </xsl:if>
                    <xsl:apply-templates mode="otherwithoutwhitespace"/>
                    <xsl:if test="position() != last() and not(./text()[position() + 1 = last()])">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>

    <xsl:template name="materialspec">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.materialspec')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:materialspec">
                <xsl:apply-templates mode="otherwithoutwhitespace"/>
                <xsl:if test="position() != last()">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="physfacet">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.physfacet')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:physfacet">
                <xsl:apply-templates mode="otherwithoutwhitespace"/>
                <xsl:if test="position() != last()">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>
    <!-- Extent option -->
    <xsl:template name="extent">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.extent')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:extent">
                <xsl:apply-templates mode="otherwithoutwhitespace"/>
                <xsl:if test="./@unit">
                    <!--  <xsl:text> (</xsl:text>-->
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="./@unit"/>
                    <!--  <xsl:text>)</xsl:text>-->
                </xsl:if>
                <xsl:if test="position() != last()">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="genreform">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.genreform')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:genreform">
                <xsl:apply-templates mode="otherwithoutwhitespace"/>
                <xsl:if test="position() != last()">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="dimensions">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.dimensions')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:dimensions">
                <xsl:apply-templates mode="otherwithoutwhitespace"/>
                <xsl:if test="position() != last()">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="physdescText">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.physdesc')"/>
        </h2>
        <div class="ead-content">
            <xsl:apply-templates mode="otherwithcoma"/>
        </div>
    </xsl:template>
    <xsl:template name="note">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.note')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:note">
                <xsl:if test="./@label">
                    <h4>
                        <xsl:value-of select="./@label"/>
                    </h4>
                </xsl:if>
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="dao">
        <xsl:variable name="isPreview" select="ape:typeOfDisplay('preview')"/>
        <xsl:variable name="isChild" select="ape:typeOfDisplay('child')"/>
        <xsl:variable name="maxNumber">
            <xsl:choose>
                <xsl:when test="$isPreview">
                    <xsl:value-of select="number('2')"/>
                </xsl:when>
                <xsl:when test="$isChild">
                    <xsl:value-of select="number('2')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="number('0')"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="maxDisplayNumber">
            <xsl:choose>
                <xsl:when test="$isChild">
                    <xsl:value-of select="number('1')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="number('0')"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="thumbnailHref"
                      select="'/Portal-theme/images/ape/icons/dao_types/unspecified_big.gif'"/>
        <xsl:variable name="thumbnail" select='ead:dao[@xlink:title = "thumbnail"]'/>
        <xsl:variable name="numberOfThumbnails" select="count($thumbnail)"/>
        <xsl:variable name="allDaos" select='ead:dao[(@xlink:title != "thumbnail" and @xlink:title != "manifest" and @xlink:title != "service") or not(@xlink:title)]'/>
        <xsl:variable name="numberOfAllDaos" select="count($allDaos)"/>
        <xsl:if test="$numberOfAllDaos &gt; 0">
            <h2 class="dao-list">
                <xsl:value-of select="ape:resource('eadcontent.dao')"/>
            </h2>
            <div class="ead-content">
                <div class="daolistContainer">
                    <div class="daolist"> </div>
                    <div class="daolist-orig">
                        <xsl:for-each select='ead:dao[(@xlink:title != "thumbnail" and @xlink:title != "manifest" and @xlink:title != "service") or not(@xlink:title)]'>
                            <xsl:variable name="linkPosition" select="position()"/>
                            <xsl:variable name="href" select="./@xlink:href"/>
                            <xsl:if
                                test="number($maxNumber) eq 0 or $linkPosition &lt;= number($maxNumber)">
                                <xsl:variable name="dao.title">
                                    <xsl:choose>
                                        <xsl:when test="./@xlink:title">
                                            <xsl:value-of select="./@xlink:title"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of
                                                select="ape:resource('eadcontent.dao.notitle')"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                <!-- if thumbnail exists -->
                                <xsl:variable name="thumbnailHref">
                                    <xsl:choose>
                                        <xsl:when test="$thumbnail">
                                            <xsl:choose>
                                                <xsl:when test="$numberOfThumbnails >= $linkPosition">
                                                    <xsl:value-of
                                                        select="$thumbnail[$linkPosition]/@xlink:href"/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of select="$thumbnail[1]/@xlink:href"/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:choose>
                                                <xsl:when test="./@xlink:role">
                                                    <xsl:variable name="type"
                                                                  select="fn:lower-case(./@xlink:role)"/>
                                                    <xsl:choose>
                                                        <xsl:when test="./@xlink:role">
                                                            <xsl:variable name="type"
                                                                          select="fn:lower-case(./@xlink:role)"/>
                                                            <xsl:choose>
                                                                <xsl:when
                                                                    test='$type eq "text" or $type eq "image" or $type eq "sound" or $type eq "video" or $type eq "3d"'>
                                                                    <xsl:value-of
                                                                        select="concat('/Portal-theme/images/ape/icons/dao_types/normal/', $type, '.png')"
                                                                    />
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                    <xsl:value-of
                                                                        select="'/Portal-theme/images/ape/icons/dao_types/normal/unspecified.png'"
                                                                    />
                                                                </xsl:otherwise>
                                                            </xsl:choose>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <xsl:value-of
                                                                select="'/Portal-theme/images/ape/icons/dao_types/normal/unspecified.png'"
                                                            />
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of
                                                        select="'/Portal-theme/images/ape/icons/dao_types/normal/unspecified.png'"
                                                    />
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                <xsl:choose>
                                    <xsl:when
                                        test="number($maxDisplayNumber) eq 0 or $linkPosition &lt;= number($maxDisplayNumber)">
                                        <div class="dao">
                                            <a href="{$href}" target="_blank">
                                                <xsl:choose>
                                                    <xsl:when test="$linkPosition &lt;= 5">
                                                        <img src="{$thumbnailHref}" alt="{$dao.title}"
                                                             title="{$dao.title}"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <img data-src="{$thumbnailHref}"
                                                             alt="{$dao.title}" title="{$dao.title}"/>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                <span>
                                                    <xsl:value-of select="$dao.title"/>
                                                </span>
                                            </a>
                                        </div>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <div class="dao">
                                            <span>
                                                <xsl:value-of
                                                    select="ape:resource('eadcontent.dao.more.lowerlevel')"
                                                />
                                            </span>
                                        </div>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:if>

                        </xsl:for-each>
                    </div>
                    <xsl:if test="not($isPreview) and not($isChild)">
                        <div class="linkButton" id="moreDaosButton">
                            <a href="javascript:void(0)">
                                <xsl:value-of select="ape:resource('eadcontent.dao.more')"/>
                                (<span/>)</a>
                        </div>
                    </xsl:if>
                </div>
            </div>
        </xsl:if>
    </xsl:template>
    <xsl:template name="origination">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.origination')"/>
        </h2>
        <div class="ead-content">
            <xsl:if test="ead:origination[@label = 'pre']">
                <br>
                    <b>
                        <xsl:value-of select="ape:resource('eadcontent.origination.pre')"/>:</b>
                </br>
                <xsl:for-each select="ead:origination[@label = 'pre']">
                    <p class="pOrigination">
                        <xsl:for-each select="child::node()">
                            <xsl:value-of select="fn:normalize-space(ape:highlight(., 'other'))"
                                          disable-output-escaping="yes"/>
                            <xsl:text> </xsl:text>
                        </xsl:for-each>
                    </p>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="ead:origination[@label = 'final']">
                <br>
                    <b>
                        <xsl:value-of select="ape:resource('eadcontent.origination.final')"/>:</b>
                </br>
                <xsl:for-each select="ead:origination[@label = 'final']">
                    <p class="pOrigination">
                        <xsl:for-each select="child::node()">
                            <xsl:value-of select="fn:normalize-space(ape:highlight(., 'other'))"
                                          disable-output-escaping="yes"/>
                            <xsl:text> </xsl:text>
                        </xsl:for-each>
                    </p>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="ead:origination[@label = 'organisational unit']">
                <br>
                    <b>
                        <xsl:value-of select="ape:resource('eadcontent.origination.orgunit')"/>:</b>
                </br>
                <xsl:for-each select="ead:origination[@label = 'organisational unit']">
                    <p class="pOrigination">
                        <xsl:for-each select="child::node()">
                            <xsl:value-of select="fn:normalize-space(ape:highlight(., 'other'))"
                                          disable-output-escaping="yes"/>
                            <xsl:text> </xsl:text>
                        </xsl:for-each>
                    </p>
                </xsl:for-each>
            </xsl:if>
            <xsl:if
                test="ead:origination[@label != 'pre'] and ead:origination[@label != 'final'] and ead:origination[@label != 'organisational unit']">
                <xsl:for-each select="ead:origination">
                    <xsl:if
                        test="./@label != 'pre' and ./@label != 'final' and ./@label != 'organisational unit'">
                        <br>
                            <b>
                                <xsl:value-of select="./@label"/>:</b>
                        </br>
                        <p class="pOrigination">
                            <xsl:for-each select="child::node()">
                                <xsl:value-of select="fn:normalize-space(ape:highlight(., 'other'))"
                                              disable-output-escaping="yes"/>
                                <xsl:text> </xsl:text>
                            </xsl:for-each>
                        </p>
                    </xsl:if>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="ead:origination[not(@label)]">
                <xsl:if test="ead:origination[@label]">
                    <br/>
                </xsl:if>
                <xsl:for-each select="ead:origination[not(@label)]">
                    <p>
                        <xsl:for-each select="child::node()">
                            <xsl:choose>
                                <xsl:when test="name() = 'corpname' or 'famname' or 'persname'">
                                    <xsl:apply-templates select="."/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of
                                        select="fn:normalize-space(ape:highlight(., 'other'))"
                                        disable-output-escaping="yes"/>
                                    <xsl:text> </xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </p>
                </xsl:for-each>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template name="scopecontent">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.scopecontent')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:scopecontent">
                <xsl:apply-templates mode="scopecontent"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="bioghist">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.bioghist')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:bioghist">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="custodhist">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.custodhist')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:custodhist">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="appraisal">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.appraisal')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:appraisal">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="processinfo">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.processinfo')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:processinfo">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="accruals">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.accruals')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:accruals">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="arrangement">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.arrangement')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:arrangement">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="fileplan">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.fileplan')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:fileplan">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="accessrestrict">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.accessrestrict')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:accessrestrict">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="userestrict">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.userestrict')"/>
        </h2>
        <div class="ead-content">
            <xsl:if test="ead:userestrict[@type = 'dao']">
                <br>
                    <b>
                        <xsl:value-of select="ape:resource('eadcontent.userestrict.dao')"/>:</b>
                </br>
                <xsl:for-each select="ead:userestrict[@type = 'dao']">
                    <xsl:apply-templates mode="other"/>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="ead:userestrict[@type = 'ead']">
                <br>
                    <b>
                        <xsl:value-of select="ape:resource('eadcontent.userestrict.ead')"/>:</b>
                </br>
                <xsl:for-each select="ead:userestrict[@type = 'ead']">
                    <xsl:apply-templates mode="other"/>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="ead:userestrict[(@type != 'ead' and @type != 'dao') or not(@type)]">
                <xsl:if test="ead:userestrict[@type = 'ead' or @type = 'dao']">
                    <br/>
                </xsl:if>
                <xsl:for-each
                    select="ead:userestrict[(@type != 'ead' and @type != 'dao') or not(@type)]">
                    <xsl:apply-templates mode="other"/>
                </xsl:for-each>
            </xsl:if>
        </div>
    </xsl:template>
    <xsl:template name="phystech">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.phystech')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:phystech">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="otherfindaid">
        <h2>
            <xsl:value-of select="ape:otherfindingaid('title')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:otherfindaid">
                <xsl:apply-templates mode="otherfindingaids"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="relatedmaterial">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.relatedmaterial')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:relatedmaterial">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="separatedmaterial">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.separatedmaterial')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:separatedmaterial">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="altformavail">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.altformavail')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:altformavail">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="originalsloc">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.originalsloc')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:originalsloc">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="bibliography">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.bibliography')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:bibliography">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="prefercite">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.prefercite')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:prefercite">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="odd">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.odd')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:odd">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="acqinfo">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.acqinfo')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:acqinfo">
                <xsl:apply-templates mode="other"/>
            </xsl:for-each>
        </div>
    </xsl:template>
    <xsl:template name="controlaccess">
        <h2>
            <xsl:value-of select="ape:resource('eadcontent.controlaccess')"/>
        </h2>
        <div class="ead-content">
            <xsl:for-each select="ead:controlaccess">
                <xsl:if test="ead:head | ead:p">
                    <xsl:apply-templates select="ead:head | ead:p" mode="other"/>
                </xsl:if>
                <xsl:if test="ead:subject">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.subject')"
                            />:</b>
                        <xsl:apply-templates select="ead:subject" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:geogname">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.geogname')"
                            />:</b>
                        <xsl:apply-templates select="ead:geogname" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:persname">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.persname')"
                            />:</b>
                        <xsl:apply-templates select="ead:persname" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:famname">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.famname')"
                            />:</b>
                        <xsl:apply-templates select="ead:famname" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:corpname">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.corpname')"
                            />: </b>
                        <xsl:apply-templates select="ead:corpname" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:name">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.name')"
                            />:</b>
                        <xsl:apply-templates select="ead:name" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:occupation">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of
                                select="ape:resource('eadcontent.controlaccess.occupation')"/>:</b>
                        <xsl:apply-templates select="ead:occupation" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:function">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.function')"
                            />:</b>
                        <xsl:apply-templates select="ead:function" mode="other"/>
                    </div>
                </xsl:if>
                <xsl:if test="ead:genreform">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of
                                select="ape:resource('eadcontent.controlaccess.genreform')"/>:</b>
                        <div>
                            <xsl:for-each select="ead:genreform">
                                <br>
                                    <xsl:value-of select="ape:highlight(., 'other')"
                                                  disable-output-escaping="yes"/>
                                </br>
                            </xsl:for-each>
                        </div>
                    </div>
                </xsl:if>
                <xsl:if test="ead:title">
                    <div class="pControlacces">
                        <b>
                            <xsl:value-of select="ape:resource('eadcontent.controlaccess.title')"
                            />:</b>
                        <xsl:apply-templates select="ead:title" mode="other"/>
                    </div>
                </xsl:if>
            </xsl:for-each>
        </div>
    </xsl:template>
    <!-- 	<xsl:template name="container">
            <h2>
                    <xsl:value-of select="ape:resource('eadcontent.container')" />
            </h2>
            <div class="ead-content">
            <xsl:for-each select="ead:container">
                    <xsl:apply-templates mode="otherwithcoma"/>
            </xsl:for-each> 
            </div>
    </xsl:template> -->

    <!-- Extent option -->
    <xsl:template match="ead:extent">
        <xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes"/>
        <xsl:if test="./@unit">
            <!--<xsl:text> (</xsl:text>-->
            <xsl:text> </xsl:text>
            <xsl:value-of select="./@unit"/>
            <!--  <xsl:text>)</xsl:text>-->
        </xsl:if>
    </xsl:template>

    <xsl:template match="ead:dimensions">
        <xsl:value-of select="ape:highlight(., 'other')" disable-output-escaping="yes"/>
        <xsl:if test="./@unit">
            <!-- <xsl:text> (</xsl:text>-->
            <xsl:text> </xsl:text>
            <xsl:value-of select="./@unit"/>
            <!--<xsl:text>)</xsl:text>-->
        </xsl:if>
    </xsl:template>

    <xsl:template match="ead:physfacet">
        <xsl:apply-templates mode="other"/>
    </xsl:template>

    <xsl:template match="ead:genreform">
        <xsl:apply-templates mode="other"/>
    </xsl:template>

    <xsl:template match="ead:bibref" mode="other">
        <xsl:choose>
            <xsl:when test="ead:imprint">
                <p>
                    <xsl:for-each select="ead:imprint">
                        <p>
                            <xsl:variable name="nameCount" select="count(./parent::node()/ead:name)"/>
                            <xsl:for-each select="./parent::node()/ead:name">
                                <xsl:value-of select="ape:highlight(., 'other')"
                                              disable-output-escaping="yes"/>
                                <xsl:if test="$nameCount > position()">
                                    <xsl:text>, </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text>: </xsl:text>
                            <xsl:variable name="titleCount"
                                          select="count(./parent::node()/ead:title)"/>
                            <xsl:for-each select="./parent::node()/ead:title">
                                <i>
                                    <xsl:value-of select="ape:highlight(., 'other')"
                                                  disable-output-escaping="yes"/>
                                </i>
                                <xsl:if test="$titleCount > position()">
                                    <xsl:text>, </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text>. </xsl:text>
                            <xsl:variable name="geognameCount" select="count(ead:geogname)"/>
                            <xsl:for-each select="ead:geogname">
                                <xsl:value-of select="."/>
                                <xsl:if test="$geognameCount > position()">
                                    <xsl:text>, </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text>: </xsl:text>
                            <xsl:variable name="publisherCount" select="count(ead:publisher)"/>
                            <xsl:for-each select="ead:publisher">
                                <xsl:value-of select="ape:highlight(., 'other')"
                                              disable-output-escaping="yes"/>
                                <xsl:if test="$publisherCount > position()">
                                    <xsl:text>, </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text>, </xsl:text>
                            <xsl:variable name="dateCount" select="count(ead:date)"/>
                            <xsl:for-each select="ead:date">
                                <xsl:value-of select="ape:highlight(., 'other')"
                                              disable-output-escaping="yes"/>
                                <xsl:if test="$dateCount > position()">
                                    <xsl:text>, </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:text>. (</xsl:text>
                            <xsl:value-of select="ape:resource('eadcontent.online')"/>
                            <xsl:text>: </xsl:text>
                            <xsl:variable name="href" select="./parent::node()/@xlink:href"/>
                            <xsl:choose>
                                <xsl:when test="./parent::node()/@xlink:title">
                                    <a href="{$href}" target="_blank">
                                        <xsl:value-of select="./parent::node()/@xlink:title"/>
                                    </a>
                                </xsl:when>
                                <xsl:otherwise>
                                    <a href="{$href}" target="_blank">
                                        <xsl:value-of select="$href"/>
                                    </a>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:text>).</xsl:text>
                        </p>
                    </xsl:for-each>
                </p>
            </xsl:when>
            <!-- ead:imprint -->
            <xsl:otherwise>
                <p>
                    <xsl:value-of select="."/>
                    <xsl:text>.</xsl:text>
                    <xsl:if test="./@xlink:href != ''">
                        <xsl:text> (</xsl:text>
                        <xsl:value-of select="ape:resource('eadcontent.online')"/>
                        <xsl:text>: </xsl:text>
                        <xsl:variable name="href" select="./@xlink:href"/>
                        <xsl:choose>
                            <xsl:when test="./@xlink:title">
                                <a href="{$href}" target="_blank">
                                    <xsl:value-of select="./@xlink:title"/>
                                </a>
                            </xsl:when>
                            <xsl:otherwise>
                                <a href="{$href}" target="_blank">
                                    <xsl:value-of select="$href"/>
                                </a>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:text>).</xsl:text>
                    </xsl:if>
                </p>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="createEacLinkInternal">
        <xsl:param name="element"/>
        <xsl:variable name="aiCode" select="ape:aiFromEac($element/@authfilenumber, '')"/>
        <xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)"/>
        <xsl:variable name="encodedRecordid"
                      select="ape:encodeSpecialCharacters($element/@authfilenumber)"/>
        <a href="{$eacUrl}/aicode/{$encodedAiCode}/type/ec/id/{$encodedRecordid}" target="_blank">
            <xsl:value-of select="$element/text()"/>
        </a>
        <br/>
    </xsl:template>
    <xsl:template name="createEacLinkExternal">
        <xsl:param name="element"/>
        <xsl:variable name="link" select="$element/@authfilenumber"/>
        <a href="{$link}" target="_blank">
            <xsl:value-of select="$element/text()"/>
        </a>
        <br/>
    </xsl:template>
</xsl:stylesheet>
