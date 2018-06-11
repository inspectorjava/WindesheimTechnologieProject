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

        settings.receivers.put(
                "Light",
                new File(classLoader.getResource("command/switch/Light.java").getPath())
        );

        helper.testCommandPattern(settings);
    }

//    TODO: Enable this test when we support Lambda expressions in the command pattern.
//    @Test
//    public void testSwitchJava8Command() throws IOException {
//        TestSettings settings = new TestSettings();
//        settings.codeDir = new File(classLoader.getResource("command/switchJava8").getPath());
//
//        settings.interfaceName = "Command";
//        settings.interfaceFile = new File(classLoader.getResource("command/switchJava8/Command.java").getPath());
//
//        helper.testCommandPattern(settings);
//    }

    @Test
    public void testDemoCommand() throws IOException {
        helper.testInvalidCommandPattern(new File(classLoader.getResource("command/commandDemo").getPath()));
    }

    @Test
    public void testStrategyPattern() throws IOException {
        helper.testInvalidCommandPattern(new File(classLoader.getResource("strategy/compression").getPath()));
    }

    @Test
    public void testStrategyWiki() throws IOException {
        helper.testInvalidCommandPattern(new File(classLoader.getResource("strategy/wiki").getPath()));
    }

    @Test
    public void testObserverNumberSystem() throws IOException {
        helper.testInvalidCommandPattern(new File(classLoader.getResource("observer/numbersystem").getPath()));
    }

    @Test
    public void testChainOfResponsibiltyAtm() throws IOException {
        helper.testInvalidCommandPattern(new File(classLoader.getResource("chainOfResponsibility/atm").getPath()));
    }

}
