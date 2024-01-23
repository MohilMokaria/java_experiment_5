<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="myClass.Note" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<head>
    <title>Simple Chat App</title>
    <!-- Google Icon CDN -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    <!-- BOOTSTRAP CDNs -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <!-- LOCAL CSS LINK -->
    <link rel="stylesheet" href="./home_style.css" />
</head>
<body>
    <%
        if (session.getAttribute("userEmail") != null) {
    %>

    <div class="d-flex justify-content-between">
        <span class="navbar-text mr-3">
            <strong><%= session.getAttribute("userEmail")%></strong>
        </span>

        <form action="LogoutServlet" method="get">
            <button class="btn btn-outline-danger">Logout</button>
        </form>
    </div>

    <%
    String exceptionMsg = (String) request.getAttribute("exception");
    if (exceptionMsg != null && !exceptionMsg.isEmpty()) {
    %>
    <p class="alert alert-danger"><%= exceptionMsg%></p> 
    <%
        }
    %>
    <br>
    <hr class="border border-primary border-2">
    <section id="NewForm">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-10 col-sm-8 mx-auto">
                    <div class="card shadow align-items-center">
                        <div class="card-body d-flex flex-column col-md-9 col-sm-6 mb-4 mt-4">
                            <form action="NewNote" method="post">
                                <%
                                    String error = (String) request.getAttribute("error");
                                    if (error != null && !error.isEmpty()) {
                                %>          
                                <p class="alert alert-danger"><%= error%></p>  
                                <%
                                    }
                                %>
                                <div class="mb-4">
                                    <h2>New Note</h2>
                                </div>
                                <div class="form-outline mb-4">
                                    <label  for="title"  class="form-label">Title:</label>
                                    <input type="text" id="title" name="title" class="form-control" required/>
                                </div>

                                <div class="form-outline mb-4">
                                    <label class="form-label" for="body">Note:</label>
                                    <textarea id="body" name="body" class="form-control" required></textarea>
                                </div>

                                <center><button type="submit" class="btn btn-outline-primary btn-block mb-2 sendbtn">Add</button></center>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="UpdateForm">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-10 col-sm-8 mx-auto">
                    <div class="card shadow align-items-center">
                        <div class="card-body d-flex flex-column col-md-9 col-sm-6 mb-4 mt-4">
                            <form action="EditNote" method="post">
                                <div class="mb-4">
                                    <h2>Edit Note</h2>
                                </div>
                                <input id="id" style="display: none" type="text" name="id" class="form-control"/>
                                <div class="form-outline mb-4">
                                    <label  for="newTitle"  class="form-label">Updated Title:</label>
                                    <input id="newTitle" type="text" name="newTitle" class="form-control" required/>
                                </div>

                                <div class="form-outline mb-4">
                                    <label class="form-label" for="newBody">Updated Note:</label>
                                    <textarea id="newBody" name="newBody" class="form-control" required></textarea>
                                </div>

                                <center><button type="submit" class="btn btn-primary btn-block mb-2 sendbtn">Update</button></center>
                                <center><button onclick="cancelEdit()" class="btn btn-outline-danger btn-block mb-2 sendbtn">Cancel</button></center>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="row align-items-center mt-4 mb-4">
            <div class="col-md-9 col-sm-8 mx-auto">
                <div class="card-body d-flex flex-column">
                    <%
                        List<Note> noteList = (List<Note>) request.getAttribute("noteList");

                        if (noteList != null && !noteList.isEmpty()) {
                            for (Note n : noteList) {
                    %>
                    <div class="card mb-2">
                        <div class="card-header d-flex align-items-center justify-content-between">
                            <%= n.getTitle()%>
                            <div class="d-flex align-items-center w-50 justify-content-end">
                                <a onclick="showEditForm('<%= n.getId() %>', '<%= n.getTitle() %>', '<%= n.getBody() %>')" class="btn btn-primary delete">
                                    <span class="material-symbols-outlined">
                                        contract_edit
                                    </span>
                                </a>
                                <a href="DeleteNote?id=<%= n.getId() %>" class="btn btn-danger delete">
                                    <span class="material-symbols-outlined">
                                        delete_sweep
                                    </span>
                                </a>
                            </div>
                        </div>
                        <div class="card-body">
                            <blockquote class="blockquote mb-0">
                                <p><%= n.getBody()%></p>
                                <footer class="blockquote-footer text-muted"><small><%= n.getFormattedMsgTime()%></small></footer>
                            </blockquote>
                        </div>
                    </div>
                    <%
                        }
                    } else {
                    %>
                    <div class="card messages_card">
                        <div class="card-body">
                            <div class="row">
                                <h5 class="card-title col">No Notes are Added Yet!</h5>
                            </div>
                            <p class="card-text">Try Creating some Notes!</p>
                        </div>
                    </div>
                    <%
                        }
                    %>
                </div>

            </div>
        </div>
    </section>

    <script>
        var newFormSection = document.getElementById('NewForm');
        var formSection = document.getElementById('UpdateForm');

        function showEditForm(id, title, body) {
            newFormSection.style.display = 'none'
            formSection.style.display = 'block';

            // Scroll to the top of the page
            window.scrollTo(0, 0);

            // Set default values for the modal input fields
            document.getElementById('id').value = id;
            document.getElementById('newTitle').value = title;
            document.getElementById('newBody').value = body;

            // Set focus on the new Title input field
            document.getElementById('newTitle').focus();

            // You may also need to set the ID as a hidden input value if you need it for your update operation
            document.getElementById('noteId').value = id;
        }

        function cancelEdit() {
            newFormSection.style.display = 'block'
            formSection.style.display = 'none';
        }
    </script>


    <% } else { %>
    <p>Please log in to access the chat page.</p>
    <a href="login.jsp">Login</a>
    <% }%>
    <hr class="border border-primary border-2">
</body>
