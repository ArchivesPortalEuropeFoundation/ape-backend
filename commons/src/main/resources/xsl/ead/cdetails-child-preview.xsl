<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	exclude-result-prefixes="xlink xlink xsi ead ape">
	<xsl:param name="eadcontent.extref.prefix"/>
	<xsl:include href="common.xsl" />
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	<xsl:template match="/">
		<table cellspacing="0" cellpadding="0" width="100%">
		<xsl:for-each select="ead:c/ead:c">
				<xsl:call-template name="childheader"  />
		</xsl:for-each>
		</table>

	</xsl:template>
	<xsl:template name="childheader">
				<tr>
					<td class="expand-unitid">
						<xsl:value-of select="ape:highlight(ead:did/ead:unitid[@type='call number'], 'unitid')" disable-output-escaping="yes" />	
					</td>
					<td class="expand-unittitle">
							<xsl:for-each select="ead:did/ead:unittitle">
									<xsl:apply-templates mode="title"/><xsl:text> </xsl:text>
							</xsl:for-each>
					</td>
				</tr>
	</xsl:template>

</xsl:stylesheet>