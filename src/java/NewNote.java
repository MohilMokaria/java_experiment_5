
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import myClass.Note;

@WebServlet("/NewNote")
public class NewNote extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String jdbcUri = getServletContext().getInitParameter("jdbcUri");
        final String dbUri = getServletContext().getInitParameter("dbUri");
        final String dbId = getServletContext().getInitParameter("dbId");
        final String dbPass = getServletContext().getInitParameter("dbPass");

        HttpSession session = request.getSession();
        String who = (String) session.getAttribute("userEmail");
        List<Note> noteList = new ArrayList<>();

        try {
            Class.forName(jdbcUri);
            try (Connection connection = DriverManager.getConnection(dbUri, dbId, dbPass)) {
                String sql = "SELECT id, title, body, note_time FROM notes WHERE who = ? ORDER BY note_time DESC";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, who);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Note n = new Note();
                            n.setId(resultSet.getInt("id"));
                            n.setTitle(resultSet.getString("title"));
                            n.setBody(resultSet.getString("body"));
                            n.setTime(resultSet.getTimestamp("note_time"));
                            noteList.add(n);
                        }
                    }
                }
                request.setAttribute("noteList", noteList);
                RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
                rd.forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(NewNote.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("exception", ex.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String jdbcUri = getServletContext().getInitParameter("jdbcUri");
        final String dbUri = getServletContext().getInitParameter("dbUri");
        final String dbId = getServletContext().getInitParameter("dbId");
        final String dbPass = getServletContext().getInitParameter("dbPass");

        response.setContentType("text/html");

        HttpSession session = request.getSession();
        String who = (String) session.getAttribute("userEmail");
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        Timestamp noteTime = new Timestamp(System.currentTimeMillis());

        try {
            Class.forName(jdbcUri);

            try (Connection con = DriverManager.getConnection(dbUri, dbId, dbPass)) {
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO notes(who, title, body, note_time) values(?,?,?,?)")) {
                    ps.setString(1, who);
                    ps.setString(2, title);
                    ps.setString(3, body);
                    ps.setTimestamp(4, noteTime);

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
    }
}
