package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Brain;

/**
 * Servlet implementation class Http
 * Http.do
 */
@WebServlet("/Http.do")
public class Http extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     this.getServletContext().getRequestDispatcher("/Http.html").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Writer out = response.getWriter();
		response.setContentType("text/html");
		
		Brain model = Brain.getInstance();
		String country = request.getParameter("country");
		String query = request.getParameter("query");
		
		
		String html;
		try
		{
			 String res = model.doHttp(country, query);
			 html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a></p><b>Country Data:"
			 		+ "</b><br/><code>%s</code></body></html>", res);
			out.write(html);
			
		} catch (Exception e)
		{
			html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a><"
					+ "/p><p>Error For country and population: \"%s\"</p", e.getMessage().toString());
			out.write(html);
		}
	}

}
