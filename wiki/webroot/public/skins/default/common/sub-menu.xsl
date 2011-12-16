<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="sub-menu">
		<div id="sub-menu">
			<xsl:apply-templates select="edit-menu" />
			<xsl:apply-templates select="sub-menu-entry" />
		</div>
	</xsl:template>

	<xsl:template match="edit-menu">
		<span>
			<a>
				<xsl:attribute name="href">
					/wiki/edit/
					<xsl:value-of select="/page/article/id" />
				</xsl:attribute>
				<xsl:value-of select="." />
			</a>
		</span>
	</xsl:template>

	<xsl:template match="sub-menu-entry">
		<xsl:choose>
			<xsl:when test="@selected = 'yes'">
				<span id="selected-sub-menu">
					<xsl:value-of select="@value" />
				</span>
			</xsl:when>
			<xsl:otherwise>
				<span>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="@link" />
						</xsl:attribute>
						<xsl:value-of select="@value" />
					</a>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>