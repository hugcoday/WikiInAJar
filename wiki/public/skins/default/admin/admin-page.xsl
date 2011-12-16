<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="admin-page">
		<ul>
			<li>
				<a href="/admin/shutdown" accesskey="s" title="Shutdown [Alt-s]">shutdown this wiki server</a>
			</li>
			<li>
				<xsl:choose>
					<xsl:when
						test="only-local-connections/@value = 'yes'">
						<a href="/admin/toggleconnections" accesskey="a" title="Allow [Alt-a]">
							allow remote connections to this wiki server
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="/admin/toggleconnections" accesskey="b" title="Block [Alt-b]">
							block remote connections to this wiki server
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</li>
		</ul>
		<p>&#160;</p>
		<p>
			If remote connections are allowed, everybody who knows the
			host name and port of the machine that is running the wiki
			server can connect to it. That is useful if you would like
			access to your notes from other machines, but keep in mind
			that other people have access as well.
		</p>
		<p>&#160;</p>
		<p>
			By default the wiki server is blocking all connections that
			come from remote machines.
		</p>
	</xsl:template>

</xsl:stylesheet>