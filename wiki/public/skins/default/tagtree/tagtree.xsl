<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="tagtree">
		<xsl:apply-templates select="tag" />
	</xsl:template>

	<xsl:template match="tag">
		<div id="tagtree">
			<div id="tagtree-tag">
				<a>
					<xsl:attribute name="href">
						<xsl:value-of select="@link" />
					</xsl:attribute>
					<xsl:value-of select="@name" />
				</a>
				<xsl:apply-templates select="tag" />
				<xsl:for-each select="taggable">
					<div id="tagtree-link">
						<a>
							<xsl:attribute name="href">
								<xsl:value-of select="@link" />
							</xsl:attribute>
							<xsl:value-of select="." />
						</a>
					</div>
				</xsl:for-each>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>