package nl.windesheim.codeparser.analyzers.observer;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

public class ObserverAnalyzerHappyPathTest {
    private ClassLoader classLoader;
    private ObserverAnalyzerTestHelper helper;

    public ObserverAnalyzerHappyPathTest () {
        classLoader = this.getClass().getClassLoader();
        helper = new ObserverAnalyzerTestHelper();
    }

    @Test
    public void testNumberSystem () throws IOException {
        TestSettings settings = new TestSettings();
        String resourceDir = "observer/numbersystem";

        try {
            settings.codeDir = new File(classLoader.getResource(resourceDir).getPath());

            settings.abstractSubjectName = "Subject";
            settings.abstractSubjectFile = new File(
                    classLoader.getResource(resourceDir + "/Subject.java").getPath()
            );

            settings.abstractObserverName = "Observer";
            settings.abstractObserverFile = new File(
                    classLoader.getResource(resourceDir + "/Observer.java").getPath()
            );

            settings.concreteObservers.put(
                    "BinaryObserver",
                    new File(classLoader.getResource(resourceDir + "/BinaryObserver.java").getPath())
            );
            settings.concreteObservers.put(
                    "HexaObserver",
                    new File(classLoader.getResource(resourceDir + "/HexaObserver.java").getPath())
            );
            settings.concreteObservers.put(
                    "OctalObserver",
                    new File(classLoader.getResource(resourceDir + "/OctalObserver.java").getPath())
            );

            helper.assertValidPattern(settings);
        } catch (NullPointerException ex) {
            fail("Could not initialize test, test resources could not be loaded");
        }
    }

    @Test
    public void testObserverChat () throws IOException {
        TestSettings settings = new TestSettings();
        String resourceDir = "observer/observerChat";

        try {
            settings.codeDir = new File(classLoader.getResource("observer/observerChat").getPath());

            settings.abstractSubjectName = "shared.MyObservable";
            settings.abstractSubjectFile = new File(
                    classLoader.getResource(resourceDir + "/shared/MyObservable.java").getPath()
            );

            settings.abstractObserverName = "shared.MyObserver";
            settings.abstractObserverFile = new File(
                    classLoader.getResource(resourceDir + "/shared/MyObserver.java").getPath()
            );

            settings.concreteSubjects.put(
                    "chat.Group",
                    new File(classLoader.getResource(resourceDir + "/chat/Group.java").getPath())
            );
            settings.concreteSubjects.put(
                    "chat.User",
                    new File(classLoader.getResource(resourceDir + "/chat/User.java").getPath())
            );

            settings.concreteObservers.put(
                    "views.GroupBlock",
                    new File(classLoader.getResource(resourceDir + "/views/GroupBlock.java").getPath())
            );
            settings.concreteObservers.put(
                    "views.GroupContentPanel",
                    new File(classLoader.getResource(resourceDir + "/views/GroupContentPanel.java").getPath())
            );
            settings.concreteObservers.put(
                    "views.GroupOverviewPanel",
                    new File(classLoader.getResource(resourceDir + "/views/GroupOverviewPanel.java").getPath())
            );

            helper.assertValidPattern(settings);
        } catch (NullPointerException ex) {
            fail("Could not initialize test, test resources could not be loaded");
        }
    }
}
