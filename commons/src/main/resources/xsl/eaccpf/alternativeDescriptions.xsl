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
			This file contains the relations between the entity (corporate body) and other eac-cpf resources
			 with <code>&lt;alternativeSet&gt;</code> element.
		</xd:detail>
	</xd:doc>
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />

	<!-- Template for alternative descriptions. -->
	
	<xd:doc>
		<xd:short>
			Template for alternative descriptions. 
		</xd:short>
		<xd:detail>
			This template display the box "Alternative descriptions" in relations section.
		</xd:detail>
	</xd:doc>
	<xsl:template name="alternativeDescriptions">
		<div id="alternative" class="box">
			<div class="boxtitle">
				<span class="collapsibleIcon expanded"> </span>
				<span class="text"><xsl:value-of select="ape:resource('eaccpf.portal.alternativeDescriptions')"/>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="count(./eac:eac-cpf/eac:cpfDescription/eac:alternativeSet/eac:setComponent)"/>
					<xsl:text>)</xsl:text>
				</span>
			</div>
			<ul>
				<!--  Issue #1572: Only display the first 100 relations. -->
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:alternativeSet/eac:setComponent[position() &lt;= 100]">
					<li>
						<xsl:choose>
							<xsl:when test="./eac:componentEntry[@localType='title']">
									<xsl:call-template name="multilanguageAlternativeSetTitle">
						   			 	<xsl:with-param name="list" select="./eac:componentEntry[@localType='title']"/>
						   		</xsl:call-template>
					   		</xsl:when>
					   		<xsl:when test="./eac:componentEntry[not(@localType)]">
						   			<xsl:call-template name="multilanguageAlternativeSet">
						   			 	<xsl:with-param name="list" select="./eac:componentEntry[not(@localType)]"/>
							   		</xsl:call-template>
						   		</xsl:when>
						   		<xsl:otherwise>
					   				<xsl:variable name="link" select="./@xlink:href"/>
					   			<xsl:choose>
					   				<xsl:when test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
							   			<a href="{$link}" target="_blank">
							   				<xsl:value-of select="ape:resource('eaccpf.portal.goToAlternativeDescription')"/>
							   			</a>
					   				</xsl:when>
					   				<xsl:when test="./eac:componentEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./eac:componentEntry[@localType='agencyCode']"/>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')"/>
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--													<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
													<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
														<xsl:value-of select="ape:resource('eaccpf.portal.goToAlternativeDescription')"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="ape:resource('eaccpf.portal.goToAlternativeDescription')"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
						   				</xsl:when>
						   				<xsl:otherwise>
											<xsl:if test="$link != ''">
												<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
														<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--														<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
														<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
															<xsl:value-of select="ape:resource('eaccpf.portal.goToAlternativeDescription')"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="ape:resource('eaccpf.portal.goToAlternativeDescription')"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										<xsl:if test="$link = ''">
											<xsl:value-of select="ape:resource('eaccpf.portal.goToAlternativeDescription')"/>
										</xsl:if>
					   				</xsl:otherwise>
								</xsl:choose>

					   			<xsl:call-template name="agencyAlternativeSet">
									<xsl:with-param name="list" select="./eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
								</xsl:call-template>
					   		</xsl:otherwise>
				   		</xsl:choose>
			   		</li>
		   		</xsl:for-each>
	   		</ul>
	   		<div class="whitespace">
	   		 <!--   &nbsp;-->
	   		</div>
	   		<a class="displayLinkShowMore" href="javascript:showMore('alternative', 'li');">
				<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
			</a>
			<a class="displayLinkShowLess" href="javascript:showLess('alternative', 'li');">
				<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/><xsl:text>...</xsl:text>
			</a>
		</div>
	</xsl:template>

	<!-- Template for multilanguage alternativeSet with @localType='title'. -->
	
	<xd:doc>
	  <xd:short>Template for <code>&lt;alternativeSet&gt;</code>.
	  </xd:short>
	  <xd:detail>Template to display the details <code>&lt;alternativeSet&gt;</code> with the attribute <code>@localType='title'</code> 
	  according with the rules of the language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;alternativeSet&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageAlternativeSetTitle">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						 	 	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $language.selected]">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
											<xsl:choose>
												<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
										  			<xsl:variable name="aiCode" select="ape:aiFromEac($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
															<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--															<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
															<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
																<xsl:for-each select="$list[@xml:lang = $language.selected]">
																	<xsl:if test="position() > 1">
																		<xsl:text>. </xsl:text>
																	</xsl:if>
																	<xsl:apply-templates select="." mode="other"/>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list[@xml:lang = $language.selected]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
														<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
															<xsl:for-each select="$list[@xml:lang = $language.selected]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
															</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[@xml:lang = $language.selected]">
															<xsl:if test="position() > 1">
																<xsl:text>. </xsl:text>
															</xsl:if>
															<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
								 	   </xsl:if>
						 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[@xml:lang = $language.selected]">
										<xsl:if test="position() > 1">
											<xsl:text>. </xsl:text>
										</xsl:if>
										<xsl:apply-templates select="." mode="other"/>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						 	 	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
											<xsl:choose>
												<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
										  			<xsl:variable name="aiCode" select="ape:aiFromEac($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
															<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--															<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
															<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
																<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																	<xsl:if test="position() > 1">
																		<xsl:text>. </xsl:text>
																	</xsl:if>
																	<xsl:apply-templates select="." mode="other"/>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
														<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
															<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																<xsl:if test="position() > 1">
																	<xsl:text>. </xsl:text>
																</xsl:if>
																<xsl:apply-templates select="." mode="other"/>
															</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
															<xsl:if test="position() > 1">
																<xsl:text>. </xsl:text>
															</xsl:if>
															<xsl:apply-templates select="." mode="other"/>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
								 	   </xsl:if>
						 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
										<xsl:if test="position() > 1">
											<xsl:text>. </xsl:text>
										</xsl:if>
										<xsl:apply-templates select="." mode="other"/>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $language.default]">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--													<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
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
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
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
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[@xml:lang = $language.default]">
										<xsl:if test="position() > 1">
											<xsl:text>. </xsl:text>
										</xsl:if>
										<xsl:apply-templates select="." mode="other"/>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[not(@xml:lang)]">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--													<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
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
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
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
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[not(@xml:lang)]">
										<xsl:if test="position() > 1">
											<xsl:text>. </xsl:text>
										</xsl:if>
										<xsl:apply-templates select="." mode="other"/>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
				 	 	<xsl:choose>
				 	 		<xsl:when test="$list/parent::node()/@xlink:href">
				 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
									<a href="{$link}" target="_blank">
										<xsl:for-each select="$list">
											<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
											<xsl:if test="$currentLang = $language.first">
												<xsl:if test="position() > 1">
													<xsl:text>. </xsl:text>
												</xsl:if>
												<xsl:apply-templates select="." mode="other"/>
											</xsl:if>	
									 	</xsl:for-each>
									</a>
						 	   </xsl:if>
						 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
									<xsl:choose>
										<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
											<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
												<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--												<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
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
<!--													<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
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
						 	   </xsl:if>
				 	 		</xsl:when>
				 	 		<xsl:otherwise>
				 	 			<xsl:for-each select="$list">
									<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
									<xsl:if test="$currentLang = $language.first">
										<xsl:if test="position() > 1">
											<xsl:text>. </xsl:text>
										</xsl:if>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:if>	
							 	</xsl:for-each>
				 	 		</xsl:otherwise>
				 	 	</xsl:choose>
				 	 	<xsl:call-template name="agencyAlternativeSet">
							<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
						</xsl:call-template> 
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
		 	 	<xsl:choose>
		 	 		<xsl:when test="$list/parent::node()/@xlink:href">
		 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<a href="{$link}" target="_blank">
								<xsl:for-each select="$list">
									<xsl:apply-templates select="." mode="other"/>
							 	</xsl:for-each>
							</a>
						</xsl:if>
				 		<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
								<xsl:choose>
									<xsl:when test="$list/parent::node()[eac:componentEntry[@localType='agencyCode']]">
										<xsl:variable name="href" select="$list/parent::node()/eac:componentEntry[@localType='agencyCode']"/>
						  			<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:aiFromEac($link, $href)"/>
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--												<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
												<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
													<xsl:for-each select="$list">
														<xsl:apply-templates select="." mode="other"/>
														</xsl:for-each>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:for-each select="$list">
													<xsl:apply-templates select="." mode="other"/>
												 	</xsl:for-each>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
									<xsl:choose>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
											<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--											<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
											<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
												<xsl:for-each select="$list">
													<xsl:apply-templates select="." mode="other"/>
													</xsl:for-each>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:for-each select="$list">
												<xsl:apply-templates select="." mode="other"/>
												</xsl:for-each>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
					 		</xsl:if>
			 	 		</xsl:when>
			 	 		<xsl:otherwise>
			 	 			<xsl:for-each select="$list">
							<xsl:apply-templates select="." mode="other"/>
					 	</xsl:for-each>
		 	 		</xsl:otherwise>
		 	 	</xsl:choose>
		 	 	<xsl:call-template name="agencyAlternativeSet">
					<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template for multilanguage alternativeSet. -->
	
	<xd:doc>
	  <xd:short>Template for <code>&lt;alternativeSet&gt;</code>.</xd:short>
	  <xd:detail>Template to display the details <code>&lt;alternativeSet&gt;</code> 
	  according with the rules of the language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;alternativeSet&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageAlternativeSet">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
					 	 	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $language.selected]">
												<xsl:if test="position()=1">
													<xsl:apply-templates select="." mode="other"/>
												</xsl:if>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
											<xsl:choose>
												<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
										  			<xsl:variable name="aiCode" select="ape:aiFromEac($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
															<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--															<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
															<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
																<xsl:for-each select="$list[@xml:lang = $language.selected]">
																	<xsl:if test="position() = 1">
																		<xsl:apply-templates select="." mode="other"/>
																	</xsl:if>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list[@xml:lang = $language.selected]">
																<xsl:if test="position() = 1">
																	<xsl:apply-templates select="." mode="other"/>
																</xsl:if>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
														<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
															<xsl:for-each select="$list[@xml:lang = $language.selected]">
																<xsl:if test="position() = 1">
																	<xsl:apply-templates select="." mode="other"/>
																</xsl:if>
															</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[@xml:lang = $language.selected]">
															<xsl:if test="position() = 1">
																<xsl:apply-templates select="." mode="other"/>
															</xsl:if>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
								 	   </xsl:if>
						 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[@xml:lang = $language.selected]">
										<xsl:if test="position() = 1">
											<xsl:apply-templates select="." mode="other"/>
										</xsl:if>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
					 	 	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
												<xsl:if test="position()=1">
													<xsl:apply-templates select="." mode="other"/>
												</xsl:if>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
											<xsl:choose>
												<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
										  			<xsl:variable name="aiCode" select="ape:aiFromEac($link, $href)"/>
													<xsl:choose>
														<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
															<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
															<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--															<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
															<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
																<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																	<xsl:if test="position() = 1">
																		<xsl:apply-templates select="." mode="other"/>
																	</xsl:if>
															 	</xsl:for-each>
															</a>
														</xsl:when>
														<xsl:otherwise>
															<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																<xsl:if test="position() = 1">
																	<xsl:apply-templates select="." mode="other"/>
																</xsl:if>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
														<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
															<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
																<xsl:if test="position() = 1">
																	<xsl:apply-templates select="." mode="other"/>
																</xsl:if>
															</xsl:for-each>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
															<xsl:if test="position() = 1">
																<xsl:apply-templates select="." mode="other"/>
															</xsl:if>
														 	</xsl:for-each>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:otherwise>
											</xsl:choose>
								 	   </xsl:if>
						 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
										<xsl:if test="position() = 1">
											<xsl:apply-templates select="." mode="other"/>
										</xsl:if>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[@xml:lang = $language.default]">
												<xsl:if test="position() = 1">
													<xsl:apply-templates select="." mode="other"/>
												</xsl:if>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--													<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
													<a href="{$eacUrlBase}?repositoryCode={$href}&amp;recordId={$link}" target="_blank">
														<xsl:if test="position() = 1">
															<xsl:apply-templates select="." mode="other"/>
														</xsl:if>
													</a>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
														<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
															<xsl:if test="position() = 1">
																<xsl:apply-templates select="." mode="other"/>
															</xsl:if>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:if test="position() = 1">
															<xsl:apply-templates select="." mode="other"/>
														</xsl:if>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>	
										</xsl:choose>
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[@xml:lang = $language.default]">
										<xsl:if test="position() = 1">
											<xsl:apply-templates select="." mode="other"/>
										</xsl:if>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
					 	 	<xsl:choose>
					 	 		<xsl:when test="$list/parent::node()/@xlink:href">
					 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
										<a href="{$link}" target="_blank">
											<xsl:for-each select="$list[not(@xml:lang)]">
												<xsl:if test="position() = 1">
													<xsl:apply-templates select="." mode="other"/>
												</xsl:if>
										 	</xsl:for-each>
										</a>
							 	   </xsl:if>
							 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
										<xsl:choose>
											<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
												<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
													<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
													<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--													<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
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
<!--														<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
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
							 	   </xsl:if>
					 	 		</xsl:when>
					 	 		<xsl:otherwise>
					 	 			<xsl:for-each select="$list[not(@xml:lang)]">
										<xsl:if test="position() = 1">
											<xsl:apply-templates select="." mode="other"/>
										</xsl:if>
								 	</xsl:for-each>
					 	 		</xsl:otherwise>
					 	 	</xsl:choose>
					 	 	<xsl:call-template name="agencyAlternativeSet">
								<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
							</xsl:call-template> 
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
				 	 	<xsl:choose>
				 	 		<xsl:when test="$list/parent::node()/@xlink:href">
				 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
									<a href="{$link}" target="_blank">
										<xsl:for-each select="$list">
											<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
											<xsl:if test="$currentLang = $language.first">
												<xsl:if test="position() = 1">
													<xsl:apply-templates select="." mode="other"/>
												</xsl:if>
											</xsl:if>	
									 	</xsl:for-each>
									</a>
						 	   </xsl:if>
						 	   <xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
									<xsl:choose>
										<xsl:when test="./parent::node()[eac:componentEntry[@localType='agencyCode']]">
											<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="encodedHref" select="ape:encodeSpecialCharacters($href)" />
												<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--												<a href="{$eacUrlBase}/aicode/{$encodedHref}/type/ec/id/{$encodedlink}" target="_blank">-->
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
<!--													<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
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
						 	   </xsl:if>
				 	 		</xsl:when>
				 	 		<xsl:otherwise>
				 	 			<xsl:for-each select="$list">
									<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
									<xsl:if test="$currentLang = $language.first">
										<xsl:if test="position() = 1">
											<xsl:apply-templates select="." mode="other"/>
										</xsl:if>
									</xsl:if>	
							 	</xsl:for-each>
				 	 		</xsl:otherwise>
				 	 	</xsl:choose>
				 	 	<xsl:call-template name="agencyAlternativeSet">
							<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
						</xsl:call-template> 
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="link" select="$list/parent::node()/@xlink:href"/>
		 	 	<xsl:choose>
		 	 		<xsl:when test="$list/parent::node()/@xlink:href">
		 	 			<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
								<a href="{$link}" target="_blank">
								<xsl:for-each select="$list">
									<xsl:apply-templates select="." mode="other"/>
							 	</xsl:for-each>
							</a>
						</xsl:if>
				 		<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
								<xsl:choose>
									<xsl:when test="$list/parent::node()[eac:componentEntry[@localType='agencyCode']]">
										<xsl:variable name="href" select="$list/parent::node()/eac:componentEntry[@localType='agencyCode']"/>
						  			<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:aiFromEac($link, $href)"/>
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--												<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
												<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
													<xsl:for-each select="$list">
														<xsl:apply-templates select="." mode="other"/>
														</xsl:for-each>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:for-each select="$list">
													<xsl:apply-templates select="." mode="other"/>
												 	</xsl:for-each>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<xsl:variable name="aiCode" select="ape:aiFromEac($link, '')"/>
									<xsl:choose>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
											<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--											<a href="{$eacUrlBase}/aicode/{$aiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
											<a href="{$eacUrlBase}?repositoryCode={$aiCode}&amp;recordId={$link}" target="_blank">
												<xsl:for-each select="$list">
													<xsl:apply-templates select="." mode="other"/>
													</xsl:for-each>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:for-each select="$list">
												<xsl:apply-templates select="." mode="other"/>
												</xsl:for-each>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
					 		</xsl:if>
			 	 		</xsl:when>
			 	 		<xsl:otherwise>
			 	 			<xsl:for-each select="$list">
							<xsl:apply-templates select="." mode="other"/>
					 	</xsl:for-each>
		 	 		</xsl:otherwise>
		 	 	</xsl:choose>
		 	 	<xsl:call-template name="agencyAlternativeSet">
					<xsl:with-param name="list" select="$list/parent::node()/eac:componentEntry[@localType='agencyName' or @localType='agencyCode']"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template for the link to the related institution of an AlternativeSet. -->
	
	<xd:doc>
	  <xd:short>Template for <code>&lt;alternativeSet&gt;</code> with "agencyName".</xd:short>
	  <xd:detail>Template for the link to the related institution, attribute <code>@localType="agencyName"</code>, of an <code>&lt;alternativeSet&gt;</code> element.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;alternativeSet&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="agencyAlternativeSet">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:text> (</xsl:text>
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
					 	<xsl:for-each select="$list[@xml:lang = $language.selected]"> 
						   <xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode'] and ./parent::node()/eac:componentEntry[@localType='agencyName']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
									  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank">
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
									</xsl:when>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
									  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:value-of select="$aiName"/>
														</a>
													</xsl:when>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:apply-templates select="." mode="other"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:apply-templates select="." mode="other"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:when>
<!-- 										<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyName']">
										<xsl:variable name="aiName" select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
										<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when> -->
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
							</xsl:if>
					 	</xsl:for-each> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
					 	<xsl:for-each select="$list[@xml:lang = $language.selected]"> 
						   <xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode'] and ./parent::node()/eac:componentEntry[@localType='agencyName']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
									  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank">
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
									</xsl:when>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
									  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:value-of select="$aiName"/>
														</a>
													</xsl:when>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
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
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
							</xsl:if>
					 	</xsl:for-each> 
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode'] and ./parent::node()/eac:componentEntry[@localType='agencyName']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank">
												<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
											</a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
													<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
														<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
													<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
													<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
														<xsl:apply-templates select="." mode="other"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:apply-templates select="." mode="other"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:when>
<!-- 										<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyName']">
										<xsl:variable name="aiName" select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
										<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when> -->
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
					  	<xsl:for-each select="$list[not(@xml:lang)]"> 
							<xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode'] and ./parent::node()/eac:componentEntry[@localType='agencyName']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank">
												<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
											</a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
													<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
														<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
													<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
													<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
														<xsl:apply-templates select="." mode="other"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:apply-templates select="." mode="other"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
<!-- 										<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyName']">
										<xsl:variable name="aiName" select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
										<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:when> -->
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>	
								</xsl:choose>
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
										<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode'] and ./parent::node()/eac:componentEntry[@localType='agencyName']">
											<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
											<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank">
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:when>
										<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode']">
											<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
											<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:value-of select="$aiName"/>
														</a>
													</xsl:when>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
														<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
															<xsl:apply-templates select="." mode="other"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:apply-templates select="." mode="other"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
<!-- 											<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyName']">
											<xsl:variable name="aiName" select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
											<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when> -->
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>			
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					 <xsl:if test="position()=1">
						<xsl:text> (</xsl:text>
						<xsl:choose>
							<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode'] and ./parent::node()/eac:componentEntry[@localType='agencyName']">
								<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
								<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
									<a href="{$href}" target="_blank">
										<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
									</a>
						  		</xsl:if>
						  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
									<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
									<xsl:choose>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
											<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--											<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
											<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
												<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
							</xsl:when>
							<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyCode']">
								<xsl:variable name="href" select="./parent::node()/eac:componentEntry[@localType='agencyCode']"/>
								<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
									<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
						  		</xsl:if>
						  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
									<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
									<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
									<xsl:choose>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
											<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--											<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
											<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
												<xsl:value-of select="$aiName"/>
											</a>
										</xsl:when>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
											<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
<!--											<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
											<a href="{$aiCodeUrl}?repositoryCode={$aiCode}" target="_blank">
												<xsl:apply-templates select="." mode="other"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:if>
								</xsl:when>
<!-- 								<xsl:when test="./parent::node()/eac:componentEntry[@localType='agencyName']">
								<xsl:variable name="aiName" select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
								<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
								<xsl:choose>
									<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
										<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
										<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
											<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="./parent::node()/eac:componentEntry[@localType='agencyName']"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when> -->
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="other"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>)</xsl:text>
					 </xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
