
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DeleteNote")
public class DeleteNote extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String jdbcUri = getServletContext().getInitParameter("jdbcUri");
        final String dbUri = getServletContext().getInitParameter("dbUri");
        final String dbId = getServletContext().getInitParameter("dbId");
        final String dbPass = getServletContext().getInitParameter("dbPass");

        String idParameter = request.getParameter("id");

        if (idParameter != null) {
            try {
                int noteId = Integer.parseInt(idParameter);

                Class.forName(jdbcUri);
                try (Connection connection = DriverManager.getConnection(dbUri, dbId, dbPass)) {
                    String sql = "DELETE FROM notes WHERE id=?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, noteId);
                        int rowsDeleted = statement.executeUpdate();

                        response.sendRedirect("NewNote");

//                        if (rowsDeleted > 0) {
//                            // Note successfully deleted
//                            response.sendRedirect("SomeOtherPage.jsp");
//                        } else {
//                            // Note with the specified ID not found
//                            response.sendRedirect("NoteNotFoundPage.jsp");
//                        }
                    }
                }
            } catch (NumberFormatException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(DeleteNote.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("exception", ex.getMessage());
                RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
                rd.forward(request, response);
            }
        } else {
            // Handle the case where idParameter is null
            response.sendRedirect("InvalidIdPage.jsp");
        }
    }
}
