package com.fitfood;

import com.fitfood.config.AppConfig;
import com.fitfood.ui.FitFoodDashboardUI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        SwingUtilities.invokeLater(() -> {
            FitFoodDashboardUI ui = context.getBean(FitFoodDashboardUI.class);
            ui.setVisible(true);
        });
    }
}
