package nl.windesheim.codeparser.analyzers.observer;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ObserverAnalyzerVariantsTest {
    private ClassLoader classLoader;
    private ObserverAnalyzerTestHelper helper;

    public ObserverAnalyzerVariantsTest () {
        classLoader = this.getClass().getClassLoader();
        helper = new ObserverAnalyzerTestHelper();
    }

    @Test
    public void testNewsAgency () throws IOException {
        TestSettings settings = new TestSettings();
        String resourceDir = "observer/newsAgency";

        try {
            settings.codeDir = new File(classLoader.getResource(resourceDir).getPath());

            settings.abstractSubjectName = "NewsAgency";
            settings.abstractSubjectFile = new File(
                    classLoader.getResource(resourceDir + "/NewsAgency.java").getPath()
            );

            settings.abstractObserverName = "Channel";
            settings.abstractObserverFile = new File(
                    classLoader.getResource(resourceDir + "/Channel.java").getPath()
            );

            settings.concreteObservers.put(
                    "NewsChannel",
                    new File(classLoader.getResource(resourceDir + "/NewsChannel.java").getPath())
            );

            helper.assertValidPattern(settings);
        } catch (NullPointerException ex) {
            fail("Could not initialize test, test resources could not be loaded");
        }
    }

    @Test
    public void testSubjectInterface () throws IOException {
        TestSettings settings = new TestSettings();
        String resourceDir = "observer/subjectInterface";

        try {
            settings.codeDir = new File(classLoader.getResource(resourceDir).getPath());

            settings.abstractSubjectName = "MyTopic";
            settings.abstractSubjectFile = new File(
                    classLoader.getResource(resourceDir + "/MyTopic.java").getPath()
            );

            settings.abstractObserverName = "Observer";
            settings.abstractObserverFile = new File(
                    classLoader.getResource(resourceDir + "/Observer.java").getPath()
            );

            settings.concreteObservers.put(
                    "MyTopicSubscriber",
                    new File(classLoader.getResource(resourceDir + "/MyTopicSubscriber.java").getPath())
            );

            helper.assertValidPattern(settings);
        } catch (NullPointerException ex) {
            fail("Could not initialize test, test resources could not be loaded");
        }
    }
}
