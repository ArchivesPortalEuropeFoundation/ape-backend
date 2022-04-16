<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	xmlns:xd="http://www.pnp-software.com/XSLTdoc"
	exclude-result-prefixes="xlink xlink xsi eac ape xd">

	<xd:doc type="stylesheet">
		<xd:short>Page to display the details related to the dates in the different elements of the eac-cpf.</xd:short>
	</xd:doc>
	
	<xsl:output method="html" indent="yes" version="4.0" encoding="UTF-8" />
	
	<xd:doc>
	  <xd:short>Template for <code>&lt;existDates&gt;</code>.</xd:short>
	  <xd:detail>Template for show the details for <code>&lt;date&gt;</code>, <code>&lt;dateRange&gt;</code> and <code>&lt;dateSet&gt;</code>
	  inside the element <code>&lt;existDates&gt;</code>.
	  </xd:detail>
	</xd:doc>
	<xsl:template match="*" mode="existDates">
	  <xsl:apply-templates mode="#current"/>
	</xsl:template>
	
	<!-- template for commons dates -->
	<xd:doc>
	  <xd:short>Template to show details for dates.
	  </xd:short>
	  <xd:detail>
	  	Display the information of the dates in <code>&lt;place&gt;</code>, <code>&lt;localDescription&gt;</code>, <code>&lt;legalStatus&gt;</code>,
	  	<code>&lt;function&gt;</code>, <code>&lt;occupation&gt;</code>, <code>&lt;mandates&gt;</code> and <code>&lt;chronList&gt;</code>. 
	  </xd:detail>
	  <xd:param name="date">The parameter "date" is the content of the element <code>&lt;date&gt;</code>.</xd:param>
	  <xd:param name="dateRange">The parameter "dateRange" is the content of the element <code>&lt;dateRange&gt;</code>.</xd:param>
	  <xd:param name="dateSet">The parameter "dateSet" is the content of the element <code>&lt;dateSet&gt;</code>.</xd:param>
	  <xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	  <xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="commonDates">
		<xsl:param name="date"/>
		<xsl:param name="dateRange"/>
		<xsl:param name="dateSet"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		
		<xsl:if test="($mode = 'showAll' and ($date or $dateRange or $dateSet))
					or ($mode = 'other' and ($date[@xml:lang = $translationLanguage]/text()
						or $dateRange[eac:fromDate[@xml:lang = $translationLanguage]]/text()
						or $dateRange[eac:toDate[@xml:lang = $translationLanguage]]/text()
						or $dateSet[eac:date[@xml:lang = $translationLanguage]]/text()
						or $dateSet[eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]]/text()
						or $dateSet[eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]]/text()))">
		    <div class="row">
		    	<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.date')"/><xsl:text>:</xsl:text></h2>
				</div>          
		        <div class="rightcolumn">
		        	<span class="nameEtryDates">
						<!-- when there are only 1 dateSet -->
						<xsl:if test="$dateSet and (($dateSet/eac:dateRange/eac:fromDate or $dateSet/eac:dateRange/eac:toDate) or ($dateSet/eac:date and $dateSet/eac:date/text()))">
							<xsl:apply-templates select="$dateSet">
				   				<xsl:with-param name="mode" select="$mode"/>
							</xsl:apply-templates>
						</xsl:if>
						<!-- when there are only 1 dateRange -->
						<xsl:if test="$dateRange and ($dateRange/eac:fromDate or $dateRange/eac:toDate)">
							<xsl:apply-templates select="$dateRange"/>
						</xsl:if>
						<!-- when there are only 1 date -->
						<xsl:if test="$date and $date/text()">
							<xsl:apply-templates select="$date"/>
						</xsl:if>
					</span>
				</div> 
			</div>
		</xsl:if>
		
		<xsl:if test="$mode = 'default' and ($date[@xml:lang = $langNode]/text()
						or $dateRange[eac:fromDate[@xml:lang = $langNode]]/text()
						or $dateRange[eac:toDate[@xml:lang = $langNode]]/text()
						or $dateSet[eac:date[@xml:lang = $langNode]]/text()
						or $dateSet[eac:dateRange[eac:fromDate[@xml:lang = $langNode]]]/text()
						or $dateSet[eac:dateRange[eac:toDate[@xml:lang = $langNode]]]/text())">
		    <div class="row">
		    	<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.date')"/><xsl:text>:</xsl:text></h2>
				</div>          
		        <div class="rightcolumn">
		        	<span class="nameEtryDates">
						<!-- when there are only 1 dateSet -->
						<xsl:if test="$dateSet and (($dateSet/eac:dateRange/eac:fromDate[@xml:lang = $langNode] or $dateSet/eac:dateRange/eac:toDate[@xml:lang = $langNode]) or ($dateSet/eac:date[@xml:lang = $langNode] and $dateSet/eac:date[@xml:lang = $langNode]/text()))">
							<xsl:apply-templates select="$dateSet">
				   				<xsl:with-param name="mode" select="$mode"/>
				   				<xsl:with-param name="langNode" select="$langNode"/>
							</xsl:apply-templates>
						</xsl:if>
						<!-- when there are only 1 dateRange -->
						<xsl:if test="$dateRange and ($dateRange/eac:fromDate[@xml:lang = $langNode] or $dateRange/eac:toDate[@xml:lang = $langNode])">
							<xsl:apply-templates select="$dateRange"/>
						</xsl:if>
						<!-- when there are only 1 date -->
						<xsl:if test="$date[@xml:lang = $langNode] and $date[@xml:lang = $langNode]/text()">
							<xsl:apply-templates select="$date"/>
						</xsl:if>
					</span>
				</div> 
			</div>
		</xsl:if>
		
		<xsl:if test="$mode = 'default' and $langNode='notLang' and ($date[not(@xml:lang)]/text()
						or $dateRange[eac:fromDate[not(@xml:lang)]]/text()
						or $dateRange[eac:toDate[not(@xml:lang)]]/text()
						or $dateSet[eac:date[not(@xml:lang)]]/text()
						or $dateSet[eac:dateRange[eac:fromDate[not(@xml:lang)]]]/text()
						or $dateSet[eac:dateRange[eac:toDate[not(@xml:lang)]]]/text())">
		    <div class="row">
		    	<div class="leftcolumn">
				   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.date')"/><xsl:text>:</xsl:text></h2>
				</div>          
		        <div class="rightcolumn">
		        	<span class="nameEtryDates">
						<!-- when there are only 1 dateSet -->
						<xsl:if test="$dateSet and (($dateSet/eac:dateRange/eac:fromDate[not(@xml:lang)] or $dateSet/eac:dateRange/eac:toDate[not(@xml:lang)]) or ($dateSet/eac:date[not(@xml:lang)] and $dateSet/eac:date[not(@xml:lang)]/text()))">
							<xsl:apply-templates select="$dateSet">
				   				<xsl:with-param name="mode" select="$mode"/>
				   				<xsl:with-param name="langNode" select="$langNode"/>
							</xsl:apply-templates>
						</xsl:if>
						<!-- when there are only 1 dateRange -->
						<xsl:if test="$dateRange and ($dateRange/eac:fromDate[not(@xml:lang)] or $dateRange/eac:toDate[not(@xml:lang)])">
							<xsl:apply-templates select="$dateRange"/>
						</xsl:if>
						<!-- when there are only 1 date -->
						<xsl:if test="$date[not(@xml:lang)] and $date[not(@xml:lang)]/text()">
							<xsl:apply-templates select="$date"/>
						</xsl:if>
					</span>
				</div> 
			</div>
		</xsl:if>
	</xsl:template>

	<!-- Template for toDate or fromDate to detect the different @localType's values -->
	<xd:doc>
	  <xd:short>Template for <code>&lt;fromDate&gt;</code> or <code>&lt;toDate&gt;</code>.</xd:short>
	  <xd:detail>Template for <code>&lt;fromDate&gt;</code> or <code>&lt;toDate&gt;</code> to detect the different @localType's values.
	  </xd:detail>
	  <xd:param name="dateUnknow">The parameter "dateUnknow" gets <code>&lt;fromDate&gt;</code> or <code>&lt;toDate&gt;</code>.</xd:param>
	</xd:doc>
	<xsl:template name="dateUnknow"> 
		<!-- dateUnknow gets fromDate or toDate -->
		<xsl:param name="dateUnknow"/>
	  	<xsl:choose>
	  		<!-- when it is void or it does not exist -->
	  		<xsl:when test="($dateUnknow='' or not($dateUnknow)) and $dateUnknow/parent::node()[@localType!='open' or not(@localType)]">
        		<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
        	</xsl:when>
        	<!-- unknownStart, unknownEnd and both -->
        	<xsl:otherwise>
        		<xsl:choose>
		        	<xsl:when test="name($dateUnknow) = 'fromDate'">
		        		<xsl:choose>
							<xsl:when test="$dateUnknow/parent::node()[@localType='unknown' or @localType='unknownStart']">
		        				<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="$dateUnknow" mode="other"/> 
							</xsl:otherwise>
						</xsl:choose>	
		        	</xsl:when>
	        		<xsl:when test="name($dateUnknow) = 'toDate'">
		        		<xsl:choose>
							<xsl:when test="$dateUnknow/parent::node()[@localType='unknown' or @localType='unknownEnd']">
	        					<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
							</xsl:when>
							<xsl:when test="$dateUnknow/parent::node()[@localType='open']">
		        				<xsl:text> </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="$dateUnknow" mode="other"/> 
							</xsl:otherwise>
						</xsl:choose>
        			</xsl:when>
					<xsl:otherwise>
	        			<xsl:apply-templates select="$dateUnknow" mode="other"/>
		        	</xsl:otherwise>
	        	</xsl:choose>
        	</xsl:otherwise>
		</xsl:choose> 
	</xsl:template>

	<!-- template for dateSet -->
	<xd:doc>
	  <xd:short>Template for <code>&lt;dateSet&gt;</code>.
	  </xd:short>
	  <xd:detail>
	  	Inside the <code>&lt;dateSet&gt;</code> element we can find the elements <code>&lt;dateRange&gt;</code> and <code>&lt;date&gt;</code>.
	  </xd:detail>
	  <xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	  <xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template match="eac:dateSet">
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<xsl:if test="eac:dateRange or eac:date">
			<!-- Checks if exists any content in the translation language. -->
			<xsl:if test="$mode = 'other' and (eac:date[@xml:lang = $translationLanguage] or eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]])">
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text> (</xsl:text>
				</xsl:if>
					<span class="nameEtryDates">
						<xsl:if test="eac:date[@xml:lang = $translationLanguage] and eac:date[@xml:lang = $translationLanguage]/text() and eac:date[@xml:lang = $translationLanguage]/text() != ''">
							<xsl:call-template name="multilanguageDate">
								<xsl:with-param name="list" select="eac:date[@xml:lang = $translationLanguage]"/>
							</xsl:call-template>
						</xsl:if>
						<xsl:if test="eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]
									and eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]/text()
									and eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]/text() != ''">
							<xsl:call-template name="multilanguageDateRange">
								<xsl:with-param name="list" select="eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage] or eac:toDate[@xml:lang = $translationLanguage]]"/>	
							</xsl:call-template>
						</xsl:if>
					</span>
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$mode = 'showAll'">
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text> (</xsl:text>
				</xsl:if>
					<span class="nameEtryDates">
						<xsl:call-template name="showAllDates">
							<xsl:with-param name="list" select="eac:date"/>
						</xsl:call-template>
						<xsl:if test="eac:dateRange/eac:fromDate or eac:dateRange/eac:toDate">
							<xsl:call-template name="showAllDateRanges">
								<xsl:with-param name="list" select="eac:dateRange"/>	
							</xsl:call-template>
						</xsl:if>
					</span>
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$mode = 'default'">
	<!-- 			<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text> (</xsl:text>
				</xsl:if>
					<span class="nameEtryDates">
						<xsl:call-template name="multilanguageDate">
							<xsl:with-param name="list" select="eac:date"/>
						</xsl:call-template>
						<xsl:if test="eac:dateRange/eac:fromDate or eac:dateRange/eac:toDate">
							<xsl:call-template name="multilanguageDateRange">
								<xsl:with-param name="list" select="eac:dateRange"/>	
							</xsl:call-template>
						</xsl:if>
					</span>
				<xsl:if test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
					<xsl:text>)</xsl:text>
				</xsl:if>-->
				<xsl:choose>
					<xsl:when test="name(eac:date/parent::node()) = 'existDates' or name(eac:date/parent::node()/parent::node()) = 'existDates'
							or name(eac:dateRange/parent::node()) = 'existDates' or name(eac:dateRange/parent::node()/parent::node()) = 'existDates'">
						<xsl:text> (</xsl:text>
						<span class="nameEtryDates">
							<xsl:call-template name="multilanguageDate">
								<xsl:with-param name="list" select="eac:date"/>
							</xsl:call-template>
							<xsl:if test="eac:dateRange/eac:fromDate or eac:dateRange/eac:toDate">
								<xsl:call-template name="multilanguageDateRange">
									<xsl:with-param name="list" select="eac:dateRange"/>	
								</xsl:call-template>
							</xsl:if>
						</span>
						<xsl:text>)</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<span class="nameEtryDates">
							<xsl:if test="eac:date[@xml:lang = $langNode] and eac:date[@xml:lang = $langNode]/text() and eac:date[@xml:lang = $langNode]/text() != ''">
								<xsl:call-template name="multilanguageDate">
									<xsl:with-param name="list" select="eac:date[@xml:lang = $langNode]"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="eac:dateRange[eac:fromDate[@xml:lang = $langNode] or eac:toDate[@xml:lang = $langNode]]
										and eac:dateRange[eac:fromDate[@xml:lang = $langNode] or eac:toDate[@xml:lang = $langNode]]/text()
										and eac:dateRange[eac:fromDate[@xml:lang = $langNode] or eac:toDate[@xml:lang = $langNode]]/text() != ''">
								<xsl:call-template name="multilanguageDateRange">
									<xsl:with-param name="list" select="eac:dateRange[eac:fromDate[@xml:lang = $langNode] or eac:toDate[@xml:lang = $langNode]]"/>	
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$langNode = 'notLang' and eac:date[not(@xml:lang)] and eac:date[not(@xml:lang)]/text() and eac:date[not(@xml:lang)]/text() != ''">
								<xsl:call-template name="multilanguageDate">
									<xsl:with-param name="list" select="eac:date[not(@xml:lang)]"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$langNode = 'notLang' and eac:dateRange[eac:fromDate[not(@xml:lang)] or eac:toDate[not(@xml:lang)]]
										and eac:dateRange[eac:fromDate[not(@xml:lang)] or eac:toDate[not(@xml:lang)]]/text()
										and eac:dateRange[eac:fromDate[not(@xml:lang)] or eac:toDate[not(@xml:lang)]]/text() != ''">
								<xsl:call-template name="multilanguageDateRange">
									<xsl:with-param name="list" select="eac:dateRange[eac:fromDate[not(@xml:lang)] or eac:toDate[not(@xml:lang)]]"/>	
								</xsl:call-template>
							</xsl:if>
						</span>
					</xsl:otherwise> 
				</xsl:choose>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- Template that will show all the dates in the passed list
		 independent of its language. -->
	
	<xd:doc>
	  <xd:short>
	  	Template that display all the dates.
	  </xd:short>
	  <xd:detail>Template that will show all the dates in the passed list independent of its language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of dates.</xd:param>
	</xd:doc>
	<xsl:template name="showAllDates">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
			<xsl:if test="current()/text()">
				<xsl:choose>
					<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
						<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
					</xsl:when>
					<xsl:when test="current()[@localType='open']">
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="." mode="other"/> 
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="position() != last() and current()[not(@localType ='open')]">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- Template that will show all the date ranges in the passed list
		 independent of its language. -->
	<xd:doc>
	  <xd:short>
	  	Template to display all the <code>&lt;dateRange&gt;</code>.
	  </xd:short>
	  <xd:detail>Template that will show all the <code>&lt;dateRange&gt;</code> elements in the passed list independent of its language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;dateRange&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="showAllDateRanges">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
			<xsl:choose>
				<xsl:when test="./eac:fromDate">
					<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:call-template name="fromToDate">
						<xsl:with-param name="dateRange" select="."/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="./eac:toDate">
					<xsl:if test="position() > 1  or (position() = 1 and ./parent::node()/eac:date/text())">
						<xsl:text>, </xsl:text>
					</xsl:if>
					<xsl:call-template name="fromToDate">
						<xsl:with-param name="dateRange" select="."/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<!-- template for multilanguageDate -->
	
	<xd:doc>
	  <xd:short>
	  	Template to display multilanguage <code>&lt;date&gt;</code>.
	  </xd:short>
	  <xd:detail>Template to display the <code>&lt;date&gt;</code> elements according with the language's rules.
	  </xd:detail>
	   <xd:param name="list">The parameter "list" is an array of date elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageDate">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<xsl:if test="current()/text()">
		    			       	<xsl:choose>
									<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
										<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
									</xsl:when>
									<xsl:when test="current()[@localType='open']">
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
									<xsl:text>, </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
							<xsl:if test="current()/text()">
		    			       	<xsl:choose>
									<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
										<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
									</xsl:when>
									<xsl:when test="current()[@localType='open']">
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
									<xsl:text>, </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="current()/text()">
   		    			       	<xsl:choose>
									<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
										<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
									</xsl:when>
									<xsl:when test="current()[@localType='open']">
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
									<xsl:text>, </xsl:text>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
								<xsl:if test="current()/text()">
	   		    			       	<xsl:choose>
										<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
											<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
										</xsl:when>
										<xsl:when test="current()[@localType='open']">
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
										<xsl:text>, </xsl:text>
									</xsl:if>
							    </xsl:if>
						   	</xsl:for-each> 
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:if test="current()/text()">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
								<xsl:if test="$currentLang = $language.first">
    		    			       	<xsl:choose>
										<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
											<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
										</xsl:when>
										<xsl:when test="current()[@localType='open']">
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								    <xsl:if test="position() != last() and current()[not(@localType ='open')]">
										<xsl:text>, </xsl:text>
									</xsl:if>
								</xsl:if>			
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
			       	<xsl:choose>
						<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
							<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
						</xsl:when>
						<xsl:when test="current()[@localType='open']">
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	

	<!-- template for multilanguageDateRange -->
	
	<xd:doc>
	  <xd:short>Template to display multilanguage <code>&lt;dateRange&gt;</code>. </xd:short>
	  <xd:detail>Template to display <code>&lt;dateRange&gt;</code> elements according with the language's rules.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;dateRange&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageDateRange">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="($list/eac:fromDate[@xml:lang = $language.selected] and $list/eac:fromDate[@xml:lang = $language.selected]/text() and $list/eac:fromDate[@xml:lang = $language.selected]/text() != '')
					                 or ($list/eac:toDate[@xml:lang = $language.selected] and $list/eac:toDate[@xml:lang = $language.selected]/text() and $list/eac:toDate[@xml:lang = $language.selected]/text() != '')">
						<xsl:for-each select="$list">
						    <xsl:variable name="currentLangFrom" select="current()/eac:fromDate/@xml:lang"/>
						    <xsl:variable name="currentLangTo" select="current()/eac:toDate/@xml:lang"/>
						    <xsl:choose>
						    	<xsl:when test="./eac:fromDate">
						    		<xsl:if test="($currentLangFrom = $language.selected)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:when test="./eac:toDate">
						    		<xsl:if test="($currentLangTo = $language.selected)">
						    			<xsl:if test="position() > 1  or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:otherwise/>
						    </xsl:choose>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="($list/eac:fromDate[@xml:lang = $lang.navigator] and $list/eac:fromDate[@xml:lang = $lang.navigator]/text() and $list/eac:fromDate[@xml:lang = $lang.navigator]/text() != '')
					                 or ($list/eac:toDate[@xml:lang = $lang.navigator] and $list/eac:toDate[@xml:lang = $lang.navigator]/text() and $list/eac:toDate[@xml:lang = $lang.navigator]/text() != '')">
						<xsl:for-each select="$list">
						    <xsl:variable name="currentLangFrom" select="current()/eac:fromDate/@xml:lang"/>
						    <xsl:variable name="currentLangTo" select="current()/eac:toDate/@xml:lang"/>
						    <xsl:choose>
						    	<xsl:when test="./eac:fromDate">
						    		<xsl:if test="($currentLangFrom = $lang.navigator)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:when test="./eac:toDate">
						    		<xsl:if test="($currentLangTo = $lang.navigator)">
						    			<xsl:if test="position() > 1  or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:otherwise/>
						    </xsl:choose>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="($list/eac:fromDate[@xml:lang = $language.default] and $list/eac:fromDate[@xml:lang = $language.default]/text() and $list/eac:fromDate[@xml:lang = $language.default]/text() != '') 
					                or ($list/eac:toDate[@xml:lang = $language.default] and $list/eac:toDate[@xml:lang = $language.default]/text() and $list/eac:toDate[@xml:lang = $language.default]/text() != '')">
						<xsl:for-each select="$list">
						    <xsl:variable name="currentLangFrom" select="current()/eac:fromDate/@xml:lang"/>
						    <xsl:variable name="currentLangTo" select="current()/eac:toDate/@xml:lang"/>
							<xsl:choose>
						    	<xsl:when test="./eac:fromDate">
						    		<xsl:if test="($currentLangFrom = $language.default)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:when test="./eac:toDate">
						    		<xsl:if test="($currentLangTo = $language.default)">
						    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
											<xsl:text>, </xsl:text>
										</xsl:if>
										<xsl:call-template name="fromToDate">
				          	 				<xsl:with-param name="dateRange" select="."/>
			                			</xsl:call-template>
									</xsl:if>
						    	</xsl:when>
						    	<xsl:otherwise/>
						    </xsl:choose>
						</xsl:for-each> 
					</xsl:when>
					<xsl:when test="($list/eac:fromDate[not(@xml:lang)] and $list/eac:fromDate[not(@xml:lang)]/text() and $list/eac:fromDate[not(@xml:lang)]/text() != '')
					                or ($list/eac:toDate[not(@xml:lang)] and $list/eac:toDate[not(@xml:lang)]/text() and $list/eac:toDate[not(@xml:lang)]/text() != '')">
							  	<xsl:for-each select="$list">
									<xsl:choose>
								    	<xsl:when test="./eac:fromDate">
								    		<xsl:if test="./eac:fromDate[not(@xml:lang)]">
								    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
													<xsl:text>, </xsl:text>
												</xsl:if>
												<xsl:call-template name="fromToDate">
						          	 				<xsl:with-param name="dateRange" select="."/>
					                			</xsl:call-template>
											</xsl:if>
								    	</xsl:when>
								    	<xsl:when test="./eac:toDate">
											<xsl:if test="./eac:toDate[not(@xml:lang)]">
								    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
													<xsl:text>, </xsl:text>
												</xsl:if>
												<xsl:call-template name="fromToDate">
						          	 				<xsl:with-param name="dateRange" select="."/>
					                			</xsl:call-template>
											</xsl:if>
								    	</xsl:when>
							    	<xsl:otherwise/>
							    </xsl:choose>
						     </xsl:for-each>
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/eac:fromDate/@xml:lang"/>
						<xsl:variable name="languageTo.first" select="$list[1]/eac:toDate/@xml:lang"/>
						<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/eac:fromDate/@xml:lang"/>
								<xsl:variable name="currentToLang" select="current()/eac:toDate/@xml:lang"/>
								<xsl:choose>
									<xsl:when test="$language.first">
										<xsl:choose>
										    	<xsl:when test="./eac:fromDate">
										    		<xsl:if test="$currentLang=$language.first">
										    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
										    	<xsl:when test="./eac:toDate">
										    		<xsl:if test="$currentToLang=$language.first">
										    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
									    	<xsl:otherwise/>
									    </xsl:choose>
									 </xsl:when>
									 <xsl:otherwise>
									 	<xsl:choose>
										    	<xsl:when test="./eac:fromDate">
										    		<xsl:if test="$currentLang=$languageTo.first">
										    			<xsl:if test="position() > 1">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
										    	<xsl:when test="./eac:toDate">
										    		<xsl:if test="$currentToLang=$languageTo.first">
										    			<xsl:if test="position() > 1 or (position() = 1 and ./parent::node()/eac:date/text())">
															<xsl:text>, </xsl:text>
														</xsl:if>
														<xsl:call-template name="fromToDate">
								          	 				<xsl:with-param name="dateRange" select="."/>
							                			</xsl:call-template>
													</xsl:if>
										    	</xsl:when>
									    	<xsl:otherwise/>
									    </xsl:choose>
									 </xsl:otherwise> 
							    </xsl:choose>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise> <!--only one dateRange-->
				<xsl:if test ="$list/parent::node()/eac:date/text()">
					<xsl:text>, </xsl:text>
				</xsl:if>
				<xsl:call-template name="fromToDate">
					<xsl:with-param name="dateRange" select="$list"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	
	
	<!--template fromDate toDate-->
	
	<xd:doc>
	  <xd:short>Template to <code>&lt;fromDate&gt;</code> <code>&lt;toDate&gt;</code>.</xd:short>
	  <xd:detail>Template to call the template "dateUnknow" with the parameter "dateUnknow" that contains the elements in <code>&lt;dateRange&gt;</code>.
	  </xd:detail>
	  <xd:param name="dateRange">The parameter "dateRange" is the content of the element <code>&lt;dateRange&gt;</code>.</xd:param>
	</xd:doc>
	<xsl:template name="fromToDate">
		<xsl:param name="dateRange"/>
		
		<xsl:call-template name="dateUnknow">
			<xsl:with-param name="dateUnknow" select="$dateRange/eac:fromDate"/>
		</xsl:call-template>
		<xsl:if test="($dateRange[@localType!='unknown']
							or $dateRange[not(@localType)])
							and ($dateRange/eac:toDate
								or $dateRange[@localType='unknownEnd']
				                or not($dateRange/eac:toDate) 
				                or not($dateRange/eac:fromDate)
				                or $dateRange[@localType='open'])"> 
		
			<xsl:text> - </xsl:text>
			<xsl:call-template name="dateUnknow">
				<xsl:with-param name="dateUnknow" select="$dateRange/eac:toDate"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- template for date -->
	
	<xd:doc>
	  <xd:short>Template for <code>&lt;date&gt;</code>.</xd:short>
	  <xd:detail>Template to display <code>&lt;date&gt;</code> elements.
	  </xd:detail>
	</xd:doc>
	<xsl:template match="eac:date">
		<xsl:if test="./text()">
			<xsl:if test="position() != last() and current()[not(@localType ='open')]">
				<xsl:text>, </xsl:text>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="current()[@localType='unknown' or @localType='unknownStart' or @localType='unknownEnd']">
					<xsl:value-of select="ape:resource('eaccpf.commons.dateUnknow')"/>
				</xsl:when>
				<xsl:when test="current()[@localType='open']">
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates mode="other"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<!-- template for dateRange -->
	
	<xd:doc>
	  <xd:short>Template to <code>&lt;dateRange&gt;</code>.</xd:short>
	  <xd:detail>Template to call the template "fromToDate" with the parameter "dateRange".
	  </xd:detail>
	  <xd:param name="dateRange">The parameter "dateRange" is the content of the element <code>&lt;dateRange&gt;</code>.</xd:param>
	</xd:doc>
	<xsl:template match="eac:dateRange">
			<xsl:call-template name="fromToDate">
				<xsl:with-param name="dateRange" select="."/>
			</xsl:call-template>
	</xsl:template>

	<!-- template for multilanguage only one element date in birth, death, foundation or closing-->
	
	<xd:doc>
	  <xd:short>Template for one date.</xd:short>
	  <xd:detail>Template to display only one <code>&lt;date&gt;</code> element in birth, death, foundation or closing according with the language's rules.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;date&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageOneDate">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
						<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						    <xsl:for-each select="$list[@xml:lang = $language.selected]">
								<xsl:if test="position()=1"> 
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="."/>
									</xsl:call-template>
							 	</xsl:if> 
					 	    </xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						    <xsl:for-each select="$list[@xml:lang = $lang.navigator]">
								<xsl:if test="position()=1"> 
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="."/>
									</xsl:call-template>
							 	</xsl:if> 
					 	    </xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						 	<xsl:for-each select="$list[@xml:lang = $language.default]"> 
								  <xsl:if test="position()=1">
									<xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="."/>
									</xsl:call-template>
								 </xsl:if>
						  	</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]"> 
							 	  <xsl:if test="position()=1"> 
									 <xsl:call-template name="dateUnknow">
										<xsl:with-param name="dateUnknow" select="."/>
									</xsl:call-template>
							  	 </xsl:if>
						   	</xsl:for-each> 
						</xsl:when>
						<xsl:otherwise> 
						 	<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
										<xsl:if test="$currentLang = $language.first">
											<xsl:if test="position() = 1">
												<xsl:call-template name="dateUnknow">
										           <xsl:with-param name="dateUnknow" select="."/>
												</xsl:call-template>
										  	</xsl:if>
										</xsl:if>
							</xsl:for-each>
					   </xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$list">
							<xsl:call-template name="dateUnknow">
								<xsl:with-param name="dateUnknow" select="."/>
							</xsl:call-template>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>

	<!-- Template to display the date of the entity in case the translation
		 selected will be a specific language.  -->
	
	<xd:doc>
	  <xd:short>Template to display the date of the entity.</xd:short>
	  <xd:detail>Template to display the entity's date in case the translation selected will be a specific language.
	  </xd:detail>
	  <xd:param name="dateElement">The parameter "dateElement" is the content of the element.</xd:param>
	  <xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>	
	</xd:doc>
	<xsl:template name="dateInitialDefault" >
		<xsl:param name="dateElement"/>
		<xsl:param name="entityType"/>

		<div class="row">
			<div class="leftcolumn">
				<h2>
					<xsl:value-of select="ape:resource('eaccpf.portal.date')"/>
		   			<xsl:text>:</xsl:text>
				</h2>	
			</div>
			<div class="rightcolumn">
				<xsl:if test="name($dateElement) = 'date'">
					<xsl:apply-templates select="$dateElement"/>
				</xsl:if> 
				<xsl:if test="name($dateElement) = 'dateSet' and $dateElement/eac:date">
					<xsl:variable name="datesList" select="$dateElement/eac:date[@xml:lang = $translationLanguage]"/>
					<xsl:apply-templates select="$datesList[1]"/>
				</xsl:if>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display the date of the entity in case the translation
		 selected will be "Translations" or "Show all".  -->
	
	<xd:doc>
	  <xd:short>Template to <code>&lt;existDates&gt;</code>.</xd:short>
	  <xd:detail>Template to display the entity's date in case the translation selected will be "Original display" or "Show all".
	  </xd:detail>
	  <xd:param name="existDates">The parameter "existDates" is the content of the element <code>&lt;existDates&gt;</code>.</xd:param>
	  <xd:param name="entityType">The parameter "entityType" is the <code>&lt;entityType&gt;</code> element.</xd:param>	
	</xd:doc>
	<xsl:template name="dateInitial" >
		<xsl:param name="existDates"/>
		<xsl:param name="entityType"/>

		<div class="row">
			<div class="leftcolumn">
				<h2>
					<xsl:value-of select="ape:resource('eaccpf.portal.date')"/>
		   			<xsl:text>:</xsl:text>
				</h2>	
			</div>
			<div class="rightcolumn">
		   		<xsl:if test="$existDates/eac:date/text()">
					<xsl:apply-templates select="$existDates/eac:date"/>
				</xsl:if>
		   		<xsl:if test="$existDates/eac:dateSet/eac:date/text()">
					<xsl:apply-templates select="$existDates/eac:dateSet/eac:date[1]"/>
				</xsl:if>
			</div>
		</div>
	</xsl:template>
	
	<!--Template to show the existDates's content -->
	
	<xd:doc>
	  <xd:short>Template to display the existDates's content.
	  </xd:short>
	  <xd:detail>
	  	Display only the values in <code>&lt;date&gt;</code>, <code>&lt;dateRange&gt;</code> and <code>&lt;dateSet&gt;</code>.
	  </xd:detail>
	</xd:doc>
	<xsl:template match="eac:existDates">
		<xsl:apply-templates select="eac:date | eac:dateRange | eac:dateSet" mode="existDates"/>
	</xsl:template>
</xsl:stylesheet>