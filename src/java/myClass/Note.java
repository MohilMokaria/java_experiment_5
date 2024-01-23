package myClass;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    // Instance variables for the post title, body, and timestamp
    private int id;
    private String title;
    private String body;
    private Timestamp note_time;

    // Default constructor
    public Note() {
    }

    // Parameterized constructor to initialize the Note object with title, body, and timestamp
    public Note(int id, String title, String body, Timestamp note_time) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.note_time = note_time;
    }

    // Method to get the formatted message time in "HH:mm | dd/MM/yyyy" format
    public String getFormattedMsgTime() {
        if (note_time != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | dd/MM/yyyy");
            return dateFormat.format(new Date(note_time.getTime()));
        } else {
            return null; // Return null if the timestamp is not set
        }
    }

    // Setter method for the post id
    public void setId(int id) {
        this.id = id;
    }

    // Setter method for the post title
    public void setTitle(String title) {
        this.title = title;
    }

    // Setter method for the post body
    public void setBody(String body) {
        this.body = body;
    }

    // Setter method for the post timestamp
    public void setTime(Timestamp note_time) {
        this.note_time = note_time;
    }

    // Getter method for the post id
    public int getId() {
        return this.id;
    }

    // Getter method for the post title
    public String getTitle() {
        return this.title;
    }

    // Getter method for the post body
    public String getBody() {
        return this.body;
    }

    // Getter method for the post timestamp
    public Timestamp getTime() {
        return this.note_time;
    }

}
