<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CNT 4714 - Summer 2024 - Accountant User App</title>
    <style>
        /* Style for the container to center it */
        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            flex-direction: column;
            color: lime; /* Text color */
        }

        /* General styles for headings and text */
        body {
            background-color: black; /* Background color */
            text-align: center;
            color: lime; /* Text color */
            font-family: "Times New Roman", Times, serif;
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

        .radio-group {
            text-align: left; /* Align radio buttons to the left */
            margin-bottom: 10px;
        }

        .query-form .radio-group label {
            display: block;
            margin-bottom: 5px;
            font-size: 14pt;
        }

        .query-form button {
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: 48%; /* Ensure buttons are of equal width */
            margin: 5px;
        }

        .query-form button:hover {
            background-color: #45a049;
        }

        .button-group {
            display: flex;
            justify-content: space-between;
            gap: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="main-title">Welcome to the Summer 2024 Project 3 Enterprise System</h1>
        <h2 class="section-title">A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
        <hr>
        <p class="info-message">You are connected to the Project 3 Enterprise System database as an <span style="color: red;">accountant-level</span> user.</p>
        <p class="info-message">Please select the operation you would like to perform from the list below.</p>

    <form class="query-form" method="post" action="AccountantApp">
  <div class="radio-group">
      <label><input type="radio" name="option" value="Get_The_Maximum_Status_Of_All_Suppliers"> <span style="color: blue;">Get the maximum status value of all suppliers</span> (returns a maximum value)</label>
      <label><input type="radio" name="option" value="Get_The_Sum_Of_All_Parts_Weights"> <span style="color: blue;">Get the total weight of all parts</span> (Returns a sum)</label>
      <label><input type="radio" name="option" value="Get_The_Total_Number_Of_Shipments"> <span style="color: blue;">Get the total number of shipments</span> (Returns the current number of shipments in total)</label>
      <label><input type="radio" name="option" value="Get_The_Name_Of_The_Job_With_The_Most_Workers"> <span style="color: blue;">Get the name and number of workers of the job with the most workers</span> (Returns two values)</label>
      <label><input type="radio" name="option" value="List_The_Name_And_Status_Of_All_Suppliers"> <span style="color: blue;">List the name and status of every supplier</span> (Returns a list of supplier names with status)</label>
  </div>
  <div class="button-group">
    <button type="submit">Execute Command</button>
    <button type="button" onclick="document.getElementById('executionResults').innerHTML=''">Clear Results</button>
  </div>
</form>


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
