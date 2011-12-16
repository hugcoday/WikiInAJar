<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="edit-article">
		<h1>
			Edit:
			<xsl:value-of select="normalize-space(/page/article/id)" />
		</h1>
		<a href="/public/skins/default/common/formatting.htm"
			onclick="javascript:w = window.open('/public/skins/default/common/formatting.htm','formattinghelp', config='height=300,width=300, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, directories=no, status=no'); w.focus();return false;">
			Formatting help
		</a>
		<form method="POST" name="editform">
			<xsl:attribute name="action">
				/wiki/save/
				<xsl:value-of select="normalize-space(editfield/content-id)" />
			</xsl:attribute>
		<table>
	    <tr>
	    <td>	
			<textarea name="article-content" rows="20" cols="80">
				<xsl:value-of select="editfield/content" />
			</textarea>
			<input type="hidden" name="article-name">
				<xsl:attribute name="value">
					<xsl:value-of
						select="normalize-space(editfield/content-id)" />
				</xsl:attribute>
			</input>
			</td>
			</tr>
			<tr>
			<td align="right">
			<input type="submit" value="Save" accesskey="s"
				title="Save [Alt-s]" />
		</td>
		</tr>
	   </table>	
		</form>
			<script language="javascript">
				<!-- 
					document.editform.article-content.focus();
					// -->
			</script>
	</xsl:template>

</xsl:stylesheet>