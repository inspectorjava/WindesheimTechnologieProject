package nl.windesheim.reporting;

import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.codeparser.patterns.ChainOfResponsibility;
import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.codeparser.patterns.CompositePattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;
import nl.windesheim.codeparser.patterns.Singleton;
import nl.windesheim.codeparser.patterns.Strategy;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeReportDesignPatternMapperTest {
    @Test
    public void getSingletonBuilder() throws Exception {
        Assert.assertNotNull(Report.getMapper().getBuilder(new Singleton()));
    }

    @Test
    public void getStrategyBuilder() throws Exception {
        Assert.assertNotNull(Report.getMapper().getBuilder(new Strategy()));
    }

    @Test
    public void getChainOfResponsibilityBuilder() throws Exception {
        Assert.assertNotNull(Report.getMapper().getBuilder(new ChainOfResponsibility()));
    }

    @Test
    public void getCommandBuilder() throws Exception {
        Assert.assertNotNull(Report.getMapper().getBuilder(new Command()));
    }

    @Test
    public void getCompositeBuilder() throws Exception {
        Assert.assertNotNull(Report.getMapper().getBuilder(new CompositePattern(null, null, null)));
    }

    @Test
    public void getAbstractFactoryBuilder() throws Exception {
        Assert.assertNotNull(Report.getMapper().getBuilder(new AbstractFactory()));
    }

    @Test
    public void getObserverBuilder() throws Exception {
        Assert.assertNotNull(Report.getMapper().getBuilder(new ObserverPattern()));
    }
}