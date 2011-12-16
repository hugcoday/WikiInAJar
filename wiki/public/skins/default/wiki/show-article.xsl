<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="show-article">
		<h1>
			<xsl:value-of select="normalize-space(/page/article/id)" />
		</h1>
		<!-- just copy everything as it is expected to be HTML -->
		<xsl:copy-of select="content/*" />
	</xsl:template>

</xsl:stylesheet>