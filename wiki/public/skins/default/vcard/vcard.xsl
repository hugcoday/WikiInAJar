<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="vcard">
		<h1>
			<xsl:value-of select="../id" />
		</h1>
		<table id="vcard" cellspacing="7">
			<xsl:for-each select="property">
				<tr>
					<td>
						<xsl:value-of select="@name" />&#160;
						<xsl:if test="count(type) > 0">
							<span id="vcard-proptype">(<xsl:for-each select="type">
								<xsl:value-of
									select="normalize-space(.)" />&#160;
							</xsl:for-each>)</span>
						</xsl:if>
					</td>
					<td>
						<xsl:value-of select="value" />
					</td>
				</tr>
			</xsl:for-each>
			
			<xsl:for-each select="formatted-property">
				<tr>
					<td>
						<xsl:value-of select="@name" />&#160;
						<xsl:if test="count(type) > 0">
							<span id="vcard-proptype">(<xsl:for-each select="type">
								<xsl:value-of
									select="normalize-space(.)" />&#160;
							</xsl:for-each>)</span>
						</xsl:if>
					</td>
					<td>
						<xsl:copy-of select="value" />
					</td>
				</tr>
			</xsl:for-each>
			
		</table>
	</xsl:template>

</xsl:stylesheet>