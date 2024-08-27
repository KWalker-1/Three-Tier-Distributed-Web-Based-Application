<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CNT 4714 - Summer 2024</title>
    <style>
        /* Style for the container to center it */
        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            min-height: 100vh;
            color: lime; /* Text color */
        }

        /* General styles for headings and text */
        body {
            background-color: black; /* Background color */
            text-align: center;
            color: lime; /* Text color */
            font-family: "Times New Roman", Times, serif;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }

        h1, h2, h3, p {
            margin: 10px 0;
        }

        hr {
            border: 0;
            height: 1px;
            background: #ccc;
            margin: 20px 0;
            width: 100%;
        }

        .main-title {
            font-size: 24pt;
            color: yellow;
        }

        .section-title {
            font-size: 24pt;
            color: red;
        }

        .info-message {
            font-size: 18pt;
            color: white;
        }

        /* Style for the form */
        .query-form {
            display: flex;
            flex-direction: column;
            width: 80%;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #f9f9f9;
            color: black; /* Text color inside form */
        }

        .query-form textarea {
            width: 100%;
            height: 150px;
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14pt;
        }

        .button-group {
            display: flex;
            justify-content: space-between;
            gap: 10px;
        }

        .query-form button {
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: 32%; /* Ensure buttons are of equal width */
        }

        .query-form button:hover {
            background-color: #45a049;
        }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
        }

        table, th, td {
            border: 1px solid #ccc;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        /* Make the execution results scrollable */
        #executionResults {
            max-height: 400px;
            overflow-y: auto;
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #f9f9f9;
            color: black; /* Text color inside results */
        }
    </style>
</head>
<body>

    <div class="container">
        <h1 class="main-title">Welcome to the Summer 2024 Project 3 Enterprise System</h1>
        <h2 class="section-title">A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
        <hr>

        <p class="info-message">You are connected to the Project 3 Enterprise System database as a <span style="color: red;">root-level</span> user.</p>
        <p class="info-message">Please enter any SQL query or update command in the box below.</p>

        <form class="query-form" action="rootExecuteQueryServlet" method="post">
            <textarea name="sqlQuery" id="sqlQuery" placeholder="Enter SQL query or update command here..."></textarea>
            <div class="button-group">
                <button type="submit">Execute Command</button>
                <button type="reset">Reset Form</button>
                <button type="button" onclick="document.getElementById('executionResults').innerHTML=''">Clear Results</button>
            </div>
        </form>

        <p class="info-message">All execution results will appear below this line</p>
        <hr>

        <div id="executionResults" class="info-message">
            <%
                String queryResult = (String) request.getAttribute("queryResult");
                if (queryResult != null && !queryResult.isEmpty()) {
                    out.println(queryResult);
                } else {
                    out.println("No results to display.");
                }
            %>
        </div>
    </div>

</body>
</html>
