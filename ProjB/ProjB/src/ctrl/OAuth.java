package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class OAuth
 */
@WebServlet("/OAuth.do")
public class OAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;																																																																																																																							
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("user");
		String name = request.getParameter("name");
		String 	hash = request.getParameter("hash");	
		
		if (user != null) {
		
			Writer out = response.getWriter();
			response.setContentType("text/html");
	
			String html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a>"
						+ "</p><p>User: %s, Name: %s, Hash: %s</p></body></html>", user, name, hash);
			out.write(html);
		}
		else
			this.getServletContext().getRequestDispatcher("/OAuth.html").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String url = "http://192.168.2.93:4413" + request.getRequestURI();
		String url = request.getRequestURL().toString().trim();
		response.sendRedirect(String.format("https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi?back=%s", url));
	
    }
	

}
