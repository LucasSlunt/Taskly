package com.example.task_manager.repository_tests;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.test_helpers.RepositoryTestHelper;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TeamRepositoryTest extends RepositoryTestHelper{


    @Test
    void testFindByTeamLead_AccountId() {
        TeamMember teamLead = new TeamMember("UFO", "rock_bottom@music.com", "rock_bottom");
        entityManager.persist(teamLead);

        Team team = new Team("Rock Music", teamLead);
        entityManager.persist(team);
        entityManager.flush();

        List<Team> result = teamRepository.findByTeamLead_AccountId(teamLead.getAccountId());

        assertEquals(1, result.size());
        assertEquals(team.getTeamId(), result.get(0).getTeamId());
    }
}
