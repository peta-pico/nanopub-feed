package org.petapico.nanopubfeed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NanopubFeedServlet extends HttpServlet {

	private static final long serialVersionUID = -4542560440919522982L;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			setGeneralHeaders(resp);
			ServerRequest r = new ServerRequest(req);
			if (r.isEmpty()) {
				MainPage.show(r, resp);
			} else if (r.getFullRequest().equals("/style/plain.css")) {
				ResourcePage.show(r, resp, "style.css", "text/css");
			} else if (r.getFullRequest().equals("/style/favicon.ico")) {
				ResourcePage.show(r, resp, "favicon.ico", "image/x-icon");
			} else {
				resp.sendError(400, "Invalid GET request: " + r.getFullRequest());
			}
		} finally {
			resp.getOutputStream().close();
			req.getInputStream().close();
		}
	}


	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doOptions(req, resp);
		setGeneralHeaders(resp);
	}

	@Override
	public void init() throws ServletException {
		logger.info("Init");
	}

	private void setGeneralHeaders(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
	}

}
