resources {
    serverName = 'localhost'
    serverPort = '4000'
}

environments {
    dev {
        db_url = "jdbc:mysql://localhost/aclRestUser"
    }
}