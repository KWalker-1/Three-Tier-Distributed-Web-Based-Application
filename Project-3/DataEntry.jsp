<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CNT 4714 - Summer 2024</title>
  <style>
    /* Style for the container */
    .container {
      display: flex;
      flex-direction: column;
      align-items: center;
      color: lime; /* Text color */
      padding: 20px;
    }

    /* General styles for headings and text */
    body {
      background-color: black; /* Background color */
      text-align: center;
      color: lime; /* Text color */
      font-family: "Times New Roman", Times, serif;
      margin: 0;
    }

    h1, h2, p {
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
      .info-DBresponse {
      font-size: 24pt;
      color: greenyellow;
    }

    /* Style for the form */
    .data-entry-form {
      display: flex;
      flex-direction: column;
      width: 80%;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 10px;
      background-color: #f9f9f9;
      color: black; /* Text color inside form */
      margin-bottom: 20px;
    }

    /* Style for the table */
    table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 20px;
    }

    table, th, td {
      border: 1px solid #ccc;
    }

    th, td {
      padding: 10px;
      text-align: center;
    }

    input[type="text"] {
      width: 100%;
      padding: 5px;
      box-sizing: border-box;
    }

    .button-group {
      display: flex;
      justify-content: space-between;
      gap: 10px;
    }

    .data-entry-form button {
      padding: 10px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      width: 48%; /* Ensure buttons are of equal width */
    }

    .data-entry-form button:hover {
      background-color: #45a049;
    }
  </style>
  <script>
    // Function to clear execution results
    function clearResults() {
      document.getElementById('executionResults').innerHTML = 'Execution Results:';
    }
  </script>
</head>
<body>

  <div class="container">
    <h1 class="main-title">Welcome to the Summer 2024 Project 3 Enterprise System</h1>
    <h2 class="section-title">Data Entry Application</h2>
    <hr>

    <p class="info-message">You are connected to the Project 3 Enterprise System database as a <span style="color: red;">data-entry-level</span> user.</p>
    <p class="info-message">Please enter any SQL query or update command in the box below.</p>

    <!--Suppliers Record Insert-->
    <form class="data-entry-form" action="SuppliersInsertServlet" method="post" onreset="clearResults()">
      <p>Suppliers Record Insert</p>
      <table>
        <thead>
          <tr>
            <th>snum</th>
            <th>sname</th>
            <th>status</th>
            <th>city</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><input type="text" name="snum" placeholder="Supplier Number"></td>
            <td><input type="text" name="sname" placeholder="Supplier Name"></td>
            <td><input type="text" name="status" placeholder="Status"></td>
            <td><input type="text" name="city" placeholder="City"></td>
          </tr>
        </tbody>
      </table>

      <div class="button-group">
        <button type="submit">Enter Supplier Record Into Database</button>
        <button type="reset">Clear Data and Results</button>
      </div>
    </form>
      
    <!--Parts Record Insert-->
    <form class="data-entry-form" action="PartsInsertServlet" method="post" onreset="clearResults()">
      <p>Parts Record Insert</p>
      <table>
        <thead>
          <tr>
            <th>pnum</th>
            <th>pname</th>
            <th>color</th>
            <th>weight</th>
            <th>city</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><input type="text" name="pnum" placeholder="Part Number"></td>
            <td><input type="text" name="pname" placeholder="Part Name"></td>
            <td><input type="text" name="color" placeholder="Color"></td>
            <td><input type="text" name="weight" placeholder="Weight"></td>
            <td><input type="text" name="city" placeholder="City"></td>
          </tr>
        </tbody>
      </table>

      <div class="button-group">
        <button type="submit">Enter Part Record Into Database</button>
        <button type="reset">Clear Data and Results</button>
      </div>
    </form>
      
    <!--Jobs Record Insert-->
    <form class="data-entry-form" action="JobsInsertServlet" method="post" onreset="clearResults()">
      <p>Jobs Record Insert</p>
      <table>
        <thead>
          <tr>
            <th>jnum</th>
            <th>jname</th>
            <th>numworkers</th>
            <th>city</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><input type="text" name="jnum" placeholder="Job Number"></td>
            <td><input type="text" name="jname" placeholder="Job Name"></td>
            <td><input type="text" name="numworkers" placeholder="# of Workers"></td>
            <td><input type="text" name="city" placeholder="City"></td>
          </tr>
        </tbody>
      </table>

      <div class="button-group">
        <button type="submit">Enter Job Record Into Database</button>
        <button type="reset">Clear Data and Results</button>
      </div>
    </form>
      
    <!--Shipments Record Insert-->
    <form class="data-entry-form" action="ShipmentsInsertServlet" method="post" onreset="clearResults()">
      <p>Shipments Record Insert</p>
      <table>
        <thead>
          <tr>
            <th>snum</th>
            <th>pnum</th>
            <th>jnum</th>
            <th>quantity</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><input type="text" name="snum" placeholder="Supplier Number"></td>
            <td><input type="text" name="pnum" placeholder="Part Number"></td>
            <td><input type="text" name="jnum" placeholder="Job Number"></td>
            <td><input type="text" name="quantity" placeholder="Quantity"></td>
          </tr>
        </tbody>
      </table>

      <div class="button-group">
        <button type="submit">Enter Shipment Record Into Database</button>
        <button type="reset">Clear Data and Results</button>
      </div>
    </form>  
      
    <p class="info-message">All execution results will appear below this line</p>
    <hr>
    <div id="executionResults" class="info-DBresponse">Execution Results:
      <%= request.getAttribute("executionResults") != null ? request.getAttribute("executionResults") : "" %>
    </div>
  </div>

</body>
</html>
