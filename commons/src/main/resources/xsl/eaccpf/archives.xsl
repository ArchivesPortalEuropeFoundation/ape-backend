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
			This file contains the relations between the entity (corporate body, person or family) and other eag entities.
		</xd:detail>
	</xd:doc>
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />

	<!-- Template for archives relations. -->
	
	<xd:doc>
		<xd:short>
			Template for related names. 
		</xd:short>
		<xd:detail>
			This template display the box "Archives" in relations section.
		</xd:detail>
	</xd:doc>
	
	<xd:doc>
		<xd:short>
			Template for archives relations. 
		</xd:short>
		<xd:detail>
			This template display the box "Archives" in the relations section.
		</xd:detail>
	</xd:doc>	
	<xsl:template name="archivesRelations">
		<div id="archives" class="box">
			<div class="boxtitle">
				<span class="collapsibleIcon expanded"> </span>
				<span class="text"><xsl:value-of select="ape:resource('eaccpf.portal.archives')"/>
				    <xsl:text> (</xsl:text>
					<xsl:value-of select="count(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation[eac:relationEntry[@localType='agencyName' or @localType='agencyCode']])"/>
					<xsl:text>)</xsl:text>
				</span>
			</div>
			<ul>
				<!--  Issue #1572: Only display the first 100 relations. -->
				<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation[position() &lt;= 100]">
						<xsl:choose>
							<xsl:when test="./eac:relationEntry[@localType='agencyName'] or ./eac:relationEntry[@localType='agencyCode']">
								<li>
									<xsl:call-template name="multilanguageArchives">
					   					<xsl:with-param name="list" select="./eac:relationEntry[@localType='agencyName' or @localType='agencyCode']"/>
					   				</xsl:call-template>
				   				</li>
							</xsl:when>
							<xsl:otherwise/>
						</xsl:choose>
						
	   		    </xsl:for-each>
	   		</ul>
	   		<div class="whitespace">
	   		 <!--   &nbsp;--> 
	   		</div>
	   		<a class="displayLinkShowMore" href="javascript:showMore('archives', 'li');">
				<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/><xsl:text>...</xsl:text>
			</a>
			<a class="displayLinkShowLess" href="javascript:showLess('archives', 'li');">
				<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/><xsl:text>...</xsl:text>
			</a>
		</div>
	</xsl:template>

	<!-- template for multilanguage archives -->
	
	<xd:doc>
		<xd:short>
			Template for multilanguage archives. 
		</xd:short>
		<xd:detail>
			This template display the elements <code>&lt;relationEntry</code> with the attribute <code>@localType="agencyCode"</code>
			 or <code>@localType="agencyName"</code> according with the language's rules.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;relationEntry</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageArchives">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
					 	<xsl:for-each select="$list[@xml:lang = $language.selected]"> 
						   <xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode'] and ./parent::node()/eac:relationEntry[@localType='agencyName']">
										<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
								  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank">
												<xsl:value-of select="$aiName"/>
											</a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$aiName"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
								  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:apply-templates select="." mode="other"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:apply-templates select="." mode="other"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
<!-- 										<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyName']">
										<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
										<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
													<xsl:apply-templates select="." mode="other"/>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:apply-templates select="." mode="other"/>
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
					 	<xsl:for-each select="$list[@xml:lang = $lang.navigator]"> 
						   <xsl:if test="position()=1">
								<xsl:choose>
									<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode'] and ./parent::node()/eac:relationEntry[@localType='agencyName']">
										<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
								  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank">
												<xsl:value-of select="$aiName"/>
											</a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$aiName"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
								  		<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
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
									<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode'] and ./parent::node()/eac:relationEntry[@localType='agencyName']">
										<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank">
												<xsl:value-of select="$aiName"/>
											</a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$aiName"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:if>
									</xsl:when>
									<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode']">
										<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
										<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
											<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
								  		</xsl:if>
								  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
											<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
											<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:value-of select="$aiName"/>
													</a>
												</xsl:when>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:apply-templates select="." mode="other"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:apply-templates select="." mode="other"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:when>
<!-- 									<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyName']">
										<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
										<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
										<xsl:choose>
											<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
													<xsl:apply-templates select="." mode="other"/>
												</a>
											</xsl:when>
											<xsl:otherwise>
												<xsl:apply-templates select="." mode="other"/>
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
										<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode'] and ./parent::node()/eac:relationEntry[@localType='agencyName']">
											<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
											<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
											<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank">
													<xsl:value-of select="$aiName"/>
												</a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
															<xsl:value-of select="$aiName"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$aiName"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:when>
										<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode']">
											<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
											<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
															<xsl:value-of select="$aiName"/>
														</a>
													</xsl:when>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
															<xsl:apply-templates select="." mode="other"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:apply-templates select="." mode="other"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
<!-- 										<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyName']">
											<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
											<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:apply-templates select="." mode="other"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:apply-templates select="." mode="other"/>
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
										<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode'] and ./parent::node()/eac:relationEntry[@localType='agencyName']">
											<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
											<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
											<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank">
													<xsl:value-of select="$aiName"/>
												</a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
															<xsl:value-of select="$aiName"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$aiName"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:when>
										<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode']">
											<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
											<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
												<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
									  		</xsl:if>
									  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
												<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
												<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
												<xsl:choose>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
															<xsl:value-of select="$aiName"/>
														</a>
													</xsl:when>
													<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
														<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
														<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
															<xsl:apply-templates select="." mode="other"/>
														</a>
													</xsl:when>
													<xsl:otherwise>
														<xsl:apply-templates select="." mode="other"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:if>
											</xsl:when>
<!-- 										<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyName']">
											<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
											<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
											<xsl:choose>
												<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
													<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
													<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
														<xsl:apply-templates select="." mode="other"/>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<xsl:apply-templates select="." mode="other"/>
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
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					 <xsl:if test="position()=1">
						<xsl:choose>
							<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode'] and ./parent::node()/eac:relationEntry[@localType='agencyName']">
								<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
								<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
								<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
									<a href="{$href}" target="_blank">
										<xsl:value-of select="$aiName"/>
									</a>
						  		</xsl:if>
						  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
									<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
									<xsl:choose>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
											<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
											<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
												<xsl:value-of select="$aiName"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$aiName"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
							</xsl:when>
							<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyCode']">
								<xsl:variable name="href" select="./parent::node()/eac:relationEntry[@localType='agencyCode']"/>
								<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
									<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
						  		</xsl:if>
						  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
									<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
									<xsl:variable name="aiName" select="ape:checkAICode($href, '', 'false')" />
									<xsl:choose>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' and $aiName != 'ERROR' and $aiName != ''">
											<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
											<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
												<xsl:value-of select="$aiName"/>
											</a>
										</xsl:when>
										<xsl:when test="$aiCode != 'ERROR' and $aiCode != '' ">
											<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
											<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
												<xsl:apply-templates select="." mode="other"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:if>
								</xsl:when>
<!-- 							<xsl:when test="./parent::node()/eac:relationEntry[@localType='agencyName']">
								<xsl:variable name="aiName" select="./parent::node()/eac:relationEntry[@localType='agencyName']"/>
								<xsl:variable name="aiCode" select="ape:checkAICode('', $aiName, 'true')" />
								<xsl:choose>
									<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
										<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
										<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
											<xsl:apply-templates select="." mode="other"/>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when> -->
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="other"/>
							</xsl:otherwise>
						</xsl:choose>
					 </xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>