<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns='http://www.w3.org/1999/xhtml' xmlns:eac="urn:isbn:1-931666-33-4"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
	xmlns:xd="http://www.pnp-software.com/XSLTdoc"
	exclude-result-prefixes="xlink xlink xsi eac ape xd">
	
	<!-- Other stylesheets. -->
	<xsl:import href="commons.xsl"/>
	<xsl:import href="dateOrDateRange.xsl"/>
	<xsl:import href="alternativeDescriptions.xsl"/>
	<xsl:import href="archivalMaterials.xsl"/>
	<xsl:import href="relatedNames.xsl"/>
	<xsl:import href="archives.xsl"/>
	
	<xd:doc type="stylesheet">
		<xd:short>Page for display the data from the eac-cpf file.</xd:short>
		<xd:detail>
  			With the following data:
			<ul>
				<li><span class="detailAttribute">Title</span> (Entity Icon, Name of the entity, Dates of use)
				</li><br/>
				<li><span class="detailAttribute">Date of birth</span> (If the entity is a person or family)</li><br/>
				<li><span class="detailAttribute">Date of death</span> (If the entity is a person or family)</li><br/>
				<li><span class="detailAttribute">Date of foundation</span> (If the entity is a corporate body)</li><br/>
				<li><span class="detailAttribute">Date of suppression</span> (If the entity is a corporate body)</li><br/>
				<li><span class="detailAttribute">Note</span></li><br/>
				<li><span class="detailAttribute">Alternative names</span>
				</li><br/>
				<li><b>PLACE</b>
					<ul><br/>
						<li><span class="detailAttribute">Place</span></li>
						<li><span class="detailAttribute">Role of the place</span></li>
						<li><span class="detailAttribute">Visitors address</span> (if the address[@localType="visitors address"]) <span class="detailAttribute">Postal address</span> (if the address[@localType="postal address"]) <span class="detailAttribute">Address</span> (if the address[@localType="other" or not(@localType)]) 
							<ul>
								<li><span class="detailAttribute">Street</span> (if the addressLine[@localType="street"])</li>
								<li><span class="detailAttribute">Postal code</span> (if the addressLine[@localType="postalcode"])</li>
								<li><span class="detailAttribute">Region / Autonomous authority</span> (if the addressLine[@localType="firstdem"])</li>
								<li><span class="detailAttribute">County / Local authority</span> (if the addressLine[@localType="secondem"])</li>
								<li><span class="detailAttribute">District</span> (if the addressLine[@localType="localentity"])</li>								
								<li><span class="detailAttribute">Address Information</span> (if the addressLine[@localType="other" or not(@localType)])</li>
								<li><span class="detailAttribute">Country</span> (if the addressLine[@localType="country"])</li>
							</ul>						
						</li>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>LOCAL DESCRIPTION</b>
					<ul><br/>
						<li><span class="detailAttribute">Local description</span></li>
						<li><span class="detailAttribute">Location</span></li>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>LEGAL STATUS</b>
					<ul><br/>
						<li><span class="detailAttribute">Legal status</span></li>
						<li><span class="detailAttribute">Location</span></li>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>FUNCTION</b>
					<ul><br/>
						<li><span class="detailAttribute">Function</span></li>
						<li><span class="detailAttribute">Location</span></li>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>OCCUPATION</b>
					<ul><br/>
						<li><span class="detailAttribute">Occupation</span></li>
						<li><span class="detailAttribute">Location</span></li>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>MANDATE</b>
					<ul><br/>
						<li><span class="detailAttribute">Mandate</span></li>
						<li><span class="detailAttribute">Location</span></li>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>GENEALOGY</b> (If the entity is a person)
					<ul><br/>
						<li><span class="detailAttribute">Genealogy</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>STRUCTURE / GENEALOGY</b> (If the entity is a family)
					<ul><br/>
						<li><span class="detailAttribute">Structure / genealogy</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>STRUCTURE</b> (If the entity is a corporate body)
					<ul><br/>
						<li><span class="detailAttribute">Structure</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>GENERAL CONTEXT</b>
					<ul><br/>
						<li><span class="detailAttribute">General Context</span></li>
						<li><span class="detailAttribute">Note</span></li>
					</ul>
				</li><br/>
				<li><b>BIOGRAPHY</b> (If the entity is a person or family)
					<ul><br/>
						<li><span class="detailAttribute">Summary</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Biography</span></li><br/>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Place</span></li>
						<li><span class="detailAttribute">Event</span></li>
					</ul>
				</li><br/>
				<li><b>HISTORICAL NOTE</b> (If the entity is a corporate body)
					<ul><br/>					
						<li><span class="detailAttribute">Summary</span></li>
						<li><span class="detailAttribute">Citation</span></li>
						<li><span class="detailAttribute">Historical note</span></li><br/>
						<li><span class="detailAttribute">Date</span></li>
						<li><span class="detailAttribute">Place</span></li>
						<li><span class="detailAttribute">Event</span></li>
					</ul>
				</li><br/>
				<li><b>Other information</b>
					<ul><br/>
						<li><span class="detailAttribute">APEIdentifier</span></li>
						<li><span class="detailAttribute">Other identifier</span></li>
						<li><span class="detailAttribute">Last update</span></li>
					</ul>
				</li><br/>
				<p>Information related to relations</p>
				<li><b>ALTERNATIVE DESCRIPTIONS</b></li><br/>
				<li><b>ARCHIVAL MATERIALS</b></li><br/>
				<li><b>RELATED NAMES</b></li><br/>
				<li><b>ARCHIVES</b></li><br/>
			</ul>
   </xd:detail>  
  </xd:doc>
  <xd:doc type="import">
 	<xd:short>Import the XML: <a href="commons.xsl.xd.html"><code>commons.xsl</code></a>, <a href="dateOrDateRange.xsl.xd.html"><code>dateOrDateRange.xsl</code></a>, <a href="alternativeDescriptions.xsl.xd.html"><code>alternativeDescriptions.xsl</code></a>, <a href="archivalMaterials.xsl.xd.html"><code>archivalMaterials.xsl</code></a>, <a href="relatedNames.xsl.xd.html"><code>relatedNames.xsl</code></a> and <a href="archives.xsl.xd.html"><code>archives.xsl</code></a>.
 	</xd:short>
	<xd:detail>
		commons.xsl common templates related to relations details.<br/>
		dateOrDateRange.xsl templates related to display the dates details.<br/>
		alternativeDescriptions.xsl templates to display information related to Alternative Description in relations part.<br/>
		archivalMaterials.xsl templates to display information related to Archival Materials in relations part.<br/>
		archives.xml templates to display information related to Archives in relations part.
	</xd:detail>    
  </xd:doc>	
  
	<xsl:output method="html" indent="yes" version="4.0"
		encoding="UTF-8"/>

	<!-- Global constants. -->
	<xd:doc>
		 <xd:short>The parameter with the secondDisplay url.</xd:short>    
	</xd:doc>
	<xsl:param name="eaccontent.extref.prefix"/>
	<xd:doc>
		 <xd:short>The parameter that controls the selected language.</xd:short>    
	</xd:doc>
	<xsl:param name="language.selected"/>
	<xd:doc>
		 <xd:short>The parameter that controls the navigator's language.</xd:short>    
	</xd:doc>
	<xsl:param name="lang.navigator"/>
	<xd:doc>
		 <xd:short>The parameter that controls the translation language in the translation combo.</xd:short>    
	</xd:doc>
	<xsl:param name="translationLanguage"/>
	<xd:doc>
		 <xd:short>The parameter that controls the institution url.</xd:short>    
	</xd:doc>
	<xsl:param name="aiCodeUrl"/>
	<xd:doc>
		 <xd:short>The parameter that controls the eac url.</xd:short>    
	</xd:doc>
	<xsl:param name="eacUrlBase"/>
	<xd:doc>
		 <xd:short>The parameter that controls the ead url.</xd:short>    
	</xd:doc>
	<xsl:param name="eadUrl"/>
    <xd:doc>
		 <xd:short>The parameter that controls the search term.</xd:short>    
	</xd:doc>
    <xsl:param name="searchTerms"/>

	<!-- Global variables. -->
	
	<xd:doc>
		 <xd:short>The variable that controls the default language.</xd:short>    
	</xd:doc>
	<xsl:variable name="language.default" select="'eng'"/>
	<xd:doc>
		 <xd:short>The variable that controls the lower-case letters.</xd:short>    
	</xd:doc>
	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
	<xd:doc>
		 <xd:short>The variable that controls the upper-case letters.</xd:short>    
	</xd:doc>
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />

	
	<xd:doc>
	  <xd:short>Template <code>/text()</code>.</xd:short>
	  <xd:detail>
	   Template that display the information and highlight the search term if it's in it. 
	  </xd:detail>
	</xd:doc>
	<xsl:template match="text()" mode="other">
		<xsl:value-of select="fn:normalize-space(ape:highlight(., 'other'))" disable-output-escaping="yes" />
	</xsl:template>
	
	<xd:doc>
	  <xd:short>Template <code>main</code>.</xd:short>
	  <xd:detail>
	   Template main that display the eac-cpf details in the different sections.
	  </xd:detail>
	</xd:doc>
	<xsl:template match="/">
        <xsl:variable name="existDates" select="/eac:eac-cpf/eac:cpfDescription/eac:description/eac:existDates"/>
        <xsl:variable name="entityType" select="/eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType"/>

		<!-- Set the variable which indicates the mode used to display the translations. -->
   		<xsl:variable name="translationMode">
    		<xsl:choose>
    			<xsl:when test="$translationLanguage = 'default'">
    				<xsl:value-of select="'default'"/>
    			</xsl:when>
    			<xsl:when test="$translationLanguage = 'showAll'">
    				<xsl:value-of select="'showAll'"/>
    			</xsl:when>
    			<xsl:otherwise>
		    		<xsl:value-of select="'other'"/>
    			</xsl:otherwise>
    		</xsl:choose>
   		</xsl:variable>
   		<xsl:variable name="iconType">
   			<xsl:choose>
	   			<xsl:when test="$entityType='person'">
				   <xsl:value-of select="'iconPerson'"/>
				</xsl:when>
				<xsl:when test="$entityType='corporateBody'">
				   <xsl:value-of select="'iconCorporateBody'"/>
				</xsl:when>
				<xsl:when test="$entityType='family'">
				   <xsl:value-of select="'iconFamily'"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="''"/>
				</xsl:otherwise>
			</xsl:choose>
   		</xsl:variable>
   		<xsl:variable name="location" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places"/>
   		
   		<!-- Set the variable which checks if at least one of the links in
				 "<citation>" element inside "<localDescriptions>" is a valid
				 one. -->
			<xsl:variable name="validHrefLinkPlaces">
				<xsl:choose>
					<xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places//eac:citation[@xlink:href != '']">
						<xsl:value-of select="ape:checkHrefValue(string-join(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places//eac:citation/@xlink:href, '_HREF_SEPARATOR_'))"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'false'"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
   		<xsl:variable name="langLocation">
		 	<xsl:variable name="lang" select="$location//@xml:lang"/>
		    <xsl:variable name="listLang" select="string-join($lang,'')"/>
		 	
		 	<xsl:variable name="conditionLangSelected" select="($location//eac:placeRole[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
						or $location//eac:placeEntry[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
						or $location//eac:date[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
						or $location//eac:fromDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != '' 
						or $location//eac:toDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
						or $location//eac:citation[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
						or ($location//eac:citation[@xlink:href != '' and @xml:lang=$language.selected]
							and $validHrefLinkPlaces = 'true')
						or $location//eac:citation[@xlink:title != '' and @xml:lang=$language.selected]
						or $location//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
						or $location//eac:addressLine[@xml:lang=$language.selected]/text() != '')"/>
		 	
		 	<xsl:variable name="conditionLangNavigator" select="($location//eac:placeRole[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
						or $location//eac:placeEntry[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
						or $location//eac:date[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
						or $location//eac:fromDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '' 
						or $location//eac:toDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
						or $location//eac:citation[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
						or ($location//eac:citation[@xlink:href != '' and @xml:lang=$lang.navigator]
							and $validHrefLinkPlaces = 'true')
						or $location//eac:citation[@xlink:title != '' and @xml:lang=$lang.navigator]
						or $location//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
						or $location//eac:addressLine[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
		 	
		 	<xsl:variable name="conditionLangDefault" select="($location//eac:placeRole[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
						or $location//eac:placeEntry[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
						or $location//eac:date[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
						or $location//eac:fromDate[@xml:lang=$language.default]/fn:normalize-space(text()) != '' 
						or $location//eac:toDate[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
						or $location//eac:citation[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
						or ($location//eac:citation[@xlink:href != '' and @xml:lang=$language.default]
							and $validHrefLinkPlaces = 'true')
						or $location//eac:citation[@xlink:title != '' and @xml:lang=$language.default]
						or $location//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
						or $location//eac:addressLine[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
						
		 	<xsl:variable name="conditionNotLang" select="($location//eac:placeRole[not(@xml:lang)]/fn:normalize-space(text()) != ''
						or $location//eac:placeEntry[not(@xml:lang)]/fn:normalize-space(text()) != ''
						or $location//eac:date[not(@xml:lang)]/fn:normalize-space(text()) != ''
						or $location//eac:fromDate[not(@xml:lang)]/fn:normalize-space(text()) != '' 
						or $location//eac:toDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
						or $location//eac:citation[not(@xml:lang)]/fn:normalize-space(text()) != ''
						or ($location//eac:citation[@xlink:href != '' and not(@xml:lang)]
							and $validHrefLinkPlaces = 'true')
						or $location//eac:citation[@xlink:title != '' and not(@xml:lang)]
						or $location//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != ''
						or $location//eac:addressLine[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
		 	
		 	 <xsl:choose>
		    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
		    	  <xsl:value-of select="$language.selected"/>
		    	</xsl:when>
		    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
		    		<xsl:value-of select="$lang.navigator"/>
		    	</xsl:when>
		    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
		    		<xsl:value-of select="$language.default"/>
		    	</xsl:when>
		    	<xsl:when test="$conditionNotLang">
		    		<xsl:value-of select="'notLang'"/>
		    	</xsl:when>
		    	<xsl:otherwise>
		    	  <xsl:value-of select="substring($listLang,0,4)"/>
		    	</xsl:otherwise>
		    </xsl:choose> 
		</xsl:variable>
			
		<h1 class="blockHeader {$iconType}">
		    <!-- nameEntry -->
			<!-- Checks if exists any content in the translation language. -->
			<xsl:choose>
				<xsl:when test="$translationMode = 'other' and //eac:nameEntry[eac:part[@xml:lang = $translationLanguage]] and //eac:nameEntry[eac:part[@xml:lang = $translationLanguage]/text()] and //eac:nameEntry[eac:part[@xml:lang = $translationLanguage]/text() != '']">
					<xsl:call-template name="namePriorisation">
						<xsl:with-param name="list" select="//eac:nameEntry[eac:part[@xml:lang = $translationLanguage]]"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="namePriorisation">
			       		 <xsl:with-param name="list" select="//eac:nameEntry"/>
				    </xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>

			<!-- dates -->
			<xsl:if test="$existDates/eac:date/text() or $existDates/eac:dateRange/eac:fromDate or $existDates/eac:dateRange/eac:toDate or $existDates/eac:dateSet/eac:date/text() or $existDates/eac:dateSet/eac:dateRange/eac:fromDate or $existDates/eac:dateSet/eac:dateRange/eac:toDate">
				<!-- when there are only 1 dateSet -->
				<xsl:if test="$existDates/eac:dateSet and (($existDates/eac:dateSet/eac:dateRange/eac:fromDate or $existDates/eac:dateSet/eac:dateRange/eac:toDate) or ($existDates/eac:dateSet/eac:date and $existDates/eac:dateSet/eac:date/text()))">
					<xsl:apply-templates select="$existDates/eac:dateSet">
						<xsl:with-param name="mode" select="$translationMode" />
						<xsl:with-param name="langNode" select="''"/>
					</xsl:apply-templates>
				</xsl:if>
				<!-- when there are only 1 dateRange -->
				<xsl:if test="$existDates/eac:dateRange and ($existDates/eac:dateRange/eac:fromDate or $existDates/eac:dateRange/eac:toDate)">
					<xsl:text> (</xsl:text>
						<span class="nameEtryDates">
							<xsl:apply-templates select="$existDates/eac:dateRange"/>
						</span>
					<xsl:text>)</xsl:text>
				</xsl:if>
				<!-- when there are only 1 date -->
				<xsl:if test="$existDates/eac:date and $existDates/eac:date/text()">
					<xsl:text> (</xsl:text>
						<span class="nameEtryDates">
							<xsl:apply-templates select="$existDates/eac:date"/>
						</span>
					<xsl:text>)</xsl:text>
				</xsl:if>
				<span class="existDates hidden">
					<xsl:apply-templates select="$existDates"/>
				</span>
			</xsl:if>
		<!--  	<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityType/text()">
				<xsl:text> </xsl:text>
				<span id="entityType">
					<xsl:if test="$entityType='person'">
					   <xsl:value-of select="ape:resource('eaccpf.portal.person')"/>
					</xsl:if>
					<xsl:if test="$entityType='corporateBody'">
					   <xsl:value-of select="ape:resource('eaccpf.portal.corporateBody')"/>
					</xsl:if>
					<xsl:if test="$entityType='family'">
					   <xsl:value-of select="ape:resource('eaccpf.portal.family')"/>
					</xsl:if>
				</span>
			</xsl:if>-->
		</h1>
		<div id="details">
			<!-- Dates -->
			<xsl:call-template name="dateDetails">
				<xsl:with-param name="existDates" select="$existDates" />
				<xsl:with-param name="mode" select="$translationMode" />
				<xsl:with-param name="entityType" select="$entityType" />
				<xsl:with-param name="langNode" select="$langLocation"/>
			</xsl:call-template>
			<!-- descriptiveNote of the existDates element. -->
			<xsl:call-template name="descriptiveNote">
				<xsl:with-param name="list" select="$existDates/eac:descriptiveNote/eac:p" />
				<xsl:with-param name="mode" select="$translationMode" />
	   			<xsl:with-param name="clazz" select="'dateDescription'"/>
				<xsl:with-param name="langNode" select="''"/>
			</xsl:call-template>

			<!-- alternative names -->
			<xsl:if test="count(//eac:nameEntry) > 1">
				<div class="row" id="titleAlternativeName">
					<div class="leftcolumn">
				   		<h2>
				   			<xsl:value-of select="ape:resource('eaccpf.portal.alternativeForms')"/>
				   			<xsl:text>:</xsl:text>
				   		</h2>
				   	</div>
				   	<div class="rightcolumn">
				   			<xsl:call-template name="alternativeNamePriorisation">
				   				<xsl:with-param name="list" select="//eac:nameEntry"/>
				   				<xsl:with-param name="clazz" select="'alternativeName'"/>
				   			</xsl:call-template>  
					</div>
				</div>
			</xsl:if>

			<!-- descriptive note inside identity -->
			<xsl:call-template name="descriptiveNote">
				<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:descriptiveNote/eac:p" />
				<xsl:with-param name="mode" select="$translationMode" />
	   			<xsl:with-param name="clazz" select="'identityDescription'"/>
				<xsl:with-param name="langNode" select="''"/>
			</xsl:call-template>

			<!-- location -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place">
				<h2 class="title expanded">
					<xsl:value-of select="translate(ape:resource('eaccpf.description.place'), $smallcase, $uppercase)"/>
				</h2>
				<div class="section">
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places"> 
						<div class="blockPlural">		
							<xsl:variable name="posParent" select="position()"/>
							<!-- placeEntry in place and other nodes-->
							    	<xsl:call-template name="placesPriorisation">
							    		<xsl:with-param name="list" select="./eac:place"/>
						   				<xsl:with-param name="posParent" select="$posParent"/>
						   				<xsl:with-param name="mode" select="$translationMode"/>
										<xsl:with-param name="validHrefLink" select="$validHrefLinkPlaces"/>
						    			<xsl:with-param name="langNode" select="$langLocation"/>
						    		</xsl:call-template> 
	
					     	<!-- descriptiveNote places-->
					     	<xsl:if test="./eac:descriptiveNote/eac:p/text()">
								 <xsl:call-template name="commonChild">
						    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
						    		<xsl:with-param name="clazz" select="'descriptiveNotelocationPlaces_'"/>
						   			<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
						   			<xsl:with-param name="posParent" select="$posParent"/>
				    				<xsl:with-param name="posChild" select="''"/>
				   					<xsl:with-param name="mode" select="$translationMode"/>
						    		<xsl:with-param name="title" select="'eaccpf.portal.place.note'"/>
			   						<xsl:with-param name="validHrefLink" select="''"/>
						    	 	<xsl:with-param name="langNode" select="$langLocation"/>
						    	 </xsl:call-template>
					    	 </xsl:if>
					    </div>
				    </xsl:for-each>
				</div>    
			</xsl:if>

			<!-- localDescription -->
			<!-- Set the variable which checks if at least one of the links in
				 "<citation>" element inside "<localDescriptions>" is a valid
				 one. -->
			<xsl:variable name="validHrefLinkLocalDescription">
				<xsl:choose>
					<xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:href != '']">
						<xsl:value-of select="ape:checkHrefValue(string-join(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation/@xlink:href, '_HREF_SEPARATOR_'))"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'false'"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<xsl:variable name="langLocalDescriptions">
			 	<xsl:variable name="lang" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:term[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:placeEntry[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:date[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:fromDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:toDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:href != '' and @xml:lang=$language.selected]
								and $validHrefLinkLocalDescription = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:title != '' and @xml:lang=$language.selected]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:term[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:placeEntry[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:date[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:fromDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:toDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:href != '' and @xml:lang=$lang.navigator]
								and $validHrefLinkLocalDescription = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:title != '' and @xml:lang=$lang.navigator]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:term[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:placeEntry[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:date[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:fromDate[@xml:lang=$language.default]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:toDate[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:href != '' and @xml:lang=$language.default]
								and $validHrefLinkLocalDescription = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:title != '' and @xml:lang=$language.default]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:term[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:placeEntry[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:date[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:fromDate[not(@xml:lang)]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:toDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:href != '' and not(@xml:lang)]
								and $validHrefLinkLocalDescription = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:citation[@xlink:title != '' and not(@xml:lang)]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>
			
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription
						and (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:term/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:placeEntry/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:dateSet/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:citation/text() != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:citation[@xlink:href != '']
								and $validHrefLinkLocalDescription = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:citation[@xlink:title != '']
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:localDescription/eac:descriptiveNote/eac:p/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions/eac:descriptiveNote/eac:p/text() != '')">
			    <h2 class="title expanded"><xsl:value-of select="translate(ape:resource('eaccpf.portal.localDescription'), $smallcase, $uppercase)"/></h2>
				<div class="section">
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:localDescriptions"> 
						<div class="blockPlural">
							<xsl:variable name="posParent" select="position()"/>
						    <xsl:for-each select="./eac:localDescription">
						    	<xsl:if test="./eac:term/text() != ''
						    				or ./eac:placeEntry/text() != ''
											or ./eac:date/text() != ''
											or ./eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:dateSet/eac:date/text() != ''
											or ./eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:citation/text() != ''
											or (./eac:citation[@xlink:href != ''] and $validHrefLinkLocalDescription = 'true')
											or ./eac:citation[@xlink:title != '']
						    				or ./eac:descriptiveNote/eac:p/text() != ''">
							    	<div class="blockSingular">
							    		<xsl:variable name="posChild" select="position()"/>
							    		
								     	<xsl:call-template name="term">
								    		<xsl:with-param name="list" select="./eac:term"/>
								    		<xsl:with-param name="clazz" select="'localDescription_'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.localDescription'"/>
								    		<xsl:with-param name="langNode" select="$langLocalDescriptions"/>
								    	</xsl:call-template> 
	
										<!-- placeEntry in localDescription -->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:placeEntry"/>
								    		<xsl:with-param name="clazz" select="'locationLocalDescription_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
							   				<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langLocalDescriptions"/>
								    	</xsl:call-template>
	
										<!-- dates in localDescription -->
										<xsl:call-template name="commonDates">
								    		<xsl:with-param name="date" select="./eac:date"/>
								    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
								    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="langNode" select="$langLocalDescriptions"/>
								    	</xsl:call-template>
	
										<!-- citation in localDescription -->
									  	<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:citation"/>
								    		<xsl:with-param name="clazz" select="'citationLocalDescription_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkLocalDescription"/>
								    		<xsl:with-param name="langNode" select="$langLocalDescriptions"/>
								    	</xsl:call-template>
	
										<!-- descriptiveNote in localDescription -->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
								    		<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescription_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langLocalDescriptions"/>
								    	</xsl:call-template>
								  	</div>  
							  	</xsl:if>	
							</xsl:for-each>
	
							<!-- descriptiveNote in localDescriptions -->
						 	<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLocalDescriptions_'"/>
					    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
			   					<xsl:with-param name="mode" select="$translationMode"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
								<xsl:with-param name="validHrefLink" select="''"/>
				    	 		<xsl:with-param name="langNode" select="$langLocalDescriptions"/>
				    	 	</xsl:call-template>
					   </div>
					</xsl:for-each>
				</div>	
		   </xsl:if>


			<!-- legalStatus -->
			<!-- Set the variable which checks if at least one of the links in
				 "<citation>" element inside "<legalStatuses>" is a valid one. -->
			<xsl:variable name="validHrefLinkLegalStatus">
				<xsl:choose>
					<xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:href != '']">
						<xsl:value-of select="ape:checkHrefValue(string-join(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation/@xlink:href, '_HREF_SEPARATOR_'))"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'false'"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<xsl:variable name="langLegalStatus">
			 	<xsl:variable name="langLegal" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//@xml:lang"/>
			    <xsl:variable name="listLangLegal" select="string-join($langLegal,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelectedLegal" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:term[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:placeEntry[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:date[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:fromDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
                            or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:toDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:href != '' and @xml:lang=$language.selected]
								and $validHrefLinkLegalStatus = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:title != '' and @xml:lang=$language.selected]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigatorLegal" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:term[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:placeEntry[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:date[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:fromDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
                            or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:toDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:href != '' and @xml:lang=$lang.navigator]
								and $validHrefLinkLegalStatus = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:title != '' and @xml:lang=$lang.navigator]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefaultLegal" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:term[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:placeEntry[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:date[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:fromDate[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
                            or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:toDate[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:href != '' and @xml:lang=$language.default]
								and $validHrefLinkLegalStatus = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:title != '' and @xml:lang=$language.default]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLangLegal" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:term[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:placeEntry[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:date[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:fromDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
                            or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:toDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:href != '' and not(@xml:lang)]
								and $validHrefLinkLegalStatus = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:citation[@xlink:title != '' and not(@xml:lang)]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLangLegal,$language.selected) and $conditionLangSelectedLegal">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLangLegal,$lang.navigator) and $conditionLangNavigatorLegal">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLangLegal,$language.default) and $conditionLangDefaultLegal">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLangLegal">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLangLegal,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>
			
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus
						and (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:term/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:placeEntry/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:dateSet/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:citation/text() != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:citation[@xlink:href != '']
								and $validHrefLinkLegalStatus = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:citation[@xlink:title != '']
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:legalStatus/eac:descriptiveNote/eac:p/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses/eac:descriptiveNote/eac:p/text() != '')">
			    <h2 class="title expanded"><xsl:value-of select="translate(ape:resource('eaccpf.portal.legalStatus'), $smallcase, $uppercase)"/></h2>
				<div class="section">
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:legalStatuses">
						<div class="blockPlural">
							<xsl:variable name="posParent" select="position()"/> 
						    <xsl:for-each select="./eac:legalStatus">
							    <xsl:if test="./eac:term/text() != ''
						    				or ./eac:placeEntry/text() != ''
											or ./eac:date/text() != ''
											or ./eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:dateSet/eac:date/text() != ''
											or ./eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:citation/text() != ''
											or (./eac:citation[@xlink:href != ''] and $validHrefLinkLegalStatus = 'true')
											or ./eac:citation[@xlink:title != '']
						    				or ./eac:descriptiveNote/eac:p/text() != ''">
							    	<div class="blockSingular">	
							    		<xsl:variable name="posChild" select="position()"/>
	
							    		<!-- term legalStatus -->
								    	<xsl:call-template name="term">
								    		<xsl:with-param name="list" select="./eac:term"/>
								    		<xsl:with-param name="clazz" select="'legalStatus_'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.legalStatus'"/>
								    		<xsl:with-param name="langNode" select="$langLegalStatus"/>
								    	</xsl:call-template>
	
										<!-- placeEntry in legalStatus -->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:placeEntry"/>
								    		<xsl:with-param name="clazz" select="'locationLegalStatus_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langLegalStatus"/>
								    	</xsl:call-template>
	
										<!-- dates in legalStatus -->
										<xsl:call-template name="commonDates">
								    		<xsl:with-param name="date" select="./eac:date"/>
								    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
								    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="langNode" select="$langLegalStatus"/>
								    	</xsl:call-template>
	
										<!-- citation in legalStatus -->
									  	<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:citation"/>
								    		<xsl:with-param name="clazz" select="'citationLegalStatus_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkLegalStatus"/>
								    		<xsl:with-param name="langNode" select="$langLegalStatus"/>
								    	</xsl:call-template>
	
										<!-- descriptiveNote in legalStatus -->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
								    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatus_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langLegalStatus"/>
								    	</xsl:call-template>
						    		</div>
						    	</xsl:if>
							</xsl:for-each>
	
							<!-- descriptiveNote in legalStatus -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteLegalStatuses_'"/>
					    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
			   					<xsl:with-param name="mode" select="$translationMode"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
								<xsl:with-param name="validHrefLink" select="''"/>
					    		<xsl:with-param name="langNode" select="$langLegalStatus"/>
					    	</xsl:call-template>
			    	 	</div>
					</xsl:for-each>
				</div>	
		   </xsl:if>


			<!-- function -->
			<!-- Set the variable which checks if at least one of the links in
				 "<citation>" element inside "<functions>" is a valid one. -->
			<xsl:variable name="validHrefLinkFunction">
				<xsl:choose>
					<xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:href != '']">
						<xsl:value-of select="ape:checkHrefValue(string-join(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation/@xlink:href, '_HREF_SEPARATOR_'))"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'false'"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
	       <xsl:variable name="langFunctions">
			 	<xsl:variable name="lang" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:term[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:placeEntry[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:date[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:fromDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:toDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:href != '' and @xml:lang=$language.selected]
								and $validHrefLinkFunction = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:title != '' and @xml:lang=$language.selected]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:term[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:placeEntry[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:date[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:fromDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:toDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:href != '' and @xml:lang=$lang.navigator]
								and $validHrefLinkFunction = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:title != '' and @xml:lang=$lang.navigator]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:term[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:placeEntry[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:date[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:fromDate[@xml:lang=$language.default]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:toDate[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:href != '' and @xml:lang=$language.default]
								and $validHrefLinkFunction = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:title != '' and @xml:lang=$language.default]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:term[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:placeEntry[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:date[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:fromDate[not(@xml:lang)]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:toDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:href != '' and not(@xml:lang)]
								and $validHrefLinkFunction = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:citation[@xlink:title != '' and not(@xml:lang)]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>

			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function
						and (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:term/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:placeEntry/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:date/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:dateRange[eac:fromDate or eac:toDate]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:dateSet/eac:date/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:citation/text() != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:citation[@xlink:href != '']
								and $validHrefLinkFunction = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:citation[@xlink:title != '']
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:function/eac:descriptiveNote/eac:p/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions/eac:descriptiveNote/eac:p/text() != '')">
			    <h2 class="title expanded"><xsl:value-of select="translate(ape:resource('eaccpf.portal.function'), $smallcase, $uppercase)"/></h2>
				<div class="section">
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:functions">
						<div class="blockPlural">
							<xsl:variable name="posParent" select="position()"/> 
						    <xsl:for-each select="./eac:function">
							    <xsl:if test="./eac:term/text() != ''
						    				or ./eac:placeEntry/text() != ''
											or ./eac:date/text() != ''
											or ./eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:dateSet/eac:date/text() != ''
											or ./eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:citation/text() != ''
											or (./eac:citation[@xlink:href != ''] and $validHrefLinkFunction = 'true')
											or ./eac:citation[@xlink:title != '']
						    				or ./eac:descriptiveNote/eac:p/text() != ''">
							    	<div class="blockSingular">	
							    		<xsl:variable name="posChild" select="position()"/>
	
							    		<!-- term function -->
								  		<xsl:call-template name="term">
								    		<xsl:with-param name="list" select="./eac:term"/>
								    		<xsl:with-param name="clazz" select="'function_'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.function'"/>
								    		<xsl:with-param name="langNode" select="$langFunctions"/>
								    	</xsl:call-template> 
	
										<!-- placeEntry in function-->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:placeEntry"/>
								    		<xsl:with-param name="clazz" select="'locationFunction_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langFunctions"/>
								    	</xsl:call-template>
	
										<!-- dates in function-->
										<xsl:call-template name="commonDates">
								    		<xsl:with-param name="date" select="./eac:date"/>
								    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
								    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="langNode" select="$langFunctions"/>
								    	</xsl:call-template>
	
										<!-- citation in function -->
									  	<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:citation"/>
								    		<xsl:with-param name="clazz" select="'citationFunction_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkFunction"/>
								    		<xsl:with-param name="langNode" select="$langFunctions"/>
								    	</xsl:call-template>
	
										<!-- descriptiveNote in function-->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
								    		<xsl:with-param name="clazz" select="'descriptiveNoteFunction_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langFunctions"/>
								    	</xsl:call-template>
								    </div>	
								</xsl:if>
							</xsl:for-each>
	
							<!-- descriptiveNote in functions -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteFunctions_'"/>
					    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
			   					<xsl:with-param name="mode" select="$translationMode"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
								<xsl:with-param name="validHrefLink" select="''"/>
					    		<xsl:with-param name="langNode" select="$langFunctions"/>
					    	</xsl:call-template>
				    	</div>
					</xsl:for-each>
				</div>
		    </xsl:if>

		    <!-- occupation -->
			<!-- Set the variable which checks if at least one of the links in
				 "<citation>" element inside "<occupations>" is a valid one. -->
			<xsl:variable name="validHrefLinkOccupation">
				<xsl:choose>
					<xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:href != '']">
						<xsl:value-of select="ape:checkHrefValue(string-join(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation/@xlink:href, '_HREF_SEPARATOR_'))"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'false'"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
		    
		    <xsl:variable name="langOccupations">
			 	<xsl:variable name="lang" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:term[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:placeEntry[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:date[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:fromDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:toDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:href != '' and @xml:lang=$language.selected]
								and $validHrefLinkOccupation = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:title != '' and @xml:lang=$language.selected]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:term[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:placeEntry[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:date[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:fromDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:toDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:href != '' and @xml:lang=$lang.navigator]
								and $validHrefLinkOccupation = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:title != '' and @xml:lang=$lang.navigator]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:term[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:placeEntry[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:date[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:fromDate[@xml:lang=$language.default]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:toDate[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:href != '' and @xml:lang=$language.default]
								and $validHrefLinkOccupation = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:title != '' and @xml:lang=$language.default]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:term[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:placeEntry[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:date[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:fromDate[not(@xml:lang)]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:toDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:href != '' and not(@xml:lang)]
								and $validHrefLinkOccupation = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:citation[@xlink:title != '' and not(@xml:lang)]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>
			
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation
						and (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:term/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:placeEntry/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:dateSet/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:citation/text() != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:citation[@xlink:href != '']
								and $validHrefLinkOccupation = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:citation[@xlink:title != '']
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:occupation/eac:descriptiveNote/eac:p/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations/eac:descriptiveNote/eac:p/text() != '')">
			    <h2 class="title expanded"><xsl:value-of select="translate(ape:resource('eaccpf.portal.occupation'), $smallcase, $uppercase)"/></h2>
				<div class="section">
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:occupations"> 
						<div class="blockPlural">
							<xsl:variable name="posParent" select="position()"/> 
						    <xsl:for-each select="./eac:occupation">
							    <xsl:if test="./eac:term/text() != ''
						    				or ./eac:placeEntry/text() != ''
											or ./eac:date/text() != ''
											or ./eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:dateSet/eac:date/text() != ''
											or ./eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:citation/text() != ''
											or (./eac:citation[@xlink:href != ''] and $validHrefLinkOccupation = 'true')
											or ./eac:citation[@xlink:title != '']
						    				or ./eac:descriptiveNote/eac:p/text() != ''">
							    	<div class="blockSingular">	
							    		<xsl:variable name="posChild" select="position()"/>
	
							    		<!-- term occupation- -->
								    	<xsl:call-template name="term">
								    		<xsl:with-param name="list" select="./eac:term"/>
								    		<xsl:with-param name="clazz" select="'occupation_'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.occupation'"/>
								    		<xsl:with-param name="langNode" select="$langOccupations"/>
								    	</xsl:call-template>
	
										<!-- placeEntry in occupation-->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:placeEntry"/>
								    		<xsl:with-param name="clazz" select="'locationOccupation_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langOccupations"/>
								    	</xsl:call-template>
	
										<!-- dates in occupation-->
										<xsl:call-template name="commonDates">
								    		<xsl:with-param name="date" select="./eac:date"/>
								    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
								    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="langNode" select="$langOccupations"/>
								    	</xsl:call-template>
	
										<!-- citation in occupation -->
									  	<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:citation"/>
								    		<xsl:with-param name="clazz" select="'citationOccupation_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkOccupation"/>
								    		<xsl:with-param name="langNode" select="$langOccupations"/>
								    	</xsl:call-template>
	
										<!-- descriptiveNote in occupation-->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
								    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupation_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
								    		<xsl:with-param name="langNode" select="$langOccupations"/>
								    	</xsl:call-template>
								    </div>
								</xsl:if>
							</xsl:for-each>
	
							<!-- descriptiveNote in occupation -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteOccupations_'"/>
					    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
			   					<xsl:with-param name="mode" select="$translationMode"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
	   							<xsl:with-param name="validHrefLink" select="''"/>
					    		<xsl:with-param name="langNode" select="$langOccupations"/>
					    	</xsl:call-template>
			    	 	</div>
					</xsl:for-each>
				</div>	
		    </xsl:if>

		    <!--mandates-->
			<!-- Set the variable which checks if at least one of the links in
				 "<citation>" element inside "<mandates>" is a valid one. -->
			<xsl:variable name="validHrefLinkMandate">
				<xsl:choose>
					<xsl:when test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:href != '']">
						<xsl:value-of select="ape:checkHrefValue(string-join(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation/@xlink:href, '_HREF_SEPARATOR_'))"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'false'"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<xsl:variable name="langMandates">
			 	<xsl:variable name="lang" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:term[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:placeEntry[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:date[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:fromDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:toDate[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:href != '' and @xml:lang=$language.selected]
								and $validHrefLinkMandate = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:title != '' and @xml:lang=$language.selected]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:term[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:placeEntry[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:date[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:fromDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:toDate[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:href != '' and @xml:lang=$lang.navigator]
								and $validHrefLinkMandate = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:title != '' and @xml:lang=$lang.navigator]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:term[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:placeEntry[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:date[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:fromDate[@xml:lang=$language.default]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:toDate[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:href != '' and @xml:lang=$language.default]
								and $validHrefLinkMandate = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:title != '' and @xml:lang=$language.default]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:term[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:placeEntry[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:date[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:fromDate[not(@xml:lang)]/fn:normalize-space(text()) != '' 
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:toDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:href != '' and not(@xml:lang)]
								and $validHrefLinkMandate = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:citation[@xlink:title != '' and not(@xml:lang)]
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>
			
		    <xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate
						and (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:term/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:placeEntry/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:dateSet/eac:date/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:citation/text() != ''
							or (./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:citation[@xlink:href != '']
								and $validHrefLinkMandate = 'true')
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:citation[@xlink:title != '']
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:mandate/eac:descriptiveNote/eac:p/text() != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates/eac:descriptiveNote/eac:p/text() != '')">
			    <h2 class="title expanded"><xsl:value-of select="translate(ape:resource('eaccpf.portal.mandate'), $smallcase, $uppercase)"/></h2>
				<div class="section">
					<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:mandates">
						<div class="blockPlural">
							<xsl:variable name="posParent" select="position()"/>  
						    <xsl:for-each select="./eac:mandate">
						    	<xsl:if test="./eac:term/text() != ''
						    				or ./eac:placeEntry/text() != ''
											or ./eac:date/text() != ''
											or ./eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:dateSet/eac:date/text() != ''
											or ./eac:dateSet/eac:dateRange[eac:fromDate or eac:toDate]/text() != ''
											or ./eac:citation/text() != ''
											or (./eac:citation[@xlink:href != ''] and $validHrefLinkMandate = 'true')
											or ./eac:citation[@xlink:title != '']
						    				or ./eac:descriptiveNote/eac:p/text() != ''">
							    	<div class="blockSingular">	
							    		<xsl:variable name="posChild" select="position()"/>
	
							    		<!-- term mandates -->
								    	<xsl:call-template name="term">
								    		<xsl:with-param name="list" select="./eac:term"/>
								    		<xsl:with-param name="clazz" select="'mandate_'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.mandate'"/>
								    		<xsl:with-param name="langNode" select="$langMandates"/>
								    	</xsl:call-template>
	
										<!-- placeEntry in mandates-->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:placeEntry"/>
								    		<xsl:with-param name="clazz" select="'locationMandate_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.location'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
			   								<xsl:with-param name="langNode" select="$langMandates"/>
								    	</xsl:call-template>
	
										<!-- dates in mandates-->
										<xsl:call-template name="commonDates">
								    		<xsl:with-param name="date" select="./eac:date"/>
								    		<xsl:with-param name="dateRange" select="./eac:dateRange"/>
								    		<xsl:with-param name="dateSet" select="./eac:dateSet"/>
							   				<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="langNode" select="$langMandates"/>
								    	</xsl:call-template>
	
										<!-- citation in mandates -->
									  	<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:citation"/>
								    		<xsl:with-param name="clazz" select="'citationMandate_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.citation'"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkMandate"/>
								    		<xsl:with-param name="langNode" select="$langMandates"/>
								    	</xsl:call-template>
	
										<!-- descriptiveNote in mandates-->
										<xsl:call-template name="commonChild">
								    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
								    		<xsl:with-param name="clazz" select="'descriptiveNoteMandate_'"/>
								    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								    		<xsl:with-param name="posParent" select="$posParent"/>
								    		<xsl:with-param name="posChild" select="$posChild"/>
						   					<xsl:with-param name="mode" select="$translationMode"/>
								    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
			   								<xsl:with-param name="validHrefLink" select="''"/>
			   								<xsl:with-param name="langNode" select="$langMandates"/>
								    	</xsl:call-template>
								    </div>
							    </xsl:if>
							</xsl:for-each>
	
							<!-- descriptiveNote in mandates -->
							<xsl:call-template name="commonChild">
					    		<xsl:with-param name="list" select="./eac:descriptiveNote/eac:p"/>
					    		<xsl:with-param name="clazz" select="'descriptiveNoteMandates_'"/>
					    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
					    		<xsl:with-param name="posParent" select="$posParent"/>
					    		<xsl:with-param name="posChild" select="''"/>
			   					<xsl:with-param name="mode" select="$translationMode"/>
					    		<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
	   							<xsl:with-param name="validHrefLink" select="''"/>
					    		<xsl:with-param name="langNode" select="$langMandates"/>
					    	</xsl:call-template>
				    	 </div>
					</xsl:for-each>
				</div>
		    </xsl:if>
			
			<!-- languagesUsed -->
			<xsl:variable name="languagesUsed">
			 	<xsl:variable name="lang" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:language[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:script[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:language[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:script[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:language[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:script[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:language[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:script[not(@xml:lang)]/fn:normalize-space(text()) != ''
							or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>
			
			<!--language -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:language/text()">
				<xsl:call-template name="languageDetails">
					<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:language"/>
					<xsl:with-param name="mode" select="$translationMode"/>
					<xsl:with-param name="langNode" select="$languagesUsed"/>
				</xsl:call-template>
			</xsl:if>

			<!-- script -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:script/text()">				
				<xsl:call-template name="scriptDetails">
					<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:languageUsed/eac:script"/>
					<xsl:with-param name="mode" select="$translationMode"/>
					<xsl:with-param name="langNode" select="$languagesUsed"/>
				</xsl:call-template>
			</xsl:if>

			<!--descriptive note inside languagesUsed -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:descriptiveNote/eac:p/text()">
				<xsl:call-template name="descriptiveNote">
					<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:languagesUsed/eac:descriptiveNote/eac:p" />
					<xsl:with-param name="mode" select="$translationMode" />
		   			<xsl:with-param name="clazz" select="'languagesUsedDescription'"/>
					<xsl:with-param name="langNode" select="$languagesUsed"/>
				</xsl:call-template>
			</xsl:if>

			<!-- structureOrGenealogy -->
			<xsl:variable name="langStructureOrGenealogy">
			 	<xsl:variable name="lang" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:p//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>
			
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:p/text() or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy/eac:outline/eac:level/eac:item/text()">
			    <xsl:call-template name="structureOrGenealogyDetails">
					<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:structureOrGenealogy" />
					<xsl:with-param name="mode" select="$translationMode" />
					<xsl:with-param name="entityType" select="$entityType" />
					<xsl:with-param name="langNode" select="$langStructureOrGenealogy"/>
				</xsl:call-template>
			</xsl:if>

			<!-- generalContext -->
			<xsl:variable name="langGeneralContext">
			 	<xsl:variable name="lang" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:p//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="(./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
			</xsl:variable>
			
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:p/text() or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext/eac:outline/eac:level/eac:item/text()">
 				<xsl:call-template name="generalContextDetails">
					<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:generalContext" />
					<xsl:with-param name="mode" select="$translationMode" />
					<xsl:with-param name="langNode" select="$langGeneralContext"/>
				</xsl:call-template>
			</xsl:if>

			<!-- biogHist -->
			<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist">
				 <xsl:call-template name="bioHistMultilanguage">
					<xsl:with-param name="bioHist" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:biogHist"/>
					<xsl:with-param name="entityType" select="$entityType"/>
					<xsl:with-param name="mode" select="$translationMode"/> 
				</xsl:call-template> 
			</xsl:if>
			
			<!-- provided by -->
	<!-- 	<xsl:if test="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName/text()">
				<div class="row">
						<div class="leftcolumn">
					   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.agencyName')"/><xsl:text>:</xsl:text></h2>
					   	</div>
					   	<div class="rightcolumn">
							<xsl:choose>
								<xsl:when test="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyCode/text()">
									<xsl:variable name="href" select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyCode"/>
									<xsl:if test="starts-with($href, 'http') or starts-with($href, 'https') or starts-with($href, 'ftp') or starts-with($href, 'www')">
										<a href="{$href}" target="_blank">
											<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
										</a>
							  		</xsl:if>
							  		<xsl:if test="not(starts-with($href, 'http')) and not(starts-with($href, 'https')) and not(starts-with($href, 'ftp')) and not(starts-with($href, 'www'))">
							  			<xsl:variable name="aiCode" select="ape:checkAICode($href, '', 'true')" />
							  			<xsl:choose>
							  				<xsl:when test="$aiCode != 'ERROR' and $aiCode != ''">
												<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCode)" />
												<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">
													<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
												</a>
							  				</xsl:when>
							  				<xsl:otherwise>
							  					<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
							  				</xsl:otherwise>
							  			</xsl:choose>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName" mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
						</div>
				</div>
			</xsl:if>-->

			<!-- other information -->
			<div id="footer">
				<h2 class="otherInformation"><xsl:value-of select="ape:resource('eaccpf.portal.otherInformation')"/></h2>
					<!-- identifier-->
				<xsl:if test="./eac:eac-cpf/eac:control/eac:recordId/text()">  	
					<div class="row grey">
						<div class="leftcolumn">
							<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.identifier')"/><xsl:text>:</xsl:text></h2>
						</div>
						<div class="rightcolumn"> 
							<span><xsl:apply-templates select="./eac:eac-cpf/eac:control/eac:recordId" mode="other"/></span>
						</div>
					</div>
				</xsl:if>
				<!-- other identifier -->
				<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityId/text()">  
					<div class="row grey">
						<div class="leftcolumn">
							<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.otherIdentifier')"/><xsl:text>:</xsl:text></h2>
						</div>
						<div class="rightcolumn moreDisplay" id="otherIdentifier"> 
							<xsl:for-each select="./eac:eac-cpf/eac:cpfDescription/eac:identity/eac:entityId">
								<xsl:if test="./text()">
								   		<xsl:call-template name="typeEntityId">
								   			<xsl:with-param name="entityId" select="."/>
								   		</xsl:call-template>
								 </xsl:if>  
							</xsl:for-each>
							<div class="linkMore">
								<a class="displayLinkShowMore linkShow" href="javascript:showMore('otherIdentifier', 'p');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
								</a>
								<a class="displayLinkShowLess linkShow" href="javascript:showLess('otherIdentifier', 'p');">
									<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
								</a>
							</div>
						</div>
					</div>
				</xsl:if>
				<!-- last update -->
				<xsl:if test="./eac:eac-cpf/eac:control/eac:maintenanceHistory/eac:maintenanceEvent/eac:eventDateTime/text()">     
					<div class="row grey">
						<div class="leftcolumn">
							<h2 class="grey"><xsl:value-of select="ape:resource('eaccpf.portal.eventDate')"/><xsl:text>:</xsl:text></h2>
						</div>
						<div class="rightcolumn"> 
							<xsl:variable name="lastMaintenance" select="./eac:eac-cpf/eac:control/eac:maintenanceHistory/eac:maintenanceEvent[last()]"/>		
							<span><xsl:apply-templates select="$lastMaintenance/eac:eventDateTime" mode="other"/></span>
						</div>
					</div>	
				</xsl:if>
			</div>
	</div>
		<!-- Relations boxes-->
		<div id="relations">
			<!-- Alternative descriptions. -->
			<xsl:if test="(./eac:eac-cpf/eac:cpfDescription/eac:alternativeSet/eac:setComponent/eac:componentEntry/text()
							and ./eac:eac-cpf/eac:cpfDescription/eac:alternativeSet/eac:setComponent/eac:componentEntry/text() != '')
						or ./eac:eac-cpf/eac:cpfDescription/eac:alternativeSet/eac:setComponent[@xlink:href != '']">
				<xsl:call-template name="alternativeDescriptions"/>
			</xsl:if>

			<!-- Archival materials. -->
			<xsl:if test="(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/text()
							and ./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/text() != '')
						or ./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation[@xlink:href != '']">
				<xsl:call-template name="archivalMaterials"/>
			</xsl:if>

			<!-- Related names. -->
			<xsl:if test="(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:cpfRelation/eac:relationEntry/text()
							and ./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:cpfRelation/eac:relationEntry/text() != '')
						or ./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:cpfRelation[@xlink:href != '']">
				<xsl:call-template name="relatedNames"/>
			</xsl:if>

			<!-- Archives. -->
			<xsl:if test="(./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/@localType='agencyName'
							and ./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry[@localType='agencyName']/text() != '')
						or (./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry/@localType='agencyCode'
							and ./eac:eac-cpf/eac:cpfDescription/eac:relations/eac:resourceRelation/eac:relationEntry[@localType='agencyCode']/text() != '')">
				<xsl:call-template name="archivesRelations"/>
			</xsl:if>	
		</div>
	</xsl:template>

	<!-- Template to display correctly the details of the "<existDates>"
		 element. -->
	<xd:doc>
	  <xd:short>Template to display <code>&lt;existDates&gt;</code>.</xd:short>
	  <xd:detail>Template to display correctly the details of the <code>&lt;existDates&gt;</code> 
	  according with the mode in the translation combo and the node's language.</xd:detail>
		<xd:param name="existDates">The parameter "existDates" is the content of the element <code>&lt;existDates&gt;</code> .</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="dateDetails">
		<xsl:param name="existDates"/>
		<xsl:param name="mode"/>
		<xsl:param name="entityType"/>
		<xsl:param name="langNode"/>
		
		<!-- dateRange fromDate -->
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other'
					and ($existDates/eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]/text()
						or $existDates/eac:dateSet[eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]]/text())">
			<xsl:if test="$existDates/eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]/text()">
				<xsl:call-template name="fromDateInitialDefault">
					<xsl:with-param name="dateElement" select="$existDates/eac:dateRange"/>
					<xsl:with-param name="entityType" select="$entityType"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$existDates/eac:dateSet[eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]]/text()">
				<xsl:call-template name="fromDateInitialDefault">
					<xsl:with-param name="dateElement" select="$existDates/eac:dateSet"/>
					<xsl:with-param name="entityType" select="$entityType"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>

		<!-- Only one element should be displayed so the modes 'default' and
			 'showAll' have the same actions. -->
		<xsl:if test="($mode = 'default' or $mode = 'showAll')
					and ($existDates/eac:dateRange/eac:fromDate/text()
						or $existDates/eac:dateSet/eac:dateRange/eac:fromDate/text())">
			<xsl:call-template name="fromDateInitial">
				<xsl:with-param name="existDates" select="$existDates"/>
				<xsl:with-param name="entityType" select="$entityType"/>
				<xsl:with-param name="langNode" select="$langNode"/>
				<xsl:with-param name="mode" select="$mode"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- dateRange toDate -->
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other'
					and ($existDates/eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]/text()
						or $existDates/eac:dateSet[eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]]/text())">
			<xsl:if test="$existDates/eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]/text()">
				<xsl:call-template name="toDateInitialDefault">
					<xsl:with-param name="dateElement" select="$existDates/eac:dateRange"/>
					<xsl:with-param name="entityType" select="$entityType"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$existDates/eac:dateSet[eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]]/text()">
				<xsl:call-template name="toDateInitialDefault">
					<xsl:with-param name="dateElement" select="$existDates/eac:dateSet"/>
					<xsl:with-param name="entityType" select="$entityType"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>

		<!-- Only one element should be displayed so the modes 'default' and
			 'showAll' have the same actions. -->
		<xsl:if test="($mode = 'default' or $mode = 'showAll')
					and ($existDates/eac:dateRange/eac:toDate/text()
						or $existDates/eac:dateSet/eac:dateRange/eac:toDate/text())">
			<xsl:call-template name="toDateInitial">
				<xsl:with-param name="existDates" select="$existDates"/>
				<xsl:with-param name="entityType" select="$entityType"/>
				<xsl:with-param name="langNode" select="$langNode"/>
				<xsl:with-param name="mode" select="$mode"/>
			</xsl:call-template>
		</xsl:if>

		<!-- Date -->
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other'
					and ($existDates/eac:date[@xml:lang = $translationLanguage]/text()
						or $existDates/eac:dateSet[eac:date[@xml:lang = $translationLanguage]]/text())
					and (not($existDates/eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]/text())
						and not($existDates/eac:dateSet[eac:dateRange[eac:fromDate[@xml:lang = $translationLanguage]]]/text())
						and not($existDates/eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]/text())
						and not($existDates/eac:dateSet[eac:dateRange[eac:toDate[@xml:lang = $translationLanguage]]]/text()))">
			<xsl:if test="$existDates/eac:date[@xml:lang = $translationLanguage]/text()">
				<xsl:call-template name="dateInitialDefault">
					<xsl:with-param name="dateElement" select="$existDates/eac:date"/>
					<xsl:with-param name="entityType" select="$entityType"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$existDates/eac:dateSet[eac:date[@xml:lang = $translationLanguage]]/text()">
				<xsl:call-template name="dateInitialDefault">
					<xsl:with-param name="dateElement" select="$existDates/eac:dateSet"/>
					<xsl:with-param name="entityType" select="$entityType"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>

		<!-- Only one element should be displayed so the modes 'default' and
			 'showAll' have the same actions. -->
		<xsl:if test="($mode = 'default' or $mode = 'showAll')
					and ($existDates/eac:date/text()
						or $existDates/eac:dateSet/eac:date/text())
					and (not($existDates/eac:dateRange/eac:fromDate/text())
						and not($existDates/eac:dateSet/eac:dateRange/eac:fromDate/text())
						and not($existDates/eac:dateRange/eac:toDate/text())
						and not($existDates/eac:dateSet/eac:dateRange/eac:toDate/text()))">
			<xsl:call-template name="dateInitial">
				<xsl:with-param name="existDates" select="$existDates"/>
				<xsl:with-param name="entityType" select="$entityType"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- Template to display the date of creation of the entity in case the
		 translation selected will be a specific language.  -->
	
	<xd:doc>
	  <xd:short>Template to <code>&lt;fromDate&gt;</code> with specific language</xd:short>
	  <xd:detail>Template related with the data  <code>&lt;fromDate&gt;</code> in case the translation selected will be a specific language.</xd:detail>
		<xd:param name="dateElement">The parameter "dateElement" is the date of the element.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
	</xd:doc>
	<xsl:template name="fromDateInitialDefault" >
		<xsl:param name="dateElement"/>
		<xsl:param name="entityType"/>

		<div class="row">
			<div class="leftcolumn">
				<h2>
					<xsl:if test="$entityType = 'person' or $entityType = 'family'">
						<xsl:value-of select="ape:resource('eaccpf.portal.birthDate')"/>
					</xsl:if>
					<xsl:if test="$entityType = 'corporateBody'">
						<xsl:value-of select="ape:resource('eaccpf.portal.foundationDate')"/>
					</xsl:if>	
					<xsl:text>:</xsl:text>
				</h2>	
			</div>
			<div class="rightcolumn">
				<p>
				<xsl:if test="name($dateElement) = 'dateRange'">
					<xsl:call-template name="dateUnknow">
						<xsl:with-param name="dateUnknow" select="$dateElement/eac:fromDate"/>
					</xsl:call-template>
				</xsl:if> 
				<xsl:if test="name($dateElement) = 'dateSet' and $dateElement/eac:dateRange/eac:fromDate">
					<xsl:call-template name="multilanguageOneDate">
						<xsl:with-param name="list" select="$dateElement/eac:dateRange/eac:fromDate[@xml:lang = $translationLanguage]"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="$entityType = 'person' or $entityType = 'family'">
					<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth' and @xml:lang=$translationLanguage]/text()">
				  		<xsl:text>, </xsl:text>
				  		<xsl:call-template name="multilanguagePlaceEntry">
				  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth' and @xml:lang=$translationLanguage]"/>
				  		</xsl:call-template>		
				    </xsl:if>
				</xsl:if>
				<xsl:if test="$entityType = 'corporateBody'">
					<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation' and @xml:lang=$translationLanguage]/text()">
				  		<xsl:text>, </xsl:text>
				  		<xsl:call-template name="multilanguagePlaceEntry">
				  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation' and @xml:lang=$translationLanguage]"/>
				  		</xsl:call-template>		
				    </xsl:if>
				</xsl:if>
				</p>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display the date of creation of the entity in case the
		 translation selected will be "Translations" or "Show all".  -->
	
	<xd:doc>
	<xd:short>Template to <code>&lt;fromDate&gt;</code> with "Original display" or "Show all"</xd:short>
	  <xd:detail>Template to display the date of creation of the entity in case the translation selected will be "Original display" or "Show all".</xd:detail>
		<xd:param name="existDates">The parameter "existDates" is the content of the element <code>existDates</code>.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	</xd:doc>
	<xsl:template name="fromDateInitial" >
		<xsl:param name="existDates"/>
		<xsl:param name="entityType"/>
		<xsl:param name="langNode"/>
		<xsl:param name="mode"/>
		
		<div class="row">
			<div class="leftcolumn">
				<h2>
					<xsl:if test="$entityType = 'person' or $entityType = 'family'">
						<xsl:value-of select="ape:resource('eaccpf.portal.birthDate')"/>
					</xsl:if>
					<xsl:if test="$entityType = 'corporateBody'">
						<xsl:value-of select="ape:resource('eaccpf.portal.foundationDate')"/>
					</xsl:if>	
					<xsl:text>:</xsl:text>
				</h2>	
			</div>
			<div class="rightcolumn">
				<p>
					<xsl:if test="$existDates/eac:dateRange">
						<xsl:call-template name="dateUnknow">
							<xsl:with-param name="dateUnknow" select="$existDates/eac:dateRange/eac:fromDate"/>
						</xsl:call-template>
					</xsl:if> 
					<xsl:if test="$existDates/eac:dateSet/eac:dateRange/eac:fromDate">
						<xsl:call-template name="multilanguageOneDate">
							<xsl:with-param name="list" select="$existDates/eac:dateSet/eac:dateRange/eac:fromDate"/>
						</xsl:call-template>	
					</xsl:if>
					<xsl:if test="($entityType = 'person' or $entityType = 'family') and $mode='showAll'">
						<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']/text()">
					  		<xsl:text>, </xsl:text>
					  		<xsl:call-template name="multilanguagePlaceEntry">
					  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']"/>
					  		</xsl:call-template>		
					    </xsl:if>
					</xsl:if>
					<xsl:if test="$entityType = 'corporateBody' and $mode='showAll'">
						<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']/text()">
					  		<xsl:text>, </xsl:text>
					  		<xsl:call-template name="multilanguagePlaceEntry">
					  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']"/>
					  		</xsl:call-template>		
					    </xsl:if>
					</xsl:if>
					<xsl:if test="($entityType = 'person' or $entityType = 'family') and $mode='default'">
						<xsl:if test="($langNode='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth'][not(@xml:lang)]/text()) 
									 or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth'][@xml:lang=$langNode]/text() 
					                 or ($searchTerms!='' and 
					                 	(
						                 	./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth'][@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
							      		 	or (
							      		 		$langNode!='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth'][not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
						      		 		)
						      		 	)
						      		 )
								">
								<xsl:text>, </xsl:text>
								<xsl:call-template name="showDetailsPlaceEntry">
									<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='birth']"/>
									<xsl:with-param name="langNode" select="$langNode"/>
								</xsl:call-template>
						
						</xsl:if>
					</xsl:if>
					<xsl:if test="$entityType = 'corporateBody' and $mode='default'">
						<xsl:if test="($langNode='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation'][not(@xml:lang)]/text()) 
									 or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation'][@xml:lang=$langNode]/text() 
					                 or ($searchTerms!='' and 
					                 	(
						                 	./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation'][@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
							      		 	or (
							      		 		$langNode!='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation'][not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
						      		 		)
						      		 	)
						      		 )
								">
								<xsl:text>, </xsl:text>
								<xsl:call-template name="showDetailsPlaceEntry">
									<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='foundation']"/>
									<xsl:with-param name="langNode" select="$langNode"/>
								</xsl:call-template>
						
						</xsl:if>
					</xsl:if>
				</p>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display the date of closure of the entity in case the
		 translation selected will be a specific language.  -->
	
	<xd:doc>
	  <xd:short>Template to <code>&lt;toDate&gt;</code> with a specific language.</xd:short>
	  <xd:detail>Template to display the date of closure of the entity in case the translation selected will be a specific language.</xd:detail>
		<xd:param name="dateElement">The parameter "dateElement" is the element related with dates.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
	</xd:doc>
	<xsl:template name="toDateInitialDefault" >
		<xsl:param name="dateElement"/>
		<xsl:param name="entityType"/>

		<div class="row">
			<div class="leftcolumn">
				<h2>
					<xsl:if test="$entityType = 'person' or $entityType = 'family'">
						<xsl:value-of select="ape:resource('eaccpf.portal.deathDate')"/>
					</xsl:if>
					<xsl:if test="$entityType = 'corporateBody'">
						<xsl:value-of select="ape:resource('eaccpf.portal.closingDate')"/>
					</xsl:if>	
					<xsl:text>:</xsl:text>
				</h2>	
			</div>
			<div class="rightcolumn">
				<p>
				<xsl:if test="name($dateElement) = 'dateRange'">
					<xsl:call-template name="dateUnknow">
						<xsl:with-param name="dateUnknow" select="$dateElement/eac:toDate"/>
					</xsl:call-template>
				</xsl:if> 
				<xsl:if test="name($dateElement) = 'dateSet' and $dateElement/eac:dateRange/eac:toDate">
					<xsl:call-template name="multilanguageOneDate">
						<xsl:with-param name="list" select="$dateElement/eac:dateRange/eac:toDate[@xml:lang = $translationLanguage]"/>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="$entityType = 'person' or $entityType = 'family'">
					<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death' and @xml:lang=$translationLanguage]/text()">
				  		<xsl:text>, </xsl:text>
				  		<xsl:call-template name="multilanguagePlaceEntry">
				  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death' and @xml:lang=$translationLanguage]"/>
				  		</xsl:call-template>		
				    </xsl:if>
				</xsl:if>
				<xsl:if test="$entityType = 'corporateBody'">
					<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression' and @xml:lang=$translationLanguage]/text()">
				  		<xsl:text>, </xsl:text>
				  		<xsl:call-template name="multilanguagePlaceEntry">
				  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression' and @xml:lang=$translationLanguage]"/>
				  		</xsl:call-template>		
				    </xsl:if>
				</xsl:if>
				</p>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display the date of closure of the entity in case the
		 translation selected will be "Translations" or "Show all".  -->
	<xd:doc>
		<xd:short>Template to <code>&lt;toDate&gt;</code> with "Original display" or "Show all".</xd:short>
	  	<xd:detail>Template to display the date of closure of the entity in case the translation selected will be "Original display" or "Show all".</xd:detail>
		<xd:param name="existDates">The parameter "existDates" is the element <code>existDates</code>.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	</xd:doc>
	<xsl:template name="toDateInitial" >
		<xsl:param name="existDates"/>
		<xsl:param name="entityType"/>
		<xsl:param name="langNode"/>
		<xsl:param name="mode"/>
		
		<div class="row">
			<div class="leftcolumn">
				<h2>
					<xsl:if test="$entityType = 'person' or $entityType = 'family'">
						<xsl:value-of select="ape:resource('eaccpf.portal.deathDate')"/>
					</xsl:if>
					<xsl:if test="$entityType = 'corporateBody'">
						<xsl:value-of select="ape:resource('eaccpf.portal.closingDate')"/>
					</xsl:if>	
					<xsl:text>:</xsl:text>
				</h2>	
			</div>
			<div class="rightcolumn">
				<p>
				<xsl:if test="$existDates/eac:dateRange">
					<xsl:call-template name="dateUnknow">
						<xsl:with-param name="dateUnknow" select="$existDates/eac:dateRange/eac:toDate"/>
					</xsl:call-template>
				</xsl:if> 
				<xsl:if test="$existDates/eac:dateSet/eac:dateRange/eac:toDate">
					<xsl:call-template name="multilanguageOneDate">
						<xsl:with-param name="list" select="$existDates/eac:dateSet/eac:dateRange/eac:toDate"/>
					</xsl:call-template>	
				</xsl:if>
				<xsl:if test="($entityType = 'person' or $entityType = 'family') and $mode='showAll'">
					<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']/text()">
				  		<xsl:text>, </xsl:text>
				  		<xsl:call-template name="multilanguagePlaceEntry">
				  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']"/>
				  		</xsl:call-template>		
				    </xsl:if>
				</xsl:if>
				<xsl:if test="$entityType = 'corporateBody' and $mode='showAll'">
					<xsl:if test="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']/text()">
				  		<xsl:text>, </xsl:text>
				  		<xsl:call-template name="multilanguagePlaceEntry">
				  			<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']"/>
				  		</xsl:call-template>		
				    </xsl:if>
				</xsl:if>
				<xsl:if test="($entityType = 'person' or $entityType = 'family') and $mode='default'">
						<xsl:if test="($langNode='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death'][not(@xml:lang)]/text()) 
									 or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death'][@xml:lang=$langNode]/text() 
					                 or ($searchTerms!='' and 
					                 	(
						                 	./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death'][@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
							      		 	or (
							      		 		$langNode!='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death'][not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
						      		 		)
						      		 	)
						      		 )
								">
								<xsl:text>, </xsl:text>
								<xsl:call-template name="showDetailsPlaceEntry">
									<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='death']"/>
									<xsl:with-param name="langNode" select="$langNode"/>
								</xsl:call-template>
						</xsl:if>
					</xsl:if>
					<xsl:if test="$entityType = 'corporateBody' and $mode='default'">
						<xsl:if test="($langNode='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression'][not(@xml:lang)]/text()) 
									 or ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression'][@xml:lang=$langNode]/text() 
					                 or ($searchTerms!='' and 
					                 	(
						                 	./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression'][@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
							      		 	or (
							      		 		$langNode!='notLang' and ./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression'][not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
						      		 		)
						      		 	)
						      		 )
								">
								<xsl:text>, </xsl:text>
								<xsl:call-template name="showDetailsPlaceEntry">
									<xsl:with-param name="list" select="./eac:eac-cpf/eac:cpfDescription/eac:description/eac:places/eac:place/eac:placeEntry[@localType='suppression']"/>
									<xsl:with-param name="langNode" select="$langNode"/>
								</xsl:call-template>
						</xsl:if>
					</xsl:if>
				</p>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display correctly the details of the "<descriptiveNote>"
		 element inside other elements. -->
	
	<xd:doc>
		<xd:short>Template to <code>&lt;descriptiveNote&gt;</code>.</xd:short>
	    <xd:detail>Template to display correctly the details  <code>&lt;descriptiveNote&gt;</code> element inside other elements.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;p&gt;</code> elements in <code>&lt;descriptiveNote&gt;</code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="descriptiveNote">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="clazz"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
			   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
			   			<xsl:with-param name="clazz" select="$clazz"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/text()">
			<div class="row">
				<div class="leftcolumn">
			   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="$clazz"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
		<xsl:if test="$mode = 'default' and $langNode='' and $list/text()">
			<div class="row">
				<div class="leftcolumn">
			   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="multilanguage">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="$clazz"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>
	
		<xsl:if test="$mode = 'default' and $langNode!='' and(
			(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
				 or $list[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			
			<div class="row">
				<div class="leftcolumn">
			   		<h2><xsl:value-of select="ape:resource('eaccpf.portal.note')"/><xsl:text>:</xsl:text></h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showDetailsDescriptiveNote">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="$clazz"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   			<xsl:with-param name="langNode" select="$langNode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>	
	</xsl:template>

	<!-- Template to display correctly the details of the "<language>"
		 element inside "<languageUsed>" element. -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;language&gt;</code>.</xd:short>
	  	<xd:detail>Template to display correctly the details  <code>&lt;language&gt;</code> element inside <code>&lt;languageUsed&gt;</code> element.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;language&gt;</code> elements.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="languageDetails">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.languagesUsed')"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
			   			<xsl:with-param name="clazz" select="'language'"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.languagesUsed')"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'language'"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
		
		<xsl:if test="$mode = 'default' and (
			(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
				 or $list[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.languagesUsed')"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
				 	<xsl:call-template name="showDetailsDescriptiveNote">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'language'"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   			<xsl:with-param name="langNode" select="$langNode"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   		</xsl:call-template>
				</div>
			</div>	
		</xsl:if>	
	</xsl:template>

	<!-- Template to display correctly the details of the "<script>"
		 element inside "<languageUsed>" element. -->
	
	<xd:doc>
		<xd:short>Template to <code>&lt;script&gt;</code>.</xd:short>
	  	<xd:detail>Template to display correctly the details  <code>&lt;script&gt;</code> element inside <code>&lt;languageUsed&gt;</code> element.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>script</code> elements.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="scriptDetails">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.scriptUsed')"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
			   			<xsl:with-param name="clazz" select="'script'"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.scriptUsed')"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'script'"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
		
		<xsl:if test="$mode = 'default' and (
			(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
				 or $list[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.scriptUsed')"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showDetailsDescriptiveNote">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'language'"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   			<xsl:with-param name="langNode" select="$langNode"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>
	</xsl:template>

	<!-- Template to display correctly the content of the "<structureOrGenealogy>"
		 element. -->
	
	<xd:doc>
		<xd:short>Template to <code>&lt;structureOrGenealogy&gt;</code>.</xd:short>
	  	<xd:detail>Template to display correctly the content of the <code>&lt;structureOrGenealogy&gt;</code> element.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;structureOrGenealogy&gt;</code> elements.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="structureOrGenealogyDetails">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="entityType"/>
		<xsl:param name="langNode"/>
		
		<xsl:if test="(($mode = 'default' or $mode = 'showAll') and ($list/eac:p/text() or $list/eac:outline/eac:level/eac:item/text()))
					or ($mode = 'other' and ($list/eac:p[@xml:lang = $translationLanguage]/text() or $list/eac:outline/eac:level/eac:item[@xml:lang = $translationLanguage]/text()))">
			<xsl:if test="$entityType='person'">
				<h2 class="title expanded">
					<xsl:value-of select="translate(ape:resource('eaccpf.portal.genealogy'), $smallcase, $uppercase)"/>
				</h2>
			</xsl:if>
			<xsl:if test="$entityType='corporateBody'">
				<h2 class="title expanded">
					<xsl:value-of select="translate(ape:resource('eaccpf.portal.structure'), $smallcase, $uppercase)"/>
				</h2>
			</xsl:if>
			<xsl:if test="$entityType='family'">
				<h2 class="title expanded">
					<xsl:value-of select="translate(ape:resource('eaccpf.portal.structureOrGenealogy'), $smallcase, $uppercase)"/>
				</h2>
			</xsl:if>
		 	<div class="section genealogy"> 
		 		<xsl:for-each select="$list">
		 			<xsl:variable name="pos" select="position()"/>
					<div class="blockPlural">
						<xsl:variable name="firstchild" select="./*[1]"/>
						<xsl:if test="name($firstchild)='outline'">
							<xsl:call-template name="outlineStructure">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'structureOrGenealogy_'"/>
								<xsl:with-param name="entityType" select="$entityType"/>
								<xsl:with-param name="mode" select="$mode"/>
								<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
			
							<xsl:call-template name="p">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'structureOrGenealogyNote_'"/>
								<xsl:with-param name="mode" select="$mode"/>
								<xsl:with-param name="langNode" select="$langNode"/>
								<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
						</xsl:if>
			
						<xsl:if test="name($firstchild)='p'">
							<xsl:call-template name="p">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'structureOrGenealogyNote'"/>
								<xsl:with-param name="mode" select="$mode"/>
								<xsl:with-param name="langNode" select="$langNode"/>
								<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
			
							<xsl:call-template name="outlineStructure">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'structureOrGenealogy'"/>
								<xsl:with-param name="entityType" select="$entityType"/>
								<xsl:with-param name="mode" select="$mode"/>
							 	<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
						</xsl:if>
					</div>	
				</xsl:for-each>
		 	</div> 
		</xsl:if>
	</xsl:template>

	<!-- Template to display correctly the content of the "<generalContext>"
		 element. -->
	
	<xd:doc>
		<xd:short>Template to <code>&lt;generalContext&gt;</code>.</xd:short>
	  <xd:detail>Template to display correctly the content of the <code>&lt;generalContext&gt;</code> element.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;generalContext&gt;</code> elements.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="generalContextDetails">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<xsl:if test="(($mode = 'default' or $mode = 'showAll') and ($list/eac:p/text() or $list/eac:outline/eac:level/eac:item/text()))
					or ($mode = 'other' and ($list/eac:p[@xml:lang = $translationLanguage]/text() or $list/eac:outline/eac:level/eac:item[@xml:lang = $translationLanguage]/text()))">
			<h2 class="title expanded">
				<xsl:value-of select="translate(ape:resource('eaccpf.portal.generalContext'), $smallcase, $uppercase)"/>
			</h2>
			<div class="section genealogy">
				<xsl:for-each select="$list">
					<div class="blockPlural">
						<xsl:variable name="pos" select="position()"/>
						<xsl:variable name="firstchild" select="./*[1]"/>
						<xsl:if test="name($firstchild)='outline'">
							<xsl:call-template name="outline">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'generalContext'"/>
								<xsl:with-param name="title" select="'eaccpf.portal.generalContext'"/>
								<xsl:with-param name="mode" select="$mode" />
								<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
			
							<xsl:call-template name="p">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'generalContextNote'"/>
								<xsl:with-param name="mode" select="$mode" />
								<xsl:with-param name="langNode" select="$langNode"/>
								<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
						</xsl:if>
			
						<xsl:if test="name($firstchild)='p'">
							<xsl:call-template name="p">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'generalContextNote'"/>
								<xsl:with-param name="mode" select="$mode" />
								<xsl:with-param name="langNode" select="$langNode"/>
								<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
			
							<xsl:call-template name="outline">
								<xsl:with-param name="list" select="."/>
								<xsl:with-param name="clazz" select="'generalContext'"/>
								<xsl:with-param name="title" select="'eaccpf.portal.generalContext'"/>
								<xsl:with-param name="mode" select="$mode" />
								<xsl:with-param name="pos" select="$pos"/>
							</xsl:call-template>
						</xsl:if>
					</div>	
				</xsl:for-each>
			</div>	
		</xsl:if>
	</xsl:template>

	<xd:doc>
		<xd:short>Template to <code>&lt;biogHist&gt;</code>.</xd:short>
	  	<xd:detail>Template to display the details <code>&lt;biogHist&gt;</code> element according to the selected language.</xd:detail>
		<xd:param name="bioHist">The parameter "biogHist" is an array of <code>biogHist</code> elements.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	</xd:doc>
	<xsl:template name="bioHistMultilanguage" >
		<xsl:param name="bioHist"/>
		<xsl:param name="entityType"/>
		<xsl:param name="mode"/>
		
		<!-- Set the variable which checks if at least one of the links in
				 "<citation>" element inside "<biogHist>" is a valid
				 one. -->
		<xsl:variable name="validHrefLinkBiogHist">
			<xsl:choose>
				<xsl:when test="$bioHist/eac:citation[@xlink:href != '']">
					<xsl:value-of select="ape:checkHrefValue(string-join($bioHist/eac:citation/@xlink:href, '_HREF_SEPARATOR_'))"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'false'"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<!--Set the language to select in biogHist  -->
		<xsl:variable name="langBiogHist">
			 	<xsl:variable name="lang" select="$bioHist//@xml:lang"/>
			    <xsl:variable name="listLang" select="string-join($lang,'')"/>
			 	
			 	<xsl:variable name="conditionLangSelected" select="($bioHist//eac:abstract[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:p[@xml:lang=$language.selected]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:citation[@xml:lang=$language.selected]/fn:normalize-space(text())!= ''
			               or ($bioHist//eac:citation[@xml:lang=$language.selected and @xlink:href != ''] and $validHrefLinkBiogHist = 'true')
			               or $bioHist//eac:citation[@xml:lang=$language.selected and @xlink:title != '']
			               or $bioHist//eac:date[@xml:lang = $language.selected]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:fromDate[@xml:lang = $language.selected]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:toDate[@xml:lang = $language.selected]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:placeEntry[@xml:lang = $language.selected]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:event[@xml:lang = $language.selected]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangNavigator" select="($bioHist//eac:abstract[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:p[@xml:lang=$lang.navigator]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:citation[@xml:lang=$lang.navigator]/fn:normalize-space(text())!= ''
			               or ($bioHist//eac:citation[@xml:lang=$lang.navigator and @xlink:href != ''] and $validHrefLinkBiogHist = 'true')
			               or $bioHist//eac:citation[@xml:lang=$lang.navigator and @xlink:title != '']
			               or $bioHist//eac:date[@xml:lang = $lang.navigator]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:fromDate[@xml:lang = $lang.navigator]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:toDate[@xml:lang = $lang.navigator]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:placeEntry[@xml:lang = $lang.navigator]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:event[@xml:lang = $lang.navigator]/fn:normalize-space(text()) != '')"/>
			 	
			 	<xsl:variable name="conditionLangDefault" select="($bioHist//eac:abstract[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:p[@xml:lang=$language.default]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:citation[@xml:lang=$language.default]/fn:normalize-space(text())!= ''
			               or ($bioHist//eac:citation[@xml:lang=$language.default and @xlink:href != ''] and $validHrefLinkBiogHist = 'true')
			               or $bioHist//eac:citation[@xml:lang=$language.default and @xlink:title != '']
			               or $bioHist//eac:date[@xml:lang = $language.default]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:fromDate[@xml:lang = $language.default]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:toDate[@xml:lang = $language.default]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:placeEntry[@xml:lang = $language.default]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:event[@xml:lang = $language.default]/fn:normalize-space(text()) != '')"/>
							
			 	<xsl:variable name="conditionNotLang" select="($bioHist//eac:abstract[not(@xml:lang)]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:p[not(@xml:lang)]/fn:normalize-space(text()) != ''
			               or $bioHist//eac:citation[not(@xml:lang)]/fn:normalize-space(text())!= ''
			               or ($bioHist//eac:citation[not(@xml:lang) and @xlink:href != ''] and $validHrefLinkBiogHist = 'true')
			               or $bioHist//eac:citation[not(@xml:lang) and @xlink:title != '']
			               or $bioHist//eac:date[not(@xml:lang)]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:fromDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:toDate[not(@xml:lang)]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:placeEntry[not(@xml:lang)]/fn:normalize-space(text()) != ''
						   or $bioHist//eac:event[not(@xml:lang)]/fn:normalize-space(text()) != '')"/>
			 	
			 	 <xsl:choose>
			    	<xsl:when test="fn:contains($listLang,$language.selected) and $conditionLangSelected">
			    	  <xsl:value-of select="$language.selected"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$lang.navigator) and $conditionLangNavigator">
			    		<xsl:value-of select="$lang.navigator"/>
			    	</xsl:when>
			    	<xsl:when test="fn:contains($listLang,$language.default) and $conditionLangDefault">
			    		<xsl:value-of select="$language.default"/>
			    	</xsl:when>
			    	<xsl:when test="$conditionNotLang">
			    		<xsl:value-of select="'notLang'"/>
			    	</xsl:when>
			    	<xsl:otherwise>
			    	  <xsl:value-of select="substring($listLang,0,4)"/>
			    	</xsl:otherwise>
			    </xsl:choose> 
		</xsl:variable>
		
		<!-- bioHist section title -->
		<xsl:if test="((($bioHist/eac:p/text()
							or $bioHist/eac:citation
							or $bioHist/eac:chronList/eac:chronItem 
							or $bioHist/eac:abstract/text()) and ($mode='default' or $mode='showAll'))
		               or ($mode='other' and 
			              ($bioHist/eac:abstract[@xml:lang=$translationLanguage]/text() != ''
			               or $bioHist/eac:p[@xml:lang=$translationLanguage]/text() != ''
			               or $bioHist/eac:citation[@xml:lang=$translationLanguage]/text() != ''
			               or ($bioHist/eac:citation[@xml:lang=$translationLanguage and @xlink:href != ''] and $validHrefLinkBiogHist = 'true')
			               or $bioHist/eac:citation[@xml:lang=$translationLanguage and @xlink:title != '']
			               or $bioHist/eac:chronList/eac:chronItem/eac:date[@xml:lang = $translationLanguage]/text() != ''
						   or $bioHist/eac:chronList/eac:chronItem/eac:dateRange/eac:fromDate[@xml:lang = $translationLanguage]/text() != ''
						   or $bioHist/eac:chronList/eac:chronItem/eac:dateRange/eac:toDate[@xml:lang = $translationLanguage]/text() != ''
						   or $bioHist/eac:chronList/eac:chronItem/eac:placeEntry[@xml:lang = $translationLanguage]/text() != ''
						   or $bioHist/eac:chronList/eac:chronItem/eac:event[@xml:lang = $translationLanguage]/text() != '')))">
				<h2 id="chronListTitle" class="title expanded">
					<xsl:if test="$entityType='corporateBody'">
		   				<xsl:value-of select="translate(ape:resource('eaccpf.portal.historicalNote'), $smallcase, $uppercase)"/>
		   			</xsl:if>
		   			<xsl:if test="$entityType='person' or $entityType='family'">
		   				<xsl:value-of select="translate(ape:resource('eaccpf.portal.biogHist'), $smallcase, $uppercase)"/>
		   			</xsl:if>
				</h2>
		</xsl:if>
		<div class="section">
			<xsl:for-each select="$bioHist">
				<xsl:variable name="posDisplay" select="position()"/>
				<div class="blockPlural">
					<!-- Display the information of biogHist in the same order that are in the xml file -->
					<!-- abstract always goes in the first position -->
					
					<!-- biogHist abstract -->
				  	<xsl:if test="./eac:abstract/text()">
						<xsl:call-template name="biogHistAbstract">
							<xsl:with-param name="list" select="./eac:abstract"/>
							<xsl:with-param name="mode" select="$mode"/>
							<xsl:with-param name="langNode" select="$langBiogHist"/>
						</xsl:call-template>
					</xsl:if>
					
					<!-- Set the positions of the <citation>, <chronList>, <p> in the xml file-->
					<xsl:variable name="pPosition">
						<xsl:choose>
							<xsl:when test="./eac:p">
								<xsl:for-each select="./*">
						  			<xsl:if test="name() = 'p'">
										<xsl:value-of select="concat(position(),',')" /> 
									</xsl:if>
								</xsl:for-each>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="concat('6',',')" /> 
							</xsl:otherwise>	
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="posP" select="number(substring-before($pPosition,',' ))"/>
					
					<xsl:variable name="citationPosition">
						<xsl:choose>
						<xsl:when test="./eac:citation">
							<xsl:for-each select="./*">
					  			<xsl:if test="name() = 'citation'">
									<xsl:value-of select="concat(position(),',')" /> 
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat('6',',')" />
						</xsl:otherwise>	
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="posCitation" select="number(substring-before($citationPosition,',' ))"/>
					
					<xsl:variable name="chronListPosition">
						<xsl:choose>
							<xsl:when test="./eac:chronList">
								<xsl:for-each select="./*">
						  			<xsl:if test="name() = 'chronList'">
										<xsl:value-of select="concat(position(),',')" /> 
									</xsl:if>
								</xsl:for-each>
							</xsl:when>	
							<xsl:otherwise>
								<xsl:value-of select="concat('6',',')" /> 
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="posChron" select="number(substring-before($chronListPosition,',' ))"/>
		
					<!-- Display the information according to the position of the nodes -->
					<xsl:choose>
						<xsl:when test="($posP &lt; $posCitation) and ($posP &lt; $posChron)">
							<!-- biogHist p --> 
						 	<xsl:if test="./eac:p/text()">
								<xsl:call-template name="biogHistP">
									<xsl:with-param name="list" select="./eac:p"/>
									<xsl:with-param name="entityType" select="$entityType"/>
									<xsl:with-param name="mode" select="$mode"/>
									<xsl:with-param name="langNode" select="$langBiogHist"/>
									<xsl:with-param name="posParent" select="$posDisplay"/>
								</xsl:call-template>
							</xsl:if> 
							<xsl:choose>
								<xsl:when test="$posCitation &lt; $posChron">
									<!-- biogHist citation --> 
								  	<xsl:if test="./eac:citation">
								   		<xsl:variable name="title">
								   			<xsl:value-of select="'eaccpf.portal.citation'"/>
								   		</xsl:variable>
							
								    	<xsl:call-template name="commonChild">
								   			<xsl:with-param name="list" select="./eac:citation"/>
								   			<xsl:with-param name="clazz" select="'citationbioHist_'"/>
								   			<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								   			<xsl:with-param name="posParent" select="$posDisplay"/>
											<xsl:with-param name="posChild" select="''"/>
											<xsl:with-param name="mode" select="$mode"/>
								    		<xsl:with-param name="title" select="$title"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkBiogHist"/>
								    		<xsl:with-param name="langNode" select="$langBiogHist"/>
								    	</xsl:call-template>
									</xsl:if>
									<!-- biogHist chronList -->
									<xsl:if test="./eac:chronList/eac:chronItem">
										<xsl:call-template name="chronListMultilanguage">
											<xsl:with-param name="list" select="./eac:chronList"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<!-- biogHist chronList -->
									<xsl:if test="./eac:chronList/eac:chronItem">
										<xsl:call-template name="chronListMultilanguage">
											<xsl:with-param name="list" select="./eac:chronList"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
										</xsl:call-template>
									</xsl:if>
									<!-- biogHist citation --> 
								  	<xsl:if test="./eac:citation">
								   		<xsl:variable name="title">
								   			<xsl:value-of select="'eaccpf.portal.citation'"/>
								   		</xsl:variable>
							
								    	<xsl:call-template name="commonChild">
								   			<xsl:with-param name="list" select="./eac:citation"/>
								   			<xsl:with-param name="clazz" select="'citationbioHist_'"/>
								   			<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								   			<xsl:with-param name="posParent" select="''"/>
											<xsl:with-param name="posChild" select="''"/>
											<xsl:with-param name="mode" select="$mode"/>
								    		<xsl:with-param name="title" select="$title"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkBiogHist"/>
								    		<xsl:with-param name="langNode" select="$langBiogHist"/>
								    	</xsl:call-template>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="($posCitation &lt; $posP) and ($posCitation &lt; $posChron)">
							<!-- biogHist citation --> 
						  	<xsl:if test="./eac:citation">
						   		<xsl:variable name="title">
						   			<xsl:value-of select="'eaccpf.portal.citation'"/>
						   		</xsl:variable>
					
						    	<xsl:call-template name="commonChild">
						   			<xsl:with-param name="list" select="./eac:citation"/>
						   			<xsl:with-param name="clazz" select="'citationbioHist_'"/>
						   			<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
						   			<xsl:with-param name="posParent" select="''"/>
									<xsl:with-param name="posChild" select="''"/>
									<xsl:with-param name="mode" select="$mode"/>
						    		<xsl:with-param name="title" select="$title"/>
			   						<xsl:with-param name="validHrefLink" select="$validHrefLinkBiogHist"/>
						    		<xsl:with-param name="langNode" select="$langBiogHist"/>
						    	</xsl:call-template>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="$posP &lt; $posChron">
									<!-- biogHist p --> 
								 	<xsl:if test="./eac:p/text()">
										<xsl:call-template name="biogHistP">
											<xsl:with-param name="list" select="./eac:p"/>
											<xsl:with-param name="entityType" select="$entityType"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
											<xsl:with-param name="posParent" select="$posDisplay"/>
										</xsl:call-template>
									</xsl:if> 
									<!-- biogHist chronList -->
									<xsl:if test="./eac:chronList/eac:chronItem">
										<xsl:call-template name="chronListMultilanguage">
											<xsl:with-param name="list" select="./eac:chronList"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
										</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<!-- biogHist chronList -->
									<xsl:if test="./eac:chronList/eac:chronItem">
										<xsl:call-template name="chronListMultilanguage">
											<xsl:with-param name="list" select="./eac:chronList"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
										</xsl:call-template>
									</xsl:if>
									<!-- biogHist p --> 
								 	<xsl:if test="./eac:p/text()">
										<xsl:call-template name="biogHistP">
											<xsl:with-param name="list" select="./eac:p"/>
											<xsl:with-param name="entityType" select="$entityType"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
											<xsl:with-param name="posParent" select="$posDisplay"/>
										</xsl:call-template>
									</xsl:if> 
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="($posChron &lt; $posP) and ($posChron &lt; $posCitation)">
							<!-- biogHist chronList -->
							<xsl:if test="./eac:chronList/eac:chronItem">
								<xsl:call-template name="chronListMultilanguage">
									<xsl:with-param name="list" select="./eac:chronList"/>
									<xsl:with-param name="mode" select="$mode"/>
									<xsl:with-param name="langNode" select="$langBiogHist"/>
								</xsl:call-template>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="$posP &lt; $posCitation">
									<!-- biogHist p --> 
								 	<xsl:if test="./eac:p/text()">
										<xsl:call-template name="biogHistP">
											<xsl:with-param name="list" select="./eac:p"/>
											<xsl:with-param name="entityType" select="$entityType"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
											<xsl:with-param name="posParent" select="$posDisplay"/>
										</xsl:call-template>
									</xsl:if> 
									<!-- biogHist citation --> 
								  	<xsl:if test="./eac:citation">
								   		<xsl:variable name="title">
								   			<xsl:value-of select="'eaccpf.portal.citation'"/>
								   		</xsl:variable>
							
								    	<xsl:call-template name="commonChild">
								   			<xsl:with-param name="list" select="./eac:citation"/>
								   			<xsl:with-param name="clazz" select="'citationbioHist_'"/>
								   			<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								   			<xsl:with-param name="posParent" select="''"/>
											<xsl:with-param name="posChild" select="''"/>
											<xsl:with-param name="mode" select="$mode"/>
								    		<xsl:with-param name="title" select="$title"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkBiogHist"/>
								    		<xsl:with-param name="langNode" select="$langBiogHist"/>
								    	</xsl:call-template>
									</xsl:if>
								</xsl:when>
								<xsl:otherwise>
									<!-- biogHist citation --> 
								  	<xsl:if test="./eac:citation">
								   		<xsl:variable name="title">
								   			<xsl:value-of select="'eaccpf.portal.citation'"/>
								   		</xsl:variable>
							
								    	<xsl:call-template name="commonChild">
								   			<xsl:with-param name="list" select="./eac:citation"/>
								   			<xsl:with-param name="clazz" select="'citationbioHist_'"/>
								   			<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
								   			<xsl:with-param name="posParent" select="''"/>
											<xsl:with-param name="posChild" select="''"/>
											<xsl:with-param name="mode" select="$mode"/>
								    		<xsl:with-param name="title" select="$title"/>
			   								<xsl:with-param name="validHrefLink" select="$validHrefLinkBiogHist"/>
								    		<xsl:with-param name="langNode" select="$langBiogHist"/>
								    	</xsl:call-template>
									</xsl:if>
									<!-- biogHist p --> 
								 	<xsl:if test="./eac:p/text()">
										<xsl:call-template name="biogHistP">
											<xsl:with-param name="list" select="./eac:p"/>
											<xsl:with-param name="entityType" select="$entityType"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="$langBiogHist"/>
											<xsl:with-param name="posParent" select="$posDisplay"/>
										</xsl:call-template>
									</xsl:if> 
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>	
						<xsl:otherwise/>
					</xsl:choose>
				</div>
			</xsl:for-each>
		</div>
	</xsl:template>
	
	<!-- Template to display the <abstract> element  -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;abstract&gt;</code>.</xd:short>
	  	<xd:detail>Template to display the details <code>&lt;abstract&gt;</code> element inside the <code>&lt;biogHist&gt;</code> element.</xd:detail>
		<xd:param name="list">The parameter "list" is the element <code>&lt;abstract&gt;</code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="biogHistAbstract">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.abstract')"/><xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
			   			<xsl:with-param name="clazz" select="'biogistAbstract'"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>
	
		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.abstract')"/><xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'biogistAbstract'"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
	
		<xsl:if test="$mode = 'default' and (
			(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
				 or $list[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource('eaccpf.portal.abstract')"/><xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showDetailsDescriptiveNote">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'biogistAbstract'"/>
			   			<xsl:with-param name="posParent" select="''"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   			<xsl:with-param name="langNode" select="$langNode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!-- Template to display correctly the details of the "<p>" element inside
		 "<biogHist>" element. -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;p&gt;</code>.</xd:short>
	  	<xd:detail>Template to display correctly the details <code>&lt;p&gt;</code> element inside the <code>&lt;biogHist&gt;</code> element.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;p&gt;</code> elements.</xd:param>
		<xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="biogHistP">
		<xsl:param name="list"/>
		<xsl:param name="entityType"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		<xsl:param name="posParent"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:if test="$entityType='corporateBody'">
							<xsl:value-of select="ape:resource('eaccpf.portal.historicalNote')"/>
						</xsl:if>
						<xsl:if test="$entityType='person' or $entityType='family'">
							<xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/>
						</xsl:if>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
			   			<xsl:with-param name="clazz" select="'biogist'"/>
			   			<xsl:with-param name="posParent" select="$posParent"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:if test="$entityType='corporateBody'">
							<xsl:value-of select="ape:resource('eaccpf.portal.historicalNote')"/>
						</xsl:if>
						<xsl:if test="$entityType='person' or $entityType='family'">
							<xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/>
						</xsl:if>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'biogist'"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   			<xsl:with-param name="posParent" select="$posParent"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
	
		<xsl:if test="$mode = 'default' and (
			(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
				 or $list[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:if test="$entityType='corporateBody'">
							<xsl:value-of select="ape:resource('eaccpf.portal.historicalNote')"/>
						</xsl:if>
						<xsl:if test="$entityType='person' or $entityType='family'">
							<xsl:value-of select="ape:resource('eaccpf.portal.biogHist')"/>
						</xsl:if>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn">
					<xsl:call-template name="showDetailsDescriptiveNote">
			   			<xsl:with-param name="list" select="$list"/>
			   			<xsl:with-param name="clazz" select="'biogist_'"/>
			   			<xsl:with-param name="posParent" select="$posParent"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   			<xsl:with-param name="langNode" select="$langNode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>	
	</xsl:template>
	
	<xd:doc>
	    <xd:short>Template to <code>&lt;chronList&gt;</code>.</xd:short>
	    <xd:detail>Template to display the details <code>&lt;chronList&gt;</code> element inside the <code>&lt;biogHist&gt;</code> element.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;chronList&gt;</code> elements.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="chronListMultilanguage">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<xsl:if test="$list/eac:chronItem or $list/eac:placeEntry or $list/eac:event">

			<!-- Checks if exists any content in the translation language. -->
			<!-- Try to select the content in the specific language. -->
			<xsl:if test="$mode = 'other'
						and ($list/eac:chronItem/eac:date[@xml:lang = $translationLanguage]/text()
							or $list/eac:chronItem/eac:dateRange/eac:fromDate[@xml:lang = $translationLanguage]/text()
							or $list/eac:chronItem/eac:dateRange/eac:toDate[@xml:lang = $translationLanguage]/text()
							or $list/eac:chronItem/eac:placeEntry[@xml:lang = $translationLanguage]/text()
							or $list/eac:chronItem/eac:event[@xml:lang = $translationLanguage]/text())">
				<xsl:for-each select="$list" >
					<div class="blockPlural">
						<xsl:for-each select="./eac:chronItem" >
							<div class="blockSingular">
								<xsl:if test="./eac:date[@xml:lang = $translationLanguage]/text()
											or ./eac:dateRange/eac:fromDate[@xml:lang = $translationLanguage]/text()
											or ./eac:dateRange/eac:toDate[@xml:lang = $translationLanguage]/text()">
									<div id="chronListItemContent" >
										<xsl:call-template name="commonDates">
											<xsl:with-param name="date" select="./eac:date"/>
											<xsl:with-param name="dateRange" select="./eac:dateRange"/>
											<xsl:with-param name="dateSet" select="./eac:dateSet"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="''"/>
										</xsl:call-template>
									</div>
								</xsl:if>

								<xsl:if test="./eac:placeEntry[@xml:lang = $translationLanguage]/text()">
									<div id="chronListItemContent" class="row">
										<div class="leftcolumn">
											<h2>
												<xsl:value-of select="ape:resource('eaccpf.description.place')"/>
												<xsl:text>:</xsl:text>
											</h2>
										</div>
										<div class="rightcolumn">
											<xsl:call-template name="showAllPlaceEntry">
												<xsl:with-param name="list" select="./eac:placeEntry[@xml:lang = $translationLanguage]"/>
											</xsl:call-template>
										</div>
									</div>
								</xsl:if>

								<xsl:if test="./eac:event[@xml:lang = $translationLanguage]/text()">
									<div id="chronListItemContent" class="row">
										<div class="leftcolumn">
											<h2>
												<xsl:value-of select="ape:resource('eaccpf.portal.event')"/>
												<xsl:text>:</xsl:text>
											</h2>
										</div> 
										<div class="rightcolumn"> 
											<xsl:call-template name="showAllNoP"> <!-- multilanguageEvent -->
												<xsl:with-param name="list" select="./eac:event[@xml:lang = $translationLanguage]"/>
											</xsl:call-template>
										</div>
									</div>
								</xsl:if>
							</div>
						</xsl:for-each>
					</div>
				</xsl:for-each>
			</xsl:if>

			<!-- Try to display all the entries. -->
			<xsl:if test="$mode = 'showAll'
						and ($list/eac:chronItem/eac:date/text()
							or $list/eac:chronItem/eac:dateRange/eac:fromDate/text()
							or $list/eac:chronItem/eac:dateRange/eac:toDate/text()
							or $list/eac:chronItem/eac:placeEntry/text()
							or $list/eac:chronItem/eac:event/text())">
				<xsl:for-each select="$list" >
					<div class="blockPlural">
						<xsl:for-each select="./eac:chronItem" >
							<div class="blockSingular">
								<xsl:if test="./eac:date or ./eac:dateRange or ./eac:dateSet">
									<div id="chronListItemContent" >
										<xsl:call-template name="commonDates">
											<xsl:with-param name="date" select="./eac:date"/>
											<xsl:with-param name="dateRange" select="./eac:dateRange"/>
											<xsl:with-param name="dateSet" select="./eac:dateSet"/>
											<xsl:with-param name="mode" select="$mode"/>
											<xsl:with-param name="langNode" select="''"/>
										</xsl:call-template>
									</div>
								</xsl:if>

								<xsl:if test="./eac:placeEntry">
									<div id="chronListItemContent" class="row">
										<div class="leftcolumn">
											<h2>
												<xsl:value-of select="ape:resource('eaccpf.description.place')"/>
												<xsl:text>:</xsl:text>
											</h2>
										</div>
										<div class="rightcolumn">
											<xsl:call-template name="showAllPlaceEntry">
												<xsl:with-param name="list" select="./eac:placeEntry"/>
											</xsl:call-template>
										</div>
									</div>
								</xsl:if>

								<xsl:if test="./eac:event">
									<div id="chronListItemContent" class="row">
										<div class="leftcolumn">
											<h2>
												<xsl:value-of select="ape:resource('eaccpf.portal.event')"/>
												<xsl:text>:</xsl:text>
											</h2>
										</div> 
										<div class="rightcolumn"> 
											<xsl:call-template name="showAllNoP"> <!-- multilanguageEvent -->
												<xsl:with-param name="list" select="./eac:event"/>
											</xsl:call-template>
										</div>
									</div>
								</xsl:if>
							</div>
						</xsl:for-each>
					</div>
				</xsl:for-each>
			</xsl:if>

			<!-- Try to select the 'default' mode. -->
			
			<xsl:if test="$mode = 'default' and (
						(	$langNode='notLang' and ($list/eac:chronItem/eac:date[not(@xml:lang)]/text()
										or $list/eac:chronItem/eac:dateRange/eac:fromDate[not(@xml:lang)]/text()
										or $list/eac:chronItem/eac:dateRange/eac:toDate[not(@xml:lang)]/text()
										or $list/eac:chronItem/eac:placeEntry[not(@xml:lang)]/text()
										or $list/eac:chronItem/eac:event[not(@xml:lang)]/text())) 
							 or ($list/eac:chronItem/eac:date[@xml:lang=$langNode]/text()
										or $list/eac:chronItem/eac:dateRange/eac:fromDate[@xml:lang=$langNode]/text()
										or $list/eac:chronItem/eac:dateRange/eac:toDate[@xml:lang=$langNode]/text()
										or $list/eac:chronItem/eac:placeEntry[@xml:lang=$langNode]/text()
										or $list/eac:chronItem/eac:event[@xml:lang=$langNode]/text()) 
			                 or ($searchTerms!='' and 
			                 	(
				                 	$list/eac:chronItem/eac:placeEntry[@xml:lang!=$langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
										or $list/eac:chronItem/eac:event[@xml:lang!=$langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]  
					      		 	or (
					      		 		$langNode!='notLang' and ($list/eac:chronItem/eac:placeEntry[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
										or $list/eac:chronItem/eac:event[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))])
				      		 		)
				      		 	)
				      		 )
						)">
					
				<xsl:for-each select="$list" >
					<div class="blockPlural">
						<xsl:for-each select="./eac:chronItem" >
							<xsl:if test="$mode = 'default' and (
											(	$langNode='notLang' and (./eac:date[not(@xml:lang)]/text()
															or ./eac:dateRange/eac:fromDate[not(@xml:lang)]/text()
															or ./eac:dateRange/eac:toDate[not(@xml:lang)]/text()
															or ./eac:placeEntry[not(@xml:lang)]/text()
															or ./eac:event[not(@xml:lang)]/text())) 
												 or (./eac:date[@xml:lang=$langNode]/text()
															or ./eac:dateRange/eac:fromDate[@xml:lang=$langNode]/text()
															or ./eac:dateRange/eac:toDate[@xml:lang=$langNode]/text()
															or ./eac:placeEntry[@xml:lang=$langNode]/text()
															or ./eac:event[@xml:lang=$langNode]/text()) 
								                 or ($searchTerms!='' and 
								                 	(
									                 	./eac:placeEntry[@xml:lang!=$langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
															or ./eac:event[@xml:lang!=$langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]  
										      		 	or (
										      		 		$langNode!='notLang' and ($list/eac:chronItem/eac:placeEntry[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
															or ./eac:event[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))])
									      		 		)
									      		 	)
									      		 )
											)">
									<div class="blockSingular">
										<xsl:if test="$mode = 'default' and (
													(	$langNode='notLang' and (./eac:date[not(@xml:lang)]/text()
																	or ./eac:dateRange/eac:fromDate[not(@xml:lang)]/text()
																	or ./eac:dateRange/eac:toDate[not(@xml:lang)]/text())) 
														 or (./eac:date[@xml:lang=$langNode]/text()
																	or ./eac:dateRange/eac:fromDate[@xml:lang=$langNode]/text()
																	or ./eac:dateRange/eac:toDate[@xml:lang=$langNode]/text()) 
										                 
													)">
													
											<div id="chronListItemContent" >
												<xsl:call-template name="commonDates">
													<xsl:with-param name="date" select="./eac:date"/>
													<xsl:with-param name="dateRange" select="./eac:dateRange"/>
													<xsl:with-param name="dateSet" select="./eac:dateSet"/>
													<xsl:with-param name="mode" select="$mode"/>
													<xsl:with-param name="langNode" select="$langNode"/>
												</xsl:call-template>
											</div>
										</xsl:if>
		
										<xsl:if test="($langNode='notLang' and ./eac:placeEntry[not(@xml:lang)]/text()) 
														 or ./eac:placeEntry[@xml:lang=$langNode]/text() 
										                 or ($searchTerms!='' and 
										                 	(./eac:placeEntry[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
												      		 	or (
												      		 		$langNode!='notLang' and ./eac:placeEntry[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
											      		 		)
											      		 	)
											      		 )
													">
											<div id="chronListItemContent" class="row">
												<div class="leftcolumn">
													<h2>
														<xsl:value-of select="ape:resource('eaccpf.description.place')"/>
														<xsl:text>:</xsl:text>
													</h2>
												</div>
												<div class="rightcolumn">
													<xsl:call-template name="showDetailsPlaceEntry">
														<xsl:with-param name="list" select="./eac:placeEntry"/>
														<xsl:with-param name="langNode" select="$langNode"/>
													</xsl:call-template>
												</div>
											</div>
										</xsl:if>
		
										<xsl:if test="($langNode='notLang' and ./eac:event[not(@xml:lang)]/text()) 
														 or ./eac:event[@xml:lang=$langNode]/text() 
										                 or ($searchTerms!='' and 
										                 	(./eac:event[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
												      		 	or (
												      		 		$langNode!='notLang' and ./eac:event[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
											      		 		)
											      		 	)
											      		 )
													">
											<div id="chronListItemContent" class="row">
												<div class="leftcolumn">
													<h2>
														<xsl:value-of select="ape:resource('eaccpf.portal.event')"/>
														<xsl:text>:</xsl:text>
													</h2>
												</div> 
												<div class="rightcolumn"> 
													<xsl:call-template name="showDetailsNoP"> <!-- multilanguageEvent -->
														<xsl:with-param name="list" select="./eac:event"/>
														<xsl:with-param name="langNode" select="$langNode"/>
													</xsl:call-template>
												</div>
											</div>
										</xsl:if>
									</div>
							</xsl:if>
						</xsl:for-each>
					</div>
				</xsl:for-each>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<!-- Template for select the correct order to display the alternative names
		 based in the value of the attribute "@localType". -->
	
	<xd:doc>
	  	<xd:short>Template to the correct order of <code>&lt;nameEntry&gt;</code>.</xd:short>
	  	<xd:detail>Select the correct order to display the alternative names based in the value of the attribute <code>@localType</code>.</xd:detail>
		<xd:param name="list">The parameter "list" is the array of <code>&lt;nameEntry&gt;</code>.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="alternativeNamePriorisation">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>

	<!--  	<div id="{$clazz}" class= "moreDisplay">-->
	     <div id="{$clazz}">
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<!-- Checks the attribute "@localType". -->
					<!-- localType = preferred -->
					<xsl:if test="$list[@localType = 'preferred']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'preferred']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = authorized -->
					<xsl:if test="$list[@localType = 'authorized']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'authorized']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- not @localType -->
					<xsl:if test="$list[not(@localType)]">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[not(@localType)]"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = alternative -->
					<xsl:if test="$list[@localType = 'alternative']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'alternative']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = abbreviation -->
					<xsl:if test="$list[@localType = 'abbreviation']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'abbreviation']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- localType = other -->
					<xsl:if test="$list[@localType = 'other']">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[@localType = 'other']"/>
						</xsl:call-template> 
					</xsl:if>
					<!-- In any other case checks the whole list. -->
					<xsl:if test="$list[not(@localType = 'preferred') and not(@localType = 'authorized') and not(@localType = 'alternative') and not(@localType = 'abbreviation') and not(@localType = 'other') and @localType]">
						<xsl:call-template name="alternativeName">
							<xsl:with-param name="list" select="$list[not(@localType = 'preferred') and not(@localType = 'authorized') and not(@localType = 'alternative') and not(@localType = 'abbreviation') and not(@localType = 'other') and @localType]"/>
						</xsl:call-template> 
					</xsl:if>
				</xsl:when>

				<xsl:otherwise>
					<xsl:call-template name="alternativeName">
						<xsl:with-param name="list" select="$list"/>
					</xsl:call-template> 
				</xsl:otherwise>
			</xsl:choose>

		<!--<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>-->
		</div>
	</xsl:template>
	
	<!-- Template for alternative forms of name -->
	
	<xd:doc>
	  	<xd:short>Template for alternative names.</xd:short>
	  	<xd:detail>Template that call to the template "compositeName" to compose the different parts of the name.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of alternative names.</xd:param>
	</xd:doc>
	<xsl:template name="alternativeName">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
			<p>
				<xsl:call-template name="compositeName">
					<xsl:with-param name="listName" select="current()"/>
					<xsl:with-param name="isHeader" select="'false'"/>
				</xsl:call-template> 
		    </p>
		</xsl:for-each>
	</xsl:template>

	<!-- Template for select the correct order to display the places based in
		 the value of the attribute "@localType". -->
	
	<xd:doc>
		<xd:short>Template to order the <code>&lt;place&gt;</code> elements.</xd:short>
	  	<xd:detail>Template for select the correct order to display the places based in the value of the attribute <code>@localType</code>.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;place&gt;</code> elements.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="placePriorisation">
		<xsl:param name="list"/>
		<xsl:param name="posParent"/>
	 	<xsl:param name="posChild"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks the attribute "@localType". -->
		<!-- localType = birth -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'birth']">
	  		<xsl:call-template name="places">
   				<xsl:with-param name="list" select="$list/eac:placeEntry[@localType = 'birth']"/>
   				<xsl:with-param name="clazz" select="'locationBirthPlace_'"/>
   				<xsl:with-param name="posParent" select="$posParent"/>
   				<xsl:with-param name="posChild" select="$posChild"/>
   				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="title" select="'eaccpf.description.combo.place.role.birth'"/>
   				<xsl:with-param name="langNode" select="$langNode"/>
   			</xsl:call-template>
		</xsl:if>

		<!-- localType = foundation -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'foundation']">
			<xsl:call-template name="places">
   				<xsl:with-param name="list" select="$list/eac:placeEntry[@localType = 'foundation']"/>
   				<xsl:with-param name="clazz" select="'locationFoundationPlace_'"/>
   				<xsl:with-param name="posParent" select="$posParent"/>
   				<xsl:with-param name="posChild" select="$posChild"/>
   				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="title" select="'eaccpf.description.combo.place.role.foundation'"/>
   				<xsl:with-param name="langNode" select="$langNode"/>
   			</xsl:call-template>
		</xsl:if>

		<!-- localType = private-residence -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'private-residence']">
			<xsl:call-template name="places">
   				<xsl:with-param name="list" select="$list/eac:placeEntry[@localType = 'private-residence']"/>
   				<xsl:with-param name="clazz" select="'locationPrivatePlace_'"/>
   				<xsl:with-param name="posParent" select="$posParent"/>
   				<xsl:with-param name="posChild" select="$posChild"/>
   				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="title" select="'eaccpf.description.combo.place.role.private-residence'"/>
   				<xsl:with-param name="langNode" select="$langNode"/>
   			</xsl:call-template>
		</xsl:if>

		<!-- localType = business-residence -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'business-residence']">
			<xsl:call-template name="places">
   				<xsl:with-param name="list" select="$list/eac:placeEntry[@localType = 'business-residence']"/>
   				<xsl:with-param name="clazz" select="'locationBusinessPlace_'"/>
   				<xsl:with-param name="posParent" select="$posParent"/>
   				<xsl:with-param name="posChild" select="$posChild"/>
   				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="title" select="'eaccpf.description.combo.place.role.business-residence'"/>
   				<xsl:with-param name="langNode" select="$langNode"/>
   			</xsl:call-template>
		</xsl:if>

		<!-- localType = death -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'death']">
			<xsl:call-template name="places">
   				<xsl:with-param name="list" select="$list/eac:placeEntry[@localType = 'death']"/>
   				<xsl:with-param name="clazz" select="'locationDeathPlace_'"/>
   				<xsl:with-param name="posParent" select="$posParent"/>
   				<xsl:with-param name="posChild" select="$posChild"/>
   				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="title" select="'eaccpf.description.combo.place.role.death'"/>
   				<xsl:with-param name="langNode" select="$langNode"/>
   			</xsl:call-template>
		</xsl:if>

		<!-- localType = suppression -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'suppression']">
			<xsl:call-template name="places">
   				<xsl:with-param name="list" select="$list/eac:placeEntry[@localType = 'suppression']"/>
   				<xsl:with-param name="clazz" select="'locationSuppressionPlace_'"/>
   				<xsl:with-param name="posParent" select="$posParent"/>
   				<xsl:with-param name="posChild" select="$posChild"/>
   				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="title" select="'eaccpf.description.combo.place.role.suppression'"/>
   				<xsl:with-param name="langNode" select="$langNode"/>
   			</xsl:call-template>
		</xsl:if>

		<!-- In any other case checks the whole list. -->
		<!-- localType = other -->
		<!-- no localType -->
		<!-- empty localType -->
		<xsl:if test="$list/eac:placeEntry[not(@localType = 'birth') and not(@localType = 'death') and not(@localType = 'foundation') and not(@localType = 'private-residence') and not(@localType = 'business-residence') and not(@localType = 'suppression')]">
			<xsl:call-template name="places">
				<xsl:with-param name="list" select="$list/eac:placeEntry[not(@localType = 'birth') and not(@localType = 'death') and not(@localType = 'foundation') and not(@localType = 'private-residence') and not(@localType = 'business-residence') and not(@localType = 'suppression')]"/>
   				<xsl:with-param name="clazz" select="'locationOtherPlace_'"/>
   				<xsl:with-param name="posParent" select="$posParent"/>
   				<xsl:with-param name="posChild" select="$posChild"/>
   				<xsl:with-param name="mode" select="$mode"/>
				<xsl:with-param name="title" select="'eaccpf.description.place'"/>
				<xsl:with-param name="langNode" select="$langNode"/>
			</xsl:call-template> 
		</xsl:if>
	</xsl:template>

	<!-- template places -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;placeEntry&gt;</code>.</xd:short>
	  	<xd:detail>Template to display the details related to <code>&lt;placeEntry&gt;</code> into <code>&lt;place&gt;</code> element
	  	according with the mode in the translation combo.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;placeEntry&gt;</code> elements.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="title">The parameter "title" is the label's title.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="places">
		<xsl:param name="list"/>
	    <xsl:param name="clazz"/>
	    <xsl:param name="posParent"/>
	    <xsl:param name="posChild"/>
		<xsl:param name="mode"/>
	    <xsl:param name="title"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list[@xml:lang = $translationLanguage]/text()">
			<div class="row locationPlace">
			 	<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllPlaceEntrySeveral">
		   				<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   			</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/text()">
			<div class="row locationPlace">
			 	<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllPlaceEntrySeveral">
		   				<xsl:with-param name="list" select="$list"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   			</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
	
		<xsl:if test="$mode = 'default' and (
					(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
						 or $list[@xml:lang=$langNode]/text() 
		                 or ($searchTerms!='' and 
		                 	(
			                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
				      		 	or (
				      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
			      		 		)
			      		 	)
			      		 )
					)">
			
			<div class="row locationPlace">
			 	<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showDetailsPlaceEntrySeveral">
		   				<xsl:with-param name="list" select="$list"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   				<xsl:with-param name="langNode" select="$langNode"/>
		   			</xsl:call-template>
				</div>
			</div>
		</xsl:if>
	</xsl:template>

	<!-- Template for select the correct order to display the address based in
		 the value of the attribute "@localType". -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;addressLine&gt;</code>.</xd:short>
	  	<xd:detail>Template to select the correct order to display the <code>&lt;addressLine&gt;</code> based in the value of the attribute <code>@localType</code>.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;addressLine&gt;</code> elements.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="addressPriorisation">
		<xsl:param name="list"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		
		<!-- Tittle for the address section. -->
		<xsl:if test="($mode = 'showAll' and $list/text())
					or ($mode = 'other' and $list[@xml:lang = $translationLanguage]/text())">			
			<div class="row">
			    <div class="leftcolumn">
		   			<h2>
			   			<xsl:choose>
			    			<xsl:when test="$list/parent::node()[@localType='visitors address']">
			    				<xsl:value-of select="ape:resource('eaccpf.portal.place.address.visitors')"/>
			    			</xsl:when>
			    			<xsl:when test="$list/parent::node()[@localType='postal address']">
					    		<xsl:value-of select="ape:resource('eaccpf.portal.place.address.postal')"/>
			    			</xsl:when>
			    			<xsl:otherwise>
					    		<xsl:value-of select="ape:resource('eaccpf.portal.place.address')"/>
			    			</xsl:otherwise>
		    			</xsl:choose>
		   			<!--<xsl:text>:</xsl:text>-->
	   				</h2>
		   	    </div>
		     	<div class="rightcolumn">
			    </div>
	        </div>
		</xsl:if>
		
		<xsl:if test="$mode = 'default' and (
			(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
				 or $list[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			
			<div class="row">
			    <div class="leftcolumn">
		   			<h2>
			   			<xsl:choose>
			    			<xsl:when test="$list/parent::node()[@localType='visitors address']">
			    				<xsl:value-of select="ape:resource('eaccpf.portal.place.address.visitors')"/>
			    			</xsl:when>
			    			<xsl:when test="$list/parent::node()[@localType='postal address']">
					    		<xsl:value-of select="ape:resource('eaccpf.portal.place.address.postal')"/>
			    			</xsl:when>
			    			<xsl:otherwise>
					    		<xsl:value-of select="ape:resource('eaccpf.portal.place.address')"/>
			    			</xsl:otherwise>
		    			</xsl:choose>
	   				</h2>
		   	    </div>
		     	<div class="rightcolumn">
			    </div>
	        </div>
		</xsl:if>
		
		<!-- addressLine @localType="street" -->
    	<xsl:if test="$list[@localType='street']/text()">
	    	<xsl:call-template name="commonChild">
	   			<xsl:with-param name="list" select="$list[@localType='street']"/>
	   			<xsl:with-param name="clazz" select="'addressLineStreet'"/>
	   			<xsl:with-param name="h2Clazz" select="'subrow'"/>
	   			<xsl:with-param name="posParent" select="$posParent"/>
				<xsl:with-param name="posChild" select="$posChild"/>
				<xsl:with-param name="mode" select="$mode"/>
	    		<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.street'"/>
				<xsl:with-param name="validHrefLink" select="''"/>
	    		<xsl:with-param name="langNode" select="$langNode"/>
	    	</xsl:call-template>
		</xsl:if>

		<!-- addressLine @localType="postalcode" -->
		<xsl:if test="$list[@localType='postalcode']/text()">
    		<xsl:call-template name="commonChild">
	   			<xsl:with-param name="list" select="$list[@localType='postalcode']"/>
	   			<xsl:with-param name="clazz" select="'addressLinePC'"/>
	   			<xsl:with-param name="h2Clazz" select="'subrow'"/>
	   			<xsl:with-param name="posParent" select="$posParent"/>
				<xsl:with-param name="posChild" select="$posChild"/>
				<xsl:with-param name="mode" select="$mode"/>
    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.postalcode'"/>
				<xsl:with-param name="validHrefLink" select="''"/>
   				<xsl:with-param name="langNode" select="$langNode"/>
   			</xsl:call-template>
		</xsl:if>

		<!-- addressLine @localType="firstdem" -->
		<xsl:if test="$list[@localType='firstdem']/text()">
    		<xsl:call-template name="commonChild">
	   			<xsl:with-param name="list" select="$list[@localType='firstdem']"/>
	   			<xsl:with-param name="clazz" select="'addressLineFirst'"/>
	   			<xsl:with-param name="h2Clazz" select="'subrow'"/>
	   			<xsl:with-param name="posParent" select="$posParent"/>
				<xsl:with-param name="posChild" select="$posChild"/>
				<xsl:with-param name="mode" select="$mode"/>
    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.firstdem'"/>
				<xsl:with-param name="validHrefLink" select="''"/>
    			<xsl:with-param name="langNode" select="$langNode"/>
    		</xsl:call-template>
		</xsl:if>

		<!-- addressLine @localType="secondem" -->
		<xsl:if test="$list[@localType='secondem']/text()">
    		<xsl:call-template name="commonChild">
	   			<xsl:with-param name="list" select="$list[@localType='secondem']"/>
	   			<xsl:with-param name="clazz" select="'addressLineSecond'"/>
	   			<xsl:with-param name="h2Clazz" select="'subrow'"/>
	   			<xsl:with-param name="posParent" select="$posParent"/>
				<xsl:with-param name="posChild" select="$posChild"/>
				<xsl:with-param name="mode" select="$mode"/>
    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.secondem'"/>
				<xsl:with-param name="validHrefLink" select="''"/>
    			<xsl:with-param name="langNode" select="$langNode"/>
    		</xsl:call-template>
		</xsl:if>

		<!-- addressLine @localType="localentity" -->
	    <xsl:if test="$list[@localType='localentity']/text()">
    		<xsl:call-template name="commonChild">
	   			<xsl:with-param name="list" select="$list[@localType='localentity']"/>
	   			<xsl:with-param name="clazz" select="'addressLineLocal'"/>
	   			<xsl:with-param name="h2Clazz" select="'subrow'"/>
	   			<xsl:with-param name="posParent" select="$posParent"/>
				<xsl:with-param name="posChild" select="$posChild"/>
				<xsl:with-param name="mode" select="$mode"/>
    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.localentity'"/>
				<xsl:with-param name="validHrefLink" select="''"/>
    			<xsl:with-param name="langNode" select="$langNode"/>
    		</xsl:call-template>
		</xsl:if>

		<!-- addressLine @localType="other" -->
		<xsl:if test="$list[@localType='other']/text() or $list[not(@localType)]/text()">
    		<xsl:call-template name="commonChild">
	   			<xsl:with-param name="list" select="$list[@localType='other' or not(@localType)]"/>
	   			<xsl:with-param name="clazz" select="'addressLineOther'"/>
	   			<xsl:with-param name="h2Clazz" select="'subrow'"/>
	   			<xsl:with-param name="posParent" select="$posParent"/>
				<xsl:with-param name="posChild" select="$posChild"/>
				<xsl:with-param name="mode" select="$mode"/>
    			<xsl:with-param name="title" select="'eaccpf.portal.place.address.information'"/>
				<xsl:with-param name="validHrefLink" select="''"/>
    			<xsl:with-param name="langNode" select="$langNode"/>
    		</xsl:call-template>
		</xsl:if>

		<!-- addressLine @localType="country" -->
		<xsl:if test="$list[@localType='country']/text()">
    		<xsl:call-template name="commonChild">
	   			<xsl:with-param name="list" select="$list[@localType='country']"/>
	   			<xsl:with-param name="clazz" select="'addressLineCountry'"/>
	   			<xsl:with-param name="h2Clazz" select="'subrow'"/>
	   			<xsl:with-param name="posParent" select="$posParent"/>
				<xsl:with-param name="posChild" select="$posChild"/>
				<xsl:with-param name="mode" select="$mode"/>
    			<xsl:with-param name="title" select="'eaccpf.description.combo.address.component.country'"/>
				<xsl:with-param name="validHrefLink" select="''"/>
    			<xsl:with-param name="langNode" select="$langNode"/>
    		</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	
	<!-- template term -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;term&gt;</code>.</xd:short>
	  	<xd:detail>Template to display the details in <code>&lt;term&gt;</code> element according with the mode value 
	  	in the translation combo and the node's language.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;term&gt;</code> elements.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="title">The parameter "title" is the label's title.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="term">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:param name="mode"/>
		<xsl:param name="title"/>
		<xsl:param name="langNode"/>

		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:if test="$list/parent::node()/@localType">
				   			<xsl:text> (</xsl:text>
				   			<xsl:value-of select="$list/parent::node()/@localType"/>
				   			<xsl:text>)</xsl:text>
				   		</xsl:if>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllWithVocabularySource">
		   				<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   			</xsl:call-template>
				</div>
			</div>
			<xsl:if test="@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
			</xsl:if>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/text()">
			<div class="row">
				<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:if test="$list/parent::node()/@localType">
				   			<xsl:text> (</xsl:text>
				   			<xsl:value-of select="$list/parent::node()/@localType"/>
				   			<xsl:text>)</xsl:text>
				   		</xsl:if>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllWithVocabularySource">
		   				<xsl:with-param name="list" select="$list"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   			</xsl:call-template>
				</div>
			</div>
			<xsl:if test="@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
			</xsl:if>
		</xsl:if>
		
		<!-- Try to select the 'default' mode. -->
		
		<xsl:if test="$mode = 'default' and (
			(	$langNode='notLang' and $list[not(@xml:lang)]/text()) 
				 or $list[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			<div class="row">
				<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:if test="$list/parent::node()/@localType">
				   			<xsl:text> (</xsl:text>
				   			<xsl:value-of select="$list/parent::node()/@localType"/>
				   			<xsl:text>)</xsl:text>
				   		</xsl:if>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
			   		<xsl:call-template name="showDetails">
		   				<xsl:with-param name="list" select="$list"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   				<xsl:with-param name="langNode" select="$langNode"/>
		   			</xsl:call-template>
				</div>
			</div>
			<xsl:if test="@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<!--template for common childs -->
	
	<xd:doc>
	  	<xd:short>Template to common parts.</xd:short>
	  	<xd:detail>Template to display the details for commons parts in the node's xml.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of commons elements inside the nodes of xml.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="h2Clazz">The parameter "h2Clazz" is the attribute <code>class</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML h2 tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="title">The parameter "title" is the label's title.</xd:param>
		<xd:param name="validHrefLink">The parameter "validHrefLink" is the link to a valid href.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="commonChild">
		<xsl:param name="list"/>
	    <xsl:param name="clazz"/>
	    <xsl:param name="h2Clazz"/>
	    <xsl:param name="posParent"/>
	    <xsl:param name="posChild"/>
	    <xsl:param name="mode"/>
	    <xsl:param name="title"/>
	    <xsl:param name="validHrefLink"/>
		<xsl:param name="langNode"/>
		
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and ($list[@xml:lang = $translationLanguage]/text()
						or (name($list[1])='citation' and $list[1][@xml:lang = $translationLanguage]))">
			<div class="row">
				<div class="leftcolumn">
			   		<h2 class="{$h2Clazz}">
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllWithVocabularySource">
		   				<xsl:with-param name="list" select="$list[@xml:lang = $translationLanguage]"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   			</xsl:call-template>
				</div>
			</div>
			<xsl:if test="@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
			</xsl:if>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and ($list/text() or name($list[1])='citation')">
			<div class="row">
				<div class="leftcolumn">
			   		<h2 class="{$h2Clazz}">
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllWithVocabularySource">
		   				<xsl:with-param name="list" select="$list"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   			</xsl:call-template>
				</div>
			</div>
			<xsl:if test="@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
			</xsl:if>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
		<xsl:variable name="condSearchTermLang" select="$list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]"/>
		<xsl:variable name="condSearchTermNoLang" select="$langNode!='notLang' and $list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]"/>
		<xsl:variable name="condCitationText" select="name($list[1])='citation' and $list[fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]"/>
		<xsl:variable name="titleCitation" select="$list/@xlink:title"/>
		<xsl:variable name="condCitationNoText" select="name($list[1])='citation' and not($list/text()) and $list[fn:contains(fn:upper-case($titleCitation),fn:upper-case($searchTerms))]"/>	
		
		<xsl:if test="$mode = 'default' and (($langNode='notLang' and $list[not(@xml:lang)]/text()) or $list[@xml:lang=$langNode]/text() or (name($list[1]) ='citation' and (($langNode='notLang' and $list[not(@xml:lang)]) or $list[@xml:lang=$langNode]))
		                                     or ($searchTerms!='' and ($condSearchTermLang or $condSearchTermNoLang  or $condCitationText or $condCitationNoText)))">
																  
			<div class="row">
				<div class="leftcolumn">
			   		<h2 class="{$h2Clazz}">
			   			<xsl:value-of select="ape:resource($title)"/>
			   			<xsl:text>:</xsl:text>
			   		</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showDetails">
		   				<xsl:with-param name="list" select="$list"/>
		   				<xsl:with-param name="clazz" select="$clazz"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
		   				<xsl:with-param name="posChild" select="$posChild"/>
		   				<xsl:with-param name="langNode" select="$langNode"/>
		   			</xsl:call-template>
				</div>
			</div>
			<xsl:if test="@href">
				<xsl:variable name="href" select="./@href"/>
				<a href="{$href}" target="_blank"><xsl:apply-templates select="." mode="other"/></a>
			</xsl:if>
		</xsl:if>	
	</xsl:template>
	
	<!-- Template for select the correct name to display as title based in the
		 value of the attribute "@localType". -->
	
	<xd:doc>
		<xd:short>Template to display the <code>&lt;nameEntry&gt;</code> as title.</xd:short>
	  	<xd:detail>Template for select the correct name to display as title based in the value of the attribute <code>@localType</code>.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;nameEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="namePriorisation">
		<xsl:param name="list"/>

		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<!-- Checks the attribute "@localType". -->
				<xsl:choose>
					<!-- localType = preferred -->
					<xsl:when test="$list[@localType = 'preferred']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'preferred']"/>
						</xsl:call-template>
					</xsl:when>
					<!-- localType = authorized -->
					<xsl:when test="$list[@localType = 'authorized']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'authorized']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- not @localType -->
					<xsl:when test="$list[not(@localType)]">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[not(@localType)]"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- localType = alternative -->
					<xsl:when test="$list[@localType = 'alternative']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'alternative']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- localType = abbreviation -->
					<xsl:when test="$list[@localType = 'abbreviation']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'abbreviation']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- localType = other -->
					<xsl:when test="$list[@localType = 'other']">
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list[@localType = 'other']"/>
						</xsl:call-template> 
					</xsl:when>
					<!-- In any other case checks the whole list. -->
					<xsl:otherwise>
						<xsl:call-template name="multilanguageName">
							<xsl:with-param name="list" select="$list"/>
						</xsl:call-template> 
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>

			<xsl:otherwise>
				<xsl:call-template name="multilanguageName">
					<xsl:with-param name="list" select="$list"/>
				</xsl:call-template> 
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for language nameEntry when translation selected is "default"
		 (value shown in select "Translations"). -->
	
	<xd:doc>
	  	<xd:short>Template to multilanguage <code>&lt;nameEntry&gt;</code>.</xd:short>
	  	<xd:detail>Template for the <code>&lt;nameEntry&gt;y</code> when translation selected is "default" (value shown in selected "Original display").</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;nameEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageName">
		<xsl:param name="list"/>
			<xsl:choose>
				<xsl:when test="count($list) > 1">
					<xsl:choose>
					 	<xsl:when test="$list/eac:part[@xml:lang = $language.selected] and $list/eac:part[@xml:lang = $language.selected]/text() and $list/eac:part[@xml:lang = $language.selected]/text() != ''"> 
						    <xsl:for-each select="$list/eac:part[@xml:lang = $language.selected]">
								<xsl:if test="position()=1"> 
										<xsl:call-template name="compositeName">
											<xsl:with-param name="listName" select="current()/parent::node()"/>
											<xsl:with-param name="isHeader" select="'true'"/>
										</xsl:call-template> 
							 	</xsl:if> 
					 	    </xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list/eac:part[@xml:lang = $lang.navigator] and $list/eac:part[@xml:lang = $lang.navigator]/text() and $list/eac:part[@xml:lang = $lang.navigator]/text() != ''"> 
						    <xsl:for-each select="$list/eac:part[@xml:lang = $lang.navigator]">
								<xsl:if test="position()=1"> 
										<xsl:call-template name="compositeName">
											<xsl:with-param name="listName" select="current()/parent::node()"/>
											<xsl:with-param name="isHeader" select="'true'"/>
										</xsl:call-template> 
							 	</xsl:if> 
					 	    </xsl:for-each> 
						</xsl:when>
						<xsl:when test="$list/eac:part[@xml:lang = $language.default] and $list/eac:part[@xml:lang = $language.default]/text() and $list/eac:part[@xml:lang = $language.default]/text() != ''">
						 	<xsl:for-each select="$list/eac:part[@xml:lang = $language.default]"> 
								  <xsl:if test="position()=1">
										<xsl:call-template name="compositeName"> 
											<xsl:with-param name="listName" select="current()/parent::node()"/>
											<xsl:with-param name="isHeader" select="'true'"/>
										</xsl:call-template> 
								 </xsl:if>
						  	</xsl:for-each>
						</xsl:when>
						<xsl:when test="$list/eac:part[not(@xml:lang)] and $list/eac:part[not(@xml:lang)]/text() and $list/eac:part[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list/eac:part[not(@xml:lang)]"> 
							 	  <xsl:if test="position()=1"> 
									 <xsl:call-template name="compositeName">
												<xsl:with-param name="listName" select="current()/parent::node()"/>
												<xsl:with-param name="isHeader" select="'true'"/>
									 </xsl:call-template> 
							  	 </xsl:if>
						   	</xsl:for-each> 
						</xsl:when>
						<xsl:otherwise> 
						 	<xsl:variable name="language.first" select="$list[1]/eac:part[@xml:lang]"></xsl:variable>
							<xsl:for-each select="$list">
								<xsl:variable name="currentLang" select="current()/eac:part[@xml:lang]"></xsl:variable>
								<xsl:variable name="currentNode" select="current()/eac:part"></xsl:variable>
										<xsl:if test="$currentLang = $language.first">
											<xsl:if test="position() = 1">
												<xsl:call-template name="compositeName">
													<xsl:with-param name="listName" select="$currentNode/parent::node()"/>
													<xsl:with-param name="isHeader" select="'true'"/>
										   		</xsl:call-template> 
										  	</xsl:if>
										</xsl:if>
							</xsl:for-each>
					   </xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$list">
							<xsl:call-template name="compositeName">
								<xsl:with-param name="listName" select="current()"/>
								<xsl:with-param name="isHeader" select="'true'"/>
							</xsl:call-template> 
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
	</xsl:template>

	<!-- template for compose the name -->
	
	<xd:doc>
	  	<xd:short>Template to compose the name.</xd:short>
	  	<xd:detail>Template to compose the different <code>&lt;part&gt;</code> into <code>&lt;nameEntry&gt;</code> element.</xd:detail>
		<xd:param name="listName">The parameter "list" is an array of <code>&lt;nameEntry&gt;</code> elements.</xd:param>
		<xd:param name="isHeader" type="boolean">The parameter "isHeader" is to display or not the alternative names.</xd:param>
	</xd:doc>
	<xsl:template name="compositeName">
		<xsl:param name="listName"/>
		<xsl:param name="isHeader"/>
	    <xsl:call-template name="nameEntry">
	    	<xsl:with-param name="listName" select="$listName"/>
            <xsl:with-param name="isHeader" select="$isHeader"/>
	    </xsl:call-template>
	    <xsl:if test="$isHeader = 'true'"> <!-- to display the alternative names -->
  			<span id="nameTitle" class="hidden">
  				<xsl:call-template name="nameEntry">
  					<xsl:with-param name="listName" select="$listName"/>
                    <xsl:with-param name="isHeader" select="$isHeader"/>
  				</xsl:call-template>
  			</span>
  		</xsl:if>	
	</xsl:template> 
	
	<!-- template nameEntry -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;nameEntry&gt;</code>.</xd:short>
	  	<xd:detail>Template to display the <code>&lt;nameEntry&gt;</code> element.</xd:detail>
		<xd:param name="listName">The parameter "listName" is an array of <code>&lt;nameEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="nameEntry">
		<xsl:param name="listName"/>
        <xsl:param name="isHeader" />
    	<xsl:variable name="firstName" select="$listName/eac:part[@localType='firstname']"/>
		<xsl:variable name="surName" select="$listName/eac:part[@localType='surname']"/>
		<xsl:variable name="patronymic" select="$listName/eac:part[@localType='patronymic']"/>
	    <xsl:variable name="prefix" select="$listName/eac:part[@localType='prefix']"/>
	    <xsl:variable name="suffix" select="$listName/eac:part[@localType='suffix']"/>
		<xsl:variable name="alias" select="$listName/eac:part[@localType='alias']"/>
		<xsl:variable name="title" select="$listName/eac:part[@localType='title']"/>
		<xsl:variable name="birthname" select="$listName/eac:part[@localType='birthname']"/>
		<xsl:variable name="legalform" select="$listName/eac:part[@localType='legalform']"/>
		<xsl:variable name="corpname" select="$listName/eac:part[@localType='corpname']"/>
		<xsl:variable name="famname" select="$listName/eac:part[@localType='famname']"/>
		<xsl:variable name="persname" select="$listName/eac:part[@localType='persname']"/>
		<xsl:choose>
			<xsl:when test="$surName and $firstName">
				<xsl:for-each select="$surName">
					<xsl:apply-templates select="." mode="other"/>
					<xsl:if test="position() != last()">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:if test="$birthname">
					<xsl:text> (</xsl:text>
					<xsl:for-each select="$birthname">
						<xsl:apply-templates select="." mode="other"/>
						<xsl:if test="position() != last()">
							<xsl:text> </xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>)</xsl:text>
				</xsl:if>
				<xsl:if test="$prefix">
					<xsl:text>, </xsl:text>
					<xsl:for-each select="$prefix">
						<xsl:apply-templates select="." mode="other"/>
						<xsl:if test="position() != last()">
							<xsl:text> </xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:text>, </xsl:text><!--comma for firstName-->
				<xsl:for-each select="$firstName">
					<xsl:apply-templates select="." mode="other"/>
					<xsl:if test="position() != last()">
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:if test="$patronymic">
					<xsl:text> </xsl:text>
					<xsl:for-each select="$patronymic">
						<xsl:apply-templates select="." mode="other"/>
						<xsl:if test="position() != last()">
							<xsl:text> </xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:if test="$suffix">
					<xsl:text>, </xsl:text>
					<xsl:for-each select="$suffix">
						<xsl:apply-templates select="." mode="other"/>
						<xsl:if test="position() != last()">
							<xsl:text> </xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:if test="$title">
					<xsl:text>, </xsl:text>
					<xsl:for-each select="$title">
						<xsl:apply-templates select="." mode="other"/>
						<xsl:if test="position() != last()">
							<xsl:text> </xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
				<xsl:if test="$alias">
					<xsl:text> (</xsl:text>
					<xsl:value-of select="ape:resource('eaccpf.portal.alias')"/>
					<xsl:text>: </xsl:text>
					<xsl:for-each select="$alias">
						<xsl:apply-templates select="." mode="other"/>
						<xsl:if test="position() != last()">
							<xsl:text> </xsl:text>
						</xsl:if>
					</xsl:for-each>
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$corpname and $legalform">
						<xsl:apply-templates select="$corpname" mode="other"/>
						<xsl:text> </xsl:text>
						<xsl:apply-templates select="$legalform" mode="other"/>
					</xsl:when>
					<xsl:when test="$persname">
						<xsl:apply-templates select="$persname" mode="other"/>
					</xsl:when>
					<xsl:when test="$famname">
						<xsl:apply-templates select="$famname" mode="other"/>
					</xsl:when>
					<xsl:otherwise>
                                            <xsl:for-each select="$listName/eac:part">
						<xsl:if test="position() != 1">
							<xsl:text> </xsl:text>
						</xsl:if>
                                                <xsl:apply-templates select="." mode="other"/>
                                            </xsl:for-each>
                                        </xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- template vocabularySource -->
	
	<xd:doc>
	  	<xd:short>Template to <code>@vocabularySource</code>.</xd:short>
	  	<xd:detail>Template to display the attribute <code>@vocabularySource</code>.</xd:detail>
		<xd:param name="node">The parameter "node" is the element that has the attribute <code>@vocabularySource</code>.</xd:param>
	</xd:doc>
	<xsl:template name="vocabularySource">
		<xsl:param name="node"/>
		<xsl:variable name="link" select="$node/@vocabularySource"/>
		<xsl:if test="not(starts-with($link, 'http')) and not(starts-with($link, 'https')) and not(starts-with($link, 'ftp')) and not(starts-with($link, 'www'))">
			<xsl:variable name="aiCodeEac" select="ape:aiFromEac($link, '')"/>
			<xsl:variable name="aiCodeEad" select="ape:aiFromEad($link, '')"/>
			<xsl:variable name="aiCodeEag" select="ape:checkAICode($link, '', 'true')" />
			<xsl:choose>
				<xsl:when test="$aiCodeEac != 'ERROR' and $aiCodeEac != ''">
					<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEac)" />
					<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--					<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
					<a href="{$eacUrlBase}?repositoryCode={$aiCodeEac}&amp;recordId={$link}" target="_blank">
						<xsl:apply-templates select="$node" mode="other"/>
					</a>
				</xsl:when>
				<xsl:when test="$aiCodeEad != 'ERROR' and $aiCodeEad != ''">
					<a href="{$eadUrl}/{$aiCodeEad}" target="_blank">
						<xsl:apply-templates select="$node" mode="other"/>
					</a>
				</xsl:when>
				<xsl:when test="$aiCodeEag != 'ERROR' and $aiCodeEag != ''">
					<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEag)" />
<!--					<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
					<a href="{$aiCodeUrl}?repositoryCode={$aiCodeEag}" target="_blank">
						<xsl:apply-templates select="$node" mode="other"/>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="$node" mode="other"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if> 
		<xsl:if test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
			<a href="{$link}" target="_blank">
				<xsl:apply-templates select="$node" mode="other"/>
			</a>
		</xsl:if>	
	</xsl:template>	

	<!-- template for nodes with attribute @vocabularySource or not and several elements-->
	
	<xd:doc>
	  	<xd:short>Template for elements with the attribute <code>@vocabularySource</code>.</xd:short>
	  	<xd:detail>Template to display the elements with the attribute <code>@vocabularySource</code> or not and several elements according with the language's rules.</xd:detail>
		<xd:param name="list">The parameter "list" is an array of elements with the attribute <code>@vocabularySource</code>.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="validHrefLink">The parameter "validHrefLink" is the link to a valid href.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageWithVocabularySource">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:param name="validHrefLink"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
			<div id="{$clazz}{$posParent}{$posChild}" class= "moreDisplay">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected]
										and (($list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != '')
											or ($list[@xml:lang = $language.selected and @xlink:href != '' and $validHrefLink = 'true'])
											or $list[@xml:lang = $language.selected and @xlink:title != ''])">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(($currentLang = $language.selected) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<p>
									<xsl:choose>
										<xsl:when test="@vocabularySource">
											<xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="name(current()) = 'citation'">
											<xsl:call-template name="citationHref">
												<xsl:with-param name="link" select="./@xlink:href"/>
												<xsl:with-param name="title" select="./@xlink:title" />
												<xsl:with-param name="content" select="./text()"/>
												<xsl:with-param name="section" select="$list"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								</p>
							</xsl:if>		
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator]
										and (($list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != '')
											or ($list[@xml:lang = $lang.navigator and @xlink:href != '' and $validHrefLink = 'true'])
											or $list[@xml:lang = $lang.navigator and @xlink:title != ''])">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(($currentLang = $lang.navigator) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">	
								<p>
									<xsl:choose>
										<xsl:when test="@vocabularySource">
											<xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="name(current()) = 'citation'">
											<xsl:call-template name="citationHref">
												<xsl:with-param name="link" select="./@xlink:href"/>
												<xsl:with-param name="title" select="./@xlink:title" />
												<xsl:with-param name="content" select="./text()"/>
												<xsl:with-param name="section" select="$list"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								</p>
							</xsl:if>		
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default]
										and (($list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != '')
											or ($list[@xml:lang = $language.default and @xlink:href != '' and $validHrefLink = 'true'])
											or $list[@xml:lang = $language.default and @xlink:title != ''])">
						<!--  <xsl:for-each select="$list[@xml:lang = $language.default]">-->
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(($currentLang = $language.default) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">	
								<p>
									<xsl:choose>
										<xsl:when test="@vocabularySource">
											<xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="name(current()) = 'citation'">
											<xsl:call-template name="citationHref">
												<xsl:with-param name="link" select="./@xlink:href"/>
												<xsl:with-param name="title" select="./@xlink:title" />
												<xsl:with-param name="content" select="./text()"/>
												<xsl:with-param name="section" select="$list"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
										 <xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								</p>
							</xsl:if>		
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)]
										and (($list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != '')
											or ($list[not(@xml:lang) and @xlink:href != '' and $validHrefLink = 'true'])
											or $list[not(@xml:lang) and @xlink:title != ''])">
					  <!-- 	<xsl:for-each select="$list[not(@xml:lang)]"> -->
						 <xsl:for-each select="$list">
						   <xsl:if test="not(@xml:lang) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms)))"> 	 
						 	  <p>
								 <xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="name(current()) = 'citation'">
										<xsl:call-template name="citationHref">
											<xsl:with-param name="link" select="./@xlink:href"/>
											<xsl:with-param name="title" select="./@xlink:title" />
											<xsl:with-param name="content" select="./text()"/>
											<xsl:with-param name="section" select="$list"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
									 <xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
							  </p>
							</xsl:if>	     
					   	</xsl:for-each> 
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:variable name="currentText" select="current()/text()"></xsl:variable>
							<xsl:if test="(($currentLang = $language.first) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<p>
									<xsl:choose>
										<xsl:when test="@vocabularySource">
										    <xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="name(current()) = 'citation'">
											<xsl:call-template name="citationHref">
												<xsl:with-param name="link" select="./@xlink:href"/>
												<xsl:with-param name="title" select="./@xlink:title" />
												<xsl:with-param name="content" select="./text()"/>
												<xsl:with-param name="section" select="$list"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
										 	<xsl:apply-templates select="$currentText" mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
				<div class="linkMore">
					<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}{$posChild}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
					</a>
					<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}{$posChild}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
					</a>
				</div>
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p>
						<xsl:choose>
							<xsl:when test="@vocabularySource">
								<xsl:call-template name="vocabularySource">
									<xsl:with-param name="node" select="."/>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="name(current()) = 'citation'">
								<xsl:call-template name="citationHref">
									<xsl:with-param name="link" select="./@xlink:href"/>
									<xsl:with-param name="title" select="./@xlink:title" />
									<xsl:with-param name="content" select="./text()"/>
									<xsl:with-param name="section" select="$list"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
							 	<xsl:apply-templates select="." mode="other"/> 
							</xsl:otherwise>
						</xsl:choose>
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template that will show all the elements in the passed list
		 dependent of its language. (Template for nodes with attribute
		 @vocabularySource or not and several elements). -->
	
	<xd:doc>
	  <xd:short>Template to show all the elements with the attribute <code>@vocabularySource</code>.</xd:short>
	  <xd:detail>Template that will show all the elements in the passed list dependent of its language with attribute
	  <code>@vocabularySource</code> or not and several elements. 
	  </xd:detail>
		<xd:param name="list">The parameter "list" is an array of elements with the attribute <code>@vocabularySource</code>.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="showAllWithVocabularySource">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>

		<div id="{$clazz}{$posParent}{$posChild}" class= "moreDisplay">
			<xsl:for-each select="$list">
				<p>
					<xsl:choose>
						<xsl:when test="@vocabularySource">
							<xsl:call-template name="vocabularySource">
								<xsl:with-param name="node" select="."/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="name(current()) = 'citation'">
							<xsl:call-template name="citationHref">
								<xsl:with-param name="link" select="./@xlink:href"/>
								<xsl:with-param name="title" select="./@xlink:title" />
								<xsl:with-param name="content" select="./text()"/>
								<xsl:with-param name="section" select="$list"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="@countryCode">
						<xsl:call-template name="countryName">
							<xsl:with-param name="countryCode" select="./@countryCode"/>
	   			        </xsl:call-template>
   			        </xsl:if>
				</p>	
			</xsl:for-each>
			<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>
		</div>
	</xsl:template>
	
	<xd:doc>
		<xd:short>Template to show all the elements <code>&lt;descriptiveNote&gt;</code>.</xd:short>
	  <xd:detail>Template that will show all the elements <code>&lt;descriptiveNote&gt;</code> in the passed list dependent of its language. 
	  </xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;descriptiveNote&gt;</code> elements.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute "id" in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute "id" in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="showDetailsDescriptiveNote">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		<xsl:param name="posParent"/>
		
		<div id="{$clazz}{$posParent}" class= "moreDisplay">
			<xsl:for-each select="$list">
				<xsl:choose>
					<xsl:when test=".[@xml:lang=$langNode]/text() or ($langNode='notLang' and .[not(@xml:lang)]/text())">
						<p>
							<xsl:apply-templates select="." mode="other"/>
							<xsl:if test="name(current()) = 'language'">
								<xsl:call-template  name="descriptiveNoteNoP">
									<xsl:with-param name="element" select="current()"/>
									<xsl:with-param name="mode" select="$mode"/>
									<xsl:with-param name="clazz" select="'language'"/>
									<xsl:with-param name="langNode" select="$langNode"/>								
								</xsl:call-template>
							</xsl:if>
						</p>
					</xsl:when>
					<xsl:when test="$searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))">
						<p>
							<xsl:apply-templates select="." mode="other"/>
							<xsl:if test="name(current()) = 'language'">
								<xsl:call-template  name="descriptiveNoteNoP">
									<xsl:with-param name="element" select="current()"/>
									<xsl:with-param name="mode" select="$mode"/>
									<xsl:with-param name="clazz" select="'language'"/>
									<xsl:with-param name="langNode" select="$langNode"/>
								</xsl:call-template>
							</xsl:if>
						</p>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>	
			</xsl:for-each>

			<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>
		</div>
	</xsl:template>
	
	<!-- Template to show the information according with the language and the search term  -->
	
	<xd:doc>
	  	<xd:short>Template to show details of the eac-cpf.</xd:short>
	 	 <xd:detail>Template to show the information according with the language and the search term.
	 	 </xd:detail>
		<xd:param name="list">The parameter "list" is an array of the elements in the eac-cpf.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="showDetails">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:param name="langNode"/>
		
		
		<div id="{$clazz}{$posParent}{$posChild}" class= "moreDisplay">
			<xsl:for-each select="$list">
				<xsl:choose>
					<xsl:when test=".[@xml:lang=$langNode]/text() or ($langNode='notLang' and .[not(@xml:lang)]/text()) or (name(.)='citation'and (@xml:lang=$langNode) or ($langNode='notLang' and not(@xml:lang)))">
						<p>
							<xsl:choose>
								<xsl:when test="@vocabularySource">
									<xsl:call-template name="vocabularySource">
										<xsl:with-param name="node" select="."/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="name(current()) = 'citation'">
									<xsl:call-template name="citationHref">
										<xsl:with-param name="link" select="./@xlink:href"/>
										<xsl:with-param name="title" select="./@xlink:title" />
										<xsl:with-param name="content" select="./text()"/>
										<xsl:with-param name="section" select="$list"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/> 
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="@countryCode">
								<xsl:call-template name="countryName">
									<xsl:with-param name="countryCode" select="./@countryCode"/>
			   			        </xsl:call-template>
		   			        </xsl:if>
						</p>
					</xsl:when>
					<xsl:when test="$searchTerms != '' and (fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))
									                        or (name(.)='citation' and not(./text()) and fn:contains(fn:upper-case(./@xlink:title),fn:upper-case($searchTerms))))">
						<p>

							<xsl:choose>
								<xsl:when test="@vocabularySource">
									<xsl:call-template name="vocabularySource">
										<xsl:with-param name="node" select="."/>
									</xsl:call-template>
								</xsl:when>
								<xsl:when test="name(current()) = 'citation'">
									<xsl:call-template name="citationHref">
										<xsl:with-param name="link" select="./@xlink:href"/>
										<xsl:with-param name="title" select="./@xlink:title" />
										<xsl:with-param name="content" select="./text()"/>
										<xsl:with-param name="section" select="$list"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/> 
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="@countryCode">
								<xsl:call-template name="countryName">
									<xsl:with-param name="countryCode" select="./@countryCode"/>
			   			        </xsl:call-template>
		   			        </xsl:if>
						</p>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>	
			</xsl:for-each>
			<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>
		</div>
	</xsl:template>
	
	<xd:doc>
	  	<xd:short>Template to title in <code>&lt;citation&gt;</code>.</xd:short>
	  	<xd:detail>Template to show the title in <code>&lt;citation&gt;</code>.
	  	</xd:detail>
		<xd:param name="content">The parameter "content" is the content of the element <code>&lt;citation&gt;</code>.</xd:param>
		<xd:param name="section">The parameter "section" is the section which <code>&lt;citation&gt;</code> belongs.</xd:param>
		<xd:param name="title">The parameter "title" is the title in the attribute <code>@xlink:title</code>.</xd:param>
	</xd:doc>
	<xsl:template name="citationHrefTitle">
		<xsl:param name="content"/>
		<xsl:param name="section" />
		<xsl:param name="title"/>
		<xsl:variable name="name" select="name($section/parent::node())"/>
		<xsl:choose>
			<xsl:when test="$content != '' ">
				<xsl:apply-templates select="$content" mode="other"/> 
			</xsl:when>
			<xsl:when test="$title != ''">
				<xsl:value-of select="fn:normalize-space(ape:highlight($title, 'other'))" disable-output-escaping="yes" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="ape:resource('eaccpf.portal.seeCitation')"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for citation with/without @href -->
	
	<xd:doc>
	  <xd:short>Template to show <code>&lt;citation&gt;</code> with/without the attribute <code>@href</code>.
	  </xd:short>
		<xd:param name="link">The parameter "link" is the content of <code>@xlink:href</code>.</xd:param>
		<xd:param name="title">The parameter "title" is the title in the attribute <code>@xlink:title</code>.</xd:param>
		<xd:param name="content">The parameter "content" is the content of the element <code>&lt;citation&gt;</code>.</xd:param>
		<xd:param name="section">The parameter "section" is the section which <code>citation</code> belongs.</xd:param>
	</xd:doc>
	<xsl:template name="citationHref">
		<xsl:param name="link"/>
		<xsl:param name="title"/>
		<xsl:param name="content"/>
		<xsl:param name="section" />
		<xsl:choose>
			<!--link ok -->
			<xsl:when test="starts-with($link, 'http') or starts-with($link, 'https') or starts-with($link, 'ftp') or starts-with($link, 'www')">
				<a href="{$link}" target="_blank">
					<xsl:call-template name="citationHrefTitle">
						<xsl:with-param name="content" select="$content" />
						<xsl:with-param name="section" select="$section" />
						<xsl:with-param name="title" select="$title" />
					</xsl:call-template>
				</a>
			</xsl:when>
			<!-- internal links -->
			<xsl:when test="$link != '' and not(starts-with($link, 'http')) and 
							not(starts-with($link, 'https')) and 
							not(starts-with($link, 'ftp')) and 
							not(starts-with($link, 'www'))">
				<xsl:variable name="aiCodeEac" select="ape:aiFromEac($link, '')"/>
				<xsl:variable name="aiCodeEad" select="ape:aiFromEad($link, '')"/>
				<xsl:variable name="aiCodeEag" select="ape:checkAICode($link, '', 'true')" />
				<xsl:choose>
					<xsl:when test="$aiCodeEac != 'ERROR' and $aiCodeEac != ''">
						<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEac)" />
						<xsl:variable name="encodedlink" select="ape:encodeSpecialCharacters($link)" />
<!--						<a href="{$eacUrlBase}/aicode/{$encodedAiCode}/type/ec/id/{$encodedlink}" target="_blank">-->
						<a href="{$eacUrlBase}?repositoryCode={$aiCodeEac}&amp;recordId={$link}" target="_blank">
							<xsl:call-template name="citationHrefTitle">
								<xsl:with-param name="content" select="$content" />
								<xsl:with-param name="section" select="$section" />
								<xsl:with-param name="title" select="$title" />
							</xsl:call-template>
						</a>
					</xsl:when>
					<xsl:when test="$aiCodeEad != 'ERROR' and $aiCodeEad != ''">
						<a href="{$eadUrl}/{$aiCodeEad}" target="_blank">
							<xsl:call-template name="citationHrefTitle">
								<xsl:with-param name="content" select="$content" />
								<xsl:with-param name="section" select="$section" />
								<xsl:with-param name="title" select="$title" />
							</xsl:call-template>
						</a>
					</xsl:when>
					<xsl:when test="$aiCodeEag != 'ERROR' and $aiCodeEag != ''">
						<xsl:variable name="encodedAiCode" select="ape:encodeSpecialCharacters($aiCodeEag)" />
<!--						<a href="{$aiCodeUrl}/{$encodedAiCode}" target="_blank">-->
						<a href="{$aiCodeUrl}?repositoryCode={$aiCodeEag}" target="_blank">
							<xsl:call-template name="citationHrefTitle">
								<xsl:with-param name="content" select="$content" />
								<xsl:with-param name="section" select="$section" />
								<xsl:with-param name="title" select="$title" />
							</xsl:call-template>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="citationHrefTitle">
							<xsl:with-param name="content" select="$content" />
							<xsl:with-param name="section" select="$section" />
							<xsl:with-param name="title" select="$title" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!--link ko -->
			<xsl:otherwise>
				<xsl:call-template name="citationHrefTitle">
					<xsl:with-param name="content" select="$content" />
					<xsl:with-param name="section" select="$section" />
					<xsl:with-param name="title" select="$title" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for the details in /places/place -->
	
	<xd:doc>
	  	<xd:short>Template to <code>&lt;place&gt;</code>.</xd:short>
	  	<xd:detail>Template to show the details in <code>&lt;place&gt;</code> inside <code>&lt;places&gt;</code>.
	  	</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;place&gt;</code> elements.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="validHrefLink">The parameter "validHrefLink" is the link to a valid href.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="placeDetails">
		<xsl:param name="list"/>
		<xsl:param name="mode"/>
		<xsl:param name="posChild"/>
		<xsl:param name="posParent"/>
		<xsl:param name="validHrefLink"/>
		<xsl:param name="langNode"/>
			
			<div class="blockSingular">	
				
			   <!--  placeEntry in place  --> 
		  	 	<xsl:call-template name="placePriorisation">
		    		<xsl:with-param name="list" select="$list"/>
	   				<xsl:with-param name="posParent" select="$posParent"/>
	   				<xsl:with-param name="posChild" select="$posChild"/>
	   				<xsl:with-param name="mode" select="$mode"/>
	    			<xsl:with-param name="langNode" select="$langNode"/>
	    		</xsl:call-template> 
	
		      <!--   placeRole  -->
		       <xsl:if test="($mode = 'showAll' and $list/eac:placeRole/text() and $list/eac:placeEntry/text())
				       		or ($mode = 'other' and $list/eac:placeRole[@xml:lang = $translationLanguage]/text() and $list/eac:placeEntry[@xml:lang = $translationLanguage]/text())">
			        <div class="row">
					    <div class="leftcolumn">
				   			<h2>
				   				<xsl:value-of select="ape:resource('eaccpf.portal.roleOfLocation')"/>
				   				<xsl:text>:</xsl:text>
				   			</h2>
				   	    </div>
				     	<div class="rightcolumn">
				     		<xsl:call-template name="multilanguageWithVocabularySource">
	  								<xsl:with-param name="list" select="$list/eac:placeRole"/>
				   				<xsl:with-param name="clazz" select="'placeRole_'"/>
				   				<xsl:with-param name="posParent" select="$posParent"/>
				   				<xsl:with-param name="posChild" select="$posChild"/>
		   						<xsl:with-param name="validHrefLink" select="''"/>
				   			</xsl:call-template>
					    </div>
			        </div>
			    </xsl:if> 
			
				<xsl:if test="$mode = 'default' and (
							(	$langNode='notLang' and $list/eac:placeRole[not(@xml:lang)]/text()) 
								 or $list/eac:placeRole[@xml:lang=$langNode]/text() 
				                 or ($searchTerms!='' and 
				                 	(
					                 	$list/eac:placeRole[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
						      		 	or (
						      		 		$langNode!='notLang' and $list/eac:placeRole[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
					      		 		)
					      		 	)
					      		 )
							)">

			
					<div class="row">
					    <div class="leftcolumn">
				   			<h2>
				   				<xsl:value-of select="ape:resource('eaccpf.portal.roleOfLocation')"/>
				   				<xsl:text>:</xsl:text>
				   			</h2>
				   	    </div>
				     	<div class="rightcolumn">
				     		<xsl:call-template name="showDetails">
	  							<xsl:with-param name="list" select="$list/eac:placeRole"/>
				   				<xsl:with-param name="clazz" select="'placeRole_'"/>
				   				<xsl:with-param name="posParent" select="$posParent"/>
				   				<xsl:with-param name="posChild" select="$posChild"/>
				   				<xsl:with-param name="langNode" select="$langNode"/>
				   			</xsl:call-template>
					    </div>
			        </div>
				</xsl:if>
			    
				<!--  addressLine  -->
			 	<xsl:call-template name="addressPriorisation">
		    		<xsl:with-param name="list" select="$list/eac:address/eac:addressLine"/>
	   				<xsl:with-param name="posParent" select="$posParent"/>
	   				<xsl:with-param name="posChild" select="$posChild"/>
	   				<xsl:with-param name="mode" select="$mode"/>
	    			<xsl:with-param name="langNode" select="$langNode"/>
	    		</xsl:call-template> 
	
				<!--   dates in place -->
			  	<xsl:call-template name="commonDates">
		    		<xsl:with-param name="date" select="$list/eac:date"/>
		    		<xsl:with-param name="dateRange" select="$list/eac:dateRange"/>
		    		<xsl:with-param name="dateSet" select="$list/eac:dateSet"/>
	   				<xsl:with-param name="mode" select="$mode"/>
		    		<xsl:with-param name="langNode" select="$langNode"/>
		    	</xsl:call-template>
	
				<!--  descriptive note place -->
				<xsl:if test="$list/eac:descriptiveNote/eac:p/text()">   
		    	 	<xsl:call-template name="commonChild">
			    		<xsl:with-param name="list" select="$list/eac:descriptiveNote/eac:p"/>
			    		<xsl:with-param name="clazz" select="'descriptiveNotelocationPlace_'"/>					   			
		   				<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
		   				<xsl:with-param name="posParent" select="$posParent"/>
	   					<xsl:with-param name="posChild" select="$posChild"/>
	   					<xsl:with-param name="mode" select="$mode"/>
		    			<xsl:with-param name="title" select="'eaccpf.portal.note'"/>
						<xsl:with-param name="validHrefLink" select="''"/>
	    				<xsl:with-param name="langNode" select="$langNode"/>
	    			</xsl:call-template> 
			    </xsl:if>
	
			    <!--  citation in place  -->
			    <xsl:call-template name="commonChild">
		    		<xsl:with-param name="list" select="$list/eac:citation"/>
		    		<xsl:with-param name="clazz" select="'citationlocationPlace_'"/>
		    		<xsl:with-param name="h2Clazz" select="'noSubrow'"/>
		    		<xsl:with-param name="posParent" select="$posParent"/>
		    		<xsl:with-param name="posChild" select="$posChild"/>
	  				<xsl:with-param name="mode" select="$mode"/>
		    		<xsl:with-param name="title" select="'eaccpf.relations.link'"/>
					<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
		    		<xsl:with-param name="langNode" select="$langNode"/>
		    	</xsl:call-template>
	    	</div>
	</xsl:template>
	 
	 <!--template to order eac:placeEntry into eac:places level -->
	
	<xd:doc>
	  	<xd:short>Template to order <code>&lt;placeEntry&gt;</code>.</xd:short>
	  	<xd:detail>Template to order <code>&lt;placeEntry&gt;</code> into <code>&lt;places&gt;</code> level.
	  	</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;place&gt;</code> elements.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
		<xd:param name="validHrefLink">The parameter "validHrefLink" is the link to a valid href.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="placesPriorisation">
		<xsl:param name="list"/>
		<xsl:param name="posParent"/>
		<xsl:param name="mode"/>
		<xsl:param name="validHrefLink"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks the attribute "@localType". -->
		<!-- localType = birth -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'birth']">
			<xsl:for-each select="$list">
				<xsl:variable name="posChild" select="position()"/>
				<xsl:if test="./eac:placeEntry[@localType = 'birth']">
					<xsl:call-template name="placeDetails">
						<xsl:with-param name="list" select="."/>
					  	<xsl:with-param name="posChild" select="$posChild"/>
						<xsl:with-param name="posParent" select="$posParent"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
						<xsl:with-param name="langNode" select="$langNode"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>

		<!-- localType = foundation -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'foundation']">
			<xsl:for-each select="$list">
				<xsl:variable name="posChild" select="position()"/>
				<xsl:if test="./eac:placeEntry[@localType ='foundation'] and not(./eac:placeEntry[@localType = 'birth'])">
				  	<xsl:call-template name="placeDetails">
						<xsl:with-param name="list" select="."/>
						<xsl:with-param name="posChild" select="$posChild"/>
						<xsl:with-param name="posParent" select="$posParent"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
						<xsl:with-param name="langNode" select="$langNode"/>
					</xsl:call-template>	
				</xsl:if>
			</xsl:for-each>
		</xsl:if>

		<!-- localType = private-residence -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'private-residence']">
			<xsl:for-each select="$list">
				<xsl:variable name="posChild" select="position()"/>
				<xsl:if test="./eac:placeEntry[@localType ='private-residence'] and not(./eac:placeEntry[@localType = 'birth']) and not(./eac:placeEntry[@localType = 'foundation'])">
				  	<xsl:call-template name="placeDetails">
						<xsl:with-param name="list" select="."/>
						<xsl:with-param name="posChild" select="$posChild"/>
						<xsl:with-param name="posParent" select="$posParent"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
						<xsl:with-param name="langNode" select="$langNode"/>
					</xsl:call-template>	
				</xsl:if>
			</xsl:for-each>
		</xsl:if>

		<!-- localType = business-residence -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'business-residence']">
			<xsl:for-each select="$list">
				<xsl:variable name="posChild" select="position()"/>
				<xsl:if test="./eac:placeEntry[@localType ='business-residence'] and not(./eac:placeEntry[@localType = 'birth']) and not(./eac:placeEntry[@localType = 'foundation'])
								and not(./eac:placeEntry[@localType = 'private-residence'])">
				  	<xsl:call-template name="placeDetails">
						<xsl:with-param name="list" select="."/>
						<xsl:with-param name="posChild" select="$posChild"/>
						<xsl:with-param name="posParent" select="$posParent"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
						<xsl:with-param name="langNode" select="$langNode"/>
					</xsl:call-template>	
				</xsl:if>
			</xsl:for-each>
		</xsl:if>

		<!-- localType = death -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'death']">
			<xsl:for-each select="$list">
				<xsl:variable name="posChild" select="position()"/>
				<xsl:if test="./eac:placeEntry[@localType ='death'] and not(./eac:placeEntry[@localType = 'birth']) and not(./eac:placeEntry[@localType = 'foundation'])
								and not(./eac:placeEntry[@localType = 'private-residence']) and not(./eac:placeEntry[@localType = 'business-residence'])">
				  	<xsl:call-template name="placeDetails">
						<xsl:with-param name="list" select="."/>
						<xsl:with-param name="posChild" select="$posChild"/>
						<xsl:with-param name="posParent" select="$posParent"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
						<xsl:with-param name="langNode" select="$langNode"/>
					</xsl:call-template>	
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
		
		<!-- localType = suppression -->
		<xsl:if test="$list/eac:placeEntry[@localType = 'suppression']">
			<xsl:for-each select="$list">
				<xsl:variable name="posChild" select="position()"/>
				<xsl:if test="./eac:placeEntry[@localType ='suppression'] and not(./eac:placeEntry[@localType = 'birth']) and not(./eac:placeEntry[@localType = 'foundation'])
								and not(./eac:placeEntry[@localType = 'private-residence']) and not(./eac:placeEntry[@localType = 'business-residence'])
								and not(./eac:placeEntry[@localType = 'death'])">
				  	<xsl:call-template name="placeDetails">
						<xsl:with-param name="list" select="."/>
						<xsl:with-param name="posChild" select="$posChild"/>
						<xsl:with-param name="posParent" select="$posParent"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
						<xsl:with-param name="langNode" select="$langNode"/>
					</xsl:call-template>	
				</xsl:if>
			</xsl:for-each>
		</xsl:if>

		<!-- In any other case checks the whole list. -->
		<!-- localType = other -->
		<!-- no localType -->
		<!-- empty localType -->
		<xsl:if test="$list/eac:placeEntry[not(@localType = 'birth') and not(@localType = 'death') and not(@localType = 'foundation') and not(@localType = 'private-residence') and not(@localType = 'business-residence') and not(@localType = 'suppression')]">
			<xsl:for-each select="$list">
				<xsl:variable name="posChild" select="position()"/>
				<xsl:if test="./eac:placeEntry[not(@localType = 'birth') and not(@localType = 'death') and not(@localType = 'foundation') and not(@localType = 'private-residence') and not(@localType = 'business-residence') and not(@localType = 'suppression')] 
								and not(./eac:placeEntry[@localType = 'birth']) and not(./eac:placeEntry[@localType = 'foundation'])
								and not(./eac:placeEntry[@localType = 'private-residence']) and not(./eac:placeEntry[@localType = 'business-residence'])
								and not(./eac:placeEntry[@localType = 'death']) and not(./eac:placeEntry[@localType = 'suppression'])">
				  	<xsl:call-template name="placeDetails">
						<xsl:with-param name="list" select="."/>
						<xsl:with-param name="posChild" select="$posChild"/>
						<xsl:with-param name="posParent" select="$posParent"/>
						<xsl:with-param name="mode" select="$mode"/>
						<xsl:with-param name="validHrefLink" select="$validHrefLink"/>
						<xsl:with-param name="langNode" select="$langNode"/>
					</xsl:call-template>	
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	 
	<!-- template for language only one element -->
	
	<xd:doc>
	  <xd:short>Template to multilanguage <code>&lt;placeEntry&gt;</code>.</xd:short>
	  <xd:detail>Template to display <code>&lt;placeEntry&gt;</code> in the right language.
	  </xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;placeEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguagePlaceEntry">
		<xsl:param name="list"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.selected]">
							<xsl:if test="position() = 1">
								<xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="@countryCode">
									<xsl:call-template name="countryName">
										<xsl:with-param name="countryCode" select="./@countryCode"/>
				   			        </xsl:call-template>
			   			        </xsl:if>	
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $lang.navigator]">
							<xsl:if test="position() = 1">
								<xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="@countryCode">
									<xsl:call-template name="countryName">
										<xsl:with-param name="countryCode" select="./@countryCode"/>
				   			        </xsl:call-template>
			   			        </xsl:if>	
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list[@xml:lang = $language.default]">
							<xsl:if test="position() = 1">
								<xsl:choose>
									<xsl:when test="@vocabularySource">
										<xsl:call-template name="vocabularySource">
											<xsl:with-param name="node" select="."/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
									 <xsl:apply-templates select="." mode="other"/> 
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="@countryCode">
									<xsl:call-template name="countryName">
										<xsl:with-param name="countryCode" select="./@countryCode"/>
				   			        </xsl:call-template>
			   			        </xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						  	<xsl:for-each select="$list[not(@xml:lang)]">
							 	  <xsl:if test="position()=1"> 
									 <xsl:choose>
										<xsl:when test="@vocabularySource">
											<xsl:call-template name="vocabularySource">
												<xsl:with-param name="node" select="."/>
										    </xsl:call-template>
										</xsl:when>
										<xsl:otherwise>
										 <xsl:apply-templates select="." mode="other"/> 
										</xsl:otherwise>
									</xsl:choose>
									<xsl:if test="@countryCode">
										<xsl:call-template name="countryName">
											<xsl:with-param name="countryCode" select="./@countryCode"/>
					   			        </xsl:call-template>
				   			        </xsl:if>
							  	 </xsl:if>
						   	</xsl:for-each> 
						</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:variable name="currentText" select="current()/text()"></xsl:variable>
									<xsl:if test="$currentLang = $language.first">
										<xsl:if test="position() = 1">
											<xsl:choose>
												<xsl:when test="@vocabularySource">
													<xsl:call-template name="vocabularySource">
														<xsl:with-param name="node" select="."/>
										   		    </xsl:call-template>
												</xsl:when>
												<xsl:otherwise>
												 <xsl:apply-templates select="$currentText" mode="other"/> 
												</xsl:otherwise>
											</xsl:choose>
											<xsl:if test="@countryCode">
												<xsl:call-template name="countryName">
													<xsl:with-param name="countryCode" select="./@countryCode"/>
							   			        </xsl:call-template>
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
						<xsl:when test="$list[@vocabularySource]">
							<xsl:call-template name="vocabularySource">
								<xsl:with-param name="node" select="."/>
				   		    </xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
						 <xsl:apply-templates select="." mode="other"/> 
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="@countryCode">
						<xsl:call-template name="countryName">
							<xsl:with-param name="countryCode" select="./@countryCode"/>
	   			        </xsl:call-template>
   			        </xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template that will show all the elements in the passed list
		 independent of its language. -->
	
	<xd:doc>
	  <xd:short>Template to  display all the elements <code>&lt;placeEntry&gt;</code>.</xd:short>
	  <xd:detail>Template to display all the elements <code>&lt;placeEntry&gt;</code> according with the language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;placeEntry&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="showAllPlaceEntry">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
			<xsl:if test="position() = 1">
				<xsl:choose>
					<xsl:when test="@vocabularySource">
						<xsl:call-template name="vocabularySource">
							<xsl:with-param name="node" select="."/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="." mode="other"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="@countryCode">
					<xsl:call-template name="countryName">
						<xsl:with-param name="countryCode" select="./@countryCode"/>
   			        </xsl:call-template>
		        </xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	
	<!-- Template that will show the information in placeEntry (only first)-->
	
	<xd:doc>
	  <xd:short>Template to display the information of the first <code>&lt;placeEntry&gt;</code> in the right language.
	  </xd:short>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;placeEntry&gt;</code> elements.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="showDetailsPlaceEntry">
		<xsl:param name="list"/>
		<xsl:param name="langNode"/>
		
			<xsl:choose>
				<xsl:when test="$list[@xml:lang=$langNode]/text()">
					<xsl:for-each select="$list[@xml:lang=$langNode]">
						<xsl:if test="position() = 1">
							<xsl:choose>
								<xsl:when test="@vocabularySource">
									<xsl:call-template name="vocabularySource">
										<xsl:with-param name="node" select="."/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="@countryCode">
								<xsl:call-template name="countryName">
									<xsl:with-param name="countryCode" select="./@countryCode"/>
			   			        </xsl:call-template>
		   			        </xsl:if>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>	
				<xsl:when test="$langNode='notLang' and $list[not(@xml:lang)]/text()">
					<xsl:for-each select="$list[not(@xml:lang)]">
						<xsl:if test="position() =1 and $langNode = 'notLang'">
							<xsl:choose>
								<xsl:when test="@vocabularySource">
									<xsl:call-template name="vocabularySource">
										<xsl:with-param name="node" select="."/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="@countryCode">
								<xsl:call-template name="countryName">
									<xsl:with-param name="countryCode" select="./@countryCode"/>
			   			        </xsl:call-template>
		   			        </xsl:if>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
				<xsl:when test="$searchTerms != '' and ($list[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
														or ($list[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] and $langNode!='notLang'))">
					
					<xsl:for-each select="$list[@xml:lang != $langNode or not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]">
						<xsl:if test="position()=1 and $langNode!='notLang'"> 
							<xsl:choose>
							<xsl:when test="@vocabularySource">
								<xsl:call-template name="vocabularySource">
									<xsl:with-param name="node" select="."/>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="other"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="@countryCode">
							<xsl:call-template name="countryName">
								<xsl:with-param name="countryCode" select="./@countryCode"/>
		   			        </xsl:call-template>
	   			        </xsl:if>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
	</xsl:template>
	
	<!--Template to show the information about placeEntry  -->
	
	<xd:doc>
	  	<xd:short>Template to several details <code>&lt;placeEntry&gt;</code>.</xd:short>
	  	<xd:detail>Template to display the information of the <code>&lt;placeEntry&gt;</code> in the right language.
	  	</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;placeEntry&gt;</code> elements.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="showDetailsPlaceEntrySeveral">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>
		<xsl:param name="langNode"/>
		
		<div id="{$clazz}{$posParent}{$posChild}" class= "moreDisplay">
			<xsl:for-each select="$list">
				<xsl:choose>
					<xsl:when test=".[@xml:lang=$langNode]/text() or ($langNode='notLang' and .[not(@xml:lang)]/text())">
						<p>
							<xsl:choose>
								<xsl:when test="@vocabularySource">
									<xsl:call-template name="vocabularySource">
										<xsl:with-param name="node" select="."/>
			   		   			    </xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
		
							<xsl:if test="@countryCode">
								<xsl:call-template name="countryName">
									<xsl:with-param name="countryCode" select="./@countryCode"/>
			   			        </xsl:call-template>
		   			        </xsl:if>
						</p>
					</xsl:when>
					<xsl:when test="$searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))">
						<p>
							<xsl:choose>
								<xsl:when test="@vocabularySource">
									<xsl:call-template name="vocabularySource">
										<xsl:with-param name="node" select="."/>
			   		   			    </xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates select="." mode="other"/>
								</xsl:otherwise>
							</xsl:choose>
		
							<xsl:if test="@countryCode">
								<xsl:call-template name="countryName">
									<xsl:with-param name="countryCode" select="./@countryCode"/>
			   			        </xsl:call-template>
		   			        </xsl:if>
						</p>
					</xsl:when>
					<xsl:otherwise/>
				</xsl:choose>	
			</xsl:for-each>

			<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>
		</div>
	</xsl:template>
	
	<!-- Template that will show all the elements in the passed list
		 independent of its language. -->
	
	<xd:doc>
	  	<xd:short>Template to show all <code>&lt;placeEntry&gt;</code> elements. </xd:short>
	  	<xd:detail>Template to display the information of all <code>&lt;placeEntry&gt;</code> elements independent of its language.
	 	</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;placeEntry&gt;</code> elements.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
		<xd:param name="posChild">The parameter "posChild" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="showAllPlaceEntrySeveral">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="posChild"/>

		<div id="{$clazz}{$posParent}{$posChild}" class= "moreDisplay">
			<xsl:for-each select="$list">
				<p>
					<xsl:choose>
						<xsl:when test="@vocabularySource">
							<xsl:call-template name="vocabularySource">
								<xsl:with-param name="node" select="."/>
	   		   			    </xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="." mode="other"/>
						</xsl:otherwise>
					</xsl:choose>

					<xsl:if test="@countryCode">
						<xsl:call-template name="countryName">
							<xsl:with-param name="countryCode" select="./@countryCode"/>
	   			        </xsl:call-template>
   			        </xsl:if>
				</p>
			</xsl:for-each>

			<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}{$posChild}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display the country name from country code. -->
	
	<xd:doc>
	  	<xd:short>Template to <code>@countryCode</code>.</xd:short>
	  	<xd:detail>Template to display the country name from <code>@countryCode</code>.
	  	</xd:detail>
		<xd:param name="countryCode">The parameter "countryCode" is the content of the attribute <code>@countryCode</code>.</xd:param>
	</xd:doc>
	<xsl:template name="countryName">
		<xsl:param name="countryCode"/>

		<xsl:choose>
			<xsl:when test="$countryCode = 'EU' or $countryCode = 'eu'">
				<xsl:text> (</xsl:text>
					<xsl:value-of select="ape:resource('country.europe')"/>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="countryName" select="ape:countryName($language.selected, $countryCode)" />
				<xsl:if test="$countryName != 'ERROR' and $countryName != ''">
					<xsl:text> (</xsl:text>
						<xsl:value-of select="$countryName"/>
					<xsl:text>)</xsl:text>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- template for multilanguage -->
	
	<xd:doc>
		<xd:short>Template multilanguage information.</xd:short>	
		<xd:detail>Template to display the elements according to the language's rules.
		</xd:detail>
		<xd:param name="list">The parameter "list" is an array of <code>&lt;p&gt;</code> elements.</xd:param>
		<xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguage">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:choose>
			<xsl:when test="count($list) > 1">
			<div id="{$clazz}" class= "moreDisplay">
				<xsl:choose>
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list">
						    <xsl:variable name="currentLang" select="current()/@xml:lang"/>
							 <xsl:if test="(($currentLang = $language.selected) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<p>
								    <xsl:apply-templates mode="other"/> 
			   						<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>
								</p>
							</xsl:if> 	
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(($currentLang = $lang.navigator) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<p>
								    <xsl:apply-templates mode="other"/> 
			   						<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(($currentLang = $language.default) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<p>
									<xsl:apply-templates mode="other"/> 
			   						<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>
								</p>
							</xsl:if>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
						     <xsl:for-each select="$list">
						     	<xsl:if test="not(@xml:lang) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms)))">
									<p>
									  	<xsl:apply-templates select="." mode="other"/>
			   							<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>
							   		</p>
						   	   </xsl:if>	
						   	</xsl:for-each> 
					</xsl:when>
					<xsl:otherwise> <!-- first language -->
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(($currentLang = $language.first) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<p>
									 <xsl:apply-templates select="." mode="other"/> 	
			   							<xsl:if test="name(current()) = 'language'">
										<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
											<xsl:text> (</xsl:text>
											<xsl:call-template  name="multilanguageNoP">
									   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
									   			<xsl:with-param name="clazz" select="'language'"/>
											</xsl:call-template>
											<xsl:text>)</xsl:text>
										</xsl:if>
									</xsl:if>	 
								</p>			
							</xsl:if>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
				<div class="linkMore">
					<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
					</a>
					<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}', 'p');">
						<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
					</a>
				</div>
			</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<p>
						<xsl:apply-templates mode="other"/>
						<xsl:if test="name(current()) = 'language'">
							<xsl:if test="./parent::node()/eac:descriptiveNote/eac:p/text() ">
								<xsl:text> (</xsl:text>
								<xsl:call-template  name="multilanguageNoP">
						   			<xsl:with-param name="list" select="./parent::node()/eac:descriptiveNote/eac:p"/>
						   			<xsl:with-param name="clazz" select="'language'"/>
								</xsl:call-template>
								<xsl:text>)</xsl:text>
							</xsl:if>
						</xsl:if>
					</p>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template that will show all the elements in the passed list
		 independent of its language. -->
	
	<xd:doc>
	  <xd:short>Template to show all elements.</xd:short>
	  <xd:detail>Template to display all elements in the passed list independent of it's language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of elements.</xd:param>
	  <xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	  <xd:param name="posParent">The parameter "posParent" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	  <xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	</xd:doc>
	<xsl:template name="showAllElements">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="posParent"/>
		<xsl:param name="mode"/>

		<div id="{$clazz}{$posParent}" class= "moreDisplay">
			<xsl:for-each select="$list">
				<p>
					<xsl:apply-templates select="." mode="other"/>
					<xsl:if test="name(current()) = 'language'">
						<xsl:call-template  name="descriptiveNoteNoP">
							<xsl:with-param name="element" select="current()"/>
							<xsl:with-param name="mode" select="$mode"/>
							<xsl:with-param name="clazz" select="'language'"/>
							<xsl:with-param name="langNode" select="''"/>
						</xsl:call-template>
					</xsl:if>
				</p>
			</xsl:for-each>

			<div class="linkMore">
				<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$posParent}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
				</a>
				<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$posParent}', 'p');">
					<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
				</a>
			</div>
		</div>
	</xsl:template>

	<!-- Template to display correctly the details of the "<descriptiveNote>"
		 element inside "<languageUsed>" element. -->
	
	<xd:doc>
	  <xd:short>Template to <code>/languagesUsed/descriptiveNote</code></xd:short>
	  <xd:detail>Template to display correctly the details of the <code>&lt;descriptiveNote&gt;</code> element inside <code>&lt;languagesUsed&gt;</code> element.
	  </xd:detail>
	   <xd:param name="element">The parameter "element" is the element <code>&lt;p&gt;</code> in <code>/languagesUsed/descriptiveNote</code>.</xd:param>
	  <xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	  <xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	  <xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="descriptiveNoteNoP">
		<xsl:param name="element"/>
		<xsl:param name="mode"/>
		<xsl:param name="clazz"/>
		<xsl:param name="langNode"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $element/parent::node()/eac:descriptiveNote/eac:p[@xml:lang = $translationLanguage]/text()">
			<xsl:text> (</xsl:text>
			<xsl:call-template  name="showAllNoP">
				<xsl:with-param name="list" select="$element/parent::node()/eac:descriptiveNote/eac:p[@xml:lang = $translationLanguage]"/>
			</xsl:call-template>
			<xsl:text>)</xsl:text>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $element/parent::node()/eac:descriptiveNote/eac:p/text()">
			<xsl:text> (</xsl:text>
			<xsl:call-template  name="showAllNoP">
				<xsl:with-param name="list" select="$element/parent::node()/eac:descriptiveNote/eac:p"/>
			</xsl:call-template>
			<xsl:text>)</xsl:text>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
	  	<xsl:if test="$mode = 'default' and $langNode='' and $element/parent::node()/eac:descriptiveNote/eac:p/text()">
			<xsl:text> (</xsl:text>
			<xsl:call-template  name="multilanguageNoP">
				<xsl:with-param name="list" select="$element/parent::node()/eac:descriptiveNote/eac:p"/>
				<xsl:with-param name="clazz" select="'language'"/>
			</xsl:call-template>
			<xsl:text>)</xsl:text>
		</xsl:if>
		<xsl:if test="$mode = 'default' and (
			(	$langNode='notLang' and $element/parent::node()/eac:descriptiveNote/eac:p[not(@xml:lang)]/text()) 
				 or $element/parent::node()/eac:descriptiveNote/eac:p[@xml:lang=$langNode]/text() 
                 or ($searchTerms!='' and 
                 	(
	                 	$element/parent::node()/eac:descriptiveNote/eac:p[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
		      		 	or (
		      		 		$langNode!='notLang' and $element/parent::node()/eac:descriptiveNote/eac:p[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
	      		 		)
	      		 	)
	      		 )
			)">
			
			<xsl:text> (</xsl:text>
			<xsl:call-template  name="showDetailsNoP">
				<xsl:with-param name="list" select="$element/parent::node()/eac:descriptiveNote/eac:p"/>
				<xsl:with-param name="langNode" select="$langNode"/>
			</xsl:call-template>
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:template>

	<!-- template for show details in descriptiveNote -->
	
	<xd:doc>
	  <xd:short>Template to <code>/languagesUsed/descriptiveNote/p</code>.</xd:short>
	  <xd:detail>Template for show details in <code>&lt;descriptiveNote&gt;</code> according with the language's rule.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of elements <code>&lt;p&gt;</code>.</xd:param>
	  <xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	</xd:doc>
	<xsl:template name="showDetailsNoP">
		<xsl:param name="list"/>
		<xsl:param name="langNode"/>
		
		<xsl:for-each select="$list">
			<xsl:choose>
				<xsl:when test=".[@xml:lang=$langNode]/text() or ($langNode='notLang' and .[not(@xml:lang)]/text())">
					<xsl:if test="position() > 1">
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:apply-templates mode="other"/>
				</xsl:when>
				<xsl:when test="$searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))">
					<xsl:if test="position() > 1">
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:apply-templates mode="other"/>
				</xsl:when>
				<xsl:otherwise/>
				</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<!-- template for multilanguage with no <p> -->
	
	<xd:doc>
	  <xd:short>Template multilanguage <code>/languagesUsed/descriptiveNote/p</code>.</xd:short>
	  <xd:detail>Template for multilanguage <code>&lt;p&gt;</code> in <code>&lt;languagesUsed&gt;</code>.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;p&gt;</code> elements.</xd:param>
	  <xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageNoP">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>	
		<xsl:choose>
			<xsl:when test="count($list) > 1">
				<xsl:choose>
					<!-- lang equals selected language -->
					<xsl:when test="$list[@xml:lang = $language.selected] and $list[@xml:lang = $language.selected]/text() and $list[@xml:lang = $language.selected]/text() != ''">
						<xsl:for-each select="$list">
						   <xsl:variable name="currentLang" select="current()/@xml:lang"/>
							 <xsl:if test="(($currentLang = $language.selected) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<xsl:if test="position() > 1">
									<xsl:text> </xsl:text>
							    </xsl:if>
						   		 <xsl:apply-templates mode="other"/> 
							 </xsl:if>
						</xsl:for-each>
					</xsl:when>
					<!-- navigator's language -->
					<xsl:when test="$list[@xml:lang = $lang.navigator] and $list[@xml:lang = $lang.navigator]/text() and $list[@xml:lang = $lang.navigator]/text() != ''">
						<xsl:for-each select="$list">
						   <xsl:variable name="currentLang" select="current()/@xml:lang"/>
							 <xsl:if test="(($currentLang = $lang.navigator) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<xsl:if test="position() > 1">
									<xsl:text> </xsl:text>
							    </xsl:if>
						   		 <xsl:apply-templates mode="other"/> 
							 </xsl:if>
						</xsl:for-each>
					</xsl:when>
					<!-- default language -->
					<xsl:when test="$list[@xml:lang = $language.default] and $list[@xml:lang = $language.default]/text() and $list[@xml:lang = $language.default]/text() != ''">
						<xsl:for-each select="$list">
						   <xsl:variable name="currentLang" select="current()/@xml:lang"/>
							 <xsl:if test="(($currentLang = $language.default) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<xsl:if test="position() > 1">
									<xsl:text> </xsl:text>
							    </xsl:if>
						   		 <xsl:apply-templates mode="other"/> 
							 </xsl:if>
						</xsl:for-each>
					</xsl:when>
				  	
				  	<!-- no lang -->
					<xsl:when test="$list[not(@xml:lang)] and $list[not(@xml:lang)]/text() and $list[not(@xml:lang)]/text() != ''">
					   <xsl:for-each select="$list">
					     <xsl:if test="not(@xml:lang) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms)))">
							<xsl:if test="position() > 1">
								<xsl:text> </xsl:text>
						    </xsl:if>
					   		 <xsl:apply-templates mode="other"/> 
						 </xsl:if>
						</xsl:for-each>
					</xsl:when>
					
					<!-- first language -->
					<xsl:otherwise> 
						<xsl:variable name="language.first" select="$list[1]/@xml:lang"></xsl:variable>
						<xsl:for-each select="$list">
							<xsl:variable name="currentLang" select="current()/@xml:lang"></xsl:variable>
							<xsl:if test="(($currentLang = $language.first) or ($searchTerms != '' and fn:contains(fn:upper-case(./text()),fn:upper-case($searchTerms))))">
								<xsl:if test="position() > 1">
								   <xsl:text> </xsl:text>
						        </xsl:if>
					   		 	<xsl:apply-templates mode="other"/> 
						    </xsl:if>
						 </xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- 1 element -->
			<xsl:otherwise>
				<xsl:for-each select="$list">
					<xsl:apply-templates mode="other"/>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Template that will show all the elements in the passed list
		 independent of its language. -->
	
	<xd:doc>
	  <xd:short>Template to to show all <code>/languagesUsed/descriptiveNote</code> elements.</xd:short>
	  <xd:detail>Template that will show all the elements <code>&lt;p&gt;</code> in the passed list
		 independent of its language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;p&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="showAllNoP">
		<xsl:param name="list"/>

		<xsl:for-each select="$list">
			<xsl:if test="position() > 1">
				<xsl:text> </xsl:text>
			</xsl:if>
			<xsl:apply-templates mode="other"/>
		</xsl:for-each>
	</xsl:template>
	
	<!--template for outline in structureOrGenealogy-->
	
	<xd:doc>
	  <xd:short>Template for <code>/structureOrGenealogy/outline</code>.</xd:short>
	  <xd:detail>Template for the element <code>&lt;outline&gt;</code> into the element <code>&lt;structureOrGenealogy&gt;</code>. 
	  </xd:detail>
	 <xd:param name="list">The parameter "list" is an array of <code>&lt;structureOrGenealogy&gt;</code> elements.</xd:param>
	  <xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	  <xd:param name="entityType">The parameter "entityType" is the entity type.</xd:param>
	  <xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	  <xd:param name="pos">The parameter "pos" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="outlineStructure">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="entityType"/>
		<xsl:param name="mode"/>
		<xsl:param name="pos"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list/eac:outline/eac:level/eac:item[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
					<xsl:if test="$entityType='person'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.genealogy')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
					<xsl:if test="$entityType='corporateBody'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.structure')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
					<xsl:if test="$entityType='family'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.structureOrGenealogy')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
				</div>
				<div class="rightcolumn moreDisplay" id="{$clazz}{$pos}">
					<xsl:call-template name="showAllOutline">
						<xsl:with-param name="list" select="$list/eac:outline/eac:level"/>
						<xsl:with-param name="mode" select="$mode"/>
					</xsl:call-template>

					<div class="linkMore">
						<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
						</a>
						<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
						</a>
					</div>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/eac:outline/eac:level/eac:item/text()">
			<div class="row">
				<div class="leftcolumn">
					<xsl:if test="$entityType='person'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.genealogy')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
					<xsl:if test="$entityType='corporateBody'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.structure')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
					<xsl:if test="$entityType='family'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.structureOrGenealogy')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
				</div>
				<div class="rightcolumn moreDisplay" id="{$clazz}{$pos}">
					<xsl:call-template name="showAllOutline">
						<xsl:with-param name="list" select="$list/eac:outline/eac:level"/>
						<xsl:with-param name="mode" select="$mode"/>
					</xsl:call-template>

					<div class="linkMore">
						<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
						</a>
						<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
						</a>
					</div>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
		<xsl:if test="$mode = 'default' and $list/eac:outline/eac:level/eac:item/text()">
			<div class="row">
				<div class="leftcolumn">
					<xsl:if test="$entityType='person'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.genealogy')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
					<xsl:if test="$entityType='corporateBody'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.structure')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
					<xsl:if test="$entityType='family'">
						<h2>
							<xsl:value-of select="ape:resource('eaccpf.portal.structureOrGenealogy')"/><xsl:text>:</xsl:text>
						</h2>
					</xsl:if>
				</div>
				<div class="rightcolumn moreDisplay" id="{$clazz}{$pos}">
					<xsl:call-template name="multilanguageOutline">
						<xsl:with-param name="list" select="$list/eac:outline/eac:level"/>
					</xsl:call-template>

					<div class="linkMore">
						<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
						</a>
						<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
						</a>
					</div>
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!--template for outline in generalContext-->
	
	<xd:doc>
	  <xd:short>Template <code>/generalContext/outline</code>.</xd:short>
	  <xd:detail>Template for the element <code>&lt;outline&gt;</code> into the element <code>&lt;generalContext&gt;</code> 
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;generalContext&gt;</code>.</xd:param>
	  <xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	  <xd:param name="title">The parameter "title" is the title in the label of the corresponding section.</xd:param>
	  <xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	  <xd:param name="pos">The parameter "pos" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="outline">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="title"/>
		<xsl:param name="mode"/>
		<xsl:param name="pos"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list/eac:outline/eac:level/eac:item[@xml:lang = $translationLanguage]/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource($title)"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn moreDisplay" id="{$clazz}{$pos}">
					<xsl:call-template name="showAllOutline">
						<xsl:with-param name="list" select="$list/eac:outline/eac:level"/>
						<xsl:with-param name="mode" select="$mode"/>
					</xsl:call-template>

					<div class="linkMore">
						<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
						</a>
						<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
						</a>
					</div>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/eac:outline/eac:level/eac:item/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource($title)"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn moreDisplay" id="{$clazz}{$pos}">
					<xsl:call-template name="showAllOutline">
						<xsl:with-param name="list" select="$list/eac:outline/eac:level"/>
						<xsl:with-param name="mode" select="$mode"/>
					</xsl:call-template>

					<div class="linkMore">
						<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
						</a>
						<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
						</a>
					</div>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
		<xsl:if test="$mode = 'default' and $list/eac:outline/eac:level/eac:item/text()">
			<div class="row">
				<div class="leftcolumn">
					<h2>
						<xsl:value-of select="ape:resource($title)"/>
						<xsl:text>:</xsl:text>
					</h2>
				</div>
				<div class="rightcolumn moreDisplay" id="{$clazz}{$pos}">
					<xsl:call-template name="multilanguageOutline">
						<xsl:with-param name="list" select="$list/eac:outline/eac:level"/>
					</xsl:call-template>

					<div class="linkMore">
						<a class="displayLinkShowMore linkShow" href="javascript:showMore('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showmore')"/>
						</a>
						<a class="displayLinkShowLess linkShow" href="javascript:showLess('{$clazz}{$pos}', 'li');">
							<xsl:value-of select="ape:resource('eaccpf.portal.showless')"/>
						</a>
					</div>
				</div>
			</div>
		</xsl:if>
	</xsl:template>
	
	<!-- template for <p> in structureOrGenealogy or generalContext-->
	
	<xd:doc>
	  <xd:short>Template for <code>structureOrGenealogy/p</code> or <code>generalContext/p</code></xd:short>
	  <xd:detail>Template for the element <code>&lt;p&gt;</code> in <code>&lt;generalContext&gt;</code> or <code>&lt;structureOrGenealogy&gt;</code>.
	  </xd:detail>
	   <xd:param name="list">The parameter "list" is an array of <code>&lt;p&gt;</code> elements.</xd:param>
	  <xd:param name="clazz">The parameter "clazz" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	  <xd:param name="mode">The parameter "mode" is the mode in the translation combo.</xd:param>
	  <xd:param name="langNode">The parameter "langNode" is the node's language.</xd:param>
	  <xd:param name="pos">The parameter "pos" is the attribute <code>id</code> in the <code>HTML div tag </code>.</xd:param>
	</xd:doc>
	<xsl:template name="p">
		<xsl:param name="list"/>
		<xsl:param name="clazz"/>
		<xsl:param name="mode"/>
		<xsl:param name="langNode"/>
		<xsl:param name="pos"/>
		
		<!-- Checks if exists any content in the translation language. -->
		<!-- Try to select the content in the specific language. -->
		<xsl:if test="$mode = 'other' and $list/eac:p[@xml:lang = $translationLanguage]/text()">
	    	<div class="row">
				<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource('eaccpf.portal.note')"/>
			   			<xsl:text>:</xsl:text>
		   			</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list/eac:p[@xml:lang = $translationLanguage]"/>
			   			<xsl:with-param name="clazz" select="$clazz"/>
			   			<xsl:with-param name="posParent" select="$pos"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to display all the entries. -->
		<xsl:if test="$mode = 'showAll' and $list/eac:p/text()">
	    	<div class="row">
				<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource('eaccpf.portal.note')"/>
			   			<xsl:text>:</xsl:text>
		   			</h2>
			   	</div>
			   	<div class="rightcolumn">
					<xsl:call-template name="showAllElements">
			   			<xsl:with-param name="list" select="$list/eac:p"/>
			   			<xsl:with-param name="clazz" select="$clazz"/>
			   			<xsl:with-param name="posParent" select="$pos"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>

		<!-- Try to select the 'default' mode. -->
	
		<xsl:if test="$mode = 'default' and (
					(	$langNode='notLang' and $list/eac:p[not(@xml:lang)]/text()) 
						 or $list/eac:p[@xml:lang=$langNode]/text() 
		                 or ($searchTerms!='' and 
		                 	(
			                 	$list/eac:p[@xml:lang != $langNode][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))] 
				      		 	or (
				      		 		$langNode!='notLang' and $list/eac:p[not(@xml:lang)][fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))]
			      		 		)
			      		 	)
			      		 )
					)">
	
			<div class="row">
				<div class="leftcolumn">
			   		<h2>
			   			<xsl:value-of select="ape:resource('eaccpf.portal.note')"/>
			   			<xsl:text>:</xsl:text>
		   			</h2>
			   	</div>
			   	<div class="rightcolumn">
					
					<xsl:call-template name="showDetailsDescriptiveNote">
			   			<xsl:with-param name="list" select="$list/eac:p"/>
			   			<xsl:with-param name="clazz" select="$clazz"/>
			   			<xsl:with-param name="posParent" select="$pos"/>
			   			<xsl:with-param name="mode" select="$mode"/>
			   			<xsl:with-param name="langNode" select="$langNode"/>
			   		</xsl:call-template>
				</div>
			</div>
		</xsl:if>
	</xsl:template>

	<!-- template for multilanguage outline -->
	
	<xd:doc>
	  <xd:short>Template for multilanguage <code>&lt;outline&gt;</code>.</xd:short>
	  <xd:detail>Template for the element <code>&lt;outline&gt;</code> following the language's rules.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;level&gt;</code> elements.</xd:param>
	</xd:doc>
	<xsl:template name="multilanguageOutline">
		<xsl:param name="list"/><!-- outline/level -->
		<xsl:choose>
		    <!-- selected language -->
			<xsl:when test="$list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected] and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected]/text() and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.selected]/text() != ''">
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="language" select="$language.selected"/>
				</xsl:call-template>
			</xsl:when> 
			<!-- language's navigator -->
			<xsl:when test="$list/descendant-or-self::node()/eac:item[@xml:lang = $lang.navigator] and $list/descendant-or-self::node()/eac:item[@xml:lang = $lang.navigator]/text() and $list/descendant-or-self::node()/eac:item[@xml:lang = $lang.navigator]/text() != ''">
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="language" select="$lang.navigator"/>
				</xsl:call-template>
			</xsl:when> 
			<!-- default language (english) -->
			<xsl:when test="$list/descendant-or-self::node()/eac:item[@xml:lang = $language.default] and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.default]/text() and $list/descendant-or-self::node()/eac:item[@xml:lang = $language.default]/text() != ''">
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="language" select="$language.default"/>
				</xsl:call-template>
			</xsl:when>
			<!-- not lang -->
			<xsl:when test="$list/descendant-or-self::node()/eac:item[not(@xml:lang)] and $list/descendant-or-self::node()/eac:item[not(@xml:lang)]/text() and $list/descendant-or-self::node()/eac:item[not(@xml:lang)]/text() != ''">
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="language" select="'notLang'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise> <!-- First language -->
				<xsl:variable name="language.first" select="$list[1]/eac:item/@xml:lang"/>
				<xsl:call-template name="multilanguageOutlineRecursive">
					<xsl:with-param name="list" select="$list"/>
					<xsl:with-param name="language" select="$language.first"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>	
	</xsl:template>

	<!-- template for outline recursive -->
	
	<xd:doc>
	  <xd:short>Template for <code>&lt;item&gt;</code>.</xd:short>
	  <xd:detail>Template to display the element <code>&lt;item&gt;</code> into the element <code>/outline/level</code>.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;level&gt;</code> elements.</xd:param>
	  <xd:param name="language">The parameter "language" is the language selected to display the information.</xd:param>	
	</xd:doc>
	<xsl:template name="multilanguageOutlineRecursive">
		<xsl:param name="list"/>
		<xsl:param name="language"/>
	    <ul class="level">
			 <xsl:for-each select="$list">
				 	<xsl:if test="./descendant-or-self::node()/eac:item[@xml:lang = $language] or ($language='notLang' and ./descendant-or-self::node()/eac:item[not(@xml:lang)]) 
				 				  or ($searchTerms != '' and ./descendant-or-self::node()/eac:item[fn:contains(fn:upper-case(text()),fn:upper-case($searchTerms))])"> 
						<xsl:choose>
							<xsl:when test="name(./parent::node()) != 'outline'">
							   <ul class="level">
							      <li class="item">
							        <xsl:apply-templates select="./eac:item" mode="other"/> 
							      </li>
							   </ul>
							</xsl:when>
							<xsl:otherwise>
								<li class="item">
								 	<xsl:apply-templates select="./eac:item" mode="other"/> 
							 	</li>
						 	</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:for-each select="./eac:level">
						<xsl:call-template name="multilanguageOutlineRecursive">
							<xsl:with-param name="list" select="."/>
							<xsl:with-param name="language" select="$language"/>
						</xsl:call-template>
					</xsl:for-each> 
				</xsl:for-each> 
 		</ul>
	</xsl:template>

	<!-- Template that will show all the elements in the passed list
		 independent of its language. -->
	
	<xd:doc>
	  <xd:short>Template to <code>/outline/level</code> elements.</xd:short>
	  <xd:detail>Template that call to the template "showAllOutlineRecursive" with the elements <code>&lt;level&gt;</code> according with the language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;level&gt;</code> elements.</xd:param>
	  <xd:param name="mode">The parameter "mode" is the mode selected in the combo translation to display the information.</xd:param>
	</xd:doc>
	<xsl:template name="showAllOutline">
		<xsl:param name="list"/><!-- outline/level -->
		<xsl:param name="mode"/>

		<xsl:if test="$mode = 'other'">
			<xsl:call-template name="showAllOutlineRecursive">
				<xsl:with-param name="list" select="$list"/>
				<xsl:with-param name="language" select="$translationLanguage"/>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="$mode = 'showAll'">
			<xsl:call-template name="showAllOutlineRecursive">
				<xsl:with-param name="list" select="$list"/>
				<xsl:with-param name="language" select="'allElements'"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- Template for outline recursive and display all elements. -->
	
	<xd:doc>
	  <xd:short>Template to display <code>/outline/level</code> elements.</xd:short>	
	  <xd:detail>Template to display recursively the element <code>&lt;item&gt;</code> into the element <code>/outline/level</code> according with the language.
	  </xd:detail>
	  <xd:param name="list">The parameter "list" is an array of <code>&lt;level&gt;</code> elements.</xd:param>
	  <xd:param name="language">The parameter "language" is the language selected to display the information.</xd:param>	
	</xd:doc>
	<xsl:template name="showAllOutlineRecursive">
		<xsl:param name="list"/>
		<xsl:param name="language"/>

	    <ul class="level">
			<xsl:for-each select="$list">
				<xsl:if test="./descendant-or-self::node()/eac:item[@xml:lang = $language] or $language='allElements'">
					<xsl:choose>
						<xsl:when test="name(./parent::node()) != 'outline'">
							<ul class="level">
								<li class="item">
									<xsl:apply-templates select="./eac:item" mode="other"/>
								</li>
							</ul>
						</xsl:when>
						<xsl:otherwise>
							<li class="item">
								<xsl:apply-templates select="./eac:item" mode="other"/>
							</li>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>

				<xsl:for-each select="./eac:level">
					<xsl:call-template name="showAllOutlineRecursive">
					<xsl:with-param name="list" select="."/>
					<xsl:with-param name="language" select="$language"/>
					</xsl:call-template>
				</xsl:for-each>
			</xsl:for-each>
		</ul>
	</xsl:template>
	
	<!-- template to know the @localType in <entityId>-->
	
	<xd:doc>
	  <xd:short>Template for type in <code>&lt;entityId&gt;</code>.</xd:short>
	  <xd:detail>Template to know the attribute <code>@localType</code> into the element <code>&lt;entityId&gt;</code>.
	  </xd:detail>
	  <xd:param name="entityId">The parameter "entityId" is the element that contains the attribute.</xd:param>	
	</xd:doc>
	<xsl:template name="typeEntityId">
		<xsl:param name="entityId"/>
		<xsl:choose>
   			<xsl:when test="$entityId[fn:normalize-space(@localType)!='']">
   				<xsl:choose>
   					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='VIAF']">
		   				<xsl:call-template name="displayEntityId">
		   					<xsl:with-param name="entityId" select="$entityId"/>
		   					<xsl:with-param name="url" select="'http://viaf.org/viaf/'"/>
		   				</xsl:call-template>
					</xsl:when>
					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='GND']">
		   				<xsl:call-template name="displayEntityId">
		   					<xsl:with-param name="entityId" select="$entityId"/>
		   					<xsl:with-param name="url" select="'http://d-nb.info/gnd/'"/>
		   				</xsl:call-template>
					</xsl:when>
					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='CGUIA']">
		   				<xsl:call-template name="displayEntityId">
		   					<xsl:with-param name="entityId" select="$entityId"/>
		   					<xsl:with-param name="url" select="'http://censoarchivos.mcu.es/CensoGuia/productordetail.htm?id='"/>
		   				</xsl:call-template>
					</xsl:when>
					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='LCNAF']">
		   				<xsl:call-template name="displayEntityId">
		   					<xsl:with-param name="entityId" select="$entityId"/>
		   					<xsl:with-param name="url" select="'http://id.loc.gov/authorities/names/'"/>
		   				</xsl:call-template>
					</xsl:when>
					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='ISNI']">
		   				<xsl:call-template name="displayEntityId">
		   					<xsl:with-param name="entityId" select="$entityId"/>
		   					<xsl:with-param name="url" select="'http://isni.org/isni/'"/>
		   				</xsl:call-template>
					</xsl:when>
   					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='BNF']">
   						<xsl:call-template name="displayEntityId">
   							<xsl:with-param name="entityId" select="$entityId"/>
   							<xsl:with-param name="url" select="'http://data.bnf.fr/'"/>
   						</xsl:call-template>
   					</xsl:when>
   					<!-- HANDLE added on 2016-08-08 -->
   					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='HANDLE']">
   						<xsl:call-template name="displayEntityId">
   							<xsl:with-param name="entityId" select="$entityId"/>
   							<xsl:with-param name="url" select="'http://proxy.handle.net/'"/>
   						</xsl:call-template>
   					</xsl:when>
   					<!-- PARES added on 2016-10-20 -->
   					<xsl:when test="$entityId[fn:upper-case(fn:normalize-space(@localType))='PARES']">
   						<xsl:call-template name="displayEntityId">
   							<xsl:with-param name="entityId" select="$entityId"/>
   							<xsl:with-param name="url" select="'http://pares.mcu.es/'"/>
   						</xsl:call-template>
   					</xsl:when>
   					<xsl:otherwise>
		   				<p>
							<xsl:apply-templates select="$entityId" mode="other"/>
							<xsl:text> (</xsl:text>
							<xsl:value-of select="$entityId/fn:normalize-space(@localType)"/>												
							<xsl:text>)</xsl:text>
						</p>
					</xsl:otherwise>
				</xsl:choose>
   			</xsl:when>
   			<xsl:otherwise>
				<p>
					<xsl:apply-templates select="$entityId" mode="other"/> 
				</p>
			</xsl:otherwise>
   		</xsl:choose>
	</xsl:template>
	
	<!-- template to display the <entityId> -->
	
	<xd:doc>
	  <xd:short>Template for <code>&lt;entityId&gt;</code>.</xd:short>
	  <xd:detail>Template to display the element <code>&lt;entityId&gt;</code>.
	  </xd:detail>
	  <xd:param name="entityId">The parameter "entityId" is the content of the element <code>&lt;entityId&gt;</code>.</xd:param>	
	  <xd:param name="url">The parameter "url" is the url that goes the element <code>&lt;entityId&gt;</code>.</xd:param>		
	</xd:doc>
	<xsl:template name="displayEntityId">
		<xsl:param name="entityId"/>
		<xsl:param name="url"/>
		
		<p>
			<xsl:variable name="href" select="$entityId/text()"/>
			<a href="{fn:concat($url,$href)}" target="_blank"><xsl:apply-templates select="$entityId" mode="other"/></a>
			<xsl:text> (</xsl:text>
			<xsl:value-of select="$entityId/fn:normalize-space(@localType)"/>												
			<xsl:text>)</xsl:text>
		</p>
	</xsl:template>
</xsl:stylesheet>
