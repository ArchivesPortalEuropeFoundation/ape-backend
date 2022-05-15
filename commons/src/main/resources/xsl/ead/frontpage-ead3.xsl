<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="http://ead3.archivists.org/schema/" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi ead ape">
	<xsl:param name="eadcontent.extref.prefix"/>
	<xsl:include href="common-ead3.xsl"/>
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<div id="body">
			<xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unitid">
				<div class="eadid">
					<xsl:value-of select="ape:highlight(., 'unitid')" disable-output-escaping="yes"
					/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:archdesc/ead:did/ead:unittitle">
				<h1 class="titleproper">
					<xsl:apply-templates mode="title"/>
				</h1>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:control/ead:filedesc/ead:titlestmt/ead:subtitle">
				<div class="subtitle">
					<xsl:apply-templates mode="searchable"/>
				</div>
			</xsl:for-each>
			<div class="subtitle">
				<xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did/ead:unitdate"
					mode="alterdate"/>
			</div>
			<xsl:for-each select="/ead:ead/ead:control/ead:filedesc/ead:titlestmt/ead:author">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:control/ead:filedesc/ead:publicationstmt/ead:date">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each
				select="/ead:ead/ead:control/ead:filedesc/ead:publicationstmt/ead:publisher">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:if test="/ead:ead/ead:control/ead:filedesc/ead:publicationstmt/ead:address">
				<xsl:apply-templates
					select="/ead:ead/ead:control/ead:filedesc/ead:publicationstmt/ead:address"
					mode="notsearchable"/>
			</xsl:if>
			<xsl:if test="/ead:ead/ead:control/ead:filedesc/ead:publicationstmt/ead:p">
				<xsl:for-each select="/ead:ead/ead:control/ead:filedesc/ead:publicationstmt/ead:p">
					<div class="defaultlayout">
						<xsl:apply-templates mode="notsearchable"/>
					</div>
				</xsl:for-each>
			</xsl:if>

			<xsl:for-each select="/ead:ead/ead:control/ead:filedesc/ead:seriesstmt/ead:titleproper">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<xsl:for-each select="/ead:ead/ead:control/ead:profiledesc/ead:langusage">
				<div class="defaultlayout">
					<xsl:apply-templates mode="notsearchable"/>
				</div>
			</xsl:for-each>
			<div class="defaultlayout">
				<xsl:if test="/ead:ead/ead:control/ead:eadid/@url">
					<xsl:variable name="seeInContext" select="/ead:ead/ead:control/ead:eadid/@url"/>
					<a href="{$seeInContext}" target="_blank">
						<xsl:value-of select="ape:resource('eadcontent.frontpage.eadid.url')"/>
					</a>
				</xsl:if>
			</div>
			<div id="content">
				<div id="expandableContent">
					<xsl:if test="/ead:ead/ead:archdesc/ead:did/ead:head">
						<div class="archdescDidHead">
							<xsl:value-of select="/ead:ead/ead:archdesc/ead:did/ead:head"/>
						</div>
					</xsl:if>
					<xsl:apply-templates select="/ead:ead/ead:archdesc" mode="searchable"/>
					<xsl:for-each select="/ead:ead/ead:archdesc/ead:did">

						<xsl:if test="ead:langmaterial">
							<xsl:call-template name="langmaterial"/>
						</xsl:if>
						<xsl:if test="ead:origination">
							<xsl:call-template name="origination"/>
						</xsl:if>

						<xsl:if test="ead:repository">
							<xsl:call-template name="repository"/>
						</xsl:if>
						<xsl:if test="ead:container/text()">
							<xsl:call-template name="container"/>
						</xsl:if>
						<xsl:if test="ead:physloc/text()">
							<xsl:call-template name="physloc"/>
						</xsl:if>
						<xsl:if test="ead:materialspec">
							<xsl:call-template name="materialspec"/>
						</xsl:if>
						<xsl:if test="ead:physdesc">
							<xsl:if test="ead:physfacet">
								<xsl:call-template name="physfacet"/>
							</xsl:if>
							<xsl:if test="ead:extent">
								<xsl:call-template name="extent"/>
							</xsl:if>
							<xsl:if test="ead:genreform">
								<xsl:call-template name="genreform"/>
							</xsl:if>
							<xsl:if test="ead:dimensions">
								<xsl:call-template name="dimensions"/>
							</xsl:if>
						</xsl:if>
						<xsl:if test="ead:dao">
							<xsl:call-template name="dao"/>
						</xsl:if>
					</xsl:for-each>
				</div>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="ead:archdesc" mode="#all">
		<xsl:if test="ead:scopecontent">
			<xsl:call-template name="scopecontent" />
		</xsl:if>
		<xsl:if test="ead:bioghist">
			<xsl:call-template name="bioghist" />			
		</xsl:if>
		<xsl:if test="ead:custodhist">
			<xsl:call-template name="custodhist" />
		</xsl:if>
		<xsl:if test="ead:appraisal">
			<xsl:call-template name="appraisal" />
		</xsl:if>
		<xsl:if test="ead:processinfo">
			<xsl:call-template name="processinfo" />
		</xsl:if>
		<xsl:if test="ead:accruals">
			<xsl:call-template name="accruals" />
		</xsl:if>
		<xsl:if test="ead:acqinfo">
			<xsl:call-template name="acqinfo" />
		</xsl:if>
		<xsl:if test="ead:arrangement">
			<xsl:call-template name="arrangement" />
		</xsl:if>
		<xsl:if test="ead:fileplan">
			<xsl:call-template name="fileplan" />
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
		<xsl:if test="ead:otherfindaid">
			<xsl:call-template name="otherfindaid" />
		</xsl:if>
		<xsl:if test="ead:relatedmaterial">
			<xsl:call-template name="relatedmaterial" />
		</xsl:if>
		<xsl:if test="ead:separatedmaterial">
			<xsl:call-template name="separatedmaterial" />
		</xsl:if>
		<xsl:if test="ead:altformavail">
			<xsl:call-template name="altformavail" />
		</xsl:if>
		<xsl:if test="ead:originalsloc">
			<xsl:call-template name="originalsloc" />
		</xsl:if>
		<xsl:if test="ead:bibliography">
			<xsl:call-template name="bibliography" />
		</xsl:if>
		<xsl:if test="ead:prefercite">
			<xsl:call-template name="prefercite" />
		</xsl:if>
		<xsl:apply-templates select="./ead:did"/>

		<xsl:if test="ead:odd">
			<xsl:call-template name="odd" />
		</xsl:if>
		<xsl:if test="ead:controlaccess">
			<xsl:call-template name="controlaccess" />
		</xsl:if>
	</xsl:template>
	<xsl:template match="ead:did">
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
	</xsl:template>

</xsl:stylesheet>