<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	xmlns:xd="http://www.pnp-software.com/XSLTdoc"
	exclude-result-prefixes="xlink xlink xsi eac ape xd">

	<xd:doc type="stylesheet">
		<xd:short>Page to display common parts to relation's section.</xd:short>
	</xd:doc>
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
		
	<!-- Template for multilanguage resourceRelations with @localType='title'. -->
	
	<xd:doc>
		<xd:short>Template for multilanguage relations with title.</xd:short>
		<xd:detail>
			Template for multilanguage <code>&lt;resourceRelation&gt;</code> with the attribute <code>@localType</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> elements with the attribute <code>@localType</code>.</xd:param>
	</xd:doc>

	<xsl:template name="multilanguageRelationsTitle">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="$list[@xml:lang = $language.selected and @localType='title'] and $list[@xml:lang = $language.selected and @localType='title']/text() and $list[@xml:lang = $language.selected and @localType='title']/text() != ''">
				<xsl:call-template name="multilanguageRelationTitleLanguageSelected">
					<xsl:with-param name="list" select="$list"></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$list[@xml:lang = $lang.navigator and @localType='title'] and $list[@xml:lang = $lang.navigator and @localType='title']/text() and $list[@xml:lang = $lang.navigator and @localType='title']/text() != ''">
			 	<xsl:call-template name="multilanguageRelationTitleLanguageNavigator">
			 		<xsl:with-param name="list" select="$list"></xsl:with-param>
			 	</xsl:call-template>
			</xsl:when>				
			<xsl:when test="$list[@xml:lang = $language.default and @localType='title'] and $list[@xml:lang = $language.default and @localType='title']/text() and $list[@xml:lang = $language.default and @localType='title']/text() != ''">
				<xsl:call-template name="multilanguageRelationTitleLanguageDefault">
					<xsl:with-param name="list" select="$list"></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$list[not(@xml:lang) and @localType='title'] and $list[not(@xml:lang) and @localType='title']/text() and $list[not(@xml:lang) and @localType='title']/text() != ''">
				<xsl:call-template name="multilanguageRelationTitleNotLanguage">
					<xsl:with-param name="list" select="$list"></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise> <!-- first language -->
				<xsl:call-template name="multilanguageRelationTitleFirstLanguage">
					<xsl:with-param name="list" select="$list"></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- BEGIN multilanguageRelationTitle templates with multilanguage included not(@xml:lang) -->
	<xd:doc>
		<xd:short>Template to multilanguage relations.</xd:short>
		<xd:detail>
			Template to display the relation's details with multilanguage included not <code>@xml:lang</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> element.</xd:param>
		<xd:param name="paramLanguage">The parameter "paramLanguage" is the language to apply.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelationTitleLanguageParam">
		<xsl:param name="list"/>
		<xsl:param name="paramLanguage"/>
		<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
 	 	<xsl:variable name="first" select="$list[1]"/>
		<xsl:choose>
 	 		<xsl:when test="$list/parent::node()/@xlink:href">
 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
					<a href="{$link}" target="_blank"> <!-- $list[@xml:lang = $language.selected and @localType='title'] -->
						<xsl:choose>
							<xsl:when test="count($list[@localType='title']) > 0">
								<xsl:choose>
									<xsl:when test="$paramLanguage!='notLang'">
										<xsl:for-each select="$list[@xml:lang = $paramLanguage and @localType='title']">
											<xsl:if test="position() > 1">
												<xsl:text>. </xsl:text>
											</xsl:if>
											<xsl:apply-templates select="." mode="other"/>
									 	</xsl:for-each>
									 </xsl:when>
									 <xsl:otherwise>
									 	<xsl:for-each select="$list[not(@xml:lang) and @localType='title']">
											<xsl:if test="position() > 1">
												<xsl:text>. </xsl:text>
											</xsl:if>
											<xsl:apply-templates select="." mode="other"/>
									 	</xsl:for-each>
									 </xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="$first/parent::node()/eac:relationEntry[@localType='id']">
								<xsl:call-template name="resourceRelationListId">
									<xsl:with-param name="list" select="$list[(not(@xml:lang) and @localType='title') or @localType='id']"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
							</xsl:otherwise>
						</xsl:choose>
					</a>
		 	   </xsl:if>
		 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
					<xsl:choose>
						<xsl:when test="$list/parent::node()[eac:relationEntry[@localType='agencyCode']]">
							<xsl:variable name="href" select="$list/parent::node()/eac:relationEntry[@localType='agencyCode']"/>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
								<xsl:variable name="aiCodeEad" select="ape:aiFromEad($link, $href)"/>
					  			<xsl:variable name="aiCodeEac" select="ape:aiFromEac($link, $href)"/>
								<xsl:choose>
									<xsl:when test="$aiCodeEad != 'ERROR' and $aiCodeEad != ''">
										<a href="{$eadUrl}/{$aiCodeEad}" target="_blank">
											<xsl:call-template name="multilanguageRelationTitleLanguageSelectedAList">
												<xsl:with-param name="list" select="$list"></xsl:with-param>
												<xsl:with-param name="paramLanguage" select="$paramLanguage"></xsl:with-param>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:when test="$aiCodeEac != 'ERROR' and $aiCodeEac != ''">
										<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEac)" />
										<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--										<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
										<a href="{$eacUrlBase}?repositoryCode={$aiCodeEac}&amp;recordId={$link}" target="_blank">
											<xsl:call-template name="multilanguageRelationTitleLanguageSelectedAList">
												<xsl:with-param name="list" select="$list"></xsl:with-param>
												<xsl:with-param name="paramLanguage" select="$paramLanguage"></xsl:with-param>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="multilanguageRelationTitleLanguageSelectedAList">
											<xsl:with-param name="list" select="$list"></xsl:with-param>
											<xsl:with-param name="paramLanguage" select="$paramLanguage"></xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="$link != ''">
									<xsl:variable name="aiCodeEac" select="ape:aiFromEac($link, '')"/>
									<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEac)" />
									<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--									<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
									<a href="{$eacUrlBase}?repositoryCode={$aiCodeEac}&amp;recordId={$link}" target="_blank">
										<xsl:call-template name="multilanguageRelationTitleLanguageSelectedAList">
											<xsl:with-param name="list" select="$list"></xsl:with-param>
											<xsl:with-param name="paramLanguage" select="$paramLanguage"></xsl:with-param>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:when test="$paramLanguage!='notLang'">
									<xsl:call-template name="resourceRelationListId">
										<xsl:with-param name="list" select="$list[(@xml:lang = $paramLanguage and @localType='title') or @localType='id']"/>
									</xsl:call-template>
								 </xsl:when>
								 <xsl:otherwise>
								 	<xsl:call-template name="resourceRelationListId">
										<xsl:with-param name="list" select="$list[(not(@xml:lang) and @localType='title') or @localType='id']"/>
									</xsl:call-template>
								 </xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
		 	   </xsl:if>
 	 		</xsl:when>
 	 		<xsl:when test="$paramLanguage = 'notLang' and $list[not(@xml:lang) and @localType='title']">
 	 			<xsl:call-template name="resourceRelationListId">
					<xsl:with-param name="list" select="$list[(not(@xml:lang) and @localType='title') or @localType='id']"/>
				</xsl:call-template>
 	 		</xsl:when>
 	 		<xsl:otherwise>
				<xsl:call-template name="resourceRelationListId">
					<xsl:with-param name="list" select="$list[(@xml:lang = $paramLanguage and @localType='title') or @localType='id']"/>
				</xsl:call-template>
 	 		</xsl:otherwise>
	 	</xsl:choose>
	 	
	 	<xsl:call-template name="relationType">
			<xsl:with-param name="current" select="$list/parent::node()"/>
		</xsl:call-template>
		
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations with title.</xd:short>
		<xd:detail>
			Template to display the relation's details when the language is the selected one or it hasn't language, and has the <code>@localType="title"</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code>.</xd:param>
		<xd:param name="paramLanguage">The parameter "paramLanguage" is the selected language.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelationTitleLanguageSelectedAList">
		<xsl:param name="list" />
		<xsl:param name="paramLanguage" />
		
		<xsl:choose>
			<xsl:when test="$paramLanguage!='notLang'">
				<xsl:for-each select="$list[@xml:lang = $paramLanguage and @localType='title']">
					<xsl:if test="position() > 1">
						<xsl:text>. </xsl:text>
					</xsl:if>
					<xsl:apply-templates select="." mode="other"/>
			 	</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list[not(@xml:lang) and @localType='title']">
					<xsl:if test="position() > 1">
						<xsl:text>. </xsl:text>
					</xsl:if>
					<xsl:apply-templates select="." mode="other"/>
			 	</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations with the language selected </xd:short>
		<xd:detail>
			Template to call the "multilanguageRelationTitleLanguageParam" when the language is the selected one.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelationTitleLanguageSelected">
		<xsl:param name="list"/>
 	 	
 	 	<xsl:call-template name="multilanguageRelationTitleLanguageParam">
 	 		<xsl:with-param name="list" select="$list"></xsl:with-param>
 	 		<xsl:with-param name="paramLanguage" select="$language.selected"></xsl:with-param>
 	 	</xsl:call-template>
	 	
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations with the navigator's language.</xd:short>
		<xd:detail>
			Template to call the "multilanguageRelationTitleLanguageParam" whith the navigator's language.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelationTitleLanguageNavigator">
		<xsl:param name="list"/>
 	 	
 	 	<xsl:call-template name="multilanguageRelationTitleLanguageParam">
 	 		<xsl:with-param name="list" select="$list"></xsl:with-param>
 	 		<xsl:with-param name="paramLanguage" select="$lang.navigator"></xsl:with-param>
 	 	</xsl:call-template>
	
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations with the default language.</xd:short>
		<xd:detail>
			Template to call the "multilanguageRelationTitleLanguageParam" whith the default language (english).
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelationTitleLanguageDefault">
		<xsl:param name="list"/>
 	 	
 	 	<xsl:call-template name="multilanguageRelationTitleLanguageParam">
 	 		<xsl:with-param name="list" select="$list"></xsl:with-param>
 	 		<xsl:with-param name="paramLanguage" select="$language.default"></xsl:with-param>
 	 	</xsl:call-template>
 	 	
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations with the first language.</xd:short>
		<xd:detail>
			Template to call the "multilanguageRelationTitleLanguageParam" whith the first language in the element <code>&lt;relationEntry&gt;</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelationTitleFirstLanguage">
		<xsl:param name="list"/>
		
 	 	<xsl:call-template name="multilanguageRelationTitleLanguageParam">
 	 		<xsl:with-param name="list" select="$list"></xsl:with-param>
 	 		<xsl:with-param name="paramLanguage" select="$list[@localType='title'][1]/@xml:lang" ></xsl:with-param>
 	 	</xsl:call-template>
		
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations with not <code>@xml:lang</code>.</xd:short>
		<xd:detail>
			Template to call the "multilanguageRelationTitleLanguageParam" whith the parameter "notLang".
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code>.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelationTitleNotLanguage">
		<xsl:param name="list"/>
 	 	
 	 	<xsl:call-template name="multilanguageRelationTitleLanguageParam">
 	 		<xsl:with-param name="list" select="$list"></xsl:with-param>
 	 		<xsl:with-param name="paramLanguage" select="'notLang'"></xsl:with-param>
 	 	</xsl:call-template>
	
	</xsl:template>
	
	<!-- END multilanguageRelationTitle templates with multilanguage included not(@xml:lang) -->
	
	<xd:doc>
		<xd:short>Template to <code>&lt;resourceRelation&gt;</code> with link and title.</xd:short>
		<xd:detail>
			Template to display the details of the element <code>&lt;resourceRelation&gt;</code> when it has the attribute <code>@xlink:href</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code>.</xd:param>
		<xd:param name="first">The parameter "first" is the first <code>&lt;resourceRelation&gt;</code> of the relation.</xd:param>
	</xd:doc>
	<xsl:template name="resourceRelationListAWithLink">
		<xsl:param name="list"></xsl:param>
		<xsl:param name="first"></xsl:param>
		
		<xsl:variable name="link" select="$first/parent::node()/@xlink:href"/>
		<xsl:variable name="link2" select="ape:aiFromEad($link,'')"/>
		<xsl:choose>
			<xsl:when test="$link2 != '' and $link2 != 'ERROR'">
				<a href="{$eadUrl}/{$link2}" target="_blank">
					<xsl:for-each select="$list[@localType='title']">
						<xsl:if test="(position() > 1)">
							<xsl:text>. </xsl:text>
						</xsl:if>
						<xsl:apply-templates select="." mode="other"/>
				 	</xsl:for-each>
				</a>
			</xsl:when>
			<xsl:otherwise>	<!-- link is not a valid id or published id so no link is drawn -->
				<!-- <a href="{$link}" target="_blank"> -->
					<xsl:for-each select="$list[@localType='title']">
						<xsl:if test="(position() > 1)">
							<xsl:text>. </xsl:text>
						</xsl:if>
						<xsl:apply-templates select="." mode="other"/>
				 	</xsl:for-each>
				<!-- </a>  -->
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to <code>&lt;resourceRelation&gt;</code> with title.</xd:short>
		<xd:detail>
			Template to display the title of <code>&lt;resourceRelation&gt;</code> as a link to ead.
		</xd:detail>
		<xd:param name="link2">The parameter "link2" is a link to ead.</xd:param>
		<xd:param name="title">The parameter "title" is the title from ead.</xd:param>
	</xd:doc>
	<xsl:template name="resourceRelationLinkWithIdATitleTwoCases">
		<xsl:param name="link2" />
		<xsl:param name="title" />
		<xsl:choose>
			<xsl:when test="$link2 != '' and $link2 != 'ERROR'">
				<a href="{$eadUrl}/{$link2}" target="_blank">
					<xsl:value-of select="$title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$title"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to <code>&lt;resourceRelation&gt;</code> with a link .</xd:short>
		<xd:detail>
			Template to display the details of the title of <code>&lt;resourceRelation&gt;</code> when it has the attribute <code>@xlink:href</code>.
		</xd:detail>
		<xd:param name="link">The parameter "link" is a link to ead.</xd:param>
		<xd:param name="title">The parameter "title" is the content of the first <code>&lt;relationEntry&gt;</code> with not <code>@localType</code>, in other case is a text plane like "Go to a related resource".</xd:param>
	</xd:doc>
	<xsl:template name="resourceRelationLinkWithIdATitleThreeCases">
		<xsl:param name="link" />
		<xsl:param name="title" />
		<xsl:choose>
			<xsl:when test="$link != ''">
				<xsl:variable name="link2" select="ape:aiFromEad($link,'')"/>
				<xsl:call-template name="resourceRelationLinkWithIdATitleTwoCases">
					<xsl:with-param name="link2" select="$link2"></xsl:with-param>
					<xsl:with-param name="title" select="$title"></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$title"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to <code>&lt;resourceRelation&gt;</code> with <code>@localType="id"</code>.</xd:short>
		<xd:detail>
			Template to search the title with <code>@localType="id"</code> when there is a link without a title.
		</xd:detail>
		<xd:param name="id">The parameter "id" is the attribute <code>@localType="id"</code>.</xd:param>
		<xd:param name="first">The parameter "first" is the first <code>&lt;resourceRelation&gt;</code> of the relation.</xd:param>
	</xd:doc>
	<xsl:template name="resourceRelationLinkWithIdA">
		<xsl:param name="id"></xsl:param>
		<xsl:param name="first"></xsl:param>
	
		<xsl:variable name="link" select="$first/parent::node()/@xlink:href"/>
		<xsl:variable name="title" select="ape:titleFromEad($id,'')"/>
		
		<xsl:choose>
			<xsl:when test="$title != ''">
				<xsl:choose>
					<xsl:when test="$link != ''">
						<xsl:variable name="link2" select="ape:aiFromEad($link,'')"/>
						<xsl:call-template name="resourceRelationLinkWithIdATitleTwoCases">
							<xsl:with-param name="link2" select="$link2"></xsl:with-param>
							<xsl:with-param name="title" select="$title"></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="aiCode" select="ape:aiFromEad($id,'')"/>
						<xsl:call-template name="resourceRelationLinkWithIdATitleTwoCases">
							<xsl:with-param name="link2" select="$aiCode"></xsl:with-param>
							<xsl:with-param name="title" select="$title"></xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$first/parent::node()/eac:relationEntry[not(@localType)]">
						<xsl:variable name="title" select="$first/parent::node()/eac:relationEntry[not(@localType)][1]/text()"/>
						<xsl:call-template name="resourceRelationLinkWithIdATitleThreeCases">
							<xsl:with-param name="title" select="$title"></xsl:with-param>
							<xsl:with-param name="link" select="$link"></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="title" select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
						<xsl:call-template name="resourceRelationLinkWithIdATitleThreeCases">
							<xsl:with-param name="title" select="$title"></xsl:with-param>
							<xsl:with-param name="link" select="$link"></xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations without <code>@localType="id"</code></xd:short>
		<xd:detail>
			Template to display the details of the element <code>&lt;resourceRelation&gt;</code> when it hasn't the attribute <code>@localType="id"</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code>.</xd:param>
		<xd:param name="title">The parameter "title" is the attribute <code>@xlink:title</code>.</xd:param>
		<xd:param name="first">The parameter "first" is the first <code>&lt;resourceRelation&gt;</code> of the relation.</xd:param>
	</xd:doc>
	<xsl:template name="resourceRelationListWithoutIdNotA">
		<xsl:param name="list"></xsl:param>
		<xsl:param name="title"></xsl:param>
		<xsl:param name="first"></xsl:param>
								
		<xsl:choose>
			<xsl:when test="$title != '' and $title != 'ERROR'">
				<xsl:value-of select="$title"/>
			</xsl:when>
			<xsl:when test="$list[not(@localType)]">
				<xsl:for-each select="$list[not(@localType)]">
					<xsl:if test="(position() > 1)">
						<xsl:text>. </xsl:text>
					</xsl:if>
					<xsl:apply-templates select="." mode="other"/>
			 	</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list[@localType='title']">
					<xsl:if test="(position() > 1)">
						<xsl:text>. </xsl:text>
					</xsl:if>
					<xsl:apply-templates select="." mode="other"/>
			 	</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	
	
	<xd:doc>
		<xd:short>Template to relations with <code>@localType="id"</code></xd:short>
		<xd:detail>
			Template to display the details of the element <code>&lt;resourceRelation&gt;</code> when it has the attribute <code>@localType="id"</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code></xd:param>
		<xd:param name="first">The parameter "first" is the first <code>&lt;resourceRelation&gt;</code> of the relation.</xd:param>
	</xd:doc>
	<xsl:template name="resourceRelationListWithIdA">
		<xsl:param name="list"></xsl:param>
		<xsl:param name="first"></xsl:param>
	
		<xsl:variable name="id" select="$first/parent::node()/eac:relationEntry[@localType='id'][1]/text()"/>
		<xsl:variable name="aiCode" select="ape:aiFromEad($id,'')"/>
		<xsl:variable name="title" select="$list[@localType='title']/text()"/>
		<xsl:choose>
			<xsl:when test="$title != ''">
				<xsl:choose>
					<xsl:when test="$aiCode != '' and $aiCode != 'ERROR'">
						<a href="{$eadUrl}/{$aiCode}" target="_blank">
							<xsl:for-each select="$list[@localType='title']">
								<xsl:if test="position() > 1">
									<xsl:text>. </xsl:text>
								</xsl:if>
								<xsl:apply-templates select="." mode="other"/>
						 	</xsl:for-each>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each select="$list[@localType='title']">
							<xsl:if test="position() > 1">
								<xsl:text>. </xsl:text>
							</xsl:if>
							<xsl:apply-templates select="." mode="other"/>
					 	</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$aiCode != ''"> <!-- id is valid -->
				<xsl:variable name="eadTitle" select="ape:titleFromEad($id,'')"/>
				<xsl:choose> <!-- generate at the same way that up -->
					<xsl:when test="$eadTitle != 'ERROR' and $eadTitle != ''">
						<a href="{$eadUrl}/{$aiCode}" target="_blank">
							<xsl:value-of select="$eadTitle"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="{$eadUrl}/{$aiCode}" target="_blank">
							<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$first/parent::node()/eac:relationEntry[not(@localType)]">
				<xsl:value-of select="$first/parent::node()/eac:relationEntry[not(@localType)][1]/text()"></xsl:value-of>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to relations with <code>@localType="id"</code></xd:short>
		<xd:detail>
			Template to display the details of the element <code>&lt;resourceRelation&gt;</code> when it has the attribute <code>@localType="id"</code>.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="resourceRelationListId">
		<xsl:param name="list"/>
		<xsl:variable name="first" select="$list[1]/parent::node()/eac:relationEntry[1]" />
		<xsl:variable name="title" select="$list[@localType='title']/text()"/>
		<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
		<xsl:choose>
			<xsl:when test="$link != ''">
				<xsl:choose>
					<xsl:when test="$title!=''"> <!-- there is a link with a title -->
						<xsl:choose>
							<xsl:when test="$first/parent::node()/@xlink:href">
								<xsl:call-template name="resourceRelationListAWithLink">
									<xsl:with-param name="list" select="$list"></xsl:with-param>
									<xsl:with-param name="first" select="$first"></xsl:with-param>
								</xsl:call-template>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise> <!-- there is a link without a title -->
						<xsl:choose>
							<xsl:when test="$first/parent::node()/eac:relationEntry[@localType='id']"> <!-- search title with id -->
								<xsl:call-template name="resourceRelationLinkWithIdA">
									<xsl:with-param name="id" select="$first/parent::node()/eac:relationEntry[@localType='id'][1]/text()"></xsl:with-param>
									<xsl:with-param name="first" select="$first"></xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<!-- <xsl:variable name="title" select="ape:titleFromEad($href,'')"/>  -->
								<xsl:call-template name="resourceRelationListWithoutIdNotA">
									<xsl:with-param name="list" select="$list"></xsl:with-param>
									<xsl:with-param name="title" select="$title"></xsl:with-param>
									<xsl:with-param name="first" select="$first"></xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$first/parent::node()/eac:relationEntry[@localType='id']">
						<xsl:call-template name="resourceRelationListWithIdA">
							<xsl:with-param name="list" select="$list"></xsl:with-param>
							<xsl:with-param name="first" select="$first"></xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="$list[@localType='title']">
						<xsl:for-each select="$list[@localType='title']">
							<xsl:if test="position() > 1">
								<xsl:text>. </xsl:text>
							</xsl:if>
							<xsl:apply-templates select="." mode="other"/>
					 	</xsl:for-each>
					</xsl:when>
					<xsl:when test="$first/parent::node()/eac:relationEntry[not(@localType)]">
						<xsl:value-of select="$first/parent::node()/eac:relationEntry[not(@localType)][1]/text()"></xsl:value-of>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="ape:resource('eaccpf.portal.goToRelatedResource')"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	

	<!-- Template for multilanguage relations. -->
	
	<xd:doc>
		<xd:short>Template for multilanguage relations.</xd:short>
		<xd:detail>
			Template to display the details of the relations elements according to the rules language.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageRelations">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
					 	<xsl:for-each select="$list[@xml:lang = $language.selected]">
						   <xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/@xlink:href">
										<xsl:call-template name="linkToFile">
											<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
							</xsl:if>
					 	</xsl:for-each> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
					 	<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
						   <xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/@xlink:href">
										<xsl:call-template name="linkToFile">
											<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
							</xsl:if>
					 	</xsl:for-each> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/@xlink:href">
										<xsl:call-template name="linkToFile">
											<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
					  	<xsl:for-each select="$list[not(@xml:lang)]"> 
							<xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/@xlink:href">
										<xsl:call-template name="linkToFile">
											<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
								<xsl:call-template name="relationType">
									<xsl:with-param name="current" select="./parent::node()"/>
								</xsl:call-template>
							</xsl:if>
					   	</xsl:for-each> 
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="$currentLang = $language.first">
								<xsl:if test="position()=1">
									<xsl:choose>
										<xsl:when test="./parent::node()/@xlink:href">
											<xsl:call-template name="linkToFile">
												<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/>
										</xsl:otherwise>	
									</xsl:choose>
									<xsl:call-template name="relationType">
										<xsl:with-param name="current" select="./parent::node()"/>
									</xsl:call-template>
								</xsl:if>			
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					 <xsl:if test="position()=1">
						<xsl:choose>
							<xsl:when test="./parent::node()/@xlink:href">
								<xsl:call-template name="linkToFile">
									<xsl:with-param name="link" select="./parent::node()/@xlink:href"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="other"/>
							</xsl:otherwise>	
						</xsl:choose>
							<xsl:call-template name="relationType">
								<xsl:with-param name="current" select="./parent::node()"/>
							</xsl:call-template>
					 </xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template for links to file type. -->
	<xd:doc>
		<xd:short>Template to <code>@xlink:href</code>.</xd:short>
		<xd:detail>
			Template to display the details of the links to file type.
		</xd:detail>
		<xd:param name="link">The parameter "link" is the link to file type.</xd:param>
	</xd:doc>
	<xsl:template name="linkToFile">
		<xsl:param name="link"/>
	   	<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
			<a href="{$link}" target="_blank"><xsl:apply-templates select="current()" mode="other"/></a>
		</xsl:if>
		<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
			<xsl:choose>
				<xsl:when test="name(./parent::node()) = 'cpfRelation'">
					<xsl:choose>
						<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
							<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
								<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--								<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
								<a href="{$eacUrlBase}?repositoryCode={$href}&amp;recordId={$link}" target="_blank">
									<xsl:apply-templates select="." mode="other"/>
								</a>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--									<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
									<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>	
					</xsl:choose>
				</xsl:when>
				<xsl:when test="name(./parent::node()) = 'resourceRelation'">
					<xsl:choose>
						<xsl:when test="./parent::node()[eac:relationEntry[@localType='agencyCode']]">
							<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
							<xsl:variable name="aiCode" select="ape:aiFromEad($link, $href)"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
									<a href="{$eadUrl}/{$aiCode}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="aiCode" select="ape:aiFromEad($link, '')"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
									<a href="{$eadUrl}/{$aiCode}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>	
					</xsl:choose>
				</xsl:when>
				<xsl:when test="name(./parent::node()) != 'alternativeSet'">
					<xsl:choose>
						<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
							<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
					  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
								<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--								<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
								<a href="{$eacUrlBase}?repositoryCode={$href}&amp;recordId={$link}" target="_blank">
									<xsl:apply-templates select="." mode="other"/>
								</a>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
							<xsl:choose>
								<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
								<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--									<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
									<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
										<xsl:apply-templates select="." mode="other"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>	
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<!-- TODO: functionRelation -->
					<a href="#">
						<xsl:apply-templates select="." mode="other"/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!-- Template for attribute @cpfRelationType or @resourceRelationType. -->
	
	<xd:doc>
		<xd:short>Template for the relation type.</xd:short>
		<xd:detail>
			Template for attribute <code>@cpfRelationType</code> or <code>@resourceRelationType</code>.
		</xd:detail>
		<xd:param name="current">The parameter "current" is the <code>&lt;resourceRelation&gt;</code> or the <code>&lt;cpfRelation&gt;</code>.</xd:param>
	</xd:doc>
	<xsl:template name="relationType">
		<xsl:param name="current"/>
		<xsl:if test="name($current)='cpfRelation' and $current[@cpfRelationType]"> 
			<xsl:text> (</xsl:text>
			<xsl:choose>
				<xsl:when test="$current[@cpfRelationType='associative']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.associative')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='identity']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.identity')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='hierarchical']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchical')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='hierarchical-parent']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchicalParent')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='hierarchical-child']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.hierarchicalChild')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='temporal']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporal')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='temporal-earlier']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporalEarlier')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='temporal-later']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.temporalLater')"/>
				</xsl:when>
				<xsl:when test="$current[@cpfRelationType='family']"> 
					<xsl:value-of select="ape:resource('eaccpf.portal.family')"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
			<xsl:text>)</xsl:text>	
		</xsl:if>
		<xsl:if test="name($current)='resourceRelation' and $current[@resourceRelationType]"> 
			<xsl:text> (</xsl:text>
			<xsl:choose>
				<xsl:when test="$current[@resourceRelationType='creatorOf']">
					<xsl:value-of select="ape:resource('eaccpf.portal.creatorOf')"/>
				</xsl:when>
				<xsl:when test="$current[@resourceRelationType='subjectOf']">
					<xsl:value-of select="ape:resource('eaccpf.portal.subjectOf')"/>
				</xsl:when>
				<xsl:when test="$current[@resourceRelationType='other']">
					<xsl:value-of select="ape:resource('eaccpf.portal.other')"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
			<xsl:text>)</xsl:text>	
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
