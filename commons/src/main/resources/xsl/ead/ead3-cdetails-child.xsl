<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi ead ape fn #default">
	<xsl:param name="eadcontent.extref.prefix"/>
	<xsl:include href="common.xsl" />
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<xsl:for-each select="ead:c/ead:c">
		<div class="child">
				<xsl:call-template name="childheader"  />
				<xsl:apply-templates select="."  />
		</div>		
		</xsl:for-each>


	</xsl:template>
	<xsl:template name="childheader">
		<div class="childHeader">
			<table>
				<tr>
					<td class="expand-unitid">
						<xsl:for-each select="ead:did/ead:unitid[@type='call number']">
							<xsl:if test="position() = 1">
							<xsl:value-of select="ape:highlight(., 'unitid')" disable-output-escaping="yes" />
							</xsl:if>
						</xsl:for-each>
					</td>
					<td class="expand-unittitle">
							<xsl:for-each select="ead:did/ead:unittitle">
									<xsl:apply-templates mode="title"/><xsl:text> </xsl:text>
							</xsl:for-each>
					</td>
					<td class="expand-unitdate">
						<xsl:apply-templates  select="/ead:c/ead:did/ead:unitdate"  mode="alterdate"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>



	<xsl:template match="ead:c">
		<div class="childContent">
			<xsl:if test="ead:scopecontent">
				<xsl:call-template name="scopecontent" />
			</xsl:if>
			<xsl:apply-templates select="./ead:did" />
		</div>
	</xsl:template>
	<xsl:template match="ead:did">
		<xsl:if test="ead:langmaterial">
			<xsl:call-template name="langmaterial" />
		</xsl:if>
		<xsl:if test="./ead:dao[@xlink:title!='thumbnail' or not(@xlink:title)]">
			<xsl:call-template name="dao" />
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>