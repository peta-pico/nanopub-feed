package org.petapico.nanopubfeed;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class MainPage extends Page {

	public static void show(ServerRequest req, HttpServletResponse httpResp) throws IOException {
		MainPage obj = new MainPage(req, httpResp);
		obj.show();
	}

	public MainPage(ServerRequest req, HttpServletResponse httpResp) {
		super(req, httpResp);
	}

	public void show() throws IOException {
		String format;
		String ext = getReq().getExtension();
		if ("json".equals(ext)) {
			format = "application/json";
		} else if (ext == null || "html".equals(ext)) {
			String suppFormats = "application/json,text/html";
			format = Utils.getMimeType(getHttpReq(), suppFormats);
		} else {
			getResp().sendError(400, "Invalid request: " + getReq().getFullRequest());
			return;
		}
		if ("application/json".equals(format)) {
			// TODO
		} else {
			printHtmlHeader("Nanopub Feed");
			println("<h1>Nanopub Feed</h1>");
			println("<ul>");
			try {
				for (String u : Utils.getSigningUsers()) {
					println("<li>" + u + "</li>");
				}
			} catch (IOException ex) {
				println("<li>ERROR</li>");
			}
			println("</ul>");
			printHtmlFooter();
		}
		getResp().setContentType(format);
	}

}
