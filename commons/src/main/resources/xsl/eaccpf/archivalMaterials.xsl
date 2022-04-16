<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	xmlns:xd="http://www.pnp-software.com/XSLTdoc"
	exclude-result-prefixes="xlink xlink xsi eac ape xd">

	<xd:doc type="stylesheet">
		<xd:short>Page to display the information that links the entity.</xd:short>
		<xd:detail>
			This file contains the relations between the entity and other ead resources.
		</xd:detail>
	</xd:doc>
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />

	<!-- Template for archival materials. -->
	
	<xd:doc>
		<xd:short>
			Template for archival materials. 
		</xd:short>
		<xd:detail>
			This template display the box "Archival materials" in relations section.
		</xd:detail>
	</xd:doc>
	<xsl:template name="archivalMaterials">
		<div id="material" class="box">
			<div class="boxtitle">
				<span class="collapsibleIcon expanded"> </span>
				<span class="text"><xsl:value-of select="ape:resource('eaccpf.portal.archivalMaterial')"/>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="count(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation)"/>
					<xsl:text>)</xsl:text>
				</span>
			</div>
			<ul>
				<!--  Issue #1572: Only display the first 100 relations. -->
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation[position() &lt;= 100]">
						<li>
						<xsl:if test="./eac:relationEntry[@localType='title'] or ./eac:relationEntry[@localType='id'] or ./eac:relationEntry[not(@localType)] or @xlink:href">
							<xsl:choose>
								<xsl:when test="./eac:relationEntry[@localType='title' or @localType='id']">
									<xsl:call-template name="multilanguageRelationsTitle">
							   			<xsl:with-param name="list" select="./eac:relationEntry[@localType='title' or @localType='id']"/>
							   		</xsl:call-template>
						   		</xsl:when>
						   		<xsl:when test="./eac:relationEntry[not(@localType)]">
						   			<xsl:call-template name="multilanguageRelations">
							   			<xsl:with-param name="list" select="./eac:relationEntry[not(@localType)][1]"/>
							   		</xsl:call-template>
						   		</xsl:when>
						   		<xsl:otherwise>
						   			<xsl:choose>
							   			<xsl:when test="./@xlink:href != ''">
											<xsl:variable name="link" select="./@xlink:href"/>
											<xsl:choose>
												<xsl:when test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
													<a href="{$link}" target="_blank">
								   						<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
								   					</a>
												</xsl:when>
												<xsl:when test="./eac:relationEntry[@localType='agencyCode']">
													<xsl:variable name="href" select="./eac:relationEntry[@localType='agencyCode']"/>
											  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
														<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
														<xsl:choose>
															<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
																<a href="{$eadUrl}/{$aiCode}" target="_blank">
																	<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
																</a>
															</xsl:when>
															<xsl:otherwise>
																<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:if>
												</xsl:when>
												<xsl:when test="$link != ''">
													<xsl:variable name="aiCode" select="ape:aiFromEad($link,'')"/>
													<xsl:choose>
														<xsl:when test="$aiCode != '' and $aiCode != 'ERROR'">
															<a href="{$eadUrl}/{$aiCode}" target="_blank">
																<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:when>
												<xsl:when test="$link = ''">
													<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
												</xsl:when>
												<xsl:otherwise />
											</xsl:choose>
											<xsl:if test="./@xlink:href != ''">
									   			<xsl:call-template name="relationType">
													<xsl:with-param name="current" select="."/>
												</xsl:call-template>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise />
									</xsl:choose>
						   		</xsl:otherwise>
					   		</xsl:choose>
					   	</xsl:if>
			   		</li>
		   		</xsl:for-each>
	   		</ul>
	   		<div class="whitespace">
	   		<!--   &nbsp;--> 
	   		</div>
	   		<a class="displayLinkShowMore" href="javascript:showMore('material', 'li');">
				<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
			</a>
			<a class="displayLinkShowLess" href="javascript:showLess('material', 'li');">
				<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/><xsl:text>...</xsl:text>
			</a>
		</div>
	</xsl:template>
</xsl:stylesheet>