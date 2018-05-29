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
        settings.codeDir = new File(classLoader.getResource("command/switch").getPath());

        settings.contextClassName = "PressSwitch";
        settings.contextfile = new File(classLoader.getResource("command/switch/PressSwitch.java").getPath());

        settings.interfaceName = "Command";
        settings.interfaceFile = new File(classLoader.getResource("command/switch/Command.java").getPath());

        settings.commands.put(
                "FlipUpCommand",
                new File(classLoader.getResource("command/switch/FlipUpCommand.java").getPath())
        );
        settings.commands.put(
                "FlipDownCommand",
                new File(classLoader.getResource("command/switch/FlipDownCommand.java").getPath())
        );

        helper.testCommandPattern(settings);
    }

    @Test
    public void testSwitchJava8Command() throws IOException {
        TestSettings settings = new TestSettings();
        settings.codeDir = new File(classLoader.getResource("command/switchJava8").getPath());

        settings.contextClassName = "Main";
        settings.contextfile = new File(classLoader.getResource("command/switchJava8/Main.java").getPath());

        settings.interfaceName = "Command";
        settings.interfaceFile = new File(classLoader.getResource("command/switchJava8/Command.java").getPath());

        helper.testCommandPattern(settings);
    }

    @Test
    public void testDemoCommand() throws IOException {
        TestSettings settings = new TestSettings();
        settings.codeDir = new File(classLoader.getResource("command/commandDemo").getPath());

        settings.contextClassName = "CommandDemo";
        settings.contextfile = new File(classLoader.getResource("command/commandDemo/CommandDemo.java").getPath());

        settings.interfaceName = "Command";
        settings.interfaceFile = new File(classLoader.getResource("command/commandDemo/Command.java").getPath());

        settings.commands.put(
                "DomesticEngineer",
                new File(classLoader.getResource("command/commandDemo/DomesticEngineer.java").getPath())
        );
        settings.commands.put(
                "Politician",
                new File(classLoader.getResource("command/commandDemo/Politician.java").getPath())
        );
        settings.commands.put(
                "Programmer",
                new File(classLoader.getResource("command/commandDemo/Programmer.java").getPath())
        );

        helper.testCommandPattern(settings);
    }

}
