package com.cykor.hub.base.config;

import com.cykor.hub.base.HubApplication;
import com.cykor.hub.base.repos.CongregationRepository;
import com.cykor.hub.base.repos.LoginHistoryRepository;
import com.cykor.hub.base.repos.PrivilegeRepository;
import com.cykor.hub.base.repos.RoleRepository;
import com.cykor.hub.base.repos.UserRepository;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.PostgreSQLContainer;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(
        classes = HubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql({"/data/clearAll.sql", "/data/roleData.sql", "/data/userData.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @ServiceConnection
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16.4");

    static {
        postgreSQLContainer.withReuse(true)
                .start();
    }

    @LocalServerPort
    public int serverPort;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public PrivilegeRepository privilegeRepository;

    @Autowired
    public LoginHistoryRepository loginHistoryRepository;

    @Autowired
    public CongregationRepository congregationRepository;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
    }

    public String roleAdminJwtToken() {
        // user roleAdmin, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJyb2xlQWRtaW4iLCJyb2xlcyI6WyJST0xFX0FETUlOIl0sImlzcyI6ImJvb3RpZnkiLCJpYXQiOjE3MjYyNDQxMDIsImV4cCI6MjIwODk4ODgwMH0." +
                "RxugPyzmx-L6_WjQvTZ-YXWGK4Xr31RVFW-RDGfpKBGX6B9OpQ8AwPedimxsKAHa-CWuRezd3JgGLR_cAaCxYQ";
    }

    public String roleUserJwtToken() {
        // user roleUser, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJyb2xlVXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpc3MiOiJib290aWZ5IiwiaWF0IjoxNzI2MjQ0MTAyLCJleHAiOjIyMDg5ODg4MDB9." +
                "PDvvIR47JyxFToo867bNYyO8xkaautfS0zbliUnpdOdZMJc9qFqPF6ZQZ1WO_lPEFCRBkHMyYPwg4dF6K06evw";
    }

}
