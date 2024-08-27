# Client creation and permission
#
# IMPORTANT NOTE: Run all database creation scripts before running this script.
#
# Delete the client1, client2,  users created for Project 2
#  NOTE: If any of these users are currently connected to the MySQL server, terminate their connections prior to running this script.
#  If any of these clients are currently connected, the drop user command will not take effect until the that user's connection is terminated.
#revoke all privileges, grant option from "client1";
drop user if exists "client1";
#revoke all privileges, grant option from "client2";
drop user if exists "client2";
#revoke all privileges, grant option from "project2app";
drop user if exists "project2app";
#revoke all privileges, grant option from "theaccountant";
drop user if exists "theaccountant";

# Create the client users for Project 3
create user if not exists "client" identified with sha256_password by "client";
create user if not exists "systemapp" identified with sha256_password by "systemapp";
create user if not exists "dataentryuser" identified with sha256_password by "dataentryuser";
create user if not exists "theaccountant" identified with sha256_password by "theaccountant";


# Assign privileges to the client users for Project 3
grant select on project3.* to client;
grant select, insert, update on project3.* to dataentryuser;
grant execute  on project3.* to theaccountant;
grant all on credentialsDB.* to systemapp;

