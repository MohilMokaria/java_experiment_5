
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/EditNote")
public class EditNote extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String jdbcUri = getServletContext().getInitParameter("jdbcUri");
        final String dbUri = getServletContext().getInitParameter("dbUri");
        final String dbId = getServletContext().getInitParameter("dbId");
        final String dbPass = getServletContext().getInitParameter("dbPass");

        response.setContentType("text/html");

        String idParameter = request.getParameter("id");
        String title = request.getParameter("newTitle");
        String body = request.getParameter("newBody");
        Timestamp noteTime = new Timestamp(System.currentTimeMillis());

        if (idParameter != null && !idParameter.isEmpty()) {
            try {
                int id = Integer.parseInt(idParameter);
                Class.forName(jdbcUri);

                try (Connection con = DriverManager.getConnection(dbUri, dbId, dbPass)) {
                    try (PreparedStatement ps = con.prepareStatement("UPDATE notes SET title=?, body=?, note_time=? WHERE id=?")) {
                        ps.setString(1, title);
                        ps.setString(2, body);
                        ps.setTimestamp(3, noteTime);
                        ps.setInt(4, id);

                        int i = ps.executeUpdate();

                        response.sendRedirect("NewNote");
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(NewNote.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("exception", ex.getMessage());
                RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
                rd.forward(request, response);
            }
        } else {
            response.sendRedirect("NewNote");
        }
    }
}
