package com.example.task_manager.repository_tests;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.TeamRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeamRepository teamRepository;

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
