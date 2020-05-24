package com.qa.keyword.tests;
import com.qa.keyword.engine.ExecutionEngine;
import org.testng.annotations.Test;
import java.io.IOException;

public class LoginTest1 {

    public String excelFileName = System.getProperty("user.dir")+"\\src\\test\\resources\\config\\facebook_scenarios.xlsx";

    @Test
    public void runLoginTestScenario() throws IOException, ClassNotFoundException {
        ExecutionEngine eng = new ExecutionEngine();
        eng.startExecution(excelFileName,"login");
    }

    @Test
    public void runValidateNavBarScenario() throws IOException, ClassNotFoundException {
        ExecutionEngine eng = new ExecutionEngine();
        eng.startExecution(excelFileName,"checkNavBar");
    }

    @Test
    public void runAllScenarios() throws IOException, ClassNotFoundException {
        ExecutionEngine eng = new ExecutionEngine();
        eng.startExecution(excelFileName,null);
    }

}
