<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="http://ead3.archivists.org/schema/" xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
                exclude-result-prefixes="xlink xlink xsi ead ape fn #default">
    <xsl:param name="eadcontent.extref.prefix"/>
    <xsl:include href="common-ead3.xsl" />
    <xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
    <xsl:template match="/">
        <xsl:apply-templates select="./ead:c"/>
    </xsl:template>

    <xsl:template name="header">
        <xsl:for-each select="./ead:did/ead:unitid[@type='call number']">
            <xsl:if test="position() = 1">
                <div class="eadid">
                    <xsl:value-of select="ape:highlight(., 'unitid')" disable-output-escaping="yes" />			
                </div>
            </xsl:if>
        </xsl:for-each>
        <xsl:for-each select="/ead:c/ead:did/ead:unittitle">
            <h1 class="titleproper">
                <xsl:for-each select="text()">
                    <xsl:value-of select="normalize-space(.)"/>
                </xsl:for-each>
            </h1>
        </xsl:for-each>

        <div class="subtitle">
            <xsl:apply-templates  select="/ead:c/ead:did/ead:unitdate"  mode="alterdate"/>
        </div>

        <xsl:for-each select="/ead:c/ead:did/ead:unitid/ead:ref">
            <div class="defaultlayout">
                <a target="_blank">
                    <xsl:attribute name="href">
                        <xsl:value-of select="@href" />
                    </xsl:attribute>
                    <xsl:value-of select="ape:resource('eadcontent.frontpage.eadid.url')" />
                </a>
            </div>
        </xsl:for-each>
    </xsl:template>


    <xsl:template match="ead:c" >
        <xsl:call-template name="header" />
        <xsl:if test="ead:scopecontent">
            <xsl:call-template name="scopecontent" />
        </xsl:if>
        <xsl:apply-templates select="./ead:did" />
        <xsl:if test="ead:appraisal">
            <xsl:call-template name="appraisal" />
        </xsl:if>
        <xsl:if test="ead:processinfo">
            <xsl:call-template name="processinfo" />
        </xsl:if>
        <xsl:if test="ead:custodhist">
            <xsl:call-template name="custodhist" />
        </xsl:if>
        <xsl:if test="ead:bioghist">
            <xsl:call-template name="bioghist" />
        </xsl:if>
        <xsl:if test="ead:arrangement">
            <xsl:call-template name="arrangement" />
        </xsl:if>
        <xsl:if test="ead:fileplan">
            <xsl:call-template name="fileplan" />
        </xsl:if>
        <xsl:if test="ead:accruals">
            <xsl:call-template name="accruals" />
        </xsl:if>
        <xsl:if test="ead:acqinfo">
            <xsl:call-template name="acqinfo" />
        </xsl:if>
        <xsl:if test="ead:altformavail">
            <xsl:call-template name="altformavail" />
        </xsl:if>
        <xsl:if test="ead:relatedmaterial">
            <xsl:call-template name="relatedmaterial" />
        </xsl:if>
        <xsl:if test="ead:separatedmaterial">
            <xsl:call-template name="separatedmaterial" />
        </xsl:if>
        <xsl:if test="ead:originalsloc">
            <xsl:call-template name="originalsloc" />
        </xsl:if>
        <xsl:if test="ead:prefercite">
            <xsl:call-template name="prefercite" />
        </xsl:if>
        <xsl:if test="ead:otherfindaid">
            <xsl:call-template name="otherfindaid" />
        </xsl:if>
        <xsl:if test="ead:accessrestrict">
            <xsl:call-template name="accessrestrict" />
        </xsl:if>
        <xsl:if test="ead:userestrict">
            <xsl:call-template name="userestrict" />
        </xsl:if>
        <xsl:if test="ead:phystech">
            <xsl:call-template name="phystech" />
        </xsl:if>
        <xsl:if test="ead:bibliography">
            <xsl:call-template name="bibliography" />
        </xsl:if>
        <xsl:if test="ead:odd">
            <xsl:call-template name="odd" />
        </xsl:if>
        <xsl:if test="ead:controlaccess">
            <xsl:call-template name="controlaccess" />
        </xsl:if>
    </xsl:template>

    <xsl:template match="ead:did">
        <xsl:if test="ead:origination">
            <xsl:call-template name="origination" />
        </xsl:if>
        <xsl:if test="ead:langmaterial">
            <xsl:call-template name="langmaterial" />
        </xsl:if>
        <xsl:if test="ead:repository">
            <xsl:call-template name="repository" />
        </xsl:if>
        <xsl:if test="ead:container/text()">
            <xsl:call-template name="container" />
        </xsl:if>
        <xsl:if test="ead:physloc/text()"> 
            <xsl:call-template name="physloc" />
        </xsl:if>   
        <xsl:if test="ead:materialspec">
            <xsl:call-template name="materialspec" />
        </xsl:if>
        <xsl:for-each select="ead:physdesc">
            <xsl:choose>
                <xsl:when test="normalize-space(text()[1])">
                    <xsl:call-template name="physdescText" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="ead:physfacet">
                        <xsl:call-template name="physfacet" />
                    </xsl:if>
                    <xsl:if test="ead:extent">
                        <xsl:call-template name="extent" />
                    </xsl:if>
                    <xsl:if test="ead:genreform">
                        <xsl:call-template name="genreform" />
                    </xsl:if>
                    <xsl:if test="ead:dimensions">
                        <xsl:call-template name="dimensions" />
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
        <xsl:if test="ead:note">
            <xsl:call-template name="note" />
        </xsl:if>

        <xsl:if test="ead:dao">
            <xsl:call-template name="dao" />
        </xsl:if>

    </xsl:template>

</xsl:stylesheet>