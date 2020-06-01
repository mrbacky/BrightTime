/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.bll.util;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.TaskBase;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskEntry;
import brighttime.be.User;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rados
 */
public class TaskDurationCalculatorTest {

    public TaskDurationCalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of calculateTaskDuration method, of class TaskDurationCalculator.
     */
    @Test
    public void testCalculateTaskDuration() {

        System.out.println("calculateTaskDuration");
        List<TaskEntry> entryList = new ArrayList<>();
        User user = new User(1, "John", "Colins", "j@gmail.com", User.UserType.STANDARD);
        Client client = new Client(1, "Nike", 1000);
        Project project = new Project(1, "Nike Air Max 7", client, 0);
        TaskConcrete1 task = new TaskConcrete1(14, "update material", project, TaskBase.Billability.BILLABLE, entryList, LocalDateTime.now(), user);
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T09:00:00"), LocalDateTime.parse("2020-05-05T09:30:00")));
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T15:00:00"), LocalDateTime.parse("2020-05-05T16:00:00")));
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T15:00:00"), LocalDateTime.parse("2020-05-05T16:00:00")));
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T18:00:00"), LocalDateTime.parse("2020-05-05T20:00:00")));

        TaskDurationCalculator instance = new TaskDurationCalculator();
        Duration expResult = Duration.ofSeconds(16200);
        Duration result = instance.calculateTaskDuration(entryList);
        assertEquals(expResult, result);
    }

    @Test
    public void testCalculateTaskDuration2() {
        System.out.println("calculateTaskDuration");
        List<TaskEntry> entryList = new ArrayList<>();
        User user = new User(2, "Mike", "Wazowski", "m@gmail.com", User.UserType.STANDARD);
        Client client = new Client(1, "Nike", 1000);
        Project project = new Project(1, "Nike Air Max 7", client, 0);
        TaskConcrete1 task = new TaskConcrete1(15, "update color", project, TaskBase.Billability.BILLABLE, entryList, LocalDateTime.now(), user);
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T09:00:00"), LocalDateTime.parse("2020-05-05T11:30:00")));
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T15:00:00"), LocalDateTime.parse("2020-05-05T15:30:00")));
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T15:00:00"), LocalDateTime.parse("2020-05-05T16:00:00")));
        entryList.add(new TaskEntry(task, LocalDateTime.parse("2020-05-05T18:00:00"), LocalDateTime.parse("2020-05-05T22:00:00")));

        TaskDurationCalculator instance = new TaskDurationCalculator();
        Duration expResult = Duration.ofSeconds(28800);
        Duration result = instance.calculateTaskDuration(entryList);
        assertEquals(expResult, result);
    }

}
