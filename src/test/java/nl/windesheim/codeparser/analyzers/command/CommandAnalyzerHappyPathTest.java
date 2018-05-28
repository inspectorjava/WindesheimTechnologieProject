package nl.windesheim.codeparser.analyzers.command;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CommandAnalyzerHappyPathTest {

    private ClassLoader classLoader;
    private CommandAnalyzerTestHelper helper;

    public CommandAnalyzerHappyPathTest() {
        classLoader = this.getClass().getClassLoader();
        helper = new CommandAnalyzerTestHelper();
    }

    @Test
    public void testSwitchCommand() throws IOException {
        TestSettings settings = new TestSettings();
//        settings.codeDir = new File(classLoader.getResource("command/switch").getPath());
//
//        settings.contextClassName = "PressSwitch";
//        settings.contextfile = new File(classLoader.getResource("command/switch/PressSwitch.java").getPath());
//
//        settings.interfaceName = "Command";
//        settings.interfaceFile = new File(classLoader.getResource("command/switch/Command.java").getPath());

//        settings.commands.put(
//                "NormalStrategy",
//                new File(classLoader.getResource("strategy/wiki/NormalStrategy.java").getPath())
//        );
//        settings.commands.put(
//                "HappyHourStrategy",
//                new File(classLoader.getResource("strategy/wiki/HappyHourStrategy.java").getPath())
//        );

        helper.testCommandPattern(settings);
    }

}
