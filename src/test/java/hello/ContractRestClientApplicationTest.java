package hello;

import org.assertj.core.api.BDDAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractRestClientApplicationTest {

    /*
    @Rule
    public StubRunnerRule stubRunnerRule = new StubRunnerRule()
            .downloadStub("com.example", "contract-server", "0.0.1-SNAPSHOT", "stubs")
            .withPort(8100)
            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);
    */
    @RegisterExtension
    public StubRunnerExtension stubRunnerExtension = new StubRunnerExtension()
            .downloadStub("com.example:contract-server")
            .repoRoot("stubs://file://seanl/IdeaProjects/spring-contract-base-server/build/stubs/")
            .stubsMode(StubRunnerProperties.StubsMode.REMOTE);

    @Test
    public void get_person_from_service_contract() {
        // given:
        RestTemplate restTemplate = new RestTemplate();

        // when:
        ResponseEntity<Person> personResponseEntity = restTemplate.getForEntity("http://localhost:8100/person/1", Person.class);

        // then:
        BDDAssertions.then(personResponseEntity.getStatusCodeValue()).isEqualTo(200);
        BDDAssertions.then(personResponseEntity.getBody().getId()).isEqualTo(1l);
        BDDAssertions.then(personResponseEntity.getBody().getName()).isEqualTo("foo");
        BDDAssertions.then(personResponseEntity.getBody().getSurname()).isEqualTo("bee");

    }
}